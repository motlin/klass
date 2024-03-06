package cool.klass.model.converter.compiler.state.property;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.property.AssociationEndModifierImpl.AssociationEndModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrAssociationEndModifier extends AntlrModifier
{
    private final AntlrAssociationEnd           surroundingElement;
    private       AssociationEndModifierBuilder elementBuilder;

    public AntlrAssociationEndModifier(
            @Nonnull AssociationEndModifierContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            AntlrAssociationEnd surroundingElement)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
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
                this.inferred,
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
