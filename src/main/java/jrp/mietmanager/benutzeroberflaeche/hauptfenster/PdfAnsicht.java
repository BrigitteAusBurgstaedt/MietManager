package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import jrp.mietmanager.logik.Wohnung;
import jrp.mietmanager.logik.Zaehlerstand;

import java.text.DecimalFormat;

public class PdfAnsicht extends VBox {
    private Wohnung wohnung;

    public PdfAnsicht(Wohnung wohnung) {
        this.wohnung = wohnung;

        VBox platzhalter = new VBox();
        VBox fuerTitel = new VBox();
        Label titel = new Label();

        platzhalter.getStyleClass().add("platzhalter");
        fuerTitel.getStyleClass().add("behaelter");
        titel.getStyleClass().add("titel-label");

        fuerTitel.setAlignment(Pos.CENTER);
        titel.textProperty().bind(wohnung.bezeichnungProperty());

        fuerTitel.getChildren().add(titel);
        platzhalter.getChildren().add(fuerTitel);
        getChildren().add(platzhalter);

        getStyleClass().add("hauptbehaelter");
        erstelleDiagramm();
    }

    private void erstelleDiagramm() {

        //
        // Diagrammelemente
        //
        NumberAxis xAchse = new NumberAxis("Monat", 0.0, 1.0, (1.0/12.0)); // X-Achse mit Datum
        NumberAxis yAchse = new NumberAxis(); // Y-Achse mit Zählerwerten
        LineChart<Number, Number> diagramm = new LineChart<>(xAchse, yAchse);
        ObservableList<XYChart.Data<Number, Number>> daten = FXCollections.observableArrayList();
        XYChart.Series<Number, Number> serie = new XYChart.Series<>(daten);

        // Füllen der Datenreihe (nötig, wenn zwischen Wohnungen gewechselt wird)
        for (Zaehlerstand z : wohnung.getZaehlerstaende()) {
            XYChart.Data<Number, Number> datenpunkt = new XYChart.Data<>();
            datenpunkt.XValueProperty().bind(z.datumAlsDoubleProperty());
            datenpunkt.YValueProperty().bind(z.wertProperty());
            daten.add(datenpunkt);
            System.out.println("Im Loop");
        }

        wohnung.getZaehlerstaende().addListener((ListChangeListener<Zaehlerstand>) veraenderung -> {
            while (veraenderung.next()) {

                if (!veraenderung.wasAdded()) {
                    daten.remove(veraenderung.getFrom());
                }

                if (!veraenderung.wasRemoved()) {
                    for (Zaehlerstand z : veraenderung.getAddedSubList()) {
                        XYChart.Data<Number, Number> datenpunkt = new XYChart.Data<>();
                        datenpunkt.XValueProperty().bind(z.datumAlsDoubleProperty());
                        datenpunkt.YValueProperty().bind(z.wertProperty());
                        daten.add(datenpunkt);
                    }
                }

                // obere und untere Schwelle angleichen
                double min = wohnung.getZaehlerstaende().get(0).getDatumAlsDouble();
                double max = wohnung.getZaehlerstaende().get(wohnung.getZaehlerstaende().size() - 1).getDatumAlsDouble();
                double obereSchwelle;

                if ((max - min) > 1)
                    obereSchwelle = max + ((1.0/12.0) - (max % (1.0/12.0)));
                else
                    obereSchwelle = min - (min % (1.0/12.0)) + 1;

                double untereSchwelle = obereSchwelle - 1;

                xAchse.setLowerBound(untereSchwelle);
                xAchse.setUpperBound(obereSchwelle);
            }
        });

        // Beschriftung der Achse
        xAchse.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number number) {
                DecimalFormat df = new DecimalFormat("#.00");
                return switch (df.format(number.doubleValue() % 1.0)) {
                    case ",00", "1,00" -> "Jan";
                    case ",08" -> "Feb";
                    case ",17" -> "Mär";
                    case ",25" -> "Apr";
                    case ",33" -> "Mai";
                    case ",42" -> "Jun";
                    case ",50" -> "Jul";
                    case ",58" -> "Aug";
                    case ",67" -> "Sep";
                    case ",75" -> "Okt";
                    case ",83" -> "Nov";
                    case ",92" -> "Dez";
                    default -> null;
                };
            }

            @Override
            public Number fromString(String s) {
                return null;
            }
        });

        xAchse.setMinorTickCount(4); // pro Woche ein Strich
        yAchse.setForceZeroInRange(false);
        yAchse.setLabel("Zählerwert");
        serie.setName("Serie 1");

        // Serie hinzufügen
        diagramm.getData().add(serie);

        getChildren().add(diagramm);
    }

    // Getter und Setter

    public Wohnung getWohnung() {
        return wohnung;
    }

    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
    }
}
