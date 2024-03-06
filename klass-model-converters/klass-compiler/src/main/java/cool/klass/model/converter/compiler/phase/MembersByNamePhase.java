package cool.klass.model.converter.compiler.phase;

import java.util.LinkedHashMap;
import java.util.List;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.EscapedIdentifierVisitor;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassMemberContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class MembersByNamePhase extends AbstractCompilerPhase
{
    private static final ClassMemberContext AMBIGUOUS_CLASS_MEMBER =
            new ClassMemberContext(null, -1);

    private static final DataTypePropertyContext    AMBIGUOUS_DATA_TYPE_PROPERTY   =
            new DataTypePropertyContext(null, -1);
    private static final PrimitivePropertyContext   AMBIGUOUS_PRIMITIVE_PROPERTY   =
            new PrimitivePropertyContext(null, -1);
    private static final EnumerationPropertyContext AMBIGUOUS_ENUMERATION_PROPERTY =
            new EnumerationPropertyContext(null, -1);

    private static final AssociationEndContext AMBIGUOUS_ASSOCIATION_END =
            new AssociationEndContext(null, -1);

    private final DeclarationsByNamePhase    declarationsByNamePhase;
    private final ResolveTypeReferencesPhase resolveTypeReferencesPhase;
    private final ResolveTypesPhase          resolveTypesPhase;

    private final MutableOrderedMap<ClassDeclarationContext, MutableOrderedMap<String, ParserRuleContext>>
            classMembersByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableOrderedMap<ClassDeclarationContext, MutableOrderedMap<String, ParserRuleContext>>
            dataTypePropertiesByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableOrderedMap<ClassDeclarationContext, MutableOrderedMap<String, PrimitivePropertyContext>>
            primitivePropertiesByName   = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ClassDeclarationContext, MutableOrderedMap<String, EnumerationPropertyContext>>
            enumerationPropertiesByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ClassDeclarationContext, MutableOrderedMap<String, AssociationEndContext>>
            associationEndsByName       = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected MembersByNamePhase(
            CompilerErrorHolder compilerErrorHolder,
            MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext,
            DeclarationsByNamePhase declarationsByNamePhase,
            ResolveTypeReferencesPhase resolveTypeReferencesPhase,
            ResolveTypesPhase resolveTypesPhase)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.declarationsByNamePhase = declarationsByNamePhase;
        this.resolveTypeReferencesPhase = resolveTypeReferencesPhase;
        this.resolveTypesPhase = resolveTypesPhase;
    }

    @Override
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        super.enterAssociationDeclaration(ctx);

        List<AssociationEndContext> associationEndContexts = ctx.associationBody().associationEnd();
        if (associationEndContexts.size() == 2)
        {
            AssociationEndContext source = associationEndContexts.get(0);
            AssociationEndContext target = associationEndContexts.get(1);

            ClassDeclarationContext sourceReturnType = this.resolveTypesPhase.getType(source);
            ClassDeclarationContext targetReturnType = this.resolveTypesPhase.getType(target);

            // TODO: process association ends as properties
            if (sourceReturnType != DeclarationsByNamePhase.NO_SUCH_CLASS
                && sourceReturnType != DeclarationsByNamePhase.AMBIGUOUS_CLASS_DECLARATION)
            {
                // target end is member of source
            }
        }
    }

    @Override
    public void enterPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        String name = EscapedIdentifierVisitor.getName(ctx.escapedIdentifier());

        MutableOrderedMap<String, ParserRuleContext> members = this.classMembersByName.getIfAbsentPut(
                this.classDeclarationContext,
                OrderedMapAdapter.adapt(new LinkedHashMap<>()));

        MutableOrderedMap<String, ParserRuleContext> dataTypeProperties =
                this.dataTypePropertiesByName.getIfAbsentPut(
                        this.classDeclarationContext,
                        OrderedMapAdapter.adapt(new LinkedHashMap<>()));

        MutableOrderedMap<String, PrimitivePropertyContext> primitiveProperties =
                this.primitivePropertiesByName.getIfAbsentPut(
                        this.classDeclarationContext,
                        OrderedMapAdapter.adapt(new LinkedHashMap<>()));

        members.compute(
                name,
                (nameParameter, classMemberContext) -> classMemberContext == null
                        ? ctx
                        : AMBIGUOUS_CLASS_MEMBER);

        dataTypeProperties.compute(
                name,
                (nameParameter, dataTypePropertyContext) -> dataTypePropertyContext == null
                        ? ctx
                        : AMBIGUOUS_DATA_TYPE_PROPERTY);

        primitiveProperties.compute(
                name,
                (nameParameter, primitivePropertyContext) -> primitivePropertyContext == null
                        ? ctx
                        : AMBIGUOUS_PRIMITIVE_PROPERTY);
    }

    @Override
    public void enterEnumerationProperty(EnumerationPropertyContext ctx)
    {
        String name = EscapedIdentifierVisitor.getName(ctx.escapedIdentifier());

        MutableOrderedMap<String, ParserRuleContext> members = this.classMembersByName.getIfAbsentPut(
                this.classDeclarationContext,
                OrderedMapAdapter.adapt(new LinkedHashMap<>()));

        MutableOrderedMap<String, ParserRuleContext> dataTypeProperties =
                this.dataTypePropertiesByName.getIfAbsentPut(
                        this.classDeclarationContext,
                        OrderedMapAdapter.adapt(new LinkedHashMap<>()));

        MutableOrderedMap<String, EnumerationPropertyContext> enumerationProperties =
                this.enumerationPropertiesByName.getIfAbsentPut(
                        this.classDeclarationContext,
                        OrderedMapAdapter.adapt(new LinkedHashMap<>()));

        members.compute(
                name,
                (nameParameter, classMemberContext) -> classMemberContext == null
                        ? ctx
                        : AMBIGUOUS_CLASS_MEMBER);

        dataTypeProperties.compute(
                name,
                (nameParameter, dataTypePropertyContext) -> dataTypePropertyContext == null
                        ? ctx
                        : AMBIGUOUS_DATA_TYPE_PROPERTY);

        enumerationProperties.compute(
                name,
                (nameParameter, enumerationPropertyContext) -> enumerationPropertyContext == null
                        ? ctx
                        : AMBIGUOUS_ENUMERATION_PROPERTY);
    }
}
