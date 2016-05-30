package it.eurotn.panjea.magazzino.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaCategoriaEstesa;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.IDescrizioneLingua;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.InformazioneLinguaCategoria;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.util.PanjeaEJBUtil;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "maga_categorie", uniqueConstraints = @UniqueConstraint(columnNames = { "codice", "codiceazienda" }) )
@org.hibernate.annotations.Table(appliesTo = "maga_categorie", indexes = {
        @Index(name = "IdxCodice", columnNames = { "codice", "codiceazienda" }) })
@NamedQueries({
        @NamedQuery(name = "Categoria.caricaAll", query = "select DISTINCT c from Categoria c left join fetch c.informazioniLingua where c.codiceAzienda = :paramCodiceAzienda order by c.codice", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "categoria") }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "categoria")
public class Categoria extends EntityBase implements IDescrizioneFactory, IDescrizioneEstesaFactory {

    private static final long serialVersionUID = 2191331765662570897L;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    @Column(length = 40, nullable = false)
    private String codice;

    // descrizione in lingua aziendale
    private String descrizioneLinguaAziendale;

    @Embedded
    private GenerazioneCodiceArticoloData generazioneCodiceArticoloData;

    @OneToOne
    private Categoria padre;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "categoria")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<AttributoCategoria> attributiCategoria;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @MapKey(name = "codiceLingua")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @Fetch(FetchMode.JOIN)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "categoria")
    private Map<String, InformazioneLinguaCategoria> informazioniLingua;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @MapKey(name = "codiceLingua")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @Fetch(FetchMode.JOIN)
    private Map<String, DescrizioneLinguaCategoriaEstesa> descrizioniLinguaEstesa;

    private boolean cauzione;

    private String formulaPredefinitaComponente;

    /**
     * Costruttore.
     */
    public Categoria() {
        initialize();
    }

    /**
     * Aggiunge una descrizione alla categoria.
     * 
     * @param lingua
     *            descrizione da aggiungere
     */
    public void addDescrizione(InformazioneLinguaCategoria lingua) {
        if (informazioniLingua == null) {
            informazioniLingua = new HashMap<String, InformazioneLinguaCategoria>();
        }
        informazioniLingua.put(lingua.getCodiceLingua(), lingua);
    }

    /**
     * Aggiunge una descrizione estesa alla categoria.
     * 
     * @param lingua
     *            descrizione da aggiungere
     */
    public void aggiungiDescrizioneEstesa(DescrizioneLinguaCategoriaEstesa lingua) {
        if (descrizioniLinguaEstesa == null) {
            descrizioniLinguaEstesa = new HashMap<String, DescrizioneLinguaCategoriaEstesa>();
        }
        descrizioniLinguaEstesa.put(lingua.getCodiceLingua(), lingua);
    }

    @Override
    public int compareTo(EntityBase obj) {
        if (obj instanceof Categoria) {
            Categoria categoriaCompare = (Categoria) obj;
            String baseValue = codice + getDescrizione();
            String compareValue = categoriaCompare.getCodice() + categoriaCompare.getDescrizione();
            return baseValue.compareTo(compareValue);
        }
        return 1;
    }

    @Override
    public IDescrizioneLingua createDescrizioneLingua() {
        return new InformazioneLinguaCategoria();
    }

    @Override
    public IDescrizioneLingua createDescrizioneLinguaEstesa() {
        return new DescrizioneLinguaCategoriaEstesa();
    }

    /**
     * @return the attributCategoria
     */
    public List<AttributoCategoria> getAttributiCategoria() {
        return attributiCategoria;
    }

    /**
     * @return categoria lite creata dalla categoria
     */
    public CategoriaLite getCategoriaLite() {
        CategoriaLite categoriaLite = new CategoriaLite();
        PanjeaEJBUtil.copyProperties(categoriaLite, this);
        return categoriaLite;
    }

    /**
     * @return codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * Restituisce la descrizione aziendale.
     * 
     * @return descrizione in lingua
     */
    public String getDescrizione() {
        return descrizioneLinguaAziendale;
    }

    /**
     * Restituisce la descrizione in lingua aziendale per l'esportazione. codice + " - " + descrizione in lingua
     * aziendale
     * 
     * @return descrizione in lingua aziendale
     */
    public String getDescrizioneEsportazione() {
        return codice;
    }

    /**
     * @return the descrizioniLinguaEstesa
     */
    public Map<String, DescrizioneLinguaCategoriaEstesa> getDescrizioniLinguaEstesa() {
        return descrizioniLinguaEstesa;
    }

    /**
     * @return the formulaPredefinitaComponente
     */
    public String getFormulaPredefinitaComponente() {
        return formulaPredefinitaComponente;
    }

    /**
     * @return generazioneCodiceArticoloData
     */
    public GenerazioneCodiceArticoloData getGenerazioneCodiceArticoloData() {
        if (generazioneCodiceArticoloData == null) {
            generazioneCodiceArticoloData = new GenerazioneCodiceArticoloData();
        }
        return generazioneCodiceArticoloData;
    }

    /**
     * @return the informazioniLingua
     */
    public Map<String, InformazioneLinguaCategoria> getInformazioniLingua() {
        return informazioniLingua;
    }

    /**
     * @return padre
     */
    public Categoria getPadre() {
        return padre;
    }

    /**
     * Inizializza i valori di default della categoria.
     */
    private void initialize() {
        this.descrizioniLinguaEstesa = new TreeMap<String, DescrizioneLinguaCategoriaEstesa>();
        this.informazioniLingua = new HashMap<String, InformazioneLinguaCategoria>();
        attributiCategoria = new ArrayList<AttributoCategoria>();
        this.cauzione = Boolean.FALSE;
    }

    /**
     * @return the cauzione
     */
    public boolean isCauzione() {
        return cauzione;
    }

    /**
     * @param attributiCategoria
     *            the attributiCategoria to set
     */
    public void setAttributiCategoria(List<AttributoCategoria> attributiCategoria) {
        this.attributiCategoria = attributiCategoria;
    }

    /**
     * @param cauzione
     *            the cauzione to set
     */
    public void setCauzione(boolean cauzione) {
        this.cauzione = cauzione;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * 
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizioneLinguaAziendale = descrizione;
    }

    /**
     * @param descrizione
     *            the descrizioneEsportazione to set
     */
    public void setDescrizioneEsportazione(String descrizione) {
        throw new UnsupportedOperationException("Non è possibile settare la descrizione importazione.");
    }

    /**
     * @param descrizioniLinguaEstesa
     *            the descrizioniLinguaEstesa to set
     */
    public void setDescrizioniLinguaEstesa(Map<String, DescrizioneLinguaCategoriaEstesa> descrizioniLinguaEstesa) {
        this.descrizioniLinguaEstesa = descrizioniLinguaEstesa;
    }

    /**
     * @param formulaPredefinitaComponente
     *            the formulaPredefinitaComponente to set
     */
    public void setFormulaPredefinitaComponente(String formulaPredefinitaComponente) {
        this.formulaPredefinitaComponente = formulaPredefinitaComponente;
    }

    /**
     * @param generazioneCodiceArticoloData
     *            the generazioneCodiceArticoloData to set
     */
    public void setGenerazioneCodiceArticoloData(GenerazioneCodiceArticoloData generazioneCodiceArticoloData) {
        this.generazioneCodiceArticoloData = generazioneCodiceArticoloData;
    }

    /**
     * @param informazioniLingua
     *            the informazioniLingua to set
     */
    public void setInformazioniLingua(Map<String, InformazioneLinguaCategoria> informazioniLingua) {
        this.informazioniLingua = informazioniLingua;
    }

    /**
     * @param padre
     *            the padre to set
     */
    public void setPadre(Categoria padre) {
        this.padre = padre;
    }

    /**
     * Metodo utilizzato solamente dal formmodel dell'editor della categoria.
     * 
     * @param list
     *            tipiAttributo
     */
    public void setTipiAttributoEreditati(List<TipoAttributo> list) {

    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     * 
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {
        StringBuilder retValue = new StringBuilder();

        retValue.append("Categoria[ ").append(super.toString()).append(" codiceAzienda = ").append(this.codiceAzienda)
                .append(" codice = ").append(this.codice).append(" ]");

        return retValue.toString();
    }
}
