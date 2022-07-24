module com.mietmanager {
    requires javafx.swing;
    requires javafx.fxml;
    requires java.logging;
    requires kernel;
    requires layout;
    requires io;
    requires javafx.controls;

    opens com.mietmanager to javafx.fxml;
    exports com.mietmanager;
    exports com.mietmanager.logik;
    exports com.mietmanager.speicherung;
    exports com.mietmanager.gbo;
    opens com.mietmanager.gbo to javafx.fxml;
}