package klass.model.meta.domain;

public class ThisMemberReferencePath extends ThisMemberReferencePathAbstract
{
    public ThisMemberReferencePath()
    {
        // You must not modify this constructor. Mithra calls this internally.
        // You can call this constructor. You can also add new constructors.
    }

    @Override
    public String toString()
    {
        String associationEndsString = this.getAssociationEnds().isEmpty()
                ? ""
                : this.getAssociationEnds()
                        .asEcList()
                        .collect(MemberReferencePathAssociationEndMappingAbstract::getAssociationEndName)
                        .makeString(".");

        return "this" + associationEndsString + "." + this.getPropertyName();
    }
}
