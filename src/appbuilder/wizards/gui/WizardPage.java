package appbuilder.wizards.gui;

import javax.swing.JPanel;

public abstract class WizardPage extends JPanel {
    private static final String NO_TITLE = "";
    
    private String title;
    private Wizard wizard;

    public WizardPage() {
        this(NO_TITLE);
    }

    public WizardPage(String title) {
        this(title, null);
    }

    public WizardPage(Wizard wizard) {
        this(NO_TITLE, wizard);
    }

    public WizardPage(String title, Wizard wizard) {
        super();
        this.title  = title;
        this.wizard = wizard;
    }

    public String getTitle() {
        return title;
    }

    public abstract boolean canGoNext();

    public boolean onBeforeNext() {
        return true;
    }

    public void onBeforeShow() {
    }

    public boolean onBeforeFinish() {
        return true;
    }

    public void addProperty(String key, Object value) {
        this.getWizard().addProperty(key, value);
    }

    public Object getProperty(String key) {
        return this.getWizard().getProperty(key);
    }

    public void removeProperty(String key) {
        this.getWizard().removeProperty(key);
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public Wizard getWizard() {
        return wizard;
    }
}
