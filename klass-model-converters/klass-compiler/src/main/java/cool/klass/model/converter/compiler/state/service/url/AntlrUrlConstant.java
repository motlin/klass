package cool.klass.model.converter.compiler.state.service.url;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
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
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
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
        return new UrlConstantBuilder(this.elementContext, this.nameContext, this.name, ordinal);
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: URLs can contain almost anything. The parser is probably already more strict than any error checking that needs to happen here.
        // https://stackoverflow.com/questions/7109143/what-characters-are-valid-in-a-url
    }
}
