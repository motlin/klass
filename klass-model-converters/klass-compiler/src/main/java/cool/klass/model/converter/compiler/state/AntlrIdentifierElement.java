package cool.klass.model.converter.compiler.state;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrIdentifierElement
        extends AntlrNamedElement
{
    protected AntlrIdentifierElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
    }

    @Nonnull
    @Override
    public IdentifierContext getNameContext()
    {
        return (IdentifierContext) super.getNameContext();
    }
}
