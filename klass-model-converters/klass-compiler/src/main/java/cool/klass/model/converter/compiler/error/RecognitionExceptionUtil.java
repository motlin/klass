package cool.klass.model.converter.compiler.error;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

public final class RecognitionExceptionUtil
{
    private static final Pattern TAB_PATTERN       = Pattern.compile("\t");
    private static final Pattern START_END_PATTERN = Pattern.compile("(?m)^|$");
    private static final Pattern NEWLINE_PATTERN   = Pattern.compile("\r?\n");

    private RecognitionExceptionUtil()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static String formatVerbose(RecognitionException e)
    {
        return String.format(
                "ERROR on line %s:%s => %s%nrule stack: %s%noffending token %s => %s%n%s",
                RecognitionExceptionUtil.getLineNumberString(e),
                RecognitionExceptionUtil.getCharPositionInLineString(e),
                e.getMessage(),
                RecognitionExceptionUtil.getRuleStackString(e),
                RecognitionExceptionUtil.getOffendingTokenString(e),
                RecognitionExceptionUtil.getOffendingTokenVerboseString(e),
                START_END_PATTERN.matcher(RecognitionExceptionUtil.getErrorLineStringUnderlined(e)).replaceAll("|"));
    }

    public static String getRuleStackString(RecognitionException e)
    {
        if (e == null || e.getRecognizer() == null
            || e.getCtx() == null
            || e.getRecognizer().getRuleNames() == null)
        {
            return "";
        }
        List<String> stack = ((Parser) e.getRecognizer()).getRuleInvocationStack(e.getCtx());
        Collections.reverse(stack);
        return stack.toString();
    }

    public static String getLineNumberString(RecognitionException e)
    {
        if (e == null || e.getOffendingToken() == null)
        {
            return "";
        }
        return String.format("%d", e.getOffendingToken().getLine());
    }

    public static String getCharPositionInLineString(RecognitionException e)
    {
        if (e == null || e.getOffendingToken() == null)
        {
            return "";
        }
        return String.format("%d", e.getOffendingToken().getCharPositionInLine());
    }

    public static String getOffendingTokenString(RecognitionException e)
    {
        if (e == null || e.getOffendingToken() == null)
        {
            return "";
        }
        return e.getOffendingToken().toString();
    }

    public static String getOffendingTokenVerboseString(RecognitionException e)
    {
        Token offendingToken = e.getOffendingToken();
        if (offendingToken == null)
        {
            return "";
        }
        Recognizer<?, ?> recognizer = e.getRecognizer();
        return RecognitionExceptionUtil.getOffendingTokenVerboseString(offendingToken, recognizer);
    }

    private static String getOffendingTokenVerboseString(Token offendingToken, Recognizer<?, ?> recognizer)
    {
        return String.format(
                "at tokenStream[%d], inputString[%d..%d] = '%s', tokenType<%d> = %s, on line %d, character %d",
                offendingToken.getTokenIndex(),
                offendingToken.getStartIndex(),
                offendingToken.getStopIndex(),
                offendingToken.getText(),
                offendingToken.getType(),
                recognizer.getTokenNames()[offendingToken.getType()],
                offendingToken.getLine(),
                offendingToken.getCharPositionInLine());
    }

    public static String getErrorLineStringUnderlined(RecognitionException e)
    {
        Token offendingToken = e.getOffendingToken();
        String errorLine = RecognitionExceptionUtil.getErrorLineString(e);
        if (errorLine.isEmpty())
        {
            return errorLine;
        }
        return RecognitionExceptionUtil.getErrorLineStringUnderlined(offendingToken, errorLine);
    }

    public static String getErrorLineString(RecognitionException e)
    {
        Token offendingToken = e.getOffendingToken();
        if (e == null || e.getRecognizer() == null
            || e.getRecognizer().getInputStream() == null
            || offendingToken == null)
        {
            return "";
        }
        TokenStream tokens = (TokenStream) e.getRecognizer().getInputStream();
        String      input  = tokens.getTokenSource().getInputStream().toString();
        String[]    lines  = NEWLINE_PATTERN.split(input);
        return lines[offendingToken.getLine() - 1];
    }

    public static String getErrorLineStringUnderlined(Token offendingToken, String errorLine)
    {
        // replace tabs with single space so that charPositionInLine gives us the column to start underlining.
        String        errorLineWithoutTabs = TAB_PATTERN.matcher(errorLine).replaceAll(" ");
        String        formatString         = "%" + errorLineWithoutTabs.length() + 's';
        StringBuilder underLine            = new StringBuilder(String.format(formatString, ""));
        int           start                = offendingToken.getStartIndex();
        int           stop                 = offendingToken.getStopIndex();
        if (start >= 0 && stop >= 0)
        {
            for (int i = 0; i <= (stop - start); i++)
            {
                underLine.setCharAt(offendingToken.getCharPositionInLine() + i, '^');
            }
        }
        return underLine.toString();
    }
}
