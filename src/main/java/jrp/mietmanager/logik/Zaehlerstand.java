package jrp.mietmanager.logik;

import javafx.beans.property.*;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Zaehlerstand implements Comparable<Zaehlerstand> {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private final Wohnung wohnung;
    private Zaehlerstand vorgaenger;

    private final ObjectProperty<LocalDate> datum;
    private final DoubleProperty datumAlsDouble;
    private final DoubleProperty differenz;
    private final DoubleProperty wert;
    private final Zaehlermodus modus;

    protected Zaehlerstand(Wohnung wohnung, LocalDate datum, double wert, Zaehlermodus modus) throws InstantiationException {
        // Datumsumwandlung für Diagrammdarstellung
        this.datum = new SimpleObjectProperty<>(datum);
        double tageImJahr = datum.isLeapYear() ? 366 : 365;
        this.datumAlsDouble = new SimpleDoubleProperty(datum.getYear() + (datum.getDayOfYear() / tageImJahr));
        this.datum.addListener((beobachtbarerWert, altesDatum, neuesDatum) -> {
            double tageImJahr1 = neuesDatum.isLeapYear() ? 366 : 365;
            this.datumAlsDouble.set(neuesDatum.getYear() + (neuesDatum.getDayOfYear() / tageImJahr1));

            try {
                bestimmeVorhergehendenZaehlerstand();
            } catch (InstantiationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Falsch Eingabe!");
                alert.setContentText("Der Zählerwert darf nicht niedriger sein als der Vorhergehende.");
                alert.show();
                log.log(Level.WARNING, "Falsche Eingabe beim verändern des Datums", e);
            }
        });

        this.wert = new SimpleDoubleProperty(wert);
        this.modus = modus;
        this.differenz = new SimpleDoubleProperty();

        this.wohnung = wohnung;                                 // Festlegen der dazugehörigen Wohnung
        this.wohnung.getZaehlerstaende().add(this);             // Eintragen in die Zählerstandliste der Wohnung

        bestimmeVorhergehendenZaehlerstand();
    }

    /*
    Bestimmung des richtigen Vorgängers ist wichtig für die Differenz bestimmung
     */

    private void bestimmeVorhergehendenZaehlerstand() throws InstantiationException {
        SortedList<Zaehlerstand> sz = wohnung.getZaehlerstaende().sorted();         // Es wird eine Sortierte Liste erstellt
        int index = sz.indexOf(this);                                               // Es wird der Index des übergebenen Zählerstandes bestimmt
        Zaehlerstand vorgaengerNeu = (index-1 < 0) ? null : sz.get(index-1);
        if (vorgaenger != null && vorgaenger == vorgaengerNeu) return;              // Zählerstand hatte schon einen Vorgänger doch dieser bleibt gleich
        if (vorgaenger != null) {                                                   // Zählerstand hatte schon einen Vorgänger und dieser wird sich ändern
            int indexVorgaenger = sz.indexOf(vorgaenger);
            if (indexVorgaenger+1 < sz.size()) sz.get(indexVorgaenger+1).setVorgaenger(vorgaenger); // Wenn es einen Nachfolger zum alten Vorgänger gibt, dann wird dessen Vorgänger aktualisiert.
        }
        setVorgaenger(vorgaengerNeu);                                               // Der Vorgänger wird zugewiesen. Falls es keinen gibt, wird null zugewiesen.
        if (index+1 < sz.size()) sz.get(index+1).setVorgaenger(this);               // Falls der Zählerstand einen Nachfolger hat, wird dessen Vorgänger aktualisiert
    }

    private void setVorgaenger(Zaehlerstand vorgaenger) throws InstantiationException {
        this.vorgaenger = vorgaenger;
        if ((this.vorgaenger != null) && (this.vorgaenger.wert.get() > wert.get()))
            throw new InstantiationException("Der Vorhergehende Zählerstand kann nicht größer sein"); // Wichtige Exception die auf falsche Nutzereingabe zurückgeführt werden kann
        bestimmeDifferenz();
    }

    private void bestimmeDifferenz() {
        if (vorgaenger == null) {
            differenz.set(0);       // Die erste Differenz soll Null sein
        } else {
            if (differenz.isBound()) differenz.unbind();
            differenz.bind(wert.subtract(vorgaenger.wert));
        }
    }



    public boolean istMonat(int monat, int jahr) {
        return (datum.getValue().getMonthValue() == monat) && (datum.getValue().getYear() == jahr);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(Zaehlerstand o) {
        return getDatum().compareTo(o.getDatum());
        // Genauer aber schwieriger bei Veränderungen: return (getDatum().compareTo(o.getDatum()) != 0) ? getDatum().compareTo(o.getDatum()) : ((int) getWert() - (int) o.getWert());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return getDatum().equals(((Zaehlerstand) o).getDatum());
        // Genauer aber schwieriger bei Veränderungen: return (getDatum().equals(((Zaehlerstand) o).getDatum()) && ((int) getWert() == ((int) ((Zaehlerstand) o).getWert())));
    }

    // gewöhnliche Getter und Setter


    public Wohnung getWohnung() {
        return wohnung;
    }

    public LocalDate getDatum() {
        return datum.get();
    }

    public ObjectProperty<LocalDate> datumProperty() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum.set(datum);
    }

    public double getWert() {
        return wert.get();
    }

    public DoubleProperty wertProperty() {
        return wert;
    }

    public void setWert(double wert) {
        this.wert.set(wert);
    }

    public Zaehlermodus getModus() {
        return modus;
    }

    public double getDifferenz() {
        return differenz.get();
    }

    public DoubleProperty differenzProperty() {
        return differenz;
    }

    public void setDifferenz(double differenz) {
        this.differenz.set(differenz);
    }

    public double getDatumAlsDouble() {
        return datumAlsDouble.get();
    }

    public DoubleProperty datumAlsDoubleProperty() {
        return datumAlsDouble;
    }
}
