package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.meta.domain.ClassModifierImpl.ClassModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrClassModifier extends AntlrModifier
{
    public static final AntlrClassModifier NOT_FOUND = new AntlrClassModifier(
            new ParserRuleContext(),
            Optional.empty(),
            new ParserRuleContext(),
            "not found class modifier",
            -1,
            AntlrClass.NOT_FOUND);

    public static final AntlrClassModifier AMBIGUOUS = new AntlrClassModifier(
            new ParserRuleContext(),
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous class modifier",
            -1,
            AntlrClass.AMBIGUOUS);

    private final AntlrClassifier      owningClassifierState;
    private       ClassModifierBuilder elementBuilder;

    public AntlrClassModifier(
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
    public ClassModifierContext getElementContext()
    {
        return (ClassModifierContext) super.getElementContext();
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
    public ClassModifierBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new ClassModifierBuilder(
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
    public ClassModifierBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
