package cool.klass.model.reladomo.tree;

import java.util.Optional;

public interface ReladomoTreeNodeToManyAwareListener
{
    default void assertInvariants()
    {
    }

    void enterListIndex(int index);

    void exitListIndex(int index);

    Optional<Integer> enterRoot(RootReladomoTreeNode rootReladomoTreeNode);

    void exitRoot(RootReladomoTreeNode rootReladomoTreeNode);

    void enterDataTypeProperty(DataTypePropertyReladomoTreeNode dataTypePropertyReladomoTreeNode);

    void exitDataTypeProperty(DataTypePropertyReladomoTreeNode dataTypePropertyReladomoTreeNode);

    void enterSuperClass(SuperClassReladomoTreeNode superClassReladomoTreeNode);

    void exitSuperClass(SuperClassReladomoTreeNode superClassReladomoTreeNode);

    void enterSubClass(SubClassReladomoTreeNode subClassReladomoTreeNode);

    void exitSubClass(SubClassReladomoTreeNode subClassReladomoTreeNode);

    Optional<Integer> enterReferenceProperty(ReferencePropertyReladomoTreeNode referencePropertyReladomoTreeNode);

    void exitReferenceProperty(ReferencePropertyReladomoTreeNode referencePropertyReladomoTreeNode);

    Optional<Integer> enterReference(ReferenceReladomoTreeNode referenceReladomoTreeNode);

    void exitReference(ReferenceReladomoTreeNode referenceReladomoTreeNode);
}
