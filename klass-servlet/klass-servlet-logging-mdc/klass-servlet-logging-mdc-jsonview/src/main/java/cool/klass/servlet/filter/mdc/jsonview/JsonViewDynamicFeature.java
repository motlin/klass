package cool.klass.servlet.filter.mdc.jsonview;

import java.lang.reflect.Method;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import com.fasterxml.jackson.annotation.JsonView;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.serialization.jackson.jsonview.KlassJsonView;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;

public class JsonViewDynamicFeature implements DynamicFeature
{
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context)
    {
        Method   resourceMethod     = resourceInfo.getResourceMethod();
        JsonView jsonViewAnnotation = resourceMethod.getAnnotation(JsonView.class);
        if (jsonViewAnnotation == null)
        {
            return;
        }

        Class<?>[] jsonViewClasses = jsonViewAnnotation.value();
        if (jsonViewClasses.length == 0)
        {
            return;
        }

        if (jsonViewClasses.length > 1)
        {
            throw new RuntimeException(ArrayAdapter.adapt(jsonViewClasses).makeString());
        }

        Class<?> jsonViewClass = jsonViewClasses[0];
        if (!KlassJsonView.class.isAssignableFrom(jsonViewClass))
        {
            return;
        }

        KlassJsonView klassJsonView = this.instantiate(jsonViewClass);
        Projection    projection    = klassJsonView.getProjection();
        context.register(new JsonViewFilter(projection));
    }

    private KlassJsonView instantiate(Class<?> jsonViewClass)
    {
        try
        {
            return jsonViewClass.asSubclass(KlassJsonView.class).newInstance();
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }
}
