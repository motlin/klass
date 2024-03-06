package cool.klass.test.constants;

public final class KlassTestConstants
{
    //<editor-fold desc="source code">
    //language=Klass
    public static final String STACK_OVERFLOW_SOURCE_CODE_TEXT = ""
            + "/*\n"
            + " * Simplified StackOverflow domain model. One question has many answers.\n"
            + " */\n"
            + "package com.stackoverflow\n"
            + "\n"
            + "// 'user' is just a special class that represents logged in users\n"
            + "// There must only be a single user class in the model, it must have a single key, and the key must be of type String. Other properties must be nullable\n"
            + "user User\n"
            + "    read\n"
            + "    systemTemporal\n"
            + "{\n"
            + "    // `read` keyword with no projection means create read services with the default read projection\n"
            + "    // The default read projection includes just this class and all its properties\n"
            + "    userId        : String key userId;\n"
            + "\n"
            + "    // TODO: String max lengths\n"
            + "    firstName         : String?;\n"
            + "    lastName          : String?;\n"
            + "    // TODO: Consider adding primitive types like email address and url\n"
            + "    email             : String?;\n"
            + "\n"
            + "    // TODO: Unique property sets\n"
            + "}\n"
            + "\n"
            + "enumeration Status\n"
            + "{\n"
            + "    OPEN(\"Open\"),\n"
            + "    ON_HOLD(\"On hold\"),\n"
            + "    CLOSED(\"Closed\"),\n"
            + "}\n"
            + "\n"
            + "// TODO: Sets of unique properties other than keys\n"
            + "// TODO: Try to infer whether a query is for a unique item and assert multiplicity one\n"
            + "// TODO: Check for ownership cycles and give a good error message\n"
            + "// TODO: Errors for using reserved names like system, systemFrom, systemTo, etc.\n"
            + "// TODO: Errors for using versioned when not systemTemporal, audited when not systemTemporal, optimisticallyLocked when not versioned\n"
            + "class Question\n"
            + "    read(QuestionReadProjection)\n"
            + "    create(QuestionWriteProjection)\n"
            + "    update(QuestionWriteProjection)\n"
            + "    systemTemporal\n"
            + "    versioned\n"
            + "    audited\n"
            + "    optimisticallyLocked\n"
            + "{\n"
            + "    id                : Long key id;\n"
            + "    title             : String;\n"
            + "    body              : String;\n"
            + "\n"
            + "    // TODO: Statemachine\n"
            + "    status            : Status;\n"
            + "    deleted           : Boolean;\n"
            + "\n"
            + "    // TODO: Consider type inference on paramterized property parameters based on usage in relationship\n"
            + "    // orderBy natural key ascending is the default\n"
            + "    answersWithSubstring(substring: String[1..1]): Answer[0..*]\n"
            + "        orderBy: this.id ascending\n"
            + "    {\n"
            + "        this.id == Answer.questionId\n"
            + "            && Answer.body contains substring\n"
            + "    }\n"
            + "\n"
            + "    activeAnswers(): Answer[0..*]\n"
            + "    {\n"
            + "        this.id == Answer.questionId\n"
            + "            && Answer.deleted == false\n"
            + "    }\n"
            + "\n"
            + "    // TODO: Add keywords for declaring special properties. It may look redundant like:\n"
            + "    // TODO: system system: TemporalRange, but would allow renames like:\n"
            + "    // TODO: system known: TemporalRange\n"
            + "\n"
            + "    // This isn't even a real property. It's referenced in criteria as a shorthand. For example Question.system == {time} is shorthand for Question.systemFrom <= {time} && Question.systemTo > {time}\n"
            + "    system            : TemporalRange;\n"
            + "\n"
            + "    // These two properties are implied by systemTemporal, and are not supposed to actually be declared\n"
            + "    systemFrom        : TemporalInstant;\n"
            + "    systemTo          : TemporalInstant;\n"
            + "\n"
            + "    // TODO: private properties must not be used in projections\n"
            + "\n"
            + "    // These three properties and two parameterized properties are implied by audited, and are not supposed to actually be declared\n"
            + "    // They could automatically be part of read projections and not write projections\n"
            + "    createdById       : String private;\n"
            + "\n"
            + "    // createdOn, or createdDate?\n"
            + "    createdOn         : Instant;\n"
            + "    lastUpdatedById   : String private;\n"
            + "\n"
            + "    createdBy(): User[1..1]\n"
            + "    {\n"
            + "        this.createdById == User.userId\n"
            + "    }\n"
            + "\n"
            + "    lastUpdatedBy(): User[1..1]\n"
            + "    {\n"
            + "        this.lastUpdatedById == User.userId\n"
            + "    }\n"
            + "}\n"
            + "\n"
            + "// The _Version class is implied by the versioned annotation, and not supposed to actually be declared\n"
            + "// TODO: If you do declare one, we'd need a special keyword version instead of class\n"
            + "// TODO: Needs a syntax like versions(Question)\n"
            + "// TODO: Error message if versions does point to a version, if the versioned type isn't systemTemporal, maybe also if the versioned type isn't systemTemporal/versioned\n"
            + "/*\n"
            + "class QuestionVersion\n"
            + "    systemTemporal\n"
            + "    versions(Question)\n"
            + "{\n"
            + "    // Key properties copied from Question\n"
            + "    key id            : ID\n"
            + "\n"
            + "    // The version number\n"
            + "    // It gets incremented automatically when anything in the version project (QuestionWriteProjection) gets edited\n"
            + "    // TODO: Consider changing the primitive names to int32, int64, float32, float64, id32, id64\n"
            + "    // TODO: Consider a primitive property type VersionNumber\n"
            + "    number            : Integer\n"
            + "}\n"
            + "*/\n"
            + "\n"
            + "// TODO: Error message if versions doesn't point to a version class\n"
            + "association QuestionHasAnswer\n"
            + "{\n"
            + "    // TODO: *final* question: Question[1..1]\n"
            + "    question: Question[1..1];\n"
            + "    answers: Answer[0..*]\n"
            + "        orderBy: this.id ascending;\n"
            + "\n"
            + "    // TODO: check that the foreign key (questionId) is required since the to-one relationship end is also required (question: Question[1..1])\n"
            + "    // ordering by primary key ascending is the default\n"
            + "    relationship this.id == Answer.questionId\n"
            + "}\n"
            + "\n"
            + "// The _HasVersion association is implied by the versioned annotation, and not supposed to actually be declared\n"
            + "// It could automatically be part of read projections and not write projections\n"
            + "/*\n"
            + "association QuestionHasVersion\n"
            + "{\n"
            + "    question: Question[1..1];\n"
            + "    version: QuestionVersion[1..1];\n"
            + "\n"
            + "    // implied\n"
            + "    relationship this.id == QuestionVersion.id\n"
            + "            && this.system == QuestionVersion.system\n"
            + "}\n"
            + "*/\n"
            + "\n"
            + "class Answer\n"
            + "    systemTemporal\n"
            + "    versioned\n"
            + "    audited\n"
            + "    optimisticallyLocked\n"
            + "{\n"
            + "    id                : Long key id;\n"
            + "    body              : String;\n"
            + "    deleted           : Boolean;\n"
            + "    // TODO: questionId: Long private *final*\n"
            + "    questionId: Long private;\n"
            + "}\n"
            + "\n"
            + "// TODO Model AnswerVote\n"
            + "// TODO Interface Vote\n"
            + "// TODO superclass AbstractVote\n"
            + "// TODO Aggregation for number of upvotes and downvotes\n"
            + "class QuestionVote\n"
            + "    systemTemporal\n"
            + "    audited\n"
            + "{\n"
            + "    questionId    : Long key;\n"
            + "    createdById   : String key;\n"
            + "    direction         : VoteDirection;\n"
            + "}\n"
            + "\n"
            + "enumeration VoteDirection\n"
            + "{\n"
            + "    UP(\"up\"),\n"
            + "    DOWN(\"down\"),\n"
            + "}\n"
            + "\n"
            + "// TODO: Projection inheritance? QuestionReadProjection extends QuestionWriteProjection\n"
            + "projection QuestionReadProjection on Question\n"
            + "{\n"
            + "    // field names are \"includes\", field values are column headers when serializing to a tabular format\n"
            + "    id             : \"Question id\",\n"
            + "    title          : \"Question title\",\n"
            + "    body           : \"Question body\",\n"
            + "    status         : \"Question status\",\n"
            + "    deleted        : \"Question is deleted\",\n"
            + "    systemFrom     : \"Question system From\",\n"
            + "    systemTo       : \"Question system To\",\n"
            + "    createdById    : \"Question created by ID\",\n"
            + "    createdOn      : \"Question created on\",\n"
            + "    lastUpdatedById: \"Question last updated by ID\",\n"
            + "\n"
            + "    answers        :\n"
            + "    {\n"
            + "        id  : \"Answer id\",\n"
            + "        body: \"Answer body\",\n"
            + "    },\n"
            + "\n"
            + "    version        :\n"
            + "    {\n"
            + "        number: \"Question version number\",\n"
            + "    },\n"
            + "}\n"
            + "\n"
            + "projection QuestionWriteProjection on Question\n"
            + "{\n"
            + "    title: \"Question title\",\n"
            + "    body : \"Question body\",\n"
            + "}\n"
            + "\n"
            + "// TODO: Type inference on projection paramters\n"
            + "/*\n"
            + "projection FilteredAnswersProjection(substring: String[1..1]) on Question\n"
            + "{\n"
            + "    title                          : \"Question title\",\n"
            + "    body                           : \"Question body\",\n"
            + "    answersWithSubstring(substring):\n"
            + "    {\n"
            + "        body: \"Answer body\",\n"
            + "    },\n"
            + "}\n"
            + "*/\n"
            + "\n"
            + "// Just embed inside the Question class?\n"
            + "service Question\n"
            + "{\n"
            + "    // TODO: matrix url params\n"
            + "    /*\n"
            + "        /api/question/{id: Long[1..1]}/{substring: String[1..1]}\n"
            + "            GET\n"
            + "            {\n"
            + "                multiplicity: many\n"
            + "                criteria    : this.id == id\n"
            + "                projection  : FilteredAnswersProjection(substring)\n"
            + "            }\n"
            + "        /api/question/{titleSubstring: String[1..1]}\n"
            + "            GET\n"
            + "            {\n"
            + "                multiplicity: many\n"
            + "                criteria    : this.title startsWith titleSubstring\n"
            + "                projection  : QuestionReadProjection\n"
            + "            }\n"
            + "    */\n"
            + "    // TODO: Service syntax\n"
            + "    /*\n"
            + "    service getById(id: Long[1..1]): QuestionReadProjection[1]\n"
            + "    {\n"
            + "        resource: QuestionResource\n"
            + "        verb    : GET\n"
            + "        url     : /api/question/{id: Long[1..1]}\n"
            + "        criteria: this.id == id\n"
            + "    }\n"
            + "    */\n"
            + "\n"
            + "    // TODO: Consider forcing exact type matches. Meaning id's type here would be ID, not Long.\n"
            + "    /api/question/{id: Long[1..1]}\n"
            + "        GET\n"
            + "        {\n"
            + "            multiplicity: one;\n"
            + "            criteria    : this.id == id;\n"
            + "            projection  : QuestionReadProjection;\n"
            + "            // TODO: format: json\n"
            + "        }\n"
            + "        PUT\n"
            + "        {\n"
            + "            // PUT, PATCH, and DELETE should implicitly get ?{version: Integer[1..1]} due to optimistic locking\n"
            + "            // TODO Version should be a primitive type, as in {version: Version[1..1]}\n"
            + "            multiplicity: one;\n"
            + "            criteria    : this.id == id;\n"
            + "            // TODO: Consider 'conflict' 409, 412 Precondition Failed, or 417 Expectation Failed\n"
            + "            // TODO: Allow anonymous/undeclared projections created from all owned association ends\n"
            + "            projection  : QuestionWriteProjection;\n"
            + "        }\n"
            + "        DELETE\n"
            + "        {\n"
            + "            // TODO\n"
            + "            // PUT, PATCH, and DELETE should implicitly get ?version={version} due to optimistic locking\n"
            + "            multiplicity: one;\n"
            + "            criteria    : this.id == id;\n"
            + "            // 'authorize' is like 'validate' but should give 401 Unauthorized or 403 forbidden instead of 400\n"
            + "            // TODO: conditions implemented in code, like `|| native(UserIsModeratorClass)`\n"
            + "            authorize   : this.createdById == user;\n"
            + "            projection  : QuestionWriteProjection;\n"
            + "        }\n"
            + "    /api/question/in?{ids: Long[0..*]}\n"
            + "        GET\n"
            + "        {\n"
            + "            multiplicity: many;\n"
            + "            criteria    : this.id in ids;\n"
            + "            projection  : QuestionReadProjection;\n"
            + "        }\n"
            + "    /api/question/firstTwo\n"
            + "        GET\n"
            + "        {\n"
            + "            // TODO: Warn if multiplicity is one and criteria uses in clause, or anything other than equality on a unique property\n"
            + "            multiplicity: many;\n"
            + "            criteria    : this.id in (1, 2);\n"
            + "            projection  : QuestionReadProjection;\n"
            + "        }\n"
            + "    /api/question\n"
            + "        POST\n"
            + "        {\n"
            + "            multiplicity: one;\n"
            + "            projection  : QuestionWriteProjection;\n"
            + "        }\n"
            + "        GET\n"
            + "        {\n"
            + "            multiplicity: many;\n"
            + "            criteria    : this.title startsWith \"Why do\";\n"
            + "            projection  : QuestionReadProjection;\n"
            + "        }\n"
            + "\n"
            + "    // TODO: Type inference on url parameters\n"
            + "    // TODO: Consider adding a primitive type UserId\n"
            + "    /api/user/{userId: String[1..1]}/questions\n"
            + "        GET\n"
            + "        {\n"
            + "            //?page=1&pageSize=25\n"
            + "            multiplicity: many;\n"
            + "            criteria    : this.createdById == userId;\n"
            + "            projection  : QuestionWriteProjection;\n"
            + "\n"
            + "            // Deliberately shallow\n"
            + "            orderBy     : this.createdOn;\n"
            + "\n"
            + "            // pageSize: ???\n"
            + "            // page: ???\n"
            + "        }\n"
            + "}\n";
    //</editor-fold>

    private KlassTestConstants()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }
}
