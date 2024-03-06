package cool.klass.model.converter.compiler.syntax.highlighter.ansi;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.syntax.highlighter.ansi.scheme.AnsiColorScheme;
import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.map.MapIterable;
import org.fusesource.jansi.Ansi;

public final class AnsiTokenColorizer
{
    @Nonnull
    private final AnsiColorScheme                   colorScheme;
    @Nonnull
    private final MapIterable<Token, TokenCategory> tokenCategoriesFromParser;
    @Nonnull
    private final MapIterable<Token, TokenCategory> tokenCategoriesFromLexer;

    public AnsiTokenColorizer(
            @Nonnull AnsiColorScheme colorScheme,
            @Nonnull MapIterable<Token, TokenCategory> tokenCategoriesFromParser,
            @Nonnull MapIterable<Token, TokenCategory> tokenCategoriesFromLexer)
    {
        this.colorScheme               = Objects.requireNonNull(colorScheme);
        this.tokenCategoriesFromParser = Objects.requireNonNull(tokenCategoriesFromParser);
        this.tokenCategoriesFromLexer  = Objects.requireNonNull(tokenCategoriesFromLexer);
    }

    @Nonnull
    public void colorizeText(Ansi ansi, Token token)
    {
        Optional<TokenCategory> tokenCategory = this.getTokenCategory(token);
        tokenCategory.ifPresent(justTokenCategory -> TokenCategoryToAnsiColor.applyColor(justTokenCategory, ansi, this.colorScheme));
        ansi.a(token.getText());
    }

    private Optional<TokenCategory> getTokenCategory(Token token)
    {
        TokenCategory lexerCategory  = this.tokenCategoriesFromLexer.get(token);
        TokenCategory parserCategory = this.tokenCategoriesFromParser.get(token);
        if (lexerCategory != null && parserCategory != null)
        {
            throw new AssertionError(token);
        }
        if (lexerCategory != null)
        {
            return Optional.of(lexerCategory);
        }
        return Optional.ofNullable(parserCategory);
    }
}
