package cool.klass.model.converter.compiler;

import cool.klass.model.converter.compiler.error.RecognitionExceptionUtil;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class ThrowingErrorListener extends BaseErrorListener
{
    private final String classpathLocation;
    private final String[] lines;

    public ThrowingErrorListener(String classpathLocation, String[] lines)
    {
        this.classpathLocation = classpathLocation;
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
        String sourceLine = this.lines[line - 1];
        String error = String.format("%s[%d:%d] %s%n%s", this.classpathLocation, line, charPositionInLine, msg, sourceLine);
        String s = RecognitionExceptionUtil.formatVerbose(e);
        throw new ParseCancellationException(error);
    }
}
