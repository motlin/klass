package cool.klass.model.reladomo.tree;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Klass;

public class SubClassReladomoTreeNode
        extends AbstractReladomoTreeNode
{
    private final Klass klass;
    private final Klass subClass;

    public SubClassReladomoTreeNode(String name, Klass klass, Klass subClass)
    {
        super(name);
        this.klass    = Objects.requireNonNull(klass);
        this.subClass = Objects.requireNonNull(subClass);
    }

    @Override
    public void visit(ReladomoTreeNodeVisitor visitor)
    {
        visitor.visitSubClass(this);
    }

    @Override
    public Klass getOwningClassifier()
    {
        return this.klass;
    }

    @Override
    public Klass getType()
    {
        return this.subClass;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }

        SubClassReladomoTreeNode that = (SubClassReladomoTreeNode) o;

        return this.klass.equals(that.klass) && this.subClass.equals(that.subClass);
    }

    @Override
    public int hashCode()
    {
        int result = this.klass.hashCode();
        result = 31 * result + this.subClass.hashCode();
        return result;
    }
}
