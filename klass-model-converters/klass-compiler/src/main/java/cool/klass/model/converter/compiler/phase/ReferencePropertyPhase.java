package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassReference;
import cool.klass.model.converter.compiler.state.AntlrClassReferenceOwner;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrMultiplicityOwner;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;

public class ReferencePropertyPhase
        extends AbstractCompilerPhase
{
    @Nullable
    protected AntlrClassReferenceOwner classReferenceOwnerState;
    @Nullable
    protected AntlrMultiplicityOwner   multiplicityOwnerState;

    public ReferencePropertyPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    public void handleClassReference(@Nonnull ClassReferenceContext ctx)
    {
        if (this.classReferenceOwnerState == null)
        {
            return;
        }

        String           className        = ctx.identifier().getText();
        AntlrDomainModel domainModelState = this.compilerState.getDomainModelState();
        AntlrClass       classState       = domainModelState.getClassByName(className);

        AntlrClassReference classReferenceState = new AntlrClassReference(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.classReferenceOwnerState,
                classState);

        this.classReferenceOwnerState.enterClassReference(classReferenceState);
    }

    public void handleMultiplicity(@Nonnull MultiplicityContext ctx)
    {
        if (this.multiplicityOwnerState == null)
        {
            return;
        }

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.multiplicityOwnerState);

        this.multiplicityOwnerState.enterMultiplicity(multiplicityState);
    }
}
