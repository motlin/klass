package cool.klass.model.converter.compiler.state.order;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.state.IAntlrElement;

public interface AntlrOrderByOwner extends IAntlrElement
{
    void enterOrderByDeclaration(@Nonnull Optional<AntlrOrderBy> orderByState);
}
