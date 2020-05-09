package com.test;

import explorer.Folder;
import explorer.Item;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.ArrayList;

public class Tests {
    ArrayList<TreeItem<Item>> items;

    @FXML
    private TreeView<Folder> treeExplorer;

    @FXML
    void initialize(){
        Folder fold1 = new Folder("00","E:\\Тест");
        Folder fold2 = new Folder("01","E:\\Тест\\Тест");
        Folder fold3 = new Folder("02","E:\\Тест\\rnAV3Xi9MG8.jpg");

        TreeItem<Folder> rootItem = new TreeItem<>(fold1);
        rootItem.setExpanded(true);

        TreeItem<Folder> itemFold = new TreeItem<>(fold2);
        TreeItem<Folder> itemPhoto = new TreeItem<>(fold3);
        rootItem.getChildren().addAll(itemFold, itemPhoto);

        treeExplorer.setRoot(rootItem);
    }

    void getAllFiles(Folder folder){
        File[] filesInside = folder.getFolder().listFiles();
        for (File dirOrFile: filesInside){
            if (dirOrFile.isDirectory()){

            }
        }
    }

}
