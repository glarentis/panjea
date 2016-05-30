package it.eurotn.panjea.corrispettivi.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.IEntityCodiceAzienda;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

@Entity
@Audited
@Table(name = "cont_corrispettivi_link_tipi_documento")
public class CorrispettivoLinkTipoDocumento extends EntityBase implements java.io.Serializable, IEntityCodiceAzienda {

    private static final long serialVersionUID = -5282749495168065445L;

    private String codiceAzienda;

    @ManyToOne
    private TipoDocumento tipoDocumentoOrigine;

    @ManyToOne
    private TipoDocumento tipoDocumentoDestinazione;

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the tipoDocumentoDestinazione
     */
    public TipoDocumento getTipoDocumentoDestinazione() {
        return tipoDocumentoDestinazione;
    }

    /**
     * @return the tipoDocumentoOrigine
     */
    public TipoDocumento getTipoDocumentoOrigine() {
        return tipoDocumentoOrigine;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param tipoDocumentoDestinazione
     *            the tipoDocumentoDestinazione to set
     */
    public void setTipoDocumentoDestinazione(TipoDocumento tipoDocumentoDestinazione) {
        this.tipoDocumentoDestinazione = tipoDocumentoDestinazione;
    }

    /**
     * @param tipoDocumentoOrigine
     *            the tipoDocumentoOrigine to set
     */
    public void setTipoDocumentoOrigine(TipoDocumento tipoDocumentoOrigine) {
        this.tipoDocumentoOrigine = tipoDocumentoOrigine;
    }
}
