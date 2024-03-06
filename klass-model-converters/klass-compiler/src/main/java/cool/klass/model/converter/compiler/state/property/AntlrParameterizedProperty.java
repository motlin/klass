package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.converter.compiler.state.parameter.AntlrEnumerationParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterOwner;
import cool.klass.model.converter.compiler.state.parameter.AntlrPrimitiveParameter;
import cool.klass.model.converter.compiler.state.service.AntlrCriteriaOwner;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.property.ParameterizedPropertyImpl.ParameterizedPropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrParameterizedProperty extends AntlrReferenceTypeProperty implements AntlrCriteriaOwner, AntlrParameterOwner, AntlrOrderByOwner
{
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
    private final AntlrClass        owningClassState;

    private final MutableList<AntlrParameter>               parameterStates       = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrParameter> parameterStatesByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableList<AntlrPrimitiveParameter>               primitiveParameterStates       = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrPrimitiveParameter> primitiveParameterStatesByName =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableList<AntlrEnumerationParameter>               enumerationParameterStates       = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrEnumerationParameter> enumerationParameterStatesByName =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private       ParameterizedPropertyBuilder                         parameterizedPropertyBuilder;
    private       AntlrCriteria                                        criteriaState;

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

    @Override
    public int getNumParameters()
    {
        return this.parameterStates.size();
    }

    @Override
    public void enterPrimitiveParameterDeclaration(AntlrPrimitiveParameter primitiveParameterState)
    {
        this.primitiveParameterStates.add(primitiveParameterState);
        this.primitiveParameterStatesByName.compute(
                primitiveParameterState.getName(),
                (name, builder) -> builder == null
                        ? primitiveParameterState
                        : AntlrPrimitiveParameter.AMBIGUOUS);
    }

    @Override
    public void enterEnumerationParameterDeclaration(AntlrEnumerationParameter enumerationParameterState)
    {
        this.enumerationParameterStates.add(enumerationParameterState);
        this.enumerationParameterStatesByName.compute(
                enumerationParameterState.getName(),
                (name, builder) -> builder == null
                        ? enumerationParameterState
                        : AntlrEnumerationParameter.AMBIGUOUS);
    }

    @Nonnull
    public ParameterizedPropertyBuilder getParameterizedPropertyBuilder()
    {
        return Objects.requireNonNull(this.parameterizedPropertyBuilder);
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        this.reportKeywordCollision(compilerErrorHolder, this.owningClassState.getElementContext());

        if (!MEMBER_NAME_PATTERN.matcher(this.name).matches())
        {
            String message = String.format(
                    "ERR_END_NME: Name must match pattern %s but was %s",
                    CONSTANT_NAME_PATTERN,
                    this.name);
            compilerErrorHolder.add(
                    message,
                    this.nameContext,
                    this.owningClassState.getElementContext());
        }
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Check that there are no duplicate modifiers

        if (this.orderByState != null)
        {
            this.orderByState.ifPresent(o -> o.reportErrors(compilerErrorHolder));
        }
    }

    public void reportDuplicateMemberName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format(
                "ERR_DUP_PZP: Duplicate member: '%s.%s'.",
                this.owningClassState.getName(),
                this.name);

        compilerErrorHolder.add(
                message,
                this.nameContext,
                this.owningClassState.getElementContext());
    }

    @Nonnull
    @Override
    public AntlrCriteria getCriteria()
    {
        return this.criteriaState;
    }

    @Override
    public void setCriteria(@Nonnull AntlrCriteria criteria)
    {
        this.criteriaState = criteria;
    }

    @Override
    public void getParserRuleContexts(MutableList<ParserRuleContext> parserRuleContexts)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getParserRuleContexts() not implemented yet");
    }

    @Override
    public ImmutableList<ParserRuleContext> getParserRuleContexts()
    {
        MutableList<ParserRuleContext> result = Lists.mutable.empty();
        this.getParserRuleContexts(result);
        return result.toImmutable();
    }
}
