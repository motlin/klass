package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassModifier;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;

public class ServiceCriteriaInferencePhase extends AbstractCompilerPhase
{
    public ServiceCriteriaInferencePhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public String getName()
    {
        return "Service criteria";
    }

    @Override
    public void exitServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        AntlrService serviceState = this.compilerState.getCompilerWalkState().getServiceState();
        if (serviceState.needsVersionCriteriaInferred())
        {
            // TODO: ♻️ Get names from model (system, version, number, version)
            String sourceCodeText = "            version: this.system equalsEdgePoint && this.version.number == version;";
            this.compilerState.runNonRootCompilerMacro(
                    getMacroElement(),
                    ctx,
                    this,
                    sourceCodeText,
                    KlassParser::serviceCriteriaDeclaration);
        }

        if (serviceState.needsConflictCriteriaInferred())
        {
            // TODO: ♻️ Get names from model (version, version)
            String sourceCodeText = "            conflict: this.version.number == version;";
            this.compilerState.runNonRootCompilerMacro(
                    getMacroElement(),
                    ctx,
                    this,
                    sourceCodeText,
                    KlassParser::serviceCriteriaDeclaration);
        }

        super.exitServiceDeclaration(ctx);
    }

    private AntlrElement getMacroElement()
    {
        AntlrClass         classState        = this.compilerState.getCompilerWalkState().getServiceGroupState().getKlass();
        AntlrClassModifier versionedModifier = classState.getClassModifierByName("versioned");
        if (versionedModifier != null)
        {
            return versionedModifier;
        }

        return classState.getAssociationEndStates()
                .detect(AntlrAssociationEnd::isVersion)
                .getAssociationEndModifierByName("version");
    }
}
