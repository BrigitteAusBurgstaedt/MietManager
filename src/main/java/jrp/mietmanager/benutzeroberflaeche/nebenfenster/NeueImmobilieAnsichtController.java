package jrp.mietmanager.benutzeroberflaeche.nebenfenster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jrp.mietmanager.benutzeroberflaeche.hauptfenster.Hauptfenster;
import jrp.mietmanager.logik.Immobilie;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NeueImmobilieAnsichtController {
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @FXML public Button immobilieErstellenTaster;
    @FXML public TextField bezeichnungFeld;
    @FXML public TextField anzahlFeld;

    @FXML
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
        new Hauptfenster(immobilie);

        // Abschließend das Aktuelle Fenster schließen
        Button taster = (Button) actionEvent.getSource();
        Stage fenster = (Stage) taster.getScene().getWindow();
        fenster.close();
    }
}
