package appbuilder.application.data;

import java.util.Collection;
import static appbuilder.application.util.Toolkit.createCollection;

/**
 * @author Bruno Gama Catão
 * @since 2007-12-27
 */
public class DataSource {

    private String name;
    private Collection<Table> tables;

    public DataSource(String name) {
        this.name = name;
        this.tables = createCollection();
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public void removeTable(Table table) {
        tables.remove(table);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Table> getTables() {
        return tables;
    }

    public void setTables(Collection<Table> tables) {
        this.tables = tables;
    }
}
