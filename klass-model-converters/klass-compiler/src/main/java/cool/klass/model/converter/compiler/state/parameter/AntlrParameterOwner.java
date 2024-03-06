package cool.klass.model.converter.compiler.state.parameter;

import javax.annotation.Nonnull;

public interface AntlrParameterOwner
{
    int getNumParameters();

    void enterParameterDeclaration(@Nonnull AntlrParameter parameterState);
}
