package cool.klass.model.meta.domain.api.source;

import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;

public interface EnumerationWithSourceCode
        extends Enumeration, TopLevelElementWithSourceCode
{
    @Override
    EnumerationDeclarationContext getElementContext();
}
