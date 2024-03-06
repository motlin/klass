package cool.klass.model.converter.compiler.syntax.highlighter.ansi.scheme;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

public enum DarkAnsiColorScheme
        implements AnsiColorScheme
{
    INSTANCE;

    @Override
    public void background(Ansi ansi)
    {
        ansi.bg(Color.BLACK);
    }

    @Override
    public void blockComment(Ansi ansi)
    {
        ansi.fg(Color.WHITE);
    }

    @Override
    public void keyword(Ansi ansi)
    {
        ansi.fg(Color.MAGENTA);
    }

    @Override
    public void verb(Ansi ansi)
    {
        ansi.fg(Color.GREEN);
    }

    @Override
    public void modifier(Ansi ansi)
    {
        ansi.fg(Color.GREEN);
    }

    @Override
    public void identifier(Ansi ansi)
    {
        ansi.fgDefault();
    }

    @Override
    public void literal(Ansi ansi)
    {
        ansi.fg(Color.BLUE);
    }

    @Override
    public void literalThis(Ansi ansi)
    {
        ansi.fg(Color.GREEN);
    }

    @Override
    public void literalNative(Ansi ansi)
    {
        ansi.fg(Color.GREEN);
    }

    @Override
    public void punctuation(Ansi ansi)
    {
        ansi.fg(Color.CYAN);
    }

    @Override
    public void operator(Ansi ansi)
    {
        ansi.fg(Color.MAGENTA);
    }
}
