package explorer;

import com.files.Folder;
import com.files.Photo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class FolderParametrs {
    private long size = 0;
    private ArrayList<Object> listOfFiles = new ArrayList<>();

    public void createTheTree(File folder, String name) {
        if (Objects.nonNull(folder.listFiles())) {
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    size += file.length();
                    listOfFiles.add(new Object(new Photo(name + "\\" + file.getName(), file.length())));
                } else {
                    Folder newFolder = new Folder(new File(folder.getPath() + "\\" + file.getName()), name + "\\" + file.getName());
                    size += newFolder.getSize();
                    listOfFiles.add(new Object(newFolder));
                }
            }
        }
    }

    public long getSize() {
        return size;
    }

    public ArrayList<Object> getListOfFiles() {
        return listOfFiles;
    }

    public static class Object implements Serializable {
        public Folder folder;
        public Photo photo;

        Object(Photo photo) {
            this.photo = photo;
        }

        Object(Folder folder) {
            this.folder = folder;
        }

        @Override
        public String toString() {
            if (folder != null) {
                return folder.toString();
            }
            return photo.toString();
        }

        public Folder getFolder() {
            return folder;
        }

        public Photo getPhoto() {
            return photo;
        }

        public boolean isFolder() {
            return folder != null;
        }
    }
}
