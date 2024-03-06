package cool.klass.generator.grahql.fragment;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class GraphQLFragmentSourceCodeGenerator
{
    private GraphQLFragmentSourceCodeGenerator()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    @Nonnull
    public static String getPackageSourceCode(DomainModel domainModel, String fullyQualifiedPackage)
    {
        String sourceCode = domainModel
                .getClassifiers()
                .select(c -> c.getPackageName().equals(fullyQualifiedPackage))
                .collect(GraphQLFragmentSourceCodeGenerator::getClassifierSourceCode)
                .makeString("\n");

        return ""
               + "# Auto-generated by " + GraphQLFragmentSourceCodeGenerator.class.getCanonicalName() + "\n"
               + "\n"
               + sourceCode;
    }

    private static String getClassifierSourceCode(Classifier classifier)
    {
        String dataTypePropertiesSourceCode = getDataTypePropertiesSourceCode(classifier, false)
                .makeString("");

        String referencePropertiesSourceCode = getReferencePropertiesSourceCode(classifier, false)
                .makeString("");

        ImmutableList<Klass> subClasses = classifier instanceof Klass klass
                ? klass.getSubClassChain()
                : Lists.immutable.empty();

        String subClassesSourceCode = subClasses
                .collect(GraphQLFragmentSourceCodeGenerator::getSubClassSourceCode)
                .makeString("");

        if (dataTypePropertiesSourceCode.isEmpty()
                && referencePropertiesSourceCode.isEmpty()
                && subClassesSourceCode.isEmpty())
        {
            return "";
        }

        String typeNameSourceCode = classifier.isAbstract() ? "  __typename\n" : "";

        return "fragment " + classifier.getName() + "Fragment on " + classifier.getName() + " {\n"
                + typeNameSourceCode
                + dataTypePropertiesSourceCode
                + referencePropertiesSourceCode
                + subClassesSourceCode
                + "}\n";
    }

    @Nonnull
    private static String getSubClassSourceCode(Klass subClass)
    {
        String dataTypePropertiesSourceCode = getDataTypePropertiesSourceCode(subClass, true)
                .makeString("");

        String referencePropertiesSourceCode = getReferencePropertiesSourceCode(subClass, true)
                .makeString("");

        if (dataTypePropertiesSourceCode.isEmpty() && referencePropertiesSourceCode.isEmpty())
        {
            return "";
        }

        return "  ... on " + subClass.getName() + " {\n"
                + dataTypePropertiesSourceCode
                + referencePropertiesSourceCode
                + "  }\n";
    }

    private static ImmutableList<String> getDataTypePropertiesSourceCode(Classifier classifier, boolean subClassMode)
    {
        ImmutableList<DataTypeProperty> dataTypeProperties = subClassMode
                ? classifier.getDeclaredDataTypeProperties()
                : classifier.getDataTypeProperties();

        return dataTypeProperties
                .reject(DataTypeProperty::isPrivate)
                .reject(property -> property.isForeignKey() && !property.isForeignKeyToSelf())
                .collectWith(GraphQLFragmentSourceCodeGenerator::getDataTypePropertySourceCode, subClassMode);
    }

    private static String getDataTypePropertySourceCode(DataTypeProperty dataTypeProperty, boolean subClassMode)
    {
        String indentation = subClassMode ? "  " : "";
        return String.format("  %s%s\n", indentation, dataTypeProperty.getName());
    }

    private static ImmutableList<String> getReferencePropertiesSourceCode(Classifier classifier, boolean subClassMode)
    {
        ImmutableList<ReferenceProperty> properties = subClassMode
                ? classifier.getDeclaredReferenceProperties()
                : classifier.getReferenceProperties();

        return properties
                .select(GraphQLFragmentSourceCodeGenerator::includeInProjection)
                .reject(ReferenceProperty::isPrivate)
                .collectWith(GraphQLFragmentSourceCodeGenerator::getReferencePropertySourceCode, subClassMode);
    }

    private static String getReferencePropertySourceCode(ReferenceProperty referenceProperty, boolean subClassMode)
    {
        String indentation = subClassMode ? "  " : "";
        return indentation + "  " + referenceProperty.getName() + " {\n"
               + GraphQLFragmentSourceCodeGenerator.getReferencePropertyBody(referenceProperty, subClassMode)
               + indentation + "  }\n";
    }

    private static String getReferencePropertyBody(ReferenceProperty referenceProperty, boolean subClassMode)
    {
        String indentation = subClassMode ? "  " : "";

        if (referenceProperty.isOwned() || GraphQLFragmentSourceCodeGenerator.isOneRequiredToOneOptional(
                referenceProperty))
        {
            return indentation + "    ..." + referenceProperty.getType().getName() + "Fragment\n";
        }

        String typeNameString =  referenceProperty.getType().isAbstract() ? indentation + "    __typename\n" : "";

        String keyPropertiesSourceCode = referenceProperty
                .getType()
                .getKeyProperties()
                .reject(dataTypeProperty -> dataTypeProperty.isForeignKey() && !dataTypeProperty.isForeignKeyToSelf())
                .collect(dataTypeProperty -> String.format("    %s%s\n", indentation, dataTypeProperty.getName()))
                .makeString("");
        return typeNameString + keyPropertiesSourceCode;
    }

    private static boolean isOneRequiredToOneOptional(ReferenceProperty referenceProperty)
    {
        return referenceProperty instanceof AssociationEnd associationEnd && associationEnd.getMultiplicity().isToOne()
               && !associationEnd.getMultiplicity().isRequired()
               && associationEnd.getOpposite().getMultiplicity().isToOne()
               && associationEnd.getOpposite().getMultiplicity().isRequired();
    }

    private static boolean includeInProjection(ReferenceProperty referenceProperty)
    {
        return !(referenceProperty instanceof AssociationEnd associationEnd)
               || associationEnd.getOwningAssociation().getTargetAssociationEnd() == associationEnd
               || associationEnd.isToSelf() && associationEnd.getMultiplicity().isToOne();
    }
}
