package cool.klass.model.reladomo.projection;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.projection.ProjectionChild;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;
import cool.klass.model.meta.domain.api.projection.ProjectionReferenceProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionWithReferenceProperty;
import cool.klass.model.meta.domain.api.property.Property;
import org.eclipse.collections.api.factory.Stacks;
import org.eclipse.collections.api.stack.MutableStack;

public final class ReladomoProjectionConverter
{
    public static final Converter<String, String> UPPER_TO_LOWER_CAMEL =
            CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL);

    private ReladomoProjectionConverter()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static void projectionChildrenToReladomoTree(
            ProjectionElementReladomoNode reladomoNode,
            ProjectionParent projectionParent)
    {
        for (ProjectionChild projectionChild : projectionParent.getChildren())
        {
            projectionElementToReladomoTree(reladomoNode, projectionChild);
        }
    }

    private static void projectionElementToReladomoTree(
            ProjectionElementReladomoNode projectionReladomoNode,
            ProjectionChild projectionChild)
    {
        Property property = projectionChild.getProperty();
        Classifier declaredClassifier = projectionChild.getDeclaredClassifier();
        String name = property.getName();

        ProjectionElementReladomoNode reladomoNode = projectionReladomoNode;
        reladomoNode = getInheritancePath(reladomoNode, property.getOwningClassifier());
        if (reladomoNode == null)
        {
            return;
        }

        if (!(projectionChild instanceof ProjectionWithReferenceProperty projectionWithReferenceProperty))
        {
            return;
        }

        if (projectionWithReferenceProperty instanceof ProjectionProjectionReference projectionProjectionReference)
        {
            var childNode = new ProjectionProjectionReferenceReladomoNode(name, projectionProjectionReference);
            reladomoNode = reladomoNode.computeChild(name, childNode);
        }
        else if (projectionWithReferenceProperty instanceof ProjectionReferenceProperty projectionReferenceProperty)
        {
            var childNode = new ProjectionReferencePropertyReladomoNode(name, projectionReferenceProperty);
            reladomoNode = reladomoNode.computeChild(name, childNode);
            projectionChildrenToReladomoTree(reladomoNode, projectionReferenceProperty);
        }
    }

    public static ProjectionElementReladomoNode getInheritancePath(
            ProjectionElementReladomoNode reladomoNode,
            Classifier end)
    {
        ProjectionElementReladomoNode eachReladomoNode = reladomoNode;
        Classifier start = reladomoNode.getType();

        if (start.isStrictSubTypeOf(end))
        {
            Klass eachKlass = (Klass) start;
            while (eachKlass != end)
            {
                Klass superClass = eachKlass.getSuperClass().orElse(null);
                if (superClass == null)
                {
                    return eachReladomoNode;
                }
                String name          = UPPER_TO_LOWER_CAMEL.convert(superClass.getName()) + "SuperClass";
                var nextReladomoNode = new SuperClassReladomoNode(name, eachKlass, superClass);
                eachReladomoNode = eachReladomoNode.computeChild(name, nextReladomoNode);
                eachKlass        = superClass;
            }
            return eachReladomoNode;
        }

        if (start.isStrictSuperTypeOf(end))
        {
            MutableStack<Klass> stack          = Stacks.mutable.empty();
            Klass               eachClassifier = (Klass) end;
            while (eachClassifier != start)
            {
                Klass superClass = eachClassifier.getSuperClass().get();
                stack.push(eachClassifier);
                eachClassifier = superClass;
            }

            Klass eachClassifier2 = (Klass) start;
            while (stack.notEmpty())
            {
                Klass eachSubClass = stack.pop();
                String name          = UPPER_TO_LOWER_CAMEL.convert(eachSubClass.getName()) + "SubClass";
                var nextReladomoNode = new SubClassReladomoNode(name, eachClassifier2, eachSubClass);
                eachReladomoNode = eachReladomoNode.computeChild(name, nextReladomoNode);
                eachClassifier2  = eachSubClass;
            }

            return eachReladomoNode;
        }

        if (start == end)
        {
            return eachReladomoNode;
        }

        return null;
    }
}
