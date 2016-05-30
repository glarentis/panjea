package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.intra.domain.ModalitaTrasporto;

/**
 * Contiene il listino e l'eventuale listino alternativo della sede entità.
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_sedi_magazzino")
@NamedQueries({
        @NamedQuery(name = "SedeMagazzino.caricaSediMagazzinoByEntita", query = " select sede from SedeMagazzino as sede where sede.sedeEntita.entita.id = :paramEntitaId and sede.sedeEntita.abilitato = true "),
        @NamedQuery(name = "SedeMagazzino.caricaSedeMagazzinoPrincipale", query = " select sede from SedeMagazzino as sede where sede.sedeEntita.entita.id = :paramEntitaId and sede.sedeEntita.tipoSede.sedePrincipale = true "),
        @NamedQuery(name = "SedeMagazzino.caricaSediMagazzinoBySedeEntita", query = " select sede from SedeMagazzino as sede where sede.sedeEntita.id = :paramSedeEntitaId "),
        @NamedQuery(name = "SedeMagazzino.caricaSediMagazzinoSenzaContrattoByEntita", query = " select sede from SedeMagazzino as sede where sede.sedeEntita.entita.id = :paramEntitaId and sede.contratto.id is null "),
        @NamedQuery(name = "SedeMagazzino.caricaSediMagazzinoDiRifatturazione", query = " select sml from SedeMagazzinoLite sml where sml.sedeEntita.entita.anagrafica.codiceAzienda = :paramCodiceAzienda and sml.sedeDiRifatturazione = true order by sml.sedeEntita.entita.codice "),
        @NamedQuery(name = "SedeMagazzino.caricaSediRifatturazioneNonAssociate", query = " select sml from SedeMagazzinoLite sml where sml.sedeEntita.entita = :paramEntita and sml.sedePerRifatturazione = null"),
        @NamedQuery(name = "SedeMagazzino.caricaSediRifatturazioneAssociate", query = " select sml from SedeMagazzinoLite sml where sml.sedeEntita.entita.anagrafica.codiceAzienda = :paramCodiceAzienda and sml.sedePerRifatturazione != null"),
        @NamedQuery(name = "SedeMagazzino.caricaCategoriaSedeMagazzino", query = " select sede.categoriaSedeMagazzino from SedeMagazzino as sede inner join sede.sedeEntita sedeEntita where sedeEntita.id = :paramIdSede") })
@EntityConverter(properties = "sedeEntita.codice,sedeEntita.sede.indirizzo")
public class SedeMagazzino extends EntityBase {

    public enum ETipologiaCodiceIvaAlternativo {
        NESSUNO {

            @Override
            public CodiceIva getCodiceIva(Articolo articolo, CodiceIva codiceIvaSedeAlternativo) {
                return articolo.getCodiceIva();
            }
        },
        ESENZIONE {
            @Override
            public CodiceIva getCodiceIva(Articolo articolo, CodiceIva codiceIvaSedeAlternativo) {
                if (codiceIvaSedeAlternativo != null) {
                    return codiceIvaSedeAlternativo;
                }
                return articolo.getCodiceIva();
            }
        },
        AGEVOLATA {
            @Override
            public CodiceIva getCodiceIva(Articolo articolo, CodiceIva codiceIvaSedeAlternativo) {
                if (codiceIvaSedeAlternativo != null && articolo.isIvaAlternativa()) {
                    return codiceIvaSedeAlternativo;
                }
                return articolo.getCodiceIva();
            }
        },
        ESENZIONE_DICHIARAZIONE_INTENTO {
            @Override
            public CodiceIva getCodiceIva(Articolo articolo, CodiceIva codiceIvaSedeAlternativo) {
                if (codiceIvaSedeAlternativo != null) {
                    return codiceIvaSedeAlternativo;
                }
                return articolo.getCodiceIva();
            }
        },
        SOMMINISTRAZIONE {
            @Override
            public CodiceIva getCodiceIva(Articolo articolo, CodiceIva codiceIvaSedeAlternativo) {
                if (codiceIvaSedeAlternativo != null && articolo.isSomministrazione()) {
                    return codiceIvaSedeAlternativo;
                }
                return articolo.getCodiceIva();
            }
        };

        /**
         *
         * @param articolo
         *            articolo interessato
         * @param codiceIvaSedeAlternativo
         *            evantuale codice iva da sostituire
         * @return codice iva da associare alla riga
         */
        public abstract CodiceIva getCodiceIva(Articolo articolo, CodiceIva codiceIvaSedeAlternativo);

    }

    /**
     * Indica la tipologia di generazione del documento di fatturazione. Le tipologie che possiamo avere sono:<br>
     * <ul>
     * <li>ENTITA: viene generato 1 documento di fatturazione per tutte le sedi dell'entità</li>
     * <li>SEDE: viene generato 1 documento di fatturazione per ogni sede dell'entità</li>
     * <li>DOCUMENTO: viene generato 1 documento di fatturazione per ogni documento da fatturare</li>
     * </ul>
     *
     * @author fattazzo
     */
    public enum ETipologiaGenerazioneDocumentoFatturazione {
        ENTITA, SEDE, DOCUMENTO
    }

    private static final long serialVersionUID = -4704047863793752842L;

    @OneToOne
    private SedeEntita sedeEntita;

    @ManyToOne
    private Listino listino;

    @ManyToOne
    @Fetch(FetchMode.SELECT)
    private CausaleTrasporto causaleTrasporto;

    @ManyToOne
    @Fetch(FetchMode.SELECT)
    private TipoPorto tipoPorto;

    @ManyToOne
    @Fetch(FetchMode.SELECT)
    private AspettoEsteriore aspettoEsteriore;

    @ManyToOne
    @Fetch(FetchMode.SELECT)
    private TrasportoCura trasportoCura;

    private ModalitaTrasporto modalitaTrasporto;

    @ManyToOne
    private Listino listinoAlternativo;

    @ManyToOne
    private Contratto contratto;

    @Column(length = 3)
    private String codiceValuta;

    private boolean calcoloSpese;

    private boolean raggruppamentoBolle;

    private boolean stampaPrezzo;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private CategoriaSedeMagazzino categoriaSedeMagazzino;

    private ETipologiaGenerazioneDocumentoFatturazione tipologiaGenerazioneDocumentoFatturazione;

    // flag che indica che la sede magazzino è utilizzata per la rifatturazione
    private boolean sedeDiRifatturazione;

    // indica l'eventuale sede sulla quale dovrà avvenire la rifatturazione
    @OneToOne
    private SedeMagazzinoLite sedePerRifatturazione;

    @ManyToOne
    private CodiceIva codiceIvaAlternativo;

    private ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo;

    /**
     * Inizializza i valori di default.
     */
    {
        this.tipologiaGenerazioneDocumentoFatturazione = ETipologiaGenerazioneDocumentoFatturazione.ENTITA;
        this.sedeDiRifatturazione = false;
        this.tipologiaCodiceIvaAlternativo = ETipologiaCodiceIvaAlternativo.NESSUNO;
        this.stampaPrezzo = true;
        this.raggruppamentoBolle = true;
        this.dichiarazioneIntento = new DichiarazioneIntento();
    }

    @Embedded
    private DichiarazioneIntento dichiarazioneIntento;

    /**
     * Costruttore.
     */
    public SedeMagazzino() {
    }

    /**
     * @return the aspettoEsteriore
     */
    public AspettoEsteriore getAspettoEsteriore() {
        return aspettoEsteriore;
    }

    /**
     * @return the categoriaContabileSedeMagazzino
     */
    public CategoriaContabileSedeMagazzino getCategoriaContabileSedeMagazzino() {
        return categoriaContabileSedeMagazzino;
    }

    /**
     * @return the categoriaSedeMagazzino
     */
    public CategoriaSedeMagazzino getCategoriaSedeMagazzino() {
        return categoriaSedeMagazzino;
    }

    /**
     * @return the causaleTrasporto
     */
    public CausaleTrasporto getCausaleTrasporto() {
        return causaleTrasporto;
    }

    /**
     * @return the codiceIvaAlternativo
     */
    public CodiceIva getCodiceIvaAlternativo() {
        return codiceIvaAlternativo;
    }

    /**
     * @return the codiceValuta
     */
    public String getCodiceValuta() {
        return codiceValuta;
    }

    /**
     * @return the contratto
     */
    public Contratto getContratto() {
        return contratto;
    }

    /**
     * @return Returns the dichiarazioneIntento.
     */
    public DichiarazioneIntento getDichiarazioneIntento() {
        if (this.dichiarazioneIntento == null) {
            this.dichiarazioneIntento = new DichiarazioneIntento();
        }
        return dichiarazioneIntento;
    }

    /**
     * @return the listino
     */
    public Listino getListino() {
        return listino;
    }

    /**
     * @return the listinoAlternativo
     */
    public Listino getListinoAlternativo() {
        return listinoAlternativo;
    }

    /**
     * @return the modalitaTrasporto
     */
    public ModalitaTrasporto getModalitaTrasporto() {
        return modalitaTrasporto;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return sedeMagazzinoLite
     */
    public SedeMagazzinoLite getSedeMagazzinoLite() {
        SedeMagazzinoLite sedeMagazzinoLite = new SedeMagazzinoLite();
        sedeMagazzinoLite.setId(getId());
        sedeMagazzinoLite.setVersion(getVersion());
        return sedeMagazzinoLite;
    }

    /**
     * @return the sedePerRifatturazione
     */
    public SedeMagazzinoLite getSedePerRifatturazione() {
        return sedePerRifatturazione;
    }

    /**
     * @return the tipologiaCodiceIvaAlternativo
     */
    public ETipologiaCodiceIvaAlternativo getTipologiaCodiceIvaAlternativo() {
        return tipologiaCodiceIvaAlternativo;
    }

    /**
     * @return the tipologiaGenerazioneDocumentoFatturazione
     */
    public ETipologiaGenerazioneDocumentoFatturazione getTipologiaGenerazioneDocumentoFatturazione() {
        return tipologiaGenerazioneDocumentoFatturazione;
    }

    /**
     * @return the tipoPorto
     */
    public TipoPorto getTipoPorto() {
        return tipoPorto;
    }

    /**
     * @return the trasportoCura
     */
    public TrasportoCura getTrasportoCura() {
        return trasportoCura;
    }

    /**
     * @return the calcoloSpese
     */
    public boolean isCalcoloSpese() {
        return calcoloSpese;
    }

    /**
     * @return the raggruppamentoBolle
     */
    public boolean isRaggruppamentoBolle() {
        return raggruppamentoBolle;
    }

    /**
     * @return the sedeDiRifatturazione
     */
    public boolean isSedeDiRifatturazione() {
        return sedeDiRifatturazione;
    }

    /**
     * @return the stampaPrezzo
     */
    public boolean isStampaPrezzo() {
        return stampaPrezzo;
    }

    /**
     * @param aspettoEsteriore
     *            the aspettoEsteriore to set
     */
    public void setAspettoEsteriore(AspettoEsteriore aspettoEsteriore) {
        this.aspettoEsteriore = aspettoEsteriore;
    }

    /**
     * @param calcoloSpese
     *            the calcoloSpese to set
     */
    public void setCalcoloSpese(boolean calcoloSpese) {
        this.calcoloSpese = calcoloSpese;
    }

    /**
     * @param categoriaContabileSedeMagazzino
     *            the categoriaContabileSedeMagazzino to set
     */
    public void setCategoriaContabileSedeMagazzino(CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino) {
        this.categoriaContabileSedeMagazzino = categoriaContabileSedeMagazzino;
    }

    /**
     * @param categoriaSedeMagazzino
     *            the categoriaSedeMagazzino to set
     */
    public void setCategoriaSedeMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino) {
        this.categoriaSedeMagazzino = categoriaSedeMagazzino;
    }

    /**
     * @param causaleTrasporto
     *            the causaleTrasporto to set
     */
    public void setCausaleTrasporto(CausaleTrasporto causaleTrasporto) {
        this.causaleTrasporto = causaleTrasporto;
    }

    /**
     * @param codiceIvaAlternativo
     *            the codiceIvaAlternativo to set
     */
    public void setCodiceIvaAlternativo(CodiceIva codiceIvaAlternativo) {
        this.codiceIvaAlternativo = codiceIvaAlternativo;
    }

    /**
     * @param codiceValuta
     *            the codiceValuta to set
     */
    public void setCodiceValuta(String codiceValuta) {
        this.codiceValuta = codiceValuta;
    }

    /**
     * @param contratto
     *            the contratto to set
     */
    public void setContratto(Contratto contratto) {
        this.contratto = contratto;
    }

    /**
     * @param dichiarazioneIntento
     *            The dichiarazioneIntento to set.
     */
    public void setDichiarazioneIntento(DichiarazioneIntento dichiarazioneIntento) {
        this.dichiarazioneIntento = dichiarazioneIntento;
    }

    /**
     * @param listino
     *            the listino to set
     */
    public void setListino(Listino listino) {
        this.listino = listino;
    }

    /**
     * @param listinoAlternativo
     *            the listinoAlternativo to set
     */
    public void setListinoAlternativo(Listino listinoAlternativo) {
        this.listinoAlternativo = listinoAlternativo;
    }

    /**
     * @param modalitaTrasporto
     *            the modalitaTrasporto to set
     */
    public void setModalitaTrasporto(ModalitaTrasporto modalitaTrasporto) {
        this.modalitaTrasporto = modalitaTrasporto;
    }

    /**
     * @param raggruppamentoBolle
     *            the raggruppamentoBolle to set
     */
    public void setRaggruppamentoBolle(boolean raggruppamentoBolle) {
        this.raggruppamentoBolle = raggruppamentoBolle;
    }

    /**
     * @param sedeDiRifatturazione
     *            the sedeDiRifatturazione to set
     */
    public void setSedeDiRifatturazione(boolean sedeDiRifatturazione) {
        this.sedeDiRifatturazione = sedeDiRifatturazione;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

    /**
     * @param sedePerRifatturazione
     *            the sedePerRifatturazione to set
     */
    public void setSedePerRifatturazione(SedeMagazzinoLite sedePerRifatturazione) {
        this.sedePerRifatturazione = sedePerRifatturazione;
    }

    /**
     * @param stampaPrezzo
     *            the stampaPrezzo to set
     */
    public void setStampaPrezzo(boolean stampaPrezzo) {
        this.stampaPrezzo = stampaPrezzo;
    }

    /**
     * @param tipologiaCodiceIvaAlternativo
     *            the tipologiaCodiceIvaAlternativo to set
     */
    public void setTipologiaCodiceIvaAlternativo(ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo) {
        this.tipologiaCodiceIvaAlternativo = tipologiaCodiceIvaAlternativo;
    }

    /**
     * @param tipologiaGenerazioneDocumentoFatturazione
     *            the tipologiaGenerazioneDocumentoFatturazione to set
     */
    public void setTipologiaGenerazioneDocumentoFatturazione(
            ETipologiaGenerazioneDocumentoFatturazione tipologiaGenerazioneDocumentoFatturazione) {
        this.tipologiaGenerazioneDocumentoFatturazione = tipologiaGenerazioneDocumentoFatturazione;
    }

    /**
     * @param tipoPorto
     *            the tipoPorto to set
     */
    public void setTipoPorto(TipoPorto tipoPorto) {
        this.tipoPorto = tipoPorto;
    }

    /**
     * @param trasportoCura
     *            the trasportoCura to set
     */
    public void setTrasportoCura(TrasportoCura trasportoCura) {
        this.trasportoCura = trasportoCura;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     *
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {
        StringBuilder retValue = new StringBuilder();
        retValue.append("SedeMagazzino[ ");
        retValue.append(super.toString());
        retValue.append(" sedeEntita = ");
        retValue.append(this.sedeEntita != null ? this.sedeEntita.getId() : null);
        retValue.append(" listino = ");
        retValue.append(this.listino != null ? this.listino.getId() : null);
        retValue.append(" listinoAlternativo = ");
        retValue.append(this.listinoAlternativo != null ? this.listinoAlternativo.getId() : null);
        retValue.append(" contratto = ").append(this.contratto != null ? this.contratto.getId() : null);
        retValue.append(" categoriaSedeMagazzino = ");
        retValue.append(this.categoriaSedeMagazzino != null ? this.categoriaSedeMagazzino.getId() : null);
        retValue.append(" ]");

        return retValue.toString();
    }

}
