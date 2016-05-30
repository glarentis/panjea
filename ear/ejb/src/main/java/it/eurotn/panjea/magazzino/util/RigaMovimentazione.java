package it.eurotn.panjea.magazzino.util;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;

/**
 * Espone una riga di movimentazione.<br/.> Rispetto alla giacenza Articolo ha dei dati in più utili per visualizzare
 * nella movimentazione e la giacenza corrisponde alla giacenaza progessiva calcolata in base alle righe della
 * movimentazione.
 */
public class RigaMovimentazione extends GiacenzaArticolo {

    private static final long serialVersionUID = 5328893568053282519L;

    private TipoOmaggio tipoOmaggio;

    private Integer areaMagazzinoId;

    private Date dataRegistrazione;

    private BigDecimal prezzoUnitario;

    private BigDecimal prezzoNetto;

    private BigDecimal prezzoTotale;

    private BigDecimal importoCaricoTotale;

    private BigDecimal importoScaricoTotale;
    private BigDecimal importoFatturatoCarico;
    private BigDecimal importoFatturatoScarico;
    private BigDecimal importoProvvigione;
    private String descrizioneRiga;
    private Double giacenzaProgressiva;

    private Integer numeroDecimaliPrezzo;

    private Integer numeroDecimaliQuantita;

    private String noteRiga;

    private EntitaDocumento entitaDocumento;

    private SedeEntita sedeEntita;

    private Documento documento;
    private Sconto variazione;

    private Integer tipoOperazione;

    private AziendaLite azienda;

    /**
     * Costruttore di default.
     */
    public RigaMovimentazione() {
        super();
        initialize();
    }

    /**
     * Da una riga magazzino creo una rigaMovimentazione. Utile per una riga sull'inventario.<br/>
     * <b>NB:</b>La qta iniziale rimane 0.
     *
     * @param rigaArticolo
     *            rigaArticolo che genera la movimentazione
     */
    public RigaMovimentazione(final RigaArticolo rigaArticolo) {
        super();
        initialize();
        areaMagazzinoId = rigaArticolo.getAreaMagazzino().getId();
        setCodiceArticolo(rigaArticolo.getArticolo().getCodice());
        setCodiceCategoria(rigaArticolo.getArticolo().getCategoria().getCodice());
        setCodiceDeposito(rigaArticolo.getAreaMagazzino().getDepositoOrigine().getCodice());
        if (rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita() != null) {
            setIdEntita(rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita().getEntita().getId());
            setCodiceEntita(rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita().getEntita().getCodice());
            setDescrizioneEntita(rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita().getEntita()
                    .getAnagrafica().getDenominazione());
            if (rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita().getEntita() instanceof Fornitore) {
                setTipoEntita("F");
            } else {
                if (rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita().getEntita() instanceof Cliente) {
                    setTipoEntita("C");
                } else {
                    setTipoEntita(null);
                }
            }
            setIdSedeEntita(rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita().getId());
            setCodiceSedeEntita(rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita().getCodice());
            setDescrizioneSedeEntita(
                    rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita().getSede().getDescrizione());
        }
        setIdTipoDocumento(rigaArticolo.getAreaMagazzino().getDocumento().getTipoDocumento().getId());
        setNumeroDocumento(rigaArticolo.getAreaMagazzino().getDocumento().getCodice().getCodice());
        setNumeroDocumentoOrder(rigaArticolo.getAreaMagazzino().getDocumento().getCodice().getCodiceOrder());
        setCodiceTipoDocumento(rigaArticolo.getAreaMagazzino().getTipoAreaMagazzino().getTipoDocumento().getCodice());
        setDataDocumento(rigaArticolo.getAreaMagazzino().getDocumento().getDataDocumento());
        dataRegistrazione = rigaArticolo.getAreaMagazzino().getDataRegistrazione();
        setDescrizioneArticolo(rigaArticolo.getArticolo().getDescrizione());
        setDescrizioneCategoria(rigaArticolo.getArticolo().getCategoria().getDescrizione());
        setDescrizioneDeposito(rigaArticolo.getAreaMagazzino().getDepositoOrigine().getDescrizione());
        setDescrizioneTipoDocumento(
                rigaArticolo.getAreaMagazzino().getTipoAreaMagazzino().getTipoDocumento().getDescrizione());
        qtaMagazzinoCaricoTotale = rigaArticolo.getQtaMagazzino();

        numeroDecimaliPrezzo = rigaArticolo.getNumeroDecimaliPrezzo();
        numeroDecimaliQuantita = rigaArticolo.getNumeroDecimaliQta();
        noteRiga = rigaArticolo.getNoteRiga();
        tipoOperazione = rigaArticolo.getAreaMagazzino().getTipoOperazione();
        setPrezzoNetto(rigaArticolo.getPrezzoNetto().getImportoInValutaAzienda());
        setPrezzoUnitario(rigaArticolo.getPrezzoUnitario().getImportoInValutaAzienda());
        setPrezzoTotale(rigaArticolo.getPrezzoTotale().getImportoInValutaAzienda());
    }

