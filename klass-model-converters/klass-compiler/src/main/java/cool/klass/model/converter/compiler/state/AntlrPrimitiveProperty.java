package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.PrimitiveProperty.PrimitivePropertyBuilder;
import cool.klass.model.meta.domain.PrimitiveType;
import cool.klass.model.meta.domain.PrimitiveType.PrimitiveTypeBuilder;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrPrimitiveProperty extends AntlrDataTypeProperty<PrimitiveType>
{
    @Nullable
    public static final AntlrPrimitiveProperty AMBIGUOUS = new AntlrPrimitiveProperty(
            new PrimitivePropertyContext(null, -1),
            null,
            true,
            "ambiguous primitive property name",
            Element.NO_CONTEXT,
            false,
            Lists.immutable.empty(),
            AntlrClass.AMBIGUOUS,
            null);

    private final PrimitiveTypeBuilder primitiveTypeBuilder;

    private PrimitivePropertyBuilder primitivePropertyBuilder;

    public AntlrPrimitiveProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull String name,
            @Nonnull ParserRuleContext nameContext,
            boolean isOptional,
            @Nonnull ImmutableList<AntlrPropertyModifier> modifiers,
            AntlrClass owningClassState,
            PrimitiveTypeBuilder primitiveTypeBuilder)
    {
        super(elementContext, compilationUnit, inferred, name, nameContext, isOptional, modifiers, owningClassState);
        this.primitiveTypeBuilder = primitiveTypeBuilder;
    }

    @Override
    public boolean isTemporal()
    {
        return this.primitiveTypeBuilder.getPrimitiveType().isTemporal();
    }

    @Override
    public PrimitivePropertyBuilder build()
    {
        if (this.primitivePropertyBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.primitivePropertyBuilder = new PrimitivePropertyBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                this.primitiveTypeBuilder,
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
