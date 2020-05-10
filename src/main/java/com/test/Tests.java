package com.test;

import explorer.Folder;
import explorer.Item;
import explorer.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.Arrays;

public class Tests {
    @FXML
    private TreeView<Item> treeExplorer;

    private TreeItem<Item> createNodes(File f) {
        return new TreeItem<>((f.isFile()) ? new Photo("00", f) : new Folder("00", f)) {
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public ObservableList<TreeItem<Item>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    ObservableList<TreeItem<Item>> child = buildChildren(this);
                    if (child.size() != 0) {
                        super.getChildren().setAll(child);
                    }
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    Item f = getValue();
                    isLeaf = f.isFile();
                }
                return isLeaf;
            }

            private ObservableList<TreeItem<Item>> buildChildren(TreeItem<Item> TreeItem) {
                File f = TreeItem.getValue().getFile();
                if (f == null) {
                    return FXCollections.emptyObservableList();
                }
                System.out.println(f.getName());
                if (f.isFile()) {
                    System.out.println("АЭВЭЫЖПЫВП");
                    return FXCollections.emptyObservableList();
                }
                File[] files = f.listFiles();
                System.out.println(Arrays.toString(files));
                if (files != null) {
                    ObservableList<TreeItem<Item>> children = FXCollections.observableArrayList();
                    for (File childFile : files) {
                        children.add(createNodes(childFile));
                    }
                    return children;
                }
                return FXCollections.emptyObservableList();
            }
        };
    }

    @FXML
    void initialize() {
        TreeItem<Item> root = createNodes(new File("E:\\Тест"));
        treeExplorer.setRoot(root);
    }

}
