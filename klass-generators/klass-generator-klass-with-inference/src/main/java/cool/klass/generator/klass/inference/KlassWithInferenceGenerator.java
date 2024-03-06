package cool.klass.generator.klass.inference;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.PackageableElement;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.tuple.Pair;

public class KlassWithInferenceGenerator
{
    @Nonnull
    private final DomainModel domainModel;
    @Nonnull
    private final Instant     now;

    public KlassWithInferenceGenerator(@Nonnull DomainModel domainModel, @Nonnull Instant now)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.now         = Objects.requireNonNull(now);
    }

    public void writeKlassFiles(@Nonnull Path outputPath)
    {
        this.domainModel
                .getTopLevelElements()
                .groupBy(PackageableElement::getPackageName)
                .keyMultiValuePairsView()
                .forEachWith(this::writeFile, outputPath);
    }

    private void writeFile(Pair<String, RichIterable<PackageableElement>> packagePair, Path outputPath)
    {
        String                           fullyQualifiedPackage = packagePair.getOne();
        RichIterable<PackageableElement> packageableElements   = packagePair.getTwo();

        Path   klassOutputPath = this.getOutputPath(outputPath, fullyQualifiedPackage);
        String classSourceCode = this.getKlassSourceCode(packageableElements, fullyQualifiedPackage);

        this.printStringToFile(klassOutputPath, classSourceCode);
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

    @Nonnull
    private String getKlassSourceCode(
            @Nonnull RichIterable<PackageableElement> packageableElements,
            @Nonnull String fullyQualifiedPackage)
    {
        String topLevelElementsSourceCode = packageableElements
                .collect(Element::getSourceCodeWithInference)
                .makeString("\n\n");

        //language=Klass
        return "package " + fullyQualifiedPackage + ".inference\n"
                + "\n"
                + "/*\n"
                + " * Auto-generated by {@link cool.klass.generator.klass.inference.KlassWithInferenceGenerator}\n"
                + " * at " + this.now + "\n"
                + " */\n"
                + "\n"
                + topLevelElementsSourceCode;
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