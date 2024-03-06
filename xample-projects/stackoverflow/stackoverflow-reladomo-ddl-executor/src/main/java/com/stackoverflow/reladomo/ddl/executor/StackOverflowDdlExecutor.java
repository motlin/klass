package com.stackoverflow.reladomo.ddl.executor;

import cool.klass.reladomo.connectionmanager.h2.H2ConnectionManager;
import cool.klass.reladomo.ddl.executor.DatabaseDdlExecutor;

public final class StackOverflowDdlExecutor
{
    private StackOverflowDdlExecutor()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static void main(String[] args)
    {
        DatabaseDdlExecutor.executeSql(H2ConnectionManager.getInstance().getConnection());
    }
}
