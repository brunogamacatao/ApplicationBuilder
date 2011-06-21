package appbuilder.application.config.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import appbuilder.application.Application;
import appbuilder.application.util.StringUtils;
import appbuilder.builder.Builder;

import com.google.inject.Inject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author Bruno Gama Catão
 * @since 2007-12-28
 */
public abstract class ConfigBuilder implements Builder {
    private static Logger logger = Logger.getLogger(ConfigBuilder.class.getName());
    public static final String UTIL_PACKAGE = "util";
    protected Application application;
    protected VelocityContext context;

    @Inject
    public ConfigBuilder(VelocityContext context, Application application) {
        this.context = context;
        this.application = application;
    }
    
    public void clean() {
        logger.info("Deleting the source directory");
        deleteFile(getApplication().getBaseDirectory() + File.separator + getApplication().getSourceDirectory());
        logger.info("Deleting the build directory");
        deleteFile(getApplication().getBaseDirectory() + File.separator + getApplication().getBuildDirectory());
        logger.info("Deleting the distribution directory");
        deleteFile(getApplication().getBaseDirectory() + File.separator + getApplication().getDistributionDirectory());
    }

    public void build() {
        try {
            logger.info("Generating MessageUtil class");
            generateMessageUtil();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        logger.info("Copying required libraries");
        copyLibraries();
    }

    private void generateMessageUtil() throws ResourceNotFoundException, ParseErrorException, Exception {
        context.put("package", buildPackage(UTIL_PACKAGE));

        Template template = Velocity.getTemplate("templates/util/MessageUtil.vm");

        File path = createDir(getBaseDirectory() + "." + UTIL_PACKAGE);
        File file = new File(path, "MessageUtil.java");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        template.merge(context, writer);

        writer.flush();
        writer.close();
    }
    
    private void copyLibraries() {
        File path = createDir(getApplication().getBaseDirectory() + "." + application.getLibDirectory());
        
        for (String library : application.getLibraries()) {
            try {
                logger.info("Copying the library " + library);
                copyFile(library, path);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    private String buildPackage(String suffix) {
        return getApplication().getBasePackage() + "." + suffix;
    }

    private String getBaseDirectory() {
        return getApplication().getBaseDirectory() + "." +
                getApplication().getSourceDirectory() + "." +
                getApplication().getBasePackage();
    }

    protected File createDir(String pkgName) {
        String dirName = pkgName.replace('.', '/');

        File baseDir = new File(dirName);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        return baseDir;
    }

    private void deleteFile(String filename) {
        deleteFile(new File(filename));
    }

    private void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFile(f);
            }
        }

        file.delete();
    }
    
    /**
     * This method copies a file to a directory.
     */
    private void copyFile(String file, File directory) throws IOException {
        String fileName = StringUtils.getInstance().getFileName(file);
        
        FileChannel in = new FileInputStream(file).getChannel();
        FileChannel out = new FileOutputStream(new File(directory, fileName)).getChannel();
        
        out.transferFrom(in, 0, in.size());
        
        out.close();
        in.close();
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
