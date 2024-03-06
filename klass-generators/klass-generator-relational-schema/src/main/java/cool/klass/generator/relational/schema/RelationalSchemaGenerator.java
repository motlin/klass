package cool.klass.generator.relational.schema;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;

public class RelationalSchemaGenerator
{
    private static final Converter<String, String> CONVERTER =
            CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);

    private final DomainModel domainModel;

    public RelationalSchemaGenerator(DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    public void writeRelationalSchema(@Nonnull Path path)
    {
        for (Klass klass : this.domainModel.getClasses())
        {
            String tableName = CONVERTER.convert(klass.getName());

            String packageName  = klass.getPackageName();
            String relativePath = packageName.replaceAll("\\.", "/");
            Path   parentPath   = path.resolve(relativePath);
            createDirectories(parentPath);

            Path ddlOutputPath = parentPath.resolve(tableName + ".ddl");
            if (!ddlOutputPath.toFile().exists())
            {
                String sourceCode = DdlGenerator.getDdl(klass);
                this.printStringToFile(ddlOutputPath, sourceCode);
            }

            Path idxOutputPath = parentPath.resolve(tableName + ".idx");
            if (!idxOutputPath.toFile().exists())
            {
                String sourceCode = IdxGenerator.getIdx(klass);
                this.printStringToFile(idxOutputPath, sourceCode);
            }

            Path fkOutputPath = parentPath.resolve(tableName + ".fk");
            if (!fkOutputPath.toFile().exists())
            {
                Optional<String> sourceCode = FkGenerator.getFk(klass);
                sourceCode.ifPresent(s -> this.printStringToFile(fkOutputPath, s));
            }
        }
    }

    private static void createDirectories(Path dir)
    {
        try
        {
            Files.createDirectories(dir);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
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
