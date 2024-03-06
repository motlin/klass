package cool.klass.generator.reladomo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import cool.klass.model.meta.domain.DomainModel;

public class AbstractReladomoGenerator
{
    protected final DomainModel domainModel;

    public AbstractReladomoGenerator(DomainModel domainModel)
    {
        this.domainModel = domainModel;
    }

    protected String sanitizeXmlString(StringBuilder stringBuilder)
    {
        // TODO
        return stringBuilder.toString();
    }

    protected void printStringToFile(Path path, String contents)
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
