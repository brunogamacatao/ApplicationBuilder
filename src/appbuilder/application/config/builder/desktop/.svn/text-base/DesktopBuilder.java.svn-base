package appbuilder.application.config.builder.desktop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import appbuilder.application.Application;
import appbuilder.application.config.builder.ConfigBuilder;

import com.google.inject.Inject;

/**
 * @author Bruno Gama Catão
 * @since 2007-12-28
 */
public class DesktopBuilder extends ConfigBuilder {

    private static Logger logger = Logger.getLogger(DesktopBuilder.class.getName());

    @Inject
    public DesktopBuilder(VelocityContext context, Application application) {
        super(context, application);
    }

    @Override
    public void build() {
        try {
            super.build();
            logger.info("generating an ANT build file");
            generateAntBuilder();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private void generateAntBuilder() throws Exception {
        Template template = Velocity.getTemplate("templates/build.vm");

        File path = createDir(getApplication().getBaseDirectory());
        File file = new File(path, "build.xml");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        template.merge(context, writer);

        writer.flush();
        writer.close();
    }
}
