package cool.klass.model.meta.domain;

import java.util.Objects;

import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class DomainModel
{
    private final ImmutableList<Enumeration> enumerations;
    private final ImmutableList<Klass> klasses;

    public DomainModel(
            ImmutableList<Enumeration> enumerations,
            ImmutableList<Klass> klasses)
    {
        this.enumerations = enumerations;
        this.klasses = klasses;
    }

    public ImmutableList<Enumeration> getEnumerations()
    {
        return this.enumerations;
    }

    public ImmutableList<Klass> getKlasses()
    {
        return this.klasses;
    }

    public static final class DomainModelBuilder
    {
        private final MutableList<EnumerationBuilder> enumerationBuilders = Lists.mutable.empty();
        private final MutableList<KlassBuilder> klassBuilders = Lists.mutable.empty();

        public DomainModelBuilder enumeration(EnumerationBuilder enumerationBuilder)
        {
            this.enumerationBuilders.add(Objects.requireNonNull(enumerationBuilder));
            return this;
        }

        public DomainModelBuilder klass(KlassBuilder klassBuilder)
        {
            this.klassBuilders.add(Objects.requireNonNull(klassBuilder));
            return this;
        }

        public DomainModel build()
        {
            return new DomainModel(Lists.immutable.empty(), Lists.immutable.empty());
        }
    }
}
