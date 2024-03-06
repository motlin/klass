package cool.klass.model.meta.domain;

import java.util.Objects;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class PackageableElement extends NamedElement
{
    private final String packageName;

    protected PackageableElement(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            String packageName)
    {
        super(elementContext, nameContext, name);
        this.packageName = Objects.requireNonNull(packageName);
    }

    public final String getPackageName()
    {
        return this.packageName;
    }

    public abstract static class PackageableElementBuilder extends NamedElementBuilder
    {
        // TODO: package context instead of package name?
        protected final String packageName;

        protected PackageableElementBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                String packageName)
        {
            super(elementContext, nameContext, name);
            this.packageName = Objects.requireNonNull(packageName);
        }
    }
}
