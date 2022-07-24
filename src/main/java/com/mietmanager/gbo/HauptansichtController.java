package com.mietmanager.gbo;

import com.mietmanager.gbo.Controller;
import com.mietmanager.gbo.FensterGenerator;
import com.mietmanager.logik.Immobilie;
import com.mietmanager.speicherung.Dateiverwalter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.mietmanager.logik.Visualisierbar;
import com.mietmanager.logik.Wohnung;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller für hauptansicht.fxml.
 */
public class HauptansichtController implements Controller {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Immobilie immobilie;
    private Stage fenster;
    private File datei;

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
            w.bezeichnungProperty().addListener((b, a, n) -> objektBaum.refresh());
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

        datei = dateiWaehler.showSaveDialog(fenster);
        if (datei != null) {
            Dateiverwalter.speichern(datei, immobilie);
        }

    }

    public void speichern() {
        if (datei != null) {
            Dateiverwalter.speichern(datei, immobilie);
        } else {
            speichernUnter();
        }
    }

    public void oeffnen() {
        FileChooser dateiWaehler = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Immobilien Dateien", "*.immo");

        dateiWaehler.setTitle("Öffnen");
        dateiWaehler.getExtensionFilters().add(filter);

        datei = dateiWaehler.showOpenDialog(fenster);
        if (datei != null) {
            Immobilie neueImmobilie = Dateiverwalter.lesen(datei);
            try {
                new FensterGenerator("hauptansicht.fxml", "MietManager - " + neueImmobilie, "iconIMsmall.png", neueImmobilie);
            } catch (IOException ioe) {
                log.log(Level.SEVERE, "Hauptfenster konnte nicht geöffnet werden", ioe);
            }
        }
    }

    public void pdfErstellen() {
        try {
            new FensterGenerator("pdfErstellenAnsicht.fxml", "MietManager - PDF erstellen", "iconIMsmall.png", immobilie);
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "PDF-Erstellen-Ansicht konnte nicht geöffnet werden", ioe);
        }
    }

    public void uebergeben(Object... daten) {
        this.immobilie = (Immobilie) daten[0];
        this.fenster = (Stage) daten[1];
        fenster.setMaximized(true);
        pflanzeBaum();
    }


}
