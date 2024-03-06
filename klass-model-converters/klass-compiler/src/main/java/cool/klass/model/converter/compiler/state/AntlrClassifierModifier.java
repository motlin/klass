package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.meta.domain.ClassifierModifierImpl.ClassifierModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrClassifierModifier extends AntlrModifier
{
    public static final AntlrClassifierModifier NOT_FOUND = new AntlrClassifierModifier(
            new ParserRuleContext(),
            Optional.empty(),
            new ParserRuleContext(),
            "not found classifier modifier",
            -1,
            AntlrClassifier.NOT_FOUND);

    public static final AntlrClassifierModifier AMBIGUOUS = new AntlrClassifierModifier(
            new ParserRuleContext(),
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous classifier modifier",
            -1,
            AntlrClassifier.AMBIGUOUS);

    private final AntlrClassifier           owningClassifierState;
    private       ClassifierModifierBuilder elementBuilder;

    public AntlrClassifierModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClassifier owningClassifierState)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
        this.owningClassifierState = Objects.requireNonNull(owningClassifierState);
    }

    @Nonnull
    @Override
    public ClassifierModifierContext getElementContext()
    {
        return (ClassifierModifierContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningClassifierState);
    }

    public boolean isTransient()
    {
        return this.name.equals("transient");
    }

    @Override
    public boolean isAudit()
    {
        return this.name.equals("audited");
    }

    @Override
    protected boolean isUser()
    {
        return this.name.equals("userId");
    }

    @Override
    @Nonnull
    public ClassifierModifierBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new ClassifierModifierBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.nameContext,
                this.name,
                this.ordinal,
                this.owningClassifierState.getElementBuilder());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public ClassifierModifierBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
