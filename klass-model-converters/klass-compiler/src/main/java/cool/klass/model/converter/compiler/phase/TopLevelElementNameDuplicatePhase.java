package cool.klass.model.converter.compiler.phase;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import org.eclipse.collections.api.map.MapIterable;

public class TopLevelElementNameDuplicatePhase extends AbstractCompilerPhase
{
    @Nonnull
    private final TopLevelElementNameCountPhase compilerPhase;

    public TopLevelElementNameDuplicatePhase(
            @Nonnull MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext,
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull TopLevelElementNameCountPhase compilerPhase)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.compilerPhase = Objects.requireNonNull(compilerPhase);
    }

    @Override
    public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        this.logDuplicateTopLevelNames(ctx.identifier());
    }

    @Override
    public void enterEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
    {
        this.logDuplicateTopLevelNames(ctx.identifier());
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        this.logDuplicateTopLevelNames(ctx.identifier());
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        this.logDuplicateTopLevelNames(ctx.identifier());
    }

    protected void logDuplicateTopLevelNames(@Nonnull IdentifierContext identifier)
    {
        String topLevelItemName = identifier.getText();
        int occurrences = this.compilerPhase.occurrencesOf(topLevelItemName);
        if (occurrences > 1)
        {
            String message = String.format("Duplicate top level item name: '%s'.", topLevelItemName);
            this.error(message, identifier, this.currentCompilationUnit.getCompilationUnitContext());
        }
    }
}
