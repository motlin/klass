package cool.klass.model.converter.compiler.phase;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.map.mutable.MapAdapter;

public class DeclarationsByNamePhase extends KlassBaseListener
{
    @Nullable
    public static final ParserRuleContext             AMBIGUOUS_DECLARATION             =
            new ParserRuleContext(null, -1);
    @Nullable
    public static final EnumerationDeclarationContext AMBIGUOUS_ENUMERATION_DECLARATION =
            new EnumerationDeclarationContext(null, -1);
    @Nullable
    public static final ClassDeclarationContext       AMBIGUOUS_CLASS_DECLARATION       =
            new ClassDeclarationContext(null, -1);
    @Nullable
    public static final AssociationDeclarationContext AMBIGUOUS_ASSOCIATION_DECLARATION =
            new AssociationDeclarationContext(null, -1);
    @Nullable
    public static final ProjectionDeclarationContext  AMBIGUOUS_PROJECTION_DECLARATION  =
            new ProjectionDeclarationContext(null, -1);

    @Nullable
    public static final EnumerationDeclarationContext NO_SUCH_ENUMERATION = new EnumerationDeclarationContext(null, -1);
    @Nullable
    public static final ClassDeclarationContext       NO_SUCH_CLASS       = new ClassDeclarationContext(null, -1);
    @Nullable
    public static final AssociationDeclarationContext NO_SUCH_ASSOCIATION = new AssociationDeclarationContext(null, -1);
    @Nullable
    public static final ProjectionDeclarationContext  NO_SUCH_PROJECTION  = new ProjectionDeclarationContext(null, -1);

    private final MutableMap<String, ParserRuleContext> declarationsByName = MapAdapter.adapt(new LinkedHashMap<>());

    private final MutableMap<String, EnumerationDeclarationContext> enumerationsByName =
            MapAdapter.adapt(new LinkedHashMap<>());
    private final MutableMap<String, ClassDeclarationContext>       classesByName      =
            MapAdapter.adapt(new LinkedHashMap<>());
    private final MutableMap<String, AssociationDeclarationContext> associationsByName =
            MapAdapter.adapt(new LinkedHashMap<>());
    private final MutableMap<String, ProjectionDeclarationContext>  projectionsByName  =
            MapAdapter.adapt(new LinkedHashMap<>());

    @Override
    public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        this.declarationsByName.compute(
                ctx.identifier().getText(),
                (name, classDeclarationContext) -> classDeclarationContext == null
                        ? ctx
                        : AMBIGUOUS_DECLARATION);
        this.classesByName.compute(
                ctx.identifier().getText(),
                (name, classDeclarationContext) -> classDeclarationContext == null
                        ? ctx
                        : AMBIGUOUS_CLASS_DECLARATION);
    }

    @Override
    public void enterEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
    {
        this.declarationsByName.compute(
                ctx.identifier().getText(),
                (name, enumerationDeclarationContext) -> enumerationDeclarationContext == null
                        ? ctx
                        : AMBIGUOUS_DECLARATION);
        this.enumerationsByName.compute(
                ctx.identifier().getText(),
                (name, enumerationDeclarationContext) -> enumerationDeclarationContext == null
                        ? ctx
                        : AMBIGUOUS_ENUMERATION_DECLARATION);
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        this.declarationsByName.compute(
                ctx.identifier().getText(),
                (name, associationDeclarationContext) -> associationDeclarationContext == null
                        ? ctx
                        : AMBIGUOUS_DECLARATION);
        this.associationsByName.compute(
                ctx.identifier().getText(),
                (name, associationDeclarationContext) -> associationDeclarationContext == null
                        ? ctx
                        : AMBIGUOUS_ASSOCIATION_DECLARATION);
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        this.declarationsByName.compute(
                ctx.identifier().getText(),
                (name, projectionDeclarationContext) -> projectionDeclarationContext == null
                        ? ctx
                        : AMBIGUOUS_DECLARATION);
        this.projectionsByName.compute(
                ctx.identifier().getText(),
                (name, projectionDeclarationContext) -> projectionDeclarationContext == null
                        ? ctx
                        : AMBIGUOUS_PROJECTION_DECLARATION);
    }

    public EnumerationDeclarationContext getEnumerationByName(String enumerationName)
    {
        return this.enumerationsByName.getIfAbsentValue(enumerationName, NO_SUCH_ENUMERATION);
    }

    public ClassDeclarationContext getClassByName(String className)
    {
        return this.classesByName.getIfAbsentValue(className, NO_SUCH_CLASS);
    }

    public AssociationDeclarationContext getAssociationByName(String associationName)
    {
        return this.associationsByName.getIfAbsentValue(associationName, NO_SUCH_ASSOCIATION);
    }

    public ProjectionDeclarationContext getProjectionByName(String projectionName)
    {
        return this.projectionsByName.getIfAbsentValue(projectionName, NO_SUCH_PROJECTION);
    }

    public ParserRuleContext getDeclarationByName(String declarationName)
    {
        return this.declarationsByName.get(declarationName);
    }
}
