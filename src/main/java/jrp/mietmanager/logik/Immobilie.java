package jrp.mietmanager.logik;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import jrp.mietmanager.benutzeroberflaeche.hauptfenster.ImmobilienReiter;

import java.util.logging.Logger;

public class Immobilie implements Visualisierbar {
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final StringProperty bezeichnung;
    private final IntegerProperty wohnungsanzahl;
    private final ObservableList<Wohnung> wohnungen = FXCollections.observableArrayList();

    private boolean istGeoeffnet;

    /**
     * Konstruktor für die erst Erstellung des Objektes.
     * @param bezeichnung Name der Immobilie
     * @param wohnungsanzahl Anzahl der Wohnungen in der Immobilie
     */
    public Immobilie(String bezeichnung, int wohnungsanzahl) {
        this.bezeichnung = new SimpleStringProperty(bezeichnung);
        this.wohnungsanzahl = new SimpleIntegerProperty(wohnungsanzahl);

        istGeoeffnet = false;

        erstelleWohnungen();
    }

    /**
     * Erstellt die im Konstruktor angegebene Anzahl an Wohnungen mit standardisiertem Namen.
     */
    private void erstelleWohnungen() {

        for (int i = 0; i < getWohnungsanzahl(); i++) {
            wohnungen.add(new Wohnung("Wohnung " + (i+1), 1, 0));
        }

        /*
        for (Wohnung w : wohnungen) {
            w.bezeichnungProperty().addListener((observableValue, oldString, newString) -> {
                for (Wohnung wl : wohnungen) {
                    if(wl.toString().equals(newString) && w != wl) {
                        w.setBezeichnung(oldString);
                        Alert info = new Alert(Alert.AlertType.INFORMATION, "Wohnungen müssen eineindeutige Namen besitzen.");
                        info.setHeaderText("Wohnungsumbenennung fehlgeschlagen!");
                        info.show();
                    }
                }
            });
        }
         */

    }

    @Override
    public ImmobilienReiter oeffneReiter() {
        ImmobilienReiter ir = new ImmobilienReiter(this);
        ir.setOnClosed(event -> istGeoeffnet = false);
        istGeoeffnet = true;
        return ir;
    }

    @Override
    public VBox oeffnePdfAnsicht() {
        return null;
    }

    @Override
    public boolean istGeoeffnet() {
        return istGeoeffnet;
    }

    @Override
    public boolean brauchtPdfAnsicht() {
        return false;
    }

    // Setter und Getter

    @Override
    public String toString() {
        return bezeichnung.get();
    }

    public StringProperty bezeichnungProperty() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung.set(bezeichnung);
    }

    public int getWohnungsanzahl() {
        return wohnungsanzahl.get();
    }

    public IntegerProperty wohnungsanzahlProperty() {
        return wohnungsanzahl;
    }

    public void setWohnungsanzahl(int wohnungsanzahl) {
        this.wohnungsanzahl.set(wohnungsanzahl);
    }

    public ObservableList<Wohnung> getWohnungen() {
        return wohnungen;
    }
}
