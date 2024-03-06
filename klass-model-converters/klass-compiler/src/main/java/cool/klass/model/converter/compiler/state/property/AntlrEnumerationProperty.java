package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.property.EnumerationProperty.EnumerationPropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrEnumerationProperty extends AntlrDataTypeProperty<Enumeration>
{
    @Nullable
    public static final AntlrEnumerationProperty NOT_FOUND = new AntlrEnumerationProperty(
            new EnumerationPropertyContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "not found enumeration property",
            false,
            Lists.immutable.empty(),
            AntlrClass.NOT_FOUND,
            AntlrEnumeration.NOT_FOUND);

    // TODO: Check that it's not NOT_FOUND
    @Nonnull
    private final AntlrEnumeration antlrEnumeration;

    private EnumerationPropertyBuilder enumerationPropertyBuilder;

    public AntlrEnumerationProperty(
            @Nonnull EnumerationPropertyContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            boolean isOptional,
            @Nonnull ImmutableList<AntlrPropertyModifier> modifiers,
            AntlrClass owningClassState,
            @Nonnull AntlrEnumeration antlrEnumeration)
    {
        super(elementContext, compilationUnit, inferred, name, nameContext, isOptional, modifiers, owningClassState);
        // TODO: is this nullable?
        this.antlrEnumeration = Objects.requireNonNull(antlrEnumeration);
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
                this.antlrEnumeration.getEnumerationBuilder(),
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
