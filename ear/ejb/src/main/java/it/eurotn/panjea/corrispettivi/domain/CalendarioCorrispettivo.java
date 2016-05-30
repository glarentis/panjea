package it.eurotn.panjea.corrispettivi.domain;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

import java.io.Serializable;
import java.util.List;

/**
 * @author fattazzo
 */
public class CalendarioCorrispettivo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7396959397122441309L;

    /**
     * @uml.property name="anno"
     */
    private int anno;

    /**
     * @uml.property name="mese"
     */
    private int mese;

    /**
     * @uml.property name="tipoDocumento"
     * @uml.associationEnd
     */
    private TipoDocumento tipoDocumento;

    /**
     * @uml.property name="giorniCorrispettivo"
     */
    private List<GiornoCorrispettivo> giorniCorrispettivo;

    /**
     * @return anno
     * @uml.property name="anno"
     */
    public int getAnno() {
        return anno;
    }

    /**
     * @return giorniCorrispettivo
     * @uml.property name="giorniCorrispettivo"
     */
    public List<GiornoCorrispettivo> getGiorniCorrispettivo() {
        return giorniCorrispettivo;
    }

    /**
     * @return mese
     * @uml.property name="mese"
     */
    public int getMese() {
        return mese;
    }

    /**
     * @return tipoDocumento
     * @uml.property name="tipoDocumento"
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param anno
     *            the anno to set
     * @uml.property name="anno"
     */
    public void setAnno(int anno) {
        this.anno = anno;
    }

    /**
     * @param giorniCorrispettivo
     *            the giorniCorrispettivo to set
     * @uml.property name="giorniCorrispettivo"
     */
    public void setGiorniCorrispettivo(List<GiornoCorrispettivo> giorniCorrispettivo) {
        this.giorniCorrispettivo = giorniCorrispettivo;
    }

    /**
     * @param mese
     *            the mese to set
     * @uml.property name="mese"
     */
    public void setMese(int mese) {
        this.mese = mese;
    }

    /**
     * @param tipoDocumento
     *            the tipoDocumento to set
     * @uml.property name="tipoDocumento"
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
