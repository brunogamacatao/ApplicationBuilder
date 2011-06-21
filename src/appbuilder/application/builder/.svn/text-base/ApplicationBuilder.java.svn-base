package appbuilder.application.builder;

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
import appbuilder.application.config.builder.ConfigBuilder;
import appbuilder.application.data.Column;
import appbuilder.application.data.DataSource;
import appbuilder.application.data.DataType;
import appbuilder.application.data.Table;
import appbuilder.application.data.builder.DataBuilder;
import appbuilder.application.logic.builder.LogicBuilder;
import appbuilder.application.presentation.builder.PresentationBuilder;
import appbuilder.application.util.StringUtils;
import appbuilder.builder.Builder;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * This is the starting point class for the code generation of a whole 
 * application based on its metadata.
 * 
 * @author Bruno Gama Catão
 * @since 2007-12-28
 */
public class ApplicationBuilder implements Builder {

    private static Logger logger = Logger.getLogger(ApplicationBuilder.class.getName());
    private static final String UTIL_PACKAGE = "util";
    private static final String MSGS_PACKAGE = "messages";
    private Application application;
    private DataBuilder dataBuilder;
    private ConfigBuilder configBuilder;
    private LogicBuilder logicBuilder;
    private PresentationBuilder presentationBuilder;
    protected VelocityContext context;

    /**
     * This is a simple constructor which implements the builder design pattern
     * in order to assemble all the needed builders together. 
     * 
     * @param context The velocity context.
     * @param application The application metadata.
     * @param dataBuilder The data access layer builder.
     * @param configBuilder The configuration files builder.
     * @param logicBuilder The business logic builder.
     */
    @Inject
    public ApplicationBuilder(VelocityContext context, Application application,
            DataBuilder dataBuilder, ConfigBuilder configBuilder,
            LogicBuilder logicBuilder, PresentationBuilder presentationBuilder) {
        this.application = application;
        this.context = context;
        this.dataBuilder = dataBuilder;
        this.configBuilder = configBuilder;
        this.logicBuilder = logicBuilder;
        this.presentationBuilder = presentationBuilder;
    }

    /**
     * This is the template method that triggers the whole building process.
     * It calls the correct builders for each layer of the system in the correct
     * order.
     */
    public void build() {
        logger.info("cleaning the directories before the generation");
        getConfigBuilder().clean();
        logger.info("setting up the general generation context");
        setupContext();
        logger.info("calling the database builder");
        getDataBuilder().build();
        logger.info("calling the logic builder");
        getLogicBuilder().build();
        logger.info("calling the presentation builder");
        getPresentationBuilder().build();
        logger.info("calling the base configuration builder");
        getConfigBuilder().build();

        logger.info("building the constants class");
        try {
            buildConstants();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        logger.info("building the messages file");
        try {
            buildMessagesFile();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method sets up the velicity generation context.
     */
    private void setupContext() {
        context.put("application", getApplication());
        context.put("stringUtils", StringUtils.getInstance());
        context.put("appUtilPackage", buildPackage(UTIL_PACKAGE));
    }

    /**
     * This method generates a classs that holds all the application's 
     * constants.
     */
    private void buildConstants() throws ResourceNotFoundException, ParseErrorException, Exception {
        context.put("package", buildPackage(UTIL_PACKAGE));

        Template template = Velocity.getTemplate("templates/util/Constants.vm");

        File path = createDir(getBaseDirectory() + "." + UTIL_PACKAGE);
        File file = new File(path, "Constants.java");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        template.merge(context, writer);

        writer.flush();
        writer.close();
    }

    /**
     * This method generates the Messages (I18n) file of the application.
     */
    private void buildMessagesFile() throws ResourceNotFoundException, ParseErrorException, Exception {
        Template template = Velocity.getTemplate("templates/messages/Messages.vm");

        File path = createDir(getApplication().getBaseDirectory() + "." +
                getApplication().getSourceDirectory() + "." + MSGS_PACKAGE);
        File file = new File(path, "Messages.properties");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        template.merge(context, writer);

        writer.flush();
        writer.close();
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

    public DataBuilder getDataBuilder() {
        return dataBuilder;
    }

    public Application getApplication() {
        return application;
    }

    public ConfigBuilder getConfigBuilder() {
        return configBuilder;
    }

    public LogicBuilder getLogicBuilder() {
        return logicBuilder;
    }
    
    public PresentationBuilder getPresentationBuilder() {
        return presentationBuilder;
    }

    public static void main(String[] args) throws Exception {
        Table pessoa = new Table("PESSOA");

        Column idPessoa = new Column("ID_PESSOA", DataType.INTEGER);
        idPessoa.setPrimaryKey(true);
        idPessoa.setPrecision(0, 4);
        Column nmPessoa = new Column("NM_PESSOA", DataType.VARCHAR);
        nmPessoa.setNullable(false);
        nmPessoa.setPrecision(0, 100);
        Column alturaPessoa = new Column("ALTURA_PESSOA", DataType.NUMBER);
        alturaPessoa.setNullable(false);
        alturaPessoa.setPrecision(new int[]{1, 2});
        Column pesoPessoa = new Column("PESO_PESSOA", DataType.NUMBER);
        pesoPessoa.setNullable(false);
        pesoPessoa.setPrecision(new int[]{3, 2});

        pessoa.addColumn(idPessoa);
        pessoa.addColumn(nmPessoa);
        pessoa.addColumn(alturaPessoa);
        pessoa.addColumn(pesoPessoa);

        Table escola = new Table("ESCOLA");

        Column idEscola = new Column("ID_ESCOLA", DataType.INTEGER);
        Column nmEscola = new Column("NM_ESCOLA", DataType.VARCHAR);
        Column paisEscola = new Column("PAIS_ESCOLA", DataType.VARCHAR);
        Column idDonoEscola = new Column("ID_DONO_ESCOLA", DataType.INTEGER);

        idEscola.setPrimaryKey(true);
        idEscola.setPrecision(0, 4);
        nmEscola.setNullable(false);
        nmEscola.setPrecision(0, 100);
        paisEscola.setNullable(false);
        paisEscola.setPrecision(0, 50);
        idDonoEscola.setReference(idPessoa, pessoa);
        idDonoEscola.setPrecision(0, 4);

        escola.addColumn(idEscola);
        escola.addColumn(nmEscola);
        escola.addColumn(paisEscola);
        escola.addColumn(idDonoEscola);

        DataSource ds = new DataSource("myDS");
        ds.addTable(pessoa);
        ds.addTable(escola);

        Application app = new Application("Test Application");

        app.setDataSource(ds);
        app.setAuthor("Bruno Gama Catão");
        app.setDescription("This is a test application");
        app.setBaseDirectory("c:/generated/test");
        app.setBasePackage("org.app");
        app.setSourceDirectory("src");
        app.setBuildDirectory("build");
        app.setDistributionDirectory("dist");
        app.setLibDirectory("lib");

        Injector injector = Guice.createInjector(new DesktopModule(app, "org.mydriver", "mydb:a:b"));
        ApplicationBuilder builder = (ApplicationBuilder) injector.getInstance(ApplicationBuilder.class);

        builder.build();
    }
}
