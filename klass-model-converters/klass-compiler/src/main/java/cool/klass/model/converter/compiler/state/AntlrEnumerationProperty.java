package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.EnumerationProperty.EnumerationPropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrEnumerationProperty extends AntlrDataTypeProperty<Enumeration>
{
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
    private final AntlrEnumeration antlrEnumeration;

    private EnumerationPropertyBuilder enumerationPropertyBuilder;

    public AntlrEnumerationProperty(
            EnumerationPropertyContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            ParserRuleContext nameContext,
            String name,
            boolean isOptional,
            ImmutableList<AntlrPropertyModifier> modifiers,
            AntlrClass owningClassState,
            AntlrEnumeration antlrEnumeration)
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

    public EnumerationPropertyBuilder getPropertyBuilder()
    {
        return Objects.requireNonNull(this.enumerationPropertyBuilder);
    }
}
