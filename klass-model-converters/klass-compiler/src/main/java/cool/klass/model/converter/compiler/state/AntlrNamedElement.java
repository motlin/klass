package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public abstract class AntlrNamedElement extends AntlrElement
{
    protected static final Pattern PACKAGE_NAME_PATTERN  = Pattern.compile("^[a-z]+(\\.[a-z][a-z0-9]*)*$");
    protected static final Pattern TYPE_NAME_PATTERN     = Pattern.compile("^[A-Z][a-zA-Z0-9]*$");
    protected static final Pattern MEMBER_NAME_PATTERN   = Pattern.compile("^[a-z][a-zA-Z0-9]*$");
    protected static final Pattern CONSTANT_NAME_PATTERN = Pattern.compile("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");

    protected static final ImmutableList<String> JAVA_KEYWORDS = Lists.immutable.with(
            "abstract",
            "assert",
            "boolean",
            "break",
            "byte",
            "case",
            "catch",
            "char",
            "class",
            "const",
            "continue",
            "default",
            "do",
            "double",
            "else",
            "enum",
            "extends",
            "final",
            "finally",
            "float",
            "for",
            "goto",
            "if",
            "implements",
            "import",
            "instanceof",
            "int",
            "interface",
            "long",
            "native",
            "new",
            "package",
            "private",
            "protected",
            "public",
            "return",
            "short",
            "static",
            "strictfp",
            "super",
            "switch",
            "synchronized",
            "this",
            "throw",
            "throws",
            "transient",
            "try",
            "var",
            "void",
            "volatile",
            "while");

    protected static final ImmutableList<String> JAVA_LITERALS = Lists.immutable.with("true", "false", "null");

    protected static final ImmutableList<String> RELADOMO_TYPES =
            Lists.immutable.with("OrderBy");

    @Nonnull
    protected final ParserRuleContext nameContext;
    @Nonnull
    protected final String            name;
    protected final int               ordinal;

    protected AntlrNamedElement(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, inferred);
        this.name = Objects.requireNonNull(name);
        this.nameContext = Objects.requireNonNull(nameContext);
        this.ordinal = ordinal;
    }

    @Nonnull
    public ParserRuleContext getNameContext()
    {
        return this.nameContext;
    }

    @Nonnull
    public String getName()
    {
        return this.name;
    }

    // TODO: 💡 Some name errors should really just be warnings. Rename CompilerError to CompilerAnnotation and implement severity.
    public abstract void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder);

    protected void reportKeywordCollision(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            ParserRuleContext... parserRuleContexts)
    {
        if (JAVA_KEYWORDS.contains(this.name))
        {
            String message = String.format(
                    "ERR_NME_KEY: '%s' is a reserved Java keyword.",
                    this.name);
            compilerErrorHolder.add(
                    message,
                    this.nameContext,
                    parserRuleContexts);
        }

        if (JAVA_LITERALS.contains(this.name))
        {
            String message = String.format(
                    "ERR_NME_LIT: '%s' is a reserved Java literal.",
                    this.name);
            compilerErrorHolder.add(
                    message,
                    this.nameContext,
                    parserRuleContexts);
        }
    }
}
