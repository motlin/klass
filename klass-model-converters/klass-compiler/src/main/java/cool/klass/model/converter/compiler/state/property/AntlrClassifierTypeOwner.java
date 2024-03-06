package cool.klass.model.converter.compiler.state.property;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.state.AntlrClassifierType;
import cool.klass.model.converter.compiler.state.IAntlrElement;

public interface AntlrClassifierTypeOwner
        extends IAntlrElement
{
    void enterClassifierType(@Nonnull AntlrClassifierType classifierTypeState);
}
