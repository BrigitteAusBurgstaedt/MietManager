package jrp.mietmanager.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import java.io.FileNotFoundException;

public class MonatlicheMieterInformation {

    public MonatlicheMieterInformation() throws FileNotFoundException {
        String pfad = "F:\\Workspace\\IDEA-Projects\\MietManager\\test.pdf";
        PdfDocument pdf = new PdfDocument(new PdfWriter(pfad));
        pdf.addNewPage();
        Document dokument = new Document(pdf);

        dokument.close();

    }


}
