package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.domain.api.Type.TypeGetter;

public interface AntlrType
{
    TypeGetter getTypeGetter();

    TypeGetter getElementBuilder();
}
