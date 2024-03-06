package cool.klass.reladomo.sample.data;

import java.time.LocalDateTime;

public class OptionalDataTypePropertyVisitor
        extends AbstractDataTypePropertyVisitor
{
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2000, 1, 1, 0, 0);

    @Override
    protected String getEmoji()
    {
        return "✌";
    }

    @Override
    protected int getIndex()
    {
        return 2;
    }

    @Override
    protected boolean getBoolean()
    {
        return false;
    }

    @Override
    protected LocalDateTime getLocalDateTime()
    {
        return LOCAL_DATE_TIME;
    }
}
