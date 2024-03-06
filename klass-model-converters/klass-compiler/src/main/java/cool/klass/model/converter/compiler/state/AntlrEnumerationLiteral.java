package cool.klass.model.converter.compiler.state;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.EnumerationLiteral.EnumerationLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrEnumerationLiteral extends AntlrNamedElement
{
    @Nullable
    public static final AntlrEnumerationLiteral AMBIGUOUS = new AntlrEnumerationLiteral(
            new ParserRuleContext(),
            null,
            true,
            "ambiguous enumeration literal",
            new ParserRuleContext(),
            null,
            null);
    @Nullable
    public static final AntlrEnumerationLiteral NOT_FOUND = new AntlrEnumerationLiteral(
            new ParserRuleContext(),
            null,
            true,
            "not found enumeration literal",
            new ParserRuleContext(),
            null,
            null);

    private final String           prettyName;
    private final AntlrEnumeration owningEnumeration;

    private EnumerationLiteralBuilder enumerationLiteralBuilder;

    public AntlrEnumerationLiteral(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull String name,
            @Nonnull ParserRuleContext nameContext,
            String prettyName,
            AntlrEnumeration owningEnumeration)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name);
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
                this.nameContext,
                this.name,
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

    public void reportDuplicateName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_ENM: Duplicate enumeration literal: '%s'.", this.name);
        compilerErrorHolder.add(
                this.compilationUnit,
                message,
                this.elementContext,
                this.owningEnumeration.getElementContext());
    }

    public void reportDuplicatePrettyName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_LIT: Duplicate enumeration pretty name: '%s'.", this.prettyName);
        compilerErrorHolder.add(
                this.compilationUnit,
                message,
                this.getElementContext().enumerationPrettyName(),
                this.owningEnumeration.getElementContext());
    }
}
