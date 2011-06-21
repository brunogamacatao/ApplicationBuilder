package appbuilder.application.data.builder.jdbc;

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

import appbuilder.application.Application;
import appbuilder.application.data.Column;
import appbuilder.application.data.Table;
import appbuilder.application.data.builder.DataBuilder;
import appbuilder.application.util.StringUtils;
import appbuilder.application.util.Toolkit;

import com.google.inject.Inject;

/**
 * @author Bruno Gama Catão
 * @since 2007-12-27
 */
public class JdbcDataBuilder extends DataBuilder {

    public static final String JDBC_BASE_PACKAGE = "data.jdbc";
    public static final String UTIL_PACKAGE = "util";
    public static final String ENTITIES_PACKAGE = "entities";
    public static final String DAO_PACKAGE = "dao";
    private static Logger logger = Logger.getLogger(JdbcDataBuilder.class.getName());
    private String driver;
    private String url;
    private String user;
    private String password;

    @Inject
    public JdbcDataBuilder(VelocityContext context, Application application, 
            @Driver String driver, 
            @Url String url) {
        super(context, application);

        this.driver = driver;
        this.url = url;
    }

    @Override
    public void build() {
        super.build();

        try {
            logger.info("creating the directories for jdbc technology");
            createDirectories();
            logger.info("setting up the generation context for jdbc technology");
            setupContext();
            logger.info("generating the jdbc util package and classes");
            buildJdbcUtil();
            logger.info("generating the jdbc entities package and classes");
            buildEntities();
            logger.info("generating the jdbc dao package and classes");
            buildDaos();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private void setupContext() {
        context.put("driver", driver);
        context.put("url", url);
        context.put("user", user);
        context.put("password", password);
    }

    private void buildJdbcUtil() throws Exception, ResourceNotFoundException, ParseErrorException {
        context.put("package", buildPackage(UTIL_PACKAGE));

        Template template = Velocity.getTemplate("templates/data/jdbc/JdbcUtil.vm");

        File path = createDir(getBaseDirectory() + "." + UTIL_PACKAGE);
        File file = new File(path, "JdbcUtil.java");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        template.merge(context, writer);

        writer.flush();
        writer.close();
    }

    private void buildEntities() throws Exception, ResourceNotFoundException, ParseErrorException {
        context.put("package", buildPackage(ENTITIES_PACKAGE));
        context.put("daoPackage", buildPackage(DAO_PACKAGE));

        Template template = Velocity.getTemplate("templates/data/jdbc/Entity.vm");

        File path = createDir(getBaseDirectory() + "." + ENTITIES_PACKAGE);

        for (Table table : getApplication().getDataSource().getTables()) {
            String className = StringUtils.getInstance().tableToClassName(table.getName());
            context.put("className", className);
            context.put("table", table);

            Collection<Table> ref = getReferencesTo(table);
            if (!ref.isEmpty()) {
                context.put("referencesTo", ref);
            } else {
                if (context.containsKey("referencesTo")) {
                    context.remove("referencesTo");
                }
            }

            File file = new File(path, className + ".java");

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            template.merge(context, writer);

            writer.flush();
            writer.close();
        }
    }

    private void buildDaos() throws Exception, ResourceNotFoundException, ParseErrorException {
        context.put("package", buildPackage(DAO_PACKAGE));
        context.put("utilPackage", buildPackage(UTIL_PACKAGE));
        context.put("entitiesPackage", buildPackage(ENTITIES_PACKAGE));

        Template template = Velocity.getTemplate("templates/data/jdbc/Dao.vm");

        File path = createDir(getBaseDirectory() + "." + DAO_PACKAGE);

        for (Table table : getApplication().getDataSource().getTables()) {
            String className = StringUtils.getInstance().tableToClassName(table.getName()) + "DAO";
            context.put("className", className);
            context.put("entityName", StringUtils.getInstance().tableToClassName(table.getName()));
            context.put("table", table);

            File file = new File(path, className + ".java");

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            template.merge(context, writer);

            writer.flush();
            writer.close();
        }

    }

    private String buildPackage(String suffix) {
        return getApplication().getBasePackage() + "." + JDBC_BASE_PACKAGE +
                "." + suffix;
    }

    private String getBaseDirectory() {
        return getApplication().getBaseDirectory() + "." +
                getApplication().getSourceDirectory() + "." +
                getApplication().getBasePackage() + "." + JDBC_BASE_PACKAGE;
    }

    private void createDirectories() {
        createDir(getBaseDirectory());
    }

    private Collection<Table> getReferencesTo(Table table) {
        Collection<Table> result = Toolkit.createCollection();

        for (Table t : getApplication().getDataSource().getTables()) {
            if (!table.equals(t)) {
                for (Column col : t.getColumns()) {
                    if (col.getReferencedTable() != null && col.getReferencedTable().equals(table)) {
                        if (!result.contains(t)) {
                            result.add(t);
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
