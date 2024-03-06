package cool.klass.jackson.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KlassResponsePagination
{
    private final int pageSize;
    private final int numberOfPages;
    private final int pageNumber;

    public KlassResponsePagination(int pageSize, int numberOfPages, int pageNumber)
    {
        this.pageSize = pageSize;
        this.numberOfPages = numberOfPages;
        this.pageNumber = pageNumber;
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
}
