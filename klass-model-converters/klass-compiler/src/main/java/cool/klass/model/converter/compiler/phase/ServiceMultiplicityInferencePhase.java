package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
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
    public void enterServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        super.enterServiceDeclaration(ctx);

        AntlrService service = this.compilerState.getCompilerWalk().getService();
        if (service.getServiceMultiplicity() == null)
        {
            String sourceCodeText = "            multiplicity: one;\n";
            this.runCompilerMacro(sourceCodeText);
        }
    }

    private void runCompilerMacro(@Nonnull String sourceCodeText)
    {
        AntlrService      service       = this.compilerState.getCompilerWalk().getService();
        ParseTreeListener compilerPhase = new ServiceMultiplicityPhase(this.compilerState);

        this.compilerState.runNonRootCompilerMacro(
                service,
                this,
                sourceCodeText,
                KlassParser::serviceMultiplicityDeclaration,
                compilerPhase);
    }
}
