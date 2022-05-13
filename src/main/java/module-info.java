module jrp.mietmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires kernel;
    requires layout;

    opens jrp.mietmanager to javafx.fxml;
    exports jrp.mietmanager;
    opens jrp.mietmanager.benutzeroberflaeche.hauptfenster to javafx.fxml;
    exports jrp.mietmanager.benutzeroberflaeche.hauptfenster;
    opens jrp.mietmanager.benutzeroberflaeche.nebenfenster to javafx.fxml;
    exports jrp.mietmanager.benutzeroberflaeche.nebenfenster;
}