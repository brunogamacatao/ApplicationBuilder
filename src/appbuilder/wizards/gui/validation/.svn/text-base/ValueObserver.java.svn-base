package appbuilder.wizards.gui.validation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ValueObserver implements KeyListener, MouseListener, PropertyChangeListener {
    private MyTextComponent textField;
    private String field;
    private Validator validator;

    public ValueObserver(MyTextComponent textField, String field, Validator validator) {
        this.textField = textField;
        this.field = field;
        this.validator = validator;

        textField.addKeyListener(this);
        textField.addMouseListener(this);

        fireValueChanged();
    }

    private void fireValueChanged() {
        validator.setValue(field, textField.getText());

        boolean hasErrors = false;
        List<ValidationMessage> messages = validator.validate();

        if (!messages.isEmpty()) {
            for (ValidationMessage message : messages) {
                if (message.getField().equals(field)) {
                    hasErrors = true;
                    textField.setToolTipText(message.getMessage());
                    textField.setSeverity(message.getSeverity());
                    textField.repaint();
                    break;
                }
            }
        }

        if (!hasErrors) {
            textField.setToolTipText(null);
            textField.setSeverity(null);
            textField.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent evt) {
        fireValueChanged();
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        fireValueChanged();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        fireValueChanged();
    }

    @Override
    public void keyPressed(KeyEvent evt) {
    //Do nothing
    }

    @Override
    public void keyTyped(KeyEvent evt) {
    //Do nothing
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
    //Do nothing
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
    //Do nothing
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    //Do nothing
    }

    @Override
    public void mousePressed(MouseEvent evt) {
    //Do nothing
    }
}
