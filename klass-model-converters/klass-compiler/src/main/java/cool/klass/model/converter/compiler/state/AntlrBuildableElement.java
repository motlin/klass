package cool.klass.model.converter.compiler.state;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.AbstractElement.ElementBuilder;

public interface AntlrBuildableElement<BuiltElement extends AbstractElement> extends IAntlrElement
{
    @Nonnull
    ElementBuilder<BuiltElement> build();
}
