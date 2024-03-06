package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByMemberReferencePath;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.OrderByMemberReferencePathContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

public class OrderByDirectionInferencePhase
        extends AbstractCompilerPhase
{
    public OrderByDirectionInferencePhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "OrderBy Direction";
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitOrderByMemberReferencePath(OrderByMemberReferencePathContext inPlaceContext)
    {
        this.runCompilerMacro(inPlaceContext);
        super.exitOrderByMemberReferencePath(inPlaceContext);
    }

    private void runCompilerMacro(OrderByMemberReferencePathContext inPlaceContext)
    {
        AntlrOrderByMemberReferencePath orderByMemberReferencePath =
                this.compilerState.getCompilerWalk().getOrderByMemberReferencePath();

        if (orderByMemberReferencePath.getOrderByDirection() != null)
        {
            return;
        }

        String            sourceCodeText = "ascending";
        ParseTreeListener compilerPhase  = new OrderByDirectionPhase(this.compilerState);

        this.compilerState.runInPlaceCompilerMacro(
                orderByMemberReferencePath,
                this,
                sourceCodeText,
                KlassParser::orderByDirection,
                inPlaceContext,
                compilerPhase);
    }
}
