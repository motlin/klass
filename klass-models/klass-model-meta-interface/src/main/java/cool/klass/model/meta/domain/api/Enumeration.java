package cool.klass.model.meta.domain.api;

import org.eclipse.collections.api.list.ImmutableList;

public interface Enumeration
        extends PackageableElement, DataType
{
    @Override
    default String getDataTypeName()
    {
        return this.getName();
    }

    ImmutableList<EnumerationLiteral> getEnumerationLiterals();
}
