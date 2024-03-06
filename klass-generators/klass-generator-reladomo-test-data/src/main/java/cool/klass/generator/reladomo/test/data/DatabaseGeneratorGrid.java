package cool.klass.generator.reladomo.test.data;

import java.io.BufferedWriter;
import java.io.IOException;

import cool.klass.model.meta.domain.property.PrimitiveType;
import org.eclipse.collections.api.list.ImmutableList;

public class DatabaseGeneratorGrid
{
    private static final boolean END_POINTS_INCLUSIVE = false;

    private final ImmutableList<DatabaseGeneratorColumn> columns;
    private final String                                 delimiter;
    private       int                                    currentColumn;
    private       int                                    currentRow;

    public DatabaseGeneratorGrid(
            ImmutableList<PrimitiveType> primitiveTypes,
            String delimiter)
    {
        this.columns = primitiveTypes.collect(DatabaseGeneratorColumn::new);
        this.delimiter = delimiter;
    }

    public void addValue(String value)
    {
        DatabaseGeneratorColumn databaseGeneratorColumn = this.columns.get(this.currentColumn);
        databaseGeneratorColumn.addValue(value);
        this.currentColumn++;
    }

    public void finishRow()
    {
        if (this.currentColumn != this.columns.size())
        {
            throw new AssertionError();
        }

        this.currentColumn = 0;
        this.currentRow++;
    }

    public void print(BufferedWriter writer) throws IOException
    {
        int numRows = this.currentRow;
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
        {
            for (int columnIndex = 0; columnIndex < this.columns.size() - 1; columnIndex++)
            {
                DatabaseGeneratorColumn column      = this.columns.get(columnIndex);
                String                  paddedValue = column.getPaddedValue(rowIndex);
                writer.write(paddedValue);
                writer.write(this.delimiter);
            }

            DatabaseGeneratorColumn column      = this.columns.getLast();
            String                  paddedValue = column.getPaddedValue(rowIndex);
            writer.write(paddedValue);
            writer.newLine();
        }

        writer.newLine();
    }
}
