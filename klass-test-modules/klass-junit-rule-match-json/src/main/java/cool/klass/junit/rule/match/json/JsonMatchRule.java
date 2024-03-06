package cool.klass.junit.rule.match.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;
import org.json.JSONException;
import org.junit.rules.ErrorCollector;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class JsonMatchRule extends ErrorCollector
{
    public static String slurp(String resourceClassPathLocation, Class<?> callingClass)
    {
        InputStream inputStream = callingClass.getResourceAsStream(resourceClassPathLocation);
        Objects.requireNonNull(inputStream, resourceClassPathLocation);
        return JsonMatchRule.slurp(inputStream);
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
            this.assertFileContentsOrThrow(resourceClassPathLocation, actualString, callingClass);
        }
        catch (URISyntaxException | FileNotFoundException | JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void assertFileContentsOrThrow(
            String resourceClassPathLocation,
            String actualString,
            Class<?> callingClass)
            throws URISyntaxException, FileNotFoundException, JSONException
    {
        InputStream inputStream = callingClass.getResourceAsStream(resourceClassPathLocation);
        if (inputStream == null)
        {
            String               packageName      = callingClass.getPackage().getName();
            ListIterable<String> packageNameParts = ArrayAdapter.adapt(packageName.split("\\."));
            Path                 testResources    = Paths.get("", "src", "test", "resources").toAbsolutePath();
            Path                 packagePath      = packageNameParts.injectInto(testResources, Path::resolve);
            File                 resourceFile     = packagePath.resolve(resourceClassPathLocation).toFile();

            assertThat(resourceFile.exists(), is(false));
            this.writeStringToFile(actualString, resourceFile);
            fail(resourceClassPathLocation);
        }

        String expectedStringFromFile = JsonMatchRule.slurp(inputStream);
        URI    uri                    = callingClass.getResource(resourceClassPathLocation).toURI();
        if (!actualString.equals(expectedStringFromFile))
        {
            File file = new File(uri);
            this.writeStringToFile(actualString, file);
        }
        JSONAssert.assertEquals(actualString, expectedStringFromFile, actualString, JSONCompareMode.STRICT);
    }

    private void writeStringToFile(String string, File file) throws FileNotFoundException
    {
        try (PrintWriter printWriter = new PrintWriter(file))
        {
            printWriter.write(string);
        }
    }
}
