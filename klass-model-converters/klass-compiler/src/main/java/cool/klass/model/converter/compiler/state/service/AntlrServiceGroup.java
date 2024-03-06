package cool.klass.model.converter.compiler.state.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrPackageableElement;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.domain.service.ServiceGroup.ServiceGroupBuilder;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.bag.strategy.mutable.HashBagWithHashingStrategy;
import org.eclipse.collections.impl.block.factory.HashingStrategies;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrServiceGroup extends AntlrPackageableElement
{
    @Nonnull
    public static final AntlrServiceGroup AMBIGUOUS = new AntlrServiceGroup(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous service group",
            null,
            AntlrClass.AMBIGUOUS);

    @Nonnull
    private final AntlrClass klass;

    private final MutableList<AntlrUrl> urls = Lists.mutable.empty();

    private ServiceGroupBuilder serviceGroupBuilder;

    public AntlrServiceGroup(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            String packageName,
            @Nonnull AntlrClass klass)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, packageName);
        this.klass = Objects.requireNonNull(klass);
    }

    @Nonnull
    public AntlrClass getKlass()
    {
        return this.klass;
    }

    public void enterUrlDeclaration(@Nonnull AntlrUrl url)
    {
        this.urls.add(url);
    }

    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.getElementContext());
    }

    @Nonnull
    @Override
    public ServiceGroupDeclarationContext getElementContext()
    {
        return (ServiceGroupDeclarationContext) super.getElementContext();
    }

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        this.reportDuplicateUrls(compilerErrorHolder);
        this.reportNoUrls(compilerErrorHolder);

        for (AntlrUrl url : this.urls)
        {
            url.reportErrors(compilerErrorHolder);
        }

        // TODO: Not here, but report if there are more than one service group for a class.
    }

    private void reportDuplicateUrls(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: reportDuplicateUrls
        HashBagWithHashingStrategy<AntlrUrl> antlrUrls = new HashBagWithHashingStrategy<>(HashingStrategies.fromFunction(AntlrUrl::getNormalizedPathSegments));

        MutableBag<AntlrUrl> duplicateUrls = antlrUrls.selectByOccurrences(occurrences -> occurrences > 1);
        if (duplicateUrls.notEmpty())
        {
            throw new AssertionError();
        }
    }

    private void reportNoUrls(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        if (this.urls.isEmpty())
        {
            String message = String.format(
                    "ERR_SER_EMP: Service group should declare at least one url: '%s'.",
                    this.getElementContext().classReference().getText());

            compilerErrorHolder.add(this.compilationUnit, message, this.getElementContext());
        }
    }

    public void reportDuplicateServiceGroupClass(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format(
                "ERR_DUP_SVC: Multiple service groups for class: '%s.%s'.",
                this.klass.getPackageName(),
                this.klass.getName());
        compilerErrorHolder.add(this.compilationUnit, message, this.nameContext);
    }

    public ServiceGroupBuilder build()
    {
        if (this.serviceGroupBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.serviceGroupBuilder = new ServiceGroupBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                this.packageName,
                this.klass.getKlassBuilder());

        ImmutableList<UrlBuilder> urlBuilders = this.urls
                .collect(AntlrUrl::build)
                .toImmutable();

        this.serviceGroupBuilder.setUrlBuilders(urlBuilders);
        return this.serviceGroupBuilder;
    }

    public ServiceGroupBuilder getServiceGroupBuilder()
    {
        return this.serviceGroupBuilder;
    }
}
