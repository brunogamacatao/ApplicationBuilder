package appbuilder.application.util;

/**
 * @author Bruno Gama Catão
 * @since 2007-12-27
 */
public class StringUtils {
    private static StringUtils instance;
    
    public static StringUtils getInstance() {
        if (instance == null) {
            instance = new StringUtils();
        }
        
        return instance;
    }
    
    private StringUtils() {
    }
    
    public String tableToClassName(String tableName) {
        //Breaks the table name in each underscore character
        String[] tokens  = tableName.split("_");
        String className = "";
        
        for (String token : tokens) {
            className += Character.toUpperCase(token.charAt(0)) + 
                    token.substring(1).toLowerCase();
        }
        
        return className;
    }
    
    public String columnToPropertyName(String tableName) {
        //Breaks the table name in each underscore character
        String[] tokens  = tableName.split("_");
        String propName  = "";
        
        for (String token : tokens) {
            propName += Character.toUpperCase(token.charAt(0)) + 
                    token.substring(1).toLowerCase();
        }
        
        return propName.substring(0, 1).toLowerCase() + propName.substring(1);
    }
    
    public String applicationToClassName(String applicationName) {
        /* Breaks the application name in each underscore, hifen or whitespace 
         * character. */
        String[] tokens  = applicationName.split("[\\s_-]");
        String clssName  = "";
        
        for (String token : tokens) {
            clssName += Character.toUpperCase(token.charAt(0)) + 
                    token.substring(1).toLowerCase();
        }
        
        return clssName;
    }
    
    public String getFileName(String completePath) {
        String[] path = completePath.split("[\\\\/]");
        return path[path.length - 1];
    }
}
