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
    public void enterOrderByMemberReferencePath(@Nonnull OrderByMemberReferencePathContext ctx)
    {
        super.enterOrderByMemberReferencePath(ctx);

        AntlrOrderByMemberReferencePath orderByMemberReferencePathState =
                this.compilerState.getCompilerWalkState().getOrderByMemberReferencePathState();

        if (orderByMemberReferencePathState.getOrderByDirectionState() != null)
        {
            return;
        }

        String sourceCodeText = "ascending";
        this.runCompilerMacro(orderByMemberReferencePathState, sourceCodeText);
    }

    private void runCompilerMacro(
            @Nonnull AntlrOrderByMemberReferencePath orderByMemberReferencePathState,
            @Nonnull String sourceCodeText)
    {
        ParseTreeListener compilerPhase = new OrderByDirectionPhase(this.compilerState);

        this.compilerState.runNonRootCompilerMacro(
                orderByMemberReferencePathState,
                this,
                sourceCodeText,
                KlassParser::orderByDirection,
                compilerPhase);
    }
}
