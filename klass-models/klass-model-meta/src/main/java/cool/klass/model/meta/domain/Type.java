package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Implement Classifiers, which are Types that support inheritance, specifically Class/Klass and Interface
public abstract class Type extends PackageableElement
{
    protected Type(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull String packageName)
    {
        super(elementContext, nameContext, name, packageName);
    }

    public abstract static class TypeBuilder<T extends Type> extends PackageableElementBuilder
    {
        protected TypeBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull String packageName)
        {
            super(elementContext, nameContext, name, packageName);
        }
    }
}
