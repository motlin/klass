package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class PackageableElement extends NamedElement
{
    @Nonnull
    private final String packageName;

    protected PackageableElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull String packageName)
    {
        super(elementContext, nameContext, name);
        this.packageName = Objects.requireNonNull(packageName);
    }

    @Nonnull
    public final String getPackageName()
    {
        return this.packageName;
    }

    public abstract static class PackageableElementBuilder extends NamedElementBuilder
    {
        // TODO: package context instead of package name?
        @Nonnull
        protected final String packageName;

        protected PackageableElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull String packageName)
        {
            super(elementContext, nameContext, name);
            this.packageName = Objects.requireNonNull(packageName);
        }
    }
}
