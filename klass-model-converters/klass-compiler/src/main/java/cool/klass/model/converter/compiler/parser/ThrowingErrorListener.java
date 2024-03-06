package cool.klass.model.converter.compiler.parser;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class ThrowingErrorListener extends BaseErrorListener
{
    private final String   sourceName;
    private final String[] lines;

    public ThrowingErrorListener(String sourceName, String[] lines)
    {
        this.sourceName = sourceName;
        this.lines      = lines;
    }

    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e)
    {
        String sourceLine = this.getSourceLine(line);
        String error = String.format(
                "(%s:%d) %s %s[%d:%d]%n%s",
                this.getFilenameWithoutDirectory(),
                line,
                msg,
                this.sourceName,
                line,
                charPositionInLine,
                sourceLine);
        throw new ParseCancellationException(error);
    }

    @Nonnull
    private String getFilenameWithoutDirectory()
    {
        return this.sourceName.substring(this.sourceName.lastIndexOf('/') + 1);
    }

    private String getSourceLine(int line)
    {
        if (line == 1)
        {
            return this.lines[0];
        }

        return this.lines[line - 2] + "\n" + this.lines[line - 1];
    }
}
