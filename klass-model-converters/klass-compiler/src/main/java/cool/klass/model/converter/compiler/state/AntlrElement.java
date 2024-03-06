package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public abstract class AntlrElement implements IAntlrElement
{
    @Nonnull
    protected final ParserRuleContext elementContext;
    @Nullable
    protected final CompilationUnit   compilationUnit;
    protected final boolean           inferred;

    protected AntlrElement(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred)
    {
        this.elementContext = Objects.requireNonNull(elementContext);
        this.compilationUnit = compilationUnit;
        this.inferred = inferred;
    }

    @Override
    @Nonnull
    public ParserRuleContext getElementContext()
    {
        return this.elementContext;
    }

    @Override
    @Nonnull
    public final ImmutableList<ParserRuleContext> getParserRuleContexts()
    {
        MutableList<ParserRuleContext> result = Lists.mutable.empty();
        this.getParserRuleContexts(result);
        return result.toImmutable();
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
    }

    @Nonnull
    @Override
    @Nullable
    public CompilationUnit getCompilationUnit()
    {
        return this.compilationUnit;
    }

    @Override
    public String getSourceCode()
    {
        Interval interval = new Interval(
                this.elementContext.getStart().getStartIndex(),
                this.elementContext.getStop().getStopIndex());
        return this.elementContext.getStart().getInputStream().getText(interval);
    }
}
