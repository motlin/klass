package cool.klass.generator.reladomo.objectfile;

import java.util.Objects;

import com.gs.fw.common.mithra.generator.metamodel.AttributePureType;
import com.gs.fw.common.mithra.generator.metamodel.PrimaryKeyGeneratorStrategyType;
import com.gs.fw.common.mithra.generator.metamodel.SimulatedSequenceType;
import com.gs.fw.common.mithra.generator.metamodel.TimezoneConversionType;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.validation.NumericPropertyValidation;
import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;
import io.liftwizard.reladomo.simseq.ObjectSequenceObjectFactory;

// TODO: Create a DataTypeVisitor that factors in enumerations too
class AttributeTypeVisitor
        implements PrimitiveTypeVisitor
{
    private final AttributePureType attributeType;
    private final Klass             owningClass;
    private final PrimitiveProperty primitiveProperty;

    AttributeTypeVisitor(AttributePureType attributeType, Klass owningClass, PrimitiveProperty primitiveProperty)
    {
        this.attributeType     = Objects.requireNonNull(attributeType);
        this.owningClass       = Objects.requireNonNull(owningClass);
        this.primitiveProperty = Objects.requireNonNull(primitiveProperty);
    }

    @Override
    public void visitString()
    {
        this.attributeType.setJavaType("String");
        this.attributeType.setTrim(false);

        this.primitiveProperty
                .getMaxLengthPropertyValidation()
                .map(NumericPropertyValidation::getNumber)
                .ifPresent(this.attributeType::setMaxLength);
    }

    @Override
    public void visitInteger()
    {
        this.attributeType.setJavaType("int");
    }

    @Override
    public void visitLong()
    {
        this.attributeType.setJavaType("long");

        if (this.primitiveProperty.isID() && (this.owningClass == this.primitiveProperty.getOwningClassifier() || this.owningClass.getSuperClass().isEmpty()))
        {
            // TODO: Infer during compilation that ID properties are key properties, or add an error when they are not.
            PrimaryKeyGeneratorStrategyType primaryKeyGeneratorStrategyType = new PrimaryKeyGeneratorStrategyType();
            primaryKeyGeneratorStrategyType.with("SimulatedSequence", this.attributeType);
            this.attributeType.setPrimaryKeyGeneratorStrategy(primaryKeyGeneratorStrategyType);
            SimulatedSequenceType simulatedSequence = new SimulatedSequenceType();
            simulatedSequence.setSequenceName(this.owningClass.getName());
            simulatedSequence.setSequenceObjectFactoryName(ObjectSequenceObjectFactory.class.getCanonicalName());
            simulatedSequence.setHasSourceAttribute(false);
            simulatedSequence.setBatchSize(10);
            simulatedSequence.setInitialValue(1);
            simulatedSequence.setIncrementSize(1);
            this.attributeType.setSimulatedSequence(simulatedSequence);
        }
    }

    @Override
    public void visitDouble()
    {
        this.attributeType.setJavaType("double");
    }

    @Override
    public void visitFloat()
    {
        this.attributeType.setJavaType("float");
    }

    @Override
    public void visitBoolean()
    {
        this.attributeType.setJavaType("boolean");
    }

    @Override
    public void visitInstant()
    {
        this.attributeType.setJavaType("Timestamp");
        TimezoneConversionType timezoneConversion = new TimezoneConversionType();
        timezoneConversion.with("convert-to-utc", this.attributeType);
        this.attributeType.setTimezoneConversion(timezoneConversion);
    }

    @Override
    public void visitLocalDate()
    {
        this.attributeType.setJavaType("Date");
    }

    @Override
    public void visitTemporalInstant()
    {
        throw new AssertionError();
    }

    @Override
    public void visitTemporalRange()
    {
        throw new AssertionError();
    }
}
