package jrp.mietmanager.benutzeroberflaeche.nebenfenster;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jrp.mietmanager.logik.Immobilie;
import jrp.mietmanager.logik.Wohnung;
import jrp.mietmanager.pdf.MonatlicheMieterInformation;
import jrp.mietmanager.speicherung.Dateiverwalter;

import java.io.File;
import java.io.FileNotFoundException;

public class PdfErstellenAnsichtController {

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

    public void uebergeben(Immobilie immobilie, Stage fenster) {
        this.immobilie = immobilie;
        this.fenster = fenster;
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
