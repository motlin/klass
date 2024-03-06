package cool.klass.model.converter.compiler.state.service;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrCompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrPackageableElement;
import cool.klass.model.converter.compiler.state.AntlrTopLevelElement;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.domain.service.ServiceGroupImpl.ServiceGroupBuilder;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
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

public class AntlrServiceGroup
        extends AntlrPackageableElement
        implements AntlrTopLevelElement
{
    @Nonnull
    public static final AntlrServiceGroup AMBIGUOUS = new AntlrServiceGroup(
            new ServiceGroupDeclarationContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrCompilationUnit.AMBIGUOUS,
            AntlrClass.AMBIGUOUS);

    @Nonnull
    private final AntlrClass klass;

    private final MutableList<AntlrUrl>                              urlStates     = Lists.mutable.empty();
    private final MutableOrderedMap<UrlDeclarationContext, AntlrUrl> urlsByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private ServiceGroupBuilder serviceGroupBuilder;

    public AntlrServiceGroup(
            @Nonnull ServiceGroupDeclarationContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrCompilationUnit compilationUnitState,
            @Nonnull AntlrClass klass)
    {
        super(elementContext, compilationUnit, ordinal, nameContext, compilationUnitState);
        this.klass = Objects.requireNonNull(klass);
    }

    // TODO: Should this be a Classifier rather than a Class?
    // TODO: If so, there should also be an error for PUT/POST on an interface
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

    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        this.reportNoUrls(compilerErrorHolder);
        this.reportDuplicateUrls(compilerErrorHolder);
        this.reportForwardReference(compilerErrorHolder);

        for (AntlrUrl urlState : this.urlStates)
        {
            urlState.reportErrors(compilerErrorHolder);
        }

        // TODO: Not here, but report if there are more than one service group for a class.

        if (this.klass == AntlrClass.NOT_FOUND)
        {
            this.reportTypeNotFound(compilerErrorHolder);
        }
    }

    private void reportTypeNotFound(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.klass != AntlrClass.NOT_FOUND)
        {
            return;
        }

        ClassReferenceContext reference = this.getElementContext().classReference();
        compilerErrorHolder.add(
                "ERR_SRG_TYP",
                String.format("Cannot find class '%s'", reference.getText()),
                this,
                reference);
    }

    private void reportDuplicateUrls(CompilerErrorState compilerErrorHolder)
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

    private void reportNoUrls(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.urlStates.isEmpty())
        {
            String message = String.format(
                    "Service group should declare at least one url: '%s'.",
                    this.getElementContext().classReference().getText());

            compilerErrorHolder.add("ERR_SER_EMP", message, this);
        }
    }

    public void reportDuplicateServiceGroupClass(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format(
                "Multiple service groups for class: '%s.%s'.",
                this.klass.getPackageName(),
                this.klass.getName());
        compilerErrorHolder.add("ERR_DUP_SVC", message, this);
    }

    private void reportForwardReference(CompilerErrorState compilerErrorHolder)
    {
        if (!this.isForwardReference(this.klass))
        {
            return;
        }

        String message = String.format(
                "Service group '%s' is declared on line %d and has a forward reference to class '%s' which is declared later in the source file '%s' on line %d.",
                this.toString(),
                this.getElementContext().getStart().getLine(),
                this.klass.getName(),
                this.getCompilationUnit().get().getSourceName(),
                this.klass.getElementContext().getStart().getLine());
        compilerErrorHolder.add(
                "ERR_FWD_REF",
                message,
                this,
                this.getElementContext().classReference());
    }

    public ServiceGroupBuilder build()
    {
        if (this.serviceGroupBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.serviceGroupBuilder = new ServiceGroupBuilder(
                (ServiceGroupDeclarationContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
                this.getPackageName(),
                this.klass.getElementBuilder());

        ImmutableList<UrlBuilder> urlBuilders = this.urlStates
                .collect(AntlrUrl::build)
                .toImmutable();

        this.serviceGroupBuilder.setUrlBuilders(urlBuilders);
        return this.serviceGroupBuilder;
    }

    @Override
    public ParserRuleContext getBodyContext()
    {
        return this.getElementContext().serviceGroupDeclarationBody();
    }

    @Nonnull
    @Override
    public ServiceGroupBuilder getElementBuilder()
    {
        return this.serviceGroupBuilder;
    }

    @Override
    public void reportDuplicateTopLevelName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        // Deliberately empty
    }
}
