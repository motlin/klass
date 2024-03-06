package cool.klass.model.meta.domain.api.source.property;

import cool.klass.model.meta.domain.api.property.AssociationEndSignature;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;

public interface AssociationEndSignatureWithSourceCode
        extends AssociationEndSignature, ReferencePropertyWithSourceCode
{
    @Override
    AssociationEndSignatureContext getElementContext();
}
