package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.property.AbstractProperty.PropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public abstract class AntlrProperty
        extends AntlrNamedElement
{
    @Nonnull
    private final MutableList<AntlrModifier>                          modifierStates     = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrModifier>            modifiersByName    =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ParserRuleContext, AntlrModifier> modifiersByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected AntlrProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            int ordinal)
    {
        super(elementContext, compilationUnit, nameContext, ordinal);
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return this.getEntireContext();
    }

    @Nonnull
    public abstract AntlrType getType();

    @Nonnull
    public abstract PropertyBuilder<?, ?, ?> build();

    @Override
    @Nonnull
    public abstract PropertyBuilder<?, ?, ?> getElementBuilder();

    @Nonnull
    public abstract AntlrClassifier getOwningClassifierState();

    public boolean isCreatedBy()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isCreatedBy);
    }

    public boolean isLastUpdatedBy()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isLastUpdatedBy);
    }

    public int getNumModifiers()
    {
        return this.modifierStates.size();
    }

    @Nonnull
    public ListIterable<AntlrModifier> getModifiers()
    {
        return this.modifierStates.asUnmodifiable();
    }

    public void enterModifier(@Nonnull AntlrModifier modifierState)
    {
        Objects.requireNonNull(modifierState);
        this.modifierStates.add(modifierState);
        this.modifiersByName.compute(
                modifierState.getName(),
                (name, builder) -> builder == null
                        ? modifierState
                        : AntlrModifier.AMBIGUOUS);

        AntlrModifier duplicate = this.modifiersByContext.put(
                modifierState.getElementContext(),
                modifierState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    @OverridingMethodsMustInvokeSuper
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        this.reportDuplicateModifiers(compilerErrorHolder);
    }

    private void reportDuplicateModifiers(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        MutableBag<String> duplicateModifiers = this.getModifiers()
                .asLazy()
                .collect(AntlrNamedElement::getName)
                .toBag()
                .selectDuplicates();

        for (AntlrModifier modifier : this.getModifiers())
        {
            if (duplicateModifiers.contains(modifier.getName()))
            {
                String message = String.format(
                        "Duplicate modifier '%s'.",
                        modifier.getName());
                compilerErrorHolder.add("ERR_DUP_MOD", message, modifier);
            }
        }
    }

    public void reportDuplicateUserProperty(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format(
                "Duplicate userId property: '%s.%s'.",
                this.getOwningClassifierState().getName(),
                this.getName());

        compilerErrorHolder.add("ERR_DUP_UID", message, this);
    }

    public final void reportDuplicateMemberName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format(
                "Duplicate member: '%s.%s'.",
                this.getOwningClassifierState().getName(),
                this.getName());

        compilerErrorHolder.add("ERR_DUP_PRP", message, this);
    }

    @OverridingMethodsMustInvokeSuper
    public void reportAuditErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        this.modifierStates.each(each -> each.reportAuditErrors(compilerErrorHolder));
    }

    @Override
    protected Pattern getNamePattern()
    {
        return MEMBER_NAME_PATTERN;
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s.%s: %s %s",
                this.getOwningClassifierState().getName(),
                this.getName(),
                this.getType().getName(),
                this.getModifiers().collect(AntlrNamedElement::getName).makeString(" "));
    }

    @Override
    public boolean isContext()
    {
        return true;
    }
}
