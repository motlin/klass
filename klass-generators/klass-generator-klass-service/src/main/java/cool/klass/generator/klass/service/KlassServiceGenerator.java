package cool.klass.generator.klass.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.PackageableElement;
import org.eclipse.collections.api.list.ImmutableList;

public class KlassServiceGenerator
{
    @Nonnull
    private final DomainModel domainModel;

    public KlassServiceGenerator(@Nonnull DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    public void writeKlassFiles(@Nonnull Path outputPath)
    {
        ImmutableList<String> packageNames = this.domainModel
                .getClasses()
                .asLazy()
                .collect(PackageableElement::getPackageName)
                .distinct()
                .toImmutableList();
        for (String packageName : packageNames)
        {
            this.writeFile(packageName, outputPath);
        }
    }

    private void writeFile(String fullyQualifiedPackage, Path outputPath)
    {
        Path klassOutputPath = this.getOutputPath(outputPath, fullyQualifiedPackage);
        String sourceCode    = KlassServiceSourceCodeGenerator.getPackageSourceCode(this.domainModel, fullyQualifiedPackage);

        this.printStringToFile(klassOutputPath, sourceCode);
    }

    @Nonnull
    private Path getOutputPath(
            @Nonnull Path outputPath,
            @Nonnull String fullyQualifiedPackage)
    {
        String packageRelativePath = fullyQualifiedPackage.replaceAll("\\.", "/");
        Path   klassDirectory      = outputPath.resolve(packageRelativePath);
        String fileName            = fullyQualifiedPackage + ".klass";

        klassDirectory.toFile().mkdirs();
        return klassDirectory.resolve(fileName);
    }

    private void printStringToFile(@Nonnull Path path, String contents)
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
}
