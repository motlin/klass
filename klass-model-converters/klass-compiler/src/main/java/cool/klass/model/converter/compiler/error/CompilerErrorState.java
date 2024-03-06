package cool.klass.model.converter.compiler.error;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.SourceContext;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class CompilerErrorState
{
    private final MutableList<RootCompilerError> compilerErrors = Lists.mutable.empty();

    public void add(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull String errorCode,
            @Nonnull String message,
            @Nonnull ParserRuleContext offendingParserRuleContext,
            SourceContext... sourceContexts)
    {
        RootCompilerError compilerError = new RootCompilerError(
                compilationUnit,
                Optional.empty(),
                Lists.immutable.with(offendingParserRuleContext),
                Lists.immutable.with(sourceContexts),
                errorCode,
                message);
        this.compilerErrors.add(compilerError);
    }

    public void add(@Nonnull String errorCode, @Nonnull String message, IAntlrElement element)
    {
        if (element instanceof AntlrNamedElement)
        {
            this.add(errorCode, message, element, ((AntlrNamedElement) element).getNameContext());
        }
        else
        {
            this.add(errorCode, message, element, element.getElementContext());
        }
    }

    public void add(
            @Nonnull String errorCode,
            @Nonnull String message,
            @Nonnull IAntlrElement element,
            @Nonnull ParserRuleContext offendingContext)
    {
        this.add(errorCode, message, element, element.getSurroundingElements(), Lists.immutable.with(offendingContext));
    }

    public void add(
            @Nonnull String errorCode,
            @Nonnull String message,
            @Nonnull IAntlrElement element,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts)
    {
        this.add(errorCode, message, element, element.getSurroundingElementsIncludingThis(), offendingContexts);
    }

    private void add(
            @Nonnull String errorCode,
            @Nonnull String message,
            @Nonnull IAntlrElement element,
            @Nonnull ImmutableList<IAntlrElement> surroundingElements,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts)
    {
        RootCompilerError compilerError = this.getCompilerError(
                errorCode,
                message,
                element,
                surroundingElements,
                offendingContexts);
        this.compilerErrors.add(compilerError);
    }

    @Nonnull
    private RootCompilerError getCompilerError(
            @Nonnull String errorCode,
            @Nonnull String message,
            @Nonnull IAntlrElement element,
            @Nonnull ImmutableList<IAntlrElement> surroundingElements,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts)
    {
        Optional<CauseCompilerError> macroCause = this.getCauseCompilerError(element);

        ImmutableList<SourceContext> allParserRuleContexts = this.getSourceContexts(element, surroundingElements);

        CompilationUnit compilationUnit = element.getCompilationUnit().get();
        return new RootCompilerError(
                compilationUnit,
                macroCause,
                offendingContexts,
                allParserRuleContexts,
                errorCode,
                message);
    }

    @Nonnull
    private CauseCompilerError getCauseCompilerError(
            @Nonnull IAntlrElement element,
            @Nonnull ImmutableList<IAntlrElement> surroundingElements,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts)
    {
        Optional<CauseCompilerError> macroCause = this.getCauseCompilerError(element);

        ImmutableList<SourceContext> sourceContexts = this.getSourceContexts(element, surroundingElements);

        CompilationUnit compilationUnit = element.getCompilationUnit().get();
        return new CauseCompilerError(
                compilationUnit,
                macroCause,
                offendingContexts,
                sourceContexts);
    }

    private Optional<CauseCompilerError> getCauseCompilerError(@Nonnull IAntlrElement element)
    {
        return element.getMacroElement().map(macroElement -> this.getCauseCompilerError(
                macroElement,
                macroElement.getSurroundingElements(),
                Lists.immutable.with(macroElement.getElementContext())));
    }

    private ImmutableList<SourceContext> getSourceContexts(
            @Nonnull IAntlrElement element,
            @Nonnull ImmutableList<IAntlrElement> surroundingElements)
    {
        IAntlrElement     outerElement                 = element.getOuterElement();
        CompilationUnit   outerElementCompilationUnit  = outerElement.getCompilationUnit().get();
        ParserRuleContext compilationUnitParserContext = outerElementCompilationUnit.getParserContext();
        SourceContext outerContext = new SourceContext(
                outerElementCompilationUnit,
                compilationUnitParserContext);
        if (!(compilationUnitParserContext instanceof CompilationUnitContext))
        {
            throw new AssertionError(outerContext.getClass().getSimpleName());
        }

        ImmutableList<SourceContext> innerContexts = surroundingElements
                .collect(IAntlrElement::getSourceContext);

        return Lists.immutable
                .withAll(innerContexts)
                .newWith(outerContext);
    }

    public ImmutableList<RootCompilerError> getCompilerErrors()
    {
        return this.compilerErrors
                .toSortedList()
                .toImmutable();
    }

    public boolean hasCompilerErrors()
    {
        return this.compilerErrors.notEmpty();
    }
}
