package cool.klass.servlet.filter.logging.structured;

import java.lang.reflect.Type;
import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

import com.fasterxml.jackson.annotation.JsonProperty;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.serialization.jackson.response.KlassResponse;
import cool.klass.serialization.jackson.response.KlassResponseMetadata;
import cool.klass.serialization.jackson.response.KlassResponsePagination;

public class KlassStructuredArguments
{
    private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d[HMS])(?!$)");

    private Instant startTime;
    private Instant endTime;

    // klass.request
    private Instant                       transactionTimestamp;
    private Optional<? extends Principal> principal;

    // klass.model
    private Optional<String> criteria;
    private Optional<String> orderBy;
    private Multiplicity     multiplicity;
    private Projection       projection;

    // klass.response.reladomo
    private Integer remoteRetrieveCountBefore;
    private Integer databaseRetrieveCountBefore;
    private Integer remoteRetrieveCountAfter;
    private Integer databaseRetrieveCountAfter;

    // klass.response.http
    private StatusType statusType;
    private Type       entityType;

    // klass.response.data
    private Integer  dataSize;
    private Class<?> dataType;

    // klass.response.pagination
    private Optional<Integer> pageSize;
    private Optional<Integer> numberOfPages;
    private Optional<Integer> pageNumber;

    public void setStartTime(Instant startTime)
    {
        if (this.startTime != null)
        {
            throw new AssertionError();
        }
        this.startTime = Objects.requireNonNull(startTime);
    }

    public void setEndTime(Instant endTime)
    {
        if (this.endTime != null)
        {
            throw new AssertionError();
        }
        this.endTime = Objects.requireNonNull(endTime);
    }

    public void setRemoteRetrieveCountBefore(int remoteRetrieveCountBefore)
    {
        if (this.remoteRetrieveCountBefore != null)
        {
            throw new AssertionError();
        }
        this.remoteRetrieveCountBefore = remoteRetrieveCountBefore;
    }

    public void setDatabaseRetrieveCountBefore(int databaseRetrieveCountBefore)
    {
        if (this.databaseRetrieveCountBefore != null)
        {
            throw new AssertionError();
        }
        this.databaseRetrieveCountBefore = databaseRetrieveCountBefore;
    }

    public void setRemoteRetrieveCountAfter(int remoteRetrieveCountAfter)
    {
        if (this.remoteRetrieveCountAfter != null)
        {
            throw new AssertionError();
        }
        this.remoteRetrieveCountAfter = remoteRetrieveCountAfter;
    }

    public void setDatabaseRetrieveCountAfter(int databaseRetrieveCountAfter)
    {
        if (this.databaseRetrieveCountAfter != null)
        {
            throw new AssertionError();
        }
        this.databaseRetrieveCountAfter = databaseRetrieveCountAfter;
    }

    public void setStatusType(StatusType statusType)
    {
        if (this.statusType != null)
        {
            throw new AssertionError();
        }
        this.statusType = Objects.requireNonNull(statusType);
    }

    public void setEntityType(Type entityType)
    {
        if (this.entityType != null)
        {
            throw new AssertionError();
        }
        this.entityType = Objects.requireNonNull(entityType);
    }

    public void setKlassResponse(KlassResponse klassResponse)
    {
        Object data = klassResponse.getData();
        if (data instanceof List)
        {
            List<?> list = (List<?>) data;
            int     size = list.size();
            this.dataSize = size;
        }
        else if (data != null)
        {
            this.dataType = data.getClass();
        }

        KlassResponseMetadata metadata = klassResponse.getMetadata();
        this.setKlassResponseMetadata(metadata);
    }

    private void setKlassResponseMetadata(KlassResponseMetadata metadata)
    {
        this.criteria             = metadata.getCriteria();
        this.orderBy              = metadata.getOrderBy();
        this.multiplicity         = metadata.getMultiplicity();
        this.projection           = metadata.getProjection();
        this.transactionTimestamp = metadata.getTransactionTimestamp();
        this.principal            = metadata.getPrincipal();

        Optional<KlassResponsePagination> pagination = metadata.getPagination();
        this.setKlassResponsePagination(pagination);
    }

    private void setKlassResponsePagination(Optional<KlassResponsePagination> pagination)
    {
        this.pageSize      = pagination.map(KlassResponsePagination::getPageSize);
        this.numberOfPages = pagination.map(KlassResponsePagination::getNumberOfPages);
        this.pageNumber    = pagination.map(KlassResponsePagination::getPageNumber);
    }

    private static String prettyPrintDuration(Duration duration)
    {
        String  trimmedString = duration.toString().substring(2);
        Matcher matcher       = DURATION_PATTERN.matcher(trimmedString);
        return matcher
                .replaceAll("$1 ")
                .toLowerCase();
    }

    @JsonProperty("klass.time.startTime")
    public Instant getStartTime()
    {
        return this.startTime;
    }

    @JsonProperty("klass.time.endTime")
    public Instant getEndTime()
    {
        return this.endTime;
    }

    @JsonProperty("klass.time.duration.pretty")
    public String getDurationPretty()
    {
        if (this.startTime == null || this.endTime == null)
        {
            return null;
        }

        Duration duration = Duration.between(this.startTime, this.endTime);
        return KlassStructuredArguments.prettyPrintDuration(duration);
    }

    @JsonProperty("klass.time.duration.ms")
    public Long getDurationMillis()
    {
        if (this.startTime == null || this.endTime == null)
        {
            return null;
        }

        Duration duration = Duration.between(this.startTime, this.endTime);
        return duration.toMillis();
    }

    @JsonProperty("klass.time.duration.ns")
    public Long getDurationNanos()
    {
        if (this.startTime == null || this.endTime == null)
        {
            return null;
        }

        Duration duration = Duration.between(this.startTime, this.endTime);
        return duration.toNanos();
    }

    @JsonProperty("klass.request.transactionTimestamp")
    public Instant getTransactionTimestamp()
    {
        return this.transactionTimestamp;
    }

    @JsonProperty("klass.request.principal")
    public Optional<? extends Principal> getPrincipal()
    {
        return this.principal;
    }

    @JsonProperty("klass.model.criteria")
    public Optional<String> getCriteria()
    {
        return this.criteria;
    }

    @JsonProperty("klass.model.orderBy")
    public Optional<String> getOrderBy()
    {
        return this.orderBy;
    }

    @JsonProperty("klass.model.multiplicity")
    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }

    @JsonProperty("klass.model.projection")
    public Projection getProjection()
    {
        return this.projection;
    }

    @JsonProperty("klass.response.reladomo.remoteRetrieveCount")
    public Integer getRemoteRetrieveCount()
    {
        if (this.remoteRetrieveCountAfter == null || this.remoteRetrieveCountBefore == null)
        {
            return null;
        }
        return this.remoteRetrieveCountAfter - this.remoteRetrieveCountBefore;
    }

    @JsonProperty("klass.response.reladomo.databaseRetrieveCount")
    public Integer getDatabaseRetrieveCount()
    {
        if (this.databaseRetrieveCountAfter == null || this.databaseRetrieveCountBefore == null)
        {
            return null;
        }
        return this.databaseRetrieveCountAfter - this.databaseRetrieveCountBefore;
    }

    @JsonProperty("klass.response.http.statusEnum")
    public StatusType getStatusEnum()
    {
        return this.statusType.toEnum();
    }

    @JsonProperty("klass.response.http.statusCode")
    public int getStatusCode()
    {
        return this.statusType.getStatusCode();
    }

    @JsonProperty("klass.response.http.statusFamily")
    public Family getStatusFamily()
    {
        return this.statusType.getFamily();
    }

    @JsonProperty("klass.response.http.statusPhrase")
    public String getStatusReasonPhrase()
    {
        return this.statusType.getReasonPhrase();
    }

    @JsonProperty("klass.response.http.entityType")
    public Type getEntityType()
    {
        return this.entityType;
    }

    @JsonProperty("klass.response.data.size")
    public Integer getDataSize()
    {
        return this.dataSize;
    }

    @JsonProperty("klass.response.data.type")
    public Class<?> getDataType()
    {
        return this.dataType;
    }

    @JsonProperty("klass.response.pagination.pageSize")
    public Optional<Integer> getPageSize()
    {
        return this.pageSize;
    }

    @JsonProperty("klass.response.pagination.numberOfPages")
    public Optional<Integer> getNumberOfPages()
    {
        return this.numberOfPages;
    }

    @JsonProperty("klass.response.pagination.pageNumber")
    public Optional<Integer> getPageNumber()
    {
        return this.pageNumber;
    }

    @Override
    public String toString()
    {
        return "structured logging";
    }
}
