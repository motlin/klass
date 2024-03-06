package cool.klass.model.converter.compiler;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.annotation.AbstractCompilerAnnotation;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationHolder;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.converter.compiler.parser.AbstractDelegatingKlassListener;
import cool.klass.model.converter.compiler.parser.AntlrUtils;
import cool.klass.model.converter.compiler.phase.AbstractCompilerPhase;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.meta.domain.DomainModelImpl.DomainModelBuilder;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.TopLevelDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.OrderedMaps;

public class CompilerState
{
    @Nonnull
    private final CompilerInputState       compilerInput;
    private final CompilerAnnotationHolder compilerAnnotationHolder = new CompilerAnnotationHolder();
    private final AntlrDomainModel         domainModel              = new AntlrDomainModel();
    private       CompilerWalkState        compilerWalk             = new CompilerWalkState(this.domainModel);

    private final MutableOrderedMap<CompilationUnit, CompilerWalkState> macroCompilerWalks =
            OrderedMaps.adapt(new LinkedHashMap<>());

    public CompilerState(@Nonnull ImmutableList<CompilationUnit> compilationUnits)
    {
        this.compilerInput = new CompilerInputState(compilationUnits);
    }

    public void runInPlaceCompilerMacro(
            @Nonnull AntlrElement macroElement,
            @Nonnull AbstractCompilerPhase macroExpansionCompilerPhase,
            @Nonnull String sourceCodeText,
            @Nonnull Function<KlassParser, ? extends ParserRuleContext> parserRule,
            ParserRuleContext inPlaceContext,
            ParseTreeListener... listeners)
    {
        Objects.requireNonNull(macroElement);

        CompilationUnit compilationUnit = CompilationUnit.getMacroCompilationUnit(
                this.compilerInput.getCompilationUnits().size(),
                macroElement,
                macroExpansionCompilerPhase,
                sourceCodeText,
                parserRule);

        ParserRuleContext parserContext = compilationUnit.getParserContext();
        parserContext.setParent(inPlaceContext);
        inPlaceContext.addChild(parserContext);

        this.compilerWalk.withInPlaceCompilationUnit(compilationUnit, () ->
        {
            this.compilerInput.runInPlaceCompilerMacro(compilationUnit, Lists.immutable.with(listeners));
        });
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
                this.compilerInput.getCompilationUnits().size(),
                macroElement,
                macroExpansionCompilerPhase,
                sourceCodeText,
                parserRule);

        CompilerWalkState oldCompilerWalk = this.compilerWalk;
        CompilerWalkState newCompilerWalk = oldCompilerWalk.withCompilationUnit(compilationUnit);
        this.macroCompilerWalks.put(compilationUnit, newCompilerWalk);

        this.compilerWalk = newCompilerWalk;
        try
        {
            this.compilerInput.runCompilerMacro(compilationUnit, Lists.immutable.with(listeners));
        }
        finally
        {
            this.compilerWalk = oldCompilerWalk;
        }
    }

    public void runRootCompilerMacro(
            @Nonnull AntlrElement macroElement,
            @Nonnull AbstractCompilerPhase macroExpansionCompilerPhase,
            @Nonnull String sourceCodeText,
            @Nonnull Function<KlassParser, ? extends ParserRuleContext> parserRule,
            @Nonnull ImmutableList<ParseTreeListener> listeners)
    {
        CompilationUnit compilationUnit = CompilationUnit.getMacroCompilationUnit(
                this.compilerInput.getCompilationUnits().size(),
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
        CompilerWalkState oldCompilerWalk = this.compilerWalk;
        try
        {
            this.compilerWalk = new CompilerWalkState(this.domainModel);

            this.compilerInput.runCompilerMacro(compilationUnit, listeners);
        }
        finally
        {
            this.compilerWalk = oldCompilerWalk;
        }
    }

    public void reportErrors()
    {
        this.domainModel.reportErrors(this.compilerAnnotationHolder);
    }

    @Nonnull
    public CompilationResult getCompilationResult(
            ImmutableList<RootCompilerAnnotation> compilerAnnotations)
    {
        if (compilerAnnotations.anySatisfy(AbstractCompilerAnnotation::isError))
        {
            return new CompilationResult(compilerAnnotations, Optional.empty());
        }
        return new CompilationResult(compilerAnnotations, Optional.of(this.buildDomainModel()));
    }

    @Nonnull
    private DomainModelWithSourceCode buildDomainModel()
    {
        ImmutableList<RootCompilerAnnotation> compilerAnnotations = this.compilerAnnotationHolder.getCompilerAnnotations();

        if (compilerAnnotations.anySatisfy(AbstractCompilerAnnotation::isError))
        {
            throw new AssertionError(this.compilerAnnotationHolder.getCompilerAnnotations().makeString("\n"));
        }

        ImmutableList<CompilationUnit> compilationUnits   = this.compilerInput.getCompilationUnits().toImmutable();
        DomainModelBuilder             domainModelBuilder = this.domainModel.build(compilationUnits);
        return domainModelBuilder.build();
    }

    @Nonnull
    public AntlrDomainModel getDomainModel()
    {
        return this.domainModel;
    }

    @Nonnull
    public CompilerInputState getCompilerInput()
    {
        return this.compilerInput;
    }

    @Nonnull
    public CompilerAnnotationHolder getCompilerAnnotationHolder()
    {
        return this.compilerAnnotationHolder;
    }

    public CompilerWalkState getCompilerWalk()
    {
        return this.compilerWalk;
    }

    public void withCompilationUnit(CompilationUnit compilationUnit, @Nonnull Runnable runnable)
    {
        this.compilerWalk.assertEmpty();

        CompilerWalkState compilerWalk = this.macroCompilerWalks.get(compilationUnit);

        if (compilerWalk == null)
        {
            runnable.run();
            this.compilerWalk.assertEmpty();
            return;
        }

        try
        {
            CompilerWalkState copy = compilerWalk.withCompilationUnit(compilationUnit);
            this.compilerWalk = copy;
            runnable.run();
            this.compilerWalk.assertEquals(copy);
        }
        finally
        {
            this.compilerWalk = new CompilerWalkState(this.domainModel);
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
                this.domainModel.getTopLevelElementOrdinalByContext(topLevelDeclarationContext);
        Objects.requireNonNull(topLevelElementOrdinalByContext);
        return topLevelElementOrdinalByContext;
    }

    @Nonnull
    public KlassListener asListener()
    {
        return new ListenerView();
    }

    public class ListenerView
            extends AbstractDelegatingKlassListener
    {
        @Override
        protected KlassListener getDelegate()
        {
            return CompilerState.this.compilerWalk.asListener();
        }

        @Override
        public void enterCompilationUnit(@Nonnull CompilationUnitContext ctx)
        {
            super.enterCompilationUnit(ctx);
            CompilationUnit currentCompilationUnit = CompilerState.this.compilerInput.getCompilationUnitByContext(ctx);
            CompilerState.this.compilerWalk.enterCompilationUnit(currentCompilationUnit);
        }

        @Override
        public void exitCompilationUnit(@Nonnull CompilationUnitContext ctx)
        {
            super.exitCompilationUnit(ctx);
            CompilerState.this.compilerWalk.exitCompilationUnit();
        }
    }
}
