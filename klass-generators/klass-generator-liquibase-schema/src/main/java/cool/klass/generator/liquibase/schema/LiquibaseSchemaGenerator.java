package cool.klass.generator.liquibase.schema;

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
import org.eclipse.collections.api.list.ImmutableList;

public class LiquibaseSchemaGenerator
{
    private static final Converter<String, String> CONVERTER =
            CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);

    private final DomainModel domainModel;

    public LiquibaseSchemaGenerator(DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    public void writeLiquibaseSchema(@Nonnull Path path)
    {
        ImmutableList<Klass> classes = this.domainModel.getClasses();
        for (int i = 0; i < classes.size(); i++)
        {
            Klass klass = classes.get(i);
            String tableName = CONVERTER.convert(klass.getName());

            String packageName  = klass.getPackageName();
            String relativePath = packageName.replaceAll("\\.", "/");
            Path   parentPath   = path.resolve(relativePath);
            createDirectories(parentPath);

            Path ddlOutputPath = parentPath.resolve(tableName + ".ddl");
            if (!ddlOutputPath.toFile().exists())
            {
                String sourceCode = TableGenerator.getTable(klass, i + 1);
                this.printStringToFile(ddlOutputPath, sourceCode);
            }

            Path idxOutputPath = parentPath.resolve(tableName + ".idx");
            if (!idxOutputPath.toFile().exists())
            {
                String sourceCode = IndexGenerator.getIndex(klass, i + 1);
                this.printStringToFile(idxOutputPath, sourceCode);
            }

            Path fkOutputPath = parentPath.resolve(tableName + ".fk");
            if (!fkOutputPath.toFile().exists())
            {
                Optional<String> sourceCode = ForeignKeyGenerator.getForeignKey(klass, i + 1);
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
