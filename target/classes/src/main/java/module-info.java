module NetPhoto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.album to javafx.fxml;
    exports com.album;
    opens com.test to javafx.fxml;
    exports com.test;
}
