package com.mietmanager.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.mietmanager.Werkzeugkasten;
import com.mietmanager.gbo.grafik.Pfeildiagramm;
import com.mietmanager.logik.Immobilie;
import com.mietmanager.logik.Wohnung;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Klasse zur Erstellung der monatlichen Mieterinformationen als PDF.
 *
 * @author John Robin Pfeifer
 * @since 1.0.0
 */
public class MonatlicheMieterInformation {
    private final String verzeichnispfad;
    private final String datum;
    private final double[] minAvgMax;
    private final int monat;
    private final int jahr;

    /**
     * Erstellt für jede Wohnung in der Immobilie eine monatliche Mieterinformation.
     *
     * @param verzeichnispfad der Pfad zum Ordner wo die Dokumente gespeichert werden sollen
     * @param immobilie die Immobilie für die die Dokumente erstellt werden sollen
     * @param monat der Monat der Mieterinformation
     * @param jahr das Jahr der Mieterinformation
     * @throws IOException wenn es probleme mit dem PDF-Writer oder dem Foto gibt
     */
    public MonatlicheMieterInformation(String verzeichnispfad, Immobilie immobilie, int monat, int jahr) throws IOException {
        this.verzeichnispfad = verzeichnispfad;
        this.datum = LocalDate.now().toString();
        this.minAvgMax = immobilie.bestimmeMinAvgMax(monat, jahr);
        this.monat = monat;
        this.jahr = jahr;

        for (Wohnung w : immobilie.getWohnungen()) {
            dokumentErstellen(w);
        }

    }

    /**
     * Erstellt für die angegebene Wohnung eine monatliche Mieterinformation
     *
     * @param wohnung die gewünschte Wohnung
     * @throws IOException wenn es probleme mit dem PDF-Writer oder dem Foto gibt
     */
    private void dokumentErstellen(Wohnung wohnung) throws IOException {
        File datei = new File(verzeichnispfad, wohnung.toString() + "_" + monat + "_" + jahr + ".pdf");
        PdfDocument pdf = new PdfDocument(new PdfWriter(datei));
        pdf.addNewPage();
        try (Document dokument = new Document(pdf)) {
            // dokument.setFontFamily(StandardFonts.HELVETICA); font provider

            String keineDaten = "Keine Daten vorhanden";
            String paragrafText =
                    "Datum: " + datum + "\n" +
                            "Wohneinheit: " + wohnung + "\n" +
                            "Verbrauch (aktuell): " + (wohnung.getZaehlerstand(monat, jahr) == null ? keineDaten : wohnung.getZaehlerstand(monat, jahr).getDifferenz()) + "\n" +
                            "Verbrauch (vormonat): " + (wohnung.getZaehlerstand(monat - 1, jahr) == null ? keineDaten : wohnung.getZaehlerstand(monat - 1, jahr).getDifferenz()) + "\n" +
                            "Verbrauch (vorjahr): " + (wohnung.getZaehlerstand(monat, jahr - 1) == null ? keineDaten : wohnung.getZaehlerstand(monat, jahr - 1).getDifferenz()) + "\n" +
                            "Minimum: " + (minAvgMax.length != 3 ? keineDaten : minAvgMax[0]) + "\n" +
                            "Durchschnitt: " + (minAvgMax.length != 3 ? keineDaten : minAvgMax[2]) + "\n" +
                            "Maximum: " + (minAvgMax.length != 3 ? keineDaten : minAvgMax[1]);


            Paragraph ueberschrift = new Paragraph("Verbrauchsinformation");
            // ueberschrift.setFontFamily(StandardFonts.HELVETICA_BOLD); font provider
            ueberschrift.setFontSize(28f);
            ueberschrift.setHorizontalAlignment(HorizontalAlignment.CENTER);

            Paragraph paragraf = new Paragraph(paragrafText);
            Image pfeildiagrammFoto = null;

            if (minAvgMax.length == 3 && wohnung.getZaehlerstand(monat, jahr) != null) {
                Pfeildiagramm pfeildiagramm = new Pfeildiagramm(minAvgMax[0], minAvgMax[2], wohnung.getZaehlerstand(monat, jahr).getDifferenz(), monat);
                pfeildiagrammFoto = Werkzeugkasten.erstelleFoto(pfeildiagramm);
            }

            dokument.add(ueberschrift);
            dokument.add(paragraf);
            if (pfeildiagrammFoto != null) dokument.add(pfeildiagrammFoto);

        }
    }


}
