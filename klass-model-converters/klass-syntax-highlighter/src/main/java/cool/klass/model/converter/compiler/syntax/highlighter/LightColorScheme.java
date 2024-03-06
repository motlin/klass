package cool.klass.model.converter.compiler.syntax.highlighter;

import java.util.Optional;

public enum LightColorScheme
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
                return "[48;5;255m";
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
        return new Ansi8BitColor(90);
    }

    @Override
    public Color getIdentifier()
    {
        return new Ansi8BitColor(0);
    }

    @Override
    public Color getPackageName()
    {
        return new Ansi8BitColor(88);
    }

    @Override
    public Color getTypeName()
    {
        return new Ansi8BitColor(88);
    }

    @Override
    public Color getInterfaceName()
    {
        //31
        //32
        return new Ansi8BitColor(31);
    }

    @Override
    public Color getEnumerationLiteralName()
    {
        return new Ansi8BitColor(18)
        {
            @Override
            public String getBefore()
            {
                return String.format("[3;38;5;%dm", 18);
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
        // 130
        // 166
        return new Ansi8BitColor(130);
    }

    @Override
    public Color getPropertyName()
    {
        return new Ansi8BitColor(63);
    }

    @Override
    public Color getParameterizedPropertyName()
    {
        return new Ansi8BitColor(22);
    }

    @Override
    public Color getAssociationEndName()
    {
        return new Ansi8BitColor(22);
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
        return new Ansi8BitColor(0);
    }

    @Override
    public Color getComma()
    {
        //163
        //200
        return new Ansi8BitColor(164);
    }

    @Override
    public Color getSemi()
    {
        //250
        return new Ansi8BitColor(251);
    }
}
