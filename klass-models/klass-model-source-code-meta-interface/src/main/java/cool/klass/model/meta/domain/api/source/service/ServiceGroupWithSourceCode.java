package cool.klass.model.meta.domain.api.source.service;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.service.ServiceGroup;
import cool.klass.model.meta.domain.api.source.KlassWithSourceCode;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;

public interface ServiceGroupWithSourceCode
        extends ServiceGroup, TopLevelElementWithSourceCode
{
    @Override
    ServiceGroupDeclarationContext getElementContext();

    @Nonnull
    @Override
    KlassWithSourceCode getKlass();
}
