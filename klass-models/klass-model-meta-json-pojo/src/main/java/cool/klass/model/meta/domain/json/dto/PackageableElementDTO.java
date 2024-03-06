package cool.klass.model.meta.domain.json.dto;

public abstract class PackageableElementDTO
        extends NamedElementDTO
{
    private final String packageName;

    protected PackageableElementDTO(
            String name,
            String packageName)
    {
        super(name);
        this.packageName = packageName;
    }

    public String getPackageName()
    {
        return this.packageName;
    }
}
