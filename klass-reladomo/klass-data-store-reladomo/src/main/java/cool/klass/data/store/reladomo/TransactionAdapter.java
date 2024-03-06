package cool.klass.data.store.reladomo;

import com.gs.fw.common.mithra.MithraTransaction;
import cool.klass.data.store.Transaction;

public class TransactionAdapter implements Transaction
{
    private final MithraTransaction mithraTransaction;

    public TransactionAdapter(MithraTransaction mithraTransaction)
    {
        this.mithraTransaction = mithraTransaction;
    }

    @Override
    public void setSystemTime(long systemTime)
    {
        this.mithraTransaction.setProcessingStartTime(systemTime);
    }
}
