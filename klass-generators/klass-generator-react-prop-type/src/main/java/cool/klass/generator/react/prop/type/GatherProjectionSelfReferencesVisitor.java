package cool.klass.generator.react.prop.type;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import cool.klass.model.meta.domain.api.projection.ProjectionListener;
import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Stacks;

public class GatherProjectionSelfReferencesVisitor implements ProjectionListener
{
    private final MutableStack<String> context = Stacks.mutable.empty();
    private final MutableList<String>  result  = Lists.mutable.empty();
    private final Projection           originalProjection;

    public GatherProjectionSelfReferencesVisitor(Projection originalProjection)
    {
        this.originalProjection = Objects.requireNonNull(originalProjection);
    }

    public ImmutableList<String> getResult()
    {
        return this.result.toImmutable();
    }

    @Override
    public void enterProjection(Projection projection)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjection() not implemented yet");
    }

    @Override
    public void exitProjection(Projection projection)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjection() not implemented yet");
    }

    @Override
    public void enterProjectionAssociationEnd(@Nonnull ProjectionAssociationEnd projectionAssociationEnd)
    {
        this.context.push(projectionAssociationEnd.getName());
        projectionAssociationEnd.getChildren().forEachWith(ProjectionElement::visit, this);
    }

    @Override
    public void exitProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd)
    {
        this.context.pop();
    }

    @Override
    public void enterProjectionProjectionReference(@Nonnull ProjectionProjectionReference projectionProjectionReference)
    {
        this.context.push(projectionProjectionReference.getName());
        if (projectionProjectionReference.getProjection() != this.originalProjection)
        {
            return;
        }

        AssociationEnd associationEnd = projectionProjectionReference.getProperty();
        Multiplicity   multiplicity     = associationEnd.getMultiplicity();
        String         isRequiredSuffix = multiplicity.isRequired() || multiplicity.isToMany() ? ".isRequired" : "";
        String toOnePropType = this.originalProjection.getName() + isRequiredSuffix;
        String propType = multiplicity.isToMany()
                ? "PropTypes.arrayOf(" + toOnePropType + ").isRequired"
                : toOnePropType;

        String result = String.format(
                "%s.%s = %s;\n",
                this.originalProjection.getName(),
                this.context.makeString("."),
                propType);

        this.result.add(result);
    }

    @Override
    public void exitProjectionProjectionReference(ProjectionProjectionReference projectionProjectionReference)
    {
        this.context.pop();
    }

    @Override
    public void enterProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty)
    {
        // Deliberately empty
    }

    @Override
    public void exitProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty)
    {
        // Deliberately empty
    }
}
