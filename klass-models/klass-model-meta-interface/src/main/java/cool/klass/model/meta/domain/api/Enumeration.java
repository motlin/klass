package cool.klass.model.meta.domain.api;

import org.eclipse.collections.api.list.ImmutableList;

public interface Enumeration
        extends DataType, TopLevelElement
{
    @Override
    default void visit(TopLevelElementVisitor visitor)
    {
        visitor.visitEnumeration(this);
    }

    @Override
    default String getDataTypeName()
    {
        return this.getName();
    }

    ImmutableList<EnumerationLiteral> getEnumerationLiterals();
}
