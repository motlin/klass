package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.property.ModifierImpl.ModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

// TODO: Specific subclasses for the specific antlr context types
public class AntlrModifier
        extends AntlrNamedElement
{
    public static final AntlrModifier NOT_FOUND = new AntlrModifier(
            new ParserRuleContext(null, -1),
            Optional.empty(),
            -1,
            new ParserRuleContext(),
            AntlrClassifier.NOT_FOUND);

    public static final AntlrModifier AMBIGUOUS = new AntlrModifier(
            new ParserRuleContext(null, -1),
            Optional.empty(),
            -1,
            new ParserRuleContext(),
            AntlrClassifier.AMBIGUOUS);

    public static final ImmutableList<String> AUDIT_PROPERTY_NAMES = Lists.immutable.with(
            "createdBy",
            "createdOn",
            "lastUpdatedBy");

    private final AntlrNamedElement surroundingElement;
    private       ModifierBuilder   elementBuilder;

    public AntlrModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull AntlrNamedElement surroundingElement)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
        this.surroundingElement = surroundingElement;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.surroundingElement);
    }

    @Nonnull
    @Override
    public Pattern getNamePattern()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getNamePattern() not implemented yet");
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        // intentionally blank
    }

    public boolean is(String name)
    {
        return this.getName().equals(name);
    }

    public boolean isAudit()
    {
        return this.is("audited") || AUDIT_PROPERTY_NAMES.contains(this.getName());
    }

    public boolean isCreatedBy()
    {
        return this.is("createdBy");
    }

    public boolean isCreatedOn()
    {
        return this.is("createdOn");
    }

    public boolean isLastUpdatedBy()
    {
        return this.is("lastUpdatedBy");
    }

    public boolean isUser()
    {
        return this.is("userId");
    }

    public boolean isPrivate()
    {
        return this.is("private");
    }

    public boolean isSystem()
    {
        return this.is("system");
    }

    public boolean isValid()
    {
        return this.is("valid");
    }

    public boolean isFrom()
    {
        return this.is("from");
    }

    public boolean isTo()
    {
        return this.is("to");
    }

    public boolean isVersion()
    {
        return this.is("version");
    }

    public boolean isUserId()
    {
        return this.is("userId");
    }

    public boolean isId()
    {
        return this.is("id");
    }

    public boolean isKey()
    {
        return this.is("key");
    }

    public boolean isDerived()
    {
        return this.is("derived");
    }

    public boolean isOwned()
    {
        return this.is("owned");
    }

    public boolean isTransient()
    {
        return this.getName().equals("transient");
    }

    @Nonnull
    public ModifierBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new ModifierBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
                this.surroundingElement.getElementBuilder());
        return this.elementBuilder;
    }

    @Override
    @Nonnull
    public ModifierBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
