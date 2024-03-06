package cool.klass.jackson.response;

import com.fasterxml.jackson.annotation.JsonCreator;

public class KlassMetadata
{
    private final int pageNumber;
    private final int numberOfPages;

    @JsonCreator
    public KlassMetadata(int pageNumber, int numberOfPages)
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
