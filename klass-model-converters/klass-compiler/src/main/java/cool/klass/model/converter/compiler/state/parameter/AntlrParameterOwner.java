package cool.klass.model.converter.compiler.state.parameter;

import javax.annotation.Nonnull;

import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationContext;

public interface AntlrParameterOwner
{
    int getNumParameters();

    void enterParameterDeclaration(@Nonnull AntlrParameter parameter);

    AntlrParameter getParameterByContext(@Nonnull ParameterDeclarationContext ctx);
}
