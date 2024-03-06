package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.source.SourceCode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;

public class AbstractSourceCode
        implements SourceCode
{
    @Nonnull
    protected final String            sourceName;
    @Nonnull
    protected final String            sourceCodeText;
    @Nonnull
    protected final TokenStream       tokenStream;
    @Nonnull
    protected final ParserRuleContext parserContext;

    protected AbstractSourceCode(
            @Nonnull String sourceName,
            @Nonnull String sourceCodeText,
            @Nonnull TokenStream tokenStream,
            @Nonnull ParserRuleContext parserContext)
    {
        this.sourceName     = Objects.requireNonNull(sourceName);
        this.sourceCodeText = Objects.requireNonNull(sourceCodeText);
        this.tokenStream    = Objects.requireNonNull(tokenStream);
        this.parserContext  = Objects.requireNonNull(parserContext);
    }

    protected abstract static class AbstractSourceCodeBuilder
            implements SourceCodeBuilder
    {
        @Nonnull
        protected final String            sourceName;
        @Nonnull
        protected final String            sourceCodeText;
        @Nonnull
        protected final TokenStream       tokenStream;
        @Nonnull
        protected final ParserRuleContext parserContext;

        protected AbstractSourceCodeBuilder(
                @Nonnull String sourceName,
                @Nonnull String sourceCodeText,
                @Nonnull TokenStream tokenStream,
                @Nonnull ParserRuleContext parserContext)
        {
            this.sourceName     = Objects.requireNonNull(sourceName);
            this.sourceCodeText = Objects.requireNonNull(sourceCodeText);
            this.tokenStream    = Objects.requireNonNull(tokenStream);
            this.parserContext  = Objects.requireNonNull(parserContext);
        }
    }
}
