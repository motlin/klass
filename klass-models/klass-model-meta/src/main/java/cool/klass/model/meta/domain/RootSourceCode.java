package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.api.source.SourceCode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;

public final class RootSourceCode
        extends AbstractSourceCode
{
    private RootSourceCode(
            String sourceName,
            String sourceCodeText,
            TokenStream tokenStream,
            ParserRuleContext parserContext)
    {
        super(sourceName, sourceCodeText, tokenStream, parserContext);
    }

    public static final class RootSourceCodeBuilder
            extends AbstractSourceCodeBuilder
    {
        private RootSourceCode rootSourceCode;

        public RootSourceCodeBuilder(
                String sourceName,
                String sourceCodeText,
                TokenStream tokenStream,
                ParserRuleContext parserContext)
        {
            super(sourceName, sourceCodeText, tokenStream, parserContext);
        }

        @Override
        public SourceCode build()
        {
            if (this.rootSourceCode == null)
            {
                this.rootSourceCode = new RootSourceCode(
                        this.sourceName,
                        this.sourceCodeText,
                        this.tokenStream,
                        this.parserContext);
            }
            return this.rootSourceCode;
        }
    }
}
