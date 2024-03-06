package cool.klass.generator.grahql.schema;

import javax.annotation.Nonnull;

import cool.klass.model.graphql.domain.GraphQLClass;
import cool.klass.model.graphql.domain.GraphQLElementVisitor;
import cool.klass.model.graphql.domain.GraphQLEnumeration;
import cool.klass.model.graphql.domain.GraphQLField;
import cool.klass.model.graphql.domain.GraphQLInterface;
import cool.klass.model.graphql.domain.GraphQLNamedElement;
import org.eclipse.collections.api.list.ImmutableList;

public class GraphQLElementToSchemaSourceVisitor implements GraphQLElementVisitor
{
    private String sourceCode;

    @Override
    public void visitEnumeration(@Nonnull GraphQLEnumeration graphQLEnumeration)
    {
        this.sourceCode = this.getEnumerationSourceCode(graphQLEnumeration);
    }

    @Override
    public void visitInterface(@Nonnull GraphQLInterface graphQLInterface)
    {
        this.sourceCode = this.getInterfaceSourceCode(graphQLInterface);
    }

    @Override
    public void visitClass(@Nonnull GraphQLClass graphQLClass)
    {
        this.sourceCode = this.getClassSourceCode(graphQLClass);
    }

    @Nonnull
    private String getEnumerationSourceCode(@Nonnull GraphQLEnumeration graphQLEnumeration)
    {
        String enumerationLiteralsSourceCode = graphQLEnumeration
                .getEnumerationLiterals()
                .collect(GraphQLNamedElement::getName)
                .collect(name -> String.format("    %s\n", name))
                .makeString("");

        return ""
                + "enum " + graphQLEnumeration.getName() + " {\n"
                + enumerationLiteralsSourceCode
                + "}\n"
                + '\n';
    }

    @Nonnull
    private String getInterfaceSourceCode(@Nonnull GraphQLInterface graphQLInterface)
    {
        String fieldsSourceCode = graphQLInterface
                .getFields()
                .collect(GraphQLElementToSchemaSourceVisitor::getFieldSourceCode)
                .collect(name -> String.format("    %s\n", name))
                .makeString("");

        return ""
                + "interface " + graphQLInterface.getName() + " {\n"
                + fieldsSourceCode
                + "}\n"
                + '\n';
    }

    @Nonnull
    private String getClassSourceCode(@Nonnull GraphQLClass graphQLClass)
    {
        String fieldsSourceCode = graphQLClass
                .getFields()
                .collect(GraphQLElementToSchemaSourceVisitor::getFieldSourceCode)
                .collect(name -> String.format("    %s\n", name))
                .makeString("");

        ImmutableList<String> interfaces           = graphQLClass.getInterfaces();
        String                implementsSourceCode = interfaces.isEmpty() ? "" : " implements " + interfaces.makeString();

        return ""
                + "type " + graphQLClass.getName() + implementsSourceCode + " {\n"
                + fieldsSourceCode
                + "}\n"
                + '\n';
    }

    private static String getFieldSourceCode(@Nonnull GraphQLField graphQLField)
    {
        return String.format(
                "%s: %s%s%s%s",
                graphQLField.getName(),
                graphQLField.isMany() ? "[" : "",
                graphQLField.getType(),
                graphQLField.isMany() ? "!]" : "",
                graphQLField.isRequired() ? "!" : "");
    }

    public String getSourceCode()
    {
        return this.sourceCode;
    }
}
