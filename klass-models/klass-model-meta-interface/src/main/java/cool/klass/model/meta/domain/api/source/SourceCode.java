package cool.klass.model.meta.domain.api.source;

import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public interface SourceCode
        extends Function<Token, Optional<TokenCategory>>
{
    @Nonnull
    String getSourceName();

    @Nonnull
    String getSourceCodeText();

    @Nonnull
    BufferedTokenStream getTokenStream();

    @Nonnull
    ParserRuleContext getParserContext();

    @Nonnull
    Optional<SourceCode> getMacroSourceCode();

    Optional<TokenCategory> getTokenCategory(Token token);

    @Override
    default Optional<TokenCategory> apply(Token token)
    {
        return this.getTokenCategory(token);
    }

    interface SourceCodeBuilder
    {
        SourceCode build();
    }
}
