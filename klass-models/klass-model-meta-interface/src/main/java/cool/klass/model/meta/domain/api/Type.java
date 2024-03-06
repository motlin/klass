package cool.klass.model.meta.domain.api;

public interface Type
{
    String getName();

    interface TypeGetter
    {
        Type getType();
    }
}
