package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jrp.mietmanager.logik.Immobilie;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hauptfenster extends Stage {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Hauptfenster(Immobilie immobilie) {
        oeffneFenster(immobilie);
    }

    private void oeffneFenster(Immobilie immobilie) {
        FXMLLoader mainViewLader = new FXMLLoader(getClass().getResource("hauptansicht.fxml"));

        Scene hauptansicht = null;
        try {
            hauptansicht = new Scene(mainViewLader.load());
            HauptansichtController c = mainViewLader.getController();
            c.uebergeben(this, immobilie);
            c.pflanzeBaum();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Die Hauptansicht konnte nicht geladen werden.", e);
        }


        getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("iconIMsmall.png")).toString()));
        setTitle("ImmoMa - " + immobilie.toString());
        setMaximized(true);
        setScene(hauptansicht);
        show();
    }
}
