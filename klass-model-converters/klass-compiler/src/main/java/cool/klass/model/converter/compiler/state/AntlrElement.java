package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.AbstractElement.ElementBuilder;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

public abstract class AntlrElement
        implements IAntlrElement
{
    @Nonnull
    protected final ParserRuleContext         elementContext;
    // TODO: Consider creating a "native" source file with native declarations of PrimitiveTypes
    /**
     * The type of compilationUnit is Optional because some Elements, specifically PrimitiveTypes are not declared in SourceCode
     */
    @Nonnull
    protected final Optional<CompilationUnit> compilationUnit;

    protected AntlrElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit)
    {
        this.elementContext  = Objects.requireNonNull(elementContext);
        this.compilationUnit = Objects.requireNonNull(compilationUnit);

        compilationUnit.ifPresent(cu -> AntlrElement.assertContextContains(cu.getParserContext(), elementContext));
    }

    private static void assertContextContains(ParserRuleContext parentContext, ParserRuleContext childContext)
    {
        if (parentContext == childContext)
        {
            return;
        }

        ParserRuleContext nextParent = childContext.getParent();
        Objects.requireNonNull(nextParent);
        AntlrElement.assertContextContains(parentContext, nextParent);
    }

    protected static String getSourceText(ParserRuleContext parserRuleContext)
    {
        int startIndex = parserRuleContext.getStart().getStartIndex();
        int      stopIndex = parserRuleContext.getStop().getStopIndex();
        Interval interval  = new Interval(startIndex, stopIndex);
        return parserRuleContext.getStart().getInputStream().getText(interval);
    }

    @Override
    @Nonnull
    public ParserRuleContext getElementContext()
    {
        return this.elementContext;
    }

    @Nonnull
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

    @Nonnull
    protected Optional<ElementBuilder<?>> getMacroElementBuilder()
    {
        return this.getMacroElement().map(antlrElement -> Objects.requireNonNull(antlrElement.getElementBuilder()));
    }

    protected SourceCodeBuilder getSourceCodeBuilder()
    {
        return this.compilationUnit.map(CompilationUnit::build).orElseThrow();
    }

    @Nonnull
    @Override
    public Optional<CompilationUnit> getCompilationUnit()
    {
        return this.compilationUnit;
    }

    public boolean isInSameCompilationUnit(AntlrElement other)
    {
        return this.compilationUnit.isPresent() && other.compilationUnit.isPresent() && this.compilationUnit.equals(other.compilationUnit);
    }

    public boolean isForwardReference(AntlrElement other)
    {
        return this.isInSameCompilationUnit(other)
                && this.getElementContext().getStart().getStartIndex() < other.getElementContext().getStart().getStartIndex();
    }
}
