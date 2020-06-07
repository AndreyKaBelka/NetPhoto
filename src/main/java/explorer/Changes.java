package explorer;

import java.util.ArrayList;

public class Changes {
    private ArrayList<Change> changes;

    public Changes() {
        changes = new ArrayList<>();
    }

    public void addNewChange(Change change) {
        changes.add(change);
    }

    public ArrayList<Change> getChanges() {
        return changes;
    }
}
