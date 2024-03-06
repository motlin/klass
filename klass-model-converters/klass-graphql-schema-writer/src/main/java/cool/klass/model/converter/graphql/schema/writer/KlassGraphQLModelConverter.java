package cool.klass.model.converter.graphql.schema.writer;

import java.util.Objects;

import cool.klass.model.graphql.domain.GraphQLClass;
import cool.klass.model.graphql.domain.GraphQLDomainModel;
import cool.klass.model.graphql.domain.GraphQLElement;
import cool.klass.model.graphql.domain.GraphQLEnumeration;
import cool.klass.model.graphql.domain.GraphQLEnumerationLiteral;
import cool.klass.model.graphql.domain.GraphQLField;
import cool.klass.model.graphql.domain.GraphQLInterface;
import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.property.Property;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import org.eclipse.collections.api.list.ImmutableList;

public class KlassGraphQLModelConverter
{
    private final DomainModel domainModel;

    public KlassGraphQLModelConverter(DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    public GraphQLDomainModel convert()
    {
        ImmutableList<GraphQLElement> topLevelElements = this.domainModel.getTopLevelElements()
                .reject(Association.class::isInstance)
                .reject(Projection.class::isInstance)
                .reject(ServiceGroup.class::isInstance)
                .collect(KlassGraphQLModelConverter::convertTopLevelElement);

        return new GraphQLDomainModel(topLevelElements);
    }

    public static GraphQLElement convertTopLevelElement(PackageableElement packageableElement)
    {
        if (packageableElement instanceof Enumeration)
        {
            return KlassGraphQLModelConverter.convertEnumeration((Enumeration) packageableElement);
        }

        if (packageableElement instanceof Interface)
        {
            return KlassGraphQLModelConverter.convertInterface((Interface) packageableElement);
        }

        if (packageableElement instanceof Klass)
        {
            return KlassGraphQLModelConverter.convertClass((Klass) packageableElement);
        }

        throw new AssertionError(packageableElement.getClass().getSimpleName());
    }

    private static GraphQLElement convertEnumeration(Enumeration enumeration)
    {
        return new GraphQLEnumeration(
                enumeration.getName(),
                enumeration.getEnumerationLiterals().collect(KlassGraphQLModelConverter::convertEnumerationLiteral));
    }

    private static GraphQLEnumerationLiteral convertEnumerationLiteral(EnumerationLiteral enumerationLiteral)
    {
        return new GraphQLEnumerationLiteral(enumerationLiteral.getName());
    }

    public static GraphQLInterface convertInterface(Interface anInterface)
    {
        return new GraphQLInterface(
                anInterface.getName(),
                anInterface.getProperties().collect(KlassGraphQLModelConverter::convertProperty));
    }

    public static GraphQLClass convertClass(Klass klass)
    {
        return new GraphQLClass(
                klass.getName(),
                klass.getProperties().collect(KlassGraphQLModelConverter::convertProperty),
                klass.getInterfaces().collect(NamedElement::getName));
    }

    private static GraphQLField convertProperty(Property property)
    {
        return new GraphQLField(
                property.getName(),
                convertType(property.getType()),
                property instanceof ReferenceProperty && ((ReferenceProperty) property).getMultiplicity().isToMany(),
                property.isRequired() || (property instanceof ReferenceProperty
                        && ((ReferenceProperty) property).getMultiplicity().isToMany()));
    }

    private static String convertType(@Nonnull Type type)
    {
        if (type == PrimitiveType.INTEGER)
        {
            return "Int";
        }
        return type.toString();
    }
}
