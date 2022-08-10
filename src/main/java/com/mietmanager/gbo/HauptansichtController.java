package com.mietmanager.gbo;

import com.mietmanager.logik.Immobilie;
import com.mietmanager.speicherung.Dateiverwalter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.mietmanager.logik.Visualisierbar;
import com.mietmanager.logik.Wohnung;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller für hauptansicht.fxml.
 *
 *  @since       1.0.0
 *  @author      John Robin Pfeifer
 */
public class HauptansichtController implements Controller {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // Modell
    private Immobilie immobilie;
    private File datei;

    // View Nodes
    @FXML private TreeView<Visualisierbar> objektBaum;
    @FXML private VBox objektansicht;
    @FXML private Label titel;

    private Stage fenster;

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
        objektBaum.getSelectionModel().selectedItemProperty().addListener((beobachtbarerWert, alterWert, neuerWert) -> {
            if (neuerWert.getValue() instanceof Immobilie) {
                try {
                    objektansicht.getChildren().clear();
                    objektansicht.getChildren().add(FXMLHelper.laden("immobilienansicht.fxml", neuerWert.getValue()));
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Immobilienansicht konnte nicht geladen werden", e);
                }
            } else if (neuerWert.getValue() instanceof Wohnung) {
                try {
                    objektansicht.getChildren().clear();
                    objektansicht.getChildren().add(FXMLHelper.laden("wohnungsansicht.fxml", neuerWert.getValue()));
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Wohnungsansicht konnte nicht geladen werden", e);
                }
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
                FXMLHelper.oeffneFenster("hauptansicht.fxml", "IMMA – " + neueImmobilie, "iconIMsmall.png", neueImmobilie);
            } catch (IOException ioe) {
                log.log(Level.SEVERE, "Hauptfenster konnte nicht geöffnet werden", ioe);
            }
        }
    }

    public void pdfErstellen() {
        try {
            FXMLHelper.oeffneFenster("pdfErstellenAnsicht.fxml", "IMMA – PDF erstellen", "iconIMsmall.png", immobilie);
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "PDF-Erstellen-Ansicht konnte nicht geöffnet werden", ioe);
        }
    }

    public void uebergeben(Object... daten) {
        this.immobilie = (Immobilie) daten[0];
        this.fenster = (Stage) daten[1];
        einrichten();
    }

    private void einrichten() {
        titel.textProperty().bind(immobilie.bezeichnungProperty());
        fenster.setMaximized(true);

        pflanzeBaum();
    }


    public void oeffneJavaDoc() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/BrigitteAusBurgstaedt/MietManager")); // TODO: 10.08.2022 Javadoc 
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void oeffneGitHub() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/BrigitteAusBurgstaedt/MietManager"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
