package cool.klass.model.converter.compiler.state;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.EnumerationLiteral.EnumerationLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrEnumerationLiteral extends AntlrNamedElement
{
    @Nonnull
    public static final AntlrEnumerationLiteral AMBIGUOUS = new AntlrEnumerationLiteral(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(), "ambiguous enumeration literal",
            -1,
            null,
            null);
    @Nonnull
    public static final AntlrEnumerationLiteral NOT_FOUND = new AntlrEnumerationLiteral(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(), "not found enumeration literal",
            -1,
            null,
            null);

    private final String prettyName;
    private final AntlrEnumeration owningEnumeration;

    private EnumerationLiteralBuilder enumerationLiteralBuilder;

    public AntlrEnumerationLiteral(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            String prettyName,
            AntlrEnumeration owningEnumeration)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.prettyName = prettyName;
        this.owningEnumeration = owningEnumeration;
    }

    public String getPrettyName()
    {
        return this.prettyName;
    }

    public EnumerationLiteralBuilder build()
    {
        if (this.enumerationLiteralBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.enumerationLiteralBuilder = new EnumerationLiteralBuilder(
                this.getElementContext(),
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.prettyName,
                this.owningEnumeration.getEnumerationBuilder());
        return this.enumerationLiteralBuilder;
    }

    @Nonnull
    @Override
    public EnumerationLiteralContext getElementContext()
    {
        return (EnumerationLiteralContext) super.getElementContext();
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        this.reportKeywordCollision(compilerErrorHolder);

        if (!CONSTANT_NAME_PATTERN.matcher(this.name).matches())
        {
            String message = String.format(
                    "ERR_LIT_NME: Name must match pattern %s but was %s",
                    CONSTANT_NAME_PATTERN,
                    this.name);
            compilerErrorHolder.add(
                    message,
                    this.nameContext,
                    this.owningEnumeration.getElementContext());
        }
    }

    public void reportDuplicateName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_ENM: Duplicate enumeration literal: '%s'.", this.name);
        compilerErrorHolder.add(
                message,
                this.elementContext,
                this.owningEnumeration.getElementContext());
    }

    public void reportDuplicatePrettyName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_LIT: Duplicate enumeration pretty name: '%s'.", this.prettyName);
        compilerErrorHolder.add(
                message,
                this.getElementContext().enumerationPrettyName(),
                this.owningEnumeration.getElementContext());
    }
}
