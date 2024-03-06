package cool.klass.model.converter.compiler.error;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
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
            ParserRuleContext... parserRuleContexts)
    {
        RootCompilerError compilerError = new RootCompilerError(
                compilationUnit,
                Optional.empty(),
                Lists.immutable.with(offendingParserRuleContext),
                Lists.immutable.with(parserRuleContexts),
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

    public void add(
            @Nonnull String errorCode,
            @Nonnull String message,
            @Nonnull IAntlrElement element,
            ImmutableList<IAntlrElement> surroundingElements,
            ImmutableList<ParserRuleContext> offendingContexts)
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
    public RootCompilerError getCompilerError(
            @Nonnull String errorCode,
            @Nonnull String message,
            @Nonnull IAntlrElement element,
            ImmutableList<IAntlrElement> surroundingElements,
            ImmutableList<ParserRuleContext> offendingContexts)
    {
        Optional<CauseCompilerError> macroCause = getCauseCompilerError(element);

        ParserRuleContext outerContext = element.getCompilationUnit().getParserContext();

        ImmutableList<ParserRuleContext> innerContexts = surroundingElements
                .collect(IAntlrElement::getElementContext);

        ImmutableList<ParserRuleContext> allParserRuleContexts = Lists.immutable
                .withAll(innerContexts)
                .newWith(outerContext);

        return new RootCompilerError(
                element.getCompilationUnit(),
                macroCause,
                offendingContexts,
                allParserRuleContexts,
                errorCode,
                message);
    }

    @Nonnull
    public CauseCompilerError getCauseCompilerError(
            @Nonnull IAntlrElement element,
            ImmutableList<IAntlrElement> surroundingElements,
            ImmutableList<ParserRuleContext> offendingContexts)
    {
        Optional<CauseCompilerError> macroCause = getCauseCompilerError(element);

        ParserRuleContext outerContext = element.getCompilationUnit().getParserContext();

        ImmutableList<ParserRuleContext> innerContexts = surroundingElements
                .collect(IAntlrElement::getElementContext);

        ImmutableList<ParserRuleContext> allParserRuleContexts = Lists.immutable
                .withAll(innerContexts)
                .newWith(outerContext);

        return new CauseCompilerError(
                element.getCompilationUnit(),
                macroCause,
                offendingContexts,
                allParserRuleContexts);
    }

    public Optional<CauseCompilerError> getCauseCompilerError(@Nonnull IAntlrElement element)
    {
        return element.getMacroElement().map(macroElement -> this.getCauseCompilerError(
                macroElement,
                macroElement.getSurroundingElements(),
                Lists.immutable.with(macroElement.getElementContext())));
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
