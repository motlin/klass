package cool.klass.model.meta.domain.api.property;

import cool.klass.model.meta.domain.api.NamedElement;

public interface AssociationEndModifier extends NamedElement
{
    AssociationEnd getAssociationEnd();

    default boolean isOwned()
    {
        return this.getName().equals("owned");
    }

    default boolean isVersion()
    {
        return this.getName().equals("version");
    }

    default boolean isFinal()
    {
        return this.getName().equals("final");
    }
}
