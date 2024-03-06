package cool.klass.model.converter.compiler.phase;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassServiceModifierContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypeContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypeDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class ResolveTypesPhase extends KlassBaseListener
{
    private final ResolveTypeReferencesPhase resolveTypeReferencesPhase;

    private final MutableOrderedMap<EnumerationPropertyContext, EnumerationDeclarationContext>      enumerationPropertyTypes   = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());
    private final MutableOrderedMap<ParameterizedPropertyContext, ClassDeclarationContext>          parameterizedPropertyTypes = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());
    private final MutableOrderedMap<AssociationEndContext, ClassDeclarationContext>                 associationEndTypes        = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());
    private final MutableOrderedMap<ClassServiceModifierContext, ProjectionDeclarationContext>      classServiceModifiers      = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());
    private final MutableOrderedMap<ProjectionDeclarationContext, ClassDeclarationContext>          projectionDeclarations     = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());
    private final MutableOrderedMap<ServiceGroupDeclarationContext, ClassDeclarationContext>        serviceGroupDeclarations   = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());
    private final MutableOrderedMap<ServiceProjectionDispatchContext, ProjectionDeclarationContext> serviceProjectDispatches   = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());
    private final MutableOrderedMap<ParameterDeclarationContext, EnumerationDeclarationContext>     parameterTypes             = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());

    public ResolveTypesPhase(ResolveTypeReferencesPhase resolveTypeReferencesPhase)
    {
        this.resolveTypeReferencesPhase = resolveTypeReferencesPhase;
    }

    @Override
    public void enterClassServiceModifier(@Nonnull ClassServiceModifierContext ctx)
    {
        ProjectionReferenceContext   reference   = ctx.projectionReference();
        ProjectionDeclarationContext declaration = this.resolveTypeReferencesPhase.getProjectionByReference(reference);
        this.classServiceModifiers.put(ctx, declaration);
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        ClassReferenceContext   reference   = ctx.classType().classReference();
        ClassDeclarationContext declaration = this.resolveTypeReferencesPhase.getClassByReference(reference);
        this.associationEndTypes.put(ctx, declaration);
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        ClassReferenceContext   reference   = ctx.classReference();
        ClassDeclarationContext declaration = this.resolveTypeReferencesPhase.getClassByReference(reference);
        this.projectionDeclarations.put(ctx, declaration);
    }

    @Override
    public void enterServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
    {
        ClassReferenceContext   reference   = ctx.classReference();
        ClassDeclarationContext declaration = this.resolveTypeReferencesPhase.getClassByReference(reference);
        this.serviceGroupDeclarations.put(ctx, declaration);
    }

    @Override
    public void enterServiceProjectionDispatch(@Nonnull ServiceProjectionDispatchContext ctx)
    {
        ProjectionReferenceContext   reference   = ctx.projectionReference();
        ProjectionDeclarationContext declaration = this.resolveTypeReferencesPhase.getProjectionByReference(reference);
        this.serviceProjectDispatches.put(ctx, declaration);
    }

    @Override
    public void enterEnumerationProperty(@Nonnull EnumerationPropertyContext ctx)
    {
        EnumerationReferenceContext   reference   = ctx.enumerationReference();
        EnumerationDeclarationContext declaration = this.resolveTypeReferencesPhase.getEnumerationByReference(reference);
        this.enumerationPropertyTypes.put(ctx, declaration);
    }

    @Override
    public void enterParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
    {
        ClassReferenceContext   reference   = ctx.classType().classReference();
        ClassDeclarationContext declaration = this.resolveTypeReferencesPhase.getClassByReference(reference);
        this.parameterizedPropertyTypes.put(ctx, declaration);
    }

    @Override
    public void enterParameterDeclaration(@Nonnull ParameterDeclarationContext ctx)
    {
        DataTypeDeclarationContext  dataTypeDeclarationContext = ctx.dataTypeDeclaration();
        DataTypeContext             dataTypeContext            = dataTypeDeclarationContext.dataType();
        EnumerationReferenceContext reference                  = dataTypeContext.enumerationReference();
        if (reference == null)
        {
            return;
        }
        EnumerationDeclarationContext declaration = this.resolveTypeReferencesPhase.getEnumerationByReference(reference);
        this.parameterTypes.put(ctx, declaration);
    }

    public ClassDeclarationContext getType(AssociationEndContext ctx)
    {
        return this.associationEndTypes.get(ctx);
    }

    public ClassDeclarationContext getType(ProjectionDeclarationContext ctx)
    {
        return this.projectionDeclarations.get(ctx);
    }

    public EnumerationDeclarationContext getType(EnumerationPropertyContext ctx)
    {
        return this.enumerationPropertyTypes.get(ctx);
    }

    public EnumerationDeclarationContext getType(ParameterDeclarationContext ctx)
    {
        return this.parameterTypes.get(ctx);
    }

    public ClassDeclarationContext getType(ServiceGroupDeclarationContext ctx)
    {
        return this.serviceGroupDeclarations.get(ctx);
    }

    public ProjectionDeclarationContext getType(ServiceProjectionDispatchContext ctx)
    {
        return this.serviceProjectDispatches.get(ctx);
    }

    public ProjectionDeclarationContext getType(ClassServiceModifierContext ctx)
    {
        return this.classServiceModifiers.get(ctx);
    }

    public ClassDeclarationContext getType(ParameterizedPropertyContext ctx)
    {
        return this.parameterizedPropertyTypes.get(ctx);
    }

    // TODO: Resolve types for enterTypeReference, using a combination of context for 'this' and classReference
}
