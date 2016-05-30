package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;

public class ParametriCreazioneRigaArticolo implements Serializable {

  private static final long serialVersionUID = 2498904457546041676L;
  private ProvenienzaPrezzo provenienzaPrezzo;
  private Integer idArticolo;
  private Integer idConfigurazioneDistinta;
  private Date data;
  private Integer idEntita;
  private Integer idSedeEntita;
  private Integer idListino;
  private Integer idListinoAlternativo;

  private Importo importo;
  private CodiceIva codiceIvaAlternativo;
  private Integer idTipoMezzo;
  private Integer idZonaGeografica;
  private ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo;
  private TipoMovimento tipoMovimento;
  private String codiceValuta;
  private String codiceLingua;
  private Integer idAgente;
  private ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo;
  private BigDecimal percentualeScontoCommerciale;
  private Integer idDeposito;
  private boolean calcolaGiacenza;

  private boolean noteSuDestinazione;

  private boolean gestioneConai;

  private boolean notaCredito;
  private StrategiaTotalizzazioneDocumento strategiaTotalizzazioneDocumento;
  private boolean gestioneArticoloDistinta;

  {
    strategiaTotalizzazioneDocumento = StrategiaTotalizzazioneDocumento.NORMALE;
    gestioneArticoloDistinta = false;
    calcolaGiacenza = false;
  }

  /**
   * @return Returns the codiceIvaAlternativo.
   */
  public CodiceIva getCodiceIvaAlternativo() {
    return codiceIvaAlternativo;
  }

  /**
   * @return Returns the codiceLingua.
   */
  public String getCodiceLingua() {
    return codiceLingua;
  }

  /**
   * @return Returns the codiceValuta.
   */
  public String getCodiceValuta() {
    return codiceValuta;
  }

  /**
   * @return Returns the data.
   */
  public Date getData() {
    return data;
  }

  /**
   * @return Returns the idAgente.
   */
  public Integer getIdAgente() {
    return idAgente;
  }

  /**
   * @return Returns the idArticolo.
   */
  public Integer getIdArticolo() {
    return idArticolo;
  }

  /**
   * @return the idConfigurazioneDistinta
   */
  public Integer getIdConfigurazioneDistinta() {
    return idConfigurazioneDistinta;
  }

  /**
   * @return Returns the idDeposito.
   */
  public Integer getIdDeposito() {
    return idDeposito;
  }

