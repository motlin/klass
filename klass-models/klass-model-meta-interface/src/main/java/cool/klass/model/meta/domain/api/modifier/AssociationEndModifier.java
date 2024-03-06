package cool.klass.model.meta.domain.api.modifier;

import cool.klass.model.meta.domain.api.property.AssociationEnd;

public interface AssociationEndModifier extends Modifier
{
    AssociationEnd getModifierOwner();

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
