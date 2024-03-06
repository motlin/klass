package cool.klass.model.converter.compiler.state;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.tuple.Tuples;

public interface IAntlrElement
{
    @Nonnull
    ParserRuleContext getElementContext();

    @Nonnull
    Optional<AntlrElement> getMacroElement();

    default IAntlrElement getOuterElement()
    {
        return this.getSurroundingElement()
                .map(IAntlrElement::getOuterElement)
                .orElse(this);
    }

    @Nonnull
    ImmutableList<ParserRuleContext> getParserRuleContexts();

    void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts);

    @Nonnull
    Optional<IAntlrElement> getSurroundingElement();

    default <T extends IAntlrElement> Optional<T> getSurroundingElement(Class<T> elementClass)
    {
        if (elementClass.isInstance(this))
        {
            return Optional.of(elementClass.cast(this));
        }

        return this
                .getSurroundingElement()
                .flatMap(surroundingElement -> surroundingElement.getSurroundingElement(elementClass));
    }

    @Nonnull
    default ImmutableList<IAntlrElement> getSurroundingElements()
    {
        MutableList<IAntlrElement> result = Lists.mutable.empty();
        this.gatherSurroundingElements(result);
        return result.toImmutable();
    }

    @Nonnull
    default void gatherSurroundingElements(@Nonnull MutableList<IAntlrElement> result)
    {
        result.add(this);
        this.getSurroundingElement().ifPresent(element -> element.gatherSurroundingElements(result));
    }

    default boolean isContext()
    {
        return false;
    }

    @Nonnull
    Optional<CompilationUnit> getCompilationUnit();

    default Pair<Token, Token> getContextBefore()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getContextBefore() not implemented yet");
    }

    default Pair<Token, Token> getContextAfter()
    {
        // This makes the default implementation throw, but still not need overrides just to return null
        this.getContextBefore();
        return null;
    }

    default Pair<Token, Token> getEntireContext()
    {
        return Tuples.pair(this.getElementContext().getStart(), this.getElementContext().getStop());
    }

    default void reportAuditErrors(
            CompilerErrorState compilerErrorHolder,
            ListIterable<AntlrModifier> modifierStates,
            IAntlrElement element)
    {
        ImmutableList<AntlrModifier> offendingModifiers = modifierStates
                .select(modifier -> modifier.isAudit() || modifier.isUser())
                .toImmutable();
        if (offendingModifiers.isEmpty())
        {
            return;
        }

        String message = String.format(
                "Modifiers %s require one 'user' class in the domain model.",
                offendingModifiers);
        compilerErrorHolder.add(
                "ERR_ADT_MOD",
                message,
                element,
                offendingModifiers.collect(AntlrElement::getElementContext));
    }
}
