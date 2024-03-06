package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPrimitiveProperty;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Lists;

public class ClassTemporalPropertyInferencePhase extends AbstractCompilerPhase
{
    private final AntlrDomainModel domainModelState;

    public ClassTemporalPropertyInferencePhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext, true);
        this.domainModelState = domainModelState;
    }

    @Override
    public void enterClassModifier(@Nonnull ClassModifierContext ctx)
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
            @Nonnull AntlrClass classState,
            @Nonnull ClassModifierContext ctx,
            @Nonnull String prefix)
    {
        // TODO: Syntax to link together temporal properties
        /*
        system            : TemporalRange   system;
        systemFrom        : TemporalInstant system from;
        systemTo          : TemporalInstant system to;
        */
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
            @Nonnull AntlrClass classState,
            @Nonnull ClassModifierContext ctx,
            @Nonnull String name,
            @Nonnull PrimitiveType primitiveType)
    {
        return new AntlrPrimitiveProperty(
                ctx,
                this.currentCompilationUnit,
                this.isInference,
                ctx,
                name,
                classState.getNumMembers() + 1,
                classState,
                false,
                // TODO: Temporal properties need "from" and "to" property modifiers
                Lists.immutable.empty(),
                AntlrPrimitiveType.valueOf(primitiveType));
    }
}
