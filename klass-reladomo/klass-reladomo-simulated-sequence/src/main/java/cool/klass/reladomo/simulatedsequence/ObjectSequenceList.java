package cool.klass.reladomo.simulatedsequence;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ObjectSequenceList extends ObjectSequenceListAbstract
{
    public ObjectSequenceList()
    {
    }

    public ObjectSequenceList(int initialSize)
    {
        super(initialSize);
    }

    public ObjectSequenceList(Collection c)
    {
        super(c);
    }

    public ObjectSequenceList(Operation operation)
    {
        super(operation);
    }
}
