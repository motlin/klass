package cool.klass.model.meta.domain.api.modifier;

import cool.klass.model.meta.domain.api.NamedElement;

public interface Modifier extends NamedElement
{
    ModifierOwner getModifierOwner();
}
