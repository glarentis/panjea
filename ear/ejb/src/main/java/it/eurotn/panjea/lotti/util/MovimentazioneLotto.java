package it.eurotn.panjea.lotti.util;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.LottoInterno;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Date;

public class MovimentazioneLotto implements Serializable {

	public static final String STR_ZERI = "0000000000000";
	private static final String NUMBER_FORMAT = "###,###,###,##0";

	private static final long serialVersionUID = 5325941936537738708L;

	private Integer id;

	private Documento documento;

	private TipoMovimento tipoMovimento;

	private int idAreaMagazzino;

	private Date dataRegistrazione;

	private DepositoLite deposito;

	private DepositoLite depositoDestinazione;

	private Double quantita;
	private int numeroDecimaliQta;

	private Lotto lotto;

	private LottoInterno lottoInterno;
	{
		this.documento = new Documento();
		documento.setRapportoBancarioAzienda(new RapportoBancarioAzienda());
		documento.setEntita(new ClienteLite());
		this.deposito = new DepositoLite();
		this.depositoDestinazione = new DepositoLite();
		this.quantita = 0.0;
		this.numeroDecimaliQta = 0;
		this.lotto = new Lotto();
		this.lotto.setArticolo(new ArticoloLite());
		this.lottoInterno = new LottoInterno();
	}

	/**
	 * Costruttore.
	 */
	public MovimentazioneLotto() {
		super();
	}

	/**
	 * Formatta il valore della quantità in base al numero decimali presenti.
	 * 
	 * @param value
	 *            valore della quantità
	 * @return valore formattato
	 */
	private String formatQta(Double value) {
		String result = "";
		if (value != null) {
			Format format = new DecimalFormat(NUMBER_FORMAT);
			String parteDecimale = numeroDecimaliQta != 0 ? "." + STR_ZERI.substring(0, numeroDecimaliQta) : "";
			format = new DecimalFormat(NUMBER_FORMAT + parteDecimale);
			result = format.format(value);
		}

		return result;
	}

	/**
	 * @return the dataRegistrazione
	 */
	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return the deposito
	 */
	public DepositoLite getDeposito() {
		if (deposito.getId() != null) {
			return deposito;
		} else {
			return null;
		}
	}

	/**
	 * @return Returns the depositoDestinazione.
	 */
	public DepositoLite getDepositoDestinazione() {
		if (depositoDestinazione.getId() != null) {
			return depositoDestinazione;
		} else {
			return null;
		}
	}

	/**
	 * @return the documento
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return Returns the idAreaMagazzino.
	 */
	public int getIdAreaMagazzino() {
		return idAreaMagazzino;
	}

	/**
	 * @return the lotto
	 */
	public Lotto getLotto() {
		return lotto;
	}

	/**
	 * @return the lottoInterno
	 */
	public Lotto getLottoInterno() {
		return lottoInterno;
	}

	/**
	 * @return the quantita carico
	 */
	public Double getQuantitaCarico() {
		if (quantita == null) {
			quantita = 0.0;
		}
		BigDecimal val = BigDecimal.valueOf(quantita).setScale(numeroDecimaliQta, RoundingMode.HALF_UP);
		quantita = val.doubleValue();
		if ((quantita.compareTo(0.0) > 0 && !documento.getTipoDocumento().isNotaCreditoEnable())
				|| (quantita.compareTo(0.0) < 0 && documento.getTipoDocumento().isNotaCreditoEnable())) {
			return quantita;
		} else {
			return null;
		}
	}

	/**
	 * @return the quantita carico formattata
	 */
	public String getQuantitaCaricoString() {
		return formatQta(getQuantitaCarico());
	}

	/**
	 * @return the quantita scarico
	 */
	public Double getQuantitaScarico() {
		BigDecimal val = BigDecimal.valueOf(quantita).setScale(numeroDecimaliQta, RoundingMode.HALF_UP);
		quantita = val.doubleValue();
		if ((quantita.compareTo(0.0) > 0 && documento.getTipoDocumento().isNotaCreditoEnable())) {
			return quantita * -1;
		}

		if ((quantita.compareTo(0.0) < 0 && !documento.getTipoDocumento().isNotaCreditoEnable())) {
			return Math.abs(quantita);
		}
		return null;
	}

	/**
	 * @return the quantita scarico formattata
	 */
	public String getQuantitaScaricoString() {
		return formatQta(getQuantitaScarico());
	}

	/**
	 * @return the rimanenza
	 */
	public Double getRimanenza() {
		Double qtaCarico = getQuantitaCarico() == null ? 0.0 : getQuantitaCarico();
		Double qtaScarico = getQuantitaScarico() == null ? 0.0 : getQuantitaScarico();
		return qtaCarico - qtaScarico;
	}

	/**
	 * @return the tipoMovimento
	 */
	public TipoMovimento getTipoMovimento() {
		return tipoMovimento;
	}

	/**
	 * @param codiceArticolo
	 *            the codiceArticolo to set
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.lotto.getArticolo().setCodice(codiceArticolo);
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAziendaTipoDocumento(String codiceAzienda) {
		this.documento.setCodiceAzienda(codiceAzienda);
	}

	/**
	 * @param codiceDeposito
	 *            the codiceDeposito to set
	 */
	public void setCodiceDeposito(String codiceDeposito) {
		this.deposito.setCodice(codiceDeposito);
	}

