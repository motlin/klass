package ${package}.reladomo.data.generator;

import com.gs.fw.common.mithra.MithraManagerProvider;
import ${package}.User;

// TODO: Code generate this class. Skip transient types.
public final class ${name}TestDataGenerator
{
    private ${name}TestDataGenerator()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static void main(String[] args)
    {
        ${name}TestDataGenerator.populateData();
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
