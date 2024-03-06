package cool.klass.model.converter.compiler.state;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Type.TypeGetter;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

// All AntlrTypes are named elements, but the interface doesn't exist yet
public interface AntlrType extends IAntlrElement
{
    String getName();

    TypeGetter getTypeGetter();

    @Nonnull
    TypeGetter getElementBuilder();

    default ImmutableList<AntlrType> getPotentialWiderTypes()
    {
        return Lists.immutable.with(this);
    }
}
