package cool.klass.model.converter.compiler.state;

import javax.annotation.Nonnull;

public interface AntlrMultiplicityOwner
        extends IAntlrElement
{
    void enterMultiplicity(@Nonnull AntlrMultiplicity multiplicityState);
}
