package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

/**
 * Classe di dominio che definisce i tipi documento base per l'area magazzino.
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_tipi_documento_base", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "codiceAzienda", "tipoOperazione" }),
        @UniqueConstraint(columnNames = { "codiceAzienda", "tipoAreaMagazzino_id" }) })
@NamedQueries({
        @NamedQuery(name = "TipoDocumentoBaseMagazzino.caricaByAzienda", query = "from TipoDocumentoBaseMagazzino t where t.codiceAzienda = :paramCodiceAzienda "),
        @NamedQuery(name = "TipoDocumentoBaseMagazzino.caricaByTipoOperazione", query = "from TipoDocumentoBaseMagazzino t where t.codiceAzienda = :paramCodiceAzienda and t.tipoOperazione = :paramTipoOperazione ") })
public class TipoDocumentoBaseMagazzino extends EntityBase {

    public enum TipoOperazioneTipoDocumento {
        RETTIFICA_POSITIVA, RETTIFICA_NEGATIVA, SCONTRINO_POS
    }

    private static final long serialVersionUID = -2834979001085172266L;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    /**
     * @uml.property name="tipoAreaMagazzino"
     * @uml.associationEnd
     */
    @ManyToOne
    private TipoAreaMagazzino tipoAreaMagazzino;

    /**
     * @uml.property name="tipoOperazione"
     * @uml.associationEnd
     */
    @Enumerated
    private TipoOperazioneTipoDocumento tipoOperazione;

    /**
     * Costruttore.
     * 
     */
    public TipoDocumentoBaseMagazzino() {
        super();
        initialize();
    }

    /**
     * @return codiceAzienda
     * @uml.property name="codiceAzienda"
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return tipoAreaMagazzino
     * @uml.property name="tipoAreaMagazzino"
     */
    public TipoAreaMagazzino getTipoAreaMagazzino() {
        return tipoAreaMagazzino;
    }

    /**
     * @return tipoOperazione
     * @uml.property name="tipoOperazione"
     */
    public TipoOperazioneTipoDocumento getTipoOperazione() {
        return tipoOperazione;
    }

    /**
     * Inizializza i valori di default.
     */
    private void initialize() {
        this.tipoAreaMagazzino = new TipoAreaMagazzino();
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     * @uml.property name="codiceAzienda"
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param tipoAreaMagazzino
     *            the tipoAreaMagazzino to set
     * @uml.property name="tipoAreaMagazzino"
     */
    public void setTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        this.tipoAreaMagazzino = tipoAreaMagazzino;
    }

    /**
     * @param tipoOperazione
     *            the tipoOperazione to set
     * @uml.property name="tipoOperazione"
     */
    public void setTipoOperazione(TipoOperazioneTipoDocumento tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }
}
