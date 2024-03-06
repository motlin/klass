package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;

import cool.klass.model.converter.compiler.phase.AbstractCompilerPhase;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.EnumerationLiteral.EnumerationLiteralBuilder;
import cool.klass.model.meta.domain.NamedElement.NamedElementBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrEnumeration
{
    public static final AntlrEnumeration AMBIGUOUS = new AntlrEnumeration(
            null,
            new EnumerationDeclarationContext(null, -1),
            "ambiguous enumeration");
    public static final AntlrEnumeration NOT_FOUND = new AntlrEnumeration(
            null,
            new EnumerationDeclarationContext(null, -1),
            "not found enumeration");

    private final String                        packageName;
    private final EnumerationDeclarationContext context;
    private final String                        name;

    private final MutableList<EnumerationLiteralBuilder>               enumerationLiteralBuilders = Lists.mutable.empty();
    private final MutableOrderedMap<String, EnumerationLiteralBuilder> enumerationLiteralsByName  = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());

    private EnumerationBuilder enumerationBuilder;

    public AntlrEnumeration(String packageName, EnumerationDeclarationContext context, String name)
    {
        this.packageName = packageName;
        this.context = Objects.requireNonNull(context);
        this.name = Objects.requireNonNull(name);
    }

    public String getName()
    {
        return this.name;
    }

    public void enterEnumerationLiteral(EnumerationLiteralBuilder enumerationLiteralBuilder)
    {
        this.enumerationLiteralBuilders.add(enumerationLiteralBuilder);
        this.enumerationLiteralsByName.compute(
                enumerationLiteralBuilder.getName(),
                (name, builder) -> builder == null
                        ? enumerationLiteralBuilder
                        : EnumerationLiteralBuilder.AMBIGUOUS);
    }

    public void exitEnumerationDeclaration(AbstractCompilerPhase compilerPhase)
    {
        // TODO: Move these checks into reportErrors for consistency
        MutableBag<String> duplicateNames = this.getDuplicateLiteralNames();
        this.logDuplicateLiteralNames(compilerPhase, duplicateNames);
        this.logDuplicatePrettyNames(compilerPhase);
    }

    private MutableBag<String> getDuplicateLiteralNames()
    {
        return this.enumerationLiteralBuilders
                .collect(NamedElementBuilder::getName)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1);
    }

    private void logDuplicateLiteralNames(
            AbstractCompilerPhase abstractCompilerPhase,
            MutableBag<String> duplicateNames)
    {
        this.enumerationLiteralBuilders
                .asLazy()
                .select(each -> duplicateNames.contains(each.getName()))
                .each(each ->
                {
                    String message = String.format("Duplicate enumeration literal: '%s'.", each.getName());
                    abstractCompilerPhase.error(message, each.getElementContext(), this.context);
                });
    }

    private void logDuplicatePrettyNames(AbstractCompilerPhase compilerPhase)
    {
        MutableBag<String> duplicatePrettyNames = this.enumerationLiteralBuilders
                .collect(EnumerationLiteralBuilder::getPrettyName)
                .reject(Objects::isNull)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1);
        this.enumerationLiteralBuilders
                .asLazy()
                .select(each -> duplicatePrettyNames.contains(each.getPrettyName()))
                .each(each ->
                {
                    String message = String.format(
                            "Duplicate enumeration pretty name: '%s'.",
                            each.getPrettyName());
                    EnumerationLiteralContext enumerationLiteralContext = (EnumerationLiteralContext) each.getElementContext();
                    compilerPhase.error(message, enumerationLiteralContext.enumerationPrettyName(), this.context);
                });
    }

    public EnumerationBuilder build()
    {
        if (this.enumerationBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.enumerationBuilder = new EnumerationBuilder(
                this.context,
                this.context.identifier(),
                this.name,
                this.packageName,
                this.enumerationLiteralBuilders.toImmutable());
        return this.enumerationBuilder;
    }

    public EnumerationBuilder getEnumerationBuilder()
    {
        return this.enumerationBuilder;
    }

    public EnumerationDeclarationContext getContext()
    {
        return this.context;
    }
}
