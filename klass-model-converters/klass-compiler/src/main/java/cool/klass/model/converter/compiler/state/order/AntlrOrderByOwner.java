package cool.klass.model.converter.compiler.state.order;

import java.util.Optional;

import cool.klass.model.converter.compiler.state.IAntlrElement;

public interface AntlrOrderByOwner extends IAntlrElement
{
    void setOrderByState(Optional<AntlrOrderBy> orderByState);
}
