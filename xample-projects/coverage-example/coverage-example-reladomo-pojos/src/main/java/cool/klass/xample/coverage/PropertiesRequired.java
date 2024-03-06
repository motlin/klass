package cool.klass.xample.coverage;

import java.sql.Timestamp;

import com.gs.fw.common.mithra.util.DefaultInfinityTimestamp;

public class PropertiesRequired extends PropertiesRequiredAbstract
{
    public PropertiesRequired(Timestamp system)
    {
        super(system);
        // You must not modify this constructor. Mithra calls this internally.
        // You can call this constructor. You can also add new constructors.
    }

    public PropertiesRequired()
    {
        this(DefaultInfinityTimestamp.getDefaultInfinity());
    }

    public String getRequiredDerived()
    {
        return "cool.klass.xample.coverage.PropertiesRequired.getRequiredDerived";
    }
}
