package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;

public abstract class AbstractCompilerPhase extends KlassBaseListener
{
    protected final CompilerState compilerState;

    protected AbstractCompilerPhase(CompilerState compilerState)
    {
        this.compilerState = compilerState;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterCompilationUnit(CompilationUnitContext ctx)
    {
        super.enterCompilationUnit(ctx);
        this.compilerState.enterCompilationUnit(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitCompilationUnit(CompilationUnitContext ctx)
    {
        this.compilerState.exitCompilationUnit();
        super.exitCompilationUnit(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
    {
        super.enterPackageDeclaration(ctx);
        this.compilerState.enterPackageDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        super.enterClassDeclaration(ctx);
        this.compilerState.enterClassDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.compilerState.exitClassDeclaration();
        super.exitClassDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        super.enterEnumerationDeclaration(ctx);
        this.compilerState.enterEnumerationDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.compilerState.exitEnumerationDeclaration();
        super.exitEnumerationDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        super.enterAssociationDeclaration(ctx);
        this.compilerState.enterAssociationDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.compilerState.exitAssociationDeclaration();
        super.exitAssociationDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterAssociationEnd(AssociationEndContext ctx)
    {
        super.enterAssociationEnd(ctx);
        this.compilerState.enterAssociationEnd(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitAssociationEnd(AssociationEndContext ctx)
    {
        this.compilerState.exitAssociationEnd();
        super.exitAssociationEnd(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterRelationship(RelationshipContext ctx)
    {
        super.enterRelationship(ctx);
        this.compilerState.enterRelationship();
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitRelationship(RelationshipContext ctx)
    {
        this.compilerState.exitRelationship();
        super.exitRelationship(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        super.enterProjectionDeclaration(ctx);
        this.compilerState.enterProjectionDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.compilerState.exitProjectionDeclaration();
        super.exitProjectionDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        super.enterServiceGroupDeclaration(ctx);
        this.compilerState.enterServiceGroupDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.compilerState.exitServiceGroupDeclaration();
        super.exitServiceGroupDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterUrlDeclaration(UrlDeclarationContext ctx)
    {
        super.enterUrlDeclaration(ctx);
        this.compilerState.enterUrlDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitUrlDeclaration(UrlDeclarationContext ctx)
    {
        this.compilerState.exitUrlDeclaration();
        super.exitUrlDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterServiceDeclaration(ServiceDeclarationContext ctx)
    {
        super.enterServiceDeclaration(ctx);
        this.compilerState.enterServiceDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitServiceDeclaration(ServiceDeclarationContext ctx)
    {
        this.compilerState.exitServiceDeclaration();
        super.exitServiceDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        super.enterParameterizedProperty(ctx);
        this.compilerState.enterParameterizedProperty(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        this.compilerState.exitParameterizedProperty();
        super.exitParameterizedProperty(ctx);
    }
}
