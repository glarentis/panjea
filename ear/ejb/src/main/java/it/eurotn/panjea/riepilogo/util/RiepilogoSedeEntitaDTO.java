package it.eurotn.panjea.riepilogo.util;

import java.io.Serializable;
import java.util.Date;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaGenerazioneDocumentoFatturazione;

public class RiepilogoSedeEntitaDTO implements Serializable {

    private static final long serialVersionUID = 6530287916479889507L;

    private EntitaDocumento entita;
    private SedeEntita sedeEntita;
    private String codicePagamento;
    private String codicePagamentoCodice;

    private String causaleTrasporto;

    private String trasportoCura;

    private String aspettoEsteriore;

    private String tipoPorto;

    private String categoriaContabileSedeMagazzino;
    private String categoriaSedeMagazzino;
    private ETipologiaGenerazioneDocumentoFatturazione tipologiaGenerazioneDocumentoFatturazione;
    private ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo;

    private String codiceIvaAlternativo;

    private boolean raggruppamentoBolle;

    private boolean calcoloSpese;

    private boolean stampaPrezzo;
    private String tipoSede;

    private boolean sedePrincipale;

    private String codiceValuta;
    private String lingua;
    private String zonaGeografica;

    private boolean bloccoSede;

    private String listino;
    private String listinoAlternativo;

    private Integer idListino;

    private Integer idListinoAlternativo;

    private Integer idSedeMagazzino;
    private String agente;

    private String nazione;
    private String localita;
    private String cap;

    private String lvl1;

    private String lvl2;
    private String lvl3;
    private String lvl4;
    private Date dataScadenza;
    private boolean ereditaDatiCommerciali;
    private String indirizzoMail;

    private String indirizzoPEC;

    private String telefono;
    private String fax;

    /**
     * Costruttore.
     *
     */
    public RiepilogoSedeEntitaDTO() {
        super();
        this.entita = new EntitaDocumento();
        this.sedeEntita = new SedeEntita();
    }

    /**
     * @return Returns the agente.
     */
    public String getAgente() {
        return agente;
    }

    /**
     * @return the aspettoEsteriore
     */
    public String getAspettoEsteriore() {
        return aspettoEsteriore;
    }

    /**
     * @return the cap
     */
    public String getCap() {
        return cap;
    }

