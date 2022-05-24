package jrp.mietmanager.speicherung;

import jrp.mietmanager.logik.Immobilie;
import jrp.mietmanager.logik.Wohnung;
import jrp.mietmanager.logik.Zaehlermodus;
import jrp.mietmanager.logik.Zaehlerstand;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ImmobilieMomentaufnahme implements Serializable {

    private final String bezeichnung;
    private final int wohnungsanzahl;
    private final WohnungMomentaufnahme[] wohnungen;

    public ImmobilieMomentaufnahme(Immobilie immobilie) {
        bezeichnung = immobilie.toString();
        wohnungsanzahl = immobilie.getWohnungsanzahl();

        List<Wohnung> hilfWohnungen = immobilie.getWohnungen();
        wohnungen = new WohnungMomentaufnahme[hilfWohnungen.size()];

        for (int i = 0; i < hilfWohnungen.size(); i++) {
            wohnungen[i] = new WohnungMomentaufnahme(hilfWohnungen.get(i));
        }

    }

    public Immobilie getImmobilie() {
        Immobilie immobilie = new Immobilie(bezeichnung, wohnungsanzahl);

        for (WohnungMomentaufnahme wohnungMomentaufnahmeMomentaufnahme : wohnungen) {
            immobilie.getWohnungen().add(wohnungMomentaufnahmeMomentaufnahme.getWohnung());
        }

        return immobilie;
    }

}

class WohnungMomentaufnahme implements Serializable {

    private final String bezeichnung;
    private final int mieteranzahl;
    private final double flaeche;
    private final ZaehlerstandMomentaufnahme[] zaehlerstaende;

    public WohnungMomentaufnahme(Wohnung wohnung) {
        bezeichnung = wohnung.toString();
        mieteranzahl = wohnung.getMieteranzahl();
        flaeche = wohnung.getFlaeche();

        List<Zaehlerstand> hilfZaehlerstaende = wohnung.getZaehlerstaende();
        zaehlerstaende = new ZaehlerstandMomentaufnahme[hilfZaehlerstaende.size()];

        for (int i = 0; i < hilfZaehlerstaende.size(); i++) {
            zaehlerstaende[i] = new ZaehlerstandMomentaufnahme(hilfZaehlerstaende.get(i));
        }

    }

    public Wohnung getWohnung() {
        Wohnung wohnung = new Wohnung(bezeichnung, mieteranzahl, flaeche);

        for (ZaehlerstandMomentaufnahme zaehlerstandMomentaufnahme : zaehlerstaende) {
            wohnung.hinzufuegen(zaehlerstandMomentaufnahme.getZaehlerstand());
        }

        return wohnung;
    }
}

class ZaehlerstandMomentaufnahme implements Serializable {

    private final LocalDate datum;
    private final double differenz;
    private final double wert;
    private final Zaehlermodus modus;

    public ZaehlerstandMomentaufnahme(Zaehlerstand zaehlerstand) {
        datum = zaehlerstand.getDatum();
        differenz = zaehlerstand.getDifferenz();
        wert = zaehlerstand.getWert();
        modus = zaehlerstand.getModus();
    }

    public Zaehlerstand getZaehlerstand() {
        Zaehlerstand zaehlerstand = new Zaehlerstand(datum, wert, modus);
        zaehlerstand.setDifferenz(differenz);
        return zaehlerstand;
    }

}
