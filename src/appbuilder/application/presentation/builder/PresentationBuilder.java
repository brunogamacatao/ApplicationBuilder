package appbuilder.application.presentation.builder;

import appbuilder.application.Application;
import appbuilder.builder.Builder;
import com.google.inject.Inject;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.velocity.VelocityContext;

/**
 * @author Bruno Gama Catão
 */
public class PresentationBuilder implements Builder {
    public static final String BASE_PACKAGE = "gui";
    private static Logger logger = Logger.getLogger(PresentationBuilder.class.getName());
    protected Application application;
    protected VelocityContext context;

    @Inject
    public PresentationBuilder(VelocityContext context, Application application) {
        this.application = application;
        this.context = context;
    }

    @Override
    public void build() {
        try {
            logger.info("creating the general database directories");
            createDirectories();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
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
}
