package appbuilder.wizards.gui.data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import appbuilder.exception.ApplicationBuilderException;
import appbuilder.util.JarLoader;
import appbuilder.util.MessageUtil;
import appbuilder.wizards.gui.WizardPage;
import appbuilder.wizards.gui.data.validator.DatabaseSettingsValidator;
import appbuilder.wizards.gui.validation.ComponentFactory;
import appbuilder.wizards.gui.validation.ValidationMessage;
import appbuilder.wizards.gui.validation.Validator;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class DataWizardGUI extends WizardPage implements ActionListener {

    private static final long serialVersionUID = 2395291743973397295L;
    //I18n keys
    private static final String DATABASE_PRESETS_I18N = "wizard.data.separator.databasePresets";
    private static final String DATABASE_I18N = "wizard.data.label.database";
    private static final String DATABASE_SETTINGS_I18N = "wizard.data.separator.databaseSettings";
    private static final String DRIVER_I18N = "wizard.data.label.driver";
    private static final String DRIVER_PATH_I18N = "wizard.data.label.driverPath";
    private static final String URL_I18N = "wizard.data.label.url";
    private static final String AUTHENTICATION_SETTINGS_I18N = "wizard.data.separator.authenticationSettings";
    private static final String USER_I18N = "wizard.data.label.user";
    private static final String PASSWORD_I18N = "wizard.data.label.password";
    private static final String TEST_CONNECTION_I18N = "wizard.data.button.testConnection";
    private static final String CLASS_NOT_FOUND_I18N = "wizard.data.message.classNotFound";
    private static final String MESSAGE_ERROR_TITLE_I18N = "wizard.data.message.titleError";
    private static final String MESSAGE_SUCCESS_TITLE_I18N = "wizard.data.message.titleSuccess";
    private static final String MESSAGE_SQL_EXCEPTION_I18N = "wizard.data.message.sqlException";
    private static final String CONNECT_SUCCESS_I18N = "wizard.data.message.connectSuccess";
    private static final String WAIT_MESSAGE = "wizard.data.wait.message";
    private static final String JAR_DESCRIPTION = "wizard.data.jar.description";
    private static final String DATABASE_FILE = "wizard.data.database.file";
    private static final String WINDOW_TITLE = "wizard.data.title";
    //Action Command Constant
    private static final String TEST_CONNECTION = "testConnection";
    private static final String OPEN_JAR_FILE = "openJarFile";
    //Keys for storing values in Wizard's context
    private static final String DRIVER = "driver";
    private static final String URL = "url";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    //Constants for referring XML's document entities of the wizard config file
    private static final String XML_DRIVER_PATH_TEXT = "driverPath";
    private static final String XML_URL_TEXT = "url";
    private static final String XML_DRIVER_TEXT = "driver";
    private static final String XML_ORDER_ATTRIBUTE = "order";
    private static final String XML_NAME_ATTRIBUTE = "name";
    //General constants
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String JAR_EXTENSION = ".jar";
    private JComboBox databases;
    private JTextField driver;
    private JTextField driverPath;
    private JTextField url;
    private JTextField user;
    private JPasswordField password;
    private JButton testConnection;
    private List<DatabaseInfo> databaseInfo;
    private Validator validator;

    public DataWizardGUI() {
        super(MessageUtil.getMessage(WINDOW_TITLE));
        setupLayout();
    }

    private void setupLayout() {
        String columns = "right:max(50dlu;pref),right:pref,3dlu,80dlu,7dlu,pref,3dlu,80dlu,right:max(50dlu;pref)";
        String rows = "p,3dlu,p,3dlu,p,3dlu,p,3dlu,p,3dlu,p,3dlu,p,3dlu,p,7dlu,p";

        databaseInfo = loadDatabaseInfo();

        databases = new JComboBox(getDatabaseNames());
        databases.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                changeDatabase();
            }
        });

        FormLayout layout = new FormLayout(columns, rows);
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        validator = new DatabaseSettingsValidator();

        driverPath = ComponentFactory.createTextField(DatabaseSettingsValidator.PATH, validator);
        driver     = ComponentFactory.createTextField(DatabaseSettingsValidator.DRIVER, validator);
        url        = ComponentFactory.createTextField(DatabaseSettingsValidator.URL, validator);
        user       = ComponentFactory.createTextField(DatabaseSettingsValidator.USER, validator);
        password   = ComponentFactory.createPasswordField(DatabaseSettingsValidator.PASSWORD, validator);

        FormLayout l2 = new FormLayout("235dlu,3dlu,p", "p");
        PanelBuilder b2 = new PanelBuilder(l2);
        b2.add(driverPath, cc.xy(1, 1));
        b2.add(createButton("...", OPEN_JAR_FILE), cc.xy(3, 1));

        builder.addSeparator(MessageUtil.getMessage(DATABASE_PRESETS_I18N), cc.xyw(1, 1, 9));
        builder.addLabel(MessageUtil.getMessage(DATABASE_I18N), cc.xy(2, 3));
        builder.add(databases, cc.xyw(4, 3, 6));
        builder.addSeparator(MessageUtil.getMessage(DATABASE_SETTINGS_I18N), cc.xyw(1, 5, 9));
        builder.addLabel(MessageUtil.getMessage(DRIVER_I18N), cc.xy(2, 7));
        builder.add(driver, cc.xyw(4, 7, 6));
        builder.addLabel(MessageUtil.getMessage(DRIVER_PATH_I18N), cc.xy(2, 9));
        builder.add(b2.getPanel(), cc.xyw(4, 9, 6));
        builder.addLabel(MessageUtil.getMessage(URL_I18N), cc.xy(2, 11));
        builder.add(url, cc.xyw(4, 11, 6));
        builder.addSeparator(MessageUtil.getMessage(AUTHENTICATION_SETTINGS_I18N), cc.xyw(1, 13, 9));
        builder.addLabel(MessageUtil.getMessage(USER_I18N), cc.xy(2, 15));
        builder.add(user, cc.xy(4, 15));
        builder.addLabel(MessageUtil.getMessage(PASSWORD_I18N), cc.xy(6, 15));
        builder.add(password, cc.xy(8, 15));

        testConnection = createButton(MessageUtil.getMessage(TEST_CONNECTION_I18N), TEST_CONNECTION);
        builder.add(ButtonBarFactory.buildCenteredBar(testConnection), cc.xyw(1, 17, 9));

        this.add(builder.getPanel());
    }

    private String[] getDatabaseNames() {
        String[] dbNames = new String[databaseInfo.size()];

        for (int i = 0; i < dbNames.length; i++) {
            dbNames[i] = databaseInfo.get(i).getName();
        }

        return dbNames;
    }

    private JButton createButton(String label, String command) {
        JButton button = new JButton(label);

        button.addActionListener(this);
        button.setActionCommand(command);

        return button;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals(TEST_CONNECTION)) {
            Runnable task = new Runnable() {

                @Override
                public void run() {
                    DataWizardGUI.this.getWizard().showOverlay(
                            MessageUtil.getMessage(WAIT_MESSAGE));

                    try {
                        testConnection();
                        DataWizardGUI.this.getWizard().hideOverlay();
                        JOptionPane.showMessageDialog(DataWizardGUI.this,
                                MessageUtil.getMessage(CONNECT_SUCCESS_I18N),
                                MessageUtil.getMessage(MESSAGE_SUCCESS_TITLE_I18N),
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (ApplicationBuilderException ex) {
                        DataWizardGUI.this.getWizard().hideOverlay();
                        JOptionPane.showMessageDialog(DataWizardGUI.this,
                                ex.getMessage(),
                                MessageUtil.getMessage(MESSAGE_ERROR_TITLE_I18N),
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            };

            new Thread(task).start();
        } else if (ae.getActionCommand().equals(OPEN_JAR_FILE)) {
            openJarFile();
        }
    }

    private void openJarFile() {
        JFileChooser chooser = new JFileChooser();

        chooser.setFileFilter(createFileFilter(JAR_EXTENSION,
                MessageUtil.getMessage(JAR_DESCRIPTION)));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile() != null) {
                driverPath.setText(chooser.getSelectedFile().getPath());
            }
        }
    }

    private void testConnection() throws ApplicationBuilderException {
        try {
            JarLoader.addFileToClasspath(driverPath.getText());
        } catch (Exception e) {
            throw new ApplicationBuilderException(e);
        }

        try {
            Class.forName(driver.getText());
        } catch (ClassNotFoundException e) {
            throw new ApplicationBuilderException(
                    MessageUtil.getMessage(CLASS_NOT_FOUND_I18N, driver.getText()));
        }

        try {
            if (user.getText().trim().length() > 0 && password.getPassword().length > 0) {
                DriverManager.getConnection(url.getText(), user.getText(),
                        new String(password.getPassword()));
            } else {
                DriverManager.getConnection(url.getText());
            }
        } catch (SQLException e) {
            throw new ApplicationBuilderException(
                    MessageUtil.getMessage(MESSAGE_SQL_EXCEPTION_I18N, e.getMessage()));
        }
    }

    public boolean canGoNext() {
        return true;
    }
    boolean canReturn = false;
    Object blocker = new Object();

    @Override
    public boolean onBeforeNext() {
        Runnable task = new Runnable() {

            @Override
            public void run() {
                DataWizardGUI.this.getWizard().showOverlay(
                        MessageUtil.getMessage(WAIT_MESSAGE));

                List<ValidationMessage> messages = validator.validate();
                if (messages.isEmpty()) {
                    //Store the values of the fields in wizard's context
                    addProperty(DRIVER, driver.getText());
                    addProperty(URL, url.getText());
                    addProperty(USER, user.getText());
                    addProperty(PASSWORD, new String(password.getPassword()));

                    try {
                        testConnection();
                        canReturn = true;
                        DataWizardGUI.this.getWizard().hideOverlay();
                    } catch (ApplicationBuilderException ex) {
                        DataWizardGUI.this.getWizard().hideOverlay();
                        JOptionPane.showMessageDialog(DataWizardGUI.this,
                                ex.getMessage(),
                                MessageUtil.getMessage(MESSAGE_ERROR_TITLE_I18N),
                                JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    StringBuilder messageBuffer = new StringBuilder();

                    for (ValidationMessage message : messages) {
                        messageBuffer.append(message.getMessage() + NEW_LINE);
                    }

                    DataWizardGUI.this.getWizard().hideOverlay();
                    JOptionPane.showMessageDialog(DataWizardGUI.this,
                            messageBuffer.toString(),
                            MessageUtil.getMessage(MESSAGE_ERROR_TITLE_I18N),
                            JOptionPane.WARNING_MESSAGE);
                }

                synchronized (DataWizardGUI.this) {
                    DataWizardGUI.this.notifyAll();
                }
            }
        };

        new Thread(task).start();

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        return canReturn;
    }

    private void changeDatabase() {
        driver.setText(databaseInfo.get(databases.getSelectedIndex()).getDriver());
        url.setText(databaseInfo.get(databases.getSelectedIndex()).getUrl());
        driverPath.setText(databaseInfo.get(databases.getSelectedIndex()).getDriverPath());
    }

    private List<DatabaseInfo> loadDatabaseInfo() {
        List<DatabaseInfo> result = createList();

        SAXBuilder parser = new SAXBuilder();

        try {
            Document doc = parser.build(new File(MessageUtil.getMessage(DATABASE_FILE)));
            Element databases = doc.getRootElement();

            for (Object obj : databases.getChildren()) {
                Element db = (Element) obj;
                String name = db.getAttributeValue(XML_NAME_ATTRIBUTE);
                int order = Integer.parseInt(db.getAttributeValue(XML_ORDER_ATTRIBUTE));

                DatabaseInfo info = new DatabaseInfo(order, name);
                info.setDriver(db.getChildText(XML_DRIVER_TEXT));
                info.setUrl(db.getChildText(XML_URL_TEXT));
                info.setDriverPath(db.getChildText(XML_DRIVER_PATH_TEXT));

                result.add(info);
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(result);

        return result;
    }

    private <T> List<T> createList() {
        return new ArrayList<T>();
    }

    private FileFilter createFileFilter(String extension, String description) {
        final String ext = extension;
        final String dsc = description;

        return new FileFilter() {
            @Override
            public String getDescription() {
                return dsc;
            }

            @Override
            public boolean accept(File f) {
                if (f != null) {
                    if (f.isDirectory()) {
                        return true;
                    }

                    return f.getName().toLowerCase().endsWith(ext.toLowerCase());
                }

                return false;
            }
        };
    }
}
