module jrp.mietmanager {
    requires javafx.swing;
    requires javafx.fxml;
    requires java.logging;
    requires kernel;
    requires layout;
    requires io;
    requires javafx.controls;

    opens jrp.mietmanager to javafx.fxml;
    exports jrp.mietmanager;
    opens jrp.mietmanager.benutzeroberflaeche.hauptfenster to javafx.fxml;
    exports jrp.mietmanager.benutzeroberflaeche.hauptfenster;
    opens jrp.mietmanager.benutzeroberflaeche.nebenfenster to javafx.fxml;
    exports jrp.mietmanager.benutzeroberflaeche.nebenfenster;
    exports jrp.mietmanager.logik;
    exports jrp.mietmanager.speicherung;
}