package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrMultiplicityOwner;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.property.ReferencePropertyImpl.ReferencePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;

public abstract class AntlrReferenceProperty<Type extends AntlrClassifier>
        extends AntlrProperty
        implements AntlrOrderByOwner, AntlrMultiplicityOwner
{
    //<editor-fold desc="AMBIGUOUS">
    public static final AntlrReferenceProperty AMBIGUOUS = new AntlrReferenceProperty(
            new ParserRuleContext(null, -1),
            Optional.empty(),
            AbstractElement.NO_CONTEXT,
            "ambiguous reference property",
            -1)
    {
        @Nonnull
        @Override
        public AntlrClassifier getType()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".getType() not implemented yet");
        }

        @Override
        public AntlrMultiplicity getMultiplicity()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getMultiplicity() not implemented yet");
        }

        @Nonnull
        @Override
        public ReferencePropertyBuilder<?, ?, ?> build()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".build() not implemented yet");
        }

        @Nonnull
        @Override
        public ReferencePropertyBuilder<?, ?, ?> getElementBuilder()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getElementBuilder() not implemented yet");
        }

        @Override
        protected IdentifierContext getTypeIdentifier()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getTypeIdentifier() not implemented yet");
        }

        @Nonnull
        @Override
        public AntlrClassifier getOwningClassifierState()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getOwningClassifierState() not implemented yet");
        }

        @Nonnull
        @Override
        public Optional<IAntlrElement> getSurroundingElement()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getSurroundingElement() not implemented yet");
        }
    };
    //</editor-fold>

    //<editor-fold desc="NOT_FOUND">
    public static final AntlrReferenceProperty NOT_FOUND = new AntlrReferenceProperty(
            new ParserRuleContext(null, -1),
            Optional.empty(),
            AbstractElement.NO_CONTEXT,
            "not found reference property",
            -1)
    {
        @Nonnull
        @Override
        public Optional<IAntlrElement> getSurroundingElement()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getSurroundingElement() not implemented yet");
        }

        @Nonnull
        @Override
        public AntlrClassifier getType()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".getType() not implemented yet");
        }

        @Override
        public AntlrMultiplicity getMultiplicity()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getMultiplicity() not implemented yet");
        }

        @Nonnull
        @Override
        public ReferencePropertyBuilder<?, ?, ?> build()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".build() not implemented yet");
        }

        @Nonnull
        @Override
        public ReferencePropertyBuilder<?, ?, ?> getElementBuilder()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getElementBuilder() not implemented yet");
        }

        @Nonnull
        @Override
        public AntlrClassifier getOwningClassifierState()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getOwningClassifierState() not implemented yet");
        }

        @Override
        protected IdentifierContext getTypeIdentifier()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getTypeIdentifier() not implemented yet");
        }
    };
    //</editor-fold>

    @Nonnull
    protected Optional<AntlrOrderBy> orderByState = Optional.empty();

    protected AntlrMultiplicity multiplicityState;

    protected AntlrReferenceProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
    }

    @Override
    @Nonnull
    public abstract Type getType();

    @Nonnull
    @Override
    public abstract ReferencePropertyBuilder<?, ?, ?> build();

    public AntlrMultiplicity getMultiplicity()
    {
        return this.multiplicityState;
    }

    public boolean isToOne()
    {
        return this.multiplicityState != null && this.multiplicityState.isToOne();
    }

    public boolean isToMany()
    {
        return this.multiplicityState != null && this.multiplicityState.isToMany();
    }

    public boolean isToOneOptional()
    {
        return this.multiplicityState != null && this.multiplicityState.getMultiplicity() == Multiplicity.ZERO_TO_ONE;
    }

    public boolean isToOneRequired()
    {
        return this.multiplicityState != null && this.multiplicityState.getMultiplicity() == Multiplicity.ONE_TO_ONE;
    }

    public boolean isOwned()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isOwned);
    }

    public boolean isVersion()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isVersion);
    }

    public boolean isAudit()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isAudit);
    }

    protected void reportInvalidMultiplicity(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.multiplicityState.getMultiplicity() == null)
        {
            String multiplicityChoices = ArrayAdapter.adapt(Multiplicity.values())
                    .collect(Multiplicity::getPrettyName)
                    .collect(each -> '[' + each + ']')
                    .makeString();

            String message = String.format(
                    "Reference property '%s: %s[%s..%s]' has invalid multiplicity. Expected one of %s.",
                    this.getName(),
                    this.getOwningClassifierState().getName(),
                    this.multiplicityState.getLowerBoundText(),
                    this.multiplicityState.getUpperBoundText(),
                    multiplicityChoices);

            compilerErrorHolder.add("ERR_ASO_MUL", message, this.multiplicityState);
        }
    }

    @Nonnull
    @Override
    public abstract ReferencePropertyBuilder<?, ?, ?> getElementBuilder();

    @Override
    public void enterMultiplicity(@Nonnull AntlrMultiplicity multiplicityState)
    {
        if (this.multiplicityState != null)
        {
            throw new AssertionError();
        }

        this.multiplicityState = Objects.requireNonNull(multiplicityState);
    }

    @Override
    public void enterOrderByDeclaration(@Nonnull AntlrOrderBy orderByState)
    {
        if (this.orderByState.isPresent())
        {
            throw new IllegalStateException();
        }
        this.orderByState = Optional.of(orderByState);
    }

    @Override
    @Nonnull
    public Optional<AntlrOrderBy> getOrderByState()
    {
        return this.orderByState;
    }

    public void reportTypeNotFound(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.getType() != AntlrClass.NOT_FOUND)
        {
            return;
        }

        IdentifierContext offendingToken = this.getTypeIdentifier();
        String message = String.format(
                "Cannot find class '%s'.",
                offendingToken.getText());
        compilerErrorHolder.add("ERR_PRP_TYP", message, this, offendingToken);
    }

    protected abstract IdentifierContext getTypeIdentifier();
}
