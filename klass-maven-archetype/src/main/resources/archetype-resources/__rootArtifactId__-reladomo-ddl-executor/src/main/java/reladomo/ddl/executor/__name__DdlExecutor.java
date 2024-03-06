package ${package}.reladomo.ddl.executor;

import cool.klass.reladomo.connectionmanager.h2.H2ConnectionManager;
import cool.klass.reladomo.ddl.executor.DatabaseDdlExecutor;

public final class ${name}DdlExecutor
{
    private ${name}DdlExecutor()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static void main(String[] args)
    {
        DatabaseDdlExecutor.executeSql(H2ConnectionManager.getInstance());
    }
}
