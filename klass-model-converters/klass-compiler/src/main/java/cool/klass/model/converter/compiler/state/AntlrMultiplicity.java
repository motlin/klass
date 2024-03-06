package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityBodyContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import org.antlr.v4.runtime.Token;

public class AntlrMultiplicity
{
    private final MultiplicityContext context;
    private final Multiplicity        multiplicity;

    public AntlrMultiplicity(MultiplicityContext context)
    {
        this.context = context;
        this.multiplicity = this.getMultiplicity(context);
    }

    private Multiplicity getMultiplicity(MultiplicityContext multiplicityContext)
    {
        MultiplicityBodyContext multiplicityBodyContext = multiplicityContext.multiplicityBody();

        Token  lowerBound     = multiplicityBodyContext.lowerBound;
        Token  upperBound     = multiplicityBodyContext.upperBound;
        String lowerBoundText = lowerBound.getText();
        String upperBoundText = upperBound.getText();

        if (lowerBoundText.equals("0") && upperBoundText.equals("1"))
        {
            return Multiplicity.ZERO_TO_ONE;
        }
        if (lowerBoundText.equals("1") && upperBoundText.equals("1"))
        {
            return Multiplicity.ONE_TO_ONE;
        }
        if (lowerBoundText.equals("0") && upperBoundText.equals("*"))
        {
            return Multiplicity.ZERO_TO_MANY;
        }
        if (lowerBoundText.equals("1") && upperBoundText.equals("*"))
        {
            return Multiplicity.ONE_TO_MANY;
        }
        return null;
    }

    public MultiplicityContext getContext()
    {
        return this.context;
    }

    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }
}
