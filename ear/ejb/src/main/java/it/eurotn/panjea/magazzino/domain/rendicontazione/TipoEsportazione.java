package it.eurotn.panjea.magazzino.domain.rendicontazione;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

@Entity
@Audited
@Table(name = "maga_tipi_esportazioni", uniqueConstraints = @UniqueConstraint(columnNames = { "nome" }) )
@NamedQueries({
        @NamedQuery(name = "TipoEsportazione.caricaByNome", query = "select te from TipoEsportazione te where te.codiceAzienda = :codiceAzienda and te.nome like :nome order by te.nome") })
public class TipoEsportazione extends EntityBase {

    private static final long serialVersionUID = -1830978229491992936L;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    @Column(length = 30)
    private String nome;

    @Column(length = 15)
    private String codiceCliente;

    private String template;

    private String jndiName;

    private boolean richiediData;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<TipoAreaMagazzino> tipiAreeMagazzino;

    private TipoSpedizione tipoSpedizione;

    @Embedded
    private DatiSpedizione datiSpedizione;

    /**
     * Costruttore.
     * 
     */
    public TipoEsportazione() {
        super();
        this.datiSpedizione = new DatiSpedizione();
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return Returns the codiceCliente.
     */
    public String getCodiceCliente() {
        return codiceCliente;
    }

    /**
     * @return the datiSpedizione
     */
    public DatiSpedizione getDatiSpedizione() {
        if (datiSpedizione == null) {
            datiSpedizione = new DatiSpedizione();
        }
        return datiSpedizione;
    }

    /**
     * @return Returns the jndiName.
     */
    public String getJndiName() {
        return jndiName;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @return the tipiAreeMagazzino
     */
    public List<TipoAreaMagazzino> getTipiAreeMagazzino() {
        return tipiAreeMagazzino;
    }

    /**
     * @return the tipoSpedizione
     */
    public TipoSpedizione getTipoSpedizione() {
        return tipoSpedizione;
    }

    /**
     * @return Returns the richiediData.
     */
    public boolean isRichiediData() {
        return richiediData;
    }

    /**
     * 
     * @return true se ci sono parametri da richiedere
     */
    public boolean isRichiediParametri() {
        return richiediData;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param codiceCliente
     *            The codiceCliente to set.
     */
    public void setCodiceCliente(String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }

    /**
     * @param datiSpedizione
     *            the datiSpedizione to set
     */
    public void setDatiSpedizione(DatiSpedizione datiSpedizione) {
        this.datiSpedizione = datiSpedizione;
    }

    /**
     * @param jndiName
     *            The jndiName to set.
     */
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    /**
     * @param nome
     *            the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @param richiediData
     *            The richiediData to set.
     */
    public void setRichiediData(boolean richiediData) {
        this.richiediData = richiediData;
    }

    /**
     * @param template
     *            the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * @param tipiAreeMagazzino
     *            the tipiAreeMagazzino to set
     */
    public void setTipiAreeMagazzino(List<TipoAreaMagazzino> tipiAreeMagazzino) {
        this.tipiAreeMagazzino = tipiAreeMagazzino;
    }

    /**
     * @param tipoSpedizione
     *            the tipoSpedizione to set
     */
    public void setTipoSpedizione(TipoSpedizione tipoSpedizione) {
        this.tipoSpedizione = tipoSpedizione;
    }
}
