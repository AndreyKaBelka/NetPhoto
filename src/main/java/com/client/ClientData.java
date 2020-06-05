package com.client;

public class ClientData {
    private static String pathToFolder;
    private static boolean downloadEnded = false;
    private static int cntPhotos;

    ClientData() {
    }

    public static String getPathToFolder() {
        return pathToFolder;
    }

    public static void setPathToFolder(String pathToFolder) {
        ClientData.pathToFolder = pathToFolder;
    }

    public static int getCntPhotos() {
        return cntPhotos;
    }

    public static void setCntPhotos(int cntPhotos1) {
        cntPhotos = cntPhotos1;
    }

    public static boolean isDownloadEnded() {
        return downloadEnded;
    }

    public static void setDownloadEnded(boolean downloadEnded) {
        ClientData.downloadEnded = downloadEnded;
    }
}
