import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;


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
    private TextArea TextTemp;

    @FXML
    private Button ButtonStart;

    @FXML
    private Button ButtonBrowse;

    @FXML
    void initialize() throws IOException {

        ButtonUser1.setOnAction(actionEvent -> {
            ButtonUser1.setVisible(false);
            ButtonUser2.setVisible(false);
            Name.setVisible(false);
            TextGeneratedKey.setVisible(true);
            TextPath.setVisible(true);
            TextTemp.setVisible(true);
            ButtonStart.setVisible(true);
            ButtonBrowse.setVisible(true);
        });

        ButtonBrowse.setOnAction(actionEvent -> {
            final DirectoryChooser dirChooser = new DirectoryChooser();
            File dir = dirChooser.showDialog(null);
            if (dir != null) {
                TextPath.setText(dir.getAbsolutePath());
            } else {
                TextPath.setText(null);
            }
        });

        ButtonStart.setOnAction(actionEvent -> {
            TextGeneratedKey.setText(Encrypt.encrypt("00"));
        });
    }
}