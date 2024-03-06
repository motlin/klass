package cool.klass.model.meta.domain.api.property;

import cool.klass.model.meta.domain.api.NamedElement;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

// TODO: Split for location specific modifiers?
public interface PropertyModifier extends NamedElement
{
    ImmutableList<String> AUDIT_PROPERTY_NAMES = Lists.immutable.with(
            "createdBy",
            "createdOn",
            "lastUpdatedBy");

    default boolean isKey()
    {
        return this.getName().equals("key");
    }

    default boolean isID()
    {
        return this.getName().equals("id");
    }

    default boolean isTo()
    {
        return this.getName().equals("to");
    }

    default boolean isAudit()
    {
        return AUDIT_PROPERTY_NAMES.contains(this.getName());
    }
}
