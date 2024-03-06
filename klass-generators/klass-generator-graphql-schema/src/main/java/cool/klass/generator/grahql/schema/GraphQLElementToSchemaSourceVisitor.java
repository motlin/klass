package cool.klass.generator.grahql.schema;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.TopLevelElementVisitor;
import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.property.Property;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import cool.klass.model.meta.domain.api.service.ServiceGroup;

public class GraphQLElementToSchemaSourceVisitor
        implements TopLevelElementVisitor
{
    private String sourceCode;

    @Override
    public void visitEnumeration(Enumeration enumeration)
    {
        this.sourceCode = this.getEnumerationSourceCode(enumeration);
    }

    @Override
    public void visitInterface(Interface anInterface)
    {
        this.sourceCode = "";
    }

    @Override
    public void visitKlass(Klass klass)
    {
        this.sourceCode = this.getClassSourceCode(klass);
    }

    @Override
    public void visitAssociation(Association association)
    {
        this.sourceCode = "";
    }

    @Override
    public void visitProjection(Projection projection)
    {
        this.sourceCode = "";
    }

    @Override
    public void visitServiceGroup(ServiceGroup serviceGroup)
    {
        this.sourceCode = "";
    }

    @Nonnull
    private String getEnumerationSourceCode(@Nonnull Enumeration enumeration)
    {
        String enumerationLiteralsSourceCode = enumeration
                .getEnumerationLiterals()
                .collect(NamedElement::getName)
                .collect(name -> String.format("    %s\n", name))
                .makeString("");

        return ""
                + "enum " + enumeration.getName() + " {\n"
                + enumerationLiteralsSourceCode
                + "}\n"
                + '\n';
    }

    @Nonnull
    private String getInterfaceSourceCode(@Nonnull Interface anInterface)
    {
        String fieldsSourceCode = anInterface
                .getProperties()
                .collect(GraphQLElementToSchemaSourceVisitor::getPropertySourceCode)
                .collect(name -> String.format("    %s\n", name))
                .makeString("");

        return ""
                + "interface " + anInterface.getName() + " {\n"
                + fieldsSourceCode
                + "}\n"
                + '\n';
    }

    @Nonnull
    private String getClassSourceCode(@Nonnull Klass klass)
    {
        String fieldsSourceCode = klass
                .getProperties()
                .reject(Property::isDerived)
                .reject(Property::isPrivate)
                .collect(GraphQLElementToSchemaSourceVisitor::getPropertySourceCode)
                .collect(name -> String.format("    %s\n", name))
                .makeString("");

        String implementsSourceCode = "";
        // ImmutableList<String> interfaces = klass.getInterfaces().collect(NamedElement::getName);
        // String implementsSourceCode = interfaces.isEmpty() ? "" : " implements " + interfaces.makeString();

        return ""
                + "type " + klass.getName() + implementsSourceCode + " {\n"
                + fieldsSourceCode
                + "}\n"
                + '\n';
    }

    private static String getPropertySourceCode(@Nonnull Property property)
    {
        return String.format(
                "%s: %s%s%s%s",
                property.getName(),
                GraphQLElementToSchemaSourceVisitor.isMany(property) ? "[" : "",
                GraphQLElementToSchemaSourceVisitor.getType(property),
                GraphQLElementToSchemaSourceVisitor.isMany(property) ? "!]" : "",
                property.isRequired() || GraphQLElementToSchemaSourceVisitor.isMany(property) ? "!" : "");
    }

    @Nonnull
    private static String getType(@Nonnull Property property)
    {
        Type type = property.getType();
        if (type instanceof Enumeration)
        {
            return "String";
        }
        if (type == PrimitiveType.INTEGER)
        {
            return "Int";
        }
        if (type == PrimitiveType.DOUBLE)
        {
            return "Float";
        }
        return type.toString();
    }

    private static boolean isMany(@Nonnull Property property)
    {
        return property instanceof ReferenceProperty
                && ((ReferenceProperty) property).getMultiplicity().isToMany();
    }

    public String getSourceCode()
    {
        return this.sourceCode;
    }
}
