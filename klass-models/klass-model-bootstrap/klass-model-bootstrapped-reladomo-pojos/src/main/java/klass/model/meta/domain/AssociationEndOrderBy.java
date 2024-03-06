package klass.model.meta.domain;

public class AssociationEndOrderBy extends AssociationEndOrderByAbstract
{
    public AssociationEndOrderBy()
    {
        // You must not modify this constructor. Mithra calls this internally.
        // You can call this constructor. You can also add new constructors.
    }

    @Override
    public String toString()
    {
        return this.getThisMemberReferencePath() + " " + this.getOrderByDirection();
    }
}
