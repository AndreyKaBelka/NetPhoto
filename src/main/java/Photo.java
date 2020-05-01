import java.io.File;

public class Photo {
    private int id;
    private File photo;
    private String name;
    private String size;

    Photo(File photo, int id){
        if (photo.exists()) {
            this.photo = photo;
            this.id = id;
            this.name = photo.getName();
            this.size = getSizeOfPhoto();
        }
    }

    private String getSizeOfPhoto() {
        if (photo.length() / 1024 > 1){
            if(photo.length() / 1024 / 1024 > 1){
                return photo.length() / 1024 / 1024 + "mbytes";
            } else {
                return photo.length() / 1024 + "kbytes";
            }
        } else {
            return photo.length() + "bytes";
        }
    }

    boolean RenamePhoto(String newName){
        name = newName;
        return photo.renameTo(new File(newName));
    }
}
