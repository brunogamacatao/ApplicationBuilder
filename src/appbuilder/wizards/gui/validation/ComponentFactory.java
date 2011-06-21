package appbuilder.wizards.gui.validation;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ComponentFactory {
    public static JTextField createTextField(String field, Validator validator) {
        MyJTextField textField = new MyJTextField();

        ValueObserver observer = new ValueObserver(textField, field, validator);

        textField.addKeyListener(observer);
        textField.addMouseListener(observer);
        textField.addPropertyChangeListener(observer);

        return textField;
    }

    public static JPasswordField createPasswordField(String field, Validator validator) {
        MyJPasswordField textField = new MyJPasswordField();

        ValueObserver observer = new ValueObserver(textField, field, validator);

        textField.addKeyListener(observer);
        textField.addMouseListener(observer);
        textField.addPropertyChangeListener(observer);

        return textField;
    }
}
