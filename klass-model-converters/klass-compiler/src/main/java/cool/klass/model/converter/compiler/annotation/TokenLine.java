
package cool.klass.model.converter.compiler.annotation;

import java.util.Objects;

import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.ImmutableList;

public class TokenLine
{
    private final int                  line;
    private final ImmutableList<Token> tokens;

    public TokenLine(int line, ImmutableList<Token> tokens)
    {
        this.line   = line;
        this.tokens = Objects.requireNonNull(tokens);
    }

    public int getLine()
    {
        return this.line;
    }

    public ImmutableList<Token> getTokens()
    {
        return this.tokens;
    }

    @Override
    public String toString()
    {
        return String.format("%2d: %s", this.line, this.tokens.collect(Token::getText).makeString(""));
    }
}
