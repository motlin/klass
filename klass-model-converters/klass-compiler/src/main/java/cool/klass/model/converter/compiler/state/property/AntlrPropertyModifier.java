package cool.klass.model.converter.compiler.state.property;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.property.PropertyModifierImpl.PropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrPropertyModifier extends AntlrModifier
{
    public static final ImmutableList<String>   AUDIT_PROPERTY_NAMES = Lists.immutable.with(
            "createdBy",
            "createdOn",
            "lastUpdatedBy");
    private             PropertyModifierBuilder elementBuilder;

    public AntlrPropertyModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, macroElement, nameContext, name, ordinal);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getSurroundingContext() not implemented yet");
    }

    public boolean isKey()
    {
        return this.name.equals("key");
    }

    public boolean isID()
    {
        return this.name.equals("id");
    }

    public boolean isAudit()
    {
        return AUDIT_PROPERTY_NAMES.contains(this.name);
    }

    public boolean isSystem()
    {
        return this.name.equals("system");
    }

    public boolean isValid()
    {
        return this.name.equals("valid");
    }

    public boolean isVersionNumber()
    {
        return this.name.equals("version");
    }

    @Override
    @Nonnull
    public PropertyModifierBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new PropertyModifierBuilder(
                this.elementContext,
                this.macroElement.map(AntlrElement::getElementBuilder),
                this.nameContext,
                this.name,
                this.ordinal);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public PropertyModifierBuilder getElementBuilder()
    {
        return this.elementBuilder;
    }
}
