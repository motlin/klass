package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class PackageableElement extends NamedElement
{
    private final String packageName;

    protected PackageableElement(String name, String packageName)
    {
        super(name);
        this.packageName = packageName;
    }

    public final String getPackageName()
    {
        return this.packageName;
    }

    public abstract static class PackageableElementBuilder extends NamedElementBuilder
    {
        // TODO
        protected static final Pattern PACKAGE_NAME_PATTERN = Pattern.compile("^[a-z]+(\\.[a-z][a-z0-9]*)*$");

        // TODO: package context instead of package name?
        protected final String packageName;

        protected PackageableElementBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String packageName)
        {
            super(elementContext, nameContext);
            this.packageName = Objects.requireNonNull(packageName);
        }
    }
}
