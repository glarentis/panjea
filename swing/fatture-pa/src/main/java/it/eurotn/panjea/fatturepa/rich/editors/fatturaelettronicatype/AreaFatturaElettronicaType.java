package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype;

import java.io.Serializable;

import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

public class AreaFatturaElettronicaType implements Serializable {

    private static final long serialVersionUID = 9208431085717469753L;

    private AreaMagazzinoFatturaPA areaMagazzinoFatturaPA;

    private IFatturaElettronicaType fatturaElettronicaType;

    /**
     * Costruttore.
     *
     * @param areaMagazzinoFatturaPA
     *            area magazzino pa
     * @param fatturaElettronicaType
     *            fattura elettronica type
     */
    public AreaFatturaElettronicaType(final AreaMagazzinoFatturaPA areaMagazzinoFatturaPA,
            final IFatturaElettronicaType fatturaElettronicaType) {
        super();
        this.areaMagazzinoFatturaPA = areaMagazzinoFatturaPA;
        this.fatturaElettronicaType = fatturaElettronicaType;
    }

    /**
     * @return the areaMagazzinoFatturaPA
     */
    public AreaMagazzinoFatturaPA getAreaMagazzinoFatturaPA() {
        return areaMagazzinoFatturaPA;
    }

    /**
     * @return the fatturaElettronicaType
     */
    public IFatturaElettronicaType getFatturaElettronicaType() {
        return fatturaElettronicaType;
    }

    /**
     * @param areaMagazzinoFatturaPA
     *            the areaMagazzinoFatturaPA to set
     */
    public void setAreaMagazzinoFatturaPA(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA) {
        this.areaMagazzinoFatturaPA = areaMagazzinoFatturaPA;
    }

    /**
     * @param fatturaElettronicaType
     *            the fatturaElettronicaType to set
     */
    public void setFatturaElettronicaType(IFatturaElettronicaType fatturaElettronicaType) {
        this.fatturaElettronicaType = fatturaElettronicaType;
    }

}
