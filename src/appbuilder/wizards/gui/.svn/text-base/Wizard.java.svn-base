package appbuilder.wizards.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import appbuilder.util.MessageUtil;
import appbuilder.wizards.gui.data.DataWizardGUI;
import appbuilder.wizards.gui.data.SelectTablesGUI;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.windows.WindowsLookAndFeel;

/**
 * This class implements a simple framework for building wizards. A wizard 
 * must contain one or more pages, the wizard controls the flow of the pages.
 * But each page has hook methods that are called always before a page switch
 * in order to know if the page switch is allowed or to store values at the
 * wizard's context.
 * 
 * @author Bruno Gama Catão
 */
public class Wizard extends JDialog implements ActionListener {
    private static final long serialVersionUID = -1265471462105014397L;
    
    //Action command constants
    private static final String CANCEL = "cancel";
    private static final String FINISH = "finish";
    private static final String NEXT = "next";
    private static final String BACK = "back";
    private static final String HELP = "help";
    
    //I18n keys
    private static final String CANCEL_I18N = "wizard.button.cancel";
    private static final String FINISH_I18N = "wizard.button.finish";
    private static final String NEXT_I18N = "wizard.button.next";
    private static final String BACK_I18N = "wizard.button.back";
    private static final String HELP_I18N = "wizard.button.help";
    
    private java.util.List<WizardPage> pages;
    
    private int index;
    private JButton nextButton;
    private JButton backButton;
    private JButton finishButton;
    private PanelBuilder builder;
    private CellConstraints cc;
    private Map<String, Object> context;
    private WizardPane wizardPane;

    public Wizard() {
        super();

        this.pages = createList();
        this.context = createMap();
        this.index = 0;

        setupLookAndFeel();
        setupLayout();
    }

    /**
     * This is a helper method that creates a translucent layer over the wizard 
     * with a text message written on its center.
     * 
     * @param message The text message that will be displayed at overlay.
     */
    public void showOverlay(String message) {
        wizardPane.showOverlay(message);
    }

    /**
     * This method simple hides the overlay.
     */
    public void hideOverlay() {
        wizardPane.hideOverlay();
    }

    public void addPage(WizardPage page) {
        pages.add(page);
        page.setWizard(this);

        if (pages.size() == 1) {
            builder.add(getCurrentPage(), cc.xy(1, 1));
            this.setTitle(getCurrentPage().getTitle());
            SwingUtilities.updateComponentTreeUI(this);
        } else {
            nextButton.setEnabled(getCurrentPage().canGoNext());
        }
    }

    public void addProperty(String key, Object value) {
        context.put(key, value);
    }

    public Object getProperty(String key) {
        return context.get(key);
    }

    public void removeProperty(String key) {
        context.remove(key);
    }

    protected void setupLayout() {
        String columns = "p";
        String rows = "p,3dlu,p";

        FormLayout layout = new FormLayout(columns, rows);
        builder = new PanelBuilder(layout);
        cc = new CellConstraints();

        builder.setDefaultDialogBorder();
        builder.add(createButtonBar(), cc.xy(1, 3));

        wizardPane = new WizardPane(builder.getPanel());
        setContentPane(wizardPane);
    }

    protected void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (Exception e) {
        }
    }

    protected WizardPage getCurrentPage() {
        return pages.get(index);
    }

    protected JComponent createButtonBar() {
        JButton helpButton = createButton(MessageUtil.getMessage(HELP_I18N), HELP);
        JButton cancelButton = createButton(MessageUtil.getMessage(CANCEL_I18N), CANCEL);

        backButton = createButton(MessageUtil.getMessage(BACK_I18N), BACK);
        nextButton = createButton(MessageUtil.getMessage(NEXT_I18N), NEXT);
        finishButton = createButton(MessageUtil.getMessage(FINISH_I18N), FINISH);

        backButton.setEnabled(false);
        nextButton.setEnabled(false);
        finishButton.setEnabled(false);

        JComponent bar = ButtonBarFactory.buildWizardBar(helpButton, backButton,
                nextButton, finishButton, cancelButton);

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(bar);

        return panel;
    }

    protected JButton createButton(String label, String command) {
        JButton button = new JButton(label);

        button.setActionCommand(command);
        button.addActionListener(this);

        return button;
    }

    protected <T> java.util.List<T> createList() {
        return new ArrayList<T>();
    }

    protected <K, V> Map<K, V> createMap() {
        return new HashMap<K, V>();
    }

    protected void help() {

    }

    protected void back() {
        if (index <= 0) {
            return;
        }

        remove(wizardPane);

        String columns = "p";
        String rows = "p,3dlu,p";

        FormLayout layout = new FormLayout(columns, rows);
        builder = new PanelBuilder(layout);
        cc = new CellConstraints();

        builder.setDefaultDialogBorder();
        builder.add(createButtonBar(), cc.xy(1, 3));

        index--;
        builder.add(getCurrentPage(), cc.xy(1, 1));

        wizardPane = new WizardPane(builder.getPanel());
        setContentPane(wizardPane);

        backButton.setEnabled(index > 0);
        nextButton.setEnabled(true);
        SwingUtilities.updateComponentTreeUI(this);
        this.setTitle(getCurrentPage().getTitle());

        pack();
        this.setLocationRelativeTo(null);
    }

    protected void next() {
        if (index >= pages.size() - 1) {
            return;
        }

        Thread task = new Thread() {
            @Override
            public void run() {
                synchronized (getCurrentPage()) {
                    if (getCurrentPage().onBeforeNext()) {
                        remove(wizardPane);

                        String columns = "p";
                        String rows = "p,3dlu,p";

                        FormLayout layout = new FormLayout(columns, rows);
                        builder = new PanelBuilder(layout);
                        cc = new CellConstraints();

                        builder.setDefaultDialogBorder();
                        builder.add(createButtonBar(), cc.xy(1, 3));

                        index++;
                        getCurrentPage().onBeforeShow();
                        builder.add(getCurrentPage(), cc.xy(1, 1));


                        wizardPane = new WizardPane(builder.getPanel());
                        setContentPane(wizardPane);

                        enableNext();
                        backButton.setEnabled(true);
                        SwingUtilities.updateComponentTreeUI(Wizard.this);
                        Wizard.this.setTitle(getCurrentPage().getTitle());
                        pack();
                        setLocationRelativeTo(null);
                    }
                }
            }
        };

        task.start();
    }

    public void enableNext() {
        if (index < pages.size() - 1) {
            nextButton.setEnabled(getCurrentPage().canGoNext());
        } else {
            nextButton.setEnabled(false);
        }
    }

    public void disableNext() {
        nextButton.setEnabled(false);
    }

    public void enableFinish() {
        finishButton.setEnabled(true);
    }

    public void disableFinish() {
        finishButton.setEnabled(false);
    }

    protected void finish() {
        if (getCurrentPage().onBeforeFinish()) {
            dispose();
        }
    }

    protected void cancel() {
        dispose();
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals(HELP)) {
            back();
        } else if (ae.getActionCommand().equals(BACK)) {
            back();
        } else if (ae.getActionCommand().equals(NEXT)) {
            next();
        } else if (ae.getActionCommand().equals(FINISH)) {
            finish();
        } else if (ae.getActionCommand().equals(CANCEL)) {
            cancel();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Wizard wizard = new Wizard();
                wizard.addPage(new DataWizardGUI());
                wizard.addPage(new SelectTablesGUI());
                wizard.pack();

                wizard.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });

                wizard.setLocationRelativeTo(null);
                wizard.setVisible(true);
            }
        });
    }
}
