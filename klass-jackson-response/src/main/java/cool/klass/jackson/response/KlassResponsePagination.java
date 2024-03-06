package cool.klass.jackson.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KlassResponsePagination
{
    private final int pageNumber;
    private final int numberOfPages;

    @JsonCreator
    public KlassResponsePagination(int pageNumber, int numberOfPages)
    {
        this.pageNumber = pageNumber;
        this.numberOfPages = numberOfPages;
    }

    @JsonProperty
    public int getPageNumber()
    {
        return this.pageNumber;
    }

    @JsonProperty
    public int getNumberOfPages()
    {
        return this.numberOfPages;
    }
}
