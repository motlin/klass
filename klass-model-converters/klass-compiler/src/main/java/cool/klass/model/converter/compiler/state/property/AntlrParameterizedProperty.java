package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.parameter.AntlrEnumerationParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterOwner;
import cool.klass.model.converter.compiler.state.parameter.AntlrPrimitiveParameter;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.property.ParameterizedPropertyImpl.ParameterizedPropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;

public class AntlrParameterizedProperty extends AntlrReferenceTypeProperty implements AntlrParameterOwner
{
    @Nullable
    public static final AntlrParameterizedProperty AMBIGUOUS = new AntlrParameterizedProperty(
            new ParameterizedPropertyContext(null, -1),
            null,
            true,
            AbstractElement.NO_CONTEXT,
            "ambiguous association end",
            -1,
            AntlrClass.AMBIGUOUS,
            AntlrClass.AMBIGUOUS,
            null);
    @Nullable
    public static final AntlrParameterizedProperty NOT_FOUND = new AntlrParameterizedProperty(
            new ParameterizedPropertyContext(null, -1),
            null,
            true,
            AbstractElement.NO_CONTEXT,
            "not found association end",
            -1,
            AntlrClass.AMBIGUOUS,
            AntlrClass.AMBIGUOUS,
            null);

    // @Nonnull
    // private final ImmutableList<AntlrParameterizedPropertyModifier> parameterizedPropertyModifierStates;
    private final AntlrClass owningClassState;

    private final ParameterHolder parameterHolder = new ParameterHolder();

    @Nullable
    private ParameterizedPropertyBuilder parameterizedPropertyBuilder;
    private AntlrCriteria                criteriaState;

    public AntlrParameterizedProperty(
            @Nonnull ParameterizedPropertyContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClass owningClassState,
            @Nonnull AntlrClass type,
            AntlrMultiplicity multiplicityState)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal, type, multiplicityState);
        this.owningClassState = Objects.requireNonNull(owningClassState);
    }

    @Nonnull
    @Override
    public ParameterizedPropertyContext getElementContext()
    {
        return (ParameterizedPropertyContext) super.getElementContext();
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.elementContext);
        this.owningClassState.getParserRuleContexts(parserRuleContexts);
    }

    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.ofNullable(this.owningClassState);
    }

    @Override
    public int getNumParameters()
    {
        return this.parameterHolder.getNumParameters();
    }

    @Override
    public void enterParameterDeclaration(@Nonnull AntlrParameter<?> parameterState)
    {
        this.parameterHolder.enterParameterDeclaration(parameterState);
    }

    @Override
    public void enterPrimitiveParameterDeclaration(@Nonnull AntlrPrimitiveParameter primitiveParameterState)
    {
        this.parameterHolder.enterPrimitiveParameterDeclaration(primitiveParameterState);
    }

    @Override
    public void enterEnumerationParameterDeclaration(@Nonnull AntlrEnumerationParameter enumerationParameterState)
    {
        this.parameterHolder.enterEnumerationParameterDeclaration(enumerationParameterState);
    }

    @Nonnull
    @Override
    public ParameterizedPropertyBuilder build()
    {
        if (this.parameterizedPropertyBuilder != null)
        {
            throw new IllegalStateException();
        }

        /*
        ImmutableList<ParameterizedPropertyModifierBuilder> parameterizedPropertyModifierBuilders =
                this.parameterizedPropertyModifierStates.collect(AntlrParameterizedPropertyModifier::build);
        */

        this.parameterizedPropertyBuilder = new ParameterizedPropertyBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.type.getElementBuilder(),
                this.owningClassState.getElementBuilder(),
                this.multiplicityState.getMultiplicity());

        Optional<OrderByBuilder> orderByBuilder = this.orderByState.map(AntlrOrderBy::build);
        this.parameterizedPropertyBuilder.setOrderByBuilder(orderByBuilder);

        return this.parameterizedPropertyBuilder;
    }

    @Nonnull
    public ParameterizedPropertyBuilder getParameterizedPropertyBuilder()
    {
        return Objects.requireNonNull(this.parameterizedPropertyBuilder);
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Check that there are no duplicate modifiers

        if (this.orderByState != null)
        {
            this.orderByState.ifPresent(o -> o.reportErrors(compilerErrorHolder));
        }
    }

    @Nonnull
    public AntlrCriteria getCriteria()
    {
        return this.criteriaState;
    }

    public void setCriteria(@Nonnull AntlrCriteria criteria)
    {
        this.criteriaState = criteria;
    }
}
