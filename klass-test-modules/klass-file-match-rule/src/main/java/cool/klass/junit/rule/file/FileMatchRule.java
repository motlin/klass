package cool.klass.junit.rule.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;

import org.junit.rules.ErrorCollector;

import static org.hamcrest.CoreMatchers.is;

public class FileMatchRule extends ErrorCollector
{
    public static String slurp(String resourceClassPathLocation, Class<?> callingClass)
    {
        InputStream inputStream = callingClass.getResourceAsStream(resourceClassPathLocation);
        Objects.requireNonNull(inputStream, resourceClassPathLocation);
        return slurp(inputStream);
    }

    public static String slurp(InputStream inputStream)
    {
        try (Scanner scanner = new Scanner(inputStream))
        {
            return scanner.useDelimiter("\\A").next();
        }
    }

    public void assertFileContents(
            String resourceClassPathLocation,
            String actualString,
            Class<?> callingClass)
    {
        try
        {
            assertFileContentsOrThrow(resourceClassPathLocation, actualString, callingClass);
        }
        catch (URISyntaxException | FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void assertFileContentsOrThrow(
            String resourceClassPathLocation,
            String actualString,
            Class<?> callingClass)
            throws URISyntaxException, FileNotFoundException
    {
        String expectedStringFromFile = slurp(resourceClassPathLocation, callingClass);
        URI    uri                    = callingClass.getResource(resourceClassPathLocation).toURI();
        if (!actualString.equals(expectedStringFromFile))
        {
            File file = new File(uri);
            try (PrintWriter printWriter = new PrintWriter(file))
            {
                printWriter.write(actualString);
            }
        }
        this.checkThat(
                "Writing expected file to: " + uri,
                actualString,
                is(expectedStringFromFile));
    }
}
