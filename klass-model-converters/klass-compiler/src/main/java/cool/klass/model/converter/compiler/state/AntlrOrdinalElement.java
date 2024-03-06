package cool.klass.model.converter.compiler.state;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrOrdinalElement
        extends AntlrElement
{
    protected final int ordinal;

    public AntlrOrdinalElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal)
    {
        super(elementContext, compilationUnit);
        this.ordinal = ordinal;
    }

    public int getOrdinal()
    {
        return this.ordinal;
    }
}
