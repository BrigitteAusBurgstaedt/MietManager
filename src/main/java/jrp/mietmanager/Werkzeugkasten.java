package jrp.mietmanager;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Sammlung von nicht-kohäsiven/statischen Methoden.
 *
 * @since 1.0
 * @author John Robin Pfeifer
 */
public final class Werkzeugkasten {

    private Werkzeugkasten() {}

    /**
     * Erstellt einen Snap-Schuss von einem grafischen JavaFX Objekt und konvertiert es zu einem {@link Image}.
     *
     * @param elternteil das grafische Objekt welches konvertiert werden soll.
     * @return das iText Image Objekt des angegebenen Parameters.
     * @throws IOException wenn ein I/O error entsteht.
     */
    public static Image erstelleFoto(Parent elternteil) throws IOException {
        Scene szeneMitElternteil = new Scene(elternteil); // damit ein Stylesheet angewandt wird
        WritableImage fxBild = szeneMitElternteil.snapshot(null);
        BufferedImage awtBild = SwingFXUtils.fromFXImage(fxBild, null);
        return new Image(ImageDataFactory.create(awtBild, null));
    }
}