	/**
	 * @param codiceDeposito
	 *            the codiceDeposito to set
	 */
	public void setCodiceDepositoDestinazione(String codiceDeposito) {
		this.depositoDestinazione.setCodice(codiceDeposito);
	}

	/**
	 * @param codiceDocumento
	 *            the codiceDocumento to set
	 */
	public void setCodiceDocumento(CodiceDocumento codiceDocumento) {
		this.documento.setCodice(codiceDocumento);
	}

	/**
	 * @param codiceEntita
	 *            the codiceEntita to set
	 */
	public void setCodiceEntitaDocumento(Integer codiceEntita) {
		this.documento.getEntita().setCodice(codiceEntita);
	}

	/**
	 * @param codiceLotto
	 *            the codiceLotto to set
	 */
	public void setCodiceLotto(String codiceLotto) {
		this.lotto.setCodice(codiceLotto);
	}

	/**
	 * @param codiceInternoLotto
	 *            the codiceInternoLotto to set
	 */
	public void setCodiceLottoInterno(String codiceInternoLotto) {
		this.lottoInterno.setCodice(codiceInternoLotto);
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
	 * @param dataScadenzaLotto
	 *            the dataScadenzaLotto to set
	 */
	public void setDataScadenzaLotto(Date dataScadenzaLotto) {
		this.lotto.setDataScadenza(dataScadenzaLotto);
	}

	/**
	 * @param denominazioneEntita
	 *            the denominazioneEntita to set
	 */
	public void setDenominazioneEntitaDocumento(String denominazioneEntita) {
		this.documento.getEntita().getAnagrafica().setDenominazione(denominazioneEntita);
	}

	/**
	 * @param descrizioneArticolo
	 *            the descrizioneArticolo to set
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.lotto.getArticolo().setDescrizione(descrizioneArticolo);
	}

	/**
	 * @param descrizioneDeposito
	 *            the descrizioneDeposito to set
	 */
	public void setDescrizioneDeposito(String descrizioneDeposito) {
		this.deposito.setDescrizione(descrizioneDeposito);
	}

	/**
	 * @param descrizioneDeposito
	 *            the descrizioneDeposito to set
	 */
	public void setDescrizioneDepositoDestinazione(String descrizioneDeposito) {
		this.depositoDestinazione.setDescrizione(descrizioneDeposito);
	}

	/**
	 * @param descrizioneRapportoBancario
	 *            the descrizioneRapportoBancario to set
	 */
	public void setDescrizioneRapportoBancarioDocumento(String descrizioneRapportoBancario) {
		this.documento.getRapportoBancarioAzienda().setDescrizione(descrizioneRapportoBancario);
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.documento.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
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
	 *            The idAreaMagazzino to set.
	 */
	public void setIdAreaMagazzino(int idAreaMagazzino) {
		this.idAreaMagazzino = idAreaMagazzino;
	}

	/**
	 * @param idDeposito
	 *            the idDeposito to set
	 */
	public void setIdDeposito(Integer idDeposito) {
		this.deposito.setId(idDeposito);
	}

	/**
	 * @param idDeposito
	 *            the idDeposito to set
	 */
	public void setIdDepositoDestinazione(Integer idDeposito) {
		this.depositoDestinazione.setId(idDeposito);
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
	public void setIdEntitaDocumento(Integer idEntita) {
		this.documento.getEntita().setId(idEntita);
	}

	/**
	 * @param idLotto
	 *            the idLotto to set
	 */
	public void setIdLotto(Integer idLotto) {
		this.lotto.setId(idLotto);
	}

	/**
	 * @param idLotto
	 *            the idLotto to set
	 */
	public void setIdLottoInterno(Integer idLotto) {
		this.lottoInterno.setId(idLotto);
	}

	/**
	 * @param idRapportoBancario
	 *            the idRapportoBancario to set
	 */
	public void setIdRapportoBancarioDocumento(Integer idRapportoBancario) {
		this.documento.getRapportoBancarioAzienda().setId(idRapportoBancario);
	}

	/**
	 * @param idTipoDocumento
	 *            the idTipoDocumento to set
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.documento.getTipoDocumento().setId(idTipoDocumento);
	}

	/**
	 * @param numeroDecimaliQta
	 *            the numeroDecimaliQta to set
	 */
	protected void setNumeroDecimaliQta(int numeroDecimaliQta) {
		this.numeroDecimaliQta = numeroDecimaliQta;
	}

	/**
	 * @param quantita
	 *            the quantita to set
	 */
	public void setQuantita(Double quantita) {
		this.quantita = quantita;
	}

	/**
	 * 
	 * @param storno
	 *            storno del tipoDocumento
	 */
	public void setStorno(boolean storno) {
		documento.getTipoDocumento().setNotaCreditoEnable(storno);
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 */
	public void setTipoEntitaTipoDocumento(TipoEntita tipoEntita) {
		this.documento.getTipoDocumento().setTipoEntita(tipoEntita);
	}

	/**
	 * @param tipoMovimento
	 *            the tipoMovimento to set
	 */
	public void setTipoMovimento(TipoMovimento tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

}
