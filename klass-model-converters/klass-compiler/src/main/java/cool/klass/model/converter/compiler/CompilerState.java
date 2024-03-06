package cool.klass.model.converter.compiler;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.annotation.AbstractCompilerAnnotation;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationHolder;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.converter.compiler.parser.AntlrUtils;
import cool.klass.model.converter.compiler.phase.AbstractCompilerPhase;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.meta.domain.DomainModelImpl.DomainModelBuilder;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByDirectionContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceOrderByDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.TopLevelDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
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
    private       CompilerWalkState       compilerWalk             = new CompilerWalkState(this.domainModel);

    private final MutableOrderedMap<CompilationUnit, CompilerWalkState> macroCompilerWalks =
            OrderedMaps.adapt(new LinkedHashMap<>());

    public CompilerState(@Nonnull ImmutableList<CompilationUnit> compilationUnits)
    {
        this.compilerInput = new CompilerInputState(compilationUnits);
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
            extends KlassBaseListener
    {
        @Override
        public void enterCompilationUnit(@Nonnull CompilationUnitContext ctx)
        {
            super.enterCompilationUnit(ctx);

            CompilerState.this.compilerWalk.asListener().enterCompilationUnit(ctx);

            CompilationUnit currentCompilationUnit = CompilerState.this.compilerInput.getCompilationUnitByContext(
                    ctx);
            CompilerState.this.compilerWalk.enterCompilationUnit(currentCompilationUnit);
        }

        @Override
        public void exitCompilationUnit(@Nonnull CompilationUnitContext ctx)
        {
            super.exitCompilationUnit(ctx);

            CompilerState.this.compilerWalk.asListener().exitCompilationUnit(ctx);

            CompilerState.this.compilerWalk.exitCompilationUnit();
        }

        @Override
        public void enterPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
        {
            super.enterPackageDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterPackageDeclaration(ctx);
        }

        @Override
        public void enterTopLevelDeclaration(@Nonnull TopLevelDeclarationContext ctx)
        {
            super.enterTopLevelDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterTopLevelDeclaration(ctx);
        }

        @Override
        public void exitTopLevelDeclaration(@Nonnull TopLevelDeclarationContext ctx)
        {
            super.exitTopLevelDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().exitTopLevelDeclaration(ctx);
        }

        @Override
        public void enterInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
        {
            super.enterInterfaceDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterInterfaceDeclaration(ctx);
        }

        @Override
        public void exitInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
        {
            super.exitInterfaceDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().exitInterfaceDeclaration(ctx);
        }

        @Override
        public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
        {
            super.enterClassDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterClassDeclaration(ctx);
        }

        @Override
        public void exitClassDeclaration(@Nonnull ClassDeclarationContext ctx)
        {
            super.exitClassDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().exitClassDeclaration(ctx);
        }

        @Override
        public void enterEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
        {
            super.enterEnumerationDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterEnumerationDeclaration(ctx);
        }

        @Override
        public void exitEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
        {
            super.exitEnumerationDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().exitEnumerationDeclaration(ctx);
        }

        @Override
        public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
        {
            super.enterAssociationDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterAssociationDeclaration(ctx);
        }

        @Override
        public void exitAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
        {
            super.exitAssociationDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().exitAssociationDeclaration(ctx);
        }

        @Override
        public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
        {
            super.enterAssociationEnd(ctx);

            CompilerState.this.compilerWalk.asListener().enterAssociationEnd(ctx);
        }

        @Override
        public void exitAssociationEnd(@Nonnull AssociationEndContext ctx)
        {
            super.exitAssociationEnd(ctx);

            CompilerState.this.compilerWalk.asListener().exitAssociationEnd(ctx);
        }

        @Override
        public void enterAssociationEndSignature(@Nonnull AssociationEndSignatureContext ctx)
        {
            super.enterAssociationEndSignature(ctx);

            CompilerState.this.compilerWalk.asListener().enterAssociationEndSignature(ctx);
        }

        @Override
        public void exitAssociationEndSignature(@Nonnull AssociationEndSignatureContext ctx)
        {
            super.exitAssociationEndSignature(ctx);

            CompilerState.this.compilerWalk.asListener().exitAssociationEndSignature(ctx);
        }

        @Override
        public void enterRelationship(@Nonnull RelationshipContext ctx)
        {
            super.enterRelationship(ctx);

            CompilerState.this.compilerWalk.asListener().enterRelationship(ctx);
        }

        @Override
        public void exitRelationship(@Nonnull RelationshipContext ctx)
        {
            super.exitRelationship(ctx);

            CompilerState.this.compilerWalk.asListener().exitRelationship(ctx);
        }

        @Override
        public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
        {
            super.enterProjectionDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterProjectionDeclaration(ctx);
        }

        @Override
        public void exitProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
        {
            super.exitProjectionDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().exitProjectionDeclaration(ctx);
        }

        @Override
        public void enterServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
        {
            super.enterServiceGroupDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterServiceGroupDeclaration(ctx);
        }

        @Override
        public void exitServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
        {
            super.exitServiceGroupDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().exitServiceGroupDeclaration(ctx);
        }

        @Override
        public void enterUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
        {
            super.enterUrlDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterUrlDeclaration(ctx);
        }

        @Override
        public void exitUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
        {
            super.exitUrlDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().exitUrlDeclaration(ctx);
        }

        @Override
        public void enterServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
        {
            super.enterServiceDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterServiceDeclaration(ctx);
        }

        @Override
        public void exitServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
        {
            super.exitServiceDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().exitServiceDeclaration(ctx);
        }

        @Override
        public void enterParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
        {
            super.enterParameterizedProperty(ctx);

            CompilerState.this.compilerWalk.asListener().enterParameterizedProperty(ctx);
        }

        @Override
        public void exitParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
        {
            super.exitParameterizedProperty(ctx);

            CompilerState.this.compilerWalk.asListener().exitParameterizedProperty(ctx);
        }

        @Override
        public void enterClassifierModifier(@Nonnull ClassifierModifierContext ctx)
        {
            super.enterClassifierModifier(ctx);

            CompilerState.this.compilerWalk.asListener().enterClassifierModifier(ctx);
        }

        @Override
        public void exitClassifierModifier(@Nonnull ClassifierModifierContext ctx)
        {
            super.exitClassifierModifier(ctx);

            CompilerState.this.compilerWalk.asListener().exitClassifierModifier(ctx);
        }

        @Override
        public void enterServiceOrderByDeclaration(ServiceOrderByDeclarationContext ctx)
        {
            super.enterServiceOrderByDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterServiceOrderByDeclaration(ctx);
        }

        @Override
        public void exitServiceOrderByDeclaration(ServiceOrderByDeclarationContext ctx)
        {
            CompilerState.this.compilerWalk.asListener().exitServiceOrderByDeclaration(ctx);

            super.exitServiceOrderByDeclaration(ctx);
        }

        @Override
        public void enterOrderByDeclaration(OrderByDeclarationContext ctx)
        {
            super.enterOrderByDeclaration(ctx);

            CompilerState.this.compilerWalk.asListener().enterOrderByDeclaration(ctx);
        }

        @Override
        public void exitOrderByDeclaration(OrderByDeclarationContext ctx)
        {
            CompilerState.this.compilerWalk.asListener().exitOrderByDeclaration(ctx);

            super.exitOrderByDeclaration(ctx);
        }

        @Override
        public void enterOrderByMemberReferencePath(OrderByMemberReferencePathContext ctx)
        {
            super.enterOrderByMemberReferencePath(ctx);

            CompilerState.this.compilerWalk.asListener().enterOrderByMemberReferencePath(ctx);
        }

        @Override
        public void exitOrderByMemberReferencePath(OrderByMemberReferencePathContext ctx)
        {
            CompilerState.this.compilerWalk.asListener().exitOrderByMemberReferencePath(ctx);

            super.exitOrderByMemberReferencePath(ctx);
        }

        @Override
        public void enterOrderByDirection(OrderByDirectionContext ctx)
        {
            super.enterOrderByDirection(ctx);

            CompilerState.this.compilerWalk.asListener().enterOrderByDirection(ctx);
        }

        @Override
        public void exitOrderByDirection(OrderByDirectionContext ctx)
        {
            CompilerState.this.compilerWalk.asListener().exitOrderByDirection(ctx);

            super.exitOrderByDirection(ctx);
        }
    }
}
