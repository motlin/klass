package cool.klass.model.meta.domain.api.source.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.source.ElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.property.DataTypePropertyWithSourceCode;
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferencePathContext;

public interface ThisMemberReferencePathWithSourceCode
        extends ThisMemberReferencePath, ElementWithSourceCode
{
    @Override
    ThisMemberReferencePathContext getElementContext();

    @Nonnull
    @Override
    DataTypePropertyWithSourceCode getProperty();
}
