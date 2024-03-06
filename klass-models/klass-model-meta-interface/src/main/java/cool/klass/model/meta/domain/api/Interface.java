package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.Property;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface Interface extends Classifier
{
    // TODO: Replace with an implementation that preserves order
    @Override
    @Nonnull
    default ImmutableList<Property> getProperties()
    {
        return Lists.immutable.<Property>empty()
                .newWithAll(this.getDataTypeProperties());
    }

    @Override
    default String getSourceCodeWithInference()
    {
        String sourceCode = this.getSourceCode();
        if (this.isInferred())
        {
            return sourceCode;
        }

        return ""
                + "interface" + ' ' + this.getName() + '\n'
                + this.getImplementsSourceCode()
                + this.getModifiersSourceCode()
                + "{\n"
                + this.getPropertiesSourceCode()
                + "}\n";
    }
}
