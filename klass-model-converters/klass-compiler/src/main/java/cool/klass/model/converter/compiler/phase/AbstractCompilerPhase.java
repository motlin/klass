package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.TopLevelDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;

public abstract class AbstractCompilerPhase extends KlassBaseListener
{
    protected final CompilerState compilerState;

    protected AbstractCompilerPhase(CompilerState compilerState)
    {
        this.compilerState = compilerState;
    }

    @Nonnull
    public String getName()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".getName() not implemented yet");
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterCompilationUnit(CompilationUnitContext ctx)
    {
        super.enterCompilationUnit(ctx);
        this.compilerState.asListener().enterCompilationUnit(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitCompilationUnit(CompilationUnitContext ctx)
    {
        this.compilerState.asListener().exitCompilationUnit(ctx);
        super.exitCompilationUnit(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
    {
        super.enterPackageDeclaration(ctx);
        this.compilerState.asListener().enterPackageDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterTopLevelDeclaration(TopLevelDeclarationContext ctx)
    {
        super.enterTopLevelDeclaration(ctx);
        this.compilerState.asListener().enterTopLevelDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitTopLevelDeclaration(TopLevelDeclarationContext ctx)
    {
        this.compilerState.asListener().exitTopLevelDeclaration(ctx);
        super.exitTopLevelDeclaration(ctx);
    }

    @Override
    public void enterInterfaceDeclaration(InterfaceDeclarationContext ctx)
    {
        super.enterInterfaceDeclaration(ctx);
        this.compilerState.asListener().enterInterfaceDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitInterfaceDeclaration(InterfaceDeclarationContext ctx)
    {
        this.compilerState.asListener().exitInterfaceDeclaration(ctx);
        super.exitInterfaceDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        super.enterClassDeclaration(ctx);
        this.compilerState.asListener().enterClassDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.compilerState.asListener().exitClassDeclaration(ctx);
        super.exitClassDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        super.enterEnumerationDeclaration(ctx);
        this.compilerState.asListener().enterEnumerationDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.compilerState.asListener().exitEnumerationDeclaration(ctx);
        super.exitEnumerationDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        super.enterAssociationDeclaration(ctx);
        this.compilerState.asListener().enterAssociationDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.compilerState.asListener().exitAssociationDeclaration(ctx);
        super.exitAssociationDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        super.enterAssociationEnd(ctx);
        this.compilerState.asListener().enterAssociationEnd(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitAssociationEnd(AssociationEndContext ctx)
    {
        this.compilerState.asListener().exitAssociationEnd(ctx);
        super.exitAssociationEnd(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterAssociationEndSignature(AssociationEndSignatureContext ctx)
    {
        super.enterAssociationEndSignature(ctx);
        this.compilerState.asListener().enterAssociationEndSignature(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitAssociationEndSignature(AssociationEndSignatureContext ctx)
    {
        this.compilerState.asListener().exitAssociationEndSignature(ctx);
        super.exitAssociationEndSignature(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterRelationship(RelationshipContext ctx)
    {
        super.enterRelationship(ctx);
        this.compilerState.asListener().enterRelationship(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitRelationship(RelationshipContext ctx)
    {
        this.compilerState.asListener().exitRelationship(ctx);
        super.exitRelationship(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        super.enterProjectionDeclaration(ctx);
        this.compilerState.asListener().enterProjectionDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.compilerState.asListener().exitProjectionDeclaration(ctx);
        super.exitProjectionDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        super.enterServiceGroupDeclaration(ctx);
        this.compilerState.asListener().enterServiceGroupDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.compilerState.asListener().exitServiceGroupDeclaration(ctx);
        super.exitServiceGroupDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterUrlDeclaration(UrlDeclarationContext ctx)
    {
        super.enterUrlDeclaration(ctx);
        this.compilerState.asListener().enterUrlDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitUrlDeclaration(UrlDeclarationContext ctx)
    {
        this.compilerState.asListener().exitUrlDeclaration(ctx);
        super.exitUrlDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterServiceDeclaration(ServiceDeclarationContext ctx)
    {
        super.enterServiceDeclaration(ctx);
        this.compilerState.asListener().enterServiceDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitServiceDeclaration(ServiceDeclarationContext ctx)
    {
        this.compilerState.asListener().exitServiceDeclaration(ctx);
        super.exitServiceDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        super.enterParameterizedProperty(ctx);
        this.compilerState.asListener().enterParameterizedProperty(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        this.compilerState.asListener().exitParameterizedProperty(ctx);
        super.exitParameterizedProperty(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterClassModifier(ClassModifierContext ctx)
    {
        super.enterClassModifier(ctx);
        this.compilerState.asListener().enterClassModifier(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitClassModifier(ClassModifierContext ctx)
    {
        this.compilerState.asListener().exitClassModifier(ctx);
        super.exitClassModifier(ctx);
    }
}
