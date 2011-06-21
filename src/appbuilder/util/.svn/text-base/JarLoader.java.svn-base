package appbuilder.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an utility class that dinamically adds jar files to the systems's 
 * classpath.
 * 
 * @author Bruno Gama Catão
 */
public class JarLoader {
    private static List<String> alreadyAdded;

    static {
        alreadyAdded = new ArrayList<String>();
    }

    @SuppressWarnings("unchecked")
    public static void addFileToClasspath(String path) throws SecurityException,
            NoSuchMethodException, MalformedURLException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        if (alreadyAdded.contains(path)) {
            return;
        }

        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;
        Class[] parameters = new Class[]{URL.class};
        Method method = sysclass.getDeclaredMethod("addURL", parameters);
        method.setAccessible(true);
        URL url = new URL("file", null, path);
        method.invoke(loader, new Object[]{url});

        alreadyAdded.add(path);
    }
}
