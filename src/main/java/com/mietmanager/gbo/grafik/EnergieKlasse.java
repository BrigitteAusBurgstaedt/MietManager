package com.mietmanager.gbo.grafik;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;

/**
 * Datenblatt für die Energieklassen.
 */
enum EnergieKlasse {

    A1A     ("A+", "#82c042", new double[]{5.1, 4.5, 3.9, 2.4, 1.2, 0.9, 2.4, 3.6, 4.8}),
    AB      ("A", "#a4cc41", new double[]{8.5, 7.5, 6.5, 4.0, 2.0, 1.5, 4.0, 6.0, 8.0}),
    BC      ("B", "#d6df3d", new double[]{12.8, 11.3, 9.8, 6.0, 3.0, 2.3, 6.0, 9.0, 12.0}),
    CD      ("C", "#f5ee39", new double[]{17.0, 15.0, 13.0, 8.0, 4.0, 3.0, 8.0, 12.0, 16.0}),
    DE      ("D", "#fbd829", new double[]{22.1, 19.5, 16.9, 10.4, 5.2, 3.9, 10.4, 15.6, 20.8}),
    EF      ("E", "#fecc26", new double[]{27.2, 24, 20.8, 12.8, 6.4, 4.8, 12.8, 19.2, 25.6}),
    FG      ("F", "#f69826", new double[]{34.0, 30.0, 26, 16, 8.0, 6.0, 16.0, 24.0, 32.0}),
    GH      ("G", "#f16b25", new double[]{42.5, 37.5, 32.5, 20.0, 10.0, 7.5, 20.0, 30.0, 40.0}),
    H100    ("H", "#e21f2c", new double[]{GH.wert(1) - FG.wert(1) + GH.wert(1), GH.wert(2) - FG.wert(2) + GH.wert(2), GH.wert(3) - FG.wert(3) + GH.wert(3), GH.wert(4) - FG.wert(4) + GH.wert(4), GH.wert(5) - FG.wert(5) + GH.wert(5), GH.wert(9) - FG.wert(9) + GH.wert(9), GH.wert(10) - FG.wert(10) + GH.wert(10), GH.wert(11) - FG.wert(11) + GH.wert(11), GH.wert(12) - FG.wert(12) + GH.wert(12)}),
    OA1     ("", "#23a24e", new double[]{A1A.wert(1) - AB.wert(1) + A1A.wert(1), A1A.wert(2) - AB.wert(2) + A1A.wert(2), A1A.wert(3) - AB.wert(3) + A1A.wert(3), A1A.wert(4) - AB.wert(4) + A1A.wert(4), A1A.wert(5) - AB.wert(5) + A1A.wert(5), A1A.wert(9) - AB.wert(9) + A1A.wert(9), A1A.wert(10) - AB.wert(10) + A1A.wert(10), A1A.wert(11) - AB.wert(11) + A1A.wert(11), A1A.wert(12) - AB.wert(12) + A1A.wert(12)});


    private final double[] grenzwerte;
    private final String bez;
    private final String farbe;

    EnergieKlasse(String bez, String farbe, double[] grenzwerte) {
        this.bez = bez;
        this.farbe = farbe;
        this.grenzwerte = grenzwerte;
    }

    double wert(int monat) {
        return switch (monat) {
            case 1 -> grenzwerte[0];
            case 2 -> grenzwerte[1];
            case 3 -> grenzwerte[2];
            case 4 -> grenzwerte[3];
            case 5, 6, 7, 8 -> grenzwerte[4];
            case 9 -> grenzwerte[5];
            case 10 -> grenzwerte[6];
            case 11 -> grenzwerte[7];
            case 12 -> grenzwerte[8];
            default -> -1.0;
        };
    }

    /**
     * Prozent auf der Skala
     *
     * @param m der Monat, für den der Prozentwert berechnet werden soll
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

    /**
     * Berechnet für den Farbverlauf einen String mit der Farbe und der Position der farbe in Prozent.
     *
     * @param m der Monat, für den der Prozentwert berechnet werden soll
     * @return String mit Farbe und Prozent
     */
    String farbeMitProzent(int m) {
        return this.farbe + " " + (this.prozent(m) * 100) + "%";
    }

    /**
     * Berechnet für den Farbverlauf einen String mit der Farbe und der Position der farbe in Prozent. Es kann
     * zusätzlich ein Skalenausschnitt von min bis max angegeben werden. Dies ist für den kleineren Pfeil über dem
     * Bandtacho wichtig.
     *
     * @param min das Minimum des Skalenausschnittes
     * @param max das Maximum des Skalenausschnittes
     * @param m der Monat, für den der Prozentwert berechnet werden soll
     * @return String mit Farbe und Prozent
     */
    String farbeMitProzent(double min, double max, int m) {
        return this.farbe + " " + (((this.wert(m) - min) / (max - min)) * 100) + "%";
    }

    @Override
    public String toString() {
        return bez;
    }
}
