package cool.klass.model.converter.compiler.syntax.highlighter;

import java.util.Optional;

public class Ansi8BitColor
        implements Color
{
    private final int foreground;

    public Ansi8BitColor(int foreground)
    {
        this.foreground = foreground;
    }

    @Override
    public String getBefore()
    {
        return String.format("[38;5;%dm", this.foreground);
    }

    @Override
    public Optional<String> getAfter()
    {
        return Optional.empty();
    }
}
