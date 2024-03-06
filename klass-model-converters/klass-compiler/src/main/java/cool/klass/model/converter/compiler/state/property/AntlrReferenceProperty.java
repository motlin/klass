package cool.klass.model.converter.compiler.state.property;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.property.ReferencePropertyImpl.ReferencePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrReferenceProperty<Type extends AntlrClassifier>
        extends AntlrProperty
        implements AntlrOrderByOwner
{
    @Nullable
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

    @Nullable
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

    @Nonnull
    protected Optional<AntlrOrderBy> orderByState = Optional.empty();

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

    public abstract AntlrMultiplicity getMultiplicity();

    @Nonnull
    @Override
    public abstract ReferencePropertyBuilder<?, ?, ?> build();

    @Nonnull
    @Override
    public abstract ReferencePropertyBuilder<?, ?, ?> getElementBuilder();

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
