package cool.klass.model.converter.compiler;

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
        this.lines = lines;
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
        String sourceLine = getSourceLine(line);
        String error = String.format(
                "%s[%d:%d] %s%n%s",
                this.sourceName,
                line,
                charPositionInLine,
                msg,
                sourceLine);
        throw new ParseCancellationException(error);
    }

    public String getSourceLine(int line)
    {
        if (line == 1)
        {
            return this.lines[line - 1];
        }

        return this.lines[line - 2] + "\n" + this.lines[line - 1];
    }
}
