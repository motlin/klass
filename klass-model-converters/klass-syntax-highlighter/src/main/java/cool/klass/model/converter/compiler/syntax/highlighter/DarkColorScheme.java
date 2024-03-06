package cool.klass.model.converter.compiler.syntax.highlighter;

import java.util.Optional;

public enum DarkColorScheme
        implements ColorScheme
{
    INSTANCE;

    @Override
    public Color getBackground()
    {
        return new Ansi8BitColor(0)
        {
            @Override
            public String getBefore()
            {
                return "[48;5;232m";
            }

            @Override
            public Optional<String> getAfter()
            {
                return Optional.of("[0m");
            }
        };
    }

    @Override
    public Color getBlockComment()
    {
        return new Ansi8BitColor(244);
    }

    @Override
    public Color getKeyword()
    {
        // 91
        return new Ansi8BitColor(127);
    }

    @Override
    public Color getIdentifier()
    {
        return new Ansi8BitColor(255);
    }

    @Override
    public Color getPackageName()
    {
        return new Ansi8BitColor(124);
    }

    @Override
    public Color getTopLevelElementName()
    {
        return new Ansi8BitColor(124);
    }

    @Override
    public Color getInterfaceName()
    {
        return new Ansi8BitColor(38);
    }

    @Override
    public Color getEnumerationLiteralName()
    {
        return new Ansi8BitColor(20)
        {
            @Override
            public String getBefore()
            {
                return String.format("[3;38;5;%dm", 20);
            }

            @Override
            public Optional<String> getAfter()
            {
                return Optional.of("[23m");
            }
        };
    }

    @Override
    public Color getParameterName()
    {
        return new Ansi8BitColor(172);
    }

    @Override
    public Color getPropertyName()
    {
        return new Ansi8BitColor(105);
    }

    @Override
    public Color getParameterizedPropertyName()
    {
        return new Ansi8BitColor(28);
    }

    @Override
    public Color getAssociationEndName()
    {
        return new Ansi8BitColor(28);
    }

    @Override
    public Color getLiteral()
    {
        return new Ansi8BitColor(21);
    }

    @Override
    public Color getStringLiteral()
    {
        return new Ansi8BitColor(28);
    }

    @Override
    public Color getPunctuation()
    {
        return new Ansi8BitColor(250);
    }

    @Override
    public Color getComma()
    {
        return new Ansi8BitColor(89);
    }

    @Override
    public Color getSemi()
    {
        //250
        return new Ansi8BitColor(236);
    }
}
