package cool.klass.model.converter.compiler.state.service;

import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrPackageableElement;
import cool.klass.model.converter.compiler.state.AntlrTopLevelElement;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.domain.service.ServiceGroupImpl.ServiceGroupBuilder;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.bag.strategy.mutable.HashBagWithHashingStrategy;
import org.eclipse.collections.impl.block.factory.HashingStrategies;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrServiceGroup extends AntlrPackageableElement implements AntlrTopLevelElement
{
    @Nonnull
    public static final AntlrServiceGroup AMBIGUOUS = new AntlrServiceGroup(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous service group",
            -1,
            null,
            AntlrClass.AMBIGUOUS);

    @Nonnull
    private final AntlrClass klass;

    private final MutableList<AntlrUrl>                              urlStates     = Lists.mutable.empty();
    private final MutableOrderedMap<UrlDeclarationContext, AntlrUrl> urlsByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private ServiceGroupBuilder serviceGroupBuilder;

    public AntlrServiceGroup(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            String packageName,
            @Nonnull AntlrClass klass)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal, packageName);
        this.klass = Objects.requireNonNull(klass);
    }

    @Nonnull
    public AntlrClass getKlass()
    {
        return this.klass;
    }

    public AntlrUrl getUrlByContext(UrlDeclarationContext ctx)
    {
        return this.urlsByContext.get(ctx);
    }

    public void enterUrlDeclaration(@Nonnull AntlrUrl urlState)
    {
        AntlrUrl duplicate = this.urlsByContext.put(
                urlState.getElementContext(),
                urlState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }

        this.urlStates.add(urlState);
    }

    @Nonnull
    @Override
    public ServiceGroupDeclarationContext getElementContext()
    {
        return (ServiceGroupDeclarationContext) super.getElementContext();
    }

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        this.reportNoUrls(compilerErrorHolder);
        this.reportDuplicateUrls(compilerErrorHolder);

        if (this.klass == AntlrClass.NOT_FOUND)
        {
            this.reportTypeNotFound(compilerErrorHolder);
            return;
        }

        for (AntlrUrl urlState : this.urlStates)
        {
            urlState.reportErrors(compilerErrorHolder);
        }
        // TODO: Not here, but report if there are more than one service group for a class.
    }

    private void reportTypeNotFound(CompilerErrorHolder compilerErrorHolder)
    {
        if (this.klass != AntlrClass.NOT_FOUND)
        {
            return;
        }

        ClassReferenceContext reference = this.getElementContext().classReference();
        compilerErrorHolder.add(
                String.format("ERR_SRG_TYP: Cannot find class '%s'", reference.getText()),
                this,
                reference);
    }

    private void reportDuplicateUrls(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: reportDuplicateUrls
        HashBagWithHashingStrategy<AntlrUrl> antlrUrls =
                new HashBagWithHashingStrategy<>(HashingStrategies.fromFunction(AntlrUrl::getNormalizedPathSegments));

        MutableBag<AntlrUrl> duplicateUrls = antlrUrls.selectByOccurrences(occurrences -> occurrences > 1);
        if (duplicateUrls.notEmpty())
        {
            throw new AssertionError();
        }
    }

    private void reportNoUrls(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        if (this.urlStates.isEmpty())
        {
            String message = String.format(
                    "ERR_SER_EMP: Service group should declare at least one url: '%s'.",
                    this.getElementContext().classReference().getText());

            compilerErrorHolder.add(message, this);
        }
    }

    public void reportDuplicateServiceGroupClass(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format(
                "ERR_DUP_SVC: Multiple service groups for class: '%s.%s'.",
                this.klass.getPackageName(),
                this.klass.getName());
        compilerErrorHolder.add(message, this);
    }

    public ServiceGroupBuilder build()
    {
        if (this.serviceGroupBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.serviceGroupBuilder = new ServiceGroupBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.packageName,
                this.klass.getElementBuilder());

        ImmutableList<UrlBuilder> urlBuilders = this.urlStates
                .collect(AntlrUrl::build)
                .toImmutable();

        this.serviceGroupBuilder.setUrlBuilders(urlBuilders);
        return this.serviceGroupBuilder;
    }

    @Override
    public ServiceGroupBuilder getElementBuilder()
    {
        return this.serviceGroupBuilder;
    }
}
