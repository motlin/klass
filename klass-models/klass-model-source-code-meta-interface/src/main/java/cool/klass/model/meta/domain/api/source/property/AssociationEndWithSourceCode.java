package cool.klass.model.meta.domain.api.source.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.source.KlassWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;

public interface AssociationEndWithSourceCode
        extends AssociationEnd, ReferencePropertyWithSourceCode
{
    @Override
    AssociationEndContext getElementContext();

    @Nonnull
    @Override
    KlassWithSourceCode getType();
}
