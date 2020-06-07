package explorer;

import java.io.File;

public abstract class Item {
    private String id;
    private String name;

    Item(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract boolean isFile();

    public abstract File getFile();

    public abstract Folder getParentFolder();

    public abstract Photo getParentPhoto();
}
