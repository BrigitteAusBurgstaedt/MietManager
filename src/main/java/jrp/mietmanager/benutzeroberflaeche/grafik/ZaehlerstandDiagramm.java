package jrp.mietmanager.benutzeroberflaeche.grafik;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;
import javafx.util.StringConverter;
import jrp.mietmanager.logik.Immobilie;
import jrp.mietmanager.logik.Wohnung;
import jrp.mietmanager.logik.Zaehlerstand;

import java.text.DecimalFormat;

public class ZaehlerstandDiagramm extends Region {

    private final ObservableList<XYChart.Data<Number, Number>> daten;
    private final Wohnung wohnung;

    private final NumberAxis xAchse = new NumberAxis("Monat", 0.0, 1.0, (1.0 / 12.0)); // X-Achse mit Datum
    private final NumberAxis yAchse = new NumberAxis(); // Y-Achse mit Zählerwerten

    public ZaehlerstandDiagramm(Wohnung wohnung) {
        this.wohnung = wohnung;

        daten = FXCollections.observableArrayList();

        erstelleDiagramm();
    }

    private void erstelleDiagramm() {

        LineChart<Number, Number> diagramm = new LineChart<>(xAchse, yAchse);
        XYChart.Series<Number, Number> serie = new XYChart.Series<>(daten);

        // Erstbefüllung der Datenreihe
        for (Zaehlerstand z : wohnung.getZaehlerstaende()) {
            verknuepfen(z);
        }

        wohnung.getZaehlerstaende().addListener((ListChangeListener<Zaehlerstand>) veraenderung -> {
            while (veraenderung.next()) {

                if (veraenderung.wasAdded()) {
                    for (Zaehlerstand z : veraenderung.getAddedSubList()) {
                        verknuepfen(z);
                    }
                } else if (veraenderung.wasRemoved()) {
                    daten.clear();

                    for (Zaehlerstand z : wohnung.getZaehlerstaende()) {
                        verknuepfen(z);
                    }
                }

                if (!wohnung.getZaehlerstaende().isEmpty()) {
                    berechneSchwellen();
                }

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

    private void berechneSchwellen() {
        // obere und untere Schwelle angleichen
        double min = wohnung.getZaehlerstaende().get(0).getDatumAlsDouble();
        double max = wohnung.getZaehlerstaende().get(wohnung.getZaehlerstaende().size() - 1).getDatumAlsDouble();
        double obereSchwelle;

        if ((max - min) > 1)
            obereSchwelle = max + ((1.0 / 12.0) - (max % (1.0 / 12.0)));
        else
            obereSchwelle = min - (min % (1.0 / 12.0)) + 1;

        double untereSchwelle = obereSchwelle - 1;

        xAchse.setLowerBound(untereSchwelle);
        xAchse.setUpperBound(obereSchwelle);
    }

    private void verknuepfen(Zaehlerstand z) {
        XYChart.Data<Number, Number> datenpunkt = new XYChart.Data<>();
        datenpunkt.XValueProperty().bind(z.datumAlsDoubleProperty());
        datenpunkt.YValueProperty().bind(z.differenzProperty());
        daten.add(datenpunkt);
    }
}
