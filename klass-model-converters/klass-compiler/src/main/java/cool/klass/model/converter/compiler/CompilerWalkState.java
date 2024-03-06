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
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndSignature;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.AntlrServiceGroup;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageNameContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
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
    private AntlrTopLevelElement         topLevelDeclarationState;
    @Nullable
    private AntlrEnumeration             enumerationState;
    @Nullable
    private AntlrClassifier              classifierState;
    @Nullable
    private AntlrInterface               interfaceState;
    @Nullable
    private AntlrClass                   classState;
    @Nullable
    private AntlrAssociation             associationState;
    @Nullable
    private AntlrAssociationEnd          associationEndState;
    @Nullable
    private AntlrAssociationEndSignature associationEndSignatureState;
    @Nullable
    private AntlrParameterizedProperty   parameterizedPropertyState;
    @Nullable
    private AntlrProjection              projectionState;
    @Nullable
    private AntlrServiceGroup            serviceGroupState;
    @Nullable
    private AntlrUrl                     urlState;
    @Nullable
    private AntlrService                 serviceState;

    @Nullable
    private AntlrClassifier    thisReference;
    @Nullable
    private AntlrOrderByOwner  orderByOwnerState;
    @Nullable
    private AntlrClassModifier classModifierState;
    @Nullable
    private PackageNameContext packageNameContext;

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
    public PackageNameContext getPackageNameContext()
    {
        return this.packageNameContext;
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
        this.packageNameContext     = null;
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
        compilerWalkState.packageNameContext         = this.packageNameContext;
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
        if (this.packageNameContext != null)
        {
            throw new AssertionError();
        }
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

    public void assertEquals(CompilerWalkState other)
    {
        if (!Objects.equals(this.domainModelState, other.domainModelState))
        {
            throw new AssertionError();
        }
        if (this.currentCompilationUnit != other.currentCompilationUnit)
        {
            throw new AssertionError();
        }
        if (!Objects.equals(this.packageName, other.packageName))
        {
            throw new AssertionError();
        }
        if (this.topLevelDeclarationState != other.topLevelDeclarationState)
        {
            throw new AssertionError();
        }
        if (this.enumerationState != other.enumerationState)
        {
            throw new AssertionError();
        }
        if (this.classifierState != other.classifierState)
        {
            throw new AssertionError();
        }
        if (this.interfaceState != other.interfaceState)
        {
            throw new AssertionError();
        }
        if (this.classState != other.classState)
        {
            throw new AssertionError();
        }
        if (this.associationState != other.associationState)
        {
            throw new AssertionError();
        }
        if (this.associationEndState != other.associationEndState)
        {
            throw new AssertionError();
        }
        if (this.associationEndSignatureState != other.associationEndSignatureState)
        {
            throw new AssertionError();
        }
        if (this.parameterizedPropertyState != other.parameterizedPropertyState)
        {
            throw new AssertionError();
        }
        if (this.projectionState != other.projectionState)
        {
            throw new AssertionError();
        }
        if (this.serviceGroupState != other.serviceGroupState)
        {
            throw new AssertionError();
        }
        if (this.urlState != other.urlState)
        {
            throw new AssertionError();
        }
        if (this.serviceState != other.serviceState)
        {
            throw new AssertionError();
        }
        if (this.thisReference != other.thisReference)
        {
            throw new AssertionError();
        }
        if (this.orderByOwnerState != other.orderByOwnerState)
        {
            throw new AssertionError();
        }
        if (this.classModifierState != other.classModifierState)
        {
            throw new AssertionError();
        }
    }

    public KlassListener asListener()
    {
        return new ListenerView();
    }

    public class ListenerView extends KlassBaseListener
    {
        @Override
        public void enterPackageDeclaration(@Nonnull PackageDeclarationContext packageContext)
        {
            CompilerWalkState.this.packageNameContext = packageContext.packageName();
            CompilerWalkState.this.packageName        = CompilerWalkState.this.packageNameContext.getText();
        }

        @Override
        public void enterTopLevelDeclaration(@Nonnull TopLevelDeclarationContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.topLevelDeclarationState);
            CompilerWalkState.this.topLevelDeclarationState =
                    CompilerWalkState.this.domainModelState.getTopLevelElementByContext(ctx);
        }

        @Override
        public void exitTopLevelDeclaration(@Nonnull TopLevelDeclarationContext ctx)
        {
            CompilerWalkState.this.topLevelDeclarationState = null;
        }

        @Override
        public void enterEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.enumerationState);
            CompilerWalkState.this.enumerationState = CompilerWalkState.this.domainModelState.getEnumerationByContext(ctx);
        }

        @Override
        public void exitEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
        {
            CompilerWalkState.this.enumerationState = null;
        }

        @Override
        public void enterInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.classifierState);
            CompilerWalkState.assertNull(CompilerWalkState.this.interfaceState);
            CompilerWalkState.assertNull(CompilerWalkState.this.thisReference);

            AntlrInterface interfaceByContext = CompilerWalkState.this.domainModelState.getInterfaceByContext(ctx);

            CompilerWalkState.this.classifierState = interfaceByContext;
            CompilerWalkState.this.interfaceState  = interfaceByContext;
            CompilerWalkState.this.thisReference   = interfaceByContext;
        }

        @Override
        public void exitInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
        {
            CompilerWalkState.this.classifierState = null;
            CompilerWalkState.this.interfaceState  = null;
            CompilerWalkState.this.thisReference   = null;
        }

        @Override
        public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.classifierState);
            CompilerWalkState.assertNull(CompilerWalkState.this.classState);
            CompilerWalkState.assertNull(CompilerWalkState.this.thisReference);

            AntlrClass classByContext = CompilerWalkState.this.domainModelState.getClassByContext(ctx);

            CompilerWalkState.this.classifierState = classByContext;
            CompilerWalkState.this.classState      = classByContext;
            CompilerWalkState.this.thisReference   = classByContext;
        }

        @Override
        public void exitClassDeclaration(@Nonnull ClassDeclarationContext ctx)
        {
            CompilerWalkState.this.classifierState = null;
            CompilerWalkState.this.classState      = null;
            CompilerWalkState.this.thisReference   = null;
        }

        @Override
        public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.associationState);
            CompilerWalkState.this.associationState = CompilerWalkState.this.domainModelState.getAssociationByContext(ctx);
        }

        @Override
        public void exitAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
        {
            CompilerWalkState.this.associationState = null;
        }

        @Override
        public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.associationEndState);
            CompilerWalkState.assertNull(CompilerWalkState.this.thisReference);
            CompilerWalkState.assertNull(CompilerWalkState.this.orderByOwnerState);

            if (CompilerWalkState.this.associationState == null)
            {
                return;
            }

            CompilerWalkState.this.associationEndState =
                    CompilerWalkState.this.associationState.getAssociationEndByContext(ctx);
            if (CompilerWalkState.this.associationEndState == null)
            {
                return;
            }
            CompilerWalkState.this.orderByOwnerState = CompilerWalkState.this.associationEndState;
            CompilerWalkState.this.thisReference     = CompilerWalkState.this.associationEndState.getType();
        }

        @Override
        public void exitAssociationEnd(@Nonnull AssociationEndContext ctx)
        {
            CompilerWalkState.this.associationEndState = null;
            CompilerWalkState.this.thisReference       = null;
            CompilerWalkState.this.orderByOwnerState   = null;
        }

        @Override
        public void enterAssociationEndSignature(@Nonnull AssociationEndSignatureContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.associationEndSignatureState);
            CompilerWalkState.assertNull(CompilerWalkState.this.orderByOwnerState);

            if (CompilerWalkState.this.classifierState == null)
            {
                return;
            }

            CompilerWalkState.this.associationEndSignatureState =
                    CompilerWalkState.this.classifierState.getAssociationEndSignatureByContext(ctx);
            if (CompilerWalkState.this.associationEndSignatureState == null)
            {
                return;
            }
            CompilerWalkState.this.orderByOwnerState = CompilerWalkState.this.associationEndSignatureState;
        }

        @Override
        public void exitAssociationEndSignature(@Nonnull AssociationEndSignatureContext ctx)
        {
            CompilerWalkState.this.associationEndSignatureState = null;
            CompilerWalkState.this.orderByOwnerState            = null;
        }

        @Override
        public void enterRelationship(@Nonnull RelationshipContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.thisReference);

            if (CompilerWalkState.this.associationState == null)
            {
                return;
            }

            CompilerWalkState.this.thisReference = CompilerWalkState.this.associationState.getAssociationEndStates()
                    .getFirstOptional()
                    .map(AntlrAssociationEnd::getType)
                    .orElse(AntlrClass.NOT_FOUND);
        }

        @Override
        public void exitRelationship(@Nonnull RelationshipContext ctx)
        {
            CompilerWalkState.this.thisReference = null;
        }

        @Override
        public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.projectionState);

            CompilerWalkState.this.projectionState =
                    CompilerWalkState.this.domainModelState.getProjectionByContext(ctx);
        }

        @Override
        public void exitProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
        {
            CompilerWalkState.this.projectionState = null;
        }

        @Override
        public void enterParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.parameterizedPropertyState);
            CompilerWalkState.assertNull(CompilerWalkState.this.orderByOwnerState);

            if (CompilerWalkState.this.classState == null)
            {
                return;
            }
            CompilerWalkState.this.parameterizedPropertyState =
                    CompilerWalkState.this.classState.getParameterizedPropertyByContext(ctx);
            CompilerWalkState.this.orderByOwnerState          = CompilerWalkState.this.parameterizedPropertyState;
        }

        @Override
        public void exitParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
        {
            CompilerWalkState.this.parameterizedPropertyState = null;
            CompilerWalkState.this.orderByOwnerState          = null;
        }

        @Override
        public void enterServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.serviceGroupState);
            CompilerWalkState.assertNull(CompilerWalkState.this.thisReference);

            CompilerWalkState.this.serviceGroupState = CompilerWalkState.this.domainModelState.getServiceGroupByContext(ctx);
            if (CompilerWalkState.this.serviceGroupState == null)
            {
                return;
            }
            CompilerWalkState.this.thisReference = CompilerWalkState.this.serviceGroupState.getKlass();
        }

        @Override
        public void exitServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
        {
            CompilerWalkState.this.serviceGroupState = null;
            CompilerWalkState.this.thisReference     = null;
        }

        @Override
        public void enterUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.urlState);
            if (CompilerWalkState.this.serviceGroupState == null)
            {
                return;
            }
            CompilerWalkState.this.urlState = CompilerWalkState.this.serviceGroupState.getUrlByContext(ctx);
        }

        @Override
        public void exitUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
        {
            CompilerWalkState.this.urlState = null;
        }

        @Override
        public void enterServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.serviceState);
            CompilerWalkState.assertNull(CompilerWalkState.this.orderByOwnerState);

            if (CompilerWalkState.this.urlState == null)
            {
                return;
            }
            CompilerWalkState.this.serviceState = CompilerWalkState.this.urlState.getServiceByContext(ctx);
            if (CompilerWalkState.this.serviceState == null)
            {
                return;
            }
            CompilerWalkState.this.orderByOwnerState = CompilerWalkState.this.serviceState;
        }

        @Override
        public void exitServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
        {
            CompilerWalkState.this.serviceState      = null;
            CompilerWalkState.this.orderByOwnerState = null;
        }

        @Override
        public void enterClassModifier(@Nonnull ClassModifierContext ctx)
        {
            CompilerWalkState.assertNull(CompilerWalkState.this.classModifierState);

            if (CompilerWalkState.this.classifierState == null)
            {
                return;
            }

            CompilerWalkState.this.classModifierState =
                    CompilerWalkState.this.classifierState.getClassModifierByContext(ctx);
        }

        @Override
        public void exitClassModifier(@Nonnull ClassModifierContext ctx)
        {
            CompilerWalkState.this.classModifierState = null;
        }
    }
}
