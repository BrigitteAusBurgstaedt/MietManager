package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import jrp.mietmanager.logik.Wohnung;
import jrp.mietmanager.logik.Zaehlermodus;
import jrp.mietmanager.logik.Zaehlerstand;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WohnungsReiter extends Tab {
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final TableView<Zaehlerstand> tabelle = new TableView<>();
    private final Wohnung wohnung;

    /**
     * Konstruktor des Wohnungsreiters welcher die Methode erzeugeReiter aufruft.
     *
     * @param wohnung Übernimmt die Wohnung, welche vom Tab/Reiter widergespiegelt werden soll.
     */
    public WohnungsReiter(Wohnung wohnung) {
        this.wohnung = wohnung;
        erzeugeReiter();
    }

    /**
     * Erzeugt den Tab/Reiter welcher der Tab Pane hinzugefügt werden soll.
     */
    private void erzeugeReiter() {
        textProperty().bind(wohnung.bezeichnungProperty());

        VBox container = new VBox();
        HBox fuerEingabe = new HBox();
        HBox fuerTaster = new HBox();

        DatePicker datumsFeld = new DatePicker();
        datumsFeld.setPromptText("tt.mm.jjjj");

        TextField wertFeld = new TextField();
        fuerEingabe.getChildren().addAll(datumsFeld, wertFeld);

        Button hinzufuegen = new Button("Hinzufügen");
        hinzufuegen.setOnAction(actionEvent -> {
            try {
                datumsFeld.setValue(datumsFeld.getConverter().fromString(datumsFeld.getEditor().getText()));
                wohnung.hinzufuegen(new Zaehlerstand(datumsFeld.getValue(), Double.parseDouble(wertFeld.getText()), Zaehlermodus.HEIZUNG));
            } catch (Exception e) {
                log.log(Level.WARNING, "Eingabe ungueltig.", e);

                Alert info = new Alert(Alert.AlertType.INFORMATION, "Es scheint als wäre etwas bei der Eingabe nicht ganz richtig.");
                info.setHeaderText("Überprüfen sie die Eingabe!");
                info.show();
            }
        });

        Button entfernen = new Button("Entfernen");
        entfernen.setOnAction(actionEvent -> {

            tabelle.getItems().removeAll(
                    tabelle.getSelectionModel().getSelectedItems()
            );
        });

        fuerTaster.getChildren().addAll(hinzufuegen, entfernen);

        erstelleTabelle();
        container.getChildren().addAll(tabelle, fuerEingabe, fuerTaster);
        setContent(container);
    }

    /**
     * Erstellt die Tabelle die alle Zählerstände enthält.
     */
    private void erstelleTabelle() {
        tabelle.setItems(wohnung.getZaehlerstaende());
        tabelle.setPlaceholder(new Label("Es gibt noch keine Zählerstände."));
        tabelle.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Zaehlerstand, LocalDate> c1 = new TableColumn<>("Datum");
        c1.setCellValueFactory(zaehlerstandStringCellDataFeatures -> zaehlerstandStringCellDataFeatures.getValue().datumProperty());
        tabelle.getColumns().add(c1);

        TableColumn<Zaehlerstand, Number> c2 = new TableColumn<>("Zählerstand");
        c2.setCellValueFactory(zaehlerstandNumberCellDataFeatures -> zaehlerstandNumberCellDataFeatures.getValue().wertProperty());
        tabelle.getColumns().add(c2);

        TableColumn<Zaehlerstand, Number> c3 = new TableColumn<>("Differenz");
        c3.setCellValueFactory(zaehlerstandNumberCellDataFeatures -> zaehlerstandNumberCellDataFeatures.getValue().differenzProperty());
        tabelle.getColumns().add(c3);

    }

    private LineChart<Number, Number> erstelleDiagramm() {
        NumberAxis xAchse = new NumberAxis("Monat", 0, 13, 1);
        NumberAxis yAchse = new NumberAxis();
        LineChart<Number, Number> diagramm = new LineChart<>(xAchse, yAchse);
        XYChart.Series<Number, Number> serie = new XYChart.Series<>(); // TODO: 19.04.2022 Datenpunkte 

        // Beschriftung der Achse
        xAchse.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return switch (number.toString()) {
                    case "0.0" -> "Jan";
                    case "1.0" -> "Feb";
                    case "2.0" -> "Mär";
                    case "3.0" -> "Apr";
                    case "4.0" -> "Mai";
                    case "5.0" -> "Jun";
                    case "6.0" -> "Jul";
                    case "7.0" -> "Aug";
                    case "8.0" -> "Sep";
                    case "9.0" -> "Okt";
                    case "10.0" -> "Nov";
                    case "11.0" -> "Dez";
                    default -> null;
                };
            }

            @Override
            public Number fromString(String s) {
                return null;
            }
        });

        return diagramm;
    }


}
