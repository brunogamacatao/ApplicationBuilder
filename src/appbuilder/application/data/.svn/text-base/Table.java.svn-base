package appbuilder.application.data;

import java.util.Collection;
import static appbuilder.application.util.Toolkit.createCollection;

/**
 * @author Bruno Gama Catão
 * @since 2007-12-27
 */
public class Table {

    private String name;
    private String schema;
    private Collection<Column> columns;

    public Table(String name) {
        this(name, null);
    }

    public Table(String name, String schema) {
        this.name = name;
        this.schema = schema;
        this.columns = createCollection();
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public void removeColumn(Column column) {
        columns.remove(column);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Column> getColumns() {
        return columns;
    }

    public void setColumns(Collection<Column> columns) {
        this.columns = columns;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public boolean equals(Object obj) {
        if (!Table.class.isInstance(obj)) {
            return false;
        }

        Table table = Table.class.cast(obj);

        if (this.getSchema() != null && table.getSchema() == null) {
            return false;
        }
        if (this.getSchema() == null && table.getSchema() != null) {
            return false;
        }
        if (this.getSchema() != null) {
            if (!this.getSchema().equals(table.getSchema())) {
                return false;
            }
        }

        return this.getName().equals(table.getName());
    }

    public String toString() {
        String str = "";
        if (getSchema() != null) {
            str += getSchema() + ".";
        }
        return str + getName();
    }
}
