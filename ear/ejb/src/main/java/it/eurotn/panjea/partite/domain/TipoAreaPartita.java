package it.eurotn.panjea.partite.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseDdt;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseIncassoPagamento;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseMovimentoGenerico;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseMovimentoMagazzinoGenerico;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseOrdine;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Classe di dominio di Tipo Area Partita.
 *
 * @author adriano
 * @version 1.0, 08/lug/08
 */
@Entity
@Audited
@Table(name = "part_tipi_area_partita")
@NamedQueries({
        @NamedQuery(name = "TipoAreaPartita.caricaPerPagamentiByAzienda", query = " from TipoAreaPartita t where t.tipoDocumento.codiceAzienda = :paramCodiceAzienda and (t.tipoDocumento.abilitato = true or :paramTuttiTipi = 1) and t.tipoDocumento.classeTipoDocumento='it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseIncassoPagamento'"),
        @NamedQuery(name = "TipoAreaPartita.caricaByTipoDocumento", query = " from TipoAreaPartita t where t.tipoDocumento.id = :paramTipoDocumentoId ", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "tipoAreaPartita") }),
        @NamedQuery(name = "TipoAreaPartita.caricaGenerazioneRateByTipoPartita", query = " from TipoAreaPartita t where t.tipoPartita = :paramTipoPartita and t.tipoOperazione=1 ") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "tipoAreaPartita")
public class TipoAreaPartita extends EntityBase implements Cloneable, ITipoAreaDocumento {

    /**
     * Enum per la definizione del tipo di operazione del tipo area partita.
     * <ul>
     * <li>CHIUSURA definisce che il tipo area partita effettuerà  la chiusura delle partite, utilizzato per i tipo
     * documento che descrivono un pagamento</li>
     * <li>GENERA definisce che il tipo area partita genererà  le rate del documento collegato</li>
     * <li>GESTIONE_DISTINTA definisce il tipo di area che genera/gestisce distinta da mandare in banca</li>
     * <li>NESSUNA nessuna operazione legata all'area partita: permette solamente l'assegnazione del codice di pagamento
     * </li>
     * </ul>
     *
     * @author adriano
     * @version 1.0, 08/lug/08
     */
    public enum TipoOperazione {

        CHIUSURA, GENERA, GESTIONE_DISTINTA, NESSUNA, GESTIONE_ASSEGNO, ANTICIPO_FATTURE, CHIUSURA_ANTICIPO_FATTURE;

        /**
         * restituisce un array di {@link TipoOperazione} filtrati per la classe tipo documento passata come argomento.
         *
         * @param classeTipoDocumento
         *            classe tipo documento
         * @return array di {@link TipoOperazione} coerenti per la classeTipoDocumento
         */
        public static TipoOperazione[] valuesForClasseTipoDocumento(String classeTipoDocumento) {
            if (ClasseFattura.class.getName().equals(classeTipoDocumento)) {
                return new TipoOperazione[] { GENERA, NESSUNA };
            } else if (ClasseIncassoPagamento.class.getName().equals(classeTipoDocumento)) {
                return new TipoOperazione[] { CHIUSURA, GESTIONE_DISTINTA, GESTIONE_ASSEGNO, ANTICIPO_FATTURE,
                        CHIUSURA_ANTICIPO_FATTURE };
            } else if (ClasseOrdine.class.getName().equals(classeTipoDocumento)) {
                return new TipoOperazione[] { NESSUNA };
            } else if (ClasseDdt.class.getName().equals(classeTipoDocumento)) {
                return new TipoOperazione[] { NESSUNA };
            } else if (ClasseMovimentoGenerico.class.getName().equals(classeTipoDocumento)) {
                return new TipoOperazione[] { GENERA, NESSUNA };
            } else if (ClasseMovimentoMagazzinoGenerico.class.getName().equals(classeTipoDocumento)) {
                return new TipoOperazione[] { GENERA, NESSUNA };
            }
            return new TipoOperazione[] {};
        }
    }

    /**
     * Enum per la definizione del tipo partita ATTIVA indica che le partite emesse dai documenti di questo tipo
     * dovranno essere riscosse. <br>
     * PASSIVA indica che le partite emesse dai documenti di questo tipo dovranno essere pagate
     *
     * @author adriano
     * @version 1.0, 08/lug/08
     */
    public enum TipoPartita {

        ATTIVA, PASSIVA
    }

    private static final long serialVersionUID = 1L;

    @Column(length = 100)
    private String mascheraFlussoBanca;

    @OneToOne
    private TipoDocumento tipoDocumento;

    private TipoPartita tipoPartita;

    private TipoOperazione tipoOperazione;

    private TipoPagamento tipoPagamentoChiusura;

    @Column(length = 15)
    private String descrizionePerFlusso;

    private boolean speseIncasso;

    /**
     * Indica se generare un unico documento di pagamento dalle rate selezionate di clienti diversi.
     */
    private boolean chiusuraSuPagamentoUnico;

    private boolean gestioneCorrispettivi;

