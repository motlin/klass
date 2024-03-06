package cool.klass.generator.react.prop.type;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.projection.Projection;

// TODO: Refactor out the commonality between the several Generators

public class ReactPropTypeGenerator
{
    @Nonnull
    private final DomainModel domainModel;

    public ReactPropTypeGenerator(@Nonnull DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    public void writePropTypes(@Nonnull Path outputPath)
    {
        for (Projection projection : this.domainModel.getProjections())
        {
            Path propTypeOutputPath = this.getPropTypeOutputPath(outputPath, projection);
            this.printStringToFile(propTypeOutputPath, this.getPropTypeSourceCode(projection));
        }
    }

    private String getPropTypeSourceCode(@Nonnull Projection projection)
    {
        PropTypeSourceCodeProjectionVisitor visitor = new PropTypeSourceCodeProjectionVisitor(projection, 0);
        projection.visit(visitor);
        return visitor.getResult();
    }

    @Nonnull
    private Path getPropTypeOutputPath(
            @Nonnull Path outputPath,
            @Nonnull Projection packageableElement)
    {
        String packageRelativePath = packageableElement.getPackageName()
                .replaceAll("\\.", "/");
        Path outputDirectory = outputPath
                .resolve(packageRelativePath)
                .resolve("react")
                .resolve("proptype");
        outputDirectory.toFile().mkdirs();
        String fileName = packageableElement.getName() + ".js";
        return outputDirectory.resolve(fileName);
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
