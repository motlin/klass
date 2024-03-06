* BVP, justification
* Composite writes (editing the list of tags)
* Default, final properties
* Inheritance, many-to-many
* PATCH
* State machine
* Internal design
* OpenAPI to Klass converter
* Possibly converters from other models (CMDB, Hibernate, database schema, JHipster Definition Language, etc.)
* Search (simple, full text)
* Data entitlements
* Transient / pure objects. Fully cached.
* Optional criteria, elastic search full text index, you need to constraints, non-unique constraints for index, tree, recursive projections, and inheritance, many to many
* Anonymous projections
* RPC syntax
* TODO: Document format: csv with multiple to-many ends. With systemTemporal changes.
* Opt-out of systemTemporal, versioned, audited, optimisticallyLocked instead of opt-in



```java
public class QuestionDTO
{
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String body;
    private Instant system;
    private Instant systemFrom;
    private Instant systemTo;
    @NotNull
    private String createdById;
    @NotNull
    private Instant createdOn;
    @NotNull
    private String lastUpdatedById;

    private List<AnswerDTO> answers;
    private QuestionVersionDTO version;

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getBody()
    {
        return this.body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public Instant getSystem()
    {
        return this.system;
    }

    public void setSystem(Instant system)
    {
        this.system = system;
    }

    public Instant getSystemFrom()
    {
        return this.systemFrom;
    }

    public void setSystemFrom(Instant systemFrom)
    {
        this.systemFrom = systemFrom;
    }

    public Instant getSystemTo()
    {
        return this.systemTo;
    }

    public void setSystemTo(Instant systemTo)
    {
        this.systemTo = systemTo;
    }

    public String getCreatedById()
    {
        return this.createdById;
    }

    public void setCreatedById(String createdById)
    {
        this.createdById = createdById;
    }

    public Instant getCreatedOn()
    {
        return this.createdOn;
    }

    public void setCreatedOn(Instant createdOn)
    {
        this.createdOn = createdOn;
    }

    public String getLastUpdatedById()
    {
        return this.lastUpdatedById;
    }

    public void setLastUpdatedById(String lastUpdatedById)
    {
        this.lastUpdatedById = lastUpdatedById;
    }

    public List<AnswerDTO> getAnswers()
    {
        return this.answers;
    }

    public void setAnswers(List<AnswerDTO> answers)
    {
        this.answers = answers;
    }

    public QuestionVersionDTO getVersion()
    {
        return this.version;
    }

    public void setVersion(QuestionVersionDTO version)
    {
        this.version = version;
    }
}
```
