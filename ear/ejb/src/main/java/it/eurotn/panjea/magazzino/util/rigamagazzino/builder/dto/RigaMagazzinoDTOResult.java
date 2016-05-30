package it.eurotn.panjea.magazzino.util.rigamagazzino.builder.dto;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;

/**
 * Builder per generare una specifica classe derivata da RigaMagazzino a seconda del tipoRiga.
 *
 * @author leonardo
 */
public class RigaMagazzinoDTOResult {

    private Integer id;
    private Integer idAreaMagazzino;
    private String tipoRiga;
    private Integer idArticolo;
    private String codiceArticolo;
    private String codiceArticoloEntita;
    private String descrizioneArticolo;
    private String barcodeArticolo;
    private Integer idArticoloPadre;
    private String codiceArticoloPadre;
    private String codiceArticoloPadreEntita;
    private String descrizioneArticoloPadre;
    private String descrizioneRiga;
    private String codiceValutaPrezzoUnitario;
    private Integer numeroDecimaliPrezzo;
    private Integer numeroDecimaliQta;
    private BigDecimal importoInValutaPrezzoUnitario;
    private BigDecimal importoInValutaAziendaPrezzoUnitario;
    private BigDecimal importoInValutaUnitarioImposta;
    private BigDecimal importoInValutaAziendaUnitarioImposta;
    private BigDecimal tassoDiCambioPrezzoUnitario;
    private Double qtaRiga;
    private String codiceValutaPrezzoNetto;
    private BigDecimal importoInValutaPrezzoNetto;
    private BigDecimal importoInValutaAziendaPrezzoNetto;
    private BigDecimal tassoDiCambioPrezzoNetto;
    private String codiceValutaPrezzoTotale;
    private BigDecimal importoInValutaPrezzoTotale;
    private BigDecimal importoInValutaAziendaPrezzoTotale;
    private BigDecimal tassoDiCambioPrezzoTotale;
    private double qtaChiusa;
    private String rigaNota;
    private String noteRiga;
    private Boolean ivata;
    private StrategiaTotalizzazioneDocumento strategiaTotalizzazioneDocumento;

    private boolean rigaAutomatica;

    private BigDecimal variazione1;

    private BigDecimal variazione2;

    private BigDecimal variazione3;
    private BigDecimal variazione4;
    private Integer livello;
    private String codiceTipoDocumentoCollegato;
    private Integer idAreaMagazzinoCollegata;
    private Integer idAreaOrdineCollegata;

    private Integer idCodiceIva;
    private String codiceCodiceIva;
    private BigDecimal percApplicazioneCodiceIva;

    /**
     * Default constructor.
     */
    public RigaMagazzinoDTOResult() {

    }

    /**
     * @return the barcodeArticolo
     */
    public String getBarcodeArticolo() {
        return barcodeArticolo;
    }

    /**
     * @return the codiceArticolo
     */
    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    /**
     * @return the codiceArticoloEntita
     */
    public String getCodiceArticoloEntita() {
        return codiceArticoloEntita;
    }

    /**
     * @return the codiceArticoloPadre
     */
    public String getCodiceArticoloPadre() {
        return codiceArticoloPadre;
    }

    /**
     * @return Returns the codiceArticoloPadreEntita.
     */
    public String getCodiceArticoloPadreEntita() {
        return codiceArticoloPadreEntita;
    }

    /**
     * @return Returns the codiceCodiceIva.
     */
    public String getCodiceCodiceIva() {
        return codiceCodiceIva;
    }

    /**
     * @return the codiceTipoDocumentoCollegato
     */
    public String getCodiceTipoDocumentoCollegato() {
        return codiceTipoDocumentoCollegato;
    }

    /**
     * @return the codiceValutaPrezzoNetto
     */
    public String getCodiceValutaPrezzoNetto() {
        return codiceValutaPrezzoNetto;
    }

    /**
     * @return the codiceValutaPrezzoTotale
     */
    public String getCodiceValutaPrezzoTotale() {
        return codiceValutaPrezzoTotale;
    }

    /**
     * @return the codiceValutaPrezzoUnitario
     */
    public String getCodiceValutaPrezzoUnitario() {
        return codiceValutaPrezzoUnitario;
    }

