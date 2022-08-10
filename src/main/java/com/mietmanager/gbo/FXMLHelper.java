package com.mietmanager.gbo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Generator für Fenster.
 *
 * @author John Robin Pfeifer
 * @see Controller
 * @since 1.0.1
 */
public final class FXMLHelper {

    /**
     * Privater Konstruktor da es sich um eine Werkzeugklasse handelt.
     */
    private FXMLHelper(){}

    /**
     * Lädt die {@link Scene} vom Fenster aus einer FXML-Datei und aktiviert den
     * {@link Controller}, welchem die Daten übergeben werden. Zudem werden Icon und Titel
     * des Fensters gesetzt.
     *
     * @param fxmlDatei die Bezeichnung der FXML-Datei der Ansicht des Fensters. Die Datei sollte sich im dazugehörigen Ressourcenordner befinden
     * @param titel     der Titel des Fensters
     * @param icon      die Bezeichnung des Icons für das dazugehörige Fenster. Die Datei sollte sich im dazugehörigen Ressourcenordner befinden
     * @param daten     die Daten, welche an den Controller übergeben werden sollen
     * @throws IOException wenn die Ansicht nicht geladen werden kann
     */
    public static Stage oeffneFenster(String fxmlDatei, String titel, String icon, Object... daten) throws IOException {
        Stage fenster = new Stage();

        Object[] datenMitFenster = null;
        if (daten != null) {
            datenMitFenster = Arrays.copyOf(daten, daten.length + 1);
            datenMitFenster[daten.length] = fenster;
        }
        Scene ansicht = new Scene(FXMLHelper.laden(fxmlDatei, datenMitFenster));

        fenster.getIcons().add(new Image(Objects.requireNonNull(FXMLHelper.class.getResource(icon)).toString()));
        fenster.setTitle(titel);
        fenster.setScene(ansicht);
        fenster.show();

        return fenster;
    }

    /**
     * Lädt eine FXML-Datei und übergibt die Daten an den Controller.
     *
     * @param fxmlDatei der Name der FXML-Datei. Diese sollte sich im Ressourcenordner des FXMLHelpers befinden
     * @param daten die zu übergebenden Daten
     * @param <T> der generische Datentyp
     * @return die geladene Ansicht
     * @throws IOException wenn die Ansicht nicht geladen werden kann
     * @see Controller
     * @since 1.0.3
     */
    public static <T> T laden(String fxmlDatei, Object... daten) throws IOException {
        FXMLLoader ansichtlader = new FXMLLoader(FXMLHelper.class.getResource(fxmlDatei));
        T ansicht = ansichtlader.load();
        if (daten != null) {
            ((Controller) ansichtlader.getController()).uebergeben(daten);
        }
        return ansicht;
    }
}
