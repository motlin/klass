package cool.klass.generator.perpackage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.PackageableElement;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AbstractPerPackageGenerator
{
    @Nonnull
    protected final DomainModel domainModel;

    protected AbstractPerPackageGenerator(@Nonnull DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    public void writeFiles(@Nonnull Path outputPath)
    {
        ImmutableList<String> packageNames = this.getPackageNames();
        for (String packageName : packageNames)
        {
            this.writeFile(packageName, outputPath);
        }
    }

    protected ImmutableList<String> getPackageNames()
    {
        return this.domainModel
                .getClasses()
                .asLazy()
                .collect(PackageableElement::getPackageName)
                .distinct()
                .toImmutableList();
    }

    protected void writeFile(String fullyQualifiedPackage, Path outputPath)
    {
        Path klassOutputPath = this.getOutputPath(outputPath, fullyQualifiedPackage);
        String sourceCode    = this.getPackageSourceCode(fullyQualifiedPackage);

        this.printStringToFile(klassOutputPath, sourceCode);
    }

    @Nonnull
    private Path getOutputPath(
            @Nonnull Path outputPath,
            @Nonnull String fullyQualifiedPackage)
    {
        String packageRelativePath = fullyQualifiedPackage.replaceAll("\\.", "/");
        Path   klassDirectory      = outputPath.resolve(packageRelativePath);
        klassDirectory.toFile().mkdirs();
        return klassDirectory.resolve(this.getFileName(fullyQualifiedPackage));
    }

    protected void printStringToFile(@Nonnull Path path, String contents)
    {
        try (PrintStream printStream = new PrintStream(new FileOutputStream(path.toFile())))
        {
            printStream.print(contents);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    protected abstract String getFileName(@Nonnull String fullyQualifiedPackage);

    @Nonnull
    protected abstract String getPackageSourceCode(@Nonnull String fullyQualifiedPackage);
}
