package appbuilder.application.data;

/**
 * @author Bruno Gama Catão
 * @since 2007-12-27
 */
public enum DataType {
    INTEGER(1,  "Integer",            "Int"),
    NUMBER(2,   "Double",             "Double"),
    CHAR(1,     "String",             "String"),
    VARCHAR(1,  "String",             "String"),
    VARCHAR2(1, "String",             "String"),
    TEXT(1,     "String",             "String"),
    BLOB(1,     "java.sql.Blob",      "Blob"),
    CLOB(1,     "java.sql.Clob",      "Clob"),
    DOUBLE(2,   "Double",             "Double"),
    DATETIME(1, "java.sql.Timestamp", "Timestamp");
    
    private int dimensions;
    private String javaType;
    private String jdbcName;
    
    DataType(int dimensions, String javaType, String jdbcName) {
        this.dimensions = dimensions;
        this.javaType   = javaType;
        this.jdbcName   = jdbcName;
    }
    
    public int getDimensions() {
        return dimensions;
    }
    
    public String getJavaType() {
        return javaType;
    }
    
    public String getJdbcName() {
        return jdbcName;
    }
}
