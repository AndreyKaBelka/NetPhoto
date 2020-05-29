package explorer;

import java.io.File;
import java.util.UUID;

public class ExplorerCommands {
    public static File createNewFolder(Folder folder) {
        String newName = "Новая папка";
        int cnt = 1;
        while (true) {
            if (new File(folder.getPath() + "\\" + newName).exists()) {
                newName = "Новая папка";
                newName += " (" + cnt + ")";
                cnt++;
            } else {
                break;
            }
        }
        File dir = new File(folder.getPath() + "\\" + newName);
        if (dir.mkdir()) {
            return dir;
        }
        return null;
    }

    public static boolean deleteFolder(Folder folder) {
        File dir = folder.getFolder();
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    deletePhoto(new Photo(UUID.randomUUID().toString(), file));
                } else {
                    deleteFolder(new Folder(UUID.randomUUID().toString(), file));
                }
            }
            return dir.delete();
        }
        return false;
    }

    public static File renameFolder(Folder folder, String newName) {
        File dir = folder.getFolder();
        File newDir = new File(dir.getParent() + "\\" + newName);
        if (dir.exists()) {
            if (dir.renameTo(newDir)) {
                return newDir;
            }
        }
        return null;
    }

    public static boolean deletePhoto(Photo photo) {
        File photoFile = photo.getFile();
        if (photoFile.exists()) {
            return photoFile.delete();
        }
        return false;
    }


    public static File renamePhoto(Photo photo, String newName) {
        File photoFile = photo.getFile();
        File newNameFile = new File(photoFile.getParent() + "\\" + newName);
        if (photoFile.exists()) {
            if (photoFile.renameTo(newNameFile))
                return newNameFile;
        }
        return null;
    }

    public static String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");

        if (lastIndexOfDot != -1 && lastIndexOfDot != 0) return fileName.substring(lastIndexOfDot + 1);
        return "";
    }
}