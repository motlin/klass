package cool.klass.model.meta.domain.api.source;

import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;

public interface InterfaceWithSourceCode
        extends Interface, ClassifierWithSourceCode
{
    @Override
    InterfaceDeclarationContext getElementContext();
}
