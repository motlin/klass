package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.property.PrimitiveProperty.PrimitivePropertyBuilder;
import cool.klass.model.meta.domain.property.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrPrimitiveProperty extends AntlrDataTypeProperty<PrimitiveType>
{
    @Nonnull
    public static final AntlrPrimitiveProperty AMBIGUOUS = new AntlrPrimitiveProperty(
            new PrimitivePropertyContext(null, -1),
            null,
            true,
            Element.NO_CONTEXT,
            "ambiguous primitive property name",
            false,
            Lists.immutable.empty(),
            AntlrClass.AMBIGUOUS,
            AntlrPrimitiveType.AMBIGUOUS);

    @Nonnull
    private final AntlrPrimitiveType antlrPrimitiveType;

    private PrimitivePropertyBuilder primitivePropertyBuilder;

    public AntlrPrimitiveProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            boolean isOptional,
            @Nonnull ImmutableList<AntlrPropertyModifier> modifiers,
            AntlrClass owningClassState,
            @Nonnull AntlrPrimitiveType antlrPrimitiveType)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, isOptional, modifiers, owningClassState);
        this.antlrPrimitiveType = Objects.requireNonNull(antlrPrimitiveType);
    }

    @Nonnull
    @Override
    public AntlrPrimitiveType getType()
    {
        return this.antlrPrimitiveType;
    }

    @Override
    public boolean isTemporal()
    {
        return this.antlrPrimitiveType.isTemporal();
    }

    @Override
    public PrimitivePropertyBuilder build()
    {
        if (this.primitivePropertyBuilder != null)
        {
            throw new IllegalStateException();
        }

        // TODO: Pass through property modifiers?
        this.primitivePropertyBuilder = new PrimitivePropertyBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                this.antlrPrimitiveType.build(),
                this.owningClassState.getKlassBuilder(),
                this.isKey(),
                this.isOptional);
        return this.primitivePropertyBuilder;
    }

    @Nonnull
    @Override
    public PrimitivePropertyBuilder getPropertyBuilder()
    {
        return Objects.requireNonNull(this.primitivePropertyBuilder);
    }
}
