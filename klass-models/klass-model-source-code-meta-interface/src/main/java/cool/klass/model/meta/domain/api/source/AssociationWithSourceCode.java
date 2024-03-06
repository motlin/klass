package cool.klass.model.meta.domain.api.source;

import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;

public interface AssociationWithSourceCode
        extends Association, TopLevelElementWithSourceCode
{
    @Override
    AssociationDeclarationContext getElementContext();
}
