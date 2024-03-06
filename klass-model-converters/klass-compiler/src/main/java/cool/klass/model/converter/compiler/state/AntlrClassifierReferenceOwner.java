package cool.klass.model.converter.compiler.state;

import javax.annotation.Nonnull;

public interface AntlrClassifierReferenceOwner
        extends IAntlrElement
{
    void enterClassifierReference(@Nonnull AntlrClassifierReference classifierReference);
}
