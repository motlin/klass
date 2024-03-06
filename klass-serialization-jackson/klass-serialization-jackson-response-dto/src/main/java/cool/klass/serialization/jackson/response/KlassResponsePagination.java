package cool.klass.serialization.jackson.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import cool.klass.logging.context.MDCCloseable;

public class KlassResponsePagination
{
    private final int pageSize;
    private final int numberOfPages;
    private final int pageNumber;

    public KlassResponsePagination(int pageSize, int numberOfPages, int pageNumber)
    {
        this.pageSize      = pageSize;
        this.numberOfPages = numberOfPages;
        this.pageNumber    = pageNumber;
    }

    @JsonProperty
    public int getPageSize()
    {
        return this.pageSize;
    }

    @JsonProperty
    public int getNumberOfPages()
    {
        return this.numberOfPages;
    }

    @JsonProperty
    public int getPageNumber()
    {
        return this.pageNumber;
    }

    @Override
    public String toString()
    {
        return String.format(
                "{pageSize:%d,numberOfPages:%d,pageNumber:%d}",
                this.pageSize,
                this.numberOfPages,
                this.pageNumber);
    }

    public void withMDC(MDCCloseable mdc)
    {
        mdc.put("klass.response.pagination.pageSize", String.valueOf(this.pageSize));
        mdc.put("klass.response.pagination.numberOfPages", String.valueOf(this.numberOfPages));
        mdc.put("klass.response.pagination.pageNumber", String.valueOf(this.pageNumber));
    }
}
