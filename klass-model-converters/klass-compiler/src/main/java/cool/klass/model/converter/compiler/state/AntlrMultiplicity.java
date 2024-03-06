package cool.klass.model.converter.compiler.state;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityBodyContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class AntlrMultiplicity extends AntlrElement
{
    public static final AntlrMultiplicity AMBIGUOUS = new AntlrMultiplicity();

    @Nullable
    private final Multiplicity multiplicity;

    public AntlrMultiplicity(
            @Nonnull MultiplicityContext context,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred)
    {
        super(context, compilationUnit, inferred);
        this.multiplicity = this.getMultiplicity(context);
    }

    private AntlrMultiplicity()
    {
        super(new ParserRuleContext(), null, true);
        this.multiplicity = Multiplicity.ONE_TO_ONE;
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

    @Nonnull
    @Override
    public MultiplicityContext getElementContext()
    {
        return (MultiplicityContext) super.getElementContext();
    }

    @Nullable
    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }
}
