package cool.klass.model.converter.compiler.state.property;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.state.AntlrClassType;
import cool.klass.model.converter.compiler.state.IAntlrElement;

public interface AntlrClassTypeOwner
        extends IAntlrElement
{
    void enterClassType(@Nonnull AntlrClassType classState);
}
