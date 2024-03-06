package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrMultiplicity
        extends AntlrElement
{
    private String lowerBoundText;
    private String upperBoundText;

    @Nullable
    private final Multiplicity multiplicity;

    @Nonnull
    private final AntlrMultiplicityOwner multiplicityOwner;

    public AntlrMultiplicity(
            @Nonnull MultiplicityContext context,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrMultiplicityOwner multiplicityOwner)
    {
        super(context, compilationUnit);

        this.lowerBoundText = context.multiplicityBody().lowerBound.getText();
        this.upperBoundText = context.multiplicityBody().upperBound.getText();

        this.multiplicity = this.findMultiplicity();

        this.multiplicityOwner = Objects.requireNonNull(multiplicityOwner);
    }

    private AntlrMultiplicity()
    {
        super(new ParserRuleContext(), Optional.empty());
        this.multiplicity      = Multiplicity.ONE_TO_ONE;
        this.multiplicityOwner = null;
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

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.ofNullable(this.multiplicityOwner);
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
