package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.AssociationEndModifierImpl.AssociationEndModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrAssociationEndModifier extends AntlrModifier
{
    @Nullable
    public static final AntlrAssociationEndModifier AMBIGUOUS = new AntlrAssociationEndModifier(
            new AssociationEndModifierContext(null, -1),
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous association end modifier",
            -1,
            AntlrAssociationEnd.AMBIGUOUS);

    private final AntlrReferenceTypeProperty<?> surroundingElement;
    private       AssociationEndModifierBuilder elementBuilder;

    public AntlrAssociationEndModifier(
            @Nonnull AssociationEndModifierContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrReferenceTypeProperty<?> surroundingElement)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
        this.surroundingElement = Objects.requireNonNull(surroundingElement);
    }

    @Nonnull
    @Override
    public AssociationEndModifierContext getElementContext()
    {
        return (AssociationEndModifierContext) super.getElementContext();
    }

    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.ofNullable(this.surroundingElement);
    }

    public boolean isOwned()
    {
        return this.name.equals("owned");
    }

    public boolean isVersion()
    {
        return this.name.equals("version");
    }

    @Override
    @Nonnull
    public AssociationEndModifierBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new AssociationEndModifierBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.nameContext,
                this.name,
                this.ordinal,
                (AssociationEndBuilder) this.surroundingElement.getElementBuilder());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public AssociationEndModifierBuilder getElementBuilder()
    {
        return this.elementBuilder;
    }
}
