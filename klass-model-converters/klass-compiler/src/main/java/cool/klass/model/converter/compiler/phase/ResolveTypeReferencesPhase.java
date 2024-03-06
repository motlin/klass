package cool.klass.model.converter.compiler.phase;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class ResolveTypeReferencesPhase extends AbstractDomainModelCompilerPhase
{
    private final MutableOrderedMap<EnumerationReferenceContext, AntlrEnumeration> enumerationsByReference           =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ClassReferenceContext, AntlrClass>             classesByReference                =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ProjectionReferenceContext, AntlrProjection>   projectionDeclarationsByReference =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    public ResolveTypeReferencesPhase(
            CompilerErrorHolder compilerErrorHolder,
            MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext, false, domainModelState);
    }

    @Override
    public void enterClassReference(@Nonnull ClassReferenceContext ctx)
    {
        String     className  = ctx.identifier().getText();
        AntlrClass classState = this.domainModelState.getClassByName(className);
        this.classesByReference.put(ctx, classState);
    }

    @Override
    public void enterEnumerationReference(@Nonnull EnumerationReferenceContext ctx)
    {
        String           enumerationName  = ctx.identifier().getText();
        AntlrEnumeration enumerationState = this.domainModelState.getEnumerationByName(enumerationName);
        this.enumerationsByReference.put(ctx, enumerationState);
    }

    @Override
    public void enterProjectionReference(@Nonnull ProjectionReferenceContext ctx)
    {
        String          projectionName  = ctx.identifier().getText();
        AntlrProjection projectionState = this.domainModelState.getProjectionByName(projectionName);
        this.projectionDeclarationsByReference.put(ctx, projectionState);
    }

    public AntlrEnumeration getEnumerationByReference(EnumerationReferenceContext enumerationReferenceContext)
    {
        return this.enumerationsByReference.get(enumerationReferenceContext);
    }

    public AntlrClass getClassByReference(ClassReferenceContext classReferenceContext)
    {
        return this.classesByReference.get(classReferenceContext);
    }

    public AntlrProjection getProjectionByReference(ProjectionReferenceContext projectionReferenceContext)
    {
        return this.projectionDeclarationsByReference.get(projectionReferenceContext);
    }
}
