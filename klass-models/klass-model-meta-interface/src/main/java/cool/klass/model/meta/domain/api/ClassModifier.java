package cool.klass.model.meta.domain.api;

public interface ClassModifier extends NamedElement
{
    default boolean isTransient()
    {
        return this.getName().equals("transient");
    }
}
