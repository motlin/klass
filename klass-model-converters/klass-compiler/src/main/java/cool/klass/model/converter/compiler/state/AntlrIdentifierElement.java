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
            @Nonnull IdentifierContext nameContext,
            int ordinal)
    {
        super(elementContext, compilationUnit, nameContext, ordinal);
    }

    @Nonnull
    @Override
    public IdentifierContext getNameContext()
    {
        return (IdentifierContext) super.getNameContext();
    }
}
