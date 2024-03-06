package cool.klass.model.meta.domain.api;

import javax.annotation.Nullable;

public interface IEnumerationLiteral extends ITypedElement<IEnumeration>
{
    @Nullable
    String getPrettyName();
}
