package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.domain.api.Type.TypeGetter;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface AntlrType
{
    TypeGetter getTypeGetter();

    TypeGetter getElementBuilder();

    default ImmutableList<AntlrType> getPotentialWiderTypes()
    {
        return Lists.immutable.with(this);
    }
}
