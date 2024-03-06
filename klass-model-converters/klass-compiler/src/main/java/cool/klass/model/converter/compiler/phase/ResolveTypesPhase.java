package cool.klass.model.converter.compiler.phase;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassServiceModifierContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
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

    private final MutableOrderedMap<EnumerationPropertyContext, AntlrEnumeration>      enumerationPropertyTypes   =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ParameterizedPropertyContext, AntlrClass>          parameterizedPropertyTypes =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<AssociationEndContext, AntlrClass>                 associationEndTypes        =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ClassServiceModifierContext, AntlrProjection>      classServiceModifiers      =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ProjectionDeclarationContext, AntlrClass>          projectionDeclarations     =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ServiceGroupDeclarationContext, AntlrClass>        serviceGroupDeclarations   =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ServiceProjectionDispatchContext, AntlrProjection> serviceProjectDispatches   =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableOrderedMap<EnumerationParameterDeclarationContext, AntlrEnumeration> enumerationParameterTypes =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    public ResolveTypesPhase(ResolveTypeReferencesPhase resolveTypeReferencesPhase)
    {
        this.resolveTypeReferencesPhase = resolveTypeReferencesPhase;
    }

    @Override
    public void enterClassServiceModifier(@Nonnull ClassServiceModifierContext ctx)
    {
        ProjectionReferenceContext reference       = ctx.projectionReference();
        AntlrProjection            projectionState = this.resolveTypeReferencesPhase.getProjectionByReference(reference);
        this.classServiceModifiers.put(ctx, projectionState);
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        ClassReferenceContext reference  = ctx.classType().classReference();
        AntlrClass            classState = this.resolveTypeReferencesPhase.getClassByReference(reference);
        this.associationEndTypes.put(ctx, classState);
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        ClassReferenceContext reference  = ctx.classReference();
        AntlrClass            classState = this.resolveTypeReferencesPhase.getClassByReference(reference);
        this.projectionDeclarations.put(ctx, classState);
    }

    @Override
    public void enterServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
    {
        ClassReferenceContext reference  = ctx.classReference();
        AntlrClass            classState = this.resolveTypeReferencesPhase.getClassByReference(reference);
        this.serviceGroupDeclarations.put(ctx, classState);
    }

    @Override
    public void enterServiceProjectionDispatch(@Nonnull ServiceProjectionDispatchContext ctx)
    {
        ProjectionReferenceContext reference       = ctx.projectionReference();
        AntlrProjection            projectionState = this.resolveTypeReferencesPhase.getProjectionByReference(reference);
        this.serviceProjectDispatches.put(ctx, projectionState);
    }

    @Override
    public void enterEnumerationProperty(@Nonnull EnumerationPropertyContext ctx)
    {
        EnumerationReferenceContext reference = ctx.enumerationReference();
        AntlrEnumeration enumerationState = this.resolveTypeReferencesPhase.getEnumerationByReference(
                reference);
        this.enumerationPropertyTypes.put(ctx, enumerationState);
    }

    @Override
    public void enterParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
    {
        ClassReferenceContext reference  = ctx.classType().classReference();
        AntlrClass            classState = this.resolveTypeReferencesPhase.getClassByReference(reference);
        this.parameterizedPropertyTypes.put(ctx, classState);
    }

    @Override
    public void enterEnumerationParameterDeclaration(@Nonnull EnumerationParameterDeclarationContext ctx)
    {
        EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();
        if (enumerationReferenceContext == null)
        {
            return;
        }
        AntlrEnumeration enumerationState = this.resolveTypeReferencesPhase.getEnumerationByReference(
                enumerationReferenceContext);
        this.enumerationParameterTypes.put(ctx, enumerationState);
    }

    public AntlrClass getType(AssociationEndContext ctx)
    {
        return this.associationEndTypes.get(ctx);
    }

    public AntlrClass getType(ProjectionDeclarationContext ctx)
    {
        return this.projectionDeclarations.get(ctx);
    }

    public AntlrEnumeration getType(EnumerationPropertyContext ctx)
    {
        return this.enumerationPropertyTypes.get(ctx);
    }

    public AntlrEnumeration getType(EnumerationParameterDeclarationContext ctx)
    {
        return this.enumerationParameterTypes.get(ctx);
    }

    public AntlrClass getType(ServiceGroupDeclarationContext ctx)
    {
        return this.serviceGroupDeclarations.get(ctx);
    }

    public AntlrProjection getType(ServiceProjectionDispatchContext ctx)
    {
        return this.serviceProjectDispatches.get(ctx);
    }

    public AntlrProjection getType(ClassServiceModifierContext ctx)
    {
        return this.classServiceModifiers.get(ctx);
    }

    public AntlrClass getType(ParameterizedPropertyContext ctx)
    {
        return this.parameterizedPropertyTypes.get(ctx);
    }

    // TODO: Resolve types for enterTypeReference, using a combination of context for 'this' and classReference
}
