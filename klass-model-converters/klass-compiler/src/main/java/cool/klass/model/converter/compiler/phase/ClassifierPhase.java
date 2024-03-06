package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrInterface;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.meta.grammar.KlassParser.AbstractDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;

public class ClassifierPhase
        extends AbstractCompilerPhase
{
    @Nullable
    private AntlrClassifier classifier;
    @Nullable
    private AntlrInterface iface;
    @Nullable
    private AntlrClass     klass;

    public ClassifierPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
    {
        super.enterInterfaceDeclaration(ctx);

        IdentifierContext identifier = ctx.interfaceHeader().identifier();
        this.iface      = new AntlrInterface(
                ctx,
                Optional.of(this.compilerState.getCompilerWalk().getCurrentCompilationUnit()),
                this.compilerState.getOrdinal(ctx),
                identifier,
                this.compilerState.getCompilerWalk().getCompilationUnit());
        this.classifier = this.iface;
    }

    @Override
    public void exitInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
    {
        this.compilerState.getDomainModel().exitInterfaceDeclaration(this.iface);
        this.iface      = null;
        this.classifier = null;
        super.exitInterfaceDeclaration(ctx);
    }

    @Override
    public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        super.enterClassDeclaration(ctx);

        String classOrUserKeyword = ctx.classHeader().classOrUser().getText();

        this.klass      = new AntlrClass(
                ctx,
                Optional.of(this.compilerState.getCompilerWalk().getCurrentCompilationUnit()),
                this.compilerState.getOrdinal(ctx),
                ctx.classHeader().identifier(),
                this.compilerState.getCompilerWalk().getCompilationUnit(),
                classOrUserKeyword.equals("user"));
        this.classifier = this.klass;
    }

    @Override
    public void exitClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        this.compilerState.getDomainModel().exitClassDeclaration(this.klass);
        this.klass      = null;
        this.classifier = null;
        super.exitClassDeclaration(ctx);
    }

    @Override
    public void enterAbstractDeclaration(@Nonnull AbstractDeclarationContext ctx)
    {
        super.enterAbstractDeclaration(ctx);
        this.klass.setAbstract(true);
    }

    @Override
    public void enterClassifierModifier(@Nonnull ClassifierModifierContext ctx)
    {
        super.enterClassifierModifier(ctx);

        int ordinal = this.classifier.getNumClassifierModifiers();

        AntlrModifier modifier = new AntlrModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalk().getCurrentCompilationUnit()),
                ordinal + 1,
                this.classifier);

        this.classifier.enterModifier(modifier);
    }
}
