package appbuilder.wizards.gui.data.validator;

import java.util.List;

import appbuilder.util.MessageUtil;
import appbuilder.wizards.gui.validation.ValidationMessage;
import appbuilder.wizards.gui.validation.Validator;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.util.ValidationUtils;

public class DatabaseSettingsValidator extends Validator {

    public static final String DRIVER = "driver";
    public static final String PATH = "path";
    public static final String URL = "url";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    private static final String MANDATORY_MSG_I18N = "validation.mandatory.driver";
    private static final String DRIVER_I18N = "wizard.data.driver";
    private static final String PATH_I18N = "wizard.data.path";
    private static final String URL_I18N = "wizard.data.url";

    @Override
    public List<ValidationMessage> validate() {
        List<ValidationMessage> messages = createList();

        if (ValidationUtils.isBlank(getValue(DRIVER))) {
            messages.add(new ValidationMessage(DRIVER, getMessage(DRIVER_I18N), Severity.ERROR));
        }

        if (ValidationUtils.isBlank(getValue(PATH))) {
            messages.add(new ValidationMessage(PATH, getMessage(PATH_I18N), Severity.ERROR));
        }

        if (ValidationUtils.isBlank(getValue(URL))) {
            messages.add(new ValidationMessage(URL, getMessage(URL_I18N), Severity.ERROR));
        }

        return messages;
    }

    private String getMessage(String fieldName) {
        String localizedFieldName = MessageUtil.getMessage(fieldName);
        return MessageUtil.getMessage(MANDATORY_MSG_I18N, localizedFieldName);
    }
}
