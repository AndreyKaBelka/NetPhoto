package com.test;

import explorer.ExplorerCommands;
import explorer.Folder;
import explorer.Item;
import explorer.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;

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
                if (f.isFile()) {
                    return FXCollections.emptyObservableList();
                }
                File[] files = f.listFiles();
                if (files != null) {
                    ObservableList<TreeItem<Item>> children = FXCollections.observableArrayList();
                    for (File childFile : files) {
                        String fileEx = ExplorerCommands.getFileExtension(f.getName());
                        if (fileEx.equals("jpeg") || fileEx.equals("")) {
                            children.add(createNodes(childFile));
                        }
                    }
                    return children;
                }
                return FXCollections.emptyObservableList();
            }
        };
    }

    private ContextMenu createContexMenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem openFile = new MenuItem("Открыть");
        openFile.setOnAction(event -> {
            if (treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile() != null) {
                if (treeExplorer.getSelectionModel().getSelectedItem().getValue().isFile()) {
                    System.out.println("ФОткрываю фотку!");
                } else {
                    System.out.println("ПАПКА!");
                }
            } else {
                System.out.println("НАЕБАЛОВО КАКОЕТО!");
            }
        });
        cm.getItems().add(openFile);
        MenuItem deleteFile = new MenuItem("Удалить");
        deleteFile.setOnAction(event -> {
            if (treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile() != null) {
                if (treeExplorer.getSelectionModel().getSelectedItem().getValue().isFile()) {
                    System.out.println("Удаляю фотку!");
                } else {
                    System.out.println("Удаляю папку!!");
                }
            } else {
                System.out.println("НАЕБАЛОВО КАКОЕТО!");
            }
        });
        cm.getItems().add(deleteFile);
        MenuItem renameFIle = new MenuItem("Переименовать");
        renameFIle.setOnAction(event -> {
            if (treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile() != null) {
                if (treeExplorer.getSelectionModel().getSelectedItem().getValue().isFile()) {
                    System.out.println("Переименовать фотку!");
                } else {
                    System.out.println("Переименовать папку!!");
                }
            } else {
                System.out.println("НАЕБАЛОВО КАКОЕТО!");
            }
        });
        cm.getItems().add(renameFIle);
        return cm;
    }

    @FXML
    void initialize() {
        TreeItem<Item> root = createNodes(new File("E:\\Тест"));
        treeExplorer.setRoot(root);
        treeExplorer.setContextMenu(createContexMenu());
    }

}
