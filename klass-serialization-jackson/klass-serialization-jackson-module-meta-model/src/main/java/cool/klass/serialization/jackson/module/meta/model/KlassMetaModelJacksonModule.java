package cool.klass.serialization.jackson.module.meta.model;

import com.fasterxml.jackson.databind.module.SimpleModule;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.projection.Projection;

public class KlassMetaModelJacksonModule extends SimpleModule
{
    public KlassMetaModelJacksonModule()
    {
        this.addSerializer(Multiplicity.class, new MultiplicitySerializer());
        this.addSerializer(Projection.class, new ProjectionSerializer());
    }
}
