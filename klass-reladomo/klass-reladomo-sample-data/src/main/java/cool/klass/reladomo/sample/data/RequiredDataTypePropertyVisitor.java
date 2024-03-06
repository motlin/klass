package cool.klass.reladomo.sample.data;

import java.time.LocalDateTime;

public class RequiredDataTypePropertyVisitor
        extends AbstractDataTypePropertyVisitor
{
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(1999, 12, 31, 23, 59);

    @Override
    protected String getEmoji()
    {
        return "☝";
    }

    @Override
    protected int getIndex()
    {
        return 1;
    }

    @Override
    protected boolean getBoolean()
    {
        return true;
    }

    @Override
    protected LocalDateTime getLocalDateTime()
    {
        return LOCAL_DATE_TIME;
    }
}
