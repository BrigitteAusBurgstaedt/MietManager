package jrp.mietmanager.benutzeroberflaeche.nebenfenster;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Startfenster extends Stage {
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Startfenster() {
        oeffneFenster();
    }

    private void oeffneFenster() {
        FXMLLoader startansichtlader = new FXMLLoader(getClass().getResource("startansicht.fxml"));


        Scene startansicht = null;
        try {
            startansicht = new Scene(startansichtlader.load());
        } catch (IOException e) {
            System.out.println(getClass());
            log.log(Level.SEVERE, "Die Startansicht konnte nicht geladen werden.", e);
        }

        setScene(startansicht);
        setTitle("MietManager");
        show();
    }
}
