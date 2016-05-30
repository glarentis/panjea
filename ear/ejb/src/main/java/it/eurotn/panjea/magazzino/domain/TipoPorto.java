package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.intra.domain.GruppoCondizioneConsegna;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "maga_tipi_porto")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiCommerciali")
@NamedQuery(name = "TipoPorto.caricaByDescrizione", query = "select tp from TipoPorto tp where tp.codiceAzienda=:paramCodiceAzienda and tp.descrizione like :descrizione order by descrizione")
public class TipoPorto extends EntityBase {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, unique = true, length = 45)
    private String descrizione;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    @Enumerated
    private GruppoCondizioneConsegna gruppoCondizioneConsegna;

    /**
     * @return codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the gruppoCondizioneConsegna
     */
    public GruppoCondizioneConsegna getGruppoCondizioneConsegna() {
        return gruppoCondizioneConsegna;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param gruppoCondizioneConsegna
     *            the gruppoCondizioneConsegna to set
     */
    public void setGruppoCondizioneConsegna(GruppoCondizioneConsegna gruppoCondizioneConsegna) {
        this.gruppoCondizioneConsegna = gruppoCondizioneConsegna;
    }

}
