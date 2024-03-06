package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.meta.domain.api.service.Verb;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

public class ServiceCriteriaInferencePhase
        extends AbstractCompilerPhase
{
    public ServiceCriteriaInferencePhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "Service criteria";
    }

    @Override
    public void enterServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        super.enterServiceDeclaration(ctx);

        AntlrService service = this.compilerState.getCompilerWalk().getService();
        if (service.getServiceCriterias().isEmpty() && service.getVerb().getVerb() == Verb.GET)
        {
            String sourceCodeText = "            criteria    : all;\n";
            this.runCompilerMacro(sourceCodeText);
        }
    }

    private void runCompilerMacro(@Nonnull String sourceCodeText)
    {
        AntlrService      service       = this.compilerState.getCompilerWalk().getService();
        ParseTreeListener compilerPhase = new ServiceCriteriaPhase(this.compilerState);

        this.compilerState.runNonRootCompilerMacro(
                service,
                this,
                sourceCodeText,
                KlassParser::serviceCriteriaDeclaration,
                compilerPhase);
    }
}
