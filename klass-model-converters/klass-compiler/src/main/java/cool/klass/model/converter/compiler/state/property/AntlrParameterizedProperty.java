package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterOwner;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.property.ParameterizedPropertyImpl.ParameterizedPropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;

public class AntlrParameterizedProperty
        extends AntlrClassReferenceProperty
        implements AntlrParameterOwner
{
    public static final AntlrParameterizedProperty AMBIGUOUS = new AntlrParameterizedProperty(
            new ParameterizedPropertyContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrClass.AMBIGUOUS);

    public static final AntlrParameterizedProperty NOT_FOUND = new AntlrParameterizedProperty(
            new ParameterizedPropertyContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrClass.AMBIGUOUS);

    // @Nonnull
    // private final ImmutableList<AntlrParameterizedPropertyModifier> parameterizedPropertyModifierStates;
    @Nonnull
    private final AntlrClass owningClassState;

    private final ParameterHolder parameterHolder = new ParameterHolder();

    @Nullable
    private ParameterizedPropertyBuilder parameterizedPropertyBuilder;
    private AntlrCriteria                criteriaState;

    public AntlrParameterizedProperty(
            @Nonnull ParameterizedPropertyContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrClass owningClassState)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
        this.owningClassState = Objects.requireNonNull(owningClassState);
    }

    @Nonnull
    @Override
    public ParameterizedPropertyContext getElementContext()
    {
        return (ParameterizedPropertyContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningClassState);
    }

    @Override
    public int getNumParameters()
    {
        return this.parameterHolder.getNumParameters();
    }

    @Override
    public void enterParameterDeclaration(@Nonnull AntlrParameter parameterState)
    {
        this.parameterHolder.enterParameterDeclaration(parameterState);
    }

    @Override
    public AntlrParameter getParameterByContext(@Nonnull ParameterDeclarationContext ctx)
    {
        return this.parameterHolder.getParameterByContext(ctx);
    }

    @Nonnull
    public AntlrCriteria getCriteria()
    {
        return Objects.requireNonNull(this.criteriaState);
    }

    public void setCriteria(@Nonnull AntlrCriteria criteria)
    {
        if (this.criteriaState != null)
        {
            throw new IllegalStateException();
        }
        this.criteriaState = Objects.requireNonNull(criteria);
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
                (ParameterizedPropertyContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
                this.getType().getElementBuilder(),
                this.owningClassState.getElementBuilder(),
                this.multiplicityState.getMultiplicity());

        Optional<OrderByBuilder> orderByBuilder = this.orderByState.map(AntlrOrderBy::build);
        this.parameterizedPropertyBuilder.setOrderByBuilder(orderByBuilder);

        return this.parameterizedPropertyBuilder;
    }

    @Nonnull
    @Override
    public ParameterizedPropertyBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.parameterizedPropertyBuilder);
    }

    @Override
    protected IdentifierContext getTypeIdentifier()
    {
        return this.getElementContext().classReference().identifier();
    }

    @Nonnull
    @Override
    public AntlrClass getOwningClassifierState()
    {
        return Objects.requireNonNull(this.owningClassState);
    }

    //<editor-fold desc="Report Compiler Errors">
    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        if (this.orderByState != null)
        {
            this.orderByState.ifPresent(o -> o.reportErrors(compilerErrorHolder));
        }

        this.reportTypeNotFound(compilerErrorHolder);
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportNameErrors(compilerErrorHolder);
        this.parameterHolder.reportNameErrors(compilerErrorHolder);
    }
    //</editor-fold>
}
