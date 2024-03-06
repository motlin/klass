package cool.klass.model.converter.compiler;

import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.error.RootCompilerError;
import cool.klass.model.converter.compiler.phase.AbstractCompilerPhase;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.meta.domain.DomainModelImpl.DomainModelBuilder;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.OrderedMaps;

public class CompilerState
{
    @Nonnull
    private final CompilerInputState compilerInputState;
    private final CompilerErrorState compilerErrorHolder = new CompilerErrorState();
    private final AntlrDomainModel   domainModelState    = new AntlrDomainModel();
    private       CompilerWalkState  compilerWalkState   = new CompilerWalkState(this.domainModelState);
    private       AntlrWalkState     antlrWalkState      = new AntlrWalkState();

    private final MutableOrderedMap<CompilationUnit, CompilerWalkState> macroCompilerWalkStates = OrderedMaps.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<CompilationUnit, AntlrWalkState>    macroAntlrWalkStates    = OrderedMaps.adapt(new LinkedHashMap<>());

    public CompilerState(CompilationUnit compilationUnit)
    {
        this(Lists.mutable.with(compilationUnit));
    }

    public CompilerState(@Nonnull MutableList<CompilationUnit> compilationUnits)
    {
        this.compilerInputState = new CompilerInputState(compilationUnits);
    }

    public void runNonRootCompilerMacro(
            @Nonnull AntlrElement macroElement,
            @Nonnull ParserRuleContext parserContext,
            @Nonnull AbstractCompilerPhase macroExpansionCompilerPhase,
            @Nonnull String sourceCodeText,
            @Nonnull Function<KlassParser, ? extends ParserRuleContext> parserRule,
            ParseTreeListener... listeners)
    {
        Objects.requireNonNull(macroElement);

        CompilationUnit compilationUnit = CompilationUnit.getMacroCompilationUnit(
                macroElement,
                macroExpansionCompilerPhase,
                sourceCodeText,
                parserRule);

        CompilerWalkState compilerWalkState = this.compilerWalkState.withCompilationUnit(compilationUnit);
        AntlrWalkState    antlrWalkState    = this.antlrWalkState.copy();
        this.macroCompilerWalkStates.put(compilationUnit, compilerWalkState);
        this.macroAntlrWalkStates.put(compilationUnit, antlrWalkState);

        this.compilerWalkState.withCompilationUnit(compilationUnit, () ->
                this.compilerInputState.runCompilerMacro(compilationUnit, Lists.immutable.with(listeners)));
    }

    public void runRootCompilerMacro(
            @Nonnull AntlrElement macroElement,
            @Nonnull ParserRuleContext parserContext,
            @Nonnull AbstractCompilerPhase macroExpansionCompilerPhase,
            @Nonnull String sourceCodeText,
            @Nonnull Function<KlassParser, ? extends ParserRuleContext> parserRule,
            ParseTreeListener... listeners)
    {
        this.runRootCompilerMacro(
                macroElement,
                parserContext,
                macroExpansionCompilerPhase,
                sourceCodeText,
                parserRule,
                Lists.immutable.with(listeners));
    }

    public void runRootCompilerMacro(
            @Nonnull AntlrElement macroElement,
            @Nonnull ParserRuleContext parserContext,
            @Nonnull AbstractCompilerPhase macroExpansionCompilerPhase,
            @Nonnull String sourceCodeText,
            @Nonnull Function<KlassParser, ? extends ParserRuleContext> parserRule,
            @Nonnull ImmutableList<ParseTreeListener> listeners)
    {
        CompilationUnit compilationUnit = CompilationUnit.getMacroCompilationUnit(
                macroElement,
                macroExpansionCompilerPhase,
                sourceCodeText,
                parserRule);

        this.runRootCompilerMacro(listeners, compilationUnit);
    }

    private void runRootCompilerMacro(
            @Nonnull ImmutableList<ParseTreeListener> listeners,
            @Nonnull CompilationUnit compilationUnit)
    {
        CompilerWalkState oldCompilerWalkState = this.compilerWalkState;
        AntlrWalkState    oldAntlrWalkState    = this.antlrWalkState;
        try
        {
            this.compilerWalkState = new CompilerWalkState(this.domainModelState);
            this.antlrWalkState = new AntlrWalkState();

            this.compilerInputState.runCompilerMacro(compilationUnit, listeners);
        }
        finally
        {
            this.compilerWalkState = oldCompilerWalkState;
            this.antlrWalkState = oldAntlrWalkState;
        }
    }

    public void reportErrors()
    {
        this.domainModelState.reportErrors(this.compilerErrorHolder);
    }

    @Nonnull
    public CompilationResult getCompilationResult()
    {
        ImmutableList<RootCompilerError> compilerErrors = this.compilerErrorHolder.getCompilerErrors();
        if (compilerErrors.notEmpty())
        {
            return new ErrorsCompilationResult(compilerErrors);
        }
        return new DomainModelCompilationResult(this.buildDomainModel());
    }

    @Nonnull
    private DomainModel buildDomainModel()
    {
        if (this.compilerErrorHolder.hasCompilerErrors())
        {
            throw new AssertionError(this.compilerErrorHolder.getCompilerErrors().makeString());
        }

        DomainModelBuilder domainModelBuilder = this.domainModelState.build();
        return domainModelBuilder.build();
    }

    @Nonnull
    public AntlrDomainModel getDomainModelState()
    {
        return this.domainModelState;
    }

    @Nonnull
    public CompilerInputState getCompilerInputState()
    {
        return this.compilerInputState;
    }

