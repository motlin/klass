package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrMultiplicity extends AntlrElement
{
    public static final AntlrMultiplicity AMBIGUOUS = new AntlrMultiplicity();

    private String lowerBoundText;
    private String upperBoundText;

    @Nullable
    private final Multiplicity multiplicity;

    @Nonnull
    private final AntlrMultiplicityOwner multiplicityOwnerState;

    public AntlrMultiplicity(
            @Nonnull MultiplicityContext context,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrMultiplicityOwner multiplicityOwnerState)
    {
        super(context, compilationUnit);

        this.lowerBoundText = context.multiplicityBody().lowerBound.getText();
        this.upperBoundText = context.multiplicityBody().upperBound.getText();

        this.multiplicity = this.findMultiplicity();

        this.multiplicityOwnerState = Objects.requireNonNull(multiplicityOwnerState);
    }

    private AntlrMultiplicity()
    {
        super(new ParserRuleContext(), Optional.empty());
        this.multiplicity           = Multiplicity.ONE_TO_ONE;
        this.multiplicityOwnerState = null;
    }

    @Nullable
    private Multiplicity findMultiplicity()
    {
        if (this.lowerBoundText.equals("0") && this.upperBoundText.equals("1"))
        {
            return Multiplicity.ZERO_TO_ONE;
        }
        if (this.lowerBoundText.equals("1") && this.upperBoundText.equals("1"))
        {
            return Multiplicity.ONE_TO_ONE;
        }
        if (this.lowerBoundText.equals("0") && this.upperBoundText.equals("*"))
        {
            return Multiplicity.ZERO_TO_MANY;
        }
        if (this.lowerBoundText.equals("1") && this.upperBoundText.equals("*"))
        {
            return Multiplicity.ONE_TO_MANY;
        }
        return null;
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.ofNullable(this.multiplicityOwnerState);
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

    public String getLowerBoundText()
    {
        return this.lowerBoundText;
    }

    public String getUpperBoundText()
    {
        return this.upperBoundText;
    }

    public boolean isToOne()
    {
        return this.multiplicity != null && this.multiplicity.isToOne();
    }

    public boolean isToMany()
    {
        return this.multiplicity != null && this.multiplicity.isToMany();
    }

    @Override
    public String toString()
    {
        return this.multiplicity.getPrettyName();
    }
}
