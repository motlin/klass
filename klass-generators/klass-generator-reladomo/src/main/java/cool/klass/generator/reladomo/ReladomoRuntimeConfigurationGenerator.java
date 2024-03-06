package cool.klass.generator.reladomo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.gs.fw.common.mithra.mithraruntime.CacheType;
import com.gs.fw.common.mithra.mithraruntime.ConnectionManagerType;
import com.gs.fw.common.mithra.mithraruntime.MithraObjectConfigurationType;
import com.gs.fw.common.mithra.mithraruntime.MithraPureObjectConfigurationType;
import com.gs.fw.common.mithra.mithraruntime.MithraRuntime;
import com.gs.fw.common.mithra.mithraruntime.MithraRuntimeMarshaller;
import com.gs.fw.common.mithra.mithraruntime.PropertyType;
import com.gs.fw.common.mithra.mithraruntime.PureObjectsType;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.InheritanceType;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.PackageableElement;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class ReladomoRuntimeConfigurationGenerator extends AbstractReladomoGenerator
{
    // "cool.klass.reladomo.connectionmanager.h2.H2ConnectionManager"
    // "com.gs.fw.common.mithra.test.ConnectionManagerForTests"
    private final String  connectionManagerFullyQualifiedName;
    private final boolean isTest;
    private final String  rootPackageName;

    public ReladomoRuntimeConfigurationGenerator(
            DomainModel domainModel,
            String connectionManagerFullyQualifiedName,
            boolean isTest,
            String rootPackageName)
    {
        super(domainModel);
        this.connectionManagerFullyQualifiedName = Objects.requireNonNull(connectionManagerFullyQualifiedName);
        this.isTest = isTest;
        this.rootPackageName = Objects.requireNonNull(rootPackageName);
    }

    public void writeRuntimeConfigFile(@Nonnull Path path) throws IOException
    {
        MithraRuntime mithraRuntime = this.generateMithraRuntime();

        MithraRuntimeMarshaller mithraRuntimeMarshaller = new MithraRuntimeMarshaller();
        mithraRuntimeMarshaller.setIndent(true);

        StringBuilder stringBuilder = new StringBuilder();
        mithraRuntimeMarshaller.marshall(stringBuilder, mithraRuntime);
        String xmlString = this.sanitizeXmlString(stringBuilder);

        this.printStringToFile(path, xmlString);
    }

    @Nonnull
    public MithraRuntime generateMithraRuntime()
    {
        return this.getMithraRuntime(
                this.connectionManagerFullyQualifiedName,
                this.getPropertyTypes());
    }

    @Nonnull
    public MithraRuntime getMithraRuntime(
            String connectionManagerFullyQualifiedName,
            @Nonnull ImmutableList<PropertyType> propertyTypes)
    {
        MithraRuntime mithraRuntime = new MithraRuntime();
        mithraRuntime.setConnectionManagers(this.getConnectionManagerTypes(
                connectionManagerFullyQualifiedName,
                propertyTypes));
        mithraRuntime.setPureObjects(this.getPureObjectsType());
        return mithraRuntime;
    }

    public ImmutableList<PropertyType> getPropertyTypes()
    {
        return this.isTest
                ? Lists.immutable.with(this.getPropertyType("resourceName", "testdb"))
                : Lists.immutable.empty();
    }

    private MutableList<ConnectionManagerType> getConnectionManagerTypes(
            String connectionManagerFullyQualifiedName,
            @Nonnull ImmutableList<PropertyType> propertyTypes)
    {
        ConnectionManagerType connectionManagerType = this.getConnectionManager(
                connectionManagerFullyQualifiedName,
                propertyTypes);
        return Lists.mutable.with(connectionManagerType);
    }

    @Nonnull
    private PureObjectsType getPureObjectsType()
    {
        PureObjectsType pureObjectsType = new PureObjectsType();
        pureObjectsType.setNotificationIdentifier(this.rootPackageName);
        pureObjectsType.setMithraObjectConfigurations(this.getMithraPureObjectConfigurationTypes().castToList());
        return pureObjectsType;
    }

    @Nonnull
    public PropertyType getPropertyType(String name, String value)
    {
        PropertyType propertyType = new PropertyType();
        propertyType.setName(name);
        propertyType.setValue(value);
        return propertyType;
    }

    @Nonnull
    private ConnectionManagerType getConnectionManager(
            String connectionManagerFullyQualifiedName,
            ImmutableList<PropertyType> propertyTypes)
    {
        ConnectionManagerType connectionManagerType = new ConnectionManagerType();
        connectionManagerType.setClassName(connectionManagerFullyQualifiedName);
        connectionManagerType.setProperties(propertyTypes.castToList());
        connectionManagerType.setMithraObjectConfigurations(this.getConnectionManagerObjectConfigurationTypes().castToList());
        return connectionManagerType;
    }

    private ImmutableList<MithraPureObjectConfigurationType> getMithraPureObjectConfigurationTypes()
    {
        return this.domainModel
                .getClasses()
                .select(Klass::isTransient)
                .collect(PackageableElement::getFullyQualifiedName)
                .collect(this::getMithraPureObjectConfigurationType);
    }

    private ImmutableList<MithraObjectConfigurationType> getConnectionManagerObjectConfigurationTypes()
    {
        ImmutableList<MithraObjectConfigurationType> objectConfigurationTypes              = this.getObjectConfigurationTypes();
        MithraObjectConfigurationType                objectSequenceObjectConfigurationType = this.getObjectSequenceObjectConfigurationType();

        return Lists.immutable.with(objectSequenceObjectConfigurationType)
                .newWithAll(objectConfigurationTypes);
    }

    private ImmutableList<MithraObjectConfigurationType> getObjectConfigurationTypes()
    {
        return this.domainModel
                .getClasses()
                // TODO: Can a class be transient and abstract? Is that redundant?
                .reject(Klass::isTransient)
                .reject(each -> each.getInheritanceType() == InheritanceType.TABLE_PER_SUBCLASS)
                .collect(PackageableElement::getFullyQualifiedName)
                .collectWith(this::getMithraObjectConfigurationType, CacheType.PARTIAL);
    }

    @Nonnull
    private MithraObjectConfigurationType getObjectSequenceObjectConfigurationType()
    {
        return this.getMithraObjectConfigurationType(
                "cool.klass.reladomo.simseq.ObjectSequence",
                CacheType.NONE);
    }

    @Nonnull
    private MithraObjectConfigurationType getMithraObjectConfigurationType(
            String fullyQualifiedClassName,
            CacheType cacheType)
    {
        MithraObjectConfigurationType mithraObjectConfigurationType = new MithraObjectConfigurationType();
        mithraObjectConfigurationType.setCacheType(cacheType);
        mithraObjectConfigurationType.setClassName(fullyQualifiedClassName);
        return mithraObjectConfigurationType;
    }

    @Nonnull
    private MithraPureObjectConfigurationType getMithraPureObjectConfigurationType(String fullyQualifiedClassName)
    {
        MithraPureObjectConfigurationType mithraPureObjectConfigurationType = new MithraPureObjectConfigurationType();
        mithraPureObjectConfigurationType.setClassName(fullyQualifiedClassName);
        return mithraPureObjectConfigurationType;
    }
}
