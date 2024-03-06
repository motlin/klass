package cool.klass.model.converter.compiler.state;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrIdentifierElement
        extends AntlrNamedElement
{
    public static final IdentifierContext AMBIGUOUS_IDENTIFIER_CONTEXT = new IdentifierContext(AMBIGUOUS_PARENT, -1);
    public static final IdentifierContext NOT_FOUND_IDENTIFIER_CONTEXT = new IdentifierContext(NOT_FOUND_PARENT, -1);

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
