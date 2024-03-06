package cool.klass.model.converter.compiler.state;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.annotation.CompilerAnnotationHolder;
import cool.klass.model.meta.domain.api.TopLevelElement.TopLevelElementBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

public interface AntlrTopLevelElement
        extends IAntlrElement
{
    @Override
    default Pair<Token, Token> getContextBefore()
    {
        return Tuples.pair(this.getElementContext().getStart(), this.getBodyContext().getStart());
    }

    @Nonnull
    @Override
    default Pair<Token, Token> getContextAfter()
    {
        Token token = this.getElementContext().getStop();
        return Tuples.pair(token, token);
    }

    default ParserRuleContext getBodyContext()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getBodyContext() not implemented yet");
    }

    @Nonnull
    TopLevelElementBuilder getElementBuilder();

    @Override
    @Nonnull
    default Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.empty();
    }

    @Override
    default boolean isContext()
    {
        return true;
    }

    void reportErrors(@Nonnull CompilerAnnotationHolder compilerAnnotationHolder);

    void reportNameErrors(@Nonnull CompilerAnnotationHolder compilerAnnotationHolder);

    default void reportDuplicateTopLevelName(@Nonnull CompilerAnnotationHolder compilerAnnotationHolder)
    {
        String message = String.format("Duplicate top level item name: '%s'.", this.getName());
        compilerAnnotationHolder.add("ERR_DUP_TOP", message, this);
    }

    @Nonnull
    String getName();

    int getOrdinal();
}
