package cool.klass.model.converter.compiler;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassModifier;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrInterface;
import cool.klass.model.converter.compiler.state.AntlrTopLevelElement;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.AntlrServiceGroup;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageNameContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.TopLevelDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;

// TODO: Null checks could be replaced by flags indicating whether certain data is populated yet based on the current phase.
public class CompilerWalkState
{
    private final AntlrDomainModel domainModelState;

    @Nullable
    private CompilationUnit currentCompilationUnit;

    @Nullable
    private String packageName;

    @Nullable
    private AntlrTopLevelElement       topLevelDeclarationState;
    @Nullable
    private AntlrEnumeration           enumerationState;
    @Nullable
    private AntlrClassifier            classifierState;
    @Nullable
    private AntlrInterface             interfaceState;
    @Nullable
    private AntlrClass                 classState;
    @Nullable
    private AntlrAssociation           associationState;
    @Nullable
    private AntlrAssociationEnd        associationEndState;
    @Nullable
    private AntlrParameterizedProperty parameterizedPropertyState;
    @Nullable
    private AntlrProjection            projectionState;
    @Nullable
    private AntlrServiceGroup          serviceGroupState;
    @Nullable
    private AntlrUrl                   urlState;
    @Nullable
    private AntlrService               serviceState;

    @Nullable
    private AntlrClassifier    thisReference;
    @Nullable
    private AntlrOrderByOwner  orderByOwnerState;
    @Nullable
    private AntlrClassModifier classModifierState;

    public CompilerWalkState(AntlrDomainModel domainModelState)
    {
        this.domainModelState = domainModelState;
    }

    @Nullable
    public String getPackageName()
    {
        return this.packageName;
    }

    @Nullable
    public AntlrClassifier getClassifierState()
    {
        return this.classifierState;
    }

    @Nullable
    public AntlrInterface getInterfaceState()
    {
        return this.interfaceState;
    }

    @Nullable
    public AntlrClass getClassState()
    {
        return this.classState;
    }

    @Nullable
    public AntlrAssociation getAssociationState()
    {
        return this.associationState;
    }

    @Nullable
    public AntlrServiceGroup getServiceGroupState()
    {
        return this.serviceGroupState;
    }

    @Nullable
    public AntlrUrl getUrlState()
    {
        return this.urlState;
    }

    @Nullable
    public AntlrProjection getProjectionState()
    {
        return this.projectionState;
    }

    @Nullable
    public AntlrService getServiceState()
    {
        return this.serviceState;
    }

    @Nullable
    public AntlrOrderByOwner getOrderByOwnerState()
    {
        return this.orderByOwnerState;
    }

    @Nullable
    public AntlrClassifier getThisReference()
    {
        return this.thisReference;
    }

    @Nullable
    public AntlrClassModifier getClassModifierState()
    {
        return this.classModifierState;
    }

    public void withCompilationUnit(CompilationUnit compilationUnit, @Nonnull Runnable runnable)
    {
        CompilationUnit oldCompilationUnit = this.currentCompilationUnit;

        try
        {
            this.currentCompilationUnit = compilationUnit;
            runnable.run();
        }
        finally
        {
            this.currentCompilationUnit = oldCompilationUnit;
        }
    }

    @Nullable
    public CompilationUnit getCurrentCompilationUnit()
    {
        return Objects.requireNonNull(this.currentCompilationUnit);
    }

    public void enterCompilationUnit(CompilationUnit currentCompilationUnit)
    {
        this.currentCompilationUnit = currentCompilationUnit;
    }

    public void exitCompilationUnit()
    {
        this.currentCompilationUnit = null;
        this.packageName            = null;
    }

    public void enterPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
    {
        PackageNameContext packageNameContext = ctx.packageName();
        this.packageName = packageNameContext.getText();
    }

    public void enterTopLevelDeclaration(TopLevelDeclarationContext ctx)
    {
        CompilerWalkState.assertNull(this.topLevelDeclarationState);
        this.topLevelDeclarationState = this.domainModelState.getTopLevelElementByContext(ctx);
    }

