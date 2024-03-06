package cool.klass.model.converter.compiler.state.projection;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionElementBuilder;

public interface AntlrProjectionElement extends IAntlrElement
{
    @Nonnull
    ProjectionElementBuilder build();

    void build2();

    void visit(@Nonnull AntlrProjectionVisitor visitor);

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

    void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder);

    void reportDuplicateMemberName(@Nonnull CompilerAnnotationState compilerAnnotationHolder);
}
