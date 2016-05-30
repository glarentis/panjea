package it.eurotn.panjea.manutenzioni.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;

@Entity
@Audited
@Table(name = "manu_prodotti_collegati")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_PRODOTTO", discriminatorType = DiscriminatorType.STRING, length = 2)
@DiscriminatorValue("P")
public class ProdottoCollegato extends EntityBase {

    private static final long serialVersionUID = 6237238458705306124L;

    @ManyToOne
    private ArticoloLite articolo;

    private TipoProdottoCollegato tipo;

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the tipo
     */
    public TipoProdottoCollegato getTipo() {
        return tipo;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public final void setCodiceArticolo(String codiceArticolo) {
        getArticolo().setCodice(codiceArticolo);
    }

    /**
     * @param descrizioneArticolo
     *            the descrizioneArticolo to set
     */
    public final void setDescrizioneArticolo(String descrizioneArticolo) {
        getArticolo().setDescrizione(descrizioneArticolo);
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public final void setIdArticolo(Integer idArticolo) {
        setArticolo(new ArticoloLite());
        getArticolo().setId(idArticolo);
    }

    /**
     * @param provenienzaPrezzoArticolo
     *            the provenienzaPrezzoArticolo to set
     */
    public final void setProvenienzaPrezzoArticolo(ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo) {
        getArticolo().setProvenienzaPrezzoArticolo(provenienzaPrezzoArticolo);
    }

    /**
     * @param tipo
     *            the tipo to set
     */
    public void setTipo(TipoProdottoCollegato tipo) {
        this.tipo = tipo;
    }

    /**
     * @param versionArticolo
     *            the versionArticolo to set
     */
    public final void setVersionArticolo(Integer versionArticolo) {
        getArticolo().setVersion(versionArticolo);
    }

}
