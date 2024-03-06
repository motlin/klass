package cool.klass.model.converter.compiler.state.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.meta.domain.service.url.UrlQueryParameter.UrlQueryParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrUrlQueryParameter extends AntlrUrlParameter
{
    public AntlrUrlQueryParameter(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull AntlrMultiplicity multiplicityState,
            @Nonnull AntlrUrl urlState)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, multiplicityState, urlState);
    }

    @Override
    public abstract UrlQueryParameterBuilder build();
}