    /**
     * @return the areaMagazzinoId
     */
    public Integer getAreaMagazzinoId() {
        return areaMagazzinoId;
    }

    /**
     * @return the codiceEntita
     */
    public Integer getCodiceEntita() {
        return entitaDocumento.getCodice();
    }

    /**
     * @return the dataRegistrazione
     */
    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * @return the descrizioneEntita
     */
    public String getDescrizioneEntita() {
        return entitaDocumento.getDescrizione();
    }

    /**
     * @return Returns the descrizioneRiga.
     */
    public String getDescrizioneRiga() {
        return descrizioneRiga;
    }

    /**
     * @return the documento
     */
    public Documento getDocumento() {
        return documento;
    }

    /**
     * @return the entitaDocumento
     */
    public EntitaDocumento getEntitaDocumento() {
        if (entitaDocumento == null || entitaDocumento.getTipoEntita() == null) {
            entitaDocumento = new EntitaDocumento();
            entitaDocumento.setTipoEntita(TipoEntita.AZIENDA);
            entitaDocumento.setId(azienda.getId());
            entitaDocumento.setCodice(null);
            entitaDocumento.setDescrizione(azienda.getDenominazione());
        }
        return entitaDocumento;
    }

    /**
     * @return the giacenzaProgressiva
     */
    public Double getGiacenzaProgressiva() {
        return giacenzaProgressiva;
    }

    /**
     * @return importo di carico (se non movimenta merce l'importo fatturato).
     */
    public BigDecimal getImportoCarico() {
        if (importoCaricoTotale.compareTo(BigDecimal.ZERO) == 0) {
            return importoFatturatoCarico;
        } else {
            return importoCaricoTotale;
        }
    }

    /**
     * @return PrezzoMedio * qtaMovimentata. <b>NB:</b>Per la singola riga corrisponde al totale riga
     */
    public BigDecimal getImportoMovimentatoCostoMedio() {
        if (getPrezzoMedio() != null) {
            return getPrezzoMedio().multiply(BigDecimal.valueOf(getQtaMovimentata()));
        } else {
            return null;
        }
    }

    /**
     * @return the importoProvvigione
     */
    public BigDecimal getImportoProvvigione() {
        return importoProvvigione;
    }

    /**
     * @return importo di scarico (se non movimenta merce l'importo fatturato).
     */
    public BigDecimal getImportoScarico() {
        if (importoScaricoTotale.compareTo(BigDecimal.ZERO) == 0) {
            return importoFatturatoScarico;
        } else {
            return importoScaricoTotale;
        }
    }

