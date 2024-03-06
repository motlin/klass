package cool.klass.model.meta.domain.api.modifier;

import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface DataTypePropertyModifier extends Modifier
{
    String CREATED_BY      = "createdBy";
    String CREATED_ON      = "createdOn";
    String LAST_UPDATED_BY = "lastUpdatedBy";

    ImmutableList<String> AUDIT_PROPERTY_NAMES = Lists.immutable.with(
            CREATED_BY,
            CREATED_ON,
            LAST_UPDATED_BY);

    @Override
    DataTypeProperty getModifierOwner();

    default boolean isKey()
    {
        return this.getName().equals("key");
    }

    default boolean isID()
    {
        return this.getName().equals("id");
    }

    default boolean isValid()
    {
        return this.getName().equals("valid");
    }

    default boolean isSystem()
    {
        return this.getName().equals("system");
    }

    default boolean isFrom()
    {
        return this.getName().equals("from");
    }

    default boolean isTo()
    {
        return this.getName().equals("to");
    }

    default boolean isFinal()
    {
        return this.getName().equals("final");
    }

    default boolean isPrivate()
    {
        return this.getName().equals("private");
    }

    default boolean isAudit()
    {
        return AUDIT_PROPERTY_NAMES.contains(this.getName());
    }

    default boolean isCreatedBy()
    {
        return this.getName().equals(CREATED_BY);
    }

    default boolean isCreatedOn()
    {
        return this.getName().equals(CREATED_ON);
    }

    default boolean isLastUpdatedBy()
    {
        return this.getName().equals(LAST_UPDATED_BY);
    }

    default boolean isVersion()
    {
        return this.getName().equals("version");
    }

    default boolean isDerived()
    {
        return this.getName().equals("derived");
    }
}