    /**
     * @return the descrizioneArticolo
     */
    public String getDescrizioneArticolo() {
        return descrizioneArticolo;
    }

    /**
     * @return the descrizioneArticoloPadre
     */
    public String getDescrizioneArticoloPadre() {
        return descrizioneArticoloPadre;
    }

    /**
     * @return the descrizioneRiga
     */
    public String getDescrizioneRiga() {
        return descrizioneRiga;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the idAreaCollegata
     */
    public Integer getIdAreaCollegata() {
        if (idAreaMagazzinoCollegata != null) {
            return idAreaMagazzinoCollegata;
        }

        return idAreaOrdineCollegata;
    }

    /**
     * @return the idAreaMagazzino
     */
    public Integer getIdAreaMagazzino() {
        return idAreaMagazzino;
    }

    /**
     * @return Returns the idAreaMagazzinoCollegata.
     */
    public Integer getIdAreaMagazzinoCollegata() {
        return idAreaMagazzinoCollegata;
    }

    /**
     * @return Returns the idAreaOrdineCollegata.
     */
    public Integer getIdAreaOrdineCollegata() {
        return idAreaOrdineCollegata;
    }

    /**
     * @return the idArticolo
     */
    public Integer getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return the idArticoloPadre
     */
    public Integer getIdArticoloPadre() {
        return idArticoloPadre;
    }

    /**
     * @return Returns the idCodiceIva.
     */
    public Integer getIdCodiceIva() {
        return idCodiceIva;
    }

    /**
     * @return the importoInValutaAziendaPrezzoNetto
     */
    public BigDecimal getImportoInValutaAziendaPrezzoNetto() {
        return importoInValutaAziendaPrezzoNetto;
    }

    /**
     * @return the importoInValutaAziendaPrezzoTotale
     */
    public BigDecimal getImportoInValutaAziendaPrezzoTotale() {
        return importoInValutaAziendaPrezzoTotale;
    }

    /**
     * @return the importoInValutaAziendaPrezzoUnitario
     */
    public BigDecimal getImportoInValutaAziendaPrezzoUnitario() {
        return importoInValutaAziendaPrezzoUnitario;
    }

    /**
     * @return the importoInValutaAziendaUnitarioImposta
     */
    public BigDecimal getImportoInValutaAziendaUnitarioImposta() {
        return importoInValutaAziendaUnitarioImposta;
    }

    /**
     * @return the importoInValutaPrezzoNetto
     */
    public BigDecimal getImportoInValutaPrezzoNetto() {
        return importoInValutaPrezzoNetto;
    }

    /**
     * @return the importoInValutaPrezzoTotale
     */
    public BigDecimal getImportoInValutaPrezzoTotale() {
        return importoInValutaPrezzoTotale;
    }

    /**
     * @return the importoInValutaPrezzoUnitario
     */
    public BigDecimal getImportoInValutaPrezzoUnitario() {
        return importoInValutaPrezzoUnitario;
    }

    /**
     * @return the importoInValutaUnitarioImposta
     */
    public BigDecimal getImportoInValutaUnitarioImposta() {
        return importoInValutaUnitarioImposta;
    }

    /**
     * @return the livello
     */
    public Integer getLivello() {
        return livello;
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
     * @return Returns the numeroDecimaliQta.
     */
    public Integer getNumeroDecimaliQta() {
        return numeroDecimaliQta;
    }

    /**
     * @return Returns the percApplicazioneCodiceIva.
     */
    public BigDecimal getPercApplicazioneCodiceIva() {
        return percApplicazioneCodiceIva;
    }

    /**
     * @return the qtaChiusa
     */
    public double getQtaChiusa() {
        return qtaChiusa;
    }

    /**
     * @return the qtaRiga
     */
    public Double getQtaRiga() {
        return qtaRiga;
    }

    /**
     * @return the rigaNota
     */
    public String getRigaNota() {
        return rigaNota;
    }

    /**
     * @return the strategiaTotalizzazioneDocumento
     */
    public StrategiaTotalizzazioneDocumento getStrategiaTotalizzazioneDocumento() {
        return strategiaTotalizzazioneDocumento;
    }

    /**
     * @return the tassoDiCambioPrezzoNetto
     */
    public BigDecimal getTassoDiCambioPrezzoNetto() {
        return tassoDiCambioPrezzoNetto;
    }

    /**
     * @return the tassoDiCambioPrezzoTotale
     */
    public BigDecimal getTassoDiCambioPrezzoTotale() {
        return tassoDiCambioPrezzoTotale;
    }

    /**
     * @return the tassoDiCambioPrezzoUnitario
     */
    public BigDecimal getTassoDiCambioPrezzoUnitario() {
        return tassoDiCambioPrezzoUnitario;
    }

    /**
     * @return the tipoRiga
     */
    public String getTipoRiga() {
        return tipoRiga;
    }

    /**
     * @return Returns the variazione1.
     */
    public BigDecimal getVariazione1() {
        return variazione1;
    }

    /**
     * @return Returns the variazione2.
     */
    public BigDecimal getVariazione2() {
        return variazione2;
    }

    /**
     * @return Returns the variazione3.
     */
    public BigDecimal getVariazione3() {
        return variazione3;
    }

    /**
     * @return Returns the variazione4.
     */
    public BigDecimal getVariazione4() {
        return variazione4;
    }

    /**
     * @return the ivata
     */
    public Boolean isIvata() {
        return ivata;
    }

    /**
     * @return Returns the rigaAutomatica.
     */
    public boolean isRigaAutomatica() {
        return rigaAutomatica;
    }

    /**
     * @param barcodeArticolo
     *            the barcodeArticolo to set
     */
    public void setBarcodeArticolo(String barcodeArticolo) {
        this.barcodeArticolo = barcodeArticolo;
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    /**
     * @param codiceArticoloEntita
     *            the codiceArticoloEntita to set
     */
    public void setCodiceArticoloEntita(String codiceArticoloEntita) {
        this.codiceArticoloEntita = codiceArticoloEntita;
    }

    /**
     * @param codiceArticoloPadre
     *            the codiceArticoloPadre to set
     */
    public void setCodiceArticoloPadre(String codiceArticoloPadre) {
        this.codiceArticoloPadre = codiceArticoloPadre;
    }

    /**
     * @param codiceArticoloPadreEntita
     *            The codiceArticoloPadreEntita to set.
     */
    public void setCodiceArticoloPadreEntita(String codiceArticoloPadreEntita) {
        this.codiceArticoloPadreEntita = codiceArticoloPadreEntita;
    }

    /**
     * @param codiceCodiceIva
     *            The codiceCodiceIva to set.
     */
    public void setCodiceCodiceIva(String codiceCodiceIva) {
        this.codiceCodiceIva = codiceCodiceIva;
    }

    /**
     * @param codiceTipoDocumentoCollegato
     *            the codiceTipoDocumentoCollegato to set
     */
    public void setCodiceTipoDocumentoCollegato(String codiceTipoDocumentoCollegato) {
        this.codiceTipoDocumentoCollegato = codiceTipoDocumentoCollegato;
    }

    /**
     * @param codiceValutaPrezzoNetto
     *            the codiceValutaPrezzoNetto to set
     */
    public void setCodiceValutaPrezzoNetto(String codiceValutaPrezzoNetto) {
        this.codiceValutaPrezzoNetto = codiceValutaPrezzoNetto;
    }

    /**
     * @param codiceValutaPrezzoTotale
     *            the codiceValutaPrezzoTotale to set
     */
    public void setCodiceValutaPrezzoTotale(String codiceValutaPrezzoTotale) {
        this.codiceValutaPrezzoTotale = codiceValutaPrezzoTotale;
    }

    /**
     * @param codiceValutaPrezzoUnitario
     *            the codiceValutaPrezzoUnitario to set
     */
    public void setCodiceValutaPrezzoUnitario(String codiceValutaPrezzoUnitario) {
        this.codiceValutaPrezzoUnitario = codiceValutaPrezzoUnitario;
    }

    /**
     * @param descrizioneArticolo
     *            the descrizioneArticolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.descrizioneArticolo = descrizioneArticolo;
    }

    /**
     * @param descrizioneArticoloPadre
     *            the descrizioneArticoloPadre to set
     */
    public void setDescrizioneArticoloPadre(String descrizioneArticoloPadre) {
        this.descrizioneArticoloPadre = descrizioneArticoloPadre;
    }

    /**
     * @param descrizioneRiga
     *            the descrizioneRiga to set
     */
    public void setDescrizioneRiga(String descrizioneRiga) {
        this.descrizioneRiga = descrizioneRiga;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param idAreaMagazzino
     *            the idAreaMagazzino to set
     */
    public void setIdAreaMagazzino(Integer idAreaMagazzino) {
        this.idAreaMagazzino = idAreaMagazzino;
    }

    /**
     * @param idAreaMagazzinoCollegata
     *            the idAreaMagazzinoCollegata to set
     */
    public void setIdAreaMagazzinoCollegata(Integer idAreaMagazzinoCollegata) {
        this.idAreaMagazzinoCollegata = idAreaMagazzinoCollegata;
    }

    /**
     * @param idAreaOrdineCollegata
     *            the idAreaOrdineCollegata to set
     */
    public void setIdAreaOrdineCollegata(Integer idAreaOrdineCollegata) {
        this.idAreaOrdineCollegata = idAreaOrdineCollegata;
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param idArticoloPadre
     *            the idArticoloPadre to set
     */
    public void setIdArticoloPadre(Integer idArticoloPadre) {
        this.idArticoloPadre = idArticoloPadre;
    }

    /**
     * @param idCodiceIva
     *            The idCodiceIva to set.
     */
    public void setIdCodiceIva(Integer idCodiceIva) {
        this.idCodiceIva = idCodiceIva;
    }

    /**
     * @param importoInValutaAziendaPrezzoNetto
     *            the importoInValutaAziendaPrezzoNetto to set
     */
    public void setImportoInValutaAziendaPrezzoNetto(BigDecimal importoInValutaAziendaPrezzoNetto) {
        this.importoInValutaAziendaPrezzoNetto = importoInValutaAziendaPrezzoNetto;
    }

    /**
     * @param importoInValutaAziendaPrezzoTotale
     *            the importoInValutaAziendaPrezzoTotale to set
     */
    public void setImportoInValutaAziendaPrezzoTotale(BigDecimal importoInValutaAziendaPrezzoTotale) {
        this.importoInValutaAziendaPrezzoTotale = importoInValutaAziendaPrezzoTotale;
    }

    /**
     * @param importoInValutaAziendaPrezzoUnitario
     *            the importoInValutaAziendaPrezzoUnitario to set
     */
    public void setImportoInValutaAziendaPrezzoUnitario(BigDecimal importoInValutaAziendaPrezzoUnitario) {
        this.importoInValutaAziendaPrezzoUnitario = importoInValutaAziendaPrezzoUnitario;
    }

    /**
     * @param importoInValutaAziendaUnitarioImposta
     *            the importoInValutaAziendaUnitarioImposta to set
     */
    public void setImportoInValutaAziendaUnitarioImposta(BigDecimal importoInValutaAziendaUnitarioImposta) {
        this.importoInValutaAziendaUnitarioImposta = importoInValutaAziendaUnitarioImposta;
    }

    /**
     * @param importoInValutaPrezzoNetto
     *            the importoInValutaPrezzoNetto to set
     */
    public void setImportoInValutaPrezzoNetto(BigDecimal importoInValutaPrezzoNetto) {
        this.importoInValutaPrezzoNetto = importoInValutaPrezzoNetto;
    }

    /**
     * @param importoInValutaPrezzoTotale
     *            the importoInValutaPrezzoTotale to set
     */
    public void setImportoInValutaPrezzoTotale(BigDecimal importoInValutaPrezzoTotale) {
        this.importoInValutaPrezzoTotale = importoInValutaPrezzoTotale;
    }

    /**
     * @param importoInValutaPrezzoUnitario
     *            the importoInValutaPrezzoUnitario to set
     */
    public void setImportoInValutaPrezzoUnitario(BigDecimal importoInValutaPrezzoUnitario) {
        this.importoInValutaPrezzoUnitario = importoInValutaPrezzoUnitario;
    }

    /**
     * @param importoInValutaUnitarioImposta
     *            the importoInValutaUnitarioImposta to set
     */
    public void setImportoInValutaUnitarioImposta(BigDecimal importoInValutaUnitarioImposta) {
        this.importoInValutaUnitarioImposta = importoInValutaUnitarioImposta;
    }

    /**
     * @param ivata
     *            the ivata to set
     */
    public void setIvata(Boolean ivata) {
        this.ivata = ObjectUtils.defaultIfNull(ivata, Boolean.FALSE);
    }

    /**
     * @param livello
     *            the livello to set
     */
    public void setLivello(Integer livello) {
        this.livello = livello;
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
     * @param numeroDecimaliQta
     *            The numeroDecimaliQta to set.
     */
    public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
        this.numeroDecimaliQta = numeroDecimaliQta;
    }

    /**
     * @param percApplicazioneCodiceIva
     *            The percApplicazioneCodiceIva to set.
     */
    public void setPercApplicazioneCodiceIva(BigDecimal percApplicazioneCodiceIva) {
        this.percApplicazioneCodiceIva = percApplicazioneCodiceIva;
    }

    /**
     * @param qtaChiusa
     *            the qtaChiusa to set
     */
    public void setQtaChiusa(double qtaChiusa) {
        this.qtaChiusa = qtaChiusa;
    }

    /**
     * @param qtaRiga
     *            the qtaRiga to set
     */
    public void setQtaRiga(Double qtaRiga) {
        this.qtaRiga = qtaRiga;
    }

    /**
     * @param rigaAutomatica
     *            The rigaAutomatica to set.
     */
    public void setRigaAutomatica(boolean rigaAutomatica) {
        this.rigaAutomatica = rigaAutomatica;
    }

    /**
     * @param rigaNota
     *            the rigaNota to set
     */
    public void setRigaNota(String rigaNota) {
        this.rigaNota = rigaNota;
    }

    /**
     * @param index
     *            the strategiaTotalizzazioneDocumento to set
     */
    public void setStrategiaTotalizzazioneDocumento(Integer index) {

        Integer strategiaIdx = ObjectUtils.defaultIfNull(index, 0);
        this.strategiaTotalizzazioneDocumento = StrategiaTotalizzazioneDocumento.values()[strategiaIdx];
    }

    /**
     * @param tassoDiCambioPrezzoNetto
     *            the tassoDiCambioPrezzoNetto to set
     */
    public void setTassoDiCambioPrezzoNetto(BigDecimal tassoDiCambioPrezzoNetto) {
        this.tassoDiCambioPrezzoNetto = tassoDiCambioPrezzoNetto;
    }

    /**
     * @param tassoDiCambioPrezzoTotale
     *            the tassoDiCambioPrezzoTotale to set
     */
    public void setTassoDiCambioPrezzoTotale(BigDecimal tassoDiCambioPrezzoTotale) {
        this.tassoDiCambioPrezzoTotale = tassoDiCambioPrezzoTotale;
    }

    /**
     * @param tassoDiCambioPrezzoUnitario
     *            the tassoDiCambioPrezzoUnitario to set
     */
    public void setTassoDiCambioPrezzoUnitario(BigDecimal tassoDiCambioPrezzoUnitario) {
        this.tassoDiCambioPrezzoUnitario = tassoDiCambioPrezzoUnitario;
    }

    /**
     * @param tipoRiga
     *            the tipoRiga to set
     */
    public void setTipoRiga(String tipoRiga) {
        this.tipoRiga = tipoRiga;
    }

    /**
     * @param variazione1
     *            The variazione1 to set.
     */
    public void setVariazione1(BigDecimal variazione1) {
        this.variazione1 = variazione1;
    }

    /**
     * @param variazione2
     *            The variazione2 to set.
     */
    public void setVariazione2(BigDecimal variazione2) {
        this.variazione2 = variazione2;
    }

    /**
     * @param variazione3
     *            The variazione3 to set.
     */
    public void setVariazione3(BigDecimal variazione3) {
        this.variazione3 = variazione3;
    }

    /**
     * @param variazione4
     *            The variazione4 to set.
     */
    public void setVariazione4(BigDecimal variazione4) {
        this.variazione4 = variazione4;
    }

    /**
     * Restituisce una {@link RigaMagazzinoDTO} specifica a seconda del tipo riga o lancia una
     * {@link IllegalArgumentException} se il tipoRiga e' sconosciuto.
     *
     * @return {@link RigaMagazzinoDTO}
     */
    public RigaMagazzinoDTO toRigaMagazzinoDTO() {
        throw new IllegalArgumentException("Tipo riga (" + tipoRiga + ") non previsto!");
    }
}
