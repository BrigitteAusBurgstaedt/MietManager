package jrp.mietmanager.logik;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import jrp.mietmanager.benutzeroberflaeche.hauptfenster.PdfAnsicht;
import jrp.mietmanager.benutzeroberflaeche.hauptfenster.WohnungsReiter;

import java.time.LocalDate;
import java.util.logging.Logger;

public class Wohnung implements Visualisierbar {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final Immobilie immobilie;

    private final StringProperty bezeichnung;
    private final IntegerProperty mieteranzahl;
    private final DoubleProperty flaeche;
    private final ObservableList<Zaehlerstand> zaehlerstaende = FXCollections.observableArrayList();

    private PdfAnsicht pdfAnsicht;
    private boolean istGeoffnet;

    /**
     * Konstruktor der Wohnung.
     *
     * @param bezeichnung Die Bezeichnung der Wohnung.
     * @param mieteranzahl Die Anzahl der Mieter.
     * @param flaeche Die Fläche der Wohnung in Quadratmeter.
     */
    protected Wohnung(Immobilie immobilie, String bezeichnung, int mieteranzahl, double flaeche) {
        this.immobilie = immobilie;                 // Festlegen der dazugehörigen Immobilie
        this.immobilie.getWohnungen().add(this);    // Eintragen in die Wohnungsliste der Immobilie

        this.bezeichnung = new SimpleStringProperty(bezeichnung);
        this.mieteranzahl = new SimpleIntegerProperty(mieteranzahl);
        this.flaeche = new SimpleDoubleProperty(flaeche);

        this.istGeoffnet = false;
    }

    public Zaehlerstand hinzufuegen(LocalDate datum, double wert, Zaehlermodus zaehlermodus) throws InstantiationException {
        return new Zaehlerstand(this, datum, wert, zaehlermodus);
    }

    @Override
    public WohnungsReiter oeffneReiter() {
        WohnungsReiter wr = new WohnungsReiter(this);
        wr.setOnClosed(event -> istGeoffnet = false);
        istGeoffnet = true;
        return wr;
    }

    @Override
    public VBox oeffnePdfAnsicht(Immobilie immobilie) {
        if (pdfAnsicht == null)
            pdfAnsicht = new PdfAnsicht(immobilie, this);
        return pdfAnsicht;
    }

    @Override
    public boolean istGeoeffnet() {
        return istGeoffnet;
    }

    /**
     * Wahrheitswert welcher widerspiegelt, ob ein PDF gezeigt werden soll.
     *
     * @return Gibt wahr zurück, wenn eine PDF-Ansicht benötigt wird.
     */
    @Override
    public boolean brauchtPdfAnsicht() {
        return true;
    }

    // Getter und Setter

    public Immobilie getImmobilie() {
        return immobilie;
    }

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

    public int getMieteranzahl() {
        return mieteranzahl.get();
    }

    public IntegerProperty mieteranzahlProperty() {
        return mieteranzahl;
    }

    public void setMieteranzahl(int mieteranzahl) {
        this.mieteranzahl.set(mieteranzahl);
    }

    public double getFlaeche() {
        return flaeche.get();
    }

    public DoubleProperty flaecheProperty() {
        return flaeche;
    }

    public void setFlaeche(double flaeche) {
        this.flaeche.set(flaeche);
    }

    public ObservableList<Zaehlerstand> getZaehlerstaende() {
        return zaehlerstaende;
    }

}
