package cool.klass.xample.coverage;

import java.time.Instant;
import java.time.LocalDate;

public class ClassWithDerivedProperty
        extends ClassWithDerivedPropertyAbstract
{
    public ClassWithDerivedProperty()
    {
        // You must not modify this constructor. Mithra calls this internally.
        // You can call this constructor. You can also add new constructors.
    }

    public String getDerivedRequiredString()
    {
        return "derivedRequiredString";
    }

    public Integer getDerivedRequiredInteger()
    {
        return 1;
    }

    public Long getDerivedRequiredLong()
    {
        return 1L;
    }

    public Double getDerivedRequiredDouble()
    {
        return 1.0;
    }

    public Float getDerivedRequiredFloat()
    {
        return 1.0f;
    }

    public Boolean isDerivedRequiredBoolean()
    {
        return true;
    }

    public Instant getDerivedRequiredInstant()
    {
        return Instant.now();
    }

    public LocalDate getDerivedRequiredLocalDate()
    {
        return LocalDate.now();
    }

    public String getDerivedOptionalString()
    {
        return "derivedOptionalString";
    }

    public Integer getDerivedOptionalInteger()
    {
        return 1;
    }

    public Long getDerivedOptionalLong()
    {
        return 1L;
    }

    public Double getDerivedOptionalDouble()
    {
        return 1.0;
    }

    public Float getDerivedOptionalFloat()
    {
        return 1.0f;
    }

    public Boolean isDerivedOptionalBoolean()
    {
        return true;
    }

    public Instant getDerivedOptionalInstant()
    {
        return Instant.now();
    }

    public LocalDate getDerivedOptionalLocalDate()
    {
        return LocalDate.now();
    }
}
