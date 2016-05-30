package it.eurotn.panjea.ordini.domain.documento;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.util.PanjeaEJBUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Definisce le proprietà per l'area magazzino.
 *
 * @author giangi
 */
@Entity
@Table(name = "ordi_tipi_area_ordine")
@Audited
@NamedQueries({
		@NamedQuery(name = "TipoAreaOrdine.caricaByTipoDocumento", query = "select tao from TipoAreaOrdine tao inner join tao.tipoDocumento td where td.id = :paramId "),
		@NamedQuery(name = "TipoAreaOrdine.caricaTipiDocumentiMagazzino", query = " select ta.tipoDocumento from TipoAreaMagazzino ta where ta.tipoDocumento.codiceAzienda = :paramCodiceAzienda ") })
public class TipoAreaOrdine extends EntityBase implements java.io.Serializable, Cloneable, ITipoAreaDocumento {
	private static final long serialVersionUID = -4589807956404025069L;
	public static final String REPORT_PATH = "Ordine/Documenti";

	@OneToOne
	private TipoDocumento tipoDocumento;

	private boolean dataDocLikeDataReg;

	@Column(length = 100)
	private String descrizionePerStampa;

	/**
	 * Indica se le note della riga dovranno essere riportate sul documento di destinazione.
	 *
	 */
	private boolean noteSuDestinazione;

	/**
	 * Contiene la maschera necessaria per la generazione della descrizione della riga testata sul documento di
	 * evasione.
	 */
	@Column(length = 200)
	private String tipoDocumentoEvasioneDescrizioneMaschera;

	@ManyToOne
	private DepositoLite depositoOrigine;

	private boolean depositoOrigineBloccato;

	private boolean ordineProduzione;

	/**
	 * inizializza le variabili.
	 */
	{
		this.tipoDocumento = new TipoDocumento();

		this.ordineProduzione = false;
	}

	/**
	 * Costruttore.
	 */
	public TipoAreaOrdine() {
	}

	@Override
	public Object clone() {
		TipoAreaOrdine tipoAreaOrdine = PanjeaEJBUtil.cloneObject(this);
		tipoAreaOrdine.setId(null);
		return tipoAreaOrdine;
	}

	/**
	 * @return the depositoDestinazione
	 */
	public DepositoLite getDepositoOrigine() {
		return depositoOrigine;
	}

	/**
	 * @return the descrizionePerStampa
	 */
	@Override
	public String getDescrizionePerStampa() {
		return descrizionePerStampa;
	}

	@Override
	public String getFormulaStandardNumeroCopie() {
		return "1";
	}

	@Override
	public String getReportPath() {
		return REPORT_PATH;
	}

	/**
	 * @return the tipoDocumento
	 */
	@Override
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return the tipoDocumentoEvasioneDescrizioneMaschera
	 */
	public String getTipoDocumentoEvasioneDescrizioneMaschera() {
		if (tipoDocumentoEvasioneDescrizioneMaschera == null) {
			tipoDocumentoEvasioneDescrizioneMaschera = "$descrizioneTipoDocumento$ numero  $numeroDocumento$ del $dataDocumento$";
		}
		return tipoDocumentoEvasioneDescrizioneMaschera;
	}

	/**
	 * @return the dataDocLikeDataReg
	 */
	public boolean isDataDocLikeDataReg() {
		return dataDocLikeDataReg;
	}

	/**
	 * @return the depositoOrigineBloccato
	 */
	public boolean isDepositoOrigineBloccato() {
		return depositoOrigineBloccato;
	}

	/**
	 * @return true se devo riportare le note sul documento di destinazione
	 */
	public boolean isNoteSuDestinazione() {
		return noteSuDestinazione;
	}

	/**
	 * @return the ordineProduzione
	 */
	public boolean isOrdineProduzione() {
		return ordineProduzione;
	}

	/**
	 * @param dataDocLikeDataReg
	 *            the dataDocLikeDataReg to set
	 */
	public void setDataDocLikeDataReg(boolean dataDocLikeDataReg) {
		this.dataDocLikeDataReg = dataDocLikeDataReg;
	}

	/**
	 * @param depositoOrigine
	 *            the depositoOrigine to set
	 */
	public void setDepositoOrigine(DepositoLite depositoOrigine) {
		this.depositoOrigine = depositoOrigine;
	}

	/**
	 * @param depositoOrigineBloccato
	 *            the depositoOrigineBloccato to set
	 */
	public void setDepositoOrigineBloccato(boolean depositoOrigineBloccato) {
		this.depositoOrigineBloccato = depositoOrigineBloccato;
	}

	/**
	 * @param descrizionePerStampa
	 *            the descrizionePerStampa to set
	 */
	public void setDescrizionePerStampa(String descrizionePerStampa) {
		this.descrizionePerStampa = descrizionePerStampa;
	}

	/**
	 * @param noteSuDestinazione
	 *            true se devo riportare le note sul documento di destinazione
	 */
	public void setNoteSuDestinazione(boolean noteSuDestinazione) {
		this.noteSuDestinazione = noteSuDestinazione;
	}

	/**
	 * @param ordineProduzione
	 *            the ordineProduzione to set
	 */
	public void setOrdineProduzione(boolean ordineProduzione) {
		this.ordineProduzione = ordineProduzione;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @param tipoDocumentoEvasioneDescrizioneMaschera
	 *            the tipoDocumentoEvasioneDescrizioneMaschera to set
	 */
	public void setTipoDocumentoEvasioneDescrizioneMaschera(String tipoDocumentoEvasioneDescrizioneMaschera) {
		this.tipoDocumentoEvasioneDescrizioneMaschera = tipoDocumentoEvasioneDescrizioneMaschera;
	}

	@Override
	public String toString() {
		return "TipoAreaOrdine [dataDocLikeDataReg=" + dataDocLikeDataReg + ", descrizionePerStampa="
				+ descrizionePerStampa + ", tipoDocumento=" + tipoDocumento + "]";
	}
}