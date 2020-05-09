package explorer;

import java.io.File;

public class Folder {
    private String id;
    private String name;
    private File folder;
    private String path;

    Folder(String id, File folder) {
        this.folder = folder;
        this.name = folder.getName();
        this.path = folder.getPath();
        this.id = id;
    }

    public Folder(String id, String path) {
        this.folder = new File(path);
        this.name = folder.getName();
        this.path = folder.getPath();
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setPath() {
        this.path = folder.getPath();
    }

    public void renameTo(String newName) {
        name = newName;
    }

    public File getFolder() {
        return folder;
    }

    public String getId() {
        return id;
    }

    public void setId(String newId) {
        id = newId;
    }

    @Override
    public String toString() {
        return name;
    }
}
