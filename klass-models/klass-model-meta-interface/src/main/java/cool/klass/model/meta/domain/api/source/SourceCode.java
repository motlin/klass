package cool.klass.model.meta.domain.api.source;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

public interface SourceCode
{
    @Nonnull
    String getSourceName();

    @Nonnull
    String getFullPathSourceName();

    @Nonnull
    String getSourceCodeText();

    @Nonnull
    BufferedTokenStream getTokenStream();

    @Nonnull
    ParserRuleContext getParserContext();

    @Nonnull
    Optional<SourceCode> getMacroSourceCode();

    interface SourceCodeBuilder
    {
        SourceCode build();
    }
}
