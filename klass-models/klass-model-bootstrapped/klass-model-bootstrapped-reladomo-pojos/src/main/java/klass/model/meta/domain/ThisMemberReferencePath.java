package klass.model.meta.domain;

public class ThisMemberReferencePath
        extends ThisMemberReferencePathAbstract
{
    public ThisMemberReferencePath()
    {
        // You must not modify this constructor. Mithra calls this internally.
        // You can call this constructor. You can also add new constructors.
    }

    @Override
    public String toString()
    {
        MemberReferencePath memberReferencePath = this.getMemberReferencePathSuperClass();
        String associationEndsString = memberReferencePath.getAssociationEnds().isEmpty()
                ? "."
                : memberReferencePath.getAssociationEnds()
                        .asEcList()
                        .collect(MemberReferencePathAssociationEndMappingAbstract::getAssociationEndName)
                        .makeString(".", ".", ".");

        return "this" + associationEndsString + memberReferencePath.getPropertyName();
    }
}
