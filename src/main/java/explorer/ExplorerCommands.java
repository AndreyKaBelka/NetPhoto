package explorer;

import java.io.File;

public class ExplorerCommands {
    public static boolean createNewFolder(Folder folder, String newName) {
        File dir = new File(folder.getPath() + "\\" + newName);
        File fold = folder.getFolder();
        if (fold.exists() && fold.isDirectory()) {
            return dir.mkdir();
        }
        return false;
    }

    public static boolean deleteFolder(Folder folder) {
        File dir = folder.getFolder();
        if (dir.exists() && dir.isDirectory()) {
            return dir.delete();
        }
        return false;
    }

    public static boolean renameFolder(Folder folder, String newName) {
        File dir = folder.getFolder();
        File newDir = new File(dir.getParent() + "\\" + newName);
        if (dir.exists() && dir.isDirectory()) {
            return dir.renameTo(newDir);
        }
        return false;
    }

    public static String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");

        if (lastIndexOfDot != -1 && lastIndexOfDot != 0) return fileName.substring(lastIndexOfDot + 1);
        return "";
    }
}