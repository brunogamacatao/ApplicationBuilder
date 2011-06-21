package appbuilder.wizards.gui.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Validator {
    private Map<String, String> values;

    public Validator() {
        values = createMap();
    }

    public void setValue(String field, String value) {
        values.put(field, value);
    }

    public String getValue(String field) {
        return values.get(field);
    }

    public boolean containsValue(String field) {
        return values.containsKey(field);
    }

    public abstract List<ValidationMessage> validate();

    protected <T> List<T> createList() {
        return new ArrayList<T>();
    }

    protected <K, V> Map<K, V> createMap() {
        return new HashMap<K, V>();
    }
}
