package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.meta.domain.ClassModifierImpl.ClassModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrClassModifier extends AntlrModifier
{
    public static final AntlrClassModifier NOT_FOUND = new AntlrClassModifier(
            new ParserRuleContext(),
            null,
            Optional.empty(),
            new ParserRuleContext(),
            "not found class modifier",
            -1,
            AntlrClass.NOT_FOUND);

    public static final AntlrClassModifier AMBIGUOUS = new AntlrClassModifier(
            new ParserRuleContext(),
            null,
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous class modifier",
            -1,
            AntlrClass.AMBIGUOUS);

    private final AntlrClassifier      owningClassifierState;
    private       ClassModifierBuilder elementBuilder;

    public AntlrClassModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            AntlrClassifier owningClassifierState)
    {
        super(elementContext, compilationUnit, macroElement, nameContext, name, ordinal);
        this.owningClassifierState = owningClassifierState;
    }

    @Nonnull
    @Override
    public ClassModifierContext getElementContext()
    {
        return (ClassModifierContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.ofNullable(this.owningClassifierState);
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
                this.macroElement.map(AntlrElement::getElementBuilder),
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
