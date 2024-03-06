package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ServiceBodyContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

public class ServiceMultiplicityInferencePhase
        extends AbstractCompilerPhase
{
    public ServiceMultiplicityInferencePhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "Service multiplicity";
    }

    @Override
    public void exitServiceBody(ServiceBodyContext ctx)
    {
        this.runCompilerMacro(ctx);
        super.exitServiceBody(ctx);
    }

    private void runCompilerMacro(ServiceBodyContext inPlaceContext)
    {
        AntlrService service = this.compilerState.getCompilerWalk().getService();
        if (service.getServiceMultiplicity() != null)
        {
            return;
        }
        String sourceCodeText = "            multiplicity: one;\n";
        this.runCompilerMacro(inPlaceContext, sourceCodeText);
    }

    private void runCompilerMacro(ServiceBodyContext inPlaceContext, @Nonnull String sourceCodeText)
    {
        AntlrService      service       = this.compilerState.getCompilerWalk().getService();
        ParseTreeListener compilerPhase = new ServiceMultiplicityPhase(this.compilerState);

        this.compilerState.runInPlaceCompilerMacro(
                service,
                this,
                sourceCodeText,
                KlassParser::serviceMultiplicityDeclaration,
                inPlaceContext,
                compilerPhase);
    }
}
