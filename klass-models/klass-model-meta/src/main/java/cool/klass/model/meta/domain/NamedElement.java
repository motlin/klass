package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class NamedElement extends Element
{
    private final String name;

    protected NamedElement(String name)
    {
        this.name = name;
    }

    public final String getName()
    {
        return this.name;
    }

    public abstract static class NamedElementBuilder extends ElementBuilder
    {
        // TODO: Move these to an antlr listener
        protected static final Pattern TYPE_NAME_PATTERN = Pattern.compile("^[A-Z][a-zA-Z0-9]*$");
        protected static final Pattern MEMBER_NAME_PATTERN = Pattern.compile("^[a-z][a-zA-Z0-9]*$");
        protected static final Pattern CONSTANT_NAME_PATTERN = Pattern.compile("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");

        protected final ParserRuleContext nameContext;

        protected NamedElementBuilder(ParserRuleContext elementContext, ParserRuleContext nameContext)
        {
            super(elementContext);
            this.nameContext = Objects.requireNonNull(nameContext);
        }
    }
}
