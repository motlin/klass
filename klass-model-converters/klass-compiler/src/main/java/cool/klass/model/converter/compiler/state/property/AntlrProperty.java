package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.AnnotationSeverity;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrIdentifierElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.property.AbstractProperty.PropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public abstract class AntlrProperty
        extends AntlrIdentifierElement
{
    @Nonnull
    private final MutableList<AntlrModifier>                            modifierStates     = Lists.mutable.empty();
    private final MutableOrderedMap<String, MutableList<AntlrModifier>> modifiersByName    =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ParserRuleContext, AntlrModifier>   modifiersByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected AntlrProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
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

    public boolean isVersion()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isVersion);
    }

    public boolean isPrivate()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isPrivate);
    }

    public boolean isCreatedBy()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isCreatedBy);
    }

    public boolean isFinal()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isFinal);
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
        this.modifiersByName.getIfAbsentPut(modifierState.getKeyword(), Lists.mutable::empty).add(modifierState);

        AntlrModifier duplicate = this.modifiersByContext.put(
                modifierState.getElementContext(),
                modifierState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public ImmutableList<AntlrModifier> getModifiersByName(String modifierName)
    {
        MutableList<AntlrModifier> result = this.modifiersByName.get(modifierName);
        return result == null ? Lists.immutable.empty() : result.toImmutable();
    }

    //<editor-fold desc="Report Compiler Errors">
    @OverridingMethodsMustInvokeSuper
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        this.reportDuplicateModifiers(compilerAnnotationHolder);
        this.reportDuplicateAuditModifiers(compilerAnnotationHolder);
        this.reportInvalidAuditProperties(compilerAnnotationHolder);
    }

    private void reportDuplicateModifiers(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        MutableBag<String> duplicateModifiers = this.getModifiers()
                .asLazy()
                .collect(AntlrModifier::getKeyword)
                .toBag()
                .selectDuplicates();

        for (AntlrModifier modifier : this.getModifiers())
        {
            if (duplicateModifiers.contains(modifier.getKeyword()))
            {
                String message = String.format(
                        "Duplicate modifier '%s'.",
                        modifier.getKeyword());
                compilerAnnotationHolder.add("ERR_DUP_MOD", message, modifier);
            }
        }
    }

    protected void reportDuplicateAuditModifiers(CompilerAnnotationState compilerAnnotationHolder)
    {
        if (this.isCreatedBy() && this.isLastUpdatedBy())
        {
            ImmutableList<AntlrModifier> modifiers = this
                    .getModifiers()
                    .select(modifier -> modifier.isCreatedBy() || modifier.isLastUpdatedBy())
                    .toImmutable();
            ImmutableList<ParserRuleContext> modifierContexts = modifiers
                    .collect(AntlrElement::getElementContext);
            String message = "Property may not have both 'createdBy' and lastUpdatedBy' modifiers.";
            compilerAnnotationHolder.add(
                    "ERR_CBY_LBY",
                    message,
                    this,
                    modifierContexts);
        }
    }

    @OverridingMethodsMustInvokeSuper
    protected void reportInvalidAuditProperties(CompilerAnnotationState compilerAnnotationHolder)
    {
        if (this.isCreatedBy() && this.isLastUpdatedBy())
        {
            return;
        }

        if (this.isCreatedBy() && !this.isFinal())
        {
            AntlrModifier modifier = this
                    .getModifiers()
                    .detect(AntlrModifier::isCreatedBy);

            String message = String.format(
                    "Expected createdBy property '%s' to be final.",
                    this);
            compilerAnnotationHolder.add(
                    "ERR_CRT_FIN",
                    message,
                    this,
                    Lists.immutable.with(modifier.getElementContext()));
        }

        if (this.isLastUpdatedBy() && this.isFinal())
        {
            ImmutableList<ParserRuleContext> parserRuleContexts = this
                    .getModifiers()
                    .select(antlrModifier -> antlrModifier.isLastUpdatedBy() || antlrModifier.isFinal())
                    .collect(AntlrElement::getElementContext)
                    .toImmutable();
            String message = String.format(
                    "Expected lastUpdatedBy property '%s' to not be final.",
                    this);
            compilerAnnotationHolder.add(
                    "ERR_LUB_NFI",
                    message,
                    this,
                    parserRuleContexts);
        }
    }

    public final void reportDuplicateMemberName(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        String message = String.format(
                "Duplicate member: '%s.%s'.",
                this.getOwningClassifierState().getName(),
                this.getName());

        compilerAnnotationHolder.add("ERR_DUP_PRP", message, this);
    }

    @OverridingMethodsMustInvokeSuper
    public void reportAuditErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        this.reportAuditErrors(compilerAnnotationHolder, this.modifierStates, this);
    }

    public void reportDuplicatePropertyWithModifiers(
            @Nonnull CompilerAnnotationState compilerAnnotationHolder,
            ImmutableList<String> modifierStrings)
    {
        ImmutableList<AntlrModifier> modifierStates = modifierStrings.flatCollect(this::getModifiersByName);

        String message = String.format(
                "Multiple properties on '%s' with modifiers %s.",
                this.getOwningClassifierState().getName(),
                modifierStrings);

        compilerAnnotationHolder.add(
                "ERR_PRP_MOD",
                message,
                this,
                modifierStates.collect(AntlrElement::getElementContext));
    }
    //</editor-fold>

    public void reportUnreferencedPrivateProperty(CompilerAnnotationState compilerAnnotationState)
    {
        String message = String.format(
                "Private property '%s.%s' is not referenced in any criteria.",
                this.getOwningClassifierState().getName(),
                this.getName());
        // TODO: 💡 Some name errors should really just be warnings. Rename CompilerError to CompilerAnnotation and implement severity.
        compilerAnnotationState.add("ERR_PRP_REF", message, this, AnnotationSeverity.WARNING);
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
                "%s.%s",
                this.getOwningClassifierState().getName(),
                this.getShortString());
    }

    public String getShortString()
    {
        MutableList<String> sourceCodeStrings = org.eclipse.collections.api.factory.Lists.mutable.empty();

        String typeSourceCode = this.getType().getName();
        sourceCodeStrings.add(typeSourceCode);

        this
                .getModifiers()
                .asLazy()
                .collect(AntlrElement::toString)
                .into(sourceCodeStrings);

        return String.format(
                "%s: %s",
                this.getName(),
                sourceCodeStrings.makeString(" "));
    }

    @Override
    public boolean isContext()
    {
        return true;
    }

    public abstract String getTypeName();
}
