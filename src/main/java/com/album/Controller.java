package com.album;

import com.ImgViewer;
import com.client.Client;
import explorer.ExplorerCommands;
import explorer.Folder;
import explorer.Item;
import explorer.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
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

    private int maxHeightOfWindow = 600;
    private int maxWidthOfWindow = 600;
    private int userNumber;
    private Client client;

    private void showError(String errorString) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(errorString);

        alert.showAndWait();
    }

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
                        if (fileEx.equals("jpeg") || fileEx.equals("") || fileEx.equals("jpg"))
                            children.add(createNodes(childFile));
                    }
                    return children;
                }
                return FXCollections.emptyObservableList();
            }
        };
    }

    private double[] getSizeImg(double height, double width) {
        double[] size = new double[2];
        size[0] = getValueForSize(height);
        size[1] = getValueForSize(width);
        return size;
    }

    private double getValueForSize(double value) {
        if (value / 2 < 400) {
            return 400;
        }
        if (value > maxHeightOfWindow) {
            return maxHeightOfWindow;
        }
        return value / 2;
    }

    private ContextMenu createContexMenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem openFile = new MenuItem("Открыть");
        openFile.setOnAction(event -> {
            if (treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile() != null) {
                if (treeExplorer.getSelectionModel().getSelectedItem().getValue().isFile()) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/imgViewer.fxml"));
                        Parent root = loader.load();
                        Stage stage = new Stage();
                        stage.setTitle(treeExplorer.getSelectionModel().getSelectedItem().getValue().getName());
                        stage.setScene(new Scene(root, maxHeightOfWindow, maxWidthOfWindow));
                        ImgViewer ctrl = loader.getController();
                        Image img = new Image(treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile().toURI().toString());
                        double[] size = getSizeImg(img.getHeight(), img.getWidth());
                        ctrl.setImage(img, size[1], size[0]);
                        stage.setResizable(false);
                        stage.setHeight(size[0]);
                        stage.setWidth(size[1]);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        showError("Допущена ошибка при открывании фотографии!");
                    }

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
        MenuItem createNewFolder = new MenuItem("Создать папку в выбранной");
        createNewFolder.setOnAction(actionEvent -> {
            if (!treeExplorer.getSelectionModel().getSelectedItem().getValue().isFile()) {
                File file = ExplorerCommands.createNewFolder(treeExplorer.getSelectionModel().getSelectedItem().getValue().getParentFolder());
                if (file != null) {
                    treeExplorer.getSelectionModel().getSelectedItem().getChildren().add(createNodes(file));
                } else {
                    System.out.println("Извините, произошла ошибка...");
                }
            }
        });
        cm.getItems().add(createNewFolder);
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
                        client = new Client(userNumber, UUID.randomUUID().toString());
                        new Thread(() -> {
                            Thread clientThread = new Thread(() -> {
                                try {
                                    client.run();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            clientThread.setDaemon(true);
                            clientThread.start();
                            while (client.isClientCanConnect()) {
                                if (client.isClientConnected()) {
                                    client.sendFolder(new com.files.Folder(treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile(), treeExplorer.getSelectionModel().getSelectedItem().getValue().getName()));
                                    client.setPathToFolder(treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile().getAbsolutePath());
                                    System.out.println(client.getToken());
                                    break;
                                }
                            }
                        }).start();
                        if (!client.isClientCanConnect()) {
                            showError("Ошибка в подключении к серверу!");
                        }
                    } else {
                        showError("Папка содержит подпапки! Выберите другую папку!!!");
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
        treeExplorer.setEditable(true);
        treeExplorer.setCellFactory(itemTreeView -> new TextFieldTreeCell<>(new StringConverter<>() {

            @Override
            public String toString(Item object) {
                return object.getName();
            }

            @Override
            public Item fromString(String s) {
                if (treeExplorer.getSelectionModel().getSelectedItem().getValue().isFile()) {
                    File newFile = ExplorerCommands.renamePhoto(treeExplorer.getSelectionModel().getSelectedItem().getValue().getParentPhoto(), s);
                    if (newFile != null) {
                        Photo photo = new Photo(UUID.randomUUID().toString(), newFile);
                        photo.setName(s);
                        return photo;
                    }
                } else {
                    File newFile = ExplorerCommands.renameFolder(treeExplorer.getSelectionModel().getSelectedItem().getValue().getParentFolder(), s);
                    if (newFile != null) {
                        Folder folder = new Folder(UUID.randomUUID().toString(), newFile);
                        folder.renameTo(s);
                        return folder;
                    }
                }
                return treeExplorer.getSelectionModel().getSelectedItem().getValue();
            }
        }));

        mainmenu.setVisible(true);
        treeExplorer.setVisible(false);
        keymenu.setVisible(false);
        servermenu.setVisible(false);
        connectedpane.setVisible(false);

        ButtonUser1.setOnAction(actionEvent ->

        {
            userNumber = 1;
            mainmenu.setVisible(false);
            servermenu.setVisible(true);
            treeExplorer.setVisible(true);
        });

        ButtonBrowse.setOnAction(actionEvent ->

        {
            final DirectoryChooser dirChooser = new DirectoryChooser();
            File dir = dirChooser.showDialog(null);
            if (dir != null) {
                TextPath.setText(dir.getAbsolutePath());
                pathdir.set(dir.getAbsolutePath());
            } else {
                TextPath.setText(null);
            }
        });

        ButtonUser2.setOnAction(actionEvent ->

        {
            mainmenu.setVisible(false);
            keymenu.setVisible(true);
        });

        ButtonConnect.setOnAction(actionEvent ->

        {
            ImageLoading.setVisible(true);
            if (!TextKey.getText().trim().isBlank()) {
                client = new Client(2, TextKey.getText());
                new Thread(() -> {
                    client.run();
                    while (client.isClientCanConnect()) {
                        if (client.isClientConnected()) {
                            break;
                        }
                    }
                }).start();
            }

            while (client == null || client.isClientCanConnect()) {
                if (client != null || client.isClientConnected()) {
                    if (Client2.sizeChangedListener()) {
                        try {
                            treeExplorer.getRoot().getChildren().clear();
                            treeExplorer.setRoot(new TreeItem<>(new Folder(Client2.getLastFolder())));
                            treeExplorer.setVisible(true);
                            connectedpane.setVisible(true);
                            ImageLoading.setVisible(false);
                            keymenu.setVisible(false);
                            break;
                        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                            ImageBan1.setVisible(true);
                            break;
                        }
                    }
                } else {
                    showError("Неверный ключ сессии!");
                }
            }
        });

        ButtonBrowse1.setOnAction(actionEvent ->
        {
            final DirectoryChooser dirChooser = new DirectoryChooser();
            File dir = dirChooser.showDialog(null);
            if (dir != null) {
                TextPath1.setText(dir.getAbsolutePath());
                pathdir.set(dir.getAbsolutePath());
            } else {
                TextPath1.setText(null);
            }
        });

        ButtonStart1.setOnAction(actionEvent -> {
            client.sendRequest(Client2.getLastFolder());
            new Thread(() ->
            {
                ImageLoading.setVisible(true);
                ImageOk.setVisible(true);
                ImageLoading.setVisible(false);
            }).start();
        });
    }
}