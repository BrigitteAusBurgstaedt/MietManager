package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jrp.mietmanager.logik.Immobilie;
import jrp.mietmanager.logik.Visualisierbar;
import jrp.mietmanager.logik.Wohnung;
import jrp.mietmanager.speicherung.Dateiverwalter;
import java.io.File;


public class HauptansichtController {

    // Model
    private Immobilie immobilie;

    private Stage fenster;

    // View Nodes
    @FXML private TabPane objektReiterMeister;
    @FXML private TreeView<Visualisierbar> objektBaum;
    @FXML private SplitPane spaltenfenster;

    /**
     * Füllt den Tree View mit Tree Items.
     */
    public void pflanzeBaum() {

        // Die Wurzel ist das oberste Tree Item
        TreeItem<Visualisierbar> wurzel = new TreeItem<>();
        wurzel.setExpanded(true);
        wurzel.valueProperty().set(immobilie);

        // 2. Ebene mit Wohnungen füllen
        for (Wohnung w: immobilie.getWohnungen()) {
            TreeItem<Visualisierbar> item = new TreeItem<>();
            item.valueProperty().set(w);
            wurzel.getChildren().add(item);
            w.bezeichnungProperty().addListener((observableValue, s, t1) -> objektBaum.refresh());
        }

        objektBaum.setRoot(wurzel);

        // Reiter öffnen bei Auswahl
        objektBaum.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            // Fragt, ob das Objekt welches im Objektbaum ausgewählt wurde schon geöffnet ist
            if (!newValue.getValue().istGeoeffnet()) {
                objektReiterMeister.getTabs().add(newValue.getValue().oeffneReiter()); // Erstellt Reiter und fügt ihn dem Objektreitermeister hinzu
            }

            // Braucht das Ausgewählte Objekt die PDF-Ansicht? UND Ist diese noch nicht geöffnet?
            if (newValue.getValue().brauchtPdfAnsicht() && !spaltenfenster.getItems().contains(newValue.getValue().oeffnePdfAnsicht(immobilie))) {

                // Ist bereits eine andere PDF-Ansicht geöffnet?
                if (spaltenfenster.getItems().size() == 3)
                    spaltenfenster.getItems().remove(2); // dann entferne diese
                // öffne die PDF-Ansicht
                spaltenfenster.getItems().add(newValue.getValue().oeffnePdfAnsicht(immobilie));
                spaltenfenster.setDividerPosition(1, 0.55);
            }
        });

    }

    public void speichernUnter() {
        FileChooser dateiWaehler = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Immobilien Dateien", "*.immo");

        dateiWaehler.setTitle("Speichern unter");
        dateiWaehler.getExtensionFilters().add(filter);

        File ausgewaelteDatei = dateiWaehler.showSaveDialog(fenster);
        if (ausgewaelteDatei != null) {
            Dateiverwalter.speichern(ausgewaelteDatei, immobilie);
        }

    }

    public void oeffnen() {
        FileChooser dateiWaehler = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Immobilien Dateien", "*.immo");

        dateiWaehler.setTitle("Öffnen");
        dateiWaehler.getExtensionFilters().add(filter);

        File ausgewaelteDatei = dateiWaehler.showOpenDialog(fenster);
        if (ausgewaelteDatei != null) {
            new Hauptfenster(Dateiverwalter.lesen(ausgewaelteDatei));
        }
    }

    public void uebergeben(Stage fenster, Immobilie immobilie) {
        this.fenster = fenster;
        this.immobilie = immobilie;
    }
}
