package cool.klass.model.converter.compiler.state;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.meta.domain.TopLevelElement.TopLevelElementBuilder;

public interface AntlrTopLevelElement extends IAntlrElement
{
    TopLevelElementBuilder getElementBuilder();

    @Override
    default boolean omitParentFromSurroundingElements()
    {
        return false;
    }

    @Override
    default Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.empty();
    }

    default void reportDuplicateTopLevelName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_TOP: Duplicate top level item name: '%s'.", this.getName());
        compilerErrorHolder.add(message, this);
    }

    @Nonnull
    String getName();
}
