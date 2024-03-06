package cool.klass.model.meta.domain;

import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Implement Classifiers, which are Types that support inheritance, specifically Class/Klass and Interface
public abstract class Type extends PackageableElement
{
    protected Type(String name, String packageName)
    {
        super(name, packageName);
    }

    public abstract static class TypeBuilder extends PackageableElementBuilder
    {
        protected TypeBuilder(ParserRuleContext elementContext, ParserRuleContext nameContext, String packageName)
        {
            super(elementContext, nameContext, packageName);
        }
    }
}
