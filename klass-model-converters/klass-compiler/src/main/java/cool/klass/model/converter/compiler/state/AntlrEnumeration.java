package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.EnumerationLiteral.EnumerationLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrEnumeration extends AntlrPackageableElement implements AntlrType
{
    @Nonnull
    public static final AntlrEnumeration AMBIGUOUS = new AntlrEnumeration(
            new EnumerationDeclarationContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous enumeration",
            -1, null);

    @Nonnull
    public static final AntlrEnumeration NOT_FOUND = new AntlrEnumeration(
            new EnumerationDeclarationContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "not found enumeration",
            -1,
            null);

    private final MutableList<AntlrEnumerationLiteral>               enumerationLiteralStates  = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrEnumerationLiteral> enumerationLiteralsByName = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());

    private EnumerationBuilder enumerationBuilder;

    public AntlrEnumeration(
            @Nonnull EnumerationDeclarationContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            String packageName)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal, packageName);
    }

    public int getNumLiterals()
    {
        return this.enumerationLiteralStates.size();
    }

    public void enterEnumerationLiteral(@Nonnull AntlrEnumerationLiteral antlrEnumerationLiteral)
    {
        this.enumerationLiteralStates.add(antlrEnumerationLiteral);
        this.enumerationLiteralsByName.compute(
                antlrEnumerationLiteral.getName(),
                (name, builder) -> builder == null
                        ? antlrEnumerationLiteral
                        : AntlrEnumerationLiteral.AMBIGUOUS);
    }

    public EnumerationBuilder build()
    {
        if (this.enumerationBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.enumerationBuilder = new EnumerationBuilder(
                this.getElementContext(),
                this.getElementContext().identifier(),
                this.name,
                ordinal, this.packageName);

        ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders = this.enumerationLiteralStates
                .collect(AntlrEnumerationLiteral::build)
                .toImmutable();

        this.enumerationBuilder.setEnumerationLiteralBuilders(enumerationLiteralBuilders);
        return this.enumerationBuilder;
    }

    @Nonnull
    @Override
    public EnumerationDeclarationContext getElementContext()
    {
        return (EnumerationDeclarationContext) super.getElementContext();
    }

    public EnumerationBuilder getEnumerationBuilder()
    {
        return Objects.requireNonNull(this.enumerationBuilder);
    }

    public void reportDuplicateTopLevelName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_TOP: Duplicate top level item name: '%s'.", this.name);
        compilerErrorHolder.add(message, this.nameContext);
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        this.reportKeywordCollision(compilerErrorHolder);

        for (AntlrEnumerationLiteral enumerationLiteralState : this.enumerationLiteralStates)
        {
            enumerationLiteralState.reportNameErrors(compilerErrorHolder);
        }

        if (!TYPE_NAME_PATTERN.matcher(this.name).matches())
        {
            String message = String.format(
                    "ERR_ENM_NME: Name must match pattern %s but was %s",
                    CONSTANT_NAME_PATTERN,
                    this.name);
            compilerErrorHolder.add(
                    message,
                    this.nameContext);
        }
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        this.logDuplicateLiteralNames(compilerErrorHolder);
        this.logDuplicatePrettyNames(compilerErrorHolder);
    }

    public void logDuplicateLiteralNames(CompilerErrorHolder compilerErrorHolder)
    {
        MutableBag<String> duplicateNames = this.enumerationLiteralStates
                .collect(AntlrNamedElement::getName)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1);

        this.enumerationLiteralStates
                .asLazy()
                .select(enumerationLiteral -> duplicateNames.contains(enumerationLiteral.getName()))
                .forEachWith(AntlrEnumerationLiteral::reportDuplicateName, compilerErrorHolder);
    }

    public void logDuplicatePrettyNames(CompilerErrorHolder compilerErrorHolder)
    {
        MutableBag<String> duplicatePrettyNames = this.enumerationLiteralStates
                .collect(AntlrEnumerationLiteral::getPrettyName)
                .reject(Objects::isNull)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1);

        this.enumerationLiteralStates
                .asLazy()
                .select(each -> duplicatePrettyNames.contains(each.getPrettyName()))
                .forEachWith(AntlrEnumerationLiteral::reportDuplicatePrettyName, compilerErrorHolder);
    }

    @Override
    public String toString()
    {
        return String.format("%s.%s", this.packageName, this.name);
    }

    @Override
    public EnumerationBuilder getTypeBuilder()
    {
        return Objects.requireNonNull(this.enumerationBuilder);
    }
}
