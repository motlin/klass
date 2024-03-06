package cool.klass.model.meta.domain.api;

public interface ClassifierModifier extends NamedElement
{
    default boolean isTransient()
    {
        return this.getName().equals("transient");
    }
}
