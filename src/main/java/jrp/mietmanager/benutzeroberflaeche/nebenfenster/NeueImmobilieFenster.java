package jrp.mietmanager.benutzeroberflaeche.nebenfenster;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NeueImmobilieFenster extends Stage {
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public NeueImmobilieFenster() {
        oeffneFenster();
    }

    private void oeffneFenster() {
        FXMLLoader neueImmobileAnsichtLader = new FXMLLoader(getClass().getResource("neueImmobilieAnsicht.fxml"));

        Scene neueImmobileAnsicht = null;
        try {
            neueImmobileAnsicht = new Scene(neueImmobileAnsichtLader.load());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Die \"Neue Immobilien Ansicht\" konnte nicht geladen werden.", e);
        }

        setScene(neueImmobileAnsicht);
        setTitle("Neue Immobilie erstellen");
        show();
    }
}
