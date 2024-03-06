package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.meta.domain.ClassModifierImpl.ClassModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrClassModifier extends AntlrModifier
{
    public static final AntlrClassModifier AMBIGUOUS = new AntlrClassModifier(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous class modifier",
            -1,
            AntlrClass.AMBIGUOUS);

    private final AntlrClassifier owningClassifierState;
    private ClassModifierBuilder  elementBuilder;

    public AntlrClassModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            AntlrClassifier owningClassifierState)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.owningClassifierState = owningClassifierState;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getSurroundingContext() not implemented yet");
    }

    public boolean isTransient()
    {
        return this.name.equals("transient");
    }

    public boolean isOptimisticallyLocked()
    {
        return this.name.equals("optimisticallyLocked");
    }

    @Override
    @Nonnull
    public ClassModifierBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new ClassModifierBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.owningClassifierState.getElementBuilder());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public ClassModifierBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
