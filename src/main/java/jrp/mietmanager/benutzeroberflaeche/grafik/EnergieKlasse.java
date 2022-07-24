package jrp.mietmanager.benutzeroberflaeche.grafik;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;

enum EnergieKlasse {

    A1A     ("A+", "#82c042", 5.1, 4.5, 3.9, 2.4, 1.2, 0.9, 2.4, 3.6, 4.8),
    AB      ("A", "#a4cc41", 8.5, 7.5, 6.5, 4.0, 2.0, 1.5, 4.0, 6.0, 8.0),
    BC      ("B", "#d6df3d", 12.8, 11.3, 9.8, 6.0, 3.0, 2.3, 6.0, 9.0, 12.0),
    CD      ("C", "#f5ee39", 17.0, 15.0, 13.0, 8.0, 4.0, 3.0, 8.0, 12.0, 16.0),
    DE      ("D", "#fbd829", 22.1, 19.5, 16.9, 10.4, 5.2, 3.9, 10.4, 15.6, 20.8),
    EF      ("E", "#fecc26", 27.2, 24, 20.8, 12.8, 6.4, 4.8, 12.8, 19.2, 25.6),
    FG      ("F", "#f69826", 34.0, 30.0, 26, 16, 8.0, 6.0, 16.0, 24.0, 32.0),
    GH      ("G", "#f16b25", 42.5, 37.5, 32.5, 20.0, 10.0, 7.5, 20.0, 30.0, 40.0),
    H100    ("H", "#e21f2c", GH.jan - FG.jan + GH.jan, GH.feb - FG.feb + GH.feb, GH.mar - FG.mar + GH.mar, GH.apr - FG.apr + GH.apr, GH.maiBisAug - FG.maiBisAug + GH.maiBisAug, GH.sep - FG.sep + GH.sep, GH.okt - FG.okt + GH.okt, GH.nov - FG.nov + GH.nov, GH.dez - FG.dez + GH.dez),
    OA1     ("", "#23a24e", A1A.jan - AB.jan + A1A.jan, A1A.feb - AB.feb + A1A.feb, A1A.mar - AB.mar + A1A.mar, A1A.apr - AB.apr + A1A.apr, A1A.maiBisAug - AB.maiBisAug + A1A.maiBisAug, A1A.sep - AB.sep + A1A.sep, A1A.okt - AB.okt + A1A.okt, A1A.nov - AB.nov + A1A.nov, A1A.dez - AB.dez + A1A.dez);


    private final double jan, feb, mar, apr, maiBisAug, sep, okt, nov, dez;
    private final String bez, farbe;

    EnergieKlasse(String bez, String farbe, double jan, double feb, double mar, double apr, double maiBisAug, double sep, double okt, double nov, double dez) {
        this.bez = bez;
        this.farbe = farbe;
        this.jan = jan;
        this.feb = feb;
        this.mar = mar;
        this.apr = apr;
        this.maiBisAug = maiBisAug;
        this.sep = sep;
        this.okt = okt;
        this.nov = nov;
        this.dez = dez;
    }

    double wert(int monat) {
        return switch (monat) {
            case 1 -> jan;
            case 2 -> feb;
            case 3 -> mar;
            case 4 -> apr;
            case 5, 6, 7, 8 -> maiBisAug;
            case 9 -> sep;
            case 10 -> okt;
            case 11 -> nov;
            case 12 -> dez;
            default -> -1.0;
        };
    }

    /**
     * Prozent auf der Skala
     * @param m Monat
     * @return Wert zwischen 0 und 1
     */
    double prozent(int m) {
        return  (this.wert(m) - OA1.wert(m)) / (H100.wert(m) - OA1.wert(m));
    }

    public static DoubleBinding prozent(DoubleProperty wert, int m) {
        return wert.subtract(OA1.wert(m)).divide(H100.wert(m) - OA1.wert(m));
    }

    String farbe() {
        return this.farbe;
    }

    String farbeMitProzent(int m) {
        return this.farbe + " " + (this.prozent(m) * 100) + "%";
    }

    String farbeMitProzent(double min, double max, int m) {
        return this.farbe + " " + (((this.wert(m) - min) / (max - min)) * 100) + "%";
    }

    @Override
    public String toString() {
        return bez;
    }
}
