package jrp.mietmanager.logik;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Zaehlerstand implements Comparable<Zaehlerstand> {

    private final ObjectProperty<LocalDate> datum;
    private final DoubleProperty differenz;
    private final DoubleProperty wert;
    private final Zaehlermodus modus;
    private final XYChart.Data<Number, Number> datenpunkt;

    public Zaehlerstand(LocalDate datum, double wert, Zaehlermodus modus) {
        this.datum = new SimpleObjectProperty<>(datum);
        this.wert = new SimpleDoubleProperty(wert);
        this.modus = modus;
        this.differenz = new SimpleDoubleProperty();

        StringProperty datumString = new SimpleStringProperty(datum.format(DateTimeFormatter.ISO_ORDINAL_DATE).replace("-", "."));

        Bindings.bindBidirectional(datumString, this.datum, new StringConverter<>() {
            @Override
            public String toString(LocalDate localDate) {
                return localDate.format(DateTimeFormatter.ISO_ORDINAL_DATE).replace("-",".");
            }

            @Override
            public LocalDate fromString(String s) {
                return s.equals("") ? datum : LocalDate.parse(s.replace(".","-"), DateTimeFormatter.ISO_ORDINAL_DATE);
            }
        });

        this.datenpunkt = new XYChart.Data<>();
        Bindings.bindBidirectional(datumString, datenpunkt.XValueProperty(), new NumberStringConverter());
        this.datenpunkt.YValueProperty().bind(differenzProperty());
    }

    public XYChart.Data<Number, Number> getDatenpunkt() {
        return datenpunkt;
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
        return this.getDatum().compareTo(o.getDatum());
    }
}