    /**
     * @return the categoriaContabileSedeMagazzino
     */
    public String getCategoriaContabileSedeMagazzino() {
        return categoriaContabileSedeMagazzino;
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
    public String getCodiceIvaAlternativo() {
        return codiceIvaAlternativo;
    }

    /**
     * @return the codicePagamento
     */
    public String getCodicePagamento() {
        return codicePagamento;
    }

    /**
     * @return Returns the codicePagamentoCodice.
     */
    public String getCodicePagamentoCodice() {
        return codicePagamentoCodice;
    }

    /**
     * @return the codiceValuta
     */
    public String getCodiceValuta() {
        return codiceValuta;
    }

    /**
     * @return Returns the dataScadenza.
     */
    public Date getDataScadenza() {
        return dataScadenza;
    }

    /**
     * @return the entita
     */
    public EntitaDocumento getEntita() {
        return entita;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @return Returns the idListino.
     */
    public Integer getIdListino() {
        return idListino;
    }

    /**
     * @return Returns the idListinoAlternativo.
     */
    public Integer getIdListinoAlternativo() {
        return idListinoAlternativo;
    }

    /**
     * @return Returns the idSedeMagazzino.
     */
    public Integer getIdSedeMagazzino() {
        return idSedeMagazzino;
    }

    public String getIndirizzoMail() {
        return indirizzoMail;
    }

    /**
     * @return the indirizzoPEC
     */
    public String getIndirizzoPEC() {
        return indirizzoPEC;
    }

    /**
     * @return the lingua
     */
    public String getLingua() {
        return lingua;
    }

    /**
     * @return Returns the listino.
     */
    public String getListino() {
        return listino;
    }

    /**
     * @return Returns the listinoAlternativo.
     */
    public String getListinoAlternativo() {
        return listinoAlternativo;
    }

    /**
     * @return the localita
     */
    public String getLocalita() {
        return localita;
    }

    /**
     * @return the lvl1
     */
    public String getLvl1() {
        return lvl1;
    }

    /**
     * @return the lvl2
     */
    public String getLvl2() {
        return lvl2;
    }

    /**
     * @return the lvl3
     */
    public String getLvl3() {
        return lvl3;
    }

    /**
     * @return the lvl4
     */
    public String getLvl4() {
        return lvl4;
    }

    /**
     * @return the nazione
     */
    public String getNazione() {
        return nazione;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
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
    public String getTipoPorto() {
        return tipoPorto;
    }

    /**
     * @return the tipoSede
     */
    public String getTipoSede() {
        return tipoSede;
    }

    /**
     * @return the trasportoCura
     */
    public String getTrasportoCura() {
        return trasportoCura;
    }

    /**
     * @return the zonaGeografica
     */
    public String getZonaGeografica() {
        return zonaGeografica;
    }

    /**
     * @return the bloccoSede
     */
    public boolean isBloccoSede() {
        return bloccoSede;
    }

    /**
     * @return the calcoloSpese
     */
    public boolean isCalcoloSpese() {
        return calcoloSpese;
    }

    /**
     * @return the ereditaDatiCommerciali
     */
    public boolean isEreditaDatiCommerciali() {
        return ereditaDatiCommerciali;
    }

    /**
     * @return the raggruppamentoBolle
     */
    public boolean isRaggruppamentoBolle() {
        return raggruppamentoBolle;
    }

    /**
     * @return the sedePrincipale
     */
    public boolean isSedePrincipale() {
        return sedePrincipale;
    }

    /**
     * @return the stampaPrezzo
     */
    public boolean isStampaPrezzo() {
        return stampaPrezzo;
    }

    /**
     * @param agente
     *            The agente to set.
     */
    public void setAgente(String agente) {
        this.agente = agente;
    }

    /**
     * @param aspettoEsteriore
     *            the aspettoEsteriore to set
     */
    public void setAspettoEsteriore(String aspettoEsteriore) {
        this.aspettoEsteriore = aspettoEsteriore;
    }

    /**
     * @param bloccoSede
     *            the bloccoSede to set
     */
    public void setBloccoSede(boolean bloccoSede) {
        this.bloccoSede = bloccoSede;
    }

    /**
     * @param calcoloSpese
     *            the calcoloSpese to set
     */
    public void setCalcoloSpese(boolean calcoloSpese) {
        this.calcoloSpese = calcoloSpese;
    }

    /**
     * @param cap
     *            the cap to set
     */
    public void setCap(String cap) {
        this.cap = cap;
    }

    /**
     * @param categoriaContabileSedeMagazzino
     *            the categoriaContabileSedeMagazzino to set
     */
    public void setCategoriaContabileSedeMagazzino(String categoriaContabileSedeMagazzino) {
        this.categoriaContabileSedeMagazzino = categoriaContabileSedeMagazzino;
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
     * @param codiceEntita
     *            the codiceEntita to set
     */
    public void setCodiceEntita(Integer codiceEntita) {
        this.getEntita().setCodice(codiceEntita);
    }

    /**
     * @param codiceIvaAlternativo
     *            the codiceIvaAlternativo to set
     */
    public void setCodiceIvaAlternativo(String codiceIvaAlternativo) {
        this.codiceIvaAlternativo = codiceIvaAlternativo;
    }

    /**
     * @param codicePagamento
     *            the codicePagamento to set
     */
    public void setCodicePagamento(String codicePagamento) {
        this.codicePagamento = codicePagamento;
    }

    /**
     * @param codicePagamentoCodice
     *            The codicePagamentoCodice to set.
     */
    public void setCodicePagamentoCodice(String codicePagamentoCodice) {
        this.codicePagamentoCodice = codicePagamentoCodice;
    }

    /**
     * @param codiceSedeEntita
     *            the codiceSedeEntita to set
     */
    public void setCodiceSedeEntita(String codiceSedeEntita) {
        this.getSedeEntita().setCodice(codiceSedeEntita);
    }

    /**
     * @param codiceValuta
     *            the codiceValuta to set
     */
    public void setCodiceValuta(String codiceValuta) {
        this.codiceValuta = codiceValuta;
    }

    /**
     * @param dataScadenza
     *            The dataScadenza to set.
     */
    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    /**
     * @param descrizioneEntita
     *            the descrizioneEntita to set
     */
    public void setDescrizioneEntita(String descrizioneEntita) {
        this.entita.setDescrizione(descrizioneEntita);
    }

    /**
     * @param descrizioneSedeEntita
     *            the descrizioneSedeEntita to set
     */
    public void setDescrizioneSedeEntita(String descrizioneSedeEntita) {
        this.getSedeEntita().getSede().setDescrizione(descrizioneSedeEntita);
    }

    /**
     * @param ereditaDatiCommerciali
     *            the ereditaDatiCommerciali to set
     */
    public void setEreditaDatiCommerciali(boolean ereditaDatiCommerciali) {
        this.ereditaDatiCommerciali = ereditaDatiCommerciali;
    }

    /**
     * @param fax
     *            the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @param idEntita
     *            the idEntita to set
     */
    public void setIdEntita(Integer idEntita) {
        this.getEntita().setId(idEntita);
    }

    /**
     * @param idListino
     *            The idListino to set.
     */
    public void setIdListino(Integer idListino) {
        this.idListino = idListino;
    }

    /**
     * @param idListinoAlternativo
     *            The idListinoAlternativo to set.
     */
    public void setIdListinoAlternativo(Integer idListinoAlternativo) {
        this.idListinoAlternativo = idListinoAlternativo;
    }

    /**
     * @param idSedeEntita
     *            the idSedeEntita to set
     */
    public void setIdSedeEntita(Integer idSedeEntita) {
        this.getSedeEntita().setId(idSedeEntita);
    }

    /**
     * @param idSedeMagazzino
     *            The idSedeMagazzino to set.
     */
    public void setIdSedeMagazzino(Integer idSedeMagazzino) {
        this.idSedeMagazzino = idSedeMagazzino;
    }

    public void setIndirizzoMail(String indirizzoMail) {
        this.indirizzoMail = indirizzoMail;
    }

    /**
     * @param indirizzoPEC
     *            the indirizzoPEC to set
     */
    public void setIndirizzoPEC(String indirizzoPEC) {
        this.indirizzoPEC = indirizzoPEC;
    }

    /**
     * @param indirizzoSedeEntita
     *            the indirizzoSedeEntita to set
     */
    public void setIndirizzoSedeEntita(String indirizzoSedeEntita) {
        this.getSedeEntita().getSede().setIndirizzo(indirizzoSedeEntita);
    }

    /**
     * @param lingua
     *            the lingua to set
     */
    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    /**
     * @param listino
     *            The listino to set.
     */
    public void setListino(String listino) {
        this.listino = listino;
    }

    /**
     * @param listinoAlternativo
     *            The listinoAlternativo to set.
     */
    public void setListinoAlternativo(String listinoAlternativo) {
        this.listinoAlternativo = listinoAlternativo;
    }

    /**
     * @param localita
     *            the localita to set
     */
    public void setLocalita(String localita) {
        this.localita = localita;
    }

    /**
     * @param lvl1
     *            the lvl1 to set
     */
    public void setLvl1(String lvl1) {
        this.lvl1 = lvl1;
    }

    /**
     * @param lvl2
     *            the lvl2 to set
     */
    public void setLvl2(String lvl2) {
        this.lvl2 = lvl2;
    }

    /**
     * @param lvl3
     *            the lvl3 to set
     */
    public void setLvl3(String lvl3) {
        this.lvl3 = lvl3;
    }

    /**
     * @param lvl4
     *            the lvl4 to set
     */
    public void setLvl4(String lvl4) {
        this.lvl4 = lvl4;
    }

    /**
     * @param nazione
     *            the nazione to set
     */
    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    /**
     * @param raggruppamentoBolle
     *            the raggruppamentoBolle to set
     */
    public void setRaggruppamentoBolle(boolean raggruppamentoBolle) {
        this.raggruppamentoBolle = raggruppamentoBolle;
    }

    /**
     * @param sedePrincipale
     *            the sedePrincipale to set
     */
    public void setSedePrincipale(boolean sedePrincipale) {
        this.sedePrincipale = sedePrincipale;
    }

    /**
     * @param stampaPrezzo
     *            the stampaPrezzo to set
     */
    public void setStampaPrezzo(boolean stampaPrezzo) {
        this.stampaPrezzo = stampaPrezzo;
    }

    /**
     * @param telefono
     *            the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @param tipoEntita
     *            the tipoEntita to set
     */
    public void setTipoEntita(String tipoEntita) {
        this.entita.setTipoEntita(tipoEntita);
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
    public void setTipoPorto(String tipoPorto) {
        this.tipoPorto = tipoPorto;
    }

    /**
     * @param tipoSede
     *            the tipoSede to set
     */
    public void setTipoSede(String tipoSede) {
        this.tipoSede = tipoSede;
    }

    /**
     * @param trasportoCura
     *            the trasportoCura to set
     */
    public void setTrasportoCura(String trasportoCura) {
        this.trasportoCura = trasportoCura;
    }

    /**
     * @param zonaGeografica
     *            the zonaGeografica to set
     */
    public void setZonaGeografica(String zonaGeografica) {
        this.zonaGeografica = zonaGeografica;
    }
}
