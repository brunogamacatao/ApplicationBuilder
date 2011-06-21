package appbuilder.application.builder;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import appbuilder.application.Application;
import appbuilder.application.config.builder.ConfigBuilder;
import appbuilder.application.config.builder.desktop.DesktopBuilder;
import appbuilder.application.data.builder.DataBuilder;
import appbuilder.application.data.builder.jdbc.Driver;
import appbuilder.application.data.builder.jdbc.JdbcDataBuilder;
import appbuilder.application.data.builder.jdbc.Url;
import appbuilder.application.logic.builder.LogicBuilder;
import appbuilder.application.logic.builder.simple.SimpleLogicBuilder;

import appbuilder.application.presentation.builder.PresentationBuilder;
import appbuilder.application.presentation.swing.builder.SwingPresentationBuilder;
import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * This is a GUICE module which assembles together the necessary builders that
 * generate simple desktop applications.
 *  
 * @author Bruno Gama Catão.
 */
public class DesktopModule implements Module {

    private static Logger logger = Logger.getLogger(DesktopModule.class.getName());
    private Application application;
    private String driver;
    private String url;
    private VelocityContext context;

    public DesktopModule(Application application, String driver, String url) {
        this.application = application;
        this.driver = driver;
        this.url = url;

        try {
            this.initVelocity();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void configure(Binder binder) {
        logger.info("injecting dependencies");
        binder.bind(String.class).annotatedWith(Driver.class).toInstance(driver);
        binder.bind(String.class).annotatedWith(Url.class).toInstance(url);
        binder.bind(VelocityContext.class).toInstance(context);
        binder.bind(Application.class).toInstance(application);
        binder.bind(DataBuilder.class).to(JdbcDataBuilder.class);
        binder.bind(ConfigBuilder.class).to(DesktopBuilder.class);
        binder.bind(LogicBuilder.class).to(SimpleLogicBuilder.class);
        binder.bind(PresentationBuilder.class).to(SwingPresentationBuilder.class);
    }

    private void initVelocity() throws Exception {
        logger.info("initializing the template engine");
        Velocity.init();
        context = new VelocityContext();
    }
}
