package appbuilder.wizards.gui.validation;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JTextField;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.view.ValidationResultViewFactory;

public class MyJTextField extends JTextField implements MyTextComponent {
    private static final long serialVersionUID = 8337233750895098851L;
    private static final String TEXT = "text";
    private static final int X_OFFSET = 2;
    private static final int Y_OFFSET = 2;
    private Severity severity;

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (severity != null) {
            Icon icon = ValidationResultViewFactory.getSmallIcon(severity);

            boolean isLTR = this.getComponentOrientation().isLeftToRight();
            int x = (isLTR ? 0 : this.getWidth() - 1) + X_OFFSET;

            icon.paintIcon(this, g, x, Y_OFFSET);
        }
    }

    @Override
    public void setText(String text) {
        String oldText = super.getText();
        super.setText(text);
        this.firePropertyChange(TEXT, oldText, text);
    }
}
