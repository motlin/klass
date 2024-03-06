package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.domain.Type.TypeBuilder;

public interface AntlrType
{
    default TypeBuilder getTypeBuilder()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getTypeBuilder() not implemented yet");
    }
}
