package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.PackageableElement;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractPackageableElement extends AbstractNamedElement implements PackageableElement
{
    @Nonnull
    private final String packageName;

    protected AbstractPackageableElement(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName)
    {
        super(elementContext, macroElement, nameContext, name, ordinal);
        this.packageName = Objects.requireNonNull(packageName);
    }

    @Override
    @Nonnull
    public final String getPackageName()
    {
        return this.packageName;
    }

    @Override
    @Nonnull
    public String getFullyQualifiedName()
    {
        return this.packageName + '.' + this.getName();
    }

    public abstract static class PackageableElementBuilder<BuiltElement extends AbstractPackageableElement>
            extends NamedElementBuilder<BuiltElement>
    {
        // TODO: package context instead of package name?
        @Nonnull
        protected final String packageName;

        protected PackageableElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName)
        {
            super(elementContext, macroElement, nameContext, name, ordinal);
            this.packageName = Objects.requireNonNull(packageName);
        }
    }
}
