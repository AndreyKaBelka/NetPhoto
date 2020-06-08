package com.album;

import com.ChatController;
import com.ImgViewer;
import com.client.Client;
import com.client.ClientData;
import explorer.*;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


public class Controller {

    @FXML
    public Slider sliderOddImage;

    @FXML
    public CheckBox qualityChkBox;

    @FXML
    private Button ButtonUser1;

    @FXML
    private Button ButtonUser2;

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
    private String folderPath = "E:\\Тест";
    private Changes changes;
    private String copyPath;
    private String copyPathFromTree;
    private boolean isCopy = false;
    private boolean isCut = false;
    private TreeItem<Item> itemToDelete;

    public void copy(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            copyDirectory(sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }

    private void copyDirectory(File source, File target) throws IOException {
        if (!target.exists()) {
            target.mkdir();
        }

        for (String f : source.list()) {
            copy(new File(source, f), new File(target, f));
        }
    }

    private void copyFile(File source, File target) {
        try (
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target)
        ) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                        if (userNumber == 2) {
                            changes.addNewChange(new Change(ChangesType.DELETE_FOLDER,
                                    null,
                                    null,
                                    treeExplorer.getSelectionModel().getSelectedItem().getValue().getParentFolder().getName(),
                                    getPath(treeExplorer.getSelectionModel().getSelectedItem().getParent())));
                        }
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
                    if (userNumber == 2) {
                        changes.addNewChange(new Change(ChangesType.ADD_FOLDER, file.getName(), getPath(treeExplorer.getSelectionModel().getSelectedItem()), null, null));
                    }
                } else {
                    System.out.println("Извините, произошла ошибка...");
                }
            }
        });
        cm.getItems().add(createNewFolder);
        Menu shareFolder = new Menu("Отправить папку");
        MenuItem shareFolderWithCopy = new MenuItem("Изменения к копии");
        MenuItem shareFolderWithOriginal = new MenuItem("Изменения к оригиналу");
        shareFolder.getItems().addAll(shareFolderWithCopy, shareFolderWithOriginal);
        shareFolderWithCopy.setOnAction(event -> {
            if (userNumber == 1) {
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
                            }).start();
                            while (client.isClientCanConnect()) {
                                if (client.isClientConnected()) {
                                    String nameFolder = treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile().getPath();
                                    File copyFolder = new File(nameFolder + " - Копия");
                                    try {
                                        copy(treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile(), copyFolder);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    client.setPathToFolder(copyFolder.getAbsolutePath().substring(0, copyFolder.getAbsolutePath().length() - copyFolder.getName().length() - 1));
                                    client.sendFolder(new com.files.Folder(copyFolder, copyFolder.getName()));
                                    System.out.println(client.getToken());
                                    System.out.println(copyFolder.getPath());
                                    System.out.println(client.getPathToFolder());
                                    createChat("Токен сессии: " + client.getToken());
                                    break;
                                }
                            }
                            if (!client.isClientCanConnect()) {
                                showError("Ошибка в подключении к серверу!");
                            }
                        } else {
                            showError("Папка содержит подпапки! Выберите другую папку!!!");
                        }
                    }
                }
            } else {
                client.sendChanges(changes);
            }
        });
        shareFolderWithOriginal.setOnAction(event -> {
            if (userNumber == 1) {
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
                            }).start();
                            while (client.isClientCanConnect()) {
                                if (client.isClientConnected()) {
                                    client.sendFolder(new com.files.Folder(treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile(), treeExplorer.getSelectionModel().getSelectedItem().getValue().getName()));
                                    client.setPathToFolder(treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile().getAbsolutePath().substring(0, treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile().getAbsolutePath().lastIndexOf('\\')));
                                    System.out.println(client.getToken());
                                    createChat("Токен сессии: " + client.getToken());
                                    break;
                                }
                            }
                            if (!client.isClientCanConnect()) {
                                showError("Ошибка в подключении к серверу!");
                            }
                        } else {
                            showError("Папка содержит подпапки! Выберите другую папку!!!");
                        }
                    }
                }
            } else {
                client.sendChanges(changes);
            }
        });
        cm.getItems().add(shareFolder);
        MenuItem copyFile = new MenuItem("Копировать");
        copyFile.setOnAction(event -> {
            if (treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile() != null) {
                copyPath = treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile().getPath();
                copyPathFromTree = getPath(treeExplorer.getSelectionModel().getSelectedItem());
                isCopy = true;
            } else {
                System.out.println("Извините, допущена ошибка...");
            }
        });
        cm.getItems().add(copyFile);

        MenuItem cutFile = new MenuItem("Вырезать");
        cutFile.setOnAction(event -> {
            if (treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile() != null) {
                copyPath = treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile().getPath();
                itemToDelete = treeExplorer.getSelectionModel().getSelectedItem();
                copyPathFromTree = getPath(itemToDelete);
                isCut = true;
            } else {
                System.out.println("Извините, допущена ошибка...");
            }
        });
        cm.getItems().add(cutFile);

        MenuItem pasteFile = new MenuItem("Вставить");
        pasteFile.setOnAction(event -> {
            if (isCopy) {
                String pathToPaste = treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile().getPath();
                String nameOld = ((new File(copyPath).isFile()) ? new File(copyPath).getName() : "");
                File copiedFile = new File(pathToPaste + "\\" + nameOld);
                String name;
                int cnt = 1;
                if (copiedFile.isDirectory()) {
                    name = copiedFile.getName() + " - Копия";
                } else {
                    name = copiedFile.getName().substring(0, copiedFile.getName().lastIndexOf('.')) + " - Копия" + copiedFile.getName().substring(copiedFile.getName().lastIndexOf('.'));
                }

                String newCopyName = name;

                while (true) {
                    File newfile = new File(pathToPaste + "\\" + newCopyName);
                    if (newfile.exists()) {
                        if (newfile.isDirectory()) {
                            newCopyName = name;
                            newCopyName = newCopyName + "(" + cnt + ")";
                            cnt++;
                        } else {
                            newCopyName = name;
                            newCopyName = newCopyName.replace("- Копия", "- Копия (" + cnt + ")");
                            cnt++;
                        }
                    } else {
                        break;
                    }
                }

                try {
                    Files.copy(Path.of(copyPath), Path.of(pathToPaste + "\\" + newCopyName));
                    TreeItem<Item> newItem = createNodes(new File(pathToPaste + "\\" + newCopyName));
                    treeExplorer.getSelectionModel().getSelectedItem().getChildren().add(newItem);
                    if (userNumber == 2) {
                        changes.addNewChange(new Change(ChangesType.COPY_PHOTO, null, getPath(newItem), null, copyPathFromTree));
                    }
                } catch (IOException e) {
                    showError("Не удалось скопировать файл");
                }
                isCopy = false;
            } else if (isCut) {
                String pathToPaste = treeExplorer.getSelectionModel().getSelectedItem().getValue().getFile().getPath();
                String nameOld = ((new File(copyPath).isFile()) ? new File(copyPath).getName() : "");
                File copiedFile = new File(pathToPaste + "\\" + nameOld);
                String name;
                int cnt = 1;
                if (copiedFile.isDirectory()) {
                    name = copiedFile.getName() + " - Копия";
                } else {
                    name = copiedFile.getName().substring(0, copiedFile.getName().lastIndexOf('.')) + " - Копия" + copiedFile.getName().substring(copiedFile.getName().lastIndexOf('.'));
                }

                String newCopyName = name;

                while (true) {
                    File newfile = new File(pathToPaste + "\\" + newCopyName);
                    if (newfile.exists()) {
                        if (newfile.isDirectory()) {
                            newCopyName = name;
                            newCopyName = newCopyName + "(" + cnt + ")";
                            cnt++;
                        } else {
                            newCopyName = name;
                            newCopyName = newCopyName.replace("- Копия", "- Копия (" + cnt + ")");
                            cnt++;
                        }
                    } else {
                        break;
                    }
                }

                try {
                    Files.move(Path.of(copyPath), Path.of(pathToPaste + "\\" + newCopyName));
                    treeExplorer.getSelectionModel().getSelectedItem().setExpanded(true);
                    TreeItem<Item> newItem = createNodes(new File(pathToPaste + "\\" + newCopyName));
                    treeExplorer.getSelectionModel().getSelectedItem().getChildren().add(newItem);
                    itemToDelete.getParent().getChildren().remove(itemToDelete);
                    if (userNumber == 2) {
                        changes.addNewChange(new Change(ChangesType.MOVE_PHOTO, null, getPath(newItem), null, copyPathFromTree));
                    }
                } catch (IOException e) {
                    showError("Не удалось переместить файл файл");
                }
                isCut = false;
            } else {
                showError("Никакой файл не вырезан!");
            }
        });

        cm.getItems().add(pasteFile);
        cm.setOnShown(windowEvent -> {
            if (treeExplorer.getSelectionModel().getSelectedItem().getValue().isFile() || !isCopy && !isCut) {
                pasteFile.setVisible(false);
            } else {
                pasteFile.setVisible(true);
            }
            if (userNumber == 2) {
                if (treeExplorer.getSelectionModel().getSelectedItem().getValue().isFile()) {
                    deleteFile.setVisible(false);
                } else {
                    deleteFile.setVisible(true);
                }
            } else {
                deleteFile.setVisible(true);
            }
            if (userNumber == 2) {
                shareFolderWithCopy.setVisible(false);
            } else {
                shareFolderWithCopy.setVisible(true);
            }
        });
        return cm;
    }

    private void createChat(String startText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Чат");
            stage.setScene(new Scene(root, 788, 451));
            ChatController ctrl = loader.getController();
            ctrl.setClient(client, userNumber);
            ChatController.setText(startText);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPath(TreeItem<Item> item) {
        StringBuilder newPath = new StringBuilder();
        TreeItem<Item> parent = item;
        while (parent != null) {
            newPath.insert(0, parent.getValue().getName() + "\\");
            parent = parent.getParent();
        }

        return newPath.toString();
    }

    @FXML
    void initialize() {
        AtomicReference<String> pathdir = new AtomicReference<>("");

        TreeItem<Item> root = createNodes(new File(folderPath));
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
                        Photo photo = new Photo(treeExplorer.getSelectionModel().getSelectedItem().getValue().getId(), newFile);
                        photo.setName(s);
                        if (userNumber == 2) {
                            changes.addNewChange(new Change(ChangesType.RENAME_PHOTO,
                                    s,
                                    null,
                                    treeExplorer.getSelectionModel().getSelectedItem().getValue().getName(),
                                    getPath(treeExplorer.getSelectionModel().getSelectedItem().getParent())));
                        }
                        return photo;
                    }
                } else {
                    File newFile = ExplorerCommands.renameFolder(treeExplorer.getSelectionModel().getSelectedItem().getValue().getParentFolder(), s);
                    if (newFile != null) {
                        Folder folder = new Folder(treeExplorer.getSelectionModel().getSelectedItem().getValue().getId(), newFile);
                        folder.renameTo(s);
                        if (userNumber == 2) {
                            changes.addNewChange(new Change(ChangesType.RENAME_FOLDER,
                                    s,
                                    null,
                                    treeExplorer.getSelectionModel().getSelectedItem().getValue().getName(),
                                    getPath(treeExplorer.getSelectionModel().getSelectedItem().getParent())));
                        }
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

        qualityChkBox.setOnAction(actionEvent -> {
            if (qualityChkBox.isSelected()) {
                sliderOddImage.setVisible(true);
            } else {
                sliderOddImage.setVisible(false);
                Client1.compressionQuality = 1f;
            }
        });

        sliderOddImage.valueProperty().addListener((observableValue, number, t1) -> Client1.compressionQuality = t1.floatValue());

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
            userNumber = 2;
            if (!TextKey.getText().trim().isBlank()) {
                client = new Client(userNumber, TextKey.getText());
                new Thread(() -> {
                    client.run();
                    while (client.isClientCanConnect()) {
                        if (client.isClientConnected()) {
                            break;
                        }
                    }
                }).start();
            } else {
                showError("Введите корректный ключ сессии!");
            }

            while (client.isClientCanConnect()) {
                if (client.isClientConnected()) {
                    if (Client2.sizeChangedListener()) {
                        try {
                            treeExplorer.getRoot().getChildren().clear();
                            treeExplorer.setRoot(new TreeItem<>(new Folder(Client2.getLastFolder())));
                            treeExplorer.setVisible(true);
                            connectedpane.setVisible(true);
                            ImageLoading.setVisible(false);
                            keymenu.setVisible(false);
                            createChat("Вы подключились к сессии!");
                            break;
                        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                            ImageBan1.setVisible(true);
                            break;
                        }
                    }
                } else {
                    showError("Ошбика в подключении");
                }
            }
            if (!client.isClientCanConnect()) {
                showError("Неправильный ключ сессии!");
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
            if (!TextPath1.getText().isBlank() || !TextPath1.getText().isEmpty()) {
                ClientData.setPathToFolder(TextPath1.getText());
                client.sendRequest(Client2.getLastFolder());
                ImageLoading.setVisible(true);
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ClientData.isDownloadEnded()) {
                        ImageLoading.setVisible(false);
                        break;
                    }
                }
                ImageOk.setVisible(true);
                treeExplorer.setRoot(null);
                treeExplorer.setRoot(createNodes(new File(TextPath1.getText() + "\\" + Client2.getLastFolder().getName())));
                changes = new Changes();
            } else {
                showError("Выберите папку для загрузки!");
            }
        });
    }
}