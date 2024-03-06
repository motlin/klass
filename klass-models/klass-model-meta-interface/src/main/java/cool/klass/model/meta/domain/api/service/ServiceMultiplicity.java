package cool.klass.model.meta.domain.api.service;

import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;

public enum ServiceMultiplicity
{
    ONE("one"),
    MANY("many");

    private static final ImmutableMap<String, ServiceMultiplicity> SERVICE_MULTIPLICITY_BY_PRETTY_NAME =
            ArrayAdapter.adapt(ServiceMultiplicity.values())
                    .groupByUniqueKey(ServiceMultiplicity::getPrettyName)
                    .toImmutable();

    private final String prettyName;

    ServiceMultiplicity(String prettyName)
    {
        this.prettyName = prettyName;
    }

    public static ServiceMultiplicity getByPrettyName(String prettyName)
    {
        return SERVICE_MULTIPLICITY_BY_PRETTY_NAME.get(prettyName);
    }

    public String getPrettyName()
    {
        return this.prettyName;
    }
}
