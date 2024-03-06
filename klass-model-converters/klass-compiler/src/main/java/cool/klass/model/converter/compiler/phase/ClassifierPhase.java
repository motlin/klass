package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrInterface;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.meta.domain.api.InheritanceType;
import cool.klass.model.meta.grammar.KlassParser.AbstractDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.InheritanceTypeContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;

public class ClassifierPhase extends AbstractCompilerPhase
{
    @Nullable
    private AntlrClassifier classifierState;
    @Nullable
    private AntlrInterface  interfaceState;
    @Nullable
    private AntlrClass      classState;

    public ClassifierPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
    {
        super.enterInterfaceDeclaration(ctx);

        IdentifierContext identifier = ctx.interfaceHeader().identifier();
        this.interfaceState  = new AntlrInterface(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                identifier,
                this.compilerState.getOrdinal(ctx),
                this.compilerState.getCompilerWalkState().getPackageNameContext(),
                this.compilerState.getCompilerWalkState().getPackageName());
        this.classifierState = this.interfaceState;
    }

    @Override
    public void exitInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
    {
        this.compilerState.getDomainModelState().exitInterfaceDeclaration(this.interfaceState);
        this.interfaceState  = null;
        this.classifierState = null;
        super.exitInterfaceDeclaration(ctx);
    }

    @Override
    public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        super.enterClassDeclaration(ctx);

        String classOrUserKeyword = ctx.classHeader().classOrUser().getText();

        this.classState      = new AntlrClass(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx.classHeader().identifier(),
                this.compilerState.getOrdinal(ctx),
                this.compilerState.getCompilerWalkState().getPackageNameContext(),
                this.compilerState.getCompilerWalkState().getPackageName(),
                classOrUserKeyword.equals("user"));
        this.classifierState = this.classState;
    }

    @Override
    public void exitClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        this.compilerState.getDomainModelState().exitClassDeclaration(this.classState);
        this.classState      = null;
        this.classifierState = null;
        super.exitClassDeclaration(ctx);
    }

    @Override
    public void enterAbstractDeclaration(@Nonnull AbstractDeclarationContext ctx)
    {
        super.enterAbstractDeclaration(ctx);

        this.classState.setInheritanceType(InheritanceType.TABLE_PER_SUBCLASS);
    }

    @Override
    public void enterInheritanceType(@Nonnull InheritanceTypeContext ctx)
    {
        super.enterInheritanceType(ctx);

        InheritanceType inheritanceType = InheritanceType.byPrettyName(ctx.getText());
        this.classState.setInheritanceType(inheritanceType);
    }

    @Override
    public void enterClassifierModifier(@Nonnull ClassifierModifierContext ctx)
    {
        super.enterClassifierModifier(ctx);

        int ordinal = this.classifierState.getNumClassifierModifiers();

        AntlrModifier modifierState = new AntlrModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx,
                ordinal + 1,
                this.classifierState);

        this.classifierState.enterModifier(modifierState);
    }
}
