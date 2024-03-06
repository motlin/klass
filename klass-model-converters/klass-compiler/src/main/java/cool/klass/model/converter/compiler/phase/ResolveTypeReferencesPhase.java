package cool.klass.model.converter.compiler.phase;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class ResolveTypeReferencesPhase extends KlassBaseListener
{
    private final DeclarationsByNamePhase declarationsByNamePhase;

    private final MutableOrderedMap<EnumerationReferenceContext, EnumerationDeclarationContext> enumerationDeclarationsByReference =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ClassReferenceContext, ClassDeclarationContext>             classDeclarationsByReference       =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ProjectionReferenceContext, ProjectionDeclarationContext>   projectionDeclarationsByReference  =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    public ResolveTypeReferencesPhase(DeclarationsByNamePhase declarationsByNamePhase)
    {
        this.declarationsByNamePhase = declarationsByNamePhase;
    }

    @Override
    public void enterClassReference(@Nonnull ClassReferenceContext ctx)
    {
        IdentifierContext       identifier              = ctx.identifier();
        String                  className               = identifier.getText();
        ClassDeclarationContext classDeclarationContext = this.declarationsByNamePhase.getClassByName(className);
        this.classDeclarationsByReference.put(ctx, classDeclarationContext);
    }

    @Override
    public void enterEnumerationReference(@Nonnull EnumerationReferenceContext ctx)
    {
        IdentifierContext identifier      = ctx.identifier();
        String            enumerationName = identifier.getText();
        EnumerationDeclarationContext enumerationDeclarationContext =
                this.declarationsByNamePhase.getEnumerationByName(enumerationName);
        this.enumerationDeclarationsByReference.put(ctx, enumerationDeclarationContext);
    }

    @Override
    public void enterProjectionReference(@Nonnull ProjectionReferenceContext ctx)
    {
        IdentifierContext identifier     = ctx.identifier();
        String            projectionName = identifier.getText();
        ProjectionDeclarationContext projectionDeclarationContext =
                this.declarationsByNamePhase.getProjectionByName(projectionName);
        this.projectionDeclarationsByReference.put(ctx, projectionDeclarationContext);
    }

    public EnumerationDeclarationContext getEnumerationByReference(EnumerationReferenceContext enumerationReferenceContext)
    {
        return this.enumerationDeclarationsByReference.get(enumerationReferenceContext);
    }

    public ClassDeclarationContext getClassByReference(ClassReferenceContext classReferenceContext)
    {
        return this.classDeclarationsByReference.get(classReferenceContext);
    }

    public ProjectionDeclarationContext getProjectionByReference(ProjectionReferenceContext projectionReferenceContext)
    {
        return this.projectionDeclarationsByReference.get(projectionReferenceContext);
    }
}