    /**
     * Costruttore.
     */
    public TipoAreaPartita() {
        super();
        initialize();
    }

    @Override
    public Object clone() {
        TipoAreaPartita tipoAreaPartita = PanjeaEJBUtil.cloneObject(this);
        tipoAreaPartita.setId(null);

        return tipoAreaPartita;
    }

    /**
     * @return the descrizionePerFlusso
     */
    public String getDescrizionePerFlusso() {
        return descrizionePerFlusso;
    }

    @Override
    public String getDescrizionePerStampa() {
        return "";
    }

    @Override
    public String getFormulaStandardNumeroCopie() {
        return "1";
    }

    /**
     * @return the mascheraFlussoBanca
     */
    public String getMascheraFlussoBanca() {
        return mascheraFlussoBanca;
    }

    @Override
    public String getReportPath() {
        return "";
    }

    /**
     * @return Returns the tipoDocumento.
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @return Returns the tipoOperazione.
     */
    public TipoOperazione getTipoOperazione() {
        return tipoOperazione;
    }

    /**
     * @return tipoPagamentoChiusura
     */
    public TipoPagamento getTipoPagamentoChiusura() {
        return tipoPagamentoChiusura;
    }

    /**
     * @return Returns the tipoPartita.
     */
    public TipoPartita getTipoPartita() {
        return tipoPartita;
    }

    /**
     * Inizializza i valori di default.
     */
    private void initialize() {
        this.tipoDocumento = new TipoDocumento();
        this.speseIncasso = false;
        this.chiusuraSuPagamentoUnico = false;
        this.gestioneCorrispettivi = false;
    }

    /**
     * @return the chiusuraSuPagamentoUnico
     */
    public boolean isChiusuraSuPagamentoUnico() {
        return chiusuraSuPagamentoUnico;
    }

    /**
     * @return the gestioneCorrispettivi
     */
    public boolean isGestioneCorrispettivi() {
        return gestioneCorrispettivi;
    }

    /**
     * @return true se il tipoAreaPartita è un pagamento, false genera rate
     */
    public boolean isPagamento() {
        return getTipoOperazione() == TipoOperazione.CHIUSURA
                || getTipoOperazione() == TipoOperazione.GESTIONE_DISTINTA;
    }

    /**
     * @return the speseIncasso
     */
    public boolean isSpeseIncasso() {
        return speseIncasso;
    }

    /**
     * @param chiusuraSuPagamentoUnico
     *            the chiusuraSuPagamentoUnico to set
     */
    public void setChiusuraSuPagamentoUnico(boolean chiusuraSuPagamentoUnico) {
        this.chiusuraSuPagamentoUnico = chiusuraSuPagamentoUnico;
    }

    /**
     * @param descrizionePerFlusso
     *            the descrizionePerFlusso to set
     */
    public void setDescrizionePerFlusso(String descrizionePerFlusso) {
        this.descrizionePerFlusso = descrizionePerFlusso;
    }

    /**
     * @param gestioneCorrispettivi
     *            the gestioneCorrispettivi to set
     */
    public void setGestioneCorrispettivi(boolean gestioneCorrispettivi) {
        this.gestioneCorrispettivi = gestioneCorrispettivi;
    }

    /**
     * @param mascheraFlussoBanca
     *            the mascheraFlussoBanca to set
     */
    public void setMascheraFlussoBanca(String mascheraFlussoBanca) {
        this.mascheraFlussoBanca = mascheraFlussoBanca;
    }

    /**
     * @param speseIncasso
     *            the speseIncasso to set
     */
    public void setSpeseIncasso(boolean speseIncasso) {
        this.speseIncasso = speseIncasso;
    }

    /**
     * @param tipoDocumento
     *            The tipoDocumento to set.
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @param tipoOperazione
     *            The tipoOperazione to set.
     */
    public void setTipoOperazione(TipoOperazione tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
        updateGestioneCorrispettivi();
    }

    /**
     * @param tipoPagamentoChiusura
     *            the tipoPagamentoChiusura to set
     */
    public void setTipoPagamentoChiusura(TipoPagamento tipoPagamentoChiusura) {
        this.tipoPagamentoChiusura = tipoPagamentoChiusura;
    }

    /**
     * @param tipoPartita
     *            The tipoPartita to set.
     */
    public void setTipoPartita(TipoPartita tipoPartita) {
        this.tipoPartita = tipoPartita;
        updateGestioneCorrispettivi();
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     *
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuffer retValue = new StringBuffer();

        retValue.append("TipoAreaPartita[ ").append(super.toString()).append(" tipoDocumento = ")
                .append(this.tipoDocumento).append(" tipoPartita = ").append(this.tipoPartita).append(" ]");

        return retValue.toString();
    }

    private void updateGestioneCorrispettivi() {
        this.gestioneCorrispettivi = ObjectUtils.defaultIfNull(tipoPartita == TipoPartita.ATTIVA, false)
                && ObjectUtils.defaultIfNull(tipoOperazione == TipoOperazione.GENERA, false);
    }

}
