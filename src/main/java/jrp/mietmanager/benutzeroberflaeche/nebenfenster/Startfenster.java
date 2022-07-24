package jrp.mietmanager.benutzeroberflaeche.nebenfenster;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ist das Fenster, welches zum Programmstart geöffnet wird.
 *
 * @since       1.0
 * @author      John Robin Pfeifer
 */
public class Startfenster extends Stage {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Ruft die Methode {@link #oeffneFenster()} auf.
     *
     * @see #oeffneFenster()
     */
    public Startfenster() {
        oeffneFenster();
    }

    /**
     * Lädt die {@link Scene} vom Startfenster aus startfensterAnsicht.fxml und aktiviert den
     * {@link StartansichtController}. Zudem werden Icon und Titel des Fensters gesetzt.
     *
     * @see StartansichtController
     */
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
