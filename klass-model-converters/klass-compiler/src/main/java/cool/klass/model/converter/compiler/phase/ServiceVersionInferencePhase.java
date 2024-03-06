package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassModifier;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;

public class ServiceVersionInferencePhase extends AbstractCompilerPhase
{
    public ServiceVersionInferencePhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public String getName()
    {
        return "Versioned url";
    }

    @Override
    public void exitUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        // TODO: ♻️ It no longer makes sense to share one url for a bunch of services, because of inference like this

        AntlrUrl urlState = this.compilerState.getCompilerWalkState().getUrlState();

        if (urlState
                .getServiceStates()
                .anySatisfy(AntlrService::needsVersionCriteria))
        {
            this.compilerState.runNonRootCompilerMacro(
                    this.getMacroElement(),
                    ctx,
                    this,
                    "?{version: Integer[0..1] version}",
                    KlassParser::queryParameterList,
                    new UrlParameterPhase(this.compilerState));
        }

        super.exitUrlDeclaration(ctx);
    }

    public AntlrElement getMacroElement()
    {
        AntlrClass         classState = this.compilerState.getCompilerWalkState().getServiceGroupState().getKlass();
        AntlrClassModifier versioned  = classState.getClassModifierByName("versioned");
        if (versioned != null)
        {
            return versioned;
        }

        // Detect one arbitrary version association end to use for the compiler error context.
        return classState
                .getAssociationEndStates()
                .detect(AntlrAssociationEnd::isVersion)
                .getAssociationEndModifierByName("version");
    }
}
