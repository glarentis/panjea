package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;

/**
 * Contiene una lista di RisultatiPrezzo che hanno generato l'importo per la <code>RigaArticolo.</code>
 *
 * @author fattazzo
 */
@Embeddable
public class DescrizionePoliticaPrezzo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @uml.property name="descrizioneCalcolo"
     */
    @Column(length = 1000)
    private String descrizioneCalcolo;

    /**
     * @return the descrizioneCalcolo
     * @uml.property name="descrizioneCalcolo"
     */
    public String getDescrizioneCalcolo() {
        return descrizioneCalcolo;
    }

    /**
     * 
     * @return lista di risultatoPrezzo per la riga Articolo
     */
    @SuppressWarnings("rawtypes")
    public List<RisultatoPrezzo> getValoriModuliPrezzo() {
        return new ArrayList<RisultatoPrezzo>();
    }

    /**
     * @param descrizioneCalcolo
     *            the descrizioneCalcolo to set
     * @uml.property name="descrizioneCalcolo"
     */
    public void setDescrizioneCalcolo(String descrizioneCalcolo) {
        this.descrizioneCalcolo = descrizioneCalcolo;
    }

}
