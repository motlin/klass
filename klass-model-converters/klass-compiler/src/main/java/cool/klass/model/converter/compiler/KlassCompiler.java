package cool.klass.model.converter.compiler;

import java.util.LinkedHashMap;
import java.util.function.Function;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.converter.compiler.phase.AssociationPhase;
import cool.klass.model.converter.compiler.phase.AuditAssociationInferencePhase;
import cool.klass.model.converter.compiler.phase.AuditPropertyInferencePhase;
import cool.klass.model.converter.compiler.phase.ClassTemporalPropertyInferencePhase;
import cool.klass.model.converter.compiler.phase.ClassifierPhase;
import cool.klass.model.converter.compiler.phase.CompilationUnitPhase;
import cool.klass.model.converter.compiler.phase.EnumerationsPhase;
import cool.klass.model.converter.compiler.phase.InheritancePhase;
import cool.klass.model.converter.compiler.phase.OrderByDirectionInferencePhase;
import cool.klass.model.converter.compiler.phase.OrderByDirectionPhase;
import cool.klass.model.converter.compiler.phase.OrderByPhase;
import cool.klass.model.converter.compiler.phase.ParameterizedPropertyPhase;
import cool.klass.model.converter.compiler.phase.ProjectionDeclarationPhase;
import cool.klass.model.converter.compiler.phase.ProjectionPhase;
import cool.klass.model.converter.compiler.phase.PropertyPhase;
import cool.klass.model.converter.compiler.phase.RelationshipInferencePhase;
import cool.klass.model.converter.compiler.phase.RelationshipPhase;
import cool.klass.model.converter.compiler.phase.ServiceCriteraInferencePhase;
import cool.klass.model.converter.compiler.phase.ServiceCriteriaPhase;
import cool.klass.model.converter.compiler.phase.ServiceMultiplicityInferencePhase;
import cool.klass.model.converter.compiler.phase.ServiceMultiplicityPhase;
import cool.klass.model.converter.compiler.phase.ServicePhase;
import cool.klass.model.converter.compiler.phase.TopLevelElementsPhase;
import cool.klass.model.converter.compiler.phase.UrlParameterPhase;
import cool.klass.model.converter.compiler.phase.VariableResolutionPhase;
import cool.klass.model.converter.compiler.phase.VersionAssociationInferencePhase;
import cool.klass.model.converter.compiler.phase.VersionClassInferencePhase;
import cool.klass.model.converter.compiler.syntax.highlighter.AnsiTokenColorizer;
import cool.klass.model.converter.compiler.syntax.highlighter.DarkColorScheme;
import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import cool.klass.model.converter.compiler.token.categorizing.lexer.LexerBasedTokenCategorizer;
import cool.klass.model.converter.compiler.token.categorizing.parser.ParserBasedTokenCategorizer;
import cool.klass.model.meta.grammar.KlassListener;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableMapIterable;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;
import org.fusesource.jansi.AnsiConsole;

public class KlassCompiler
{
    public static final ImmutableList<Function<CompilerState, KlassListener>> COMPILER_PHASE_BUILDERS =
            Lists.immutable.with(
                    CompilationUnitPhase::new,
                    TopLevelElementsPhase::new,
                    EnumerationsPhase::new,
                    ClassifierPhase::new,
                    PropertyPhase::new,
                    InheritancePhase::new,
                    ParameterizedPropertyPhase::new,
                    VersionClassInferencePhase::new,
                    ClassTemporalPropertyInferencePhase::new,
                    AuditPropertyInferencePhase::new,
                    AssociationPhase::new,
                    VersionAssociationInferencePhase::new,
                    AuditAssociationInferencePhase::new,
                    RelationshipPhase::new,
                    RelationshipInferencePhase::new,
                    ProjectionDeclarationPhase::new,
                    ProjectionPhase::new,
                    ServicePhase::new,
                    ServiceMultiplicityPhase::new,
                    ServiceMultiplicityInferencePhase::new,
                    UrlParameterPhase::new,
                    ServiceCriteriaPhase::new,
                    ServiceCriteraInferencePhase::new,
                    VariableResolutionPhase::new,
                    OrderByPhase::new,
                    OrderByDirectionPhase::new,
                    OrderByDirectionInferencePhase::new);

