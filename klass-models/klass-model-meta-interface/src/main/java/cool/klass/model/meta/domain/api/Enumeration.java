package cool.klass.model.meta.domain.api;

import org.eclipse.collections.api.list.ImmutableList;

public interface Enumeration extends PackageableElement, DataType
{
    ImmutableList<EnumerationLiteral> getEnumerationLiterals();
}
