package cool.klass.xample.coverage;

import java.sql.Timestamp;

import com.gs.fw.common.mithra.util.DefaultInfinityTimestamp;

public class PropertiesOptional extends PropertiesOptionalAbstract
{
    public PropertiesOptional(Timestamp system)
    {
        super(system);
        // You must not modify this constructor. Mithra calls this internally.
        // You can call this constructor. You can also add new constructors.
    }

    public PropertiesOptional()
    {
        this(DefaultInfinityTimestamp.getDefaultInfinity());
    }
}
