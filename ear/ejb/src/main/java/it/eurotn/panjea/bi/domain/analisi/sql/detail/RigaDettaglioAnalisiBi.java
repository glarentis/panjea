package it.eurotn.panjea.bi.domain.analisi.sql.detail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.util.CategoriaLite;

/**
 * @author fattazzo
 *
 */
public class RigaDettaglioAnalisiBi implements Serializable {

    private static final long serialVersionUID = 8006834223487927526L;

    private TipoOmaggio tipoOmaggio;

    private Date dataRegistrazione;

    private BigDecimal prezzoUnitario;

    private BigDecimal prezzoNetto;

    private BigDecimal prezzoTotale;

    private Integer numeroDecimaliPrezzo;

    private Integer numeroDecimaliQuantita;

    private String descrizioneRiga;

    private String noteRiga;

    private EntitaDocumento entitaDocumento;

    private SedeEntita sedeEntita;

    private Documento documento;

    private Sconto variazione;

    private ArticoloLite articoloLite = null;

    private CategoriaLite categoriaLite = null;

    private DepositoLite depositoLite = null;

    private Double qta;

    private Double qtaMagazzino;

    {
        this.articoloLite = new ArticoloLite();
        this.categoriaLite = new CategoriaLite();
        this.depositoLite = new DepositoLite();
        this.variazione = new Sconto();
        this.entitaDocumento = new EntitaDocumento();
        this.documento = new Documento();
        this.sedeEntita = new SedeEntita();
    }

    /**
     * @return the articoloLite
     */
    public ArticoloLite getArticoloLite() {
        return articoloLite;
    }

    /**
     * @return the categoriaLite
     */
    public CategoriaLite getCategoriaLite() {
        return categoriaLite;
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
     * @return the depositoLite
     */
    public DepositoLite getDepositoLite() {
        return depositoLite;
    }

    /**
     * @return the descrizioneEntita
     */
    public String getDescrizioneEntita() {
        return entitaDocumento.getDescrizione();
    }

    /**
     * @return the descrizioneRiga
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
        return entitaDocumento;
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
     * @return the qta
     */
    public Double getQta() {
        return qta;
    }

    /**
     * @return the qtaMagazzino
     */
    public Double getQtaMagazzino() {
        return qtaMagazzino;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return the tipoOmaggio
     */
    public TipoOmaggio getTipoOmaggio() {
        return tipoOmaggio;
    }

    /**
     * @return variazione.
     */
    public Sconto getVariazione() {
        return variazione;
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.articoloLite.setCodice(codiceArticolo);
    }

    /**
     * @param codiceCategoria
     *            the codiceCategoria to set
     */
    public void setCodiceCategoria(String codiceCategoria) {
        this.categoriaLite.setCodice(codiceCategoria);
    }

    /**
     * @param codiceDeposito
     *            the codiceDeposito to set
     */
    public void setCodiceDeposito(String codiceDeposito) {
        this.depositoLite.setCodice(codiceDeposito);
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
        this.sedeEntita.setCodice(codiceSedeEntita);
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
     * @param descrizioneArticolo
     *            the descrizioneArticolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.articoloLite.setDescrizione(descrizioneArticolo);
    }

    /**
     * @param descrizioneCategoria
     *            the descrizioneCategoria to set
     */
    public void setDescrizioneCategoria(String descrizioneCategoria) {
        this.categoriaLite.setDescrizione(descrizioneCategoria);
    }

    /**
     * @param descrizioneDeposito
     *            the descrizioneDeposito to set
     */
    public void setDescrizioneDeposito(String descrizioneDeposito) {
        this.depositoLite.setDescrizione(descrizioneDeposito);
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
     *            the descrizioneRiga to set
     */
    public void setDescrizioneRiga(String descrizioneRiga) {
        this.descrizioneRiga = descrizioneRiga;
    }

    /**
     * @param descrizioneSedeEntita
     *            the descrizioneSedeEntita to set
     */
    public void setDescrizioneSedeEntita(String descrizioneSedeEntita) {
        this.sedeEntita.getSede().setDescrizione(descrizioneSedeEntita);
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
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.articoloLite.setId(idArticolo);
    }

    /**
     * @param idCategoria
     *            the idCategoria to set
     */
    public void setIdCategoria(Integer idCategoria) {
        this.categoriaLite.setId(idCategoria);
    }

    /**
     * @param idDeposito
     *            the idDeposito to set
     */
    public void setIdDeposito(Integer idDeposito) {
        this.depositoLite.setId(idDeposito);
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
        this.sedeEntita.setId(idSedeEntita);
    }

    /**
     * @param idTipoDocumento
     *            the idTipoDocumento to set
     */
    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.documento.getTipoDocumento().setId(idTipoDocumento);
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
     * @param qta
     *            the qta to set
     */
    public void setQta(Double qta) {
        this.qta = qta;
    }

    /**
     * @param qtaMagazzino
     *            the qtaMagazzino to set
     */
    public void setQtaMagazzino(Double qtaMagazzino) {
        this.qtaMagazzino = qtaMagazzino;
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
     *            the tipoOmaggio to set
     */
    public void setTipoOmaggio(TipoOmaggio tipoOmaggio) {
        this.tipoOmaggio = tipoOmaggio;
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
}
