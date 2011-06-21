package appbuilder.wizards.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;

public class SimpleListModel<T> extends AbstractListModel {
    private static final long serialVersionUID = -6347886197291158046L;
    private List<T> elements;

    public SimpleListModel() {
        elements = createList();
    }

    public void addElement(T element) {
        elements.add(element);
        this.fireIntervalAdded(element, elements.size() - 1,
                elements.size() - 1);
    }

    public void addElements(Collection<T> els) {
        int oldSize = elements.size();
        elements.addAll(els);
        this.fireIntervalAdded(els, oldSize, elements.size() - 1);
    }

    public void removeElement(T element) {
        int idx = -1;

        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i) == element) {
                idx = i;
                break;
            }
        }

        elements.remove(element);
        this.fireIntervalRemoved(element, idx, idx);
    }

    public void clear() {
        if (!elements.isEmpty()) {
            int size = elements.size();
            elements.clear();
            this.fireIntervalRemoved(this, 0, size - 1);
        }
    }

    public List<T> getElements() {
        return elements;
    }

    @Override
    public Object getElementAt(int index) {
        return elements.get(index);
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    private List<T> createList() {
        return new ArrayList<T>();
    }
}
