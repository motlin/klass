package cool.klass.model.converter.compiler.state;

import javax.annotation.Nonnull;

public interface AntlrClassReferenceOwner
        extends IAntlrElement
{
    void enterClassReference(@Nonnull AntlrClassReference classReference);
}
