package it.eurotn.panjea.lotti.manager.rimanenzefinali;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.util.PanjeaEJBUtil;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class RimanenzeFinaliDTO implements Serializable, Cloneable {

	private static Logger logger = Logger.getLogger(RimanenzeFinaliDTO.class);

	private static final long serialVersionUID = -3138226586642144994L;

	private Double giacenza;
	private Double qtaScaricoVenditaMese;
	private Double qtaScaricoVenditaAnno;
	private Double qtaScaricoAltroVenditaAnno;
	private Double qtaCaricoAltroVenditaAnno;

	private ArticoloLite articolo;

	private EntitaLite fornitore;

	private String unitaMisura;

	private Lotto lotto;

	private DepositoLite deposito;

	private String telefonoFornitore;
	private String faxFornitore;

	private String ubicazioneArticolo;

	{
		this.giacenza = 0.0;
		this.qtaScaricoVenditaAnno = 0.0;
		this.qtaScaricoAltroVenditaAnno = 0.0;
		this.qtaScaricoVenditaMese = 0.0;
		this.qtaCaricoAltroVenditaAnno = 0.0;
		this.articolo = new ArticoloLite();
		this.articolo.setCategoria(new Categoria());
		this.fornitore = new FornitoreLite();
		this.deposito = new DepositoLite();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		RimanenzeFinaliDTO rimanenzaFinaleClone = new RimanenzeFinaliDTO();
		try {
			rimanenzaFinaleClone = PanjeaEJBUtil.cloneObject(this);
		} catch (Exception e) {
			logger.error("--> errore durante il clone della rimanenza", e);
			throw new RuntimeException("errore durante il clone della rimanenza", e);
		}
		return rimanenzaFinaleClone;
	}

	/**
	 * @return Returns the articolo.
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return Returns the deposito.
	 */
	public DepositoLite getDeposito() {
		return deposito;
	}

	/**
	 * @return Returns the faxFornitore.
	 */
	public String getFaxFornitore() {
		return faxFornitore;
	}

	/**
	 * @return Returns the fornitore.
	 */
	public EntitaLite getFornitore() {
		return fornitore;
	}

	/**
	 * @return Returns the giacenza.
	 */
	public Double getGiacenza() {
		return giacenza;
	}

	/**
	 * @return Returns the lotto.
	 */
	public Lotto getLotto() {
		return lotto;
	}

	/**
	 * @return the qtaCaricoAltroVenditaAnno
	 */
	public Double getQtaCaricoAltroVenditaAnno() {
		return qtaCaricoAltroVenditaAnno;
	}

	/**
	 * @return the qtaScaricoAltroVenditaAnno
	 */
	public Double getQtaScaricoAltroVenditaAnno() {
		return qtaScaricoAltroVenditaAnno;
	}

	/**
	 * @return the qtaScaricoVenditaAnno
	 */
	public Double getQtaScaricoVenditaAnno() {
		return qtaScaricoVenditaAnno;
	}

	/**
	 * @return the qtaScaricoVenditaMese
	 */
	public Double getQtaScaricoVenditaMese() {
		return qtaScaricoVenditaMese;
	}

	/**
	 * @return Returns the telefonoFornitore.
	 */
	public String getTelefonoFornitore() {
		return telefonoFornitore;
	}

	/**
	 * @return Returns the ubicazioneArticolo.
	 */
	public String getUbicazioneArticolo() {
		return ubicazioneArticolo;
	}

	/**
	 * @return Returns the unitaMisura.
	 */
	public String getUnitaMisura() {
		return unitaMisura;
	}

	/**
	 * @param codiceArticolo
	 *            the codiceArticolo to set
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.articolo.setCodice(codiceArticolo);
	}

	/**
	 * @param codiceCategoria
	 *            the codiceCategoria to set
	 */
	public void setCodiceCategoria(String codiceCategoria) {
		this.articolo.getCategoria().setCodice(codiceCategoria);
	}

	/**
	 * @param codiceDeposito
	 *            the codiceDeposito to set
	 */
	public void setCodiceDeposito(String codiceDeposito) {
		this.deposito.setCodice(codiceDeposito);
	}

	/**
	 * @param codiceFornitore
	 *            the codiceFornitore to set
	 */
	public void setCodiceFornitore(Integer codiceFornitore) {
		this.fornitore.setCodice(codiceFornitore);
	}

	/**
	 * @param denominazioneFornitore
	 *            the denominazioneFornitore to set
	 */
	public void setDenominazioneFornitore(String denominazioneFornitore) {
		this.fornitore.getAnagrafica().setDenominazione(denominazioneFornitore);
	}

	/**
	 * @param descrizioneArticolo
	 *            the descrizioneArticolo to set
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.articolo.setDescrizione(descrizioneArticolo);
	}

	/**
	 * @param descrizioneCategoria
	 *            the descrizioneCategoria to set
	 */
	public void setDescrizioneCategoria(String descrizioneCategoria) {
		this.articolo.getCategoria().setDescrizione(descrizioneCategoria);
	}

	/**
	 * @param descrizioneDeposito
	 *            the descrizioneDeposito to set
	 */
	public void setDescrizioneDeposito(String descrizioneDeposito) {
		this.deposito.setDescrizione(descrizioneDeposito);
	}

	/**
	 * @param faxFornitore
	 *            The faxFornitore to set.
	 */
	public void setFaxFornitore(String faxFornitore) {
		this.faxFornitore = faxFornitore;
	}

	/**
	 * @param fornitore
	 *            The fornitore to set.
	 */
	public void setFornitore(EntitaLite fornitore) {
		this.fornitore = fornitore;
	}

	/**
	 * @param giacenza
	 *            The giacenza to set.
	 */
	public void setGiacenza(Double giacenza) {
		this.giacenza = giacenza;
	}

	/**
	 * @param idArticolo
	 *            the idArticolo to set
	 */
	public void setIdArticolo(Integer idArticolo) {
		this.articolo.setId(idArticolo);
	}

	/**
	 * @param idCategoria
	 *            the idCategoria to set
	 */
	public void setIdCategoria(Integer idCategoria) {
		this.articolo.getCategoria().setId(idCategoria);
	}

	/**
	 * @param idDeposito
	 *            the idDeposito to set
	 */
	public void setIdDeposito(Integer idDeposito) {
		this.deposito.setId(idDeposito);
	}

	/**
	 * @param idFornitore
	 *            the idFornitore to set
	 */
	public void setIdFornitore(Integer idFornitore) {
		this.fornitore.setId(idFornitore);
	}

	/**
	 * @param lotto
	 *            The lotto to set.
	 */
	public void setLotto(Lotto lotto) {
		this.lotto = lotto;
	}

	/**
	 * @param qtaCaricoAltroVenditaAnno
	 *            the qtaCaricoAltroVenditaAnno to set
	 */
	public void setQtaCaricoAltroVenditaAnno(Double qtaCaricoAltroVenditaAnno) {
		this.qtaCaricoAltroVenditaAnno = qtaCaricoAltroVenditaAnno;
	}

	/**
	 * @param qtaScaricoAltroVenditaAnno
	 *            the qtaScaricoAltroVenditaAnno to set
	 */
	public void setQtaScaricoAltroVenditaAnno(Double qtaScaricoAltroVenditaAnno) {
		this.qtaScaricoAltroVenditaAnno = qtaScaricoAltroVenditaAnno;
	}

	/**
	 * @param qtaScaricoVenditaAnno
	 *            the qtaScaricoVenditaAnno to set
	 */
	public void setQtaScaricoVenditaAnno(Double qtaScaricoVenditaAnno) {
		this.qtaScaricoVenditaAnno = qtaScaricoVenditaAnno;
	}

	/**
	 * @param qtaScaricoVenditaMese
	 *            the qtaScaricoVenditaMese to set
	 */
	public void setQtaScaricoVenditaMese(Double qtaScaricoVenditaMese) {
		this.qtaScaricoVenditaMese = qtaScaricoVenditaMese;
	}

	/**
	 * @param telefonoFornitore
	 *            The telefonoFornitore to set.
	 */
	public void setTelefonoFornitore(String telefonoFornitore) {
		this.telefonoFornitore = telefonoFornitore;
	}

	/**
	 * @param ubicazioneArticolo
	 *            The ubicazioneArticolo to set.
	 */
	public void setUbicazioneArticolo(String ubicazioneArticolo) {
		this.ubicazioneArticolo = ubicazioneArticolo;
	}

	/**
	 * @param unitaMisura
	 *            The unitaMisura to set.
	 */
	public void setUnitaMisura(String unitaMisura) {
		this.unitaMisura = unitaMisura;
	}

}
