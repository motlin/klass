package cool.klass.serialization.jackson.module.meta.model.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.serialization.jackson.module.meta.model.domain.MultiplicitySerializer;
import cool.klass.serialization.jackson.module.meta.model.domain.ProjectionSerializer;

public class KlassMetaModelJacksonModule extends SimpleModule
{
    public KlassMetaModelJacksonModule()
    {
        this.addSerializer(Multiplicity.class, new MultiplicitySerializer());
        this.addSerializer(Projection.class, new ProjectionSerializer());
    }
}
