package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.meta.domain.EnumerationImpl.EnumerationBuilder;
import cool.klass.model.meta.domain.EnumerationLiteralImpl.EnumerationLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrEnumeration
        extends AntlrPackageableElement
        implements AntlrType, AntlrTopLevelElement
{
    @Nonnull
    public static final AntlrEnumeration AMBIGUOUS = new AntlrEnumeration(
            new EnumerationDeclarationContext(null, -1),
            Optional.empty(),
            new ParserRuleContext(),
            -1,
            AntlrCompilationUnit.AMBIGUOUS,
            new ParserRuleContext(),
            "klass.meta");

    @Nonnull
    public static final AntlrEnumeration NOT_FOUND = new AntlrEnumeration(
            new EnumerationDeclarationContext(null, -1),
            Optional.empty(),
            new ParserRuleContext(),
            -1,
            AntlrCompilationUnit.NOT_FOUND,
            new ParserRuleContext(),
            "klass.meta");

    private final MutableList<AntlrEnumerationLiteral>               enumerationLiteralStates  = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrEnumerationLiteral> enumerationLiteralsByName =
            OrderedMapAdapter.adapt(
                    new LinkedHashMap<>());

    private EnumerationBuilder enumerationBuilder;

    public AntlrEnumeration(
            @Nonnull EnumerationDeclarationContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            int ordinal,
            @Nonnull AntlrCompilationUnit compilationUnitState,
            @Nonnull ParserRuleContext packageContext,
            @Nonnull String packageName)
    {
        super(elementContext, compilationUnit, nameContext, ordinal, compilationUnitState, packageContext, packageName);
    }

    public int getNumLiterals()
    {
        return this.enumerationLiteralStates.size();
    }

    public void enterEnumerationLiteral(@Nonnull AntlrEnumerationLiteral enumerationLiteralState)
    {
        this.enumerationLiteralStates.add(enumerationLiteralState);
        this.enumerationLiteralsByName.compute(
                enumerationLiteralState.getName(),
                (name, builder) -> builder == null
                        ? enumerationLiteralState
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
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.getElementContext().identifier(),
                this.ordinal,
                this.packageName);

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

    @Nonnull
    @Override
    public EnumerationBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.enumerationBuilder);
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportNameErrors(compilerErrorHolder);
        this.enumerationLiteralStates.forEachWith(AntlrEnumerationLiteral::reportNameErrors, compilerErrorHolder);
    }

    public void reportErrors(CompilerErrorState compilerErrorHolder)
    {
        this.logDuplicateLiteralNames(compilerErrorHolder);
        this.logDuplicatePrettyNames(compilerErrorHolder);
    }

    public void logDuplicateLiteralNames(CompilerErrorState compilerErrorHolder)
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

    public void logDuplicatePrettyNames(CompilerErrorState compilerErrorHolder)
    {
        MutableBag<String> duplicatePrettyNames = this.enumerationLiteralStates
                .collect(AntlrEnumerationLiteral::getPrettyName)
                .select(Optional::isPresent)
                .collect(Optional::get)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1);

        this.enumerationLiteralStates
                .asLazy()
                .select(each -> each.getPrettyName().isPresent())
                .select(each -> duplicatePrettyNames.contains(each.getPrettyName().get()))
                .forEachWith(AntlrEnumerationLiteral::reportDuplicatePrettyName, compilerErrorHolder);
    }

    @Override
    public String toString()
    {
        return String.format("%s.%s", this.packageName, this.getName());
    }

    @Override
    public EnumerationBuilder getTypeGetter()
    {
        return Objects.requireNonNull(this.enumerationBuilder);
    }
}
