package cool.klass.model.meta.domain.api;

import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.service.ServiceGroup;

public interface TopLevelElementVisitor
{
    void visitEnumeration(Enumeration enumeration);

    void visitInterface(Interface anInterface);

    void visitKlass(Klass klass);

    void visitAssociation(Association association);

    void visitProjection(Projection projection);

    void visitServiceGroup(ServiceGroup serviceGroup);
}
