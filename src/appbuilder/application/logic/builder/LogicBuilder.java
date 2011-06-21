package appbuilder.application.logic.builder;

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
 * @since 2008-01-13
 */
public class LogicBuilder implements Builder {

    public static final String BASE_PACKAGE = "logic";
    public static final String EXCEPTIONS_PACKAGE = "exceptions";
    private static Logger logger = Logger.getLogger(LogicBuilder.class.getName());
    protected Application application;
    protected VelocityContext context;

    @Inject
    public LogicBuilder(VelocityContext context, Application application) {
        this.application = application;
        this.context = context;
    }

    @Override
    public void build() {
        try {
            logger.info("creating the general database directories");
            createDirectories();
            logger.info("setting up the general logic generation context");
            setupContext();
            logger.info("generation the general logic exceptions");
            buildLogicException();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private void setupContext() {
        context.put("logicExceptionsPackage", buildPackage(BASE_PACKAGE, EXCEPTIONS_PACKAGE));
    }

    @Override
    public Application getApplication() {
        return application;
    }

    protected String getBaseDirectory() {
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

    protected String buildPackage(String basePackage, String suffix) {
        return getApplication().getBasePackage() + "." + basePackage +
                "." + suffix;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    protected VelocityContext getContext() {
        return context;
    }

    private void buildLogicException() throws Exception, ResourceNotFoundException, ParseErrorException {
        context.put("package", buildPackage(BASE_PACKAGE, EXCEPTIONS_PACKAGE));

        Template template = Velocity.getTemplate("templates/logic/exceptions/LogicException.vm");

        File path = createDir(getBaseDirectory() + "." + EXCEPTIONS_PACKAGE);
        File file = new File(path, "LogicException.java");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        template.merge(context, writer);

        writer.flush();
        writer.close();
    }
}
