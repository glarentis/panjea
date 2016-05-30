package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;

/**
 * Utilizzata per caricare i vari dati delle sedi utili quando creo un areaMagazzino.<br>
 * Contiene i dati utilizzati quando scelgo una sede cliente (sedeAnagrafica,sedeMagazzino,sedePagamenti,RapportiBancari
 * etc...).<BR>
 * Se la sede non è la principale continene già i dati ereditati della sede principale
 *
 * @author giangi
 */
public class SedeAreaMagazzinoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private AgenteLite agente;

    private List<RapportoBancarioSedeEntita> rapportiBancari;

    /**
     * Dati da sedeMagazzino.
     */
    private Listino listino;

    private Listino listinoAlternativo;

    private String categoriaSedeMagazzino;

    private String causaleTrasporto;

    private String trasportoCura;

    private String tipoPorto;

    private String codiceValuta;

    private boolean calcoloSpese;

    private boolean raggruppamentoBolle;

    private String aspettoEsteriore;

    /**
     * Importo delle rate aperte per l'entità.
     */
    private BigDecimal importoRateAperte;

    private boolean stampaPrezzi;

    private CodiceIva codiceIvaAlternativo;

    private ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo;

    private Integer idZonaGeografica;

    /**
     * Dati sede entità.
     */
    private String valuta;

    /**
     * Dati sede pagamento.
     */
    private CodicePagamento codicePagamento;

    private VettoreLite vettore;

    /**
     * Dati sede magazzino.
     */
    private boolean inserimentoBloccato;

    private Date dataScadenzaDichiarazioneIntento;

    private SedeEntita sedeVettore;

    private BigDecimal importoDocumentiAperti;

    /**
     * Costruttore.
     * 
     * @param sedeMagazzino
     *            sedeMagazzino wrappata
     * @param rapportiBancari
     *            rapporto Bancario wrappato
     * @param sedePagamento
     *            sedePagamento wrappato
     * @param importoRateAperte
     *            importo delle rate non ancora pagate. E' null (non viene calcolato )se l'entità ha un fido == 0
     * @param importoDocumentiAperti
     *            importo dei documenti ancora da chiudere (ad esempio bolle non fatturate)
     * @param sedeEntita
     *            sede dell'entità
     * @param sedeVettore
     *            sede principale del vettore
     */
    public SedeAreaMagazzinoDTO(final SedeMagazzino sedeMagazzino,
            final List<RapportoBancarioSedeEntita> rapportiBancari, final SedePagamento sedePagamento,
            final BigDecimal importoRateAperte, final BigDecimal importoDocumentiAperti, final SedeEntita sedeEntita,
            final SedeEntita sedeVettore) {

        this.importoRateAperte = importoRateAperte;
        this.importoDocumentiAperti = importoDocumentiAperti;

        // dati da sedeMagazzino
        if (sedeMagazzino != null) {
            this.listino = sedeMagazzino.getListino();
            this.listinoAlternativo = sedeMagazzino.getListinoAlternativo();
            this.codiceValuta = sedeMagazzino.getCodiceValuta();
            this.calcoloSpese = sedeMagazzino.isCalcoloSpese();
            this.raggruppamentoBolle = sedeMagazzino.isRaggruppamentoBolle();
            if (sedeMagazzino.getCausaleTrasporto() != null) {
                this.causaleTrasporto = sedeMagazzino.getCausaleTrasporto().getDescrizione();
            } else {
                this.causaleTrasporto = null;
            }
            if (sedeMagazzino.getTrasportoCura() != null) {
                this.trasportoCura = sedeMagazzino.getTrasportoCura().getDescrizione();
            } else {
                this.trasportoCura = null;
            }
            if (sedeMagazzino.getTipoPorto() != null) {
                this.tipoPorto = sedeMagazzino.getTipoPorto().getDescrizione();
            }
            if (sedeMagazzino.getAspettoEsteriore() != null) {
                this.aspettoEsteriore = sedeMagazzino.getAspettoEsteriore().getDescrizione();
            }
            if (sedeEntita.getZonaGeografica() != null) {
                this.idZonaGeografica = sedeEntita.getZonaGeografica().getId();
            }
            this.agente = sedeEntita.getAgente();
            this.inserimentoBloccato = sedeEntita.getBloccoSede().isBlocco()
                    || sedeEntita.getEntita().getBloccoSede().isBlocco();
            if (sedeEntita.getVettore() != null) {
                this.vettore = sedeEntita.getVettore();
                this.sedeVettore = sedeVettore;
            }
            this.stampaPrezzi = sedeMagazzino.isStampaPrezzo();
            this.codiceIvaAlternativo = sedeMagazzino.getCodiceIvaAlternativo();
            this.tipologiaCodiceIvaAlternativo = sedeMagazzino.getTipologiaCodiceIvaAlternativo();
            if (sedeMagazzino.getDichiarazioneIntento() != null) {
                this.dataScadenzaDichiarazioneIntento = sedeMagazzino.getDichiarazioneIntento().getDataScadenza();
            }
        } else {
            this.listino = null;
            this.listinoAlternativo = null;
            this.causaleTrasporto = null;
            this.trasportoCura = null;
            this.tipoPorto = null;
            this.aspettoEsteriore = null;
            this.idZonaGeografica = null;
            this.stampaPrezzi = false;
            this.codiceIvaAlternativo = null;
            this.tipologiaCodiceIvaAlternativo = ETipologiaCodiceIvaAlternativo.NESSUNO;
            this.dataScadenzaDichiarazioneIntento = null;
        }

        this.rapportiBancari = rapportiBancari;
        if (sedePagamento != null) {
            this.codicePagamento = sedePagamento.getCodicePagamento();
        }
    }

    /**
     * @return the agente
     */
    public AgenteLite getAgente() {
        return agente;
    }

    /**
     * @return the aspettoEsteriore
     */
    public String getAspettoEsteriore() {
        return aspettoEsteriore;
    }

    /**
     * @return the categoriaSedeMagazzino
     */
    public String getCategoriaSedeMagazzino() {
        return categoriaSedeMagazzino;
    }

    /**
     * @return the causaleTrasporto
     */
    public String getCausaleTrasporto() {
        return causaleTrasporto;
    }

    /**
     * @return the codiceIvaAlternativo
     */
    public CodiceIva getCodiceIvaAlternativo() {
        return codiceIvaAlternativo;
    }

    /**
     * @return Returns the codicePagamento.
     */
    public CodicePagamento getCodicePagamento() {
        return codicePagamento;
    }

    /**
     * @return the codiceValuta
     */
    public String getCodiceValuta() {
        return codiceValuta;
    }

    /**
     * @return Returns the dataScadenzaDichiarazioneIntento.
     */
    public Date getDataScadenzaDichiarazioneIntento() {
        return dataScadenzaDichiarazioneIntento;
    }

    /**
     * @return the idZonaGeografica
     */
    public Integer getIdZonaGeografica() {
        return idZonaGeografica;
    }

    /**
     * @return Returns the importoDocumentiAperti.
     */
    public BigDecimal getImportoDocumentiAperti() {
        return importoDocumentiAperti;
    }

    /**
     * @return the importoRateAperte
     */
    public BigDecimal getImportoRateAperte() {
        return importoRateAperte;
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
     * @return the rapportiBancari
     */
    public List<RapportoBancarioSedeEntita> getRapportiBancari() {
        return rapportiBancari;
    }

    /**
     * @return Returns the sedeVettore.
     */
    public SedeEntita getSedeVettore() {
        return sedeVettore;
    }

    /**
     * @return the tipologiaCodiceIvaAlternativo
     */
    public ETipologiaCodiceIvaAlternativo getTipologiaCodiceIvaAlternativo() {
        return tipologiaCodiceIvaAlternativo;
    }

    /**
     * @return the tipoPorto
     */
    public String getTipoPorto() {
        return tipoPorto;
    }

    /**
     * @return the trasportoCura
     */
    public String getTrasportoCura() {
        return trasportoCura;
    }

    /**
     * @return the valuta
     */
    public String getValuta() {
        return valuta;
    }

    /**
     * @return the vettore
     */
    public VettoreLite getVettore() {
        return vettore;
    }

    /**
     * @return the calcoloSpese
     */
    public boolean isCalcoloSpese() {
        return calcoloSpese;
    }

    /**
     * @return inserimentoSedeBloccato da anagrafica
     */
    public boolean isInserimentoBloccato() {
        return inserimentoBloccato;
    }

    /**
     * @return the raggruppamentoBolle
     */
    public boolean isRaggruppamentoBolle() {
        return raggruppamentoBolle;
    }

    /**
     * @return the stampaPrezzi
     */
    public boolean isStampaPrezzi() {
        return stampaPrezzi;
    }

    /**
     * @param aspettoEsteriore
     *            the aspettoEsteriore to set
     */
    public void setAspettoEsteriore(String aspettoEsteriore) {
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
     * @param categoriaSedeMagazzino
     *            the categoriaSedeMagazzino to set
     */
    public void setCategoriaSedeMagazzino(String categoriaSedeMagazzino) {
        this.categoriaSedeMagazzino = categoriaSedeMagazzino;
    }

    /**
     * @param causaleTrasporto
     *            the causaleTrasporto to set
     */
    public void setCausaleTrasporto(String causaleTrasporto) {
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
     * @param codicePagamento
     *            The codicePagamento to set.
     */
    public void setCodicePagamento(CodicePagamento codicePagamento) {
        this.codicePagamento = codicePagamento;
    }

    /**
     * @param codiceValuta
     *            the codiceValuta to set
     */
    public void setCodiceValuta(String codiceValuta) {
        this.codiceValuta = codiceValuta;
    }

    /**
     * @param importoRateAperte
     *            the importoRateAperte to set
     */
    public void setImportoRateAperte(BigDecimal importoRateAperte) {
        this.importoRateAperte = importoRateAperte;
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
     * @param raggruppamentoBolle
     *            the raggruppamentoBolle to set
     */
    public void setRaggruppamentoBolle(boolean raggruppamentoBolle) {
        this.raggruppamentoBolle = raggruppamentoBolle;
    }

    /**
     * @param rapportiBancari
     *            the rapportiBancari to set
     */
    public void setRapportiBancari(List<RapportoBancarioSedeEntita> rapportiBancari) {
        this.rapportiBancari = rapportiBancari;
    }

    /**
     * @param stampaPrezzi
     *            the stampaPrezzi to set
     */
    public void setStampaPrezzi(boolean stampaPrezzi) {
        this.stampaPrezzi = stampaPrezzi;
    }

    /**
     * @param tipologiaCodiceIvaAlternativo
     *            the tipologiaCodiceIvaAlternativo to set
     */
    public void setTipologiaCodiceIvaAlternativo(ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo) {
        this.tipologiaCodiceIvaAlternativo = tipologiaCodiceIvaAlternativo;
    }

    /**
     * @param tipoPorto
     *            the tipoPorto to set
     */
    public void setTipoPorto(String tipoPorto) {
        this.tipoPorto = tipoPorto;
    }

    /**
     * @param trasportoCura
     *            the trasportoCura to set
     */
    public void setTrasportoCura(String trasportoCura) {
        this.trasportoCura = trasportoCura;
    }

    /**
     * @param valuta
     *            the valuta to set
     */
    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    /**
     * @param vettore
     *            the vettore to set
     */
    public void setVettore(VettoreLite vettore) {
        this.vettore = vettore;
    }
}
