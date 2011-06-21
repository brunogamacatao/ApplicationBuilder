package appbuilder.application.data;

/**
 * @author Bruno Gama Catão
 * @since 2007-12-27
 */
public class Column {

    private String name;
    private DataType type;
    private boolean primaryKey;
    private boolean nullable;
    private int[] precision;
    private Object defaultValue;
    private Column referencedColumn;
    private Table referencedTable;

    public Column(String name, DataType type) {
        this.name = name;
        this.type = type;
        this.primaryKey = false;
        this.nullable = true;
        this.precision = new int[type.getDimensions()];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int[] getPrecision() {
        return precision;
    }

    public int getPrecision(int dimension) {
        return precision[dimension];
    }

    public void setPrecision(int[] precision) {
        this.precision = precision;
    }

    public void setPrecision(int dimension, int value) {
        this.precision[dimension] = value;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Column getReferencedColumn() {
        return referencedColumn;
    }

    public void setReferencedColumn(Column referencedColumn) {
        this.referencedColumn = referencedColumn;
    }

    public void setReference(Column referencedColumn, Table referencedTable) {
        this.referencedColumn = referencedColumn;
        this.referencedTable = referencedTable;
    }

    public Table getReferencedTable() {
        return referencedTable;
    }

    public void setReferencedTable(Table referencedTable) {
        this.referencedTable = referencedTable;
    }
}
