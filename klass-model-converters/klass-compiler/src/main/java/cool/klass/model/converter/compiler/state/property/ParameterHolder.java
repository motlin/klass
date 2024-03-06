package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.annotation.CompilerAnnotationHolder;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterOwner;
import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public final class ParameterHolder
        implements AntlrParameterOwner
{
    private final MutableList<AntlrParameter>                          parameters          = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrParameter>            parametersByName    =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ParserRuleContext, AntlrParameter> parametersByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    @Override
    public int getNumParameters()
    {
        return this.parameters.size();
    }

    @Override
    public void enterParameterDeclaration(@Nonnull AntlrParameter parameter)
    {
        this.parameters.add(parameter);
        this.parametersByName.compute(
                parameter.getName(),
                (name, builder) -> builder == null
                        ? parameter
                        : AntlrParameter.AMBIGUOUS);
        AntlrParameter duplicate = this.parametersByContext.put(
                parameter.getElementContext(),
                parameter);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    @Override
    public AntlrParameter getParameterByContext(@Nonnull ParameterDeclarationContext ctx)
    {
        return this.parametersByContext.get(ctx);
    }

    public MutableList<AntlrParameter> getParameters()
    {
        return this.parameters.asUnmodifiable();
    }

    @Nonnull
    public MutableOrderedMap<String, AntlrParameter> getParametersByName()
    {
        // TODO: Override MutableOrderedMap.asUnmodifiable
        return this.parametersByName;
    }

    public void reportNameErrors(CompilerAnnotationHolder compilerAnnotationHolder)
    {
        this.parameters.forEachWith(AntlrNamedElement::reportNameErrors, compilerAnnotationHolder);
    }
}
