package cool.klass.model.converter.compiler.state.service;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.meta.domain.service.ServiceProjectionDispatch.ServiceProjectionDispatchBuilder;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrServiceProjectionDispatch extends AntlrElement
{
    @Nonnull
    private final AntlrService    serviceState;
    @Nonnull
    private final AntlrProjection projection;

    public AntlrServiceProjectionDispatch(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrService serviceState,
            @Nonnull AntlrProjection projection)
    {
        super(elementContext, compilationUnit, inferred);
        this.serviceState = Objects.requireNonNull(serviceState);
        this.projection = Objects.requireNonNull(projection);
    }

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        if (this.projection == AntlrProjection.NOT_FOUND)
        {
            String message = "TODO";
            compilerErrorHolder.add(
                    message,
                    this.getElementContext(),
                    this.serviceState.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
        }
        else
        {
            AntlrClass projectionKlass   = this.projection.getKlass();
            AntlrClass serviceGroupKlass = this.serviceState.getUrlState().getServiceGroup().getKlass();
            if (projectionKlass != serviceGroupKlass)
            {
                String error = String.format(
                        "ERR_SRV_PRJ: Expected projection referencing '%s' but projection '%s' references '%s'.",
                        serviceGroupKlass.getName(),
                        this.projection.getName(),
                        projectionKlass.getName());
                compilerErrorHolder.add(
                        error,
                        this.getElementContext().projectionReference(),
                        this.serviceState.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
            }
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
        return new ServiceProjectionDispatchBuilder(this.elementContext, this.inferred, this.projection.getProjectionBuilder());
    }
}
