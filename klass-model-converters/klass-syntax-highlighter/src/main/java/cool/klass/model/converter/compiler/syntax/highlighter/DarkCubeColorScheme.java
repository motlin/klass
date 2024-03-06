package cool.klass.model.converter.compiler.syntax.highlighter;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

public enum DarkCubeColorScheme
        implements ColorScheme
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
        ansi.fg(246);
    }

    @Override
    public void keyword(Ansi ansi)
    {
        ansi.fg(166);
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
        ansi.fg(162);
    }

    @Override
    public void propertyName(Ansi ansi)
    {
        ansi.fg(134);
    }

    @Override
    public void literal(Ansi ansi)
    {
        ansi.fg(74);
    }

    @Override
    public void punctuation(Ansi ansi)
    {
        ansi.fg(251);
    }

    @Override
    public void operator(Ansi ansi)
    {
        ansi.fg(Color.MAGENTA);
    }

    @Override
    public void urlConstant(Ansi ansi)
    {
        this.literal(ansi);
    }
}
