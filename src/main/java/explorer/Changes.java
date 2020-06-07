package explorer;

import java.io.Serializable;
import java.util.ArrayList;

public class Changes implements Serializable {
    private int id;
    private ArrayList<Change> changes;

    public Changes() {
        changes = new ArrayList<>();
        id = hashCode();
    }

    public void addNewChange(Change change) {
        changes.add(change);
    }

    public ArrayList<Change> getChanges() {
        return changes;
    }
}
