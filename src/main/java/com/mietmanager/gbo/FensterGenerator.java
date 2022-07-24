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
 * @since 1.0.1
 * @see Controller
 * @author John Robin Pfeifer
 */
public class FensterGenerator extends Stage {

    /**
     * Ruft die Methode {@link #oeffneFenster(String, String, String, Object...)} auf. Sind die Daten nicht null muss der
     * Controller das Interface {@link Controller} implementieren.
     *
     * @param fxmlDatei die Bezeichnung der FXML-Datei der Ansicht des Fensters. Die Datei sollte sich im dazugehörigen Ressourcenordner befinden
     * @param titel der Titel des Fensters
     * @param icon die Bezeichnung des Icons für das dazugehörige Fenster. Die Datei sollte sich im dazugehörigen Ressourcenordner befinden
     * @param daten die Daten, welche an den Controller übergeben werden sollen
     * @throws IOException wenn die Ansicht nicht geladen werden kann
     */
    public FensterGenerator(String fxmlDatei, String titel, String icon, Object... daten) throws IOException {
        oeffneFenster(fxmlDatei, titel, icon, daten);
    }

    /**
     * Lädt die {@link Scene} vom Fenster aus einer FXML-Datei und aktiviert den
     * {@link Controller}, welchem die Daten übergeben werden. Zudem werden Icon und Titel
     * des Fensters gesetzt.
     *
     * @param fxmlDatei die Bezeichnung der FXML-Datei der Ansicht des Fensters. Die Datei sollte sich im dazugehörigen Ressourcenordner befinden
     * @param titel der Titel des Fensters
     * @param icon die Bezeichnung des Icons für das dazugehörige Fenster. Die Datei sollte sich im dazugehörigen Ressourcenordner befinden
     * @param daten die Daten, welche an den Controller übergeben werden sollen
     * @throws IOException wenn die Ansicht nicht geladen werden kann
     */
    private void oeffneFenster(String fxmlDatei, String titel, String icon, Object... daten) throws IOException {
        FXMLLoader ansichtlader = new FXMLLoader(getClass().getResource(fxmlDatei));
        Scene ansicht = new Scene(ansichtlader.load());
        if (daten != null) {
            Object[] datenMitFenster = Arrays.copyOf(daten, daten.length + 1);
            datenMitFenster[daten.length] = this;
            ((Controller) ansichtlader.getController()).uebergeben(datenMitFenster);
        }
        getIcons().add(new Image(Objects.requireNonNull(getClass().getResource(icon)).toString()));
        setTitle(titel);
        setScene(ansicht);
        show();
    }
}
