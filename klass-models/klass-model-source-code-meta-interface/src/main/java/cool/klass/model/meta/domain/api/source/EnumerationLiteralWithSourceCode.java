package cool.klass.model.meta.domain.api.source;

import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;

public interface EnumerationLiteralWithSourceCode
        extends EnumerationLiteral, NamedElementWithSourceCode
{
    @Override
    EnumerationLiteralContext getElementContext();
}
