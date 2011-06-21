package appbuilder.application.logic.builder.simple;

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
import appbuilder.application.data.Table;
import appbuilder.application.data.builder.jdbc.JdbcDataBuilder;
import appbuilder.application.logic.builder.LogicBuilder;
import appbuilder.application.util.StringUtils;

import com.google.inject.Inject;

public class SimpleLogicBuilder extends LogicBuilder {

    public static final String BO_PACKAGE = "bo";
    private static Logger logger = Logger.getLogger(LogicBuilder.class.getName());

    @Inject
    public SimpleLogicBuilder(VelocityContext context, Application application) {
        super(context, application);
    }

    @Override
    public void build() {
        super.build();

        try {
            logger.info("creating the directories for the logic classes");
            createDirectories();
            logger.info("generating the BO classes");
            buildBO();
            logger.info("generating the Facade classe");
            buildFacade();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private void createDirectories() {
        createDir(getBaseDirectory());
    }

    private void buildBO() throws Exception, ResourceNotFoundException, ParseErrorException {
        context.put("package", buildPackage(BASE_PACKAGE, BO_PACKAGE));
        context.put("entitiesPackage", buildPackage(JdbcDataBuilder.JDBC_BASE_PACKAGE, JdbcDataBuilder.ENTITIES_PACKAGE));
        context.put("daoPackage", buildPackage(JdbcDataBuilder.JDBC_BASE_PACKAGE, JdbcDataBuilder.DAO_PACKAGE));

        Template template = Velocity.getTemplate("templates/logic/simple/BO.vm");

        File path = createDir(getBaseDirectory() + "." + BO_PACKAGE);

        for (Table table : this.getApplication().getDataSource().getTables()) {
            String className = StringUtils.getInstance().tableToClassName(table.getName()) + "BO";
            String daoName = StringUtils.getInstance().tableToClassName(table.getName()) + "DAO";
            context.put("className", className);
            context.put("daoName", daoName);
            context.put("entityName", StringUtils.getInstance().tableToClassName(table.getName()));
            context.put("table", table);

            File file = new File(path, className + ".java");

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            template.merge(context, writer);

            writer.flush();
            writer.close();
        }
    }

    private void buildFacade() throws Exception, ResourceNotFoundException, ParseErrorException {
        context.put("package", getApplication().getBasePackage() + "." + BASE_PACKAGE);
        context.put("entitiesPackage", buildPackage(JdbcDataBuilder.JDBC_BASE_PACKAGE, JdbcDataBuilder.ENTITIES_PACKAGE));
        context.put("boPackage", buildPackage(BASE_PACKAGE, BO_PACKAGE));

        Template template = Velocity.getTemplate("templates/logic/simple/Facade.vm");

        File path = createDir(getBaseDirectory());

        String className = StringUtils.getInstance().applicationToClassName(this.getApplication().getName()) + "Facade";

        context.put("className", className);
        context.put("dataSource", this.getApplication().getDataSource());

        File file = new File(path, className + ".java");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        template.merge(context, writer);

        writer.flush();
        writer.close();
    }
}
