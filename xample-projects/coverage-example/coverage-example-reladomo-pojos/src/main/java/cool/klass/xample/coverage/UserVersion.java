package cool.klass.xample.coverage;

import java.sql.Timestamp;

import com.gs.fw.common.mithra.util.DefaultInfinityTimestamp;

public class UserVersion
        extends UserVersionAbstract
{
    public UserVersion(Timestamp system)
    {
        super(system);
        // You must not modify this constructor. Mithra calls this internally.
        // You can call this constructor. You can also add new constructors.
    }

    public UserVersion()
    {
        this(DefaultInfinityTimestamp.getDefaultInfinity());
    }
}
