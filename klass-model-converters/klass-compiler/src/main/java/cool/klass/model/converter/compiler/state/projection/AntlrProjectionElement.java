package cool.klass.model.converter.compiler.state.projection;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionElementBuilder;

public interface AntlrProjectionElement extends IAntlrElement
{
    @Nonnull
    ProjectionElementBuilder build();

    void build2();

    @Nonnull
    AntlrProjectionParent getParent();

    @Nonnull
    @Override
    default Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.getParent());
    }

    @Nonnull
    String getName();

    void reportDuplicateMemberName(@Nonnull CompilerErrorState compilerErrorHolder);

    void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder);
}
