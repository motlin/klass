package cool.klass.model.meta.domain.api;

import org.eclipse.collections.api.list.ImmutableList;

public interface IEnumeration extends DataType
{
    ImmutableList<IEnumerationLiteral> getEnumerationLiterals();
}
