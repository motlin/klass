package cool.klass.model.converter.compiler.phase;

import java.util.LinkedHashMap;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClassState;
import cool.klass.model.converter.compiler.state.AntlrEnumerationState;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.HeaderContext;
import cool.klass.model.meta.grammar.KlassParser.PackageNameContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionMemberWithBodyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;
import cool.klass.model.meta.grammar.KlassParser.PropertyModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class BuildAntlrStatePhase extends AbstractCompilerPhase
{
    private final MutableOrderedMap<EnumerationDeclarationContext, AntlrEnumerationState> enumerationStates = BuildAntlrStatePhase.map();
    private final MutableOrderedMap<ClassDeclarationContext, AntlrClassState>             classStates       = BuildAntlrStatePhase.map();

    private AntlrEnumerationState enumerationState;
    private AntlrClassState       classState;

    public BuildAntlrStatePhase(
            CompilerErrorHolder compilerErrorHolder,
            MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
    }

    private static <K, V> MutableOrderedMap<K, V> map()
    {
        return OrderedMapAdapter.adapt(new LinkedHashMap<>());
    }

    @Override
    public void exitCompilationUnit(CompilationUnitContext ctx)
    {
        this.packageName = null;
    }

    @Override
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        this.classState = new AntlrClassState(ctx);
        this.classStates.put(ctx, this.classState);
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.classState = null;
    }

    @Override
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.enumerationState = new AntlrEnumerationState(ctx);
        this.enumerationStates.put(ctx, this.enumerationState);
    }

    @Override
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.enumerationState.postProcess(this);
        this.enumerationState = null;
    }

    @Override
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        // TODO
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        // TODO
    }

    @Override
    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        // TODO
    }

    @Override
    public void exitProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        // TODO
    }

    @Override
    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        // TODO
    }

    @Override
    public void exitServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        // TODO
    }

    @Override
    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        // TODO
    }

    @Override
    public void exitParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        // TODO
    }

    @Override
    public void enterPackageName(PackageNameContext ctx)
    {
        this.packageName = ctx.getText();
    }

    @Override
    public void enterEnumerationLiteral(EnumerationLiteralContext ctx)
    {
        String literalName = ctx.identifier().getText();

        EnumerationPrettyNameContext prettyNameContext = ctx.enumerationPrettyName();

        String prettyName = prettyNameContext == null
                ? null
                : prettyNameContext.getText().substring(1, prettyNameContext.getText().length() - 1);

        this.enumerationState.addLiteral(ctx, prettyNameContext, literalName, prettyName);
    }

    @Override
    public void enterEnumerationPrettyName(EnumerationPrettyNameContext ctx)
    {
        // TODO
    }

    @Override
    public void enterAssociationEnd(AssociationEndContext ctx)
    {
        // TODO
    }

    @Override
    public void exitAssociationEnd(AssociationEndContext ctx)
    {
        // TODO
    }

    @Override
    public void enterProjectionMemberWithBody(ProjectionMemberWithBodyContext ctx)
    {
        // TODO
    }

    @Override
    public void exitProjectionMemberWithBody(ProjectionMemberWithBodyContext ctx)
    {
        // TODO
    }

    @Override
    public void enterProjectionPrimitiveMember(ProjectionPrimitiveMemberContext ctx)
    {
        // TODO
    }

    @Override
    public void exitProjectionPrimitiveMember(ProjectionPrimitiveMemberContext ctx)
    {
        // TODO
    }

    @Override
    public void enterHeader(HeaderContext ctx)
    {
        // TODO
    }

    @Override
    public void exitHeader(HeaderContext ctx)
    {
        // TODO
    }

    @Override
    public void enterPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        // TODO
    }

    @Override
    public void exitPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        // TODO
    }

    @Override
    public void enterEnumerationProperty(EnumerationPropertyContext ctx)
    {
        // TODO
    }

    @Override
    public void exitEnumerationProperty(EnumerationPropertyContext ctx)
    {
        // TODO
    }

    @Override
    public void enterClassModifier(ClassModifierContext ctx)
    {
        // TODO
    }

    @Override
    public void enterPropertyModifier(PropertyModifierContext ctx)
    {
        // TODO
    }

    @Override
    public void enterAssociationEndModifier(AssociationEndModifierContext ctx)
    {
        // TODO
    }
}
