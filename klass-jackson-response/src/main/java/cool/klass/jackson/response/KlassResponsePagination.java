package cool.klass.jackson.response;

import com.fasterxml.jackson.annotation.JsonCreator;

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

    public int getPageNumber()
    {
        return this.pageNumber;
    }

    public int getNumberOfPages()
    {
        return this.numberOfPages;
    }
}
