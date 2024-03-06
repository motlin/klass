package klass.model.meta.domain;

public class AssociationEnd extends AssociationEndAbstract
{
    public AssociationEnd()
    {
        // You must not modify this constructor. Mithra calls this internally.
        // You can call this constructor. You can also add new constructors.
    }

    @Override
    public AssociationEndOrderByList getOrderBys()
    {
        AssociationEndOrderByList orderBys = super.getOrderBys();
        if (orderBys.size() > 1)
        {
            orderBys.size();
        }
        return orderBys;
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s.%s: %s[%s]",
                this.getOwningClass().getName(),
                this.getName(),
                this.getResultType().getName(),
                this.getMultiplicity());
    }
}
