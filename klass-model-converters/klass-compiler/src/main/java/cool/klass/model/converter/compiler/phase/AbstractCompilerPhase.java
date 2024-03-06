package cool.klass.model.converter.compiler.phase;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageNameContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MapIterable;

public abstract class AbstractCompilerPhase extends KlassBaseListener
{
    @Nonnull
    protected final MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext;
    @Nonnull
    protected final CompilerErrorHolder                                  compilerErrorHolder;

    @Nullable
    protected String                         packageName;
    @Nullable
    protected CompilationUnit                currentCompilationUnit;
    @Nullable
    protected ClassDeclarationContext        classDeclarationContext;
    @Nullable
    protected AssociationDeclarationContext  associationDeclarationContext;
    @Nullable
    protected ServiceGroupDeclarationContext serviceGroupDeclarationContext;
    @Nullable
    protected ParameterizedPropertyContext   parameterizedPropertyContext;
    @Nullable
    protected EnumerationDeclarationContext  enumerationDeclarationContext;
    @Nullable
    protected ProjectionDeclarationContext   projectionDeclarationContext;
    @Nullable
    protected UrlDeclarationContext          urlDeclarationContext;
    @Nullable
    protected ServiceDeclarationContext      serviceDeclarationContext;

    protected AbstractCompilerPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext)
    {
        this.compilerErrorHolder = Objects.requireNonNull(compilerErrorHolder);
        this.compilationUnitsByContext = Objects.requireNonNull(compilationUnitsByContext);
    }

    @Override
    public void enterCompilationUnit(CompilationUnitContext ctx)
    {
        this.currentCompilationUnit = this.compilationUnitsByContext.get(ctx);
    }

    @Override
    public void exitCompilationUnit(CompilationUnitContext ctx)
    {
        this.currentCompilationUnit = null;
        this.packageName = null;
    }

    @Override
    public void enterPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
    {
        PackageNameContext packageNameContext = ctx.packageName();
        this.packageName = packageNameContext.getText();
    }

    @Override
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        this.classDeclarationContext = ctx;
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.classDeclarationContext = null;
    }

    @Override
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.enumerationDeclarationContext = ctx;
    }

    @Override
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.enumerationDeclarationContext = null;
    }

    @Override
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.associationDeclarationContext = ctx;
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.associationDeclarationContext = null;
    }

    @Override
    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.projectionDeclarationContext = ctx;
    }

    @Override
    public void exitProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.projectionDeclarationContext = null;
    }

    @Override
    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.serviceGroupDeclarationContext = ctx;
    }

    @Override
    public void exitServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.serviceGroupDeclarationContext = null;
    }

    @Override
    public void enterUrlDeclaration(UrlDeclarationContext ctx)
    {
        this.urlDeclarationContext = ctx;
    }

    @Override
    public void exitUrlDeclaration(UrlDeclarationContext ctx)
    {
        this.urlDeclarationContext = null;
    }

    @Override
    public void enterServiceDeclaration(ServiceDeclarationContext ctx)
    {
        this.serviceDeclarationContext = ctx;
    }

    @Override
    public void exitServiceDeclaration(ServiceDeclarationContext ctx)
    {
        this.serviceDeclarationContext = null;
    }

    @Override
    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        this.parameterizedPropertyContext = ctx;
    }

    @Override
    public void exitParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        this.parameterizedPropertyContext = null;
    }

    public void error(
            @Nonnull String message,
            @Nonnull ParserRuleContext offendingParserRuleContext,
            ParserRuleContext... parserRuleContexts)
    {
        this.compilerErrorHolder.add(
                this.currentCompilationUnit,
                message,
                offendingParserRuleContext,
                parserRuleContexts);
    }
}
