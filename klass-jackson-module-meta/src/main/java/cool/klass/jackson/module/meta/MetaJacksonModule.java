package cool.klass.jackson.module.meta;

import com.fasterxml.jackson.databind.module.SimpleModule;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.projection.Projection;

public class MetaJacksonModule extends SimpleModule
{
    public MetaJacksonModule()
    {
        this.addSerializer(Multiplicity.class, new MultiplicitySerializer());
        this.addSerializer(Projection.class, new ProjectionSerializer());
    }
}
