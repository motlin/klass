package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import cool.klass.model.meta.domain.api.source.SourceCode;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.map.MapIterable;

public final class SourceCodeImpl
        implements SourceCode
{
    @Nonnull
    private final String                            sourceName;
    @Nonnull
    private final String                            sourceCodeText;
    @Nonnull
    private final BufferedTokenStream               tokenStream;
    @Nonnull
    private final ParserRuleContext                 parserContext;
    @Nonnull
    private final MapIterable<Token, TokenCategory> tokenCategoriesFromLexer;
    @Nonnull
    private final MapIterable<Token, TokenCategory> tokenCategoriesFromParser;
    @Nonnull
    private final Optional<SourceCode>              macroSourceCode;

    public SourceCodeImpl(
            @Nonnull String sourceName,
            @Nonnull String sourceCodeText,
            @Nonnull BufferedTokenStream tokenStream,
            @Nonnull ParserRuleContext parserContext,
            @Nonnull Optional<SourceCode> macroSourceCode,
            @Nonnull MapIterable<Token, TokenCategory> tokenCategoriesFromLexer,
            @Nonnull MapIterable<Token, TokenCategory> tokenCategoriesFromParser)
    {
        this.sourceName                = Objects.requireNonNull(sourceName);
        this.sourceCodeText            = Objects.requireNonNull(sourceCodeText);
        this.tokenStream               = Objects.requireNonNull(tokenStream);
        this.parserContext             = Objects.requireNonNull(parserContext);
        this.macroSourceCode           = Objects.requireNonNull(macroSourceCode);
        this.tokenCategoriesFromLexer  = Objects.requireNonNull(tokenCategoriesFromLexer);
        this.tokenCategoriesFromParser = Objects.requireNonNull(tokenCategoriesFromParser);
    }

    @Override
    @Nonnull
    public String getSourceName()
    {
        return this.sourceName;
    }

    @Override
    @Nonnull
    public String getSourceCodeText()
    {
        return this.sourceCodeText;
    }

    @Override
    @Nonnull
    public BufferedTokenStream getTokenStream()
    {
        return this.tokenStream;
    }

    @Override
    @Nonnull
    public ParserRuleContext getParserContext()
    {
        return this.parserContext;
    }

    @Nonnull
    @Override
    public Optional<SourceCode> getMacroSourceCode()
    {
        return this.macroSourceCode;
    }

    @Override
    public Optional<TokenCategory> getTokenCategory(Token token)
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
        if (parserCategory != null)
        {
            return Optional.of(parserCategory);
        }
        return Optional.empty();
    }

    public static final class SourceCodeBuilderImpl
            implements SourceCodeBuilder
    {
        @Nonnull
        private final String                            sourceName;
        @Nonnull
        private final String                            sourceCodeText;
        @Nonnull
        private final BufferedTokenStream               tokenStream;
        @Nonnull
        private final ParserRuleContext                 parserContext;
        @Nonnull
        private final Optional<SourceCodeBuilder>       macroSourceCodeBuilder;
        @Nonnull
        private final MapIterable<Token, TokenCategory> tokenCategoriesFromLexer;
        @Nonnull
        private final MapIterable<Token, TokenCategory> tokenCategoriesFromParser;

        private SourceCode sourceCode;

        public SourceCodeBuilderImpl(
                @Nonnull String sourceName,
                @Nonnull String sourceCodeText,
                @Nonnull BufferedTokenStream tokenStream,
                @Nonnull ParserRuleContext parserContext,
                @Nonnull Optional<SourceCodeBuilder> macroSourceCodeBuilder,
                @Nonnull MapIterable<Token, TokenCategory> tokenCategoriesFromLexer,
                @Nonnull MapIterable<Token, TokenCategory> tokenCategoriesFromParser)
        {
            this.sourceName                = Objects.requireNonNull(sourceName);
            this.sourceCodeText            = Objects.requireNonNull(sourceCodeText);
            this.tokenStream               = Objects.requireNonNull(tokenStream);
            this.parserContext             = Objects.requireNonNull(parserContext);
            this.macroSourceCodeBuilder    = Objects.requireNonNull(macroSourceCodeBuilder);
            this.tokenCategoriesFromLexer  = Objects.requireNonNull(tokenCategoriesFromLexer);
            this.tokenCategoriesFromParser = Objects.requireNonNull(tokenCategoriesFromParser);
        }

        @Override
        public SourceCode build()
        {
            if (this.sourceCode == null)
            {
                this.sourceCode = new SourceCodeImpl(
                        this.sourceName,
                        this.sourceCodeText,
                        this.tokenStream,
                        this.parserContext,
                        this.macroSourceCodeBuilder.map(SourceCodeBuilder::build),
                        this.tokenCategoriesFromLexer,
                        this.tokenCategoriesFromParser);
            }
            return this.sourceCode;
        }
    }
}
