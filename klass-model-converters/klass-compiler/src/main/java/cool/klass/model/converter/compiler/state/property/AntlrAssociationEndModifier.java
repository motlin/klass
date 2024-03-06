package cool.klass.model.converter.compiler.state.property;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.property.AssociationEndModifierImpl.AssociationEndModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrAssociationEndModifier extends AntlrModifier
{
    public static final AntlrAssociationEndModifier AMBIGUOUS = new AntlrAssociationEndModifier(
            new AssociationEndModifierContext(null, -1),
            null,
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous association end modifier",
            -1,
            AntlrAssociationEnd.AMBIGUOUS);

    private final AntlrAssociationEnd           surroundingElement;
    private       AssociationEndModifierBuilder elementBuilder;

    public AntlrAssociationEndModifier(
            @Nonnull AssociationEndModifierContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            AntlrAssociationEnd surroundingElement)
    {
        super(elementContext, compilationUnit, macroElement, nameContext, name, ordinal);
        this.surroundingElement = surroundingElement;
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
                this.macroElement.map(AntlrElement::getElementBuilder),
                this.nameContext,
                this.name,
                this.ordinal,
                this.surroundingElement.getElementBuilder());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public AssociationEndModifierBuilder getElementBuilder()
    {
        return this.elementBuilder;
    }
}
