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
    protected AntlrClassReferenceOwner classReferenceOwner;
    @Nullable
    protected AntlrMultiplicityOwner   multiplicityOwner;

    public ReferencePropertyPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    public void handleClassReference(@Nonnull ClassReferenceContext ctx)
    {
        if (this.classReferenceOwner == null)
        {
            return;
        }

        String           className   = ctx.identifier().getText();
        AntlrDomainModel domainModel = this.compilerState.getDomainModel();
        AntlrClass       klass       = domainModel.getClassByName(className);

        AntlrClassReference classReference = new AntlrClassReference(
                ctx,
                Optional.of(this.compilerState.getCompilerWalk().getCurrentCompilationUnit()),
                this.classReferenceOwner,
                klass);

        this.classReferenceOwner.enterClassReference(classReference);
    }

    public void handleMultiplicity(@Nonnull MultiplicityContext ctx)
    {
        if (this.multiplicityOwner == null)
        {
            return;
        }

        AntlrMultiplicity multiplicity = new AntlrMultiplicity(
                ctx,
                Optional.of(this.compilerState.getCompilerWalk().getCurrentCompilationUnit()),
                this.multiplicityOwner);

        this.multiplicityOwner.enterMultiplicity(multiplicity);
    }
}
