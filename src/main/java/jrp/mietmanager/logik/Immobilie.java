package jrp.mietmanager.logik;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import jrp.mietmanager.benutzeroberflaeche.hauptfenster.ImmobilienReiter;

import java.util.logging.Logger;

public class Immobilie implements Visualisierbar {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final StringProperty bezeichnung;
    private final ObservableList<Wohnung> wohnungen;

    private boolean istGeoeffnet;

    public Immobilie(String bezeichnung) {
        this.bezeichnung = new SimpleStringProperty(bezeichnung);
        this.wohnungen = FXCollections.observableArrayList();

        istGeoeffnet = false;
    }

    /**
     * Konstruktor für die erst Erstellung des Objektes.
     * @param bezeichnung Name der Immobilie
     * @param wohnungsanzahl Anzahl der Wohnungen in der Immobilie
     */
    public Immobilie(String bezeichnung, int wohnungsanzahl) {
        this(bezeichnung);

        erstelleWohnungen(wohnungsanzahl);
    }

    /**
     * Erstellt die im Konstruktor angegebene Anzahl an Wohnungen mit standardisierten Werten.
     */
    private void erstelleWohnungen(int wohnungsanzahl) {
        for (int i = 0; i < wohnungsanzahl; i++) {
            new Wohnung(this, "Wohnung " + (i+1), 1, 0);
        }
    }

    public Wohnung hinzufuegen(String bezeichnung, int mieteranzahl, double flaeche) {
        return new Wohnung(this, bezeichnung, mieteranzahl, flaeche);
    }

    @Override
    public ImmobilienReiter oeffneReiter() {
        ImmobilienReiter ir = new ImmobilienReiter(this);
        ir.setOnClosed(event -> istGeoeffnet = false);
        istGeoeffnet = true;
        return ir;
    }

    @Override
    public VBox oeffnePdfAnsicht(Immobilie immobilie) {
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

    // Getter und Setter

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

    public ObservableList<Wohnung> getWohnungen() {
        return wohnungen;
    }

}
