package com.mietmanager.gbo;

/**
 * Controller Interface.
 *
 * @since 1.0.1
 * @see FXMLHelper
 * @author John Robin Pfeifer
 */
public interface Controller {

    /**
     * Vorlage für eine Übergabefunktion. Die Daten werden von dem {@link FXMLHelper} an den Controller
     * weitergegeben.
     *
     * @param daten die Daten, welche übergeben werden sollen
     */
    void uebergeben(Object... daten);
}
