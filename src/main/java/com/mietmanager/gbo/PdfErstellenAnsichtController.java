package com.mietmanager.gbo;

import com.mietmanager.gbo.Controller;
import com.mietmanager.logik.Immobilie;
import com.mietmanager.pdf.MonatlicheMieterInformation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import com.mietmanager.logik.Wohnung;

import java.io.File;

public class PdfErstellenAnsichtController implements Controller {

    private Immobilie immobilie;
    private Stage fenster;

    @FXML private VBox behaelter;
    @FXML private TextField monat;
    @FXML private TextField jahr;

    public void erstelleTabelle() {
        TableView<Wohnung> tabelle = new TableView<>();

        tabelle.setItems(immobilie.getWohnungen()); // Tabelle soll die Wohnungen der enthalten
        tabelle.setPlaceholder(new Label("Noch keine Wohnungen erstellt."));
        tabelle.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Wohnung, String> c1 = new TableColumn<>("Bezeichnung");
        c1.setCellValueFactory(wohnungStringCellDataFeatures -> wohnungStringCellDataFeatures.getValue().bezeichnungProperty());
        c1.setCellFactory(TextFieldTableCell.forTableColumn());
        tabelle.getColumns().add(c1);

        behaelter.getChildren().add(0, tabelle);
    }

    public void uebergeben(Object... daten) {
        this.immobilie = (Immobilie) daten[0];
        this.fenster = (Stage) daten[1];
        erstelleTabelle();
    }

    public void pdfErstellen() {
        DirectoryChooser verzeichnisWaehler = new DirectoryChooser();

        verzeichnisWaehler.setTitle("Ordner für PDFs");

        File verzeichnis = verzeichnisWaehler.showDialog(fenster);
        if (verzeichnis != null) {
            try {
                new MonatlicheMieterInformation(verzeichnis.getAbsolutePath(), immobilie, Integer.parseInt(monat.getText()), Integer.parseInt(jahr.getText()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } // TODO: 22.07.2022 else : Alert kein Verzeichnis ausgewählt

        fenster.close();
    }
}
