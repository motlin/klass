package cool.klass.model.reladomo.tree;

public interface ReladomoTreeNodeListener
{
    default void assertInvariants()
    {
    }

    void enterRoot(RootReladomoTreeNode rootReladomoTreeNode);

    void exitRoot(RootReladomoTreeNode rootReladomoTreeNode);

    void enterDataTypeProperty(DataTypePropertyReladomoTreeNode dataTypePropertyReladomoTreeNode);

    void exitDataTypeProperty(DataTypePropertyReladomoTreeNode dataTypePropertyReladomoTreeNode);

    void enterSuperClass(SuperClassReladomoTreeNode superClassReladomoTreeNode);

    void exitSuperClass(SuperClassReladomoTreeNode superClassReladomoTreeNode);

    void enterSubClass(SubClassReladomoTreeNode subClassReladomoTreeNode);

    void exitSubClass(SubClassReladomoTreeNode subClassReladomoTreeNode);

    void enterReferenceProperty(ReferencePropertyReladomoTreeNode referencePropertyReladomoTreeNode);

    void exitReferenceProperty(ReferencePropertyReladomoTreeNode referencePropertyReladomoTreeNode);

    void enterReference(ReferenceReladomoTreeNode referenceReladomoTreeNode);

    void exitReference(ReferenceReladomoTreeNode referenceReladomoTreeNode);
}
