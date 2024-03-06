package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.AbstractElement.ElementBuilder;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public abstract class AntlrElement
        implements IAntlrElement
{
    @Nonnull
    protected final ParserRuleContext         elementContext;
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

    protected Optional<SourceCodeBuilder> getSourceCodeBuilder()
    {
        return this.compilationUnit.map(CompilationUnit::build);
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
}
