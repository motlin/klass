package cool.klass.model.converter.compiler.state;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.EnumerationLiteral.EnumerationLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrEnumerationLiteral extends AntlrNamedElement
{
    public static final AntlrEnumerationLiteral AMBIGUOUS = new AntlrEnumerationLiteral(
            new ParserRuleContext(),
            null,
            "ambiguous enumeration literal",
            new ParserRuleContext(),
            null,
            null);
    public static final AntlrEnumerationLiteral NOT_FOUND = new AntlrEnumerationLiteral(
            new ParserRuleContext(),
            null,
            "not found enumeration literal",
            new ParserRuleContext(),
            null,
            null);

    private final String           prettyName;
    private final AntlrEnumeration owningEnumeration;

    private EnumerationLiteralBuilder enumerationLiteralBuilder;

    public AntlrEnumerationLiteral(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            String name,
            ParserRuleContext nameContext,
            String prettyName,
            AntlrEnumeration owningEnumeration)
    {
        super(elementContext, compilationUnit, name, nameContext);
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

    @Override
    public EnumerationLiteralContext getElementContext()
    {
        return (EnumerationLiteralContext) super.getElementContext();
    }

    public void reportDuplicateName(CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_ENM: Duplicate enumeration literal: '%s'.", this.name);
        compilerErrorHolder.add(
                this.compilationUnit,
                message,
                this.elementContext,
                this.owningEnumeration.getElementContext());
    }

    public void reportDuplicatePrettyName(CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_LIT: Duplicate enumeration pretty name: '%s'.", this.prettyName);
        compilerErrorHolder.add(
                this.compilationUnit,
                message,
                this.getElementContext().enumerationPrettyName(),
                this.owningEnumeration.getElementContext());
    }
}
