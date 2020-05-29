package com.album;

import explorer.ExplorerCommands;
import explorer.Folder;
import explorer.Item;
import explorer.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


public class Controller {

    @FXML
    private Button ButtonUser1;

    @FXML
    private Button ButtonUser2;

    @FXML
    private Pane Name;

    @FXML
    private TextArea TextGeneratedKey;

    @FXML
    private TextArea TextPath;

    @FXML
    private Button ButtonBrowse;

    @FXML
    private ImageView ImageOk;

    @FXML
    private ImageView ImageBan;

    @FXML
    private TextArea TextKey;

    @FXML
    private Button ButtonConnect;

    @FXML
    private ImageView ImageLoading;

    @FXML
    private ImageView ImageBan1;

    @FXML
    private TextArea TextPath1;

    @FXML
    private Button ButtonStart1;

    @FXML
    private Button ButtonBrowse1;

    @FXML
    private AnchorPane mainmenu;

    @FXML
    private AnchorPane connectedpane;

    @FXML
    private AnchorPane servermenu;

    @FXML
    private AnchorPane keymenu;

    @FXML
    private TreeView<Item> treeExplorer;

    private TreeItem<Item> createNodes(File f) {
        return new TreeItem<>((f.isFile()) ? new Photo(UUID.randomUUID().toString(), f) : new Folder(UUID.randomUUID().toString(), f)) {
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
                    File f = getValue().getFile();
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
                        String fileEx = ExplorerCommands.getFileExtension(childFile.getName());
                        if (fileEx.equals("jpeg") || fileEx.equals(""))
                            children.add(createNodes(childFile));
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
                    treeExplorer.getSelectionModel().getSelectedItem().setExpanded(true);
                }
            } else {
                System.out.println("Извините, допущена ошибка...");
            }
        });
        cm.getItems().add(openFile);
        MenuItem deleteFile = new MenuItem("Удалить");
        deleteFile.setOnAction(event -> {
            if (treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile() != null) {
                if (treeExplorer.getSelectionModel().getSelectedItem().getValue().isFile()) {
                    if (ExplorerCommands.deletePhoto(treeExplorer.getSelectionModel().getSelectedItem().getValue().getParentPhoto())) {
                        treeExplorer.getSelectionModel().getSelectedItem().getParent().getChildren().remove(treeExplorer.getSelectionModel().getSelectedItem());
                    } else {
                        System.out.println("Ошибка при удалении!");
                    }
                } else {
                    if (ExplorerCommands.deleteFolder(treeExplorer.getSelectionModel().getSelectedItem().getValue().getParentFolder())) {
                        treeExplorer.getSelectionModel().getSelectedItem().getParent().getChildren().remove(treeExplorer.getSelectionModel().getSelectedItem());
                    } else {
                        System.out.println("Ошибка при удалении!");
                    }
                }
            } else {
                System.out.println("Извините, допущена ошибка...");
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
                System.out.println("Извините, допущена ошибка...");
            }
        });
        cm.getItems().add(renameFIle);
        MenuItem shareFolder = new MenuItem("Отправить папку");
        shareFolder.setOnAction(event -> {
            if (treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile() != null) {
                if (!treeExplorer.getSelectionModel().getSelectedItem().getValue().isFile()) {
                    boolean canShareFolder = true;
                    File[] folder = treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile().listFiles();
                    if (folder == null || folder.length == 0) {
                        canShareFolder = false;
                    }
                    if (canShareFolder) {
                        for (File file : folder) {
                            if (file.isDirectory()) {
                                canShareFolder = false;
                                break;
                            }
                        }
                    }
                    if (canShareFolder) {
                        System.out.println("Можно передать");//TODO: Ошбика при передачи папки
                    } else {
                        System.out.println("Нельзя передать");
                    }
                }
            }
        });
        cm.getItems().add(shareFolder);
        return cm;
    }


    @FXML
    void initialize() {
        AtomicReference<String> pathdir = new AtomicReference<>("");

        TreeItem<Item> root = createNodes(new File("E:\\Тест"));
        treeExplorer.setRoot(root);
        treeExplorer.setContextMenu(createContexMenu());

        mainmenu.setVisible(true);
        treeExplorer.setVisible(false);
        keymenu.setVisible(false);
        servermenu.setVisible(false);
        connectedpane.setVisible(false);

        ButtonUser1.setOnAction(actionEvent -> {
            mainmenu.setVisible(false);
            servermenu.setVisible(true);
            treeExplorer.setVisible(true);
        });

        ButtonBrowse.setOnAction(actionEvent -> {
            final DirectoryChooser dirChooser = new DirectoryChooser();
            File dir = dirChooser.showDialog(null);
            if (dir != null) {
                TextPath.setText(dir.getAbsolutePath());
                pathdir.set(dir.getAbsolutePath());
            } else {
                TextPath.setText(null);
            }
        });

        ButtonUser2.setOnAction(actionEvent -> {
            mainmenu.setVisible(false);
            keymenu.setVisible(true);
        });

        ButtonConnect.setOnAction(actionEvent -> {
            ImageLoading.setVisible(true);
            try {
                treeExplorer.setVisible(true);
                connectedpane.setVisible(true);
                ImageLoading.setVisible(false);
                keymenu.setVisible(false);
            } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                ImageBan1.setVisible(true);
            }
        });

        ButtonBrowse1.setOnAction(actionEvent -> {
            final DirectoryChooser dirChooser = new DirectoryChooser();
            File dir = dirChooser.showDialog(null);
            if (dir != null) {
                TextPath1.setText(dir.getAbsolutePath());
                pathdir.set(dir.getAbsolutePath());
            } else {
                TextPath1.setText(null);
            }
        });

        ButtonStart1.setOnAction(actionEvent -> new Thread(() -> {
            ImageLoading.setVisible(true);
            ImageOk.setVisible(true);
            ImageLoading.setVisible(false);
        }).start());
    }
}