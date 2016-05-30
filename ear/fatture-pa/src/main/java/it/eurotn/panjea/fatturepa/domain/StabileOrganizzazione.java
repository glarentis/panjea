package it.eurotn.panjea.fatturepa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;

/**
 * @author fattazzo
 *
 */
@Embeddable
public class StabileOrganizzazione implements Serializable {

    private static final long serialVersionUID = -8502838508596225519L;

    private DatiGeografici datiGeografici;

    @Column(length = 60)
    private String indirizzo;

    @Column(length = 8)
    private String numeroCivico;

    {
        datiGeografici = new DatiGeografici();
    }

    /**
     * @return the datiGeografici
     */
    public DatiGeografici getDatiGeografici() {
        return datiGeografici;
    }

    /**
     * @return the indirizzo
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * @return the numeroCivico
     */
    public String getNumeroCivico() {
        return numeroCivico;
    }

    /**
     * @param datiGeografici
     *            the datiGeografici to set
     */
    public void setDatiGeografici(DatiGeografici datiGeografici) {
        this.datiGeografici = datiGeografici;
    }

    /**
     * @param indirizzo
     *            the indirizzo to set
     */
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    /**
     * @param numeroCivico
     *            the numeroCivico to set
     */
    public void setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

}
