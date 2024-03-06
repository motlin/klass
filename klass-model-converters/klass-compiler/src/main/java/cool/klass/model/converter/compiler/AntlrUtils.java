package cool.klass.model.converter.compiler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.antlr.v4.runtime.ParserRuleContext;

public final class AntlrUtils
{
    private AntlrUtils()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    @Nullable
    public static <T> T getParentOfType(@Nonnull ParserRuleContext ctx, @Nonnull Class<T> aClass /* klass? */)
    {
        if (aClass.isInstance(ctx))
        {
            return aClass.cast(ctx);
        }

        ParserRuleContext parent = ctx.getParent();
        if (parent == null)
        {
            return null;
        }

        return getParentOfType(parent, aClass);
    }
}
