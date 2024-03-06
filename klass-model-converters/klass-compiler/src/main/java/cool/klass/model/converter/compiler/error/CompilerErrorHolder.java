package cool.klass.model.converter.compiler.error;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class CompilerErrorHolder
{
    private final MutableList<CompilerError> compilerErrors = Lists.mutable.empty();

    public void add(
            @Nonnull String message,
            @Nonnull ParserRuleContext offendingParserRuleContext,
            ParserRuleContext... parserRuleContexts)
    {
        CompilerError compilerError = new CompilerError(
                message,
                offendingParserRuleContext,
                Lists.immutable.with(parserRuleContexts));
        this.compilerErrors.add(compilerError);
    }

    public void add(@Nonnull String message, IAntlrElement element)
    {
        if (element instanceof AntlrNamedElement)
        {
            this.add(message, ((AntlrNamedElement) element).getNameContext(), element);
        }
        else
        {
            this.add(message, element.getElementContext(), element);
        }
    }

    public void add(
            @Nonnull String message,
            @Nonnull ParserRuleContext offendingParserRuleContext,
            @Nonnull IAntlrElement element)
    {
        ParserRuleContext outerContext = element.getCompilationUnit().getParserContext();

        ImmutableList<ParserRuleContext> innerContexts = element.getSurroundingElements()
                .collect(IAntlrElement::getElementContext);

        ImmutableList<ParserRuleContext> allParserRuleContexts = Lists.immutable
                .withAll(innerContexts)
                .newWith(outerContext);

        CompilerError compilerError = new CompilerError(
                message,
                offendingParserRuleContext,
                allParserRuleContexts);

        this.compilerErrors.add(compilerError);
    }

    public ImmutableList<CompilerError> getCompilerErrors()
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
