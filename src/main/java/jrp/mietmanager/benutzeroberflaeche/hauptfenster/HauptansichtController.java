package jrp.mietmanager.benutzeroberflaeche.hauptfenster;


import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import jrp.mietmanager.logik.Immobilie;
import jrp.mietmanager.logik.Visualisierbar;
import jrp.mietmanager.logik.Wohnung;


public class HauptansichtController {

    // Model
    private Immobilie immobilie;

    // View Nodes
    @FXML private TabPane objektReiterMeister;
    @FXML private TreeView<Visualisierbar> objektBaum;
    @FXML private SplitPane spaltenfenster;

    /**
     * Diese Methode wird automatisch aufgerufen, nachdem der FXML Loader "hauptansicht.fxml" geladen hat.
     */
    public void initialize() {

        // Modell initialisieren
        immobilie = Hauptfenster.getAktuelleImmobilie();

        pflanzeBaum();

    }

    /**
     * Füllt den Tree View mit Tree Items.
     */
    private void pflanzeBaum() {

        // Die Wurzel ist das oberste Tree Item
        TreeItem<Visualisierbar> wurzel = new TreeItem<>();
        wurzel.setExpanded(true);
        wurzel.valueProperty().set(immobilie);

        // 2. Ebene mit Wohnungen füllen
        for (Wohnung w: immobilie.getWohnungen()) {
            TreeItem<Visualisierbar> item = new TreeItem<>();
            item.valueProperty().set(w);
            wurzel.getChildren().add(item);
        }

        objektBaum.setRoot(wurzel);

        // Reiter öffnen bei Auswahl
        objektBaum.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            // Fragt, ob das Objekt welches im Objektbaum ausgewählt wurde schon geöffnet ist
            if (!newValue.getValue().istGeoeffnet()) {
                objektReiterMeister.getTabs().add(newValue.getValue().oeffneReiter()); // Erstellt Reiter und fügt ihn dem Objektreitermeister hinzu
            }

            if (newValue.getValue().brauchtPdfAnsicht()) {
                spaltenfenster.getItems().add(newValue.getValue().oeffnePdfAnsicht());
                spaltenfenster.setDividerPosition(1, 0.6);
            } else {
                // spaltenfenster.getItems().remove(pdfAnsicht); Todo evtl. wieder schließen
            }
        });

    }

}
