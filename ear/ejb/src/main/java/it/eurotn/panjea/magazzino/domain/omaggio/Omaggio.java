package it.eurotn.panjea.magazzino.domain.omaggio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;

/**
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "maga_omaggi")
@NamedQueries({
        @NamedQuery(name = "Omaggio.caricaByTipoOmaggio", query = " select o from Omaggio o where o.codiceAzienda=:paramCodiceAzienda and o.tipoOmaggio=:paramTipoOmaggio ") })
public class Omaggio extends EntityBase {

    private static final long serialVersionUID = 6655483787512594275L;

    @Enumerated
    private TipoOmaggio tipoOmaggio;

    @ManyToOne(fetch = FetchType.LAZY)
    private CodiceIva codiceIva;

    @Column(length = 1000)
    private String descrizionePerStampa;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    /**
     * Costruttore.
     */
    public Omaggio() {
        super();
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the codiceIva
     */
    public CodiceIva getCodiceIva() {
        return codiceIva;
    }

    /**
     * @return the descrizionePerStampa
     */
    public String getDescrizionePerStampa() {
        return descrizionePerStampa;
    }

    /**
     * @return the tipoOmaggio
     */
    public TipoOmaggio getTipoOmaggio() {
        return tipoOmaggio;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param codiceIva
     *            the codiceIva to set
     */
    public void setCodiceIva(CodiceIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    /**
     * @param descrizionePerStampa
     *            the descrizionePerStampa to set
     */
    public void setDescrizionePerStampa(String descrizionePerStampa) {
        this.descrizionePerStampa = descrizionePerStampa;
    }

    /**
     * @param tipoOmaggio
     *            the tipoOmaggio to set
     */
    public void setTipoOmaggio(TipoOmaggio tipoOmaggio) {
        this.tipoOmaggio = tipoOmaggio;
    }

}
