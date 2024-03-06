package cool.klass.model.converter.compiler.state.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.meta.domain.property.AssociationEndModifier.AssociationEndModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrAssociationEndModifier extends AntlrNamedElement
{
    public AntlrAssociationEndModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
    }

    public boolean isOwned()
    {
        return this.name.equals("owned");
    }

    public AssociationEndModifierBuilder build()
    {
        return new AssociationEndModifierBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal);
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        // intentionally blank
    }
}
