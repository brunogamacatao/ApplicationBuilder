package appbuilder.wizards.gui.data;

import appbuilder.wizards.data.DataWizard.DatabaseObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import appbuilder.application.data.DataSource;
import appbuilder.application.data.Table;
import appbuilder.util.MessageUtil;
import appbuilder.wizards.data.DataWizard;
import appbuilder.wizards.data.ProgressListener;
import appbuilder.wizards.gui.SimpleListModel;
import appbuilder.wizards.gui.WizardPage;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class SelectTablesGUI extends WizardPage implements ActionListener {

    private static final String TABLES = "tables";
    private static final long serialVersionUID = 3676542947997184006L;
    //I18n Keys
    private static final String ALL_TABLES_I18N = "wizard.selectTables.label.allTables";
    private static final String SELECTED_TABLES_I18N = "wizard.selectTables.label.selectedTables";
    private static final String WINDOW_TITLE = "wizard.selectTables.title";
    private static final String WAIT_MESSAGE = "wizard.data.wait.message";
    private static final String LOADING_MESSAGE = "wizard.selectTables.loading";
    //Action Command Constants
    private static final String ALL_RIGHT = "all right";
    private static final String ONE_RIGHT = "one right";
    private static final String ALL_LEFT = "all left";
    private static final String ONE_LEFT = "one left";
    //Keys for storing values in Wizard's context
    private static final String DRIVER = "driver";
    private static final String URL = "url";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private JList tables;
    private JList selectedTables;
    private SimpleListModel<Table> tablesModel;
    private SimpleListModel<Table> selectedTablesModel;

    public SelectTablesGUI() {
        super(MessageUtil.getMessage(WINDOW_TITLE));
        setupLayout();
    }

    private void setupLayout() {
        tablesModel = new SimpleListModel<Table>();
        selectedTablesModel = new SimpleListModel<Table>();

        tables = new JList(tablesModel);
        selectedTables = new JList(selectedTablesModel);

        selectedTablesModel.addListDataListener(new ListDataListener() {

            @Override
            public void intervalRemoved(ListDataEvent e) {
                checkFinishState();
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                checkFinishState();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                checkFinishState();
            }

            private void checkFinishState() {
                if (selectedTablesModel.getElements().isEmpty()) {
                    getWizard().disableFinish();
                } else {
                    getWizard().enableFinish();
                }
            }
        });

        String columns = "pref,3dlu,pref,3dlu,pref";
        String rows = "p,3dlu,7dlu,p,p,p,p,7dlu";

        FormLayout layout = new FormLayout(columns, rows);
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        layout.setColumnGroups(new int[][]{{1, 5}});
        builder.setDefaultDialogBorder();

        builder.addLabel(MessageUtil.getMessage(ALL_TABLES_I18N), cc.xy(1, 1));
        builder.addLabel(MessageUtil.getMessage(SELECTED_TABLES_I18N), cc.xy(5, 1));
        builder.add(new JScrollPane(tables), cc.xywh(1, 3, 1, 6));
        builder.add(new JScrollPane(selectedTables), cc.xywh(5, 3, 1, 6));
        builder.add(createButton(">", ONE_RIGHT), cc.xy(3, 4));
        builder.add(createButton("»", ALL_RIGHT), cc.xy(3, 5));
        builder.add(createButton("<", ONE_LEFT), cc.xy(3, 6));
        builder.add(createButton("«", ALL_LEFT), cc.xy(3, 7));

        this.add(builder.getPanel());
    }

    @Override
    public boolean canGoNext() {
        return true;
    }

    @Override
    public void onBeforeShow() {
        //Clean the tables list
        tablesModel.clear();

        //Obtain values from Wizard's context
        String driver = (String) getProperty(DRIVER);
        String url = (String) getProperty(URL);
        String user = (String) getProperty(USER);
        String password = (String) getProperty(PASSWORD);

        this.getWizard().showOverlay(MessageUtil.getMessage(WAIT_MESSAGE));
        
        try {
            DataWizard wizard = new DataWizard(driver, url, user, password);
            
            wizard.addProgressListener(new ProgressListener() {
                public void loadingObject(DatabaseObject object) {
                    getWizard().showOverlay(MessageUtil.getMessage(LOADING_MESSAGE, object.name));
                }
            });
            
            DataSource ds = wizard.getDataSource();

            for (Table table : ds.getTables()) {
                tablesModel.addElement(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.getWizard().hideOverlay();
        }
    }

    @Override
    public boolean onBeforeFinish() {
        //Storing the selected tables into wizard's context
        addProperty(TABLES, selectedTablesModel.getElements());

        return true;
    }

    private JButton createButton(String label, String command) {
        JButton button = new JButton(label);

        button.addActionListener(this);
        button.setActionCommand(command);

        return button;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals(ONE_LEFT)) {
            oneLeft();
        } else if (ae.getActionCommand().equals(ALL_LEFT)) {
            allLeft();
        } else if (ae.getActionCommand().equals(ONE_RIGHT)) {
            oneRight();
        } else if (ae.getActionCommand().equals(ALL_RIGHT)) {
            allRight();
        }
    }

    /**
     * Move the selected elements from the left list to the right list.
     */
    private void oneLeft() {
        if (selectedTables.getSelectedValues().length == 0) {
            return;
        }

        /*
         * Create a temporary collection to hold the selected elements in order
         * to avoid a ConcurrentModificationException.
         */
        synchronized (selectedTables) {
            Collection<Table> temp = createCollection();

            for (Object obj : selectedTables.getSelectedValues()) {
                temp.add((Table) obj);
            }

            //Remove the elements from the source list
            for (Table table : temp) {
                selectedTablesModel.removeElement(table);
            }

            //Add the elements in the destination list
            synchronized (tablesModel) {
                tablesModel.addElements(temp);
            }
        }
    }

    /**
     * Move all the elements from the left list to the right list.
     */
    private void allLeft() {
        if (selectedTablesModel.getElements().isEmpty()) {
            return;
        }

        /*
         * Create a temporary collection to hold the selected elements in order
         * to avoid a ConcurrentModificationException.
         */
        synchronized (selectedTables) {
            Collection<Object> temp = createCollection();

            temp.addAll(selectedTablesModel.getElements());

            //Remove the elements from the source list
            selectedTablesModel.clear();

            //Add the elements in the destination list
            synchronized (tablesModel) {
                for (Object obj : temp) {
                    tablesModel.addElement((Table) obj);
                }
            }
        }
    }

    /**
     * Move the selected elements from the right list to the left list.
     */
    private void oneRight() {
        if (tables.getSelectedValues().length == 0) {
            return;
        }

        /*
         * Create a temporary collection to hold the selected elements in order
         * to avoid a ConcurrentModificationException.
         */
        synchronized (tables) {
            Collection<Table> temp = createCollection();

            for (Object obj : tables.getSelectedValues()) {
                temp.add((Table) obj);
            }

            //Remove the elements from the source list
            for (Table table : temp) {
                tablesModel.removeElement(table);
            }

            //Add the elements in the destination list
            synchronized (selectedTablesModel) {
                selectedTablesModel.addElements(temp);
            }
        }
    }

    /**
     * Move all the elements from the right list to the left list.
     */
    private void allRight() {
        if (tablesModel.getElements().isEmpty()) {
            return;
        }

        /*
         * Create a temporary collection to hold the selected elements in order
         * to avoid a ConcurrentModificationException.
         */
        synchronized (tables) {
            Collection<Object> temp = createCollection();

            temp.addAll(tablesModel.getElements());

            //Remove the elements from the source list
            tablesModel.clear();

            //Add the elements in the destination list
            synchronized (selectedTablesModel) {
                for (Object obj : temp) {
                    selectedTablesModel.addElement((Table) obj);
                }
            }
        }
    }

    private <T> Collection<T> createCollection() {
        return new ArrayList<T>();
    }
}
