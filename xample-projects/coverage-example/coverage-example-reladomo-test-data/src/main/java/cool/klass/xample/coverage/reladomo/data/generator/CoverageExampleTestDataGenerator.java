package cool.klass.xample.coverage.reladomo.data.generator;

import com.gs.fw.common.mithra.MithraManagerProvider;
import cool.klass.xample.coverage.User;

// TODO: Code generate this class. Skip transient types.
public final class CoverageExampleTestDataGenerator
{
    private CoverageExampleTestDataGenerator()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static void main(String[] args)
    {
        CoverageExampleTestDataGenerator.populateData();
    }

    public static void populateData()
    {
        MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx ->
        {
            User exampleUser = new User();
            exampleUser.setUserId("Example User");
            exampleUser.setFirstName("Example 😀");
            exampleUser.setLastName("User 😁");
            exampleUser.setEmail("user@example.com");

            exampleUser.insert();

            return null;
        });
    }
}
