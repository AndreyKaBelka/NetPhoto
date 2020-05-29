package explorer;

import javafx.scene.image.Image;

import java.io.File;

public abstract class Item {
    private String id;
    private String name;
    private Image img;

    Item(String name, String id, Image img){
        this.name = name;
        this.id = id;
        this.img = img;
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

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public abstract Folder getParentFolder();

    public abstract Photo getParentPhoto();
}
