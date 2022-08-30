package com.mietmanager.gbo;

import com.mietmanager.logik.Immobilie;
import com.mietmanager.speicherung.Dateiverwalter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller für startansicht.fxml.
 *
 *  @since       1.0.0
 *  @author      John Robin Pfeifer
 */
public class StartansichtController implements Controller {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // Ansicht
    @FXML public Button immobilieErstellenTaster;
    @FXML public TextField bezeichnungFeld;
    @FXML public TextField anzahlFeld;

    private Stage fenster;

    public void hauptfensterOeffnen(ActionEvent actionEvent) {

        // Neue Immobilie Anlegen
        // Bezeichnung der Immobilie
        String bezeichnung = bezeichnungFeld.getText();
        if (bezeichnung.equals("")) {
            bezeichnung = "Neue Immobilie"; // Standard Bezeichnung
        }

        // Anzahl der Wohnungen
        int anzahl = 1; // Standard Anzahl der Wohnungen

        if (!anzahlFeld.getText().equals("")) {
            try {
                if (Integer.parseInt(anzahlFeld.getText()) > anzahl) // Wert wird nur übernommen, wenn er größer als 1 ist
                    anzahl = Integer.parseInt(anzahlFeld.getText());
            } catch (NumberFormatException e) {
                log.log(Level.WARNING, "Es wurde eine falsche Eingabe getroffen.", e);

                Alert info = new Alert(Alert.AlertType.INFORMATION, "Die Wohnungsanzahl muss als ganze Zahl angegeben werden.");
                info.setHeaderText("Überprüfen sie die Eingabe!");
                info.show();

                return; // Die Methode wird Verlassen ohne das neue Fenster zu öffnen
            }
        }

        Immobilie immobilie = new Immobilie(bezeichnung, anzahl);

        // Das Hauptfenster öffnen
        try {
            FXMLHelfer.oeffneFenster("hauptansicht.fxml", "IMMA – Hauptfenster", "iconIMsmall.png", immobilie);
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "Hauptfenster konnte nicht geöffnet werden", ioe);
        }

        // Abschließend das Aktuelle Fenster schließen
        Button taster = (Button) actionEvent.getSource();
        Stage fensterAktuell = (Stage) taster.getScene().getWindow();
        fensterAktuell.close();
    }

    @Override
    public void uebergeben(Object... daten) {
        this.fenster = (Stage) daten[0];
    }

    public void oeffnen(ActionEvent actionEvent) {
        FileChooser dateiWaehler = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Immobilien Dateien", "*.immo");

        dateiWaehler.setTitle("Öffnen");
        dateiWaehler.getExtensionFilters().add(filter);

        File datei = dateiWaehler.showOpenDialog(fenster);
        if (datei != null) {
            Immobilie neueImmobilie = Dateiverwalter.lesen(datei);
            try {
                FXMLHelfer.oeffneFenster("hauptansicht.fxml", "IMMA – " + neueImmobilie, "iconIMsmall.png", neueImmobilie);
            } catch (IOException ioe) {
                log.log(Level.SEVERE, "Hauptfenster konnte nicht geöffnet werden", ioe);
            }
        }

        // Abschließend das Aktuelle Fenster schließen
        Button taster = (Button) actionEvent.getSource();
        Stage fensterAktuell = (Stage) taster.getScene().getWindow();
        fensterAktuell.close();
    }
}
