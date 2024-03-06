package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.state.parameter.AntlrEnumerationParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterOwner;
import cool.klass.model.converter.compiler.state.parameter.AntlrPrimitiveParameter;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public final class ParameterHolder implements AntlrParameterOwner
{
    private final MutableList<AntlrParameter<?>>               parameterStates       = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrParameter<?>> parameterStatesByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableList<AntlrPrimitiveParameter>               primitiveParameterStates       = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrPrimitiveParameter> primitiveParameterStatesByName =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableList<AntlrEnumerationParameter>               enumerationParameterStates       = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrEnumerationParameter> enumerationParameterStatesByName =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    @Override
    public int getNumParameters()
    {
        return this.parameterStates.size();
    }

    public MutableList<AntlrParameter<?>> getParameterStates()
    {
        return this.parameterStates.asUnmodifiable();
    }

    @Nonnull
    public MutableOrderedMap<String, AntlrParameter<?>> getParameterStatesByName()
    {
        // TODO: Override MutableOrderedMap.asUnmodifiable
        return this.parameterStatesByName;
    }

    @Override
    public void enterParameterDeclaration(@Nonnull AntlrParameter<?> parameterState)
    {
        if (parameterState instanceof AntlrPrimitiveParameter)
        {
            this.enterPrimitiveParameterDeclaration((AntlrPrimitiveParameter) parameterState);
        }
        else if (parameterState instanceof AntlrEnumerationParameter)
        {
            this.enterEnumerationParameterDeclaration((AntlrEnumerationParameter) parameterState);
        }
        else
        {
            throw new AssertionError();
        }
    }

    @Override
    public void enterPrimitiveParameterDeclaration(@Nonnull AntlrPrimitiveParameter primitiveParameterState)
    {
        this.parameterStates.add(primitiveParameterState);
        this.parameterStatesByName.compute(
            primitiveParameterState.getName(),
            (name, builder) -> builder == null
                    ? primitiveParameterState
                    : AntlrPrimitiveParameter.AMBIGUOUS);

        this.primitiveParameterStates.add(primitiveParameterState);
        this.primitiveParameterStatesByName.compute(
                primitiveParameterState.getName(),
                (name, builder) -> builder == null
                        ? primitiveParameterState
                        : AntlrPrimitiveParameter.AMBIGUOUS);
    }

    @Override
    public void enterEnumerationParameterDeclaration(@Nonnull AntlrEnumerationParameter enumerationParameterState)
    {
        this.parameterStates.add(enumerationParameterState);
        this.parameterStatesByName.compute(
                enumerationParameterState.getName(),
                (name, builder) -> builder == null
                        ? enumerationParameterState
                        : AntlrEnumerationParameter.AMBIGUOUS);

        this.enumerationParameterStates.add(enumerationParameterState);
        this.enumerationParameterStatesByName.compute(
                enumerationParameterState.getName(),
                (name, builder) -> builder == null
                        ? enumerationParameterState
                        : AntlrEnumerationParameter.AMBIGUOUS);
    }
}
