package jrp.mietmanager.benutzeroberflaeche.nebenfenster;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Startfenster extends Stage {
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Startfenster() {
        oeffneFenster();
    }

    private void oeffneFenster() {
        FXMLLoader startfensterAnsichtLader = new FXMLLoader(getClass().getResource("startfensterAnsicht.fxml"));

        Scene startfensterAnsicht = null;
        try {
            startfensterAnsicht = new Scene(startfensterAnsichtLader.load());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Die Startfensteransicht konnte nicht geladen werden.", e);
        }

        getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("iconIMsmall.png")).toString()));
        setTitle("ImmoMa - Startfenster");
        setScene(startfensterAnsicht);
        show();
    }
}