    public void exitTopLevelDeclaration()
    {
        this.topLevelDeclarationState = null;
    }

    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        CompilerWalkState.assertNull(this.enumerationState);
        this.enumerationState = this.domainModelState.getEnumerationByContext(ctx);
    }

    public void exitEnumerationDeclaration()
    {
        this.enumerationState = null;
    }

    public void enterInterfaceDeclaration(InterfaceDeclarationContext ctx)
    {
        CompilerWalkState.assertNull(this.classifierState);
        CompilerWalkState.assertNull(this.interfaceState);
        CompilerWalkState.assertNull(this.thisReference);

        AntlrInterface interfaceByContext = this.domainModelState.getInterfaceByContext(ctx);

        this.classifierState = interfaceByContext;
        this.interfaceState  = interfaceByContext;
        this.thisReference   = interfaceByContext;
    }

    public void exitInterfaceDeclaration()
    {
        this.classifierState = null;
        this.interfaceState  = null;
        this.thisReference   = null;
    }

    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        CompilerWalkState.assertNull(this.classifierState);
        CompilerWalkState.assertNull(this.classState);
        CompilerWalkState.assertNull(this.thisReference);

        AntlrClass classByContext = this.domainModelState.getClassByContext(ctx);

        this.classifierState = classByContext;
        this.classState      = classByContext;
        this.thisReference   = classByContext;
    }

    public void exitClassDeclaration()
    {
        this.classifierState = null;
        this.classState      = null;
        this.thisReference   = null;
    }

    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        CompilerWalkState.assertNull(this.associationState);
        this.associationState = this.domainModelState.getAssociationByContext(ctx);
    }

    public void exitAssociationDeclaration()
    {
        this.associationState = null;
    }

    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        CompilerWalkState.assertNull(this.associationEndState);
        CompilerWalkState.assertNull(this.thisReference);
        CompilerWalkState.assertNull(this.orderByOwnerState);

        if (this.associationState == null)
        {
            return;
        }

        // super.enterAssociationEnd(ctx);
        this.associationEndState = this.associationState.getAssociationEndByContext(ctx);
        if (this.associationEndState == null)
        {
            return;
        }
        this.orderByOwnerState = this.associationEndState;
        this.thisReference     = this.associationEndState.getType();
    }

    public void exitAssociationEnd()
    {
        this.associationEndState = null;
        this.thisReference       = null;
        this.orderByOwnerState   = null;
    }

    public void enterRelationship()
    {
        CompilerWalkState.assertNull(this.thisReference);

        if (this.associationState == null)
        {
            return;
        }

        this.thisReference = this.associationState.getAssociationEndStates()
                .getFirstOptional()
                .map(AntlrAssociationEnd::getType)
                .orElse(AntlrClass.NOT_FOUND);
    }

    public void exitRelationship()
    {
        this.thisReference = null;
    }

    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        CompilerWalkState.assertNull(this.projectionState);

        AntlrProjection projectionState = this.domainModelState.getProjectionByContext(ctx);
        this.projectionState = projectionState;
    }

    public void exitProjectionDeclaration()
    {
        this.projectionState = null;
    }

    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        CompilerWalkState.assertNull(this.parameterizedPropertyState);
        CompilerWalkState.assertNull(this.orderByOwnerState);

        if (this.classState == null)
        {
            return;
        }
        this.parameterizedPropertyState = this.classState.getParameterizedPropertyByContext(ctx);
        this.orderByOwnerState          = this.parameterizedPropertyState;
    }

    public void exitParameterizedProperty()
    {
        this.parameterizedPropertyState = null;
        this.orderByOwnerState          = null;
    }

    public void defineInterface(AntlrInterface interfaceState)
    {
        this.classifierState = interfaceState;
        this.interfaceState  = interfaceState;
        this.domainModelState.defineInterface(this.interfaceState);
    }

    public void defineClass(AntlrClass classState)
    {
        this.classifierState = classState;
        this.classState      = classState;
        this.domainModelState.defineClass(this.classState);
    }

    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        CompilerWalkState.assertNull(this.serviceGroupState);
        CompilerWalkState.assertNull(this.thisReference);

        this.serviceGroupState = this.domainModelState.getServiceGroupByContext(ctx);
        if (this.serviceGroupState == null)
        {
            return;
        }
        this.thisReference = this.serviceGroupState.getKlass();
    }

    public void exitServiceGroupDeclaration()
    {
        this.serviceGroupState = null;
        this.thisReference     = null;
    }

    public void enterUrlDeclaration(UrlDeclarationContext ctx)
    {
        CompilerWalkState.assertNull(this.urlState);
        if (this.serviceGroupState == null)
        {
            return;
        }
        this.urlState = this.serviceGroupState.getUrlByContext(ctx);
    }

    public void exitUrlDeclaration()
    {
        this.urlState = null;
    }

    public void enterServiceDeclaration(ServiceDeclarationContext ctx)
    {
        CompilerWalkState.assertNull(this.serviceState);
        CompilerWalkState.assertNull(this.orderByOwnerState);

        if (this.urlState == null)
        {
            return;
        }
        this.serviceState = this.urlState.getServiceByContext(ctx);
        if (this.serviceState == null)
        {
            return;
        }
        this.orderByOwnerState = this.serviceState;
    }

    public void exitServiceDeclaration()
    {
        this.serviceState      = null;
        this.orderByOwnerState = null;
    }

    public void enterClassModifier(ClassModifierContext ctx)
    {
        CompilerWalkState.assertNull(this.classModifierState);

        if (this.classifierState == null)
        {
            return;
        }

        this.classModifierState = this.classifierState.getClassModifierByContext(ctx);
    }

    public void exitClassModifier()
    {
        this.classModifierState = null;
    }

    private static void assertNull(@Nullable Object object)
    {
        if (object != null)
        {
            throw new IllegalStateException();
        }
    }

    @Nonnull
    public CompilerWalkState withCompilationUnit(CompilationUnit compilationUnit)
    {
        // TODO: It's too easy for this list to get out of sync with the declared fields
        CompilerWalkState compilerWalkState = new CompilerWalkState(this.domainModelState);
        compilerWalkState.currentCompilationUnit     = compilationUnit;
        compilerWalkState.packageName                = this.packageName;
        compilerWalkState.topLevelDeclarationState   = this.topLevelDeclarationState;
        compilerWalkState.enumerationState           = this.enumerationState;
        compilerWalkState.classifierState            = this.classifierState;
        compilerWalkState.interfaceState             = this.interfaceState;
        compilerWalkState.classState                 = this.classState;
        compilerWalkState.associationState           = this.associationState;
        compilerWalkState.associationEndState        = this.associationEndState;
        compilerWalkState.parameterizedPropertyState = this.parameterizedPropertyState;
        compilerWalkState.projectionState            = this.projectionState;
        compilerWalkState.serviceGroupState          = this.serviceGroupState;
        compilerWalkState.urlState                   = this.urlState;
        compilerWalkState.serviceState               = this.serviceState;
        compilerWalkState.thisReference              = this.thisReference;
        compilerWalkState.orderByOwnerState          = this.orderByOwnerState;
        return compilerWalkState;
    }

    public void assertEmpty()
    {
        if (this.packageName != null)
        {
            throw new AssertionError();
        }
        if (this.enumerationState != null)
        {
            throw new AssertionError();
        }
        if (this.classifierState != null)
        {
            throw new AssertionError();
        }
        if (this.interfaceState != null)
        {
            throw new AssertionError();
        }
        if (this.classState != null)
        {
            throw new AssertionError();
        }
        if (this.associationState != null)
        {
            throw new AssertionError();
        }
        if (this.associationEndState != null)
        {
            throw new AssertionError();
        }
        if (this.parameterizedPropertyState != null)
        {
            throw new AssertionError();
        }
        if (this.projectionState != null)
        {
            throw new AssertionError();
        }
        if (this.serviceGroupState != null)
        {
            throw new AssertionError();
        }
        if (this.urlState != null)
        {
            throw new AssertionError();
        }
        if (this.serviceState != null)
        {
            throw new AssertionError();
        }
        if (this.thisReference != null)
        {
            throw new AssertionError();
        }
        if (this.orderByOwnerState != null)
        {
            throw new AssertionError();
        }
    }
}
