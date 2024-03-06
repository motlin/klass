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
import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser;
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

    private final MutableOrderedMap<CompilationUnit, CompilerWalkState> macroCompilerWalkStates =
            OrderedMaps.adapt(new LinkedHashMap<>());

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
        this.macroCompilerWalkStates.put(compilationUnit, compilerWalkState);
        this.compilerInputState.runCompilerMacro(compilationUnit, Lists.immutable.with(listeners));
    }

    public void runRootCompilerMacro(
            @Nonnull AntlrElement macroElement,
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
        try
        {
            this.compilerWalkState = new CompilerWalkState(this.domainModelState);

            this.compilerInputState.runCompilerMacro(compilationUnit, listeners);
        }
        finally
        {
            this.compilerWalkState = oldCompilerWalkState;
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

    public void withCompilationUnit(CompilationUnit compilationUnit, @Nonnull Runnable runnable)
    {
        this.compilerWalkState.assertEmpty();

        CompilerWalkState compilerWalkState = this.macroCompilerWalkStates.get(compilationUnit);

        if (compilerWalkState == null)
        {
            runnable.run();
            this.compilerWalkState.assertEmpty();
            return;
        }

        try
        {
            CompilerWalkState copy = compilerWalkState.withCompilationUnit(compilationUnit);
            this.compilerWalkState = copy;
            runnable.run();
            this.compilerWalkState.assertEquals(copy);
        }
        finally
        {
            this.compilerWalkState = new CompilerWalkState(this.domainModelState);
        }
    }

    public Integer getOrdinal(@Nonnull ParserRuleContext ctx)
    {
        TopLevelDeclarationContext topLevelDeclarationContext = AntlrUtils.getParentOfType(
                ctx,
                TopLevelDeclarationContext.class);

        if (ctx == topLevelDeclarationContext)
        {
            throw new AssertionError(ctx);
        }

        Integer topLevelElementOrdinalByContext =
                this.domainModelState.getTopLevelElementOrdinalByContext(topLevelDeclarationContext);
        Objects.requireNonNull(topLevelElementOrdinalByContext);
        return topLevelElementOrdinalByContext;
    }

    @Nonnull
    public KlassListener asListener()
    {
        return new ListenerView();
    }

    public class ListenerView extends KlassBaseListener
    {
        @Override
        public void enterCompilationUnit(CompilationUnitContext ctx)
        {
            super.enterCompilationUnit(ctx);

            CompilationUnit currentCompilationUnit = CompilerState.this.compilerInputState.getCompilationUnitByContext(ctx);
            CompilerState.this.compilerWalkState.enterCompilationUnit(currentCompilationUnit);
        }

        @Override
        public void exitCompilationUnit(@Nonnull CompilationUnitContext ctx)
        {
            super.exitCompilationUnit(ctx);

            CompilerState.this.compilerWalkState.exitCompilationUnit();
        }

        @Override
        public void enterPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
        {
            super.enterPackageDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().enterPackageDeclaration(ctx);
        }

        @Override
        public void enterTopLevelDeclaration(TopLevelDeclarationContext ctx)
        {
            super.enterTopLevelDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().enterTopLevelDeclaration(ctx);
        }

        @Override
        public void exitTopLevelDeclaration(TopLevelDeclarationContext ctx)
        {
            super.exitTopLevelDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().exitTopLevelDeclaration(ctx);
        }

        @Override
        public void enterInterfaceDeclaration(InterfaceDeclarationContext ctx)
        {
            super.enterInterfaceDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().enterInterfaceDeclaration(ctx);
        }

        @Override
        public void exitInterfaceDeclaration(InterfaceDeclarationContext ctx)
        {
            super.exitInterfaceDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().exitInterfaceDeclaration(ctx);
        }

        @Override
        public void enterClassDeclaration(ClassDeclarationContext ctx)
        {
            super.enterClassDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().enterClassDeclaration(ctx);
        }

        @Override
        public void exitClassDeclaration(ClassDeclarationContext ctx)
        {
            super.exitClassDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().exitClassDeclaration(ctx);
        }

        @Override
        public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
        {
            super.enterEnumerationDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().enterEnumerationDeclaration(ctx);
        }

        @Override
        public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
        {
            super.exitEnumerationDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().exitEnumerationDeclaration(ctx);
        }

        @Override
        public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
        {
            super.enterAssociationDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().enterAssociationDeclaration(ctx);
        }

        @Override
        public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
        {
            super.exitAssociationDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().exitAssociationDeclaration(ctx);
        }

        @Override
        public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
        {
            super.enterAssociationEnd(ctx);

            CompilerState.this.compilerWalkState.asListener().enterAssociationEnd(ctx);
        }

        @Override
        public void exitAssociationEnd(AssociationEndContext ctx)
        {
            super.exitAssociationEnd(ctx);

            CompilerState.this.compilerWalkState.asListener().exitAssociationEnd(ctx);
        }

        @Override
        public void enterAssociationEndSignature(AssociationEndSignatureContext ctx)
        {
            super.enterAssociationEndSignature(ctx);

            CompilerState.this.compilerWalkState.asListener().enterAssociationEndSignature(ctx);
        }

        @Override
        public void exitAssociationEndSignature(AssociationEndSignatureContext ctx)
        {
            super.exitAssociationEndSignature(ctx);

            CompilerState.this.compilerWalkState.asListener().exitAssociationEndSignature(ctx);
        }

        @Override
        public void enterRelationship(RelationshipContext ctx)
        {
            super.enterRelationship(ctx);

            CompilerState.this.compilerWalkState.asListener().enterRelationship(ctx);
        }

        @Override
        public void exitRelationship(RelationshipContext ctx)
        {
            super.exitRelationship(ctx);

            CompilerState.this.compilerWalkState.asListener().exitRelationship(ctx);
        }

        @Override
        public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
        {
            super.enterProjectionDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().enterProjectionDeclaration(ctx);
        }

        @Override
        public void exitProjectionDeclaration(ProjectionDeclarationContext ctx)
        {
            super.exitProjectionDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().exitProjectionDeclaration(ctx);
        }

        @Override
        public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
        {
            super.enterServiceGroupDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().enterServiceGroupDeclaration(ctx);
        }

        @Override
        public void exitServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
        {
            super.exitServiceGroupDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().exitServiceGroupDeclaration(ctx);
        }

        @Override
        public void enterUrlDeclaration(UrlDeclarationContext ctx)
        {
            super.enterUrlDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().enterUrlDeclaration(ctx);
        }

        @Override
        public void exitUrlDeclaration(UrlDeclarationContext ctx)
        {
            super.exitUrlDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().exitUrlDeclaration(ctx);
        }

        @Override
        public void enterServiceDeclaration(ServiceDeclarationContext ctx)
        {
            super.enterServiceDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().enterServiceDeclaration(ctx);
        }

        @Override
        public void exitServiceDeclaration(ServiceDeclarationContext ctx)
        {
            super.exitServiceDeclaration(ctx);

            CompilerState.this.compilerWalkState.asListener().exitServiceDeclaration(ctx);
        }

        @Override
        public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
        {
            super.enterParameterizedProperty(ctx);

            CompilerState.this.compilerWalkState.asListener().enterParameterizedProperty(ctx);
        }

        @Override
        public void exitParameterizedProperty(ParameterizedPropertyContext ctx)
        {
            super.exitParameterizedProperty(ctx);

            CompilerState.this.compilerWalkState.asListener().exitParameterizedProperty(ctx);
        }

        @Override
        public void enterClassModifier(ClassModifierContext ctx)
        {
            super.enterClassModifier(ctx);

            CompilerState.this.compilerWalkState.asListener().enterClassModifier(ctx);
        }

        @Override
        public void exitClassModifier(ClassModifierContext ctx)
        {
            super.exitClassModifier(ctx);

            CompilerState.this.compilerWalkState.asListener().exitClassModifier(ctx);
        }
    }
}
