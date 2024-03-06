package cool.klass.reladomo.tree.serializer;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

// TODO: Move this to a shared module
public class ReflectionCache
{
    private final MutableOrderedMap<String, Class<?>> classCache  = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<CacheKey, Method> methodCache = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    @Nonnull
    public Class<?> classForName(String dtoFQCN)
    {
        if (this.classCache.containsKey(dtoFQCN))
        {
            return this.classCache.get(dtoFQCN);
        }

        try
        {
            Class<?> result = Class.forName(dtoFQCN);
            this.classCache.put(dtoFQCN, result);
            return result;
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    public Method getMethod(Class<?> objectClass, String methodName, Class<?>... parameterTypes)
    {
        CacheKey key = new CacheKey(objectClass, methodName, List.of(parameterTypes));
        if (this.methodCache.containsKey(key))
        {
            return this.methodCache.get(key);
        }

        try
        {
            Method result = objectClass.getMethod(methodName, parameterTypes);
            this.methodCache.put(key, result);
            return result;
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    private record CacheKey(Class<?> objectClass, String methodName, List<Class<?>> parameterTypes)
    {
    }
}
