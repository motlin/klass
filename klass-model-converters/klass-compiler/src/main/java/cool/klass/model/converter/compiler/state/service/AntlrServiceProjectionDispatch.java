package cool.klass.model.converter.compiler.state.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.meta.domain.service.ServiceProjectionDispatchImpl.ServiceProjectionDispatchBuilder;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrServiceProjectionDispatch extends AntlrElement
{
    @Nonnull
    private final AntlrService                     serviceState;
    @Nonnull
    private final AntlrProjection                  projection;
    private       ServiceProjectionDispatchBuilder elementBuilder;

    public AntlrServiceProjectionDispatch(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrService serviceState,
            @Nonnull AntlrProjection projection)
    {
        super(elementContext, compilationUnit);
        this.serviceState = Objects.requireNonNull(serviceState);
        this.projection = Objects.requireNonNull(projection);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return false;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.serviceState);
    }

    @Nonnull
    public AntlrProjection getProjection()
    {
        return this.projection;
    }

    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.projection == AntlrProjection.NOT_FOUND)
        {
            ProjectionReferenceContext reference = this.getElementContext().projectionReference();

            compilerErrorHolder.add(
                    "ERR_SER_PRJ",
                    String.format("Cannot find projection '%s'", reference.getText()),
                    this,
                    reference);
            return;
        }

        AntlrClass projectionKlass = this.projection.getKlass();
        if (projectionKlass == AntlrClass.NOT_FOUND)
        {
            return;
        }
        AntlrClass serviceGroupKlass = this.serviceState.getUrlState().getServiceGroup().getKlass();
        if (serviceGroupKlass != projectionKlass && !serviceGroupKlass.isSubTypeOf(projectionKlass))
        {
            String error = String.format(
                    "Expected projection referencing '%s' but projection '%s' references '%s'.",
                    serviceGroupKlass.getName(),
                    this.projection.getName(),
                    projectionKlass.getName());
            compilerErrorHolder.add("ERR_SRV_PRJ", error, this, this.getElementContext().projectionReference());
        }
    }

    @Nonnull
    @Override
    public ServiceProjectionDispatchContext getElementContext()
    {
        return (ServiceProjectionDispatchContext) super.getElementContext();
    }

    @Nonnull
    public ServiceProjectionDispatchBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new ServiceProjectionDispatchBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.projection.getElementBuilder());
        return this.elementBuilder;
    }

    @Override
    @Nonnull
    public ServiceProjectionDispatchBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
