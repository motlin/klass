package cool.klass.model.converter.compiler.state;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.meta.domain.api.TopLevelElement.TopLevelElementBuilder;

public interface AntlrTopLevelElement extends IAntlrElement
{
    @Nonnull
    TopLevelElementBuilder getElementBuilder();

    @Override
    default boolean omitParentFromSurroundingElements()
    {
        return false;
    }

    @Nonnull
    @Override
    default Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.empty();
    }

    default void reportDuplicateTopLevelName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format("Duplicate top level item name: '%s'.", this.getName());
        compilerErrorHolder.add("ERR_DUP_TOP", message, this);
    }

    @Nonnull
    String getName();

    int getOrdinal();
}
