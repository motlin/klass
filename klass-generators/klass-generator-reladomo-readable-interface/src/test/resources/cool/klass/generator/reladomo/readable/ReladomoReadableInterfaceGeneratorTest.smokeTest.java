package cool.klass.test.reladomo.readable;

import javax.annotation.*;
import java.sql.*;
import java.time.*;

/**
 * Auto-generated by {@link cool.klass.generator.reladomo.readable.ReladomoReadableInterfaceGenerator}
 */
public interface ReladomoReadableClassWithDerivedProperty
{
    // key
    @Nonnull
    String getKey();

    // derived
    @Nonnull
    String getDerivedRequiredString();

    // derived
    @Nonnull
    int getDerivedRequiredInteger();

    // derived
    @Nonnull
    long getDerivedRequiredLong();

    // derived
    @Nonnull
    double getDerivedRequiredDouble();

    // derived
    @Nonnull
    float getDerivedRequiredFloat();

    // derived
    @Nonnull
    boolean isDerivedRequiredBoolean();

    // derived
    @Nonnull
    Timestamp getDerivedRequiredInstant();

    // derived
    @Nonnull
    Date getDerivedRequiredLocalDate();

    // derived
    String getDerivedOptionalString();

    // derived
    int getDerivedOptionalInteger();

    // derived
    long getDerivedOptionalLong();

    // derived
    double getDerivedOptionalDouble();

    // derived
    float getDerivedOptionalFloat();

    // derived
    boolean isDerivedOptionalBoolean();

    // derived
    Timestamp getDerivedOptionalInstant();

    // derived
    Date getDerivedOptionalLocalDate();
}