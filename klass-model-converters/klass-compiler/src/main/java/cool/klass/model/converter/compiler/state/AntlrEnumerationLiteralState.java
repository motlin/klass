package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;

public class AntlrEnumerationLiteralState
{
    public static final AntlrEnumerationLiteralState AMBIGUOUS = new AntlrEnumerationLiteralState(
            null,
            null,
            null,
            null);

    private final EnumerationLiteralContext    ctx;
    private final EnumerationPrettyNameContext prettyNameContext;
    private final String                       literalName;
    private final String                       prettyName;

    public AntlrEnumerationLiteralState(
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
}