    /**
     * @return the noteRiga
     */
    public String getNoteRiga() {
        return noteRiga;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the numeroDecimaliQuantita
     */
    public Integer getNumeroDecimaliQuantita() {
        return numeroDecimaliQuantita;
    }

    /**
     * @return Returns the prezzoMedio.
     */
    public BigDecimal getPrezzoMedio() {
        return null;
    }

    /**
     * @return the prezzoNetto
     */
    public BigDecimal getPrezzoNetto() {
        return prezzoNetto;
    }

    /**
     * @return the prezzoTotale
     */
    public BigDecimal getPrezzoTotale() {
        return prezzoTotale;
    }

    /**
     * @return the prezzoUnitario
     */
    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return Returns the tipoOmaggio.
     */
    public TipoOmaggio getTipoOmaggio() {
        return tipoOmaggio;
    }

    /**
     * @return the tipoOperazione
     */
    public Integer getTipoOperazione() {
        return tipoOperazione;
    }

    /**
     * @return variazione.
     */
    public Sconto getVariazione() {
        return variazione;
    }

    /**
     * Init degli oggetti di this.
     */
    private void initialize() {
        entitaDocumento = new EntitaDocumento();
        documento = new Documento();
        variazione = new Sconto();
        azienda = new AziendaLite();
    }

    /**
     * @param areaMagazzinoId
     *            the areaMagazzinoId to set
     */
    public void setAreaMagazzinoId(Integer areaMagazzinoId) {
        this.areaMagazzinoId = areaMagazzinoId;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.azienda.setCodice(codiceAzienda);
    }

    /**
     * @param codiceEntita
     *            the codiceEntita to set
     */
    public void setCodiceEntita(Integer codiceEntita) {
        this.entitaDocumento.setCodice(codiceEntita);
    }

    /**
     * @param codiceSedeEntita
     *            the codiceSedeEntita to set
     */
    public void setCodiceSedeEntita(String codiceSedeEntita) {
        if (codiceSedeEntita != null) {
            this.sedeEntita.setCodice(codiceSedeEntita);
        }
    }

    /**
     * @param codiceTipoDocumento
     *            the codiceTipoDocumento to set
     */
    public void setCodiceTipoDocumento(String codiceTipoDocumento) {
        this.documento.getTipoDocumento().setCodice(codiceTipoDocumento);
    }

    /**
     * @param dataDocumento
     *            the dataDocumento to set
     */
    public void setDataDocumento(Date dataDocumento) {
        this.documento.setDataDocumento(dataDocumento);
    }

    /**
     * @param dataRegistrazione
     *            the dataRegistrazione to set
     */
    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * @param denominazioneAzienda
     *            the denominazioneAzienda to set
     */
    public void setDenominazioneAzienda(String denominazioneAzienda) {
        this.azienda.setDenominazione(denominazioneAzienda);
    }

    /**
     * @param descrizioneEntita
     *            the descrizioneEntita to set
     */
    public void setDescrizioneEntita(String descrizioneEntita) {
        this.entitaDocumento.setDescrizione(descrizioneEntita);
    }

    /**
     * @param descrizioneRiga
     *            The descrizioneRiga to set.
     */
    public void setDescrizioneRiga(String descrizioneRiga) {
        this.descrizioneRiga = descrizioneRiga;
    }

    /**
     * @param descrizioneSedeEntita
     *            the descrizioneSedeEntita to set
     */
    public void setDescrizioneSedeEntita(String descrizioneSedeEntita) {
        if (sedeEntita != null) {
            this.sedeEntita.getSede().setDescrizione(descrizioneSedeEntita);
        }
    }

    /**
     * @param descrizioneTipoDocumento
     *            the descrizioneTipoDocumento to set
     */
    public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
        this.documento.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
    }

    /**
     * @param entitaDocumento
     *            the entitaDocumento to set
     */
    public void setEntitaDocumento(EntitaDocumento entitaDocumento) {
        this.entitaDocumento = entitaDocumento;
    }

    /**
     * @param giacenzaProgressiva
     *            the giacenzaProgressiva to set
     */
    public void setGiacenzaProgressiva(Double giacenzaProgressiva) {
        this.giacenzaProgressiva = giacenzaProgressiva;
    }

    /**
     * @param idAzienda
     *            the idAzienda to set
     */
    public void setIdAzienda(Integer idAzienda) {
        this.azienda.setId(idAzienda);
    }

    /**
     * @param idDocumento
     *            the idDocumento to set
     */
    public void setIdDocumento(Integer idDocumento) {
        this.documento.setId(idDocumento);
    }

    /**
     * @param idEntita
     *            the idEntita to set
     */
    public void setIdEntita(Integer idEntita) {
        this.entitaDocumento.setId(idEntita);
    }

    /**
     * @param idSedeEntita
     *            the idSedeEntita to set
     */
    public void setIdSedeEntita(Integer idSedeEntita) {
        if (idSedeEntita != null) {
            sedeEntita = new SedeEntita();
            this.sedeEntita.setId(idSedeEntita);
        }
    }

