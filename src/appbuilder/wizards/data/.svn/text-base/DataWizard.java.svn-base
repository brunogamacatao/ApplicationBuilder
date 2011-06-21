package appbuilder.wizards.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import appbuilder.application.data.Column;
import appbuilder.application.data.DataSource;
import appbuilder.application.data.DataType;
import appbuilder.application.data.Table;
import java.util.List;
import static appbuilder.application.util.Toolkit.createList;

public class DataWizard {
    private static final String DECIMAL_DIGITS = "DECIMAL_DIGITS";
    private static final String COLUMN_SIZE = "COLUMN_SIZE";
    private static final String IS_NULLABLE = "IS_NULLABLE";
    private static final String TYPE_NAME = "TYPE_NAME";
    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final String TABLE_NAME = "TABLE_NAME";
    private static final String SCHEMA_NAME = "TABLE_SCHEM";
    private static final String CATALOG_NAME = "TABLE_CAT";
    private static final String WILDCARD = "%";
    
    private Connection connection;
    private List<DatabaseObject> loadingQueue;
    private List<ProgressListener> listeners;

    public DataWizard(String driver, String url, String user, String password) throws Exception {
        Class.forName(driver);
        
        if (url != null && url.trim().length() > 0 &&
                password != null && password.trim().length() > 0) {
            this.connection = DriverManager.getConnection(url, user, password);
        } else {
            this.connection = DriverManager.getConnection(url);
        }
        
        listeners = createList();
    }
    
    public void addProgressListener(ProgressListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(ProgressListener listener) {
        listeners.remove(listener);
    }
    
    private void fireLoadingObject(DatabaseObject obj) {
        for (ProgressListener listener : listeners) {
            listener.loadingObject(obj);
        }
    }

    public DataSource getDataSource() throws Exception {
        loadingQueue = createList();
        
        DatabaseMetaData metaData = connection.getMetaData();
        DataSource ds = new DataSource("");

        boolean hasCatalog = false;
        try {
            ResultSet ctgRs = metaData.getCatalogs();
            
            while (ctgRs.next()) {
                hasCatalog = true;
                String catalogName = ctgRs.getString(CATALOG_NAME);
                ds.setName(catalogName);
                DatabaseObject obj = new DatabaseObject(catalogName, DatabaseObject.Type.CATALOG);
                fireLoadingObject(obj);
                loadingQueue.add(obj);
            }
            
            ctgRs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        if (!hasCatalog) {
            boolean hasSchema = false;
            
            try {
                ResultSet schRs = metaData.getSchemas(WILDCARD, WILDCARD);

                while (schRs.next()) {
                    hasSchema = true;
                    String schemaName = schRs.getString(SCHEMA_NAME);
                    DatabaseObject obj = new DatabaseObject(schemaName, DatabaseObject.Type.SCHEMA);
                    fireLoadingObject(obj);
                    loadingQueue.add(obj);
                }
                
                schRs.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } catch (AbstractMethodError err) {
                err.printStackTrace();
            }
            
            if (!hasSchema) {
                try {
                    ResultSet tblRs = metaData.getTables(WILDCARD, WILDCARD, WILDCARD, new String[]{"TABLE", "VIEW"});
                    
                    while (tblRs.next()) {
                        String tableName = tblRs.getString(TABLE_NAME);
                        DatabaseObject obj = new DatabaseObject(tableName, DatabaseObject.Type.TABLE);
                        fireLoadingObject(obj);
                        loadingQueue.add(obj);
                    }
                    
                    tblRs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        while (!loadingQueue.isEmpty()) {
            DatabaseObject obj = loadingQueue.remove(0);
            
            switch (obj.type) {
            case CATALOG:
                try {
                    ResultSet schRs = metaData.getSchemas(obj.name, WILDCARD);

                    while (schRs.next()) {
                        String schemaName = schRs.getString(SCHEMA_NAME);
                        obj = new DatabaseObject(schemaName, obj.name, DatabaseObject.Type.SCHEMA);
                        fireLoadingObject(obj);
                        loadingQueue.add(obj);
                    }

                    schRs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
                break;
            case SCHEMA:
                try {
                    ResultSet tblRs = metaData.getTables(obj.catalog, obj.name, WILDCARD, null);

                    while (tblRs.next()) {
                        String tableName = tblRs.getString(TABLE_NAME);
                        obj = new DatabaseObject(tableName, obj.catalog, obj.name, DatabaseObject.Type.TABLE);
                        fireLoadingObject(obj);
                        loadingQueue.add(obj);
                    }

                    tblRs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
                break;
            case TABLE:
                try {
                    fireLoadingObject(new DatabaseObject(obj.name, DatabaseObject.Type.TABLE));
                    loadMethod(metaData, ds, obj.catalog, obj.schema, obj.name);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
                break;
            }
        }

        return ds;
    }

    private void loadMethod(DatabaseMetaData metaData, DataSource ds,
            String catalogName, String schemaName, String tableName)
            throws SQLException {
        Table table = new Table(tableName, schemaName);

        Collection<String> primaryKeys = loadPrimaryKeys(metaData, catalogName, schemaName, tableName);

        //Discovering the foreign keys referencing this table
        //TODO - Implement this

        ResultSet rsCol = metaData.getColumns(catalogName, schemaName, tableName, WILDCARD);
        while (rsCol.next()) {
            table.addColumn(loadColumn(rsCol, primaryKeys));
        }
        rsCol.close();

        ds.addTable(table);
    }

    private Collection<String> loadPrimaryKeys(DatabaseMetaData metaData, String catalogName, String schemaName, String tableName) throws SQLException {
        Collection<String> primaryKeys = createList();

        //Storing the primary keys
        ResultSet pkeyRs = metaData.getPrimaryKeys(catalogName, schemaName, tableName);
        while (pkeyRs.next()) {
            primaryKeys.add(pkeyRs.getString(COLUMN_NAME));
        }
        pkeyRs.close();

        return primaryKeys;
    }

    private Column loadColumn(ResultSet rsCol, Collection<String> primaryKeys) throws SQLException {
        String columnName = rsCol.getString(COLUMN_NAME);
        String typeName = rsCol.getString(TYPE_NAME);

        DataType type = null;

        for (DataType tp : DataType.values()) {
            if (tp.toString().equalsIgnoreCase(typeName)) {
                type = tp;
                break;
            }
        }

        if (type == null) {
            type = DataType.VARCHAR;
        }

        Column column = new Column(columnName, type);
        column.setNullable("YES".equals(rsCol.getString(IS_NULLABLE)));
        column.setPrimaryKey(primaryKeys.contains(columnName));
        column.setPrecision(0, rsCol.getInt(COLUMN_SIZE));

        if (column.getType().getDimensions() > 1) {
            column.setPrecision(1, rsCol.getInt(DECIMAL_DIGITS));
        }

        return column;
    }

    public static class DatabaseObject {
        public static enum Type {
            CATALOG,
            SCHEMA,
            TABLE
        }
        
        public String name;
        public Type type;
        public String catalog = WILDCARD;
        public String schema  = WILDCARD;
        
        public DatabaseObject(String name, Type type) {
            this(name, "", type);
        }
        
        public DatabaseObject(String name, String catalog, Type type) {
            this(name, catalog, "", type);
        }
        
        public DatabaseObject(String name, String catalog, String schema, Type type) {
            this.name    = name;
            this.type    = type;
            this.catalog = catalog;
            this.schema  = schema;
        }
    }
}
