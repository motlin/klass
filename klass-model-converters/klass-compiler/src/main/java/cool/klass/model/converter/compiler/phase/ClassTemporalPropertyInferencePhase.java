package cool.klass.model.converter.compiler.phase;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveProperty;
import cool.klass.model.meta.domain.PrimitiveType;
import cool.klass.model.meta.domain.PrimitiveType.PrimitiveTypeBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.impl.factory.Lists;

public class ClassTemporalPropertyInferencePhase extends AbstractCompilerPhase
{
    private final AntlrDomainModel domainModelState;

    public ClassTemporalPropertyInferencePhase(
            CompilerErrorHolder compilerErrorHolder,
            MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.domainModelState = domainModelState;
    }

    @Override
    public void enterClassModifier(ClassModifierContext ctx)
    {
        AntlrClass classState = this.domainModelState.getClassByContext(this.classDeclarationContext);

        String modifierText = ctx.getText();
        // TODO: Inference happens for batches of three properties. It could check whether it needs to add each of the three individually
        if (!this.hasTemporalProperty(classState)
                && ("validTemporal".equals(modifierText) || "bitemporal".equals(modifierText)))
        {
            this.addTemporalProperties(classState, ctx, "valid");
        }

        if (!this.hasTemporalProperty(classState)
                && ("systemTemporal".equals(modifierText) || "bitemporal".equals(modifierText)))
        {
            this.addTemporalProperties(classState, ctx, "system");
        }
    }

    private boolean hasTemporalProperty(AntlrClass classState)
    {
        return classState
                .getDataTypeProperties()
                .anySatisfy(AntlrDataTypeProperty::isTemporal);
    }

    private void addTemporalProperties(
            AntlrClass classState,
            ClassModifierContext ctx,
            String prefix)
    {
        AntlrPrimitiveProperty temporalProperty = this.property(
                classState,
                ctx,
                prefix,
                PrimitiveType.TEMPORAL_RANGE);
        AntlrPrimitiveProperty temporalFromProperty = this.property(
                classState,
                ctx,
                prefix + "From",
                PrimitiveType.TEMPORAL_INSTANT);
        AntlrPrimitiveProperty temporalToProperty = this.property(
                classState,
                ctx,
                prefix + "To",
                PrimitiveType.TEMPORAL_INSTANT);

        classState.enterDataTypeProperty(temporalProperty);
        classState.enterDataTypeProperty(temporalFromProperty);
        classState.enterDataTypeProperty(temporalToProperty);
    }

    private AntlrPrimitiveProperty property(
            AntlrClass classState,
            ClassModifierContext ctx,
            String name,
            PrimitiveType primitiveType)
    {
        return new AntlrPrimitiveProperty(
                ctx,
                this.currentCompilationUnit,
                false,
                name,
                ctx,
                false,
                Lists.immutable.empty(),
                classState,
                new PrimitiveTypeBuilder(ctx, primitiveType));
    }
}
