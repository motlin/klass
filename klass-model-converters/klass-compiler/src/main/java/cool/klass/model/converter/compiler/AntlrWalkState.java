package cool.klass.model.converter.compiler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageNameContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;

public class AntlrWalkState
{
    @Nullable
    private PackageNameContext             packageContext;
    @Nullable
    private InterfaceDeclarationContext    interfaceDeclarationContext;
    @Nullable
    private ClassDeclarationContext        classDeclarationContext;
    @Nullable
    private AssociationDeclarationContext  associationDeclarationContext;
    @Nullable
    private ServiceGroupDeclarationContext serviceGroupDeclarationContext;
    @Nullable
    private ParameterizedPropertyContext   parameterizedPropertyContext;
    @Nullable
    private EnumerationDeclarationContext  enumerationDeclarationContext;
    @Nullable
    private ProjectionDeclarationContext   projectionDeclarationContext;
    @Nullable
    private UrlDeclarationContext          urlDeclarationContext;
    @Nullable
    private ServiceDeclarationContext      serviceDeclarationContext;
    @Nullable
    private ClassModifierContext           classModifierContext;

    @Nullable
    public PackageNameContext getPackageContext()
    {
        return this.packageContext;
    }

    public void exitCompilationUnit()
    {
        this.packageContext = null;
    }

    public void enterPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
    {
        this.packageContext = ctx.packageName();
    }

    public void enterInterfaceDeclaration(InterfaceDeclarationContext ctx)
    {
        AntlrWalkState.assertNull(this.interfaceDeclarationContext);
        this.interfaceDeclarationContext = ctx;
    }

    public void exitInterfaceDeclaration()
    {
        this.interfaceDeclarationContext = null;
    }

    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        AntlrWalkState.assertNull(this.classDeclarationContext);
        this.classDeclarationContext = ctx;
    }

    public void exitClassDeclaration()
    {
        this.classDeclarationContext = null;
    }

    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        AntlrWalkState.assertNull(this.enumerationDeclarationContext);
        this.enumerationDeclarationContext = ctx;
    }

    public void exitEnumerationDeclaration()
    {
        this.enumerationDeclarationContext = null;
    }

    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        AntlrWalkState.assertNull(this.associationDeclarationContext);
        this.associationDeclarationContext = ctx;
    }

    public void exitAssociationDeclaration()
    {
        this.associationDeclarationContext = null;
    }

    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        AntlrWalkState.assertNull(this.projectionDeclarationContext);
        this.projectionDeclarationContext = ctx;
    }

    public void exitProjectionDeclaration()
    {
        this.projectionDeclarationContext = null;
    }

    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.serviceGroupDeclarationContext = ctx;
    }

    public void exitServiceGroupDeclaration()
    {
        this.serviceGroupDeclarationContext = null;
    }

    public void enterUrlDeclaration(UrlDeclarationContext ctx)
    {
        this.urlDeclarationContext = ctx;
    }

    public void exitUrlDeclaration()
    {
        this.urlDeclarationContext = null;
    }

    public void enterServiceDeclaration(ServiceDeclarationContext ctx)
    {
        this.serviceDeclarationContext = ctx;
    }

    public void exitServiceDeclaration()
    {
        this.serviceDeclarationContext = null;
    }

    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        AntlrWalkState.assertNull(this.parameterizedPropertyContext);
        this.parameterizedPropertyContext = ctx;
    }

    public void exitParameterizedProperty()
    {
        this.parameterizedPropertyContext = null;
    }

    public void enterClassModifier(ClassModifierContext ctx)
    {
        AntlrWalkState.assertNull(this.classModifierContext);
        this.classModifierContext = ctx;
    }

    public void exitClassModifier()
    {
        this.classModifierContext = null;
    }

    private static void assertNull(@Nullable Object object)
    {
        if (object != null)
        {
            throw new IllegalStateException();
        }
    }

    @Nonnull
    public AntlrWalkState copy()
    {
        AntlrWalkState antlrWalkState = new AntlrWalkState();

        antlrWalkState.packageContext = this.packageContext;
        antlrWalkState.classDeclarationContext = this.classDeclarationContext;
        antlrWalkState.associationDeclarationContext = this.associationDeclarationContext;
        antlrWalkState.serviceGroupDeclarationContext = this.serviceGroupDeclarationContext;
        antlrWalkState.parameterizedPropertyContext = this.parameterizedPropertyContext;
        antlrWalkState.enumerationDeclarationContext = this.enumerationDeclarationContext;
        antlrWalkState.projectionDeclarationContext = this.projectionDeclarationContext;
        antlrWalkState.urlDeclarationContext = this.urlDeclarationContext;
        antlrWalkState.serviceDeclarationContext = this.serviceDeclarationContext;
        antlrWalkState.classModifierContext = this.classModifierContext;
        return antlrWalkState;
    }

    public void assertEmpty()
    {
        if (this.packageContext != null)
        {
            throw new AssertionError();
        }
        if (this.classDeclarationContext != null)
        {
            throw new AssertionError();
        }
        if (this.associationDeclarationContext != null)
        {
            throw new AssertionError();
        }
        if (this.serviceGroupDeclarationContext != null)
        {
            throw new AssertionError();
        }
        if (this.parameterizedPropertyContext != null)
        {
            throw new AssertionError();
        }
        if (this.enumerationDeclarationContext != null)
        {
            throw new AssertionError();
        }
        if (this.projectionDeclarationContext != null)
        {
            throw new AssertionError();
        }
        if (this.urlDeclarationContext != null)
        {
            throw new AssertionError();
        }
        if (this.serviceDeclarationContext != null)
        {
            throw new AssertionError();
        }
        if (this.classModifierContext != null)
        {
            throw new AssertionError();
        }
    }
}
