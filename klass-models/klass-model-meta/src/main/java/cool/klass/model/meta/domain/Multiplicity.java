package cool.klass.model.meta.domain;

import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;

public enum Multiplicity
{
    ZERO_TO_ONE("0..1"),
    ONE_TO_ONE("1..1"),
    ZERO_TO_MANY("0..*"),
    ONE_TO_MANY("1..*");

    private static final ImmutableMap<String, Multiplicity> MULTIPLICITY_BY_PRETTY_NAME =
            ArrayAdapter.adapt(Multiplicity.values())
                    .groupByUniqueKey(Multiplicity::getPrettyName)
                    .toImmutable();

    private final String prettyName;

    Multiplicity(String prettyName)
    {
        this.prettyName = prettyName;
    }

    public static Multiplicity getByPrettyName(String prettyName)
    {
        return MULTIPLICITY_BY_PRETTY_NAME.get(prettyName);
    }

    public String getPrettyName()
    {
        return this.prettyName;
    }

    public boolean isToOne()
    {
        return this == ZERO_TO_ONE || this == ONE_TO_ONE;
    }

    public boolean isToMany()
    {
        return this == ZERO_TO_MANY || this == ONE_TO_MANY;
    }
}
