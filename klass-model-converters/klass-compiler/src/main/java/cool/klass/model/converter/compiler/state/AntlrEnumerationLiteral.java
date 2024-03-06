package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.EnumerationLiteral.EnumerationLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;

public class AntlrEnumerationLiteral
{
    public static final AntlrEnumerationLiteral AMBIGUOUS = new AntlrEnumerationLiteral(
            null,
            null,
            null,
            null);

    private final EnumerationLiteralContext    ctx;
    private final EnumerationPrettyNameContext prettyNameContext;
    private final String                       literalName;
    private final String                       prettyName;
    private       EnumerationLiteralBuilder    enumerationLiteralBuilder;

    public AntlrEnumerationLiteral(
            EnumerationLiteralContext ctx,
            EnumerationPrettyNameContext prettyNameContext,
            String literalName,
            String prettyName)
    {
        this.ctx = ctx;
        this.prettyNameContext = prettyNameContext;
        this.literalName = literalName;
        this.prettyName = prettyName;
    }

    public EnumerationLiteralContext getCtx()
    {
        return this.ctx;
    }

    public EnumerationPrettyNameContext getPrettyNameContext()
    {
        return this.prettyNameContext;
    }

    public String getLiteralName()
    {
        return this.literalName;
    }

    public String getPrettyName()
    {
        return this.prettyName;
    }

    public EnumerationLiteralBuilder build(EnumerationBuilder enumerationBuilder)
    {
        this.enumerationLiteralBuilder = new EnumerationLiteralBuilder(
                this.ctx,
                this.ctx.identifier(),
                this.literalName,
                this.prettyName,
                enumerationBuilder);
        return this.enumerationLiteralBuilder;
    }
}
