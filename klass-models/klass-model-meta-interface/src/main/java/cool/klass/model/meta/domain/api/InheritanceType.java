package cool.klass.model.meta.domain.api;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Lists;

public enum InheritanceType
{
    TABLE_PER_SUBCLASS("table-per-subclass"),
    TABLE_FOR_ALL_SUBCLASSES("table-for-all-subclasses"),
    TABLE_PER_CLASS("table-per-class"),
    NONE("none");

    public static final ImmutableList<InheritanceType> INHERITANCE_TYPES = Lists.immutable.with(
            TABLE_PER_SUBCLASS,
            TABLE_FOR_ALL_SUBCLASSES,
            TABLE_PER_CLASS);

    private static final ImmutableMap<String, InheritanceType> BY_PRETTY_NAME =
            INHERITANCE_TYPES.groupByUniqueKey(InheritanceType::getPrettyName);

    @Nonnull
    private final String prettyName;

    InheritanceType(@Nonnull String prettyName)
    {
        this.prettyName = prettyName;
    }

    public static InheritanceType byPrettyName(String name)
    {
        return Objects.requireNonNull(BY_PRETTY_NAME.get(name));
    }

    @Nonnull
    public String getPrettyName()
    {
        return this.prettyName;
    }

    @Nonnull
    @Override
    public String toString()
    {
        return this.getPrettyName();
    }
}
