package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.modifier.Modifier;
import cool.klass.model.meta.domain.api.modifier.ModifierOwner;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractModifier
        extends AbstractNamedElement
        implements Modifier
{
    @Nonnull
    private final ModifierOwner modifierOwner;

    protected AbstractModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull ModifierOwner modifierOwner)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
        this.modifierOwner = Objects.requireNonNull(modifierOwner);
    }

    @Override
    public ModifierOwner getModifierOwner()
    {
        return this.modifierOwner;
    }

    public abstract static class ModifierBuilder<BuiltElement extends AbstractModifier>
            extends NamedElementBuilder<BuiltElement>
    {
        protected ModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
        }
    }
}
