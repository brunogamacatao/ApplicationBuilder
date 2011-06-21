package appbuilder.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessageUtil {
    private static final String MESSAGES_FILE = "messages/Messages";
    
    private static ResourceBundle bundle = null;
    
    static {
        bundle = ResourceBundle.getBundle(MESSAGES_FILE);
    }
    
    public static String getMessage(String key) {
        return bundle.getString(key);
    }
    
    public static String getMessage(String key, Object... arguments) {
        return MessageFormat.format(getMessage(key), arguments);
    }
}