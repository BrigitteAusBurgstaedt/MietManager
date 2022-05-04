package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jrp.mietmanager.logik.Immobilie;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hauptfenster extends Stage {
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static Immobilie aktuelleImmobilie;

    public Hauptfenster(Immobilie aktuelleImmobilie) {
        Hauptfenster.aktuelleImmobilie = aktuelleImmobilie;
        oeffneFenster();
    }

    private void oeffneFenster() {
        FXMLLoader mainViewLader = new FXMLLoader(getClass().getResource("hauptansicht.fxml"));

        Scene hauptansicht = null;
        try {
            hauptansicht = new Scene(mainViewLader.load());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Die Hauptansicht konnte nicht geladen werden.", e);
        }

        setScene(hauptansicht);
        setTitle("MietManager");
        setMaximized(true); // Für Fullscreen Fenster
        show();
    }

    public static Immobilie getAktuelleImmobilie() {
        return aktuelleImmobilie;
    }
}
