package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.PackageableElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractPackageableElement
        extends AbstractIdentifierElement
        implements PackageableElementWithSourceCode
{
    @Nonnull
    private final String packageName;

    protected AbstractPackageableElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull String packageName)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext);
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
            extends IdentifierElementBuilder<BuiltElement>
    {
        // TODO: package context instead of package name?
        @Nonnull
        protected final String packageName;

        protected PackageableElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull String packageName)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext);
            this.packageName = Objects.requireNonNull(packageName);
        }
    }
}
