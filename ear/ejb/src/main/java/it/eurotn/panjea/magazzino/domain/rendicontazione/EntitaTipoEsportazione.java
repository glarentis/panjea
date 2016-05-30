package it.eurotn.panjea.magazzino.domain.rendicontazione;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

@Entity
@Audited
@Table(name = "maga_tipi_esportazioni_entita", uniqueConstraints = @UniqueConstraint(columnNames = {
        "tipoEsportazione_id", "entita_id" }) )
@NamedQueries({
        @NamedQuery(name = "EntitaTipoEsportazione.caricaAll", query = "select ete from EntitaTipoEsportazione ete where ete.codiceAzienda = :codiceAzienda") })
public class EntitaTipoEsportazione extends EntityBase {

    private static final long serialVersionUID = 8660303841313232814L;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    @ManyToOne
    private EntitaLite entita;

    @ManyToOne
    private TipoEsportazione tipoEsportazione;

    /**
     * @return Returns the codiceAzienda.
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return Returns the entita.
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return Returns the tipoEsportazione.
     */
    public TipoEsportazione getTipoEsportazione() {
        return tipoEsportazione;
    }

    /**
     * @param codiceAzienda
     *            The codiceAzienda to set.
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param entita
     *            The entita to set.
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param tipoEsportazione
     *            The tipoEsportazione to set.
     */
    public void setTipoEsportazione(TipoEsportazione tipoEsportazione) {
        this.tipoEsportazione = tipoEsportazione;
    }

}
