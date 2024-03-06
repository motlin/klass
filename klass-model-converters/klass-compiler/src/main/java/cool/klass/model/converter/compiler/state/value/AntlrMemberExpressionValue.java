package cool.klass.model.converter.compiler.state.value;

import java.util.Objects;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.value.MemberExpressionValue.MemberExpressionValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrMemberExpressionValue extends AntlrExpressionValue
{
    protected final AntlrClass               classState;
    protected final AntlrDataTypeProperty<?> dataTypePropertyState;

    public AntlrMemberExpressionValue(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            AntlrClass classState,
            AntlrDataTypeProperty<?> dataTypePropertyState)
    {
        super(elementContext, compilationUnit, inferred);
        this.classState = Objects.requireNonNull(classState);
        this.dataTypePropertyState = Objects.requireNonNull(dataTypePropertyState);
    }

    @Override
    public MemberExpressionValueBuilder build()
    {
        return new MemberExpressionValueBuilder(
                this.elementContext,
                this.classState.getKlassBuilder(),
                this.dataTypePropertyState.getPropertyBuilder());
    }
}