  /**
   * @return the idEntita
   */
  public Integer getIdEntita() {
    return idEntita;
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
   * @return Returns the idSedeEntita.
   */
  public Integer getIdSedeEntita() {
    return idSedeEntita;
  }

  /**
   * @return Returns the idTipoMezzo.
   */
  public Integer getIdTipoMezzo() {
    return idTipoMezzo;
  }

  /**
   * @return Returns the idZonaGeografica.
   */
  public Integer getIdZonaGeografica() {
    return idZonaGeografica;
  }

  /**
   * @return Returns the importo.
   */
  public Importo getImporto() {
    return importo;
  }

  /**
   * @return Returns the percentualeScontoCommerciale.
   */
  public BigDecimal getPercentualeScontoCommerciale() {
    return percentualeScontoCommerciale;
  }

  /**
   * @return Returns the provenienzaPrezzo.
   */
  public ProvenienzaPrezzo getProvenienzaPrezzo() {
    return provenienzaPrezzo;
  }

  /**
   * @return Returns the provenienzaPrezzoArticolo.
   */
  public ProvenienzaPrezzoArticolo getProvenienzaPrezzoArticolo() {
    return provenienzaPrezzoArticolo;
  }

  /**
   * @return the strategiaTotalizzazioneDocumento
   */
  public StrategiaTotalizzazioneDocumento getStrategiaTotalizzazioneDocumento() {
    return strategiaTotalizzazioneDocumento;
  }

  /**
   * @return Returns the tipologiaCodiceIvaAlternativo.
   */
  public ETipologiaCodiceIvaAlternativo getTipologiaCodiceIvaAlternativo() {
    if (tipologiaCodiceIvaAlternativo == null) {
      tipologiaCodiceIvaAlternativo = ETipologiaCodiceIvaAlternativo.NESSUNO;
    }
    return tipologiaCodiceIvaAlternativo;
  }

  /**
   * @return Returns the tipoMovimento.
   */
  public TipoMovimento getTipoMovimento() {
    return tipoMovimento;
  }

  /**
   * @return Returns the calcolaGiacenza.
   */
  public boolean isCalcolaGiacenza() {
    return calcolaGiacenza;
  }

  /**
   * @return the gestioneArticoloDistinta
   */
  public boolean isGestioneArticoloDistinta() {
    return gestioneArticoloDistinta;
  }

  /**
   * @return the gestioneConai
   */
  public boolean isGestioneConai() {
    return gestioneConai;
  }

  /**
   * @return the notaCredito
   */
  public boolean isNotaCredito() {
    return notaCredito;
  }

  /**
   * @return Returns the noteSuDestinazione.
   */
  public boolean isNoteSuDestinazione() {
    return noteSuDestinazione;
  }

  /**
   * @param calcolaGiacenza
   *          The calcolaGiacenza to set.
   */
  public void setCalcolaGiacenza(boolean calcolaGiacenza) {
    this.calcolaGiacenza = calcolaGiacenza;
  }

  /**
   * @param codiceIvaAlternativo
   *          The codiceIvaAlternativo to set.
   */
  public void setCodiceIvaAlternativo(CodiceIva codiceIvaAlternativo) {
    this.codiceIvaAlternativo = codiceIvaAlternativo;
  }

  /**
   * @param codiceLingua
   *          The codiceLingua to set.
   */
  public void setCodiceLingua(String codiceLingua) {
    this.codiceLingua = codiceLingua;
  }

  /**
   * @param codiceValuta
   *          The codiceValuta to set.
   */
  public void setCodiceValuta(String codiceValuta) {
    this.codiceValuta = codiceValuta;
  }

  /**
   * @param data
   *          The data to set.
   */
  public void setData(Date data) {
    this.data = data;
  }

  /**
   * @param gestioneArticoloDistinta
   *          the gestioneArticoloDistinta to set
   */
  public void setGestioneArticoloDistinta(boolean gestioneArticoloDistinta) {
    this.gestioneArticoloDistinta = gestioneArticoloDistinta;
  }

  /**
   * @param gestioneConai
   *          the gestioneConai to set
   */
  public void setGestioneConai(boolean gestioneConai) {
    this.gestioneConai = gestioneConai;
  }

  /**
   * @param idAgente
   *          The idAgente to set.
   */
  public void setIdAgente(Integer idAgente) {
    this.idAgente = idAgente;
  }

  /**
   * @param idArticolo
   *          The idArticolo to set.
   */
  public void setIdArticolo(Integer idArticolo) {
    this.idArticolo = idArticolo;
  }

  /**
   * @param idConfigurazioneDistinta
   *          the idConfigurazioneDistinta to set
   */
  public void setIdConfigurazioneDistinta(Integer idConfigurazioneDistinta) {
    this.idConfigurazioneDistinta = idConfigurazioneDistinta;
  }

  /**
   * @param idDeposito
   *          The idDeposito to set.
   */
  public void setIdDeposito(Integer idDeposito) {
    this.idDeposito = idDeposito;
  }

  /**
   * @param idEntita
   *          the idEntita to set
   */
  public void setIdEntita(Integer idEntita) {
    this.idEntita = idEntita;
  }

  /**
   * @param idListino
   *          The idListino to set.
   */
  public void setIdListino(Integer idListino) {
    this.idListino = idListino;
  }

  /**
   * @param idListinoAlternativo
   *          The idListinoAlternativo to set.
   */
  public void setIdListinoAlternativo(Integer idListinoAlternativo) {
    this.idListinoAlternativo = idListinoAlternativo;
  }

  /**
   * @param idSedeEntita
   *          The idSedeEntita to set.
   */
  public void setIdSedeEntita(Integer idSedeEntita) {
    this.idSedeEntita = idSedeEntita;
  }

  /**
   * @param idTipoMezzo
   *          The idTipoMezzo to set.
   */
  public void setIdTipoMezzo(Integer idTipoMezzo) {
    this.idTipoMezzo = idTipoMezzo;
  }

  /**
   * @param idZonaGeografica
   *          The idZonaGeografica to set.
   */
  public void setIdZonaGeografica(Integer idZonaGeografica) {
    this.idZonaGeografica = idZonaGeografica;
  }

  /**
   * @param importo
   *          The importo to set.
   */
  public void setImporto(Importo importo) {
    this.importo = importo;
  }

  /**
   * @param notaCredito
   *          the notaCredito to set
   */
  public void setNotaCredito(boolean notaCredito) {
    this.notaCredito = notaCredito;
  }

  /**
   * @param noteSuDestinazione
   *          The noteSuDestinazione to set.
   */
  public void setNoteSuDestinazione(boolean noteSuDestinazione) {
    this.noteSuDestinazione = noteSuDestinazione;
  }

  /**
   * @param percentualeScontoCommerciale
   *          The percentualeScontoCommerciale to set.
   */
  public void setPercentualeScontoCommerciale(BigDecimal percentualeScontoCommerciale) {
    this.percentualeScontoCommerciale = percentualeScontoCommerciale;
  }

  /**
   * @param provenienzaPrezzo
   *          The provenienzaPrezzo to set.
   */
  public void setProvenienzaPrezzo(ProvenienzaPrezzo provenienzaPrezzo) {
    this.provenienzaPrezzo = provenienzaPrezzo;
  }

  /**
   * @param provenienzaPrezzoArticolo
   *          The provenienzaPrezzoArticolo to set.
   */
  public void setProvenienzaPrezzoArticolo(ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo) {
    this.provenienzaPrezzoArticolo = provenienzaPrezzoArticolo;
  }

  /**
   * @param strategiaTotalizzazioneDocumento
   *          the strategiaTotalizzazioneDocumento to set
   */
  public void setStrategiaTotalizzazioneDocumento(
      StrategiaTotalizzazioneDocumento strategiaTotalizzazioneDocumento) {
    this.strategiaTotalizzazioneDocumento = strategiaTotalizzazioneDocumento;
  }

  /**
   * @param tipologiaCodiceIvaAlternativo
   *          The tipologiaCodiceIvaAlternativo to set.
   */
  public void setTipologiaCodiceIvaAlternativo(
      ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo) {
    this.tipologiaCodiceIvaAlternativo = tipologiaCodiceIvaAlternativo;
  }

  /**
   * @param tipoMovimento
   *          The tipoMovimento to set.
   */
  public void setTipoMovimento(TipoMovimento tipoMovimento) {
    this.tipoMovimento = tipoMovimento;
    gestioneArticoloDistinta = tipoMovimento != null
        && tipoMovimento == TipoMovimento.CARICO_PRODUZIONE;
  }
}
