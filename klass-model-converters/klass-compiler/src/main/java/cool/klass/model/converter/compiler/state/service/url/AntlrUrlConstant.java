package cool.klass.model.converter.compiler.state.service.url;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.meta.domain.service.url.UrlConstant.UrlConstantBuilder;
import cool.klass.model.meta.domain.service.url.UrlPathSegment.UrlPathSegmentBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrUrlConstant extends AntlrNamedElement implements AntlrUrlPathSegment
{
    public AntlrUrlConstant(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name);
    }

    @Nonnull
    @Override
    public Object toNormalized()
    {
        return this.name;
    }

    @Nonnull
    @Override
    public UrlPathSegmentBuilder build()
    {
        return new UrlConstantBuilder(this.elementContext, this.nameContext, this.name);
    }
}