    public CompilerWalkState getCompilerWalkState()
    {
        return this.compilerWalkState;
    }

    public AntlrWalkState getAntlrWalkState()
    {
        return this.antlrWalkState;
    }

    public void enterCompilationUnit(CompilationUnitContext ctx)
    {
        CompilationUnit currentCompilationUnit = this.compilerInputState.getCompilationUnitByContext(ctx);
        this.compilerWalkState.enterCompilationUnit(currentCompilationUnit);
    }

    public void exitCompilationUnit()
    {
        this.antlrWalkState.exitCompilationUnit();
        this.compilerWalkState.exitCompilationUnit();
    }

    public void enterPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
    {
        this.antlrWalkState.enterPackageDeclaration(ctx);
        this.compilerWalkState.enterPackageDeclaration(ctx);
    }

    public void enterInterfaceDeclaration(InterfaceDeclarationContext ctx)
    {
        this.antlrWalkState.enterInterfaceDeclaration(ctx);
        this.compilerWalkState.enterInterfaceDeclaration(ctx);
    }

    public void exitInterfaceDeclaration()
    {
        this.antlrWalkState.exitInterfaceDeclaration();
        this.compilerWalkState.exitInterfaceDeclaration();
    }

    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        this.antlrWalkState.enterClassDeclaration(ctx);
        this.compilerWalkState.enterClassDeclaration(ctx);
    }

    public void exitClassDeclaration()
    {
        this.antlrWalkState.exitClassDeclaration();
        this.compilerWalkState.exitClassDeclaration();
    }

    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.antlrWalkState.enterEnumerationDeclaration(ctx);
        this.compilerWalkState.enterEnumerationDeclaration(ctx);
    }

    public void exitEnumerationDeclaration()
    {
        this.antlrWalkState.exitEnumerationDeclaration();
        this.compilerWalkState.exitEnumerationDeclaration();
    }

    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.antlrWalkState.enterAssociationDeclaration(ctx);
        this.compilerWalkState.enterAssociationDeclaration(ctx);
    }

    public void exitAssociationDeclaration()
    {
        this.antlrWalkState.exitAssociationDeclaration();
        this.compilerWalkState.exitAssociationDeclaration();
    }

    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        this.compilerWalkState.enterAssociationEnd(ctx);
    }

    public void exitAssociationEnd()
    {
        this.compilerWalkState.exitAssociationEnd();
    }

    public void enterRelationship()
    {
        this.compilerWalkState.enterRelationship();
    }

    public void exitRelationship()
    {
        this.compilerWalkState.exitRelationship();
    }

    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.antlrWalkState.enterProjectionDeclaration(ctx);
        this.compilerWalkState.enterProjectionDeclaration(ctx);
    }

    public void exitProjectionDeclaration()
    {
        this.antlrWalkState.exitProjectionDeclaration();
        this.compilerWalkState.exitProjectionDeclaration();
    }

    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.antlrWalkState.enterServiceGroupDeclaration(ctx);
        this.compilerWalkState.enterServiceGroupDeclaration(ctx);
    }

    public void exitServiceGroupDeclaration()
    {
        this.antlrWalkState.exitServiceGroupDeclaration();
        this.compilerWalkState.exitServiceGroupDeclaration();
    }

    public void enterUrlDeclaration(UrlDeclarationContext ctx)
    {
        this.antlrWalkState.enterUrlDeclaration(ctx);
        this.compilerWalkState.enterUrlDeclaration(ctx);
    }

    public void exitUrlDeclaration()
    {
        this.antlrWalkState.exitUrlDeclaration();
        this.compilerWalkState.exitUrlDeclaration();
    }

    public void enterServiceDeclaration(ServiceDeclarationContext ctx)
    {
        this.antlrWalkState.enterServiceDeclaration(ctx);
        this.compilerWalkState.enterServiceDeclaration(ctx);
    }

    public void exitServiceDeclaration()
    {
        this.antlrWalkState.exitServiceDeclaration();
        this.compilerWalkState.exitServiceDeclaration();
    }

    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        this.antlrWalkState.enterParameterizedProperty(ctx);
        this.compilerWalkState.enterParameterizedProperty(ctx);
    }

    public void exitParameterizedProperty()
    {
        this.antlrWalkState.exitParameterizedProperty();
        this.compilerWalkState.exitParameterizedProperty();
    }

    public void enterClassModifier(ClassModifierContext ctx)
    {
        this.antlrWalkState.enterClassModifier(ctx);
        this.compilerWalkState.enterClassModifier(ctx);
    }

    public void exitClassModifier()
    {
        this.antlrWalkState.exitClassModifier();
        this.compilerWalkState.exitClassModifier();
    }

    public void withCompilationUnit(CompilationUnit compilationUnit, @Nonnull Runnable runnable)
    {
        CompilerWalkState compilerWalkState = this.macroCompilerWalkStates.get(compilationUnit);
        AntlrWalkState    antlrWalkState    = this.macroAntlrWalkStates.get(compilationUnit);

        if (compilerWalkState == null && antlrWalkState == null)
        {
            runnable.run();
            return;
        }

        this.compilerWalkState.assertEmpty();
        this.antlrWalkState.assertEmpty();

        try
        {
            this.compilerWalkState = compilerWalkState;
            this.antlrWalkState = antlrWalkState;
            runnable.run();
        }
        finally
        {
            this.compilerWalkState = new CompilerWalkState(this.domainModelState);
            this.antlrWalkState = new AntlrWalkState();
        }
    }

    public ImmutableList<RootCompilerError> getCompilerErrors()
    {
        return this.compilerErrorHolder.getCompilerErrors();
    }
}
