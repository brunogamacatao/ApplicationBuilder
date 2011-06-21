package appbuilder.application.data.builder;

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
import appbuilder.builder.Builder;

import com.google.inject.Inject;

/**
 * @author Bruno Gama Catão
 * @since 2007-12-27
 */
public abstract class DataBuilder implements Builder {
    public static final String BASE_PACKAGE       = "data";
    public static final String EXCEPTIONS_PACKAGE = "exceptions";
    
    private static Logger logger = Logger.getLogger(DataBuilder.class.getName());
    
    protected Application application;
    protected VelocityContext context;
    
    @Inject
    protected DataBuilder(VelocityContext context, Application application) {
        this.context     = context;
        this.application = application;
    }
    
    public void build() {
        try {
            logger.info("creating the general database directories");
            createDirectories();
            logger.info("setting up the general database generation context");
            setupContext();
            logger.info("generation the general database exceptions");
            buildDataException();
            buildEmptyPropertyException();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    private void setupContext() {
        context.put("exceptionsPackage", buildPackage(EXCEPTIONS_PACKAGE));
    }
    
    private void buildDataException() throws Exception, ResourceNotFoundException, ParseErrorException {
        context.put("package", buildPackage(EXCEPTIONS_PACKAGE));

        Template template = Velocity.getTemplate("templates/data/exceptions/DataException.vm");
        
        File path = createDir(getBaseDirectory() + "." + EXCEPTIONS_PACKAGE);
        File file = new File(path, "DataException.java");
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        
        template.merge(context, writer);
        
        writer.flush();
        writer.close();
    }
    
    private void buildEmptyPropertyException() throws Exception, ResourceNotFoundException, ParseErrorException {
        context.put("package", buildPackage(EXCEPTIONS_PACKAGE));

        Template template = Velocity.getTemplate("templates/data/exceptions/EmptyPropertyException.vm");
        
        File path = createDir(getBaseDirectory() + "." + EXCEPTIONS_PACKAGE);
        File file = new File(path, "EmptyPropertyException.java");
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        
        template.merge(context, writer);
        
        writer.flush();
        writer.close();
    }

    public Application getApplication() {
        return application;
    }
    
    private String getBaseDirectory() {
        return getApplication().getBaseDirectory() + "." + 
                getApplication().getSourceDirectory() + "." + 
                getApplication().getBasePackage() + "." + BASE_PACKAGE;
    }

    private void createDirectories() {
        createDir(getBaseDirectory());
    }
    
    protected File createDir(String pkgName) {
        String dirName = pkgName.replace('.', '/');

        File baseDir = new File(dirName);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        
        return baseDir;
    }
    
    private String buildPackage(String suffix) {
        return getApplication().getBasePackage() + "." + BASE_PACKAGE + 
                "." + suffix;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
    
    protected VelocityContext getContext() {
        return context;
    }
}
