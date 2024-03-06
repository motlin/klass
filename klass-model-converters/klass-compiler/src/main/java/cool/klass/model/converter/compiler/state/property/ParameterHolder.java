package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterOwner;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public final class ParameterHolder implements AntlrParameterOwner
{
    private final MutableList<AntlrParameter>               parameterStates       = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrParameter> parameterStatesByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    @Override
    public int getNumParameters()
    {
        return this.parameterStates.size();
    }

    @Override
    public void enterParameterDeclaration(@Nonnull AntlrParameter parameterState)
    {
        this.parameterStates.add(parameterState);
        this.parameterStatesByName.compute(
                parameterState.getName(),
                (name, builder) -> builder == null
                        ? parameterState
                        : AntlrParameter.AMBIGUOUS);
    }

    public MutableList<AntlrParameter> getParameterStates()
    {
        return this.parameterStates.asUnmodifiable();
    }

    @Nonnull
    public MutableOrderedMap<String, AntlrParameter> getParameterStatesByName()
    {
        // TODO: Override MutableOrderedMap.asUnmodifiable
        return this.parameterStatesByName;
    }

    public void reportNameErrors(CompilerErrorState compilerErrorHolder)
    {
        this.parameterStates.forEachWith(AntlrNamedElement::reportNameErrors, compilerErrorHolder);
    }
}