    /**
     * @param idTipoDocumento
     *            the idTipoDocumento to set
     */
    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.documento.getTipoDocumento().setId(idTipoDocumento);
    }

    /**
     * @param importoCaricoTotale
     *            The importoCaricoTotale to set.
     */
    public void setImportoCaricoTotale(BigDecimal importoCaricoTotale) {
        this.importoCaricoTotale = importoCaricoTotale;
    }

    /**
     * @param importoFatturatoCarico
     *            The importoFatturatoCarico to set.
     */
    public void setImportoFatturatoCarico(BigDecimal importoFatturatoCarico) {
        this.importoFatturatoCarico = importoFatturatoCarico;
    }

    /**
     * @param importoFatturatoScarico
     *            The importoFatturatoScarico to set.
     */
    public void setImportoFatturatoScarico(BigDecimal importoFatturatoScarico) {
        this.importoFatturatoScarico = importoFatturatoScarico;
    }

    /**
     * @param importoProvvigione
     *            the importoProvvigione to set
     */
    public void setImportoProvvigione(BigDecimal importoProvvigione) {
        this.importoProvvigione = importoProvvigione;
    }

    /**
     * @param importoScaricoTotale
     *            The importoScaricoTotale to set.
     */
    public void setImportoScaricoTotale(BigDecimal importoScaricoTotale) {
        this.importoScaricoTotale = importoScaricoTotale;
    }

    /**
     * @param noteRiga
     *            the noteRiga to set
     */
    public void setNoteRiga(String noteRiga) {
        this.noteRiga = noteRiga;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param numeroDecimaliPrezzoArticolo
     *            The numeroDecimaliPrezzoArticolo to set.
     */
    public void setNumeroDecimaliPrezzoArticolo(Integer numeroDecimaliPrezzoArticolo) {
        this.articoloLite.setNumeroDecimaliPrezzo(ObjectUtils.defaultIfNull(numeroDecimaliPrezzoArticolo, 6));
    }

    /**
     * @param numeroDecimaliQuantita
     *            the numeroDecimaliQuantita to set
     */
    public void setNumeroDecimaliQuantita(Integer numeroDecimaliQuantita) {
        this.numeroDecimaliQuantita = numeroDecimaliQuantita;
    }

    /**
     * @param numeroDocumento
     *            the numeroDocumento to set
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.documento.getCodice().setCodice(numeroDocumento);
    }

    /**
     * @param numeroDocumentoOrder
     *            the numeroDocumentoOrder to set
     */
    public void setNumeroDocumentoOrder(String numeroDocumentoOrder) {
        this.documento.getCodice().setCodiceOrder(numeroDocumentoOrder);
    }

    /**
     * @param prezzoNetto
     *            the prezzoNetto to set
     */
    public void setPrezzoNetto(BigDecimal prezzoNetto) {
        this.prezzoNetto = prezzoNetto;
    }

    /**
     * @param prezzoTotale
     *            the prezzoTotale to set
     */
    public void setPrezzoTotale(BigDecimal prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    /**
     * @param prezzoUnitario
     *            the prezzoUnitario to set
     */
    public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    /**
     * @param tipoEntita
     *            the tipoEntita to set
     */
    public void setTipoEntita(String tipoEntita) {
        if (tipoEntita != null) {
            TipoEntita te = null;
            if ("C".equals(tipoEntita)) {
                te = TipoEntita.CLIENTE;
            } else if ("F".equals(tipoEntita)) {
                te = TipoEntita.FORNITORE;
            }
            this.entitaDocumento.setTipoEntita(te);
        }
    }

    /**
     * @param tipoOmaggio
     *            The tipoOmaggio to set.
     */
    public void setTipoOmaggio(TipoOmaggio tipoOmaggio) {
        this.tipoOmaggio = tipoOmaggio;
    }

    /**
     * @param tipoOperazione
     *            the tipoOperazione to set
     */
    public void setTipoOperazione(Integer tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }

    /**
     * @param codiceUm
     *            codice di um
     */
    public void setUm(String codiceUm) {
        this.articoloLite.getUnitaMisura().setCodice(codiceUm);
    }

    /**
     * @param umId
     *            id dell'unità di misura
     */
    public void setUmId(Integer umId) {
        this.articoloLite.getUnitaMisura().setId(umId);
    }

    /**
     * @param variazione1
     *            the variazione1 to set
     */
    public void setVariazione1(BigDecimal variazione1) {
        this.variazione.setSconto1(variazione1);
    }

    /**
     * @param variazione2
     *            the variazione2 to set
     */
    public void setVariazione2(BigDecimal variazione2) {
        this.variazione.setSconto2(variazione2);
    }

    /**
     * @param variazione3
     *            the variazione3 to set
     */
    public void setVariazione3(BigDecimal variazione3) {
        this.variazione.setSconto3(variazione3);
    }

    /**
     * @param variazione4
     *            the variazione4 to set
     */
    public void setVariazione4(BigDecimal variazione4) {
        this.variazione.setSconto4(variazione4);
    }

    @Override
    public String toString() {
        return "RigaMovimentazione [areaMagazzinoId=" + areaMagazzinoId + ", codiceEntita="
                + entitaDocumento.getCodice() + ", codiceTipoDocumento=" + documento.getTipoDocumento().getCodice()
                + ", dataDocumento=" + documento.getDataDocumento() + ", dataRegistrazione=" + dataRegistrazione
                + ", descrizioneEntita=" + entitaDocumento.getDescrizione() + ", descrizioneTipoDocumento="
                + documento.getTipoDocumento().getDescrizione() + ", numeroDocumento=" + documento.getCodice()
                + ", prezzoNetto=" + prezzoNetto + ", prezzoTotale=" + prezzoTotale + ", prezzoUnitario="
                + prezzoUnitario + ", Giacenza " + getGiacenza() + ", giacenzaProgressiva " + giacenzaProgressiva + "]";
    }

}