package cool.klass.generator.meta.constants;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.PropertyModifier;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.collections.api.list.ImmutableList;

public class JavaConstantsMetaModelGenerator
{
    @Nonnull
    private final DomainModel domainModel;
    @Nonnull
    private final String      applicationName;
    @Nonnull
    private final String      rootPackageName;
    @Nonnull
    private final Instant     now;

    public JavaConstantsMetaModelGenerator(
            @Nonnull DomainModel domainModel,
            @Nonnull String applicationName,
            @Nonnull String rootPackageName,
            Instant now)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.applicationName = Objects.requireNonNull(applicationName);
        this.rootPackageName = Objects.requireNonNull(rootPackageName);
        this.now = Objects.requireNonNull(now);
    }

    public void writeJavaConstantsMetaModelFiles(@Nonnull Path outputPath) throws IOException
    {
        Path domainModelOutputPath = this.getOutputPath(outputPath);
        this.printStringToFile(domainModelOutputPath, this.getDomainModelSourceCode());

        for (Enumeration enumeration : this.domainModel.getEnumerations())
        {
            Path   path                  = this.getOutputPath(outputPath, enumeration);
            String enumerationSourceCode = this.getEnumerationSourceCode(enumeration);
            this.printStringToFile(path, enumerationSourceCode);
        }

        for (Klass klass : this.domainModel.getKlasses())
        {
            Path   path            = this.getOutputPath(outputPath, klass);
            String classSourceCode = this.getClassSourceCode(klass);
            this.printStringToFile(path, classSourceCode);
        }

        for (Association association : this.domainModel.getAssociations())
        {
            Path   path                  = this.getOutputPath(outputPath, association);
            String associationSourceCode = this.getAssociationSourceCode(association);
            this.printStringToFile(path, associationSourceCode);
        }

        for (Projection projection : this.domainModel.getProjections())
        {
            Path   path                 = this.getOutputPath(outputPath, projection);
            String projectionSourceCode = this.getProjectionSourceCode(projection);
            this.printStringToFile(path, projectionSourceCode);
        }
    }

    private String getDomainModelSourceCode()
    {
        String imports = this.domainModel
                .getTopLevelElements()
                .collect(PackageableElement::getPackageName)
                .distinct()
                .toSortedList()
                .collect(packageName -> "import " + packageName + ".*;\n")
                .makeString("");

        //language=JAVA
        String sourceCode = ""
                + "package " + this.rootPackageName + ".meta.constants;\n"
                + "\n"
                + "import javax.annotation.*;\n"
                + "\n"
                + "import cool.klass.model.meta.domain.api.*;\n"
                + "import cool.klass.model.meta.domain.api.order.*;\n"
                + "import cool.klass.model.meta.domain.api.projection.*;\n"
                + "import cool.klass.model.meta.domain.api.service.*;\n"
                + "import org.eclipse.collections.api.list.ImmutableList;\n"
                + "\n"
                + imports
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.meta.constants.JavaConstantsMetaModelGenerator}\n"
                + " */\n"
                + "@Generated(\n"
                + "        value = \"cool.klass.generator.meta.constants.JavaConstantsMetaModelGenerator\",\n"
                + "        date = \"" + this.now + "\")\n"
                + "public enum " + this.applicationName + "DomainModel implements DomainModel\n"
                + "{\n"
                + "    INSTANCE;\n"
                + "    \n"
                + this.getTopLevelElementsSourceCode()
                + "    \n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public ImmutableList<PackageableElement> getTopLevelElements()\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                + \".getTopLevelElements() not implemented yet\");\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public ImmutableList<Enumeration> getEnumerations()\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                + \".getEnumerations() not implemented yet\");\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public ImmutableList<Klass> getKlasses()\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName() + \".getKlasses() not implemented yet\");\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public ImmutableList<Association> getAssociations()\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                + \".getAssociations() not implemented yet\");\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public ImmutableList<Projection> getProjections()\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                + \".getProjections() not implemented yet\");\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public ImmutableList<ServiceGroup> getServiceGroups()\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                + \".getServiceGroups() not implemented yet\");\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public Enumeration getEnumerationByName(String name)\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                + \".getEnumerationByName() not implemented yet\");\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public Klass getKlassByName(String name)\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                + \".getKlassByName() not implemented yet\");\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public Association getAssociationByName(String name)\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                + \".getAssociationByName() not implemented yet\");\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public Projection getProjectionByName(String name)\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                + \".getProjectionByName() not implemented yet\");\n"
                + "    }\n"
                + "}\n";
        return sourceCode;
    }

    @Nonnull
    private String getTopLevelElementsSourceCode()
    {
        return this.domainModel
                .getTopLevelElements()
                .collect(this::getTopLevelElementSourceCode)
                .makeString("");
    }

    private String getTopLevelElementSourceCode(PackageableElement topLevelElement)
    {
        if (topLevelElement instanceof ServiceGroup)
        {
            // TODO: ServiceGroup code generation
            return "";
        }
        return MessageFormat.format(
                "    public static final {1}_{0} {1} = {1}_{0}.INSTANCE;\n",
                this.getTypeName(topLevelElement),
                topLevelElement.getName());
    }

    private String getTypeName(Element element)
    {
        if (element instanceof Enumeration)
        {
            return Enumeration.class.getSimpleName();
        }

        if (element instanceof Klass)
        {
            return Klass.class.getSimpleName();
        }

        if (element instanceof Association)
        {
            return Association.class.getSimpleName();
        }

        if (element instanceof Projection)
        {
            return Projection.class.getSimpleName();
        }

        if (element instanceof ServiceGroup)
        {
            return ServiceGroup.class.getSimpleName();
        }

        if (element instanceof PrimitiveProperty)
        {
            return PrimitiveProperty.class.getSimpleName();
        }

        if (element instanceof EnumerationProperty)
        {
            return EnumerationProperty.class.getSimpleName();
        }

        if (element instanceof AssociationEnd)
        {
            return AssociationEnd.class.getSimpleName();
        }

        if (element instanceof ProjectionDataTypeProperty)
        {
            return ProjectionDataTypeProperty.class.getSimpleName();
        }

        if (element instanceof ProjectionAssociationEnd)
        {
            return ProjectionAssociationEnd.class.getSimpleName();
        }

        if (element instanceof EnumerationLiteral)
        {
            return EnumerationLiteral.class.getSimpleName();
        }

        throw new AssertionError(element.getClass().getSimpleName());
    }

    @Nonnull
    public Path getOutputPath(@Nonnull Path outputPath)
    {
        String rootPackageRelativePath = this.rootPackageName
                .replaceAll("\\.", "/");

        Path directory = outputPath
                .resolve(rootPackageRelativePath)
                .resolve("meta")
                .resolve("constants");
        directory.toFile().mkdirs();
        return directory.resolve(this.applicationName + "DomainModel.java");
    }

    @Nonnull
    public Path getOutputPath(
            @Nonnull Path outputPath,
            PackageableElement packageableElement)
    {
        String packageRelativePath = packageableElement.getPackageName()
                .replaceAll("\\.", "/");
        Path directory = outputPath
                .resolve(packageRelativePath)
                .resolve("meta")
                .resolve("constants");
        directory.toFile().mkdirs();
        String fileName = packageableElement.getName() + "_" + this.getTypeName(packageableElement) + ".java";
        return directory.resolve(fileName);
    }

    @Nonnull
    public Path getOutputPath(
            @Nonnull Path outputPath,
            Projection projection)
    {
        String packageRelativePath = projection.getPackageName()
                .replaceAll("\\.", "/");
        Path directory = outputPath
                .resolve(packageRelativePath)
                .resolve("meta")
                .resolve("constants");
        directory.toFile().mkdirs();
        String fileName = projection.getName() + "_" + this.getTypeName(projection) + ".java";
        return directory.resolve(fileName);
    }

    private void printStringToFile(@Nonnull Path path, String contents) throws FileNotFoundException
    {
        try (PrintStream printStream = new PrintStream(new FileOutputStream(path.toFile())))
        {
            printStream.print(contents);
        }
    }

    @Nonnull
    private String getEnumerationSourceCode(Enumeration enumeration)
    {
        String packageName = enumeration.getPackageName() + ".meta.constants";

        //language=JAVA
        String sourceCode = ""
                + "package " + packageName + ";\n"
                + "\n"
                + "import java.util.Optional;\n"
                + "\n"
                + "import javax.annotation.*;\n"
                + "\n"
                + "import cool.klass.model.meta.domain.api.*;\n"
                + "import cool.klass.model.meta.domain.api.order.*;\n"
                + "import cool.klass.model.meta.domain.api.property.*;\n"
                + "import cool.klass.model.meta.domain.api.projection.*;\n"
                + "\n"
                + "import org.eclipse.collections.api.list.*;\n"
                + "import org.eclipse.collections.impl.factory.*;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.meta.constants.JavaConstantsMetaModelGenerator}\n"
                + " */\n"
                + "@Generated(\n"
                + "        value = \"cool.klass.generator.meta.constants.JavaConstantsMetaModelGenerator\",\n"
                + "        date = \"" + this.now + "\")\n"
                + "public enum " + enumeration.getName() + "_" + "Enumeration implements Enumeration\n"
                + "{\n"
                + "    INSTANCE;\n"
                + "\n"
                + this.getEnumerationLiteralConstantsSourceCode(enumeration)
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getPackageName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(enumeration.getPackageName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(enumeration.getName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public int getOrdinal()\n"
                + "    {\n"
                + "        return " + enumeration.getOrdinal() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public boolean isInferred()\n"
                + "    {\n"
                + "        return " + enumeration.isInferred() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public ImmutableList<EnumerationLiteral> getEnumerationLiterals()\n"
                + "    {\n"
                + "        return Lists.immutable.with(" + enumeration.getEnumerationLiterals().collect(NamedElement::getName).makeString() + ");\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String toString()\n"
                + "    {\n"
                + "        return this.getName();\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String getSourceCode()\n"
                + "    {\n"
                + "        return \"\"\n"
                + "                + \"" + this.wrapSourceCode(enumeration.getSourceCode()) + "\";\n"
                + "    }\n"
                + "\n"
                + this.getEnumerationLiteralsSourceCode(enumeration)
                + "}\n";
        return sourceCode;
    }

    private String getEnumerationLiteralsSourceCode(Enumeration enumeration)
    {
        return enumeration.getEnumerationLiterals()
                .collect(this::getEnumerationLiteralSourceCode)
                .makeString("\n")
                .replaceAll("(?m)^(?!$)", "    ");
    }

    private String getEnumerationLiteralSourceCode(EnumerationLiteral enumerationLiteral)
    {
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, enumerationLiteral.getName());

        //language=JAVA
        return "public static enum " + uppercaseName + "_EnumerationLiteral implements EnumerationLiteral\n"
                + "{\n"
                + "    INSTANCE;\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(enumerationLiteral.getName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public int getOrdinal()\n"
                + "    {\n"
                + "        return " + enumerationLiteral.getOrdinal() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public boolean isInferred()\n"
                + "    {\n"
                + "        return " + enumerationLiteral.isInferred() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Nullable\n"
                + "    @Override\n"
                + "    public String getPrettyName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(enumerationLiteral.getPrettyName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public Enumeration getType()\n"
                + "    {\n"
                + "        return " + this.applicationName + "DomainModel." + enumerationLiteral.getType().getName() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String toString()\n"
                + "    {\n"
                + "        return this.getPrettyName();\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String getSourceCode()\n"
                + "    {\n"
                + "        return \"\"\n"
                + "                + \"" + this.wrapSourceCode(enumerationLiteral.getSourceCode()) + "\";\n"
                + "    }\n"
                + "}\n";
    }

    private String getEnumerationLiteralConstantsSourceCode(Enumeration enumeration)
    {
        return enumeration.getEnumerationLiterals()
                .collect(this::getEnumerationLiteralConstantSourceCode)
                .makeString("");
    }

    private <V> String getEnumerationLiteralConstantSourceCode(EnumerationLiteral enumerationLiteral)
    {
        String name          = enumerationLiteral.getName();
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
        String type          = this.getTypeName(enumerationLiteral);

        return MessageFormat.format(
                "    public static final {0}_{1} {2} = {0}_{1}.INSTANCE;\n",
                uppercaseName,
                type,
                name);
    }

    @Nonnull
    private String getClassSourceCode(Klass klass)
    {
        //language=JAVA
        String sourceCode = "package "
                + klass.getPackageName()
                + ".meta.constants;\n"
                + "\n"
                + "import java.util.Optional;\n"
                + "\n"
                + "import javax.annotation.*;\n"
                + "\n"
                + "import cool.klass.model.meta.domain.api.*;\n"
                + "import cool.klass.model.meta.domain.api.order.*;\n"
                + "import cool.klass.model.meta.domain.api.property.*;\n"
                + "\n"
                + "import org.eclipse.collections.api.list.*;\n"
                + "import org.eclipse.collections.impl.factory.*;"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.meta.constants.JavaConstantsMetaModelGenerator}\n"
                + " */\n"
                + "@Generated(\n"
                + "        value = \"cool.klass.generator.meta.constants.JavaConstantsMetaModelGenerator\",\n"
                + "        date = \"" + this.now + "\")\n"
                + "public enum " + klass.getName() + "_Klass implements Klass\n"
                + "{\n"
                + "    INSTANCE;\n"
                + "    \n"
                + this.getMemberConstantsSourceCode(klass)
                + this.getAssociationEndConstantsSourceCode(klass)
                + "    \n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getPackageName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(klass.getPackageName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(klass.getName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public int getOrdinal()\n"
                + "    {\n"
                + "        return " + klass.getOrdinal() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public boolean isInferred()\n"
                + "    {\n"
                + "        return " + klass.isInferred() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public ImmutableList<DataTypeProperty> getDataTypeProperties()\n"
                + "    {\n"
                + "        return Lists.immutable.with(" + klass.getDataTypeProperties().collect(NamedElement::getName).makeString() + ");\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public Optional<AssociationEnd> getVersionProperty()\n"
                + "    {\n"
                + "        return " + this.getOptionalAssociationEndSourceCode(klass.getVersionProperty()) + ";\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public Optional<AssociationEnd> getVersionedProperty()\n"
                + "    {\n"
                + "        return " + this.getOptionalAssociationEndSourceCode(klass.getVersionedProperty()) + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public ImmutableList<AssociationEnd> getAssociationEnds()\n"
                + "    {\n"
                + "        return Lists.immutable.with(" + klass.getAssociationEnds().collect(NamedElement::getName).makeString() + ");\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public ImmutableList<ClassModifier> getClassModifiers()\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                + \".getClassModifiers() not implemented yet\");\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public boolean isUser()\n"
                + "    {\n"
                + "        return " + klass.isUser() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public boolean isTransient()\n"
                + "    {\n"
                + "        return " + klass.isTransient() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String toString()\n"
                + "    {\n"
                + "        return this.getName();\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String getSourceCode()\n"
                + "    {\n"
                + "        return \"\"\n"
                + "                + \"" + this.wrapSourceCode(klass.getSourceCode()) + "\";\n"
                + "    }\n"
                + "\n"
                + this.getDataTypePropertiesSourceCode(klass)
                + "\n"
                + this.getAssociationEndsSourceCode(klass)
                + "}\n";
        return sourceCode;
    }

    private String getAssociationEndsSourceCode(Klass klass)
    {
        return klass.getAssociationEnds()
                .collect(this::getAssociationEndSourceCode)
                .makeString("\n");
    }

    @Nonnull
    private String getOptionalAssociationEndSourceCode(Optional<AssociationEnd> optionalAssociationEnd)
    {
        return optionalAssociationEnd
                .map(associationEnd -> String.format(
                        "Optional.of(%s)",
                        associationEnd.getName()))
                .orElse("Optional.empty()");
    }

    private String getDataTypePropertiesSourceCode(Klass klass)
    {
        return klass.getDataTypeProperties()
                .collect(this::getDataTypePropertySourceCode)
                .makeString("\n");
    }

    private String getDataTypePropertySourceCode(DataTypeProperty dataTypeProperty)
    {
        if (dataTypeProperty instanceof PrimitiveProperty)
        {
            PrimitiveProperty primitiveProperty = (PrimitiveProperty) dataTypeProperty;
            return this.getPrimitivePropertySourceCode(primitiveProperty);
        }
        if (dataTypeProperty instanceof EnumerationProperty)
        {
            EnumerationProperty enumerationProperty = (EnumerationProperty) dataTypeProperty;
            return this.getEnumerationPropertySourceCode(enumerationProperty);
        }
        throw new AssertionError();
    }

    private String getPrimitivePropertySourceCode(PrimitiveProperty primitiveProperty)
    {
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, primitiveProperty.getName());

        //language=JAVA
        return ""
                + "    public static enum " + uppercaseName + "_PrimitiveProperty implements PrimitiveProperty\n"
                + "    {\n"
                + "        INSTANCE;\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public String getName()\n"
                + "        {\n"
                + "            return \"" + StringEscapeUtils.escapeJava(primitiveProperty.getName()) + "\";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public int getOrdinal()\n"
                + "        {\n"
                + "            return " + primitiveProperty.getOrdinal() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public boolean isInferred()\n"
                + "        {\n"
                + "            return " + primitiveProperty.isInferred() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public boolean isID()\n"
                + "        {\n"
                + "            return " + primitiveProperty.isID() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public boolean isKey()\n"
                + "        {\n"
                + "            return " + primitiveProperty.isKey() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public boolean isOptional()\n"
                + "        {\n"
                + "            return " + primitiveProperty.isOptional() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public ImmutableList<PropertyModifier> getPropertyModifiers()\n"
                + "        {\n"
                + this.getPropertyModifiersSourceCode(primitiveProperty.getPropertyModifiers())
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public Klass getOwningKlass()\n"
                + "        {\n"
                + "            return " + this.applicationName + "DomainModel." + primitiveProperty.getOwningKlass().getName() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public PrimitiveType getType()\n"
                + "        {\n"
                + "            return PrimitiveType." + primitiveProperty.getType().name() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public String toString()\n"
                + "        {\n"
                + "            return this.getName();\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public String getSourceCode()\n"
                + "        {\n"
                + "            return \"\"\n"
                + "                    + \"" + this.wrapSourceCode(primitiveProperty.getSourceCode()) + "\";\n"
                + "        }\n"
                + "    }\n";
    }

    private String getPropertyModifiersSourceCode(ImmutableList<PropertyModifier> propertyModifiers)
    {
        if (propertyModifiers.isEmpty())
        {
            return "            return Lists.immutable.empty();\n";
        }
        String variablesSourceCode = propertyModifiers
                .collect(this::getPropertyModifierSourceCode)
                .makeString("\n");

        ImmutableList<String> variableNames = propertyModifiers
                .collect(NamedElement::getName)
                .collect(each -> each + "_" + PropertyModifier.class.getSimpleName());

        return variablesSourceCode
                + "\n"
                + "            return Lists.immutable.with(" + variableNames.makeString() + ");\n";
    }

    private String getPropertyModifierSourceCode(PropertyModifier propertyModifier)
    {
        return ""
                + "            PropertyModifier " + propertyModifier.getName() + "_" + PropertyModifier.class.getSimpleName() + " = new PropertyModifier()\n"
                + "            {\n"
                + "                @Nonnull\n"
                + "                @Override\n"
                + "                public String getName()\n"
                + "                {\n"
                + "                    return \"" + StringEscapeUtils.escapeJava(propertyModifier.getName()) + "\";\n"
                + "                }\n"
                + "\n"
                + "                @Override\n"
                + "                public int getOrdinal()\n"
                + "                {\n"
                + "                    return " + propertyModifier.getOrdinal() + ";\n"
                + "                }\n"
                + "\n"
                + "                @Override\n"
                + "                public boolean isInferred()\n"
                + "                {\n"
                + "                    return " + propertyModifier.isInferred() + ";\n"
                + "                }\n"
                + "\n"
                + "                @Override\n"
                + "                public String toString()\n"
                + "                {\n"
                + "                    return this.getName();\n"
                + "                }\n"
                + "\n"
                + "                @Nonnull\n"
                + "                @Override\n"
                + "                public String getSourceCode()\n"
                + "                {\n"
                + "                    return \"\"\n"
                + "                            + \"" + this.wrapSourceCode(propertyModifier.getSourceCode()) + "\";\n"
                + "                }\n"
                + "            };\n";
    }

    private String getEnumerationPropertySourceCode(EnumerationProperty enumerationProperty)
    {
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, enumerationProperty.getName());

        //language=JAVA
        return ""
                + "    public static enum " + uppercaseName + "_EnumerationProperty implements EnumerationProperty\n"
                + "    {\n"
                + "        INSTANCE;\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public String getName()\n"
                + "        {\n"
                + "            return \"" + StringEscapeUtils.escapeJava(enumerationProperty.getName()) + "\";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public int getOrdinal()\n"
                + "        {\n"
                + "            return " + enumerationProperty.getOrdinal() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public boolean isInferred()\n"
                + "        {\n"
                + "            return " + enumerationProperty.isInferred() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public boolean isKey()\n"
                + "        {\n"
                + "            return " + enumerationProperty.isKey() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public boolean isOptional()\n"
                + "        {\n"
                + "            return " + enumerationProperty.isOptional() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public ImmutableList<PropertyModifier> getPropertyModifiers()\n"
                + "        {\n"
                + this.getPropertyModifiersSourceCode(enumerationProperty.getPropertyModifiers())
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public Klass getOwningKlass()\n"
                + "        {\n"
                + "            return " + this.applicationName + "DomainModel." + enumerationProperty.getOwningKlass().getName() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public Enumeration getType()\n"
                + "        {\n"
                + "            return " + this.applicationName + "DomainModel." + enumerationProperty.getType().getName() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public String toString()\n"
                + "        {\n"
                + "            return this.getName();\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public String getSourceCode()\n"
                + "        {\n"
                + "            return \"\"\n"
                + "                    + \"" + this.wrapSourceCode(enumerationProperty.getSourceCode()) + "\";\n"
                + "        }\n"
                + "    }\n";
    }

    private String getMemberConstantsSourceCode(Klass klass)
    {
        // TODO: Change from properties to members
        return klass.getDataTypeProperties()
                .collect(this::getDataTypePropertyConstantSourceCode)
                .makeString("");
    }

    private String getDataTypePropertyConstantSourceCode(DataTypeProperty dataTypeProperty)
    {
        String name          = dataTypeProperty.getName();
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
        String type          = this.getTypeName(dataTypeProperty);

        return MessageFormat.format(
                "    public static final {0}_{1} {2} = {0}_{1}.INSTANCE;\n",
                uppercaseName,
                type,
                name);
    }

    @Nonnull
    private String wrapSourceCode(String unwrappedSourceCode)
    {
        return StringEscapeUtils.escapeJava(unwrappedSourceCode)
                .replaceAll("\\\\n", "\\\\n\" \n                + \"");
    }

    @Nonnull
    private String getAssociationSourceCode(Association association)
    {
        //language=JAVA
        String sourceCode = ""
                + "package " + association.getPackageName() + ".meta.constants;\n"
                + "\n"
                + "import java.util.Optional;\n"
                + "\n"
                + "import javax.annotation.*;\n"
                + "\n"
                + "import cool.klass.model.meta.domain.api.*;\n"
                + "import cool.klass.model.meta.domain.api.criteria.*;\n"
                + "import cool.klass.model.meta.domain.api.order.*;\n"
                + "import cool.klass.model.meta.domain.api.property.*;\n"
                + "import cool.klass.model.meta.domain.api.projection.*;\n"
                + "import org.eclipse.collections.api.list.ImmutableList;import org.eclipse.collections.impl.factory.Lists;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.meta.constants.JavaConstantsMetaModelGenerator}\n"
                + " */\n"
                + "@Generated(\n"
                + "        value = \"cool.klass.generator.meta.constants.JavaConstantsMetaModelGenerator\",\n"
                + "        date = \"" + this.now + "\")\n"
                + "public enum " + association.getName() + "_Association implements Association\n"
                + "{\n"
                + "    INSTANCE;\n"
                + "\n"
                + this.getAssociationEndConstantsSourceCode(association)
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getPackageName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(association.getPackageName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(association.getName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public int getOrdinal()\n"
                + "    {\n"
                + "        return " + association.getOrdinal() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public boolean isInferred()\n"
                + "    {\n"
                + "        return " + association.isInferred() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public Criteria getCriteria()\n"
                + "    {\n"
                + "        throw new UnsupportedOperationException(this.getClass().getSimpleName() + \".getCriteria() not implemented yet\");\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public ImmutableList<AssociationEnd> getAssociationEnds()\n"
                + "    {\n"
                + "        return Lists.immutable.with(source, target);\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public AssociationEnd getSourceAssociationEnd()\n"
                + "    {\n"
                + "        return source;\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public AssociationEnd getTargetAssociationEnd()\n"
                + "    {\n"
                + "        return target;\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String toString()\n"
                + "    {\n"
                + "        return this.getName();\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String getSourceCode()\n"
                + "    {\n"
                + "        return \"\"\n"
                + "                + \"" + this.wrapSourceCode(association.getSourceCode()) + "\";\n"
                + "    }\n"
                + "}\n";
        return sourceCode;
    }

    private String getAssociationEndSourceCode(AssociationEnd associationEnd)
    {
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, associationEnd.getName());

        //language=JAVA
        return ""
                + "    public static enum " + uppercaseName + "_AssociationEnd implements AssociationEnd\n"
                + "    {\n"
                + "        INSTANCE;\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public String getName()\n"
                + "        {\n"
                + "            return \"" + StringEscapeUtils.escapeJava(associationEnd.getName()) + "\";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public int getOrdinal()\n"
                + "        {\n"
                + "            return " + associationEnd.getOrdinal() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public boolean isInferred()\n"
                + "        {\n"
                + "            return " + associationEnd.isInferred() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public Klass getType()\n"
                + "        {\n"
                + "            return " + this.applicationName + "DomainModel." + associationEnd.getType().getName() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public Multiplicity getMultiplicity()\n"
                + "        {\n"
                + "            return Multiplicity." + associationEnd.getMultiplicity().name() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public ImmutableList<AssociationEndModifier> getAssociationEndModifiers()\n"
                + "        {\n"
                + "            throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                    + \".getAssociationEndModifiers() not implemented yet\");\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public boolean isOwned()\n"
                + "        {\n"
                + "            return " + associationEnd.isOwned() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public AssociationEnd getOpposite()\n"
                + "        {\n"
                + "            throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                    + \".getOpposite() not implemented yet\");\n"
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public Association getOwningAssociation()\n"
                + "        {\n"
                + "            return " + this.applicationName + "DomainModel." + associationEnd.getOwningAssociation().getName() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public Optional<OrderBy> getOrderBy()\n"
                + "        {\n"
                + "            throw new UnsupportedOperationException(this.getClass().getSimpleName()\n"
                + "                    + \".getOrderBy() not implemented yet\");\n"
                + "        }\n"
                + "\n"
                + "        @Nonnull\n"
                + "        @Override\n"
                + "        public Klass getOwningKlass()\n"
                + "        {\n"
                + "            return " + this.applicationName + "DomainModel." + associationEnd.getOwningKlass().getName() + ";\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public String toString()\n"
                + "        {\n"
                + "            return this.getName();\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public String getSourceCode()\n"
                + "        {\n"
                + "            return \"\"\n"
                + "                    + \"" + this.wrapSourceCode(associationEnd.getSourceCode()) + "\";\n"
                + "        }\n"
                + "    }\n";
    }

    private String getAssociationEndConstantsSourceCode(Klass klass)
    {
        return klass.getAssociationEnds()
                .collect(this::getAssociationEndConstantSourceCode)
                .makeString("");
    }

    private String getAssociationEndConstantsSourceCode(Association association)
    {
        return this.getAssociationEndConstantSourceCode(association.getSourceAssociationEnd(), "source")
                + this.getAssociationEndConstantSourceCode(association.getTargetAssociationEnd(), "target");
    }

    private String getAssociationEndConstantSourceCode(AssociationEnd associationEnd)
    {
        String name          = associationEnd.getName();
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
        String type          = this.getTypeName(associationEnd);

        return MessageFormat.format(
                "    public static final {0}_{1} {2} = {0}_{1}.INSTANCE;\n",
                uppercaseName,
                type,
                name);
    }

    private String getAssociationEndConstantSourceCode(AssociationEnd associationEnd, String sideName)
    {
        String name          = associationEnd.getName();
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);

        String result = "    public static final "
                + associationEnd.getOwningKlass().getName()
                + "_Klass."
                + uppercaseName
                + "_AssociationEnd "
                + sideName
                + " = "
                + this.applicationName
                + "DomainModel."
                + associationEnd.getOwningKlass().getName()
                + "."
                + name
                + ";\n";

        return result;
    }

    @Nonnull
    private String getProjectionSourceCode(Projection projection)
    {
        //language=JAVA
        String sourceCode = ""
                + "package " + projection.getPackageName() + ".meta.constants;\n"
                + "\n"
                + "import java.util.Optional;\n"
                + "\n"
                + "import javax.annotation.*;\n"
                + "\n"
                + "import cool.klass.model.meta.domain.api.*;\n"
                + "import cool.klass.model.meta.domain.api.order.*;\n"
                + "import cool.klass.model.meta.domain.api.property.*;\n"
                + "import cool.klass.model.meta.domain.api.projection.*;\n"
                + "import org.eclipse.collections.api.list.*;\n"
                + "import org.eclipse.collections.impl.factory.*;"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.meta.constants.JavaConstantsMetaModelGenerator}\n"
                + " */\n"
                + "@Generated(\n"
                + "        value = \"cool.klass.generator.meta.constants.JavaConstantsMetaModelGenerator\",\n"
                + "        date = \"" + this.now + "\")\n"
                + "public enum " + projection.getName() + "_Projection implements Projection\n"
                + "{\n"
                + "    INSTANCE;\n"
                + "\n"
                + this.getProjectionChildrenConstantsSourceCode(projection)
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getPackageName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(projection.getPackageName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(projection.getName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public int getOrdinal()\n"
                + "    {\n"
                + "        return " + projection.getOrdinal() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public boolean isInferred()\n"
                + "    {\n"
                + "        return " + projection.isInferred() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public Klass getKlass()\n"
                + "    {\n"
                + "        return " + this.applicationName + "DomainModel." + projection.getKlass().getName() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public ImmutableList<ProjectionElement> getChildren()\n"
                + "    {\n"
                + "        return Lists.immutable.with(" + projection.getChildren().collect(ProjectionElement::getName).makeString() + ");\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String toString()\n"
                + "    {\n"
                + "        return this.getName();\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String getSourceCode()\n"
                + "    {\n"
                + "        return \"\"\n"
                + "                + \"" + this.wrapSourceCode(projection.getSourceCode()) + "\";\n"
                + "    }\n"
                + "\n"
                + this.getProjectionChildrenSourceCode(projection)
                + "}\n";
        return sourceCode;
    }

    private String getProjectionChildrenConstantsSourceCode(ProjectionParent projectionParent)
    {
        return projectionParent.getChildren()
                .collect(this::getProjectionChildConstantSourceCode)
                .makeString("");
    }

    private String getProjectionChildConstantSourceCode(ProjectionElement projectionElement)
    {
        String name          = projectionElement.getName();
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
        String type          = this.getTypeName(projectionElement);

        return MessageFormat.format(
                "    public static final {0}_{1} {2} = {0}_{1}.INSTANCE;\n",
                uppercaseName,
                type,
                name);
    }

    private String getProjectionChildrenSourceCode(ProjectionParent projectionParent)
    {
        return projectionParent.getChildren()
                .collect(this::getProjectionChildSourceCode)
                .makeString("\n")
                // https://stackoverflow.com/questions/15888934/how-to-indent-a-multi-line-paragraph-being-written-to-the-console-in-java
                .replaceAll("(?m)^", "    ");
    }

    private String getProjectionChildSourceCode(ProjectionElement projectionElement)
    {
        if (projectionElement instanceof ProjectionDataTypeProperty)
        {
            ProjectionDataTypeProperty projectionDataTypeProperty = (ProjectionDataTypeProperty) projectionElement;
            return this.getProjectionDataTypePropertySourceCode(projectionDataTypeProperty);
        }
        if (projectionElement instanceof ProjectionAssociationEnd)
        {
            ProjectionAssociationEnd projectionAssociationEnd = (ProjectionAssociationEnd) projectionElement;
            return this.getProjectionAssociationEndSourceCode(projectionAssociationEnd);
        }
        throw new AssertionError();
    }

    private String getProjectionDataTypePropertySourceCode(ProjectionDataTypeProperty projectionDataTypeProperty)
    {
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, projectionDataTypeProperty.getName());

        DataTypeProperty dataTypeProperty = projectionDataTypeProperty.getProperty();

        //language=JAVA
        return ""
                + "public static enum " + uppercaseName + "_ProjectionDataTypeProperty implements ProjectionDataTypeProperty\n"
                + "{\n"
                + "    INSTANCE;\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(projectionDataTypeProperty.getName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public int getOrdinal()\n"
                + "    {\n"
                + "        return " + projectionDataTypeProperty.getOrdinal() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public boolean isInferred()\n"
                + "    {\n"
                + "        return " + projectionDataTypeProperty.isInferred() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getHeaderText()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(projectionDataTypeProperty.getHeaderText()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public DataTypeProperty getProperty()\n"
                + "    {\n"
                + "        return " + this.applicationName + "DomainModel." + dataTypeProperty.getOwningKlass().getName() + "." + dataTypeProperty.getName() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String toString()\n"
                + "    {\n"
                + "        return this.getName();\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String getSourceCode()\n"
                + "    {\n"
                + "        return \"\"\n"
                + "                + \"" + this.wrapSourceCode(projectionDataTypeProperty.getSourceCode()) + "\";\n"
                + "    }\n"
                + "}\n";
    }

    private String getProjectionAssociationEndSourceCode(ProjectionAssociationEnd projectionAssociationEnd)
    {
        String uppercaseName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, projectionAssociationEnd.getName());

        AssociationEnd associationEnd = projectionAssociationEnd.getAssociationEnd();

        //language=JAVA
        return ""
                + "public static enum " + uppercaseName + "_ProjectionAssociationEnd implements ProjectionAssociationEnd\n"
                + "{\n"
                + "    INSTANCE;\n"
                + "\n"
                + this.getProjectionChildrenConstantsSourceCode(projectionAssociationEnd)
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getName()\n"
                + "    {\n"
                + "        return \"" + StringEscapeUtils.escapeJava(projectionAssociationEnd.getName()) + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public int getOrdinal()\n"
                + "    {\n"
                + "        return " + projectionAssociationEnd.getOrdinal() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public boolean isInferred()\n"
                + "    {\n"
                + "        return " + projectionAssociationEnd.isInferred() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public ImmutableList<ProjectionElement> getChildren()\n"
                + "    {\n"
                + "        return Lists.immutable.with(" + projectionAssociationEnd.getChildren().collect(ProjectionElement::getName).makeString() + ");\n"
                + "    }\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public AssociationEnd getAssociationEnd()\n"
                + "    {\n"
                + "        return " + this.applicationName + "DomainModel." + associationEnd.getOwningKlass().getName() + "." + associationEnd.getName() + ";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String toString()\n"
                + "    {\n"
                + "        return this.getName();\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public String getSourceCode()\n"
                + "    {\n"
                + "        return \"\"\n"
                + "                + \"" + this.wrapSourceCode(projectionAssociationEnd.getSourceCode()) + "\";\n"
                + "    }\n"
                + "\n"
                + this.getProjectionChildrenSourceCode(projectionAssociationEnd)
                + "}\n";
    }
}
