package io.liftwizard.reladomo.test.resource.writer.tests;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.resource.ResourceAccessor;

public class CloseableLiquibase
        extends Liquibase
{
    public CloseableLiquibase(String changeLogFile, ResourceAccessor resourceAccessor, Database database)
    {
        super(changeLogFile, resourceAccessor, database);
    }
}