    private final CompilerState compilerState;

    public KlassCompiler(CompilationUnit compilationUnit)
    {
        this(Lists.immutable.with(compilationUnit));
    }

    public KlassCompiler(ImmutableList<CompilationUnit> compilationUnits)
    {
        // TODO: Move ansi stuff to ansi module or to application run
        AnsiConsole.systemInstall();

        this.compilerState = new CompilerState(compilationUnits);
    }

    private void executeCompilerPhase(KlassListener compilerPhase)
    {
        // Compiler macros may add new compilation units within a compiler phase, so take an immutable copy
        ImmutableList<CompilationUnit> immutableCompilationUnits =
                this.compilerState.getCompilerInputState().getCompilationUnits().toImmutable();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        for (CompilationUnit compilationUnit : immutableCompilationUnits)
        {
            this.compilerState.withCompilationUnit(
                    compilationUnit,
                    () -> parseTreeWalker.walk(compilerPhase, compilationUnit.getParserContext()));
        }
    }

    @Nonnull
    public CompilationResult compile()
    {
        ImmutableList<KlassListener> compilerPhases =
                COMPILER_PHASE_BUILDERS.collectWith(Function::apply, this.compilerState);
        for (KlassListener compilerPhase : compilerPhases)
        {
            try
            {
                this.executeCompilerPhase(compilerPhase);
            }
            catch (RuntimeException e)
            {
                String message = "Exception in compiler during phase: " + compilerPhase.getClass().getSimpleName();
                throw new RuntimeException(message, e);
            }
        }

        CompilerInputState                compilerInputState        = this.compilerState.getCompilerInputState();
        ImmutableList<CompilationUnit>    compilationUnits          = compilerInputState.getCompilationUnits().toImmutable();
        MapIterable<Token, TokenCategory> tokenCategoriesFromLexer  = this.getTokenCategoriesFromLexer(compilationUnits);
        MapIterable<Token, TokenCategory> tokenCategoriesFromParser = this.getTokenCategoriesFromParser(compilationUnits);

        CompilerAnnotationState compilerAnnotationHolder = this.compilerState.getCompilerAnnotationHolder();

        // TODO: Make the ColorScheme configurable
        var ansiTokenColorizer = new AnsiTokenColorizer(
                DarkColorScheme.INSTANCE,
                tokenCategoriesFromParser,
                tokenCategoriesFromLexer);
        compilerAnnotationHolder.setAnsiTokenColorizer(ansiTokenColorizer);

        this.compilerState.reportErrors();
        ImmutableList<RootCompilerAnnotation> compilerAnnotations = compilerAnnotationHolder.getCompilerAnnotations();

        return this.compilerState.getCompilationResult(compilerAnnotations);
    }

    private MapIterable<Token, TokenCategory> getTokenCategoriesFromLexer(ImmutableList<CompilationUnit> compilationUnits)
    {
        MutableMapIterable<Token, TokenCategory> tokenCategoriesFromLexer = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        compilationUnits
                .collect(CompilationUnit::getTokenStream)
                .forEachWith(LexerBasedTokenCategorizer::findTokenCategoriesFromLexer, tokenCategoriesFromLexer);
        return tokenCategoriesFromLexer.asUnmodifiable();
    }

    private MapIterable<Token, TokenCategory> getTokenCategoriesFromParser(ImmutableList<CompilationUnit> compilationUnits)
    {
        var listener = new ParserBasedTokenCategorizer();

        compilationUnits
                .collect(CompilationUnit::getParserContext)
                .forEachWith(ParserBasedTokenCategorizer::findTokenCategoriesFromParser, listener);
        return listener.getTokenCategories();
    }
}
