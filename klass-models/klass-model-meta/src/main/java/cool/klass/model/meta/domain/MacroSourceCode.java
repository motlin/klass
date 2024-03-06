package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.source.SourceCode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;

public final class MacroSourceCode
        extends AbstractSourceCode
{
    @Nonnull
    private final SourceCode macroSourceCode;

    private MacroSourceCode(
            @Nonnull String sourceName,
            @Nonnull String sourceCodeText,
            @Nonnull TokenStream tokenStream,
            @Nonnull ParserRuleContext parserContext,
            @Nonnull SourceCode macroSourceCode)
    {
        super(sourceName, sourceCodeText, tokenStream, parserContext);
        this.macroSourceCode = Objects.requireNonNull(macroSourceCode);
    }

    @Nonnull
    public SourceCode getMacroSourceCode()
    {
        return this.macroSourceCode;
    }

    public static final class MacroSourceCodeBuilder
            extends AbstractSourceCodeBuilder
    {
        private final SourceCodeBuilder macroSourceCodeBuilder;
        private       MacroSourceCode   macroSourceCode;

        public MacroSourceCodeBuilder(
                @Nonnull String sourceName,
                @Nonnull String sourceCodeText,
                @Nonnull TokenStream tokenStream,
                @Nonnull ParserRuleContext parserContext,
                @Nonnull SourceCodeBuilder macroSourceCodeBuilder)
        {
            super(sourceName, sourceCodeText, tokenStream, parserContext);
            this.macroSourceCodeBuilder = Objects.requireNonNull(macroSourceCodeBuilder);
        }

        @Override
        public SourceCode build()
        {
            if (this.macroSourceCode == null)
            {
                this.macroSourceCode = new MacroSourceCode(
                        this.sourceName,
                        this.sourceCodeText,
                        this.tokenStream,
                        this.parserContext,
                        this.macroSourceCodeBuilder.build());
            }
            return this.macroSourceCode;
        }
    }
}
