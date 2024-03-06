package cool.klass.model.converter.compiler.state.projection;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPrimitiveProperty;
import cool.klass.model.meta.domain.projection.ProjectionMember.ProjectionMemberBuilder;
import cool.klass.model.meta.domain.projection.ProjectionPrimitiveMember.ProjectionPrimitiveMemberBuilder;
import cool.klass.model.meta.grammar.KlassParser.HeaderContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrProjectionPrimitiveMember extends AntlrNamedElement implements AntlrProjectionMember
{
    @Nullable
    public static final AntlrProjectionPrimitiveMember AMBIGUOUS = new AntlrProjectionPrimitiveMember(
            new ParserRuleContext(),
            null,
            true,
            "ambiguous projection member",
            new ParserRuleContext(),
            AntlrProjection.AMBIGUOUS,
            new HeaderContext(null, -1),
            "ambiguous header",
            AntlrPrimitiveProperty.AMBIGUOUS);

    @Nonnull
    private final AntlrProjectionParent    antlrProjectionParent;
    @Nonnull
    private final HeaderContext            headerContext;
    @Nonnull
    private final String                   headerText;
    @Nonnull
    private final AntlrDataTypeProperty<?> dataTypeProperty;

    public AntlrProjectionPrimitiveMember(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull String name,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull AntlrProjectionParent antlrProjectionParent,
            @Nonnull HeaderContext headerContext,
            @Nonnull String headerText,
            @Nonnull AntlrDataTypeProperty<?> dataTypeProperty)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name);
        this.antlrProjectionParent = Objects.requireNonNull(antlrProjectionParent);
        this.headerText = Objects.requireNonNull(headerText);
        this.headerContext = Objects.requireNonNull(headerContext);
        this.dataTypeProperty = Objects.requireNonNull(dataTypeProperty);
    }

    @Nonnull
    @Override
    public ProjectionMemberBuilder build()
    {
        return new ProjectionPrimitiveMemberBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                this.headerContext,
                this.headerText,
                this.dataTypeProperty.getPropertyBuilder());
    }

    @Nonnull
    @Override
    public AntlrProjectionParent getParent()
    {
        return this.antlrProjectionParent;
    }

    @Override
    public void reportDuplicateMemberName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_PRJ: Duplicate member: '%s'.", this.name);

        compilerErrorHolder.add(
                this.compilationUnit,
                message,
                this.nameContext,
                this.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        if (this.dataTypeProperty == AntlrEnumerationProperty.NOT_FOUND)
        {
            String message = String.format(
                    "Cannot find member '%s.%s'.",
                    this.antlrProjectionParent.getKlass().getName(),
                    this.name);
            compilerErrorHolder.add(
                    this.compilationUnit,
                    message,
                    this.nameContext,
                    this.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
        }

        if (this.headerText.trim().isEmpty())
        {
            compilerErrorHolder.add(
                    this.compilationUnit,
                    "Empty header string.",
                    this.headerContext,
                    this.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
        }
    }
}
