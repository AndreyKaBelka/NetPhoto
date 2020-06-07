package explorer;

import java.io.File;

public class Photo extends Item {
    private String name;
    private String id;
    private File photo;
    private String path;

    public Photo(String id, File photo) {
        super(photo.getName(), id);
        this.id = id;
        this.name = photo.getName();
        this.photo = photo;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public File getFile() {
        return photo;
    }

    @Override
    public Folder getParentFolder() {
        return null;
    }

    @Override
    public Photo getParentPhoto() {
        return this;
    }


}
