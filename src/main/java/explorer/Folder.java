package explorer;

import java.io.File;

public class Folder extends Item {
    private String id;
    private String name;
    private File folder;
    private String path;

    public Folder(String id, File folder) {
        super(folder.getName(), id);
        this.folder = folder;
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
    public boolean isFile() {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public File getFile() {
        return folder;
    }
}
