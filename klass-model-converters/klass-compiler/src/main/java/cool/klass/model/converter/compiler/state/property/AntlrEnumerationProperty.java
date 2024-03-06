package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.property.EnumerationProperty.EnumerationPropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrEnumerationProperty extends AntlrDataTypeProperty<Enumeration>
{
    @Nonnull
    public static final AntlrEnumerationProperty NOT_FOUND = new AntlrEnumerationProperty(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "not found enumeration property",
            -1,
            false,
            Lists.immutable.empty(),
            AntlrClass.NOT_FOUND,
            AntlrEnumeration.NOT_FOUND);

    // TODO: Check that it's not NOT_FOUND
    @Nonnull
    private final AntlrEnumeration antlrEnumeration;

    private EnumerationPropertyBuilder enumerationPropertyBuilder;

    public AntlrEnumerationProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            boolean isOptional,
            @Nonnull ImmutableList<AntlrPropertyModifier> modifiers,
            AntlrClass owningClassState,
            @Nonnull AntlrEnumeration antlrEnumeration)
    {
        super(
                elementContext,
                compilationUnit,
                inferred,
                nameContext,
                name,
                ordinal,
                isOptional,
                modifiers,
                owningClassState);
        // TODO: is this nullable?
        this.antlrEnumeration = Objects.requireNonNull(antlrEnumeration);
    }

    @Nonnull
    @Override
    public AntlrEnumeration getType()
    {
        return this.antlrEnumeration;
    }

    @Override
    public boolean isTemporal()
    {
        return false;
    }

    @Override
    public EnumerationPropertyBuilder build()
    {
        if (this.enumerationPropertyBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.enumerationPropertyBuilder = new EnumerationPropertyBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                ordinal, this.antlrEnumeration.getEnumerationBuilder(),
                this.owningClassState.getKlassBuilder(),
                this.isKey(),
                this.isOptional);
        return this.enumerationPropertyBuilder;
    }

    @Nonnull
    @Override
    public EnumerationPropertyBuilder getPropertyBuilder()
    {
        return Objects.requireNonNull(this.enumerationPropertyBuilder);
    }
}
