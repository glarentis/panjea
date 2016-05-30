package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "maga_mezzi_trasporto", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda",
        "targa" }) )
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiCommerciali")
public class MezzoTrasporto extends EntityBase {

    private static final long serialVersionUID = -2042668103306857876L;

    @Column(length = 10, nullable = false)
    private String targa;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda;

    @Column(length = 30, nullable = false)
    private String descrizione;

    private boolean abilitato;

    @ManyToOne
    @JoinColumn(name = "tipoMezzoTrasporto_id")
    private TipoMezzoTrasporto tipoMezzoTrasporto;

    @ManyToOne(optional = true)
    private EntitaLite entita;

    @ManyToOne(fetch = FetchType.EAGER)
    private DepositoLite deposito;

    /**
     * Costruttore.
     */
    public MezzoTrasporto() {
        super();
        abilitato = true;
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the deposito
     */
    public DepositoLite getDeposito() {
        return deposito;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return Returns the entita.
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return the targa
     */
    public String getTarga() {
        return targa;
    }

    /**
     * @return the tipoMezzoTrasporto
     */
    public TipoMezzoTrasporto getTipoMezzoTrasporto() {
        return tipoMezzoTrasporto;
    }

    /**
     * @return Returns the abilitato.
     */
    public boolean isAbilitato() {
        return abilitato;
    }

    /**
     * @param abilitato
     *            The abilitato to set.
     */
    public void setAbilitato(boolean abilitato) {
        this.abilitato = abilitato;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param deposito
     *            the deposito to set
     */
    public void setDeposito(DepositoLite deposito) {
        this.deposito = deposito;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param entita
     *            The entita to set.
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param targa
     *            the targa to set
     */
    public void setTarga(String targa) {
        this.targa = targa;
    }

    /**
     * @param tipoMezzoTrasporto
     *            the tipoMezzoTrasporto to set
     */
    public void setTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto) {
        this.tipoMezzoTrasporto = tipoMezzoTrasporto;
    }

}
