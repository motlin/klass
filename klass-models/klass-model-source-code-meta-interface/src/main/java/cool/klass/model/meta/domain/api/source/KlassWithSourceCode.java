package cool.klass.model.meta.domain.api.source;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;

public interface KlassWithSourceCode
        extends Klass, ClassifierWithSourceCode
{
    @Override
    ClassDeclarationContext getElementContext();
}
