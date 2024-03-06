package cool.klass.reladomo.sample.data;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;

public class RequiredDataTypePropertyVisitor
        extends AbstractDataTypePropertyVisitor
{
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(1999, 12, 31, 23, 59);

    @Nonnull
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

    @Nonnull
    @Override
    protected LocalDateTime getLocalDateTime()
    {
        return LOCAL_DATE_TIME;
    }
}
