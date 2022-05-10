package jrp.mietmanager.berechnung;

public enum EnergieKlassen {
    A1A     (5.1, 4.5, 3.9, 2.4, 1.2, 0.9, 2.4, 3.6, 4.8),
    AB      (8.5, 7.5, 6.5, 4.0, 2.0, 1.5, 4.0, 6.0, 8.0),
    BC      (12.8, 11.3, 9.8, 6.0, 3.0, 2.3, 6.0, 9.0, 12.0),
    CD      (17.0, 15.0, 13.0, 8.0, 4.0, 3.0, 8.0, 12.0, 16.0),
    DE      (22.1, 19.5, 16.9, 10.4, 5.2, 3.9, 10.4, 15.6, 20.8),
    EF      (27.2, 24, 20.8, 12.8, 6.4, 4.8, 12.8, 19.2, 25.6),
    FG      (34.0, 30.0, 26, 16, 8.0, 6.0, 16.0, 24.0, 32.0),
    GH      (42.5, 37.5, 32.5, 20.0, 10.0, 7.5, 20.0, 30.0, 40.0);


    private final double jan, feb, mar, apr, maiBisAug, sep, okt, nov, dez;

    EnergieKlassen(double jan, double feb, double mar, double apr, double maiBisAug, double sep, double okt, double nov, double dez) {
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


}
