package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;

public class AntlrClassState
{
    private final ClassDeclarationContext ctx;

    public AntlrClassState(ClassDeclarationContext ctx)
    {
        this.ctx = ctx;
    }
}
