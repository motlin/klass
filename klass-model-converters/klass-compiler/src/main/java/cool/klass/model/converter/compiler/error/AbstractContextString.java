package cool.klass.model.converter.compiler.error;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;

import static org.fusesource.jansi.Ansi.ansi;

public abstract class AbstractContextString
{
    private final int    line;
    @Nonnull
    private final String string;

    protected AbstractContextString(int line, @Nonnull String string)
    {
        this.line   = line;
        this.string = Objects.requireNonNull(string);
    }

    private static String padLeft(String string, int width)
    {
        return String.format("%" + width + "s║", string);
    }

    public int getLine()
    {
        return this.line;
    }

    public String toString(int lineNumberWidth)
    {
        MutableList<String> strings = ArrayAdapter.adapt(this.string.split("\n"));
        return strings
                .collectWithIndex((string, index) -> this.toString(string, index, lineNumberWidth))
                .makeString("\n");
    }

    private String toString(String string, int offset, int lineNumberWidth)
    {
        String lineNumberString       = this.getLineNumberString(this.line + offset);
        String paddedLineNumberString = AbstractContextString.padLeft(lineNumberString, lineNumberWidth);
        return ansi().fgDefault().a(paddedLineNumberString).a(" ").a(string).toString();
    }

    @Override
    public String toString()
    {
        return this.toString(4);
    }

    @Nonnull
    protected abstract String getLineNumberString(int line);
}
