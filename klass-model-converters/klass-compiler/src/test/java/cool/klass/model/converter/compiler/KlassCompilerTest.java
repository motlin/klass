package cool.klass.model.converter.compiler;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.annotation.AbstractCompilerAnnotation;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.test.constants.KlassTestConstants;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class KlassCompilerTest
{
    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void stackOverflow()
    {
        this.assertNoCompilerErrors(KlassTestConstants.STACK_OVERFLOW_SOURCE_CODE_TEXT);
    }

    @Test
    public void meta()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = """
                package klass.model.meta.domain
                interface Element
                {
                }
                interface NamedElement
                    implements Element
                {
                    name: String key maximumLength(256);
                    ordinal: Integer;
                }
                interface PackageableElement implements NamedElement
                {
                    packageName: String maximumLength(100000);
                    // fullyQualifiedName: String = packageName + "." + name;
                }
                class Enumeration implements PackageableElement
                {
                }
                class EnumerationLiteral implements NamedElement
                {
                    enumerationName               : String key maximumLength(256);
                    prettyName                    : String? maximumLength(256);
                }
                association EnumerationHasLiterals
                {
                    enumeration: Enumeration[1..1];
                    enumerationLiterals: EnumerationLiteral[1..*]
                        orderBy: this.ordinal;
                }
                enumeration InheritanceType
                {
                    TABLE_PER_SUBCLASS("table-per-subclass"),
                    TABLE_FOR_ALL_SUBCLASSES("table-for-all-subclasses"),
                    TABLE_PER_CLASS("table-per-class"),
                    NONE("none"),
                }
                // TODO: If key properties are present, may be table-per-class, otherwise table-per-subclass
                // TODO: Ordinals should have a syntax and be inferred using macros
                class Classifier
                    abstract(table-per-class)
                    implements PackageableElement
                {
                }
                // TODO: Error when transient extends non-transient
                class Interface
                    extends Classifier
                {
                }
                class ClassifierInterfaceMapping
                {
                    classifierName: String key private maximumLength(256);
                    interfaceName: String key private maximumLength(256);
                }
                association ClassifierHasClassifierInterfaceMapping
                {
                    subClassifier: Classifier[1..1];
                    superInterfaces               : ClassifierInterfaceMapping[0..*] owned;
                }
                association ClassifierInterfaceMappingHasInterface
                {
                    superInterface                : Interface[1..1];
                    subClassifiers                : ClassifierInterfaceMapping[0..*];
                }
                class Klass
                    extends Classifier
                {
                    superClassName: String? private maximumLength(256);
                    inheritanceType: InheritanceType maximumLength(256);
                }
                // TODO: Why isn't subClasses showing up in generated code? Reladomo bug?
                association ClassHasSuperClass
                {
                    subClasses                    : Klass[0..*];
                    superClass                    : Klass[0..1];
                    // TODO: When this was backwards, I got a confusing error. How can I detect and prevent?
                    relationship this.superClassName == Klass.name
                }
                enumeration PrimitiveType
                {
                    INTEGER("Integer"),
                    LONG("Long"),
                    DOUBLE("Double"),
                    FLOAT("Float"),
                    BOOLEAN("Boolean"),
                    STRING("String"),
                    INSTANT("Instant"),
                    LOCAL_DATE("LocalDate"),
                    TEMPORAL_INSTANT("TemporalInstant"),
                    TEMPORAL_RANGE("TemporalRange"),
                }
                enumeration Multiplicity
                {
                    ZERO_TO_ONE("0..1"),
                    ONE_TO_ONE("1..1"),
                    ZERO_TO_MANY("0..*"),
                    ONE_TO_MANY("1..*"),
                }
                class DataTypeProperty
                    abstract(table-per-class)
                    implements NamedElement
                {
                    classifierName                : String key maximumLength(256);
                    optional                      : Boolean;
                }
                class PrimitiveProperty
                    extends DataTypeProperty
                {
                    primitiveType                 : PrimitiveType maximumLength(256);
                }
                class EnumerationProperty
                    extends DataTypeProperty
                {
                    enumerationName               : String private maximumLength(256);
                }
                association EnumerationPropertyHasEnumeration
                {
                    enumerationProperties         : EnumerationProperty[0..*];
                    enumeration                   : Enumeration[1..1];
                }
                // TODO: Change the orderBy syntax to orderBy(this.ordinal)
                /*
                association ClassifierHasDataTypeTypeProperties
                {
                    owningClassifier              : Classifier[1..1];
                    dataTypeProperties            : DataTypeProperty[0..*] owned
                        orderBy: this.ordinal;
                    relationship this.name == DataTypeProperty.classifierName
                }
                */
                // TODO: Owned
                association ClassifierHasPrimitiveProperties
                {
                    owningClassifier: Classifier[1..1];
                    primitiveProperties: PrimitiveProperty[0..*]
                        orderBy: this.ordinal;
                }
                // TODO: Owned
                association ClassifierHasEnumerationProperties
                {
                    owningClassifier: Classifier[1..1];
                    enumerationProperties: EnumerationProperty[0..*]
                        orderBy: this.ordinal;
                }
                interface PropertyValidation implements Element
                {
                }
                class NumericPropertyValidation
                    abstract(table-per-subclass)
                    implements PropertyValidation
                {
                    classifierName                : String key maximumLength(256);
                    propertyName                  : String key maximumLength(256);
                    number: Integer;
                }
                class MinLengthPropertyValidation
                    extends NumericPropertyValidation
                {
                }
                class MaxLengthPropertyValidation
                    extends NumericPropertyValidation
                {
                }
                class MinPropertyValidation
                    extends NumericPropertyValidation
                {
                }
                class MaxPropertyValidation
                    extends NumericPropertyValidation
                {
                }
                association DataTypePropertyHasMinLengthPropertyValidation
                {
                    dataTypeProperty              : DataTypeProperty[1..1];
                    minLengthValidation           : MinLengthPropertyValidation[0..1];
                    relationship this.classifierName == MinLengthPropertyValidation.classifierName
                            && this.name == MinLengthPropertyValidation.propertyName
                }
                association DataTypePropertyHasMaxLengthPropertyValidation
                {
                    dataTypeProperty              : DataTypeProperty[1..1];
                    maxLengthValidation           : MaxLengthPropertyValidation[0..1];
                    relationship this.classifierName == MaxLengthPropertyValidation.classifierName
                            && this.name == MaxLengthPropertyValidation.propertyName
                }
                association PrimitivePropertyHasMinPropertyValidation
                {
                    primitiveProperty             : PrimitiveProperty[1..1];
                    minValidation                 : MinPropertyValidation[0..1];
                    relationship this.classifierName == MinPropertyValidation.classifierName
                            && this.name == MinPropertyValidation.propertyName
                }
                association PrimitivePropertyHasMaxPropertyValidation
                {
                    primitiveProperty             : PrimitiveProperty[1..1];
                    maxValidation                 : MaxPropertyValidation[0..1];
                    relationship this.classifierName == MaxPropertyValidation.classifierName
                            && this.name == MaxPropertyValidation.propertyName
                }
                class PropertyModifier
                    implements Element
                {
                    keyword: String key maximumLength(256);
                    ordinal: Integer;
                    classifierName: String key maximumLength(256);
                    propertyName: String key maximumLength(256);
                }
                association DataTypePropertyHasModifiers
                {
                    dataTypeProperty              : DataTypeProperty[1..1];
                    propertyModifiers             : PropertyModifier[0..*]
                        orderBy: this.ordinal;
                    relationship this.classifierName == PropertyModifier.classifierName
                            && this.name == PropertyModifier.propertyName
                }
                class ClassifierModifier
                    implements Element
                {
                    keyword: String key maximumLength(256);
                    ordinal: Integer;
                    classifierName: String key maximumLength(256);
                }
                association ClassifierHasModifiers
                {
                    owningClassifier: Classifier[1..1];
                    classifierModifiers: ClassifierModifier[0..*]
                        orderBy: this.ordinal;
                }
                class Parameter
                    abstract(table-per-class)
                    implements NamedElement
                {
                    // name isn't key
                    name: String maximumLength(256);
                    id: Long key id;
                    multiplicity: Multiplicity maximumLength(256);
                }
                class EnumerationParameter
                    extends Parameter
                {
                    id  : Long key private;
                    enumerationName: String private maximumLength(256);
                }
                class PrimitiveParameter
                    extends Parameter
                {
                    id  : Long key private;
                    primitiveType: PrimitiveType maximumLength(256);
                }
                class ExpressionValue
                    abstract(table-per-class)
                {
                    id      : Long key id;
                }
                class UserLiteral
                    extends ExpressionValue
                {
                    id  : Long key private;
                }
                class NullLiteral
                    extends ExpressionValue
                {
                    id  : Long key private;
                }
                class VariableReference
                    extends ExpressionValue
                {
                    id  : Long key private;
                    parameterId: Long private;
                }
                class MemberReferencePath
                    abstract(table-per-class)
                    extends ExpressionValue
                {
                    id  : Long key private;
                    className: String private maximumLength(256);
                    propertyClassName: String private maximumLength(256);
                    propertyName: String private maximumLength(256);
                }
                association VariableReferenceHasParameter
                {
                    variableReferences: VariableReference[0..*];
                    parameter: Parameter[1..1];
                }
                class Criteria
                    abstract(table-per-class)
                    implements Element
                {
                    id: Long id key;
                }
                class AllCriteria
                    extends Criteria
                {
                    id  : Long key private;
                }
                class BinaryCriteria
                    abstract(table-per-class)
                    extends Criteria
                {
                    id  : Long key private;
                    leftId        : Long private;
                    rightId       : Long private;
                }
                class AndCriteria
                    extends BinaryCriteria
                {
                    id  : Long key private;
                }
                class OrCriteria
                    extends BinaryCriteria
                {
                    id  : Long key private;
                }
                enumeration Operator
                {
                    EQUALS("=="),
                    NOT_EQUALS("!="),
                    LESS_THAN("<"),
                    GREATER_THAN(">"),
                    LESS_THAN_EQUAL("<="),
                    GREATER_THAN_EQUAL(">="),
                    IN("in"),
                    CONTAINS("contains"),
                    STARTS_WITH("startsWith"),
                    ENDS_WITH("endsWith"),
                }
                class OperatorCriteria
                    extends Criteria
                {
                    id  : Long key private;
                    operator: Operator maximumLength(256);
                    sourceExpressionId: Long private;
                    targetExpressionId: Long private;
                }
                class EdgePointCriteria
                    extends Criteria
                {
                    id  : Long key private;
                    memberReferencePathId: Long private;
                }
                enumeration AssociationEndDirection
                {
                    SOURCE("source"),
                    TARGET("target"),
                }
                class Association implements PackageableElement
                {
                    criteriaId: Long private;
                    source(): AssociationEnd[1..1]
                    {
                        this.name == AssociationEnd.associationName
                            && AssociationEnd.direction == AssociationEndDirection.SOURCE
                    }
                    target(): AssociationEnd[1..1]
                    {
                        this.name == AssociationEnd.associationName
                            && AssociationEnd.direction == AssociationEndDirection.TARGET
                    }
                }
                association AssociationHasCriteria
                {
                    association: Association[0..1];
                    criteria: Criteria[1..1];
                    relationship this.criteriaId == Criteria.id
                }
                class AssociationEnd implements NamedElement
                {
                    owningClassName               : String key private maximumLength(256);
                    associationName               : String maximumLength(256);
                    direction                     : AssociationEndDirection maximumLength(256);
                    multiplicity                  : Multiplicity maximumLength(256);
                    resultTypeName                : String private maximumLength(256);
                }
                // TODO: Consider moving the foreign keys onto Association (sourceClassName, sourceName, targetClassName, targetClass)
                // simplification, ideally we'd model an association as having exactly two ends
                association AssociationHasEnds
                {
                    owningAssociation: Association[1..1];
                    associationEnds: AssociationEnd[0..*]
                        orderBy: this.direction;
                }
                association ClassHasAssociationEnds
                {
                    owningClass: Klass[1..1];
                    associationEnds: AssociationEnd[0..*];
                    // TODO: Order by this.owningAssociation.ordinal
                    relationship this.name == AssociationEnd.owningClassName
                }
                association AssociationEndHasResultType
                {
                    associationEndsResultTypeOf: AssociationEnd[0..*];
                    resultType: Klass[1..1];
                    relationship this.resultTypeName == Klass.name
                }
                class AssociationEndModifier
                    implements Element
                {
                    keyword           : String key maximumLength(256);
                    ordinal           : Integer;
                    owningClassName   : String key maximumLength(256);
                    associationEndName: String key maximumLength(256);
                }
                association AssociationEndHasModifiers
                {
                    associationEnd: AssociationEnd[1..1];
                    associationEndModifiers: AssociationEndModifier[0..*]
                        orderBy: this.ordinal;
                    relationship this.owningClassName == AssociationEndModifier.owningClassName
                            && this.name == AssociationEndModifier.associationEndName
                }
                association OperatorCriteriaHasSourceExpressionValue
                {
                    operatorCriteriaSourceOf: OperatorCriteria[0..1];
                    sourceExpressionValue: ExpressionValue[1..1];
                    relationship this.sourceExpressionId == ExpressionValue.id
                }
                association OperatorCriteriaHasTargetExpressionValue
                {
                    operatorCriteriaTargetOf: OperatorCriteria[0..1];
                    targetExpressionValue: ExpressionValue[1..1];
                    relationship this.targetExpressionId == ExpressionValue.id
                }
                association MemberReferencePathHasEdgePointCriteria
                {
                    memberReferencePath: MemberReferencePath[1..1];
                    edgePointCriteria: EdgePointCriteria[1..1] owned;
                }
                association EnumerationParameterHasEnumeration
                {
                    enumerationParameters: EnumerationParameter[0..*];
                    enumeration: Enumeration[1..1];
                }
                association MemberReferencePathHasClass
                {
                    memberReferencePaths: MemberReferencePath[0..*];
                    klass: Klass[1..1];
                    relationship this.className == Klass.name
                }
                class MemberReferencePathAssociationEndMapping
                {
                    memberReferencePathId: Long key private;
                    associationOwningClassName: String key private maximumLength(256);
                    associationEndName: String key private maximumLength(256);
                }
                association MemberReferencePathHasMemberReferencePathAssociationEndMapping
                {
                    memberReferencePath: MemberReferencePath[1..1];
                    associationEnds: MemberReferencePathAssociationEndMapping[0..*];
                }
                association MemberReferencePathAssociationEndMappingHasAssociationEnd
                {
                    memberReferencePaths: MemberReferencePathAssociationEndMapping[0..*];
                    associationEnd: AssociationEnd[1..1];
                    relationship this.associationOwningClassName == AssociationEnd.owningClassName
                            && this.associationEndName == AssociationEnd.name
                }
                // TODO: These associations only work in one direction
                association DataTypePropertyHasMemberReferencePaths
                {
                    memberReferencePaths: MemberReferencePath[0..*];
                    dataTypeProperty: DataTypeProperty[1..1];
                    relationship this.propertyClassName == DataTypeProperty.classifierName
                            && this.propertyName == DataTypeProperty.name
                }
                class ThisMemberReferencePath
                    extends MemberReferencePath
                {
                    id  : Long key private;
                }
                class TypeMemberReferencePath
                    extends MemberReferencePath
                {
                    id  : Long key private;
                }
                association BinaryCriteriaHasLeft
                {
                    ownerOfLeftCriteria : BinaryCriteria[0..1];
                    left                : Criteria[1..1];
                    relationship this.leftId == Criteria.id
                }
                association BinaryCriteriaHasRight
                {
                    ownerOfRightCriteria: BinaryCriteria[0..1];
                    right               : Criteria[1..1];
                    relationship this.rightId == Criteria.id
                }
                class ParameterizedProperty
                    implements NamedElement
                {
                    owningClassName               : String key private maximumLength(256);
                    multiplicity                  : Multiplicity maximumLength(256);
                    resultTypeName                : String private maximumLength(256);
                }
                association ClassHasParameterizedProperties
                {
                    owningClass: Klass[1..1];
                    parameterizedProperties: ParameterizedProperty[0..*] owned
                    // TODO: Sorting by the same property ascending and descending is an interesting test, move it out of the meta model
                        orderBy: this.ordinal ascending, this.ordinal descending;
                    relationship this.name == ParameterizedProperty.owningClassName
                }
                enumeration OrderByDirection
                {
                    ASCENDING("ascending"),
                    DESCENDING("descending"),
                }
                class AssociationEndOrderBy
                {
                    associationEndClassName: String key private maximumLength(256);
                    associationEndName: String key private maximumLength(256);
                    thisMemberReferencePathId: Long key private;
                    orderByDirection: OrderByDirection maximumLength(256);
                }
                association AssociationEndHasOrderBy
                {
                    associationEnd: AssociationEnd[1..1];
                    orderBys: AssociationEndOrderBy[0..*];
                    relationship this.owningClassName == AssociationEndOrderBy.associationEndClassName
                            && this.name == AssociationEndOrderBy.associationEndName
                }
                association AssociationEndOrderByHasMemberReferencePath
                {
                    associationEndOrderBy: AssociationEndOrderBy[0..1];
                    thisMemberReferencePath: ThisMemberReferencePath[1..1];
                    relationship this.thisMemberReferencePathId == ThisMemberReferencePath.id
                }
                association ParameterizedPropertyHasResultType
                {
                    parameterizedPropertiesResultTypeOf: ParameterizedProperty[0..*];
                    resultType: Klass[1..1];
                    relationship this.resultTypeName == Klass.name
                }
                class ParameterizedPropertyOrdering
                {
                    owningClassName               : String private key maximumLength(256);
                    name                          : String key maximumLength(256);
                    orderingId                    : Long; // move this to an orderBy and make it private
                }
                association ParameterizedPropertyHasOrdering
                {
                    parameterizedProperty: ParameterizedProperty[1..1];
                    parameterizedPropertyOrderings: ParameterizedPropertyOrdering[0..*];
                    relationship this.owningClassName == ParameterizedPropertyOrdering.owningClassName
                            && this.name == ParameterizedPropertyOrdering.name
                }
                class ParameterizedPropertyParameter implements NamedElement
                {
                    classifierName                : String key private maximumLength(256);
                    parameterizedPropertyName     : String key maximumLength(256);
                }
                association ParameterizedPropertyHasParameters
                {
                    parameterizedProperty         : ParameterizedProperty[1..1];
                    parameters                    : ParameterizedPropertyParameter[0..*];
                    relationship this.owningClassName == ParameterizedPropertyParameter.classifierName
                            && this.name == ParameterizedPropertyParameter.parameterizedPropertyName
                }
                class ProjectionElement
                    abstract(table-per-class)
                    implements NamedElement
                {
                    // TODO: Write a test that fails if name is a key
                    // name isn't key
                    name: String maximumLength(256);
                    id  : Long key id;
                    parentId: Long? private;
                }
                // TODO: Rename to Projection, but clashes with names in generated code.
                class ServiceProjection
                    extends ProjectionElement
                    implements PackageableElement
                {
                    id  : Long key private;
                    // TODO: unique constraint on name
                    className                     : String private maximumLength(256);
                }
                // TODO: When abstract class extends abstract class, validate that they have compatible inheritance types
                class ProjectionWithAssociationEnd
                    abstract(table-per-class)
                    extends ProjectionElement
                {
                    id  : Long key private;
                    parentId                      : Long private;
                    associationEndClass           : String private maximumLength(256);
                    associationEndName: String private maximumLength(256);
                }
                class ProjectionReferenceProperty
                    extends ProjectionWithAssociationEnd
                {
                    id  : Long key private;
                    parentId                      : Long private;
                    associationEndClass           : String private maximumLength(256);
                    associationEndName: String private maximumLength(256);
                }
                class ProjectionProjectionReference
                    extends ProjectionWithAssociationEnd
                {
                    id  : Long key private;
                    parentId                      : Long private;
                    associationEndClass           : String private maximumLength(256);
                    associationEndName: String private maximumLength(256);
                    projectionName                  : String private maximumLength(256);
                }
                class ProjectionDataTypeProperty
                    extends ProjectionElement
                {
                    id  : Long key private;
                    parentId                      : Long private;
                    propertyClassifierName        : String private maximumLength(256);
                    propertyName: String private maximumLength(256);
                }
                association ProjectionElementHasChildren
                {
                    // TODO: Validate that foreign key is optional if association end is optional and vice versa
                    parent                        : ProjectionElement[0..1];
                    children                      : ProjectionElement[0..*];
                    relationship this.id == ProjectionElement.parentId
                }
                association ProjectionWithAssociationEndHasAssociationEnd
                {
                    projectionsWithAssociationEnd : ProjectionWithAssociationEnd[0..*];
                    associationEnd                : AssociationEnd[1..1];
                    relationship this.associationEndClass == AssociationEnd.owningClassName
                            && this.associationEndName == AssociationEnd.name
                }
                association ProjectionDataTypePropertyHasDataTypeProperty
                {
                    projectionDataTypeProperties  : ProjectionDataTypeProperty[0..*];
                    dataTypeProperty              : DataTypeProperty[1..1];
                    relationship this.propertyClassifierName == DataTypeProperty.classifierName
                            && this.propertyName == DataTypeProperty.name
                }
                association ProjectionHasClass
                {
                    projections                   : ServiceProjection[0..*];
                    klass                         : Klass[1..1];
                    relationship this.className == Klass.name
                }
                association ProjectionProjectionReferenceHasProjection
                {
                    projectionProjectionReferences: ProjectionProjectionReference[0..*];
                    projection                    : ServiceProjection[1..1];
                    relationship this.projectionName == ServiceProjection.name
                }
                class ServiceGroup
                    implements PackageableElement
                {
                    className                     : String key maximumLength(256);
                }
                association ServiceGroupHasClass
                {
                    serviceGroup                  : ServiceGroup[0..1];
                    owningClass                   : Klass[1..1];
                    relationship this.className == Klass.name
                }
                class Url
                    implements Element
                {
                    className                     : String key maximumLength(256);
                    url                           : String key maximumLength(8192);
                }
                enumeration UrlParameterType
                {
                    QUERY("query"),
                    PATH("path"),
                }
                class UrlParameter
                    implements Element
                {
                    urlClassName                  : String key private maximumLength(256);
                    urlString                     : String key private maximumLength(8192);
                    parameterId                   : Long key private;
                    type                          : UrlParameterType;
                }
                association UrlHasUrlParameters
                {
                    url: Url[1..1];
                    parameters                    : UrlParameter[0..*];
                    relationship this.className == UrlParameter.urlClassName
                            && this.url == UrlParameter.urlString
                }
                association UrlParameterHasParameter
                {
                    urlParameter                  : UrlParameter[0..1];
                    parameter                     : Parameter[1..1];
                    relationship this.parameterId == Parameter.id
                }
                association ServiceGroupHasUrls
                {
                    serviceGroup                  : ServiceGroup[1..1];
                    urls                          : Url[1..*];
                    relationship this.className == Url.className
                }
                enumeration ServiceMultiplicity
                {
                    ONE("one"),
                    MANY("many"),
                }
                enumeration Verb
                {
                    GET,
                    POST,
                    PUT,
                    PATCH,
                    DELETE,
                }
                class Service
                    implements Element
                {
                    className                     : String key maximumLength(256);
                    urlString                     : String key maximumLength(8192);
                    verb                          : Verb key maximumLength(256);
                    serviceMultiplicity           : ServiceMultiplicity maximumLength(256);
                    projectionName                : String? private maximumLength(256);
                    queryCriteriaId               : Long? private;
                    authorizeCriteriaId           : Long? private;
                    validateCriteriaId            : Long? private;
                    conflictCriteriaId            : Long? private;
                }
                association UrlHasServices
                {
                    url: Url[1..1];
                    services                      : Service[1..*];
                    relationship this.className == Service.className
                            && this.url == Service.urlString
                }
                association ServiceHasProjection
                {
                    services: Service[0..*];
                    projection                    : ServiceProjection[0..1];
                    relationship this.projectionName == ServiceProjection.name
                }
                association ServiceHasQueryCriteria
                {
                    queryService                  : Service[0..1];
                    queryCriteria                 : Criteria[0..1] owned;
                    relationship this.queryCriteriaId == Criteria.id
                }
                association ServiceHasAuthorizeCriteria
                {
                    authorizeService                  : Service[0..1];
                    authorizeCriteria                 : Criteria[0..1] owned;
                    relationship this.authorizeCriteriaId == Criteria.id
                }
                association ServiceHasValidateCriteria
                {
                    validateService                  : Service[0..1];
                    validateCriteria                 : Criteria[0..1] owned;
                    relationship this.validateCriteriaId == Criteria.id
                }
                association ServiceHasConflictCriteria
                {
                    conflictService                  : Service[0..1];
                    conflictCriteria                 : Criteria[0..1] owned;
                    relationship this.conflictCriteriaId == Criteria.id
                }
                class ServiceOrderBy
                {
                    serviceClassName: String key private maximumLength(256);
                    serviceUrlString: String key private maximumLength(8192);
                    serviceVerb: Verb key private;
                    thisMemberReferencePathId     : Long key private;
                    orderByDirection: OrderByDirection;
                }
                association ServiceHasOrderBy
                {
                    service                       : Service[1..1];
                    orderBys                      : ServiceOrderBy[0..*];
                }
                association ServiceOrderByHasMemberReferencePath
                {
                    serviceOrderBy                : ServiceOrderBy[0..1];
                    thisMemberReferencePath       : ThisMemberReferencePath[1..1];
                    relationship this.thisMemberReferencePathId == ThisMemberReferencePath.id
                }
                projection EnumerationLiteralProjection on EnumerationLiteral
                {
                    name                   : "Enumeration literal name",
                    prettyName             : "Enumeration literal pretty name",
                    ordinal                : "Enumeration literal ordinal",
                }
                projection EnumerationProjection on Enumeration
                {
                    name                   : "Enumeration name",
                    packageName            : "Enumeration package name",
                    ordinal                : "Enumeration ordinal",
                    enumerationLiterals: EnumerationLiteralProjection,
                }
                projection EnumerationSummaryProjection on Enumeration
                {
                    name               : "Enumeration name",
                    packageName        : "Enumeration package name",
                    enumerationLiterals:
                    {
                        name      : "Enumeration literal name",
                        prettyName: "Enumeration literal pretty name",
                    },
                }
                service Enumeration
                {
                    /meta/enumeration/{enumerationName: String[1..1]}
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == enumerationName;
                            projection  : EnumerationProjection;
                        }
                    /meta/enumeration/{enumerationName: String[1..1]}/summary
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == enumerationName;
                            projection  : EnumerationSummaryProjection;
                        }
                    /meta/enumeration
                        GET
                        {
                            multiplicity: many;
                            criteria    : all;
                            projection  : EnumerationSummaryProjection;
                            orderBy     : this.ordinal;
                        }
                }
                projection InterfaceProjection on Interface
                {
                    name                   : "Interface name",
                    packageName            : "Interface package name",
                    ordinal                : "Interface ordinal",
                    // TODO: flat many-to-many, or parameterized property
                    superInterfaces        :
                    {
                        superInterface:
                        {
                            name           : "Super interface name",
                            packageName    : "Super interface package name",
                        },
                    },
                    classifierModifiers    :
                    {
                        keyword: "Interface modifier keyword",
                        ordinal: "Interface modifier ordinal",
                    },
                    primitiveProperties    :
                    {
                        name               : "Primitive property name",
                        primitiveType      : "Primitive property type",
                        optional           : "Primitive property is optional",
                        ordinal            : "Primitive property ordinal",
                        propertyModifiers  :
                        {
                            keyword: "Primitive property modifier keyword",
                            ordinal: "Primitive property modifier ordinal",
                        },
                        minLengthValidation:
                        {
                            number: "Min length validation number",
                        },
                        maxLengthValidation:
                        {
                            number: "Max length validation number",
                        },
                        minValidation      :
                        {
                            number: "Min validation number",
                        },
                        maxValidation      :
                        {
                            number: "Max validation number",
                        },
                    },
                    enumerationProperties  :
                    {
                        name               : "Enumeration property name",
                        optional           : "Enumeration property is optional",
                        ordinal            : "Enumeration property ordinal",
                        enumeration        :
                        {
                            name    : "Enumeration name",
                        },
                        propertyModifiers  :
                        {
                            keyword: "Enumeration property modifier keyword",
                            ordinal: "Enumeration property modifier ordinal",
                        },
                        minLengthValidation:
                        {
                            number: "Min length validation number",
                        },
                        maxLengthValidation:
                        {
                            number: "Max length validation number",
                        },
                    },
                }
                projection InterfaceSummaryProjection on Interface
                {
                    name                   : "Interface name",
                    packageName            : "Interface package name",
                    superInterfaces        :
                    {
                        superInterface:
                        {
                            name           : "Super interface name",
                        },
                    },
                    classifierModifiers    :
                    {
                        keyword: "Interface modifier keyword",
                    },
                    primitiveProperties    : PrimitivePropertySummaryProjection,
                    enumerationProperties  : EnumerationPropertySummaryProjection,
                }
                service Interface
                {
                    /meta/interface/{interfaceName: String[1..1]}
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == interfaceName;
                            projection  : InterfaceProjection;
                        }
                    /meta/interface/{interfaceName: String[1..1]}/summary
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == interfaceName;
                            projection  : InterfaceSummaryProjection;
                        }
                    /meta/interface
                        GET
                        {
                            multiplicity: many;
                            criteria    : all;
                            projection  : InterfaceSummaryProjection;
                            orderBy     : this.ordinal;
                        }
                }
                projection DataTypePropertyProjection on DataTypeProperty
                {
                    name                           : "Property name",
                    PrimitiveProperty.primitiveType: "Property primitive type",
                    optional                       : "Property is optional",
                    ordinal                        : "Property ordinal",
                    EnumerationProperty.enumeration:
                    {
                        name    : "Property Enumeration name",
                    },
                    propertyModifiers              :
                    {
                        keyword: "Property modifier keyword",
                        ordinal: "Property modifier ordinal",
                    },
                    minLengthValidation            :
                    {
                        number: "Property min length validation number",
                    },
                    maxLengthValidation            :
                    {
                        number: "Property max length validation number",
                    },
                    PrimitiveProperty.minValidation:
                    {
                        number: "Property min validation number",
                    },
                    PrimitiveProperty.maxValidation:
                    {
                        number: "Property max validation number",
                    },
                }
                projection PrimitivePropertyProjection on PrimitiveProperty
                {
                    name               : "Primitive property name",
                    primitiveType      : "Primitive property type",
                    optional           : "Primitive property is optional",
                    ordinal            : "Primitive property ordinal",
                    propertyModifiers  :
                    {
                        keyword: "Primitive property modifier keyword",
                        ordinal: "Primitive property modifier ordinal",
                    },
                    minLengthValidation:
                    {
                        number: "Min length validation number",
                    },
                    maxLengthValidation:
                    {
                        number: "Max length validation number",
                    },
                    minValidation      :
                    {
                        number: "Min validation number",
                    },
                    maxValidation      :
                    {
                        number: "Max validation number",
                    },
                }
                projection PrimitivePropertySummaryProjection on PrimitiveProperty
                {
                    name               : "Primitive property name",
                    primitiveType      : "Primitive property type",
                    optional           : "Primitive property is optional",
                    propertyModifiers  :
                    {
                        keyword: "Primitive property modifier keyword",
                    },
                    minLengthValidation:
                    {
                        number: "Min length validation number",
                    },
                    maxLengthValidation:
                    {
                        number: "Max length validation number",
                    },
                    minValidation      :
                    {
                        number: "Min validation number",
                    },
                    maxValidation      :
                    {
                        number: "Max validation number",
                    },
                }
                projection EnumerationPropertyProjection on EnumerationProperty
                {
                    name               : "Enumeration property name",
                    optional           : "Enumeration property is optional",
                    ordinal            : "Enumeration property ordinal",
                    enumeration        :
                    {
                        name    : "Enumeration name",
                    },
                    propertyModifiers  :
                    {
                        keyword: "Enumeration property modifier keyword",
                        ordinal: "Enumeration property modifier ordinal",
                    },
                    minLengthValidation:
                    {
                        number: "Min length validation number",
                    },
                    maxLengthValidation:
                    {
                        number: "Max length validation number",
                    },
                }
                projection EnumerationPropertySummaryProjection on EnumerationProperty
                {
                    name               : "Enumeration property name",
                    optional           : "Enumeration property is optional",
                    enumeration        :
                    {
                        name    : "Enumeration name",
                    },
                    propertyModifiers  :
                    {
                        keyword: "Enumeration property modifier keyword",
                    },
                    minLengthValidation:
                    {
                        number: "Min length validation number",
                    },
                    maxLengthValidation:
                    {
                        number: "Max length validation number",
                    },
                }
                projection ClassProjection on Klass
                {
                    name                   : "Class name",
                    packageName            : "Class package name",
                    inheritanceType        : "Class inheritanceType",
                    ordinal                : "Class ordinal",
                    superClass             :
                    {
                        name           : "Super Class name",
                        packageName    : "Super Class package name",
                        superClass     :
                        {
                            name       : "Super Super Class name",
                            packageName: "Super Super Class package name",
                        },
                        // TODO: flat many-to-many, or parameterized property
                        superInterfaces:
                        {
                            superInterface:
                            {
                                name       : "Super Class Super Interface name",
                                packageName: "Super Class Super Interface package name",
                            },
                        },
                    },
                    // TODO: flat many-to-many, or parameterized property
                    superInterfaces        :
                    {
                        superInterface:
                        {
                            name           : "Super interface name",
                            packageName    : "Super interface package name",
                        },
                    },
                    classifierModifiers    :
                    {
                        keyword: "Classifier modifier keyword",
                        ordinal: "Classifier modifier ordinal",
                    },
                    primitiveProperties    : PrimitivePropertyProjection,
                    enumerationProperties  : EnumerationPropertyProjection,
                    associationEnds        : AssociationEndProjection,
                }
                projection ClassSummaryProjection on Klass
                {
                    name                   : "Class name",
                    packageName            : "Class package name",
                    inheritanceType        : "Class inheritanceType",
                    ordinal                : "Class ordinal",
                    superClass             :
                    {
                        name           : "Super Class name",
                    },
                    // TODO: flat many-to-many, or parameterized property
                    superInterfaces        :
                    {
                        superInterface:
                        {
                            name           : "Super interface name",
                        },
                    },
                    classifierModifiers    :
                    {
                        keyword: "Classifier modifier keyword",
                    },
                    primitiveProperties    : PrimitivePropertySummaryProjection,
                    enumerationProperties  : EnumerationPropertySummaryProjection,
                }
                service Klass
                {
                    /meta/class/{className: String[1..1]}
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == className;
                            projection  : ClassProjection;
                        }
                    /meta/class/{className: String[1..1]}/summary
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == className;
                            projection  : ClassSummaryProjection;
                        }
                    /meta/class
                        GET
                        {
                            multiplicity: many;
                            criteria    : all;
                            projection  : ClassSummaryProjection;
                            orderBy     : this.ordinal;
                        }
                }
                projection ClassifierSummaryProjection on Classifier
                {
                    name                   : "Classifier name",
                    packageName            : "Classifier package name",
                    Klass.superClass             :
                    {
                        name           : "Super Class name",
                    },
                    // TODO: flat many-to-many, or parameterized property
                    superInterfaces        :
                    {
                        superInterface:
                        {
                            name           : "Super interface name",
                        },
                    },
                    classifierModifiers    :
                    {
                        keyword: "Classifier modifier keyword",
                    },
                    primitiveProperties    : PrimitivePropertySummaryProjection,
                    enumerationProperties  : EnumerationPropertySummaryProjection,
                }
                service Classifier
                {
                    /meta/classifier/{classifierName: String[1..1]}
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == classifierName;
                            projection  : ClassifierSummaryProjection;
                        }
                    /meta/classifier
                        GET
                        {
                            multiplicity: many;
                            criteria    : all;
                            projection  : ClassifierSummaryProjection;
                            orderBy     : this.ordinal;
                        }
                }
                projection AssociationEndProjection on AssociationEnd
                {
                    name                   : "Association end name",
                    ordinal                : "Association end ordinal",
                    direction              : "Association end direction",
                    multiplicity           : "Association end multiplicity",
                    owningClass            :
                    {
                        name       : "Association end owning class name",
                        packageName: "Association end owning class package name",
                    },
                    resultType             :
                    {
                        name       : "Association end result class name",
                        packageName: "Association end result class package name",
                    },
                    owningAssociation      :
                    {
                        name: "Association end owning association name",
                    },
                    associationEndModifiers:
                    {
                        keyword: "Association end modifier keyword",
                        ordinal: "Association end modifier ordinal",
                    },
                }
                projection MemberReferencePathSummaryProjection on MemberReferencePath
                {
                    klass           :
                    {
                        name: "MemberReferencePath Class name",
                    },
                    associationEnds :
                    {
                        associationEnd:
                        {
                            name: "MemberReferencePath Association end name",
                        },
                    },
                    dataTypeProperty:
                    {
                        name: "MemberReferencePath Property name",
                    },
                }
                projection AssociationEndSummaryProjection on AssociationEnd
                {
                    name                   : "Association end name",
                    multiplicity           : "Association end multiplicity",
                    resultType             :
                    {
                        name: "Association end result class name",
                    },
                    associationEndModifiers:
                    {
                        keyword: "Association end modifier keyword",
                    },
                    orderBys               :
                    {
                        thisMemberReferencePath: MemberReferencePathSummaryProjection,
                        orderByDirection       : "Association end order by direction",
                    },
                }
                projection ExpressionValueProjection on ExpressionValue
                {
                    MemberReferencePath.klass           :
                    {
                        name: "MemberReferencePath Class name",
                    },
                    MemberReferencePath.associationEnds :
                    {
                        associationEnd:
                        {
                            name: "MemberReferencePath Association end name",
                        },
                    },
                    MemberReferencePath.dataTypeProperty:
                    {
                        name: "MemberReferencePath Property name",
                    },
                    VariableReference.parameter         :
                    {
                        name                            : "VariableReference parameter name",
                        EnumerationParameter.enumeration:
                        {
                            name: "VariableReference parameter enumeration name",
                        },
                        PrimitiveParameter.primitiveType: "VariableReference parameter primitive type",
                        multiplicity                    : "VariableReference parameter multiplicity",
                    },
                }
                projection ExpressionValueSummaryProjection on ExpressionValue
                {
                    MemberReferencePath.klass           :
                    {
                        name: "MemberReferencePath Class name",
                    },
                    MemberReferencePath.associationEnds :
                    {
                        associationEnd:
                        {
                            name: "MemberReferencePath Association end name",
                        },
                    },
                    MemberReferencePath.dataTypeProperty:
                    {
                        name: "MemberReferencePath Property name",
                    },
                    VariableReference.parameter         :
                    {
                        name: "VariableReference parameter name",
                    },
                }
                projection MemberReferencePathProjection on MemberReferencePath
                {
                    klass           :
                    {
                        name: "MemberReferencePath Class name",
                    },
                    associationEnds :
                    {
                        associationEnd:
                        {
                            name: "MemberReferencePath Association end name",
                        },
                    },
                    dataTypeProperty:
                    {
                        name: "MemberReferencePath Property name",
                    },
                }
                projection CriteriaProjection on Criteria
                {
                    BinaryCriteria.left                   : CriteriaProjection,
                    BinaryCriteria.right                  : CriteriaProjection,
                    OperatorCriteria.operator             : "Criteria operator",
                    OperatorCriteria.sourceExpressionValue: ExpressionValueProjection,
                    OperatorCriteria.targetExpressionValue: ExpressionValueProjection,
                    EdgePointCriteria.memberReferencePath : MemberReferencePathProjection,
                }
                projection CriteriaSummaryProjection on Criteria
                {
                    BinaryCriteria.left                   : CriteriaSummaryProjection,
                    BinaryCriteria.right                  : CriteriaSummaryProjection,
                    OperatorCriteria.operator             : "Criteria operator",
                    OperatorCriteria.sourceExpressionValue: ExpressionValueSummaryProjection,
                    OperatorCriteria.targetExpressionValue: ExpressionValueSummaryProjection,
                    EdgePointCriteria.memberReferencePath : MemberReferencePathSummaryProjection,
                }
                projection AssociationProjection on Association
                {
                    name                   : "Association name",
                    packageName            : "Association package name",
                    ordinal                : "Association ordinal",
                    associationEnds        : AssociationEndProjection,
                    criteria               : CriteriaProjection,
                }
                projection AssociationSummaryProjection on Association
                {
                    name                   : "Association name",
                    packageName            : "Association package name",
                    associationEnds        : AssociationEndSummaryProjection,
                    criteria               : CriteriaSummaryProjection,
                }
                service Association
                {
                    /meta/association/{associationName: String[1..1]}
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == associationName;
                            projection  : AssociationProjection;
                        }
                    /meta/association/{associationName: String[1..1]}/summary
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == associationName;
                            projection  : AssociationSummaryProjection;
                        }
                    /meta/association
                        GET
                        {
                            multiplicity: many;
                            criteria    : all;
                            projection  : AssociationSummaryProjection;
                            orderBy     : this.ordinal;
                        }
                }
                projection ProjectionElementProjection on ProjectionElement
                {
                    name                                       : "Projection name",
                    ServiceProjection.packageName              : "Projection package name",
                    ServiceProjection.klass                    :
                    {
                        name: "Projection klass name",
                    },
                    ProjectionProjectionReference.projection   :
                    {
                        name: "Projection reference name",
                    },
                    children                                   : ProjectionElementSummaryProjection,
                }
                projection ProjectionElementSummaryProjection on ProjectionElement
                {
                    name                                       : "Projection name",
                    ServiceProjection.packageName              : "Projection package name",
                    ServiceProjection.klass                    :
                    {
                        name: "Projection klass name",
                    },
                    ProjectionProjectionReference.projection   :
                    {
                        name: "Projection reference name",
                    },
                    children                                   : ProjectionElementSummaryProjection,
                }
                service ServiceProjection
                {
                    /meta/projection/{projectionName: String[1..1]}
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == projectionName;
                            projection  : ProjectionElementProjection;
                        }
                    /meta/projection/{projectionName: String[1..1]}/summary
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == projectionName;
                            projection  : ProjectionElementSummaryProjection;
                        }
                    /meta/projection
                        GET
                        {
                            multiplicity: many;
                            criteria    : all;
                            projection  : ProjectionElementSummaryProjection;
                            orderBy     : this.ordinal;
                        }
                }
                projection ServiceGroupProjection on ServiceGroup
                {
                    name       : "ServiceGroup name",
                    packageName: "ServiceGroup package name",
                    urls       :
                    {
                        url     : "ServiceGroup url",
                        services:
                        {
                            verb               : "ServiceGroup Url Service verb",
                            serviceMultiplicity: "ServiceGroup Url Service multiplicity",
                            projection         :
                            {
                                name: "ServiceGroup Url Service Projection name",
                            },
                            queryCriteria      : CriteriaSummaryProjection,
                            authorizeCriteria  : CriteriaSummaryProjection,
                            validateCriteria   : CriteriaSummaryProjection,
                            conflictCriteria   : CriteriaSummaryProjection,
                        },
                    },
                }
                projection ServiceGroupSummaryProjection on ServiceGroup
                {
                    name       : "ServiceGroup name",
                    packageName: "ServiceGroup package name",
                    urls       :
                    {
                        url     : "ServiceGroup url",
                        services:
                        {
                            verb               : "ServiceGroup Url Service verb",
                            serviceMultiplicity: "ServiceGroup Url Service multiplicity",
                            projection         :
                            {
                                name: "ServiceGroup Url Service Projection name",
                            },
                            queryCriteria      : CriteriaSummaryProjection,
                        },
                    },
                }
                service ServiceGroup
                {
                    /meta/serviceGroup/{serviceGroupName: String[1..1]}
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == serviceGroupName;
                            projection  : ServiceGroupProjection;
                        }
                    /meta/serviceGroup/{serviceGroupName: String[1..1]}/summary
                        GET
                        {
                            multiplicity: one;
                            criteria    : this.name == serviceGroupName;
                            projection  : ServiceGroupSummaryProjection;
                        }
                    /meta/serviceGroup
                        GET
                        {
                            multiplicity: many;
                            criteria    : all;
                            projection  : ServiceGroupSummaryProjection;
                            orderBy     : this.ordinal;
                        }
                }
                """;
        //</editor-fold>

        this.assertNoCompilerErrors(sourceCodeText);
    }

    @Test
    public void emoji()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = ""
                + "package com.emoji\n"
                + "\n"
                + "// 😃\n";
        //</editor-fold>

        this.assertNoCompilerErrors(sourceCodeText);
    }

    @Test
    public void projectionOnInterface()
    {
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = """
                package dummy

                class OwningClass
                {
                    id: Long key;
                }

                class OwnedClass
                {
                    id: Long key;
                    ownerId: Long private;
                }

                interface OwningInterface
                {
                    id: Long key;
                    ownedInterfaces: OwnedInterface[0..*];
                }

                interface OwnedInterface
                {
                    id: Long key;
                    owningInterface: OwningInterface[1..1];
                }

                association OwningClassOwnsOwnedClasses
                {
                    owningClass: OwningClass[1..1];
                    ownedClasses: OwnedClass[0..*] owned;

                    relationship this.id == OwnedClass.ownerId
                }

                projection OwningClassSummaryProjection on OwningClass
                {
                    id: "OwningClass Id",
                }

                projection OwnedClassSummaryProjection on OwnedClass
                {
                    id: "OwnedClass Id",
                }

                projection OwningInterfaceSummaryProjection on OwningInterface
                {
                    id: "OwningInterface Id",
                }

                projection OwnedInterfaceSummaryProjection on OwnedInterface
                {
                    id: "OwnedInterface Id",
                }

                projection OwningClassProjection on OwningClass
                {
                    id          : "OwningClass Id",
                    ownedClasses:
                    {
                        id: "OwnedClass Id",
                    },
                }

                projection OwnedClassProjection on OwnedClass
                {
                    id         : "OwnedClass Id",
                    owningClass:
                    {
                        id: "OwningClass Id",
                    },
                }

                projection OwningInterfaceProjection on OwningInterface
                {
                    id             : "OwningInterface Id",
                    ownedInterfaces:
                    {
                        id: "OwnedInterface Id",
                    },
                }

                projection OwnedInterfaceProjection on OwnedInterface
                {
                    id             : "OwnedInterface Id",
                    owningInterface:
                    {
                        id: "OwningInterface Id",
                    },
                }

                projection OwningClassDetailedProjection on OwningClass
                {
                    id          : "OwningClass Id",
                    ownedClasses: OwnedClassSummaryProjection,
                }

                projection OwnedClassDetailedProjection on OwnedClass
                {
                    id         : "OwnedClass Id",
                    owningClass: OwningClassSummaryProjection,
                }

                projection OwningInterfaceDetailedProjection on OwningInterface
                {
                    id             : "OwningInterface Id",
                    ownedInterfaces: OwnedInterfaceSummaryProjection,
                }

                projection OwnedInterfaceDetailedProjection on OwnedInterface
                {
                    id             : "OwnedInterface Id",
                    owningInterface: OwningInterfaceSummaryProjection,
                }
                """;
        //</editor-fold>

        this.assertNoCompilerErrors(sourceCodeText);
    }

    private void assertNoCompilerErrors(@Nonnull String sourceCodeText)
    {
        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                0,
                Optional.empty(),
                "example.klass",
                sourceCodeText);
        KlassCompiler     compiler          = new KlassCompiler(compilationUnit);
        CompilationResult compilationResult = compiler.compile();
        ImmutableList<RootCompilerAnnotation> compilerErrors = compilationResult
                .compilerAnnotations()
                .select(AbstractCompilerAnnotation::isError);
        if (compilerErrors.notEmpty())
        {
            String message = compilerErrors.makeString("\n");
            fail(message);
        }
        else
        {
            Optional<DomainModelWithSourceCode> domainModelWithSourceCode = compilationResult.domainModelWithSourceCode();
            assertTrue(domainModelWithSourceCode.isPresent());
        }
    }
}
