package cool.klass.generator.react.prop.type;

import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;
import cool.klass.model.meta.domain.api.projection.ProjectionVisitor;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.ImmutableSet;

public class PropTypeSourceCodeProjectionVisitor implements ProjectionVisitor
{
    private final Projection originalProjection;
    private final Instant    now;
    private final int        indentLevel;
    private       String     result;

    PropTypeSourceCodeProjectionVisitor(
            Projection originalProjection,
            Instant now,
            int indentLevel)
    {
        this.originalProjection = Objects.requireNonNull(originalProjection);
        this.now = Objects.requireNonNull(now);
        this.indentLevel = indentLevel;
    }

    private String getPropTypeSourceCode(@Nonnull ProjectionElement projectionElement)
    {
        PropTypeSourceCodeProjectionVisitor visitor = new PropTypeSourceCodeProjectionVisitor(
                this.originalProjection,
                this.now,
                this.indentLevel + 1);
        projectionElement.visit(visitor);
        return visitor.getResult();
    }

    public String getResult()
    {
        return Objects.requireNonNull(this.result);
    }

    @Override
    public void visitProjection(@Nonnull Projection projection)
    {
        GatherProjectionReferencesVisitor visitor = new GatherProjectionReferencesVisitor(projection);
        projection.getChildren().forEachWith(ProjectionElement::visit, visitor);
        ImmutableSet<Projection> referencedProjections = visitor.getReferencedProjections();
        String imports = referencedProjections
                .asLazy()
                .toSortedListBy(PackageableElement::getFullyQualifiedName)
                .collect(each -> "import " + each.getName() + " from './" + each.getName() + "';\n")
                .makeString("");

        String childrenSourceCode = projection
                .getChildren()
                .collect(this::getPropTypeSourceCode)
                .makeString("");

        GatherProjectionSelfReferencesVisitor visitor2 = new GatherProjectionSelfReferencesVisitor(this.originalProjection);
        projection.getChildren().forEachWith(ProjectionElement::visit, visitor2);
        ImmutableList<String> selfReferences = visitor2.getResult();
        String selfReferencesSourceCode = selfReferences
                .makeString("");

        // language=JavaScript
        this.result = ""
                + "import {forbidExtraProps} from 'airbnb-prop-types';\n"
                + "import PropTypes          from 'prop-types';\n"
                + imports
                + "\n"
                + "/**\n"
                + " * Auto-generated by cool.klass.generator.react.prop.type.PropTypeSourceCodeProjectionVisitor\n"
                + " * at " + this.now + "\n"
                + " */\n"
                + "const " + projection.getName() + " = PropTypes.shape(forbidExtraProps({\n"
                + childrenSourceCode
                + "}));\n"
                + "\n"
                + selfReferencesSourceCode
                + "export default " + projection.getName() + ";\n";
    }

    @Override
    public void visitProjectionAssociationEnd(@Nonnull ProjectionAssociationEnd projectionAssociationEnd)
    {
        String childrenSourceCode = projectionAssociationEnd
                .getChildren()
                .collect(this::getPropTypeSourceCode)
                .makeString("");

        String         indent           = ReactPropTypeGenerator.getIndent(this.indentLevel);
        AssociationEnd associationEnd   = projectionAssociationEnd.getProperty();
        Multiplicity   multiplicity     = associationEnd.getMultiplicity();
        String         isRequiredSuffix = multiplicity.isRequired() || multiplicity.isToMany() ? ".isRequired" : "";
        String toOnePropType = "PropTypes.shape(forbidExtraProps({\n"
                + childrenSourceCode
                + indent + "}))"
                + isRequiredSuffix;
        String propType = multiplicity.isToMany()
                ? "PropTypes.arrayOf(" + toOnePropType + ").isRequired"
                : toOnePropType;

        this.result = String.format(
                "%s%s: %s,\n",
                indent,
                projectionAssociationEnd.getName(),
                propType);
    }

    @Override
    public void visitProjectionProjectionReference(@Nonnull ProjectionProjectionReference projectionProjectionReference)
    {
        if (projectionProjectionReference.getProjection() == this.originalProjection)
        {
            this.result = "";
            return;
        }

        String         indent           = ReactPropTypeGenerator.getIndent(this.indentLevel);
        AssociationEnd associationEnd   = projectionProjectionReference.getProperty();
        Multiplicity   multiplicity     = associationEnd.getMultiplicity();
        String         isRequiredSuffix = multiplicity.isRequired() || multiplicity.isToMany() ? ".isRequired" : "";
        String         toOnePropType    = projectionProjectionReference.getProjection().getName() + isRequiredSuffix;
        String propType = multiplicity.isToMany()
                ? "PropTypes.arrayOf(" + toOnePropType + ").isRequired"
                : toOnePropType;

        this.result = String.format(
                "%s%s: %s,\n",
                indent,
                projectionProjectionReference.getName(),
                propType);
    }

    @Override
    public void visitProjectionDataTypeProperty(@Nonnull ProjectionDataTypeProperty projectionDataTypeProperty)
    {
        DataTypeProperty property = projectionDataTypeProperty.getProperty();
        boolean isNullableInfinity = property.isTemporalRange()
                || property.isTemporalInstant() && property.isTo();

        PropTypeSourceCodeDataTypePropertyVisitor visitor = new PropTypeSourceCodeDataTypePropertyVisitor();
        property.visit(visitor);

        String isRequiredSuffix = property.isRequired() && !isNullableInfinity ? ".isRequired" : "";

        String indent = ReactPropTypeGenerator.getIndent(this.indentLevel);

        this.result = String.format(
                "%s%s: PropTypes.%s%s,\n",
                indent,
                projectionDataTypeProperty.getName(),
                visitor.getResult(),
                isRequiredSuffix);
    }
}
