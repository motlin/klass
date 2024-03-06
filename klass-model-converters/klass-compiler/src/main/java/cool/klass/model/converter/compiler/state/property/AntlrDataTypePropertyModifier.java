package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.property.DataTypePropertyModifierImpl.DataTypePropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrDataTypePropertyModifier extends AntlrModifier
{
    public static final ImmutableList<String> AUDIT_PROPERTY_NAMES = Lists.immutable.with(
            "createdBy",
            "createdOn",
            "lastUpdatedBy");

    public static final AntlrDataTypePropertyModifier AMBIGUOUS = new AntlrDataTypePropertyModifier(
            new ParserRuleContext(),
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous property modifier",
            0,
            AntlrPrimitiveProperty.AMBIGUOUS);

    @Nonnull
    private final AntlrDataTypeProperty<?> owningProperty;

    private DataTypePropertyModifierBuilder elementBuilder;

    public AntlrDataTypePropertyModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrDataTypeProperty<?> owningProperty)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
        this.owningProperty = Objects.requireNonNull(owningProperty);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningProperty);
    }

    public boolean isKey()
    {
        return this.name.equals("key");
    }

    public boolean isID()
    {
        return this.name.equals("id");
    }

    public boolean isUserId()
    {
        return this.name.equals("userId");
    }

    public boolean isDerived()
    {
        return this.name.equals("derived");
    }

    @Override
    public boolean isAudit()
    {
        return AUDIT_PROPERTY_NAMES.contains(this.name);
    }

    @Override
    protected boolean isUser()
    {
        return this.name.equals("userId");
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
    public DataTypePropertyModifierBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new DataTypePropertyModifierBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.nameContext,
                this.name,
                this.ordinal,
                this.owningProperty.getElementBuilder());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public DataTypePropertyModifierBuilder getElementBuilder()
    {
        return this.elementBuilder;
    }
}
