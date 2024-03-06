package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.AbstractElement.ElementBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public abstract class AntlrElement implements IAntlrElement
{
    @Nonnull
    protected final ParserRuleContext         elementContext;
    @Nonnull
    protected final Optional<CompilationUnit> compilationUnit;

    protected AntlrElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit)
    {
        this.elementContext = Objects.requireNonNull(elementContext);
        this.compilationUnit = Objects.requireNonNull(compilationUnit);
    }

    @Override
    @Nonnull
    public ParserRuleContext getElementContext()
    {
        return this.elementContext;
    }

    public ElementBuilder<?> getElementBuilder()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getElementBuilder() not implemented yet");
    }

    @Override
    @Nonnull
    public Optional<AntlrElement> getMacroElement()
    {
        return this.compilationUnit.flatMap(CompilationUnit::getMacroElement);
    }

    protected Optional<ElementBuilder<?>> getMacroElementBuilder()
    {
        return this.getMacroElement().map(AntlrElement::getElementBuilder);
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
    public Optional<CompilationUnit> getCompilationUnit()
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
