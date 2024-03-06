package cool.klass.generator.dto;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.PropertyModifier;
import cool.klass.model.meta.domain.api.visitor.PrimitiveToJavaTypeVisitor;
import org.eclipse.collections.api.list.ImmutableList;

public class DataTransferObjectsGenerator
{
    @Nonnull
    private final DomainModel domainModel;
    private final Instant     now;

    public DataTransferObjectsGenerator(@Nonnull DomainModel domainModel, Instant now)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.now = now;
    }

    public void writeDataTransferObjectFiles(@Nonnull Path outputPath) throws IOException
    {
        for (Enumeration enumeration : this.domainModel.getEnumerations())
        {
            Path dtoOutputPath = this.getDtoOutputPath(outputPath, enumeration);
            this.printStringToFile(dtoOutputPath, this.getEnumerationSourceCode(enumeration));
        }

        for (Klass klass : this.domainModel.getKlasses())
        {
            Path dtoOutputPath = this.getDtoOutputPath(outputPath, klass);
            this.printStringToFile(dtoOutputPath, this.getClassSourceCode(klass));
        }
    }

    @Nonnull
    public Path getDtoOutputPath(
            @Nonnull Path outputPath,
            @Nonnull PackageableElement packageableElement)
    {
        String packageRelativePath = packageableElement.getPackageName()
                .replaceAll("\\.", "/");
        Path dtoDirectory = outputPath
                .resolve(packageRelativePath)
                .resolve("dto");
        dtoDirectory.toFile().mkdirs();
        String fileName = packageableElement.getName() + "DTO.java";
        return dtoDirectory.resolve(fileName);
    }

    private void printStringToFile(@Nonnull Path path, String contents) throws FileNotFoundException
    {
        try (PrintStream printStream = new PrintStream(new FileOutputStream(path.toFile())))
        {
            printStream.print(contents);
        }
    }

    @Nonnull
    private String getEnumerationSourceCode(@Nonnull Enumeration enumeration)
    {
        String packageName        = enumeration.getPackageName() + ".dto";
        String literalsSourceCode = enumeration.getEnumerationLiterals().collect(this::getLiteral).makeString("");

        // @formatter:off
        //language=JAVA
        return ""
                + "package " + packageName + ";\n"
                + "\n"
                + "import javax.annotation.Generated;\n"
                + "\n"
                + "import com.fasterxml.jackson.annotation.JsonProperty;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.dto.DataTransferObjectsGenerator}\n"
                + " */\n"
                + "@Generated(\n"
                + "        value = \"cool.klass.generator.dto.DataTransferObjectsGenerator\",\n"
                + "        date = \"" + this.now + "\")\n"
                + "public enum " + enumeration.getName() + "DTO\n"
                + "{\n"
                + literalsSourceCode
                + "}\n";
        // @formatter:on
    }

    @Nonnull
    public String getClassSourceCode(@Nonnull Klass klass)
    {
        String packageName = klass.getPackageName() + ".dto";

        ImmutableList<DataTypeProperty> dataTypeProperties = klass.getDataTypeProperties();
        String dataFieldsSourceCode = dataTypeProperties.collect(this::getDataField).makeString("")
                + (dataTypeProperties.isEmpty() ? "" : "\n");

        String dataGettersSettersSourceCode = dataTypeProperties.collect(this::getDataGetterSetter).makeString("\n");

        ImmutableList<AssociationEnd> associationEnds = klass.getAssociationEnds();
        String referenceFieldsSourceCode = associationEnds.collect(this::getReferenceField).makeString("")
                + (associationEnds.isEmpty() ? "" : "\n");

        String referenceGettersSettersSourceCode = associationEnds.collect(this::getReferenceGetterSetter).makeString(
                "\n");

        boolean hasConstraints = dataTypeProperties
                .asLazy()
                .reject(DataTypeProperty::isKey)
                .reject(DataTypeProperty::isTemporal)
                .select(DataTypeProperty::isRequired)
                .notEmpty();
        String constraintImports = hasConstraints
                ? "import javax.validation.constraints.*;\n"
                : "";

        // @formatter:off
        //language=JAVA
        String sourceCode = ""
                + "package " + packageName + ";\n"
                + "\n"
                + "import java.time.*;\n"
                + "import java.util.*;\n"
                + "\n"
                + "import javax.annotation.*;\n"
                + constraintImports
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.dto.DataTransferObjectsGenerator}\n"
                + " */\n"
                + "@Generated(\n"
                + "        value = \"cool.klass.generator.dto.DataTransferObjectsGenerator\",\n"
                + "        date = \"" + this.now + "\")\n"
                + "public class " + klass.getName() + "DTO\n"
                + "{\n"
                + dataFieldsSourceCode
                + referenceFieldsSourceCode
                + dataGettersSettersSourceCode
                + referenceGettersSettersSourceCode
                + "}\n";
        // @formatter:on

        return sourceCode;
    }

    private String getLiteral(EnumerationLiteral enumerationLiteral)
    {
        String prettyName = enumerationLiteral.getPrettyName();
        String name       = enumerationLiteral.getName();
        String line1      = prettyName == null ? "" : "    @JsonProperty(\"" + prettyName + "\")\n";
        String line2      = "    " + name + ",\n";
        return line1 + line2;
    }

    private String getDataGetterSetter(DataTypeProperty dataTypeProperty)
    {
        String type          = this.getType(dataTypeProperty.getType());
        String name          = dataTypeProperty.getName();
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
        return this.getGetterSetter(type, name, uppercaseName);
    }

    private String getReferenceGetterSetter(AssociationEnd associationEnd)
    {
        String type          = this.getType(associationEnd.getType(), associationEnd.getMultiplicity());
        String name          = associationEnd.getName();
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
        return this.getGetterSetter(type, name, uppercaseName);
    }

    @Nonnull
    private String getGetterSetter(String type, String name, String uppercaseName)
    {
        //language=JAVA
        return ""
                + "    public " + type + " get" + uppercaseName + "()\n"
                + "    {\n"
                + "        return " + name + ";\n"
                + "    }\n"
                + "\n"
                + "    public void set" + uppercaseName + "(" + type + " " + name + ")\n"
                + "    {\n"
                + "        this." + name + " = " + name + ";\n"
                + "    }\n";
    }

    @Nonnull
    private String getType(DataType dataType)
    {
        if (dataType instanceof Enumeration)
        {
            return ((Enumeration) dataType).getName() + "DTO";
        }
        if (dataType instanceof PrimitiveType)
        {
            return PrimitiveToJavaTypeVisitor.getJavaType((PrimitiveType) dataType);
        }
        throw new AssertionError();
    }

    @Nonnull
    private String getType(Klass klass, Multiplicity multiplicity)
    {
        String toOneType = klass.getName() + "DTO";
        if (multiplicity.isToOne())
        {
            return toOneType;
        }

        return "List<" + toOneType + ">";
    }

    private String getDataField(@Nonnull DataTypeProperty dataTypeProperty)
    {
        String   annotation = this.getAnnotation(dataTypeProperty);
        String   type       = this.getType(dataTypeProperty.getType());
        return String.format("%s    private %s %s;\n", annotation, type, dataTypeProperty.getName());
    }

    @Nonnull
    private String getAnnotation(DataTypeProperty dataTypeProperty)
    {
        return isNullable(dataTypeProperty) ? "" : "    @NotNull\n";
    }

    private boolean isNullable(DataTypeProperty dataTypeProperty)
    {
        if (dataTypeProperty.isTemporal())
        {
            return true;
        }
        ImmutableList<PropertyModifier> propertyModifiers = dataTypeProperty.getPropertyModifiers();
        return dataTypeProperty.isOptional() || dataTypeProperty.isKey();
    }

    private String getReferenceField(AssociationEnd associationEnd)
    {
        Multiplicity multiplicity = associationEnd.getMultiplicity();

        // TODO: NotNull shouldn't apply if the ONE_TO_ONE is a version association end.
        String annotation = "";
        // String annotation = multiplicity.isToMany() || multiplicity == Multiplicity.ONE_TO_ONE ? "    @NotNull\n" : "";
        String type       = this.getType(associationEnd.getType(), multiplicity);
        return String.format("%s    private %s %s;\n", annotation, type, associationEnd.getName());
    }
}
