package cool.klass.model.meta.domain.api.source.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.source.ElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.KlassWithSourceCode;
import cool.klass.model.meta.domain.api.source.property.DataTypePropertyWithSourceCode;
import cool.klass.model.meta.domain.api.value.TypeMemberReferencePath;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferencePathContext;

public interface TypeMemberReferencePathWithSourceCode
        extends TypeMemberReferencePath, ElementWithSourceCode
{
    @Override
    TypeMemberReferencePathContext getElementContext();

    @Nonnull
    @Override
    KlassWithSourceCode getKlass();

    @Nonnull
    @Override
    DataTypePropertyWithSourceCode getProperty();
}
