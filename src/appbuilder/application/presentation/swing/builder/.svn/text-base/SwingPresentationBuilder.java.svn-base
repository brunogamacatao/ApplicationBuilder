package appbuilder.application.presentation.swing.builder;

import appbuilder.application.Application;
import appbuilder.application.config.builder.ConfigBuilder;
import appbuilder.application.data.Table;
import appbuilder.application.presentation.builder.PresentationBuilder;
import appbuilder.application.util.StringUtils;
import com.google.inject.Inject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * @author Bruno Gama Catão
 */
public class SwingPresentationBuilder extends PresentationBuilder {
    private static final String UTIL_PACKAGE = "util";
    
    private static Logger logger = Logger.getLogger(SwingPresentationBuilder.class.getName());
    
    @Inject
    public SwingPresentationBuilder(VelocityContext context, Application application) {
        super(context, application);
    }
    
    @Override
    public void build() {
        super.build();
        
        try {
            logger.info("configuring the libraries needed for the Swing presentation layer");
            setupLibraries();
            logger.info("setting up the generation context for the Swing presentation layer");
            setupContext();
            logger.info("building the MDIDesktopPane utility class");
            buildMDIDesktopPane();
            logger.info("building the WindowMenu utility class");
            buildWindowMenu();
            logger.info("building the ApplicationFrame class");
            buildApplicationFrame();
            logger.info("building the maintanance window classes");
            buildMaintenanceWindows();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    private void setupLibraries() {
        getApplication().addLibrary("lib/binding-2.0.0.jar");
        getApplication().addLibrary("lib/forms-1.1.0.jar");
        getApplication().addLibrary("lib/looks-2.1.4.jar");
    }
    
    private void setupContext() {
        context.put("package", getApplication().getBasePackage() + "." + BASE_PACKAGE);
        context.put("packageUtil", getApplication().getBasePackage() + "." + ConfigBuilder.UTIL_PACKAGE);
    }
    
    private void buildMDIDesktopPane() throws Exception, ResourceNotFoundException, ParseErrorException {
        Template template = Velocity.getTemplate("templates/presentation/swing/MDIDesktopPane.vm");

        File path = createDir(getBaseDirectory() + "." + UTIL_PACKAGE);
        File file = new File(path, "MDIDesktopPane.java");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        template.merge(context, writer);

        writer.flush();
        writer.close();
    }
    
    private void buildWindowMenu() throws Exception, ResourceNotFoundException, ParseErrorException {
        Template template = Velocity.getTemplate("templates/presentation/swing/WindowMenu.vm");

        File path = createDir(getBaseDirectory() + "." + UTIL_PACKAGE);
        File file = new File(path, "WindowMenu.java");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        template.merge(context, writer);

        writer.flush();
        writer.close();
    }
    
    private void buildApplicationFrame() throws Exception, ResourceNotFoundException, ParseErrorException {
        Template template = Velocity.getTemplate("templates/presentation/swing/ApplicationFrame.vm");

        String className = StringUtils.getInstance().applicationToClassName(this.getApplication().getName()) + "Frame";
        context.put("className", className);
        
        application.setMainClass(context.get("package") + "." + className);
        
        File path = createDir(getBaseDirectory());
        File file = new File(path, className + ".java");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        template.merge(context, writer);

        writer.flush();
        writer.close();
    }
    
    private void buildMaintenanceWindows() throws Exception, ResourceNotFoundException, ParseErrorException {
        Template template = Velocity.getTemplate("templates/presentation/swing/InternalFrame.vm");

        File path = createDir(getBaseDirectory());
        
        for (Table table : getApplication().getDataSource().getTables()) {
            String className = StringUtils.getInstance().tableToClassName(table.getName()) + "IFrame";
            context.put("className", className);
            context.put("table", table);

            File file = new File(path, className + ".java");
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            template.merge(context, writer);

            writer.flush();
            writer.close();
        }
    }
}
