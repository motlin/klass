package cool.klass.model.converter.compiler.state.value;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.value.MemberExpressionValue.MemberExpressionValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrMemberExpressionValue extends AntlrExpressionValue
{
    @Nonnull
    protected final AntlrClass               classState;
    @Nonnull
    protected final AntlrDataTypeProperty<?> dataTypePropertyState;

    public AntlrMemberExpressionValue(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrClass classState,
            @Nonnull AntlrDataTypeProperty<?> dataTypePropertyState)
    {
        super(elementContext, compilationUnit, inferred);
        this.classState = Objects.requireNonNull(classState);
        this.dataTypePropertyState = Objects.requireNonNull(dataTypePropertyState);
    }

    @Nonnull
    @Override
    public abstract MemberExpressionValueBuilder build();
}
