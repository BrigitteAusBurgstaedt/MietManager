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

/**
 * Ist das Fenster, welches die Ansichten zu einer bestimmten {@link Immobilie} enthält.
 *
 * @since       1.0
 * @author      John Robin Pfeifer
 */
public class Hauptfenster extends Stage {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Ruft die Methode {@link #oeffneFenster(Immobilie)} auf.
     *
     * @param immobilie Die Immobilie im Hauptfenster bearbeitet werden soll.
     * @see #oeffneFenster(Immobilie)
     */
    public Hauptfenster(Immobilie immobilie) {
        oeffneFenster(immobilie);
    }

    /**
     * Lädt die {@link Scene} vom Hauptfenster aus hauptansicht.fxml und aktiviert den
     * {@link HauptansichtController}, welchem die Stage und die Immobilie übergeben wird. Zudem werden Icon und Titel
     * des Fensters gesetzt.
     *
     * @param immobilie Die Immobilie im Hauptfenster bearbeitet werden soll.
     * @see HauptansichtController
     */
    private void oeffneFenster(Immobilie immobilie) {
        FXMLLoader hauptansichtLader = new FXMLLoader(getClass().getResource("hauptansicht.fxml"));

        Scene hauptansicht = null;
        try {
            hauptansicht = new Scene(hauptansichtLader.load());
            HauptansichtController c = hauptansichtLader.getController();
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
