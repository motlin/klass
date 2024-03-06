package cool.klass.model.meta.domain.reference;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionChild;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;
import cool.klass.model.meta.domain.api.projection.ProjectionReferenceProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionVisitor;
import cool.klass.model.meta.domain.api.source.projection.ProjectionDataTypePropertyWithSourceCode;
import cool.klass.model.meta.domain.api.source.projection.ProjectionProjectionReferenceWithSourceCode;
import cool.klass.model.meta.domain.api.source.projection.ProjectionReferencePropertyWithSourceCode;
import cool.klass.model.meta.domain.api.source.projection.ProjectionWithSourceCode;
import cool.klass.model.meta.domain.api.source.property.DataTypePropertyWithSourceCode;
import cool.klass.model.meta.domain.api.source.property.ReferencePropertyWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferencePropertyContext;

public class DomainModelReferencesProjectionVisitor
        implements ProjectionVisitor
{
    @Nonnull
    private final DomainModelReferences domainModelReferences;

    public DomainModelReferencesProjectionVisitor(@Nonnull DomainModelReferences domainModelReferences)
    {
        this.domainModelReferences = Objects.requireNonNull(domainModelReferences);
    }

    @Override
    public void visitProjection(Projection projection)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitProjection() not implemented yet");
    }

    @Override
    public void visitProjectionReferenceProperty(ProjectionReferenceProperty projectionReferenceProperty)
    {
        ProjectionReferencePropertyWithSourceCode elementWithSourceCode = (ProjectionReferencePropertyWithSourceCode) projectionReferenceProperty;
        ProjectionReferencePropertyContext        elementContext        = elementWithSourceCode.getElementContext();
        IdentifierContext                         reference             = elementContext.identifier();
        ReferencePropertyWithSourceCode           element               = elementWithSourceCode.getProperty();

        this.domainModelReferences.addReferencePropertyReference(reference, element);

        for (ProjectionChild projectionChild : projectionReferenceProperty.getChildren())
        {
            projectionChild.visit(this);
        }
    }

    @Override
    public void visitProjectionProjectionReference(ProjectionProjectionReference projectionProjectionReference)
    {
        ProjectionProjectionReferenceWithSourceCode elementWithSourceCode = (ProjectionProjectionReferenceWithSourceCode) projectionProjectionReference;
        ProjectionProjectionReferenceContext        elementContext        = elementWithSourceCode.getElementContext();
        IdentifierContext                           reference             = elementContext.identifier();
        ReferencePropertyWithSourceCode             element               = elementWithSourceCode.getProperty();

        this.domainModelReferences.addReferencePropertyReference(reference, element);

        ProjectionReferenceContext projectionReference = elementContext.projectionReference();
        ProjectionWithSourceCode   projection          = elementWithSourceCode.getProjection();

        this.domainModelReferences.addProjectionReference(projectionReference, projection);
    }

    @Override
    public void visitProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty)
    {
        ProjectionDataTypePropertyWithSourceCode elementWithSourceCode = (ProjectionDataTypePropertyWithSourceCode) projectionDataTypeProperty;
        ProjectionPrimitiveMemberContext         elementContext        = elementWithSourceCode.getElementContext();
        IdentifierContext                        reference             = elementContext.identifier();
        DataTypePropertyWithSourceCode           element               = elementWithSourceCode.getProperty();

        this.domainModelReferences.addDataTypePropertyReference(reference, element);
    }
}
