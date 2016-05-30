package it.eurotn.panjea.preventivi.domain.documento;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.preventivi.domain.documento.interfaces.ITipoAreaStampabile;
import it.eurotn.util.PanjeaEJBUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Table(name = "prev_tipi_area_preventivo")
@Audited
@Entity
@NamedQueries({
		@NamedQuery(name = "TipoAreaPreventivo.caricaByTipoDocumento", query = "select tao from TipoAreaPreventivo tao inner join tao.tipoDocumento td where td.id = :paramId "),
		@NamedQuery(name = "TipoAreaPreventivo.caricaTipiDocumentiPreventivo", query = " select ta.tipoDocumento from TipoAreaPreventivo ta where ta.tipoDocumento.codiceAzienda = :paramCodiceAzienda ") })
public class TipoAreaPreventivo extends EntityBase implements java.io.Serializable, Cloneable, ITipoAreaDocumento,
		ITipoAreaStampabile {

	private static final long serialVersionUID = 1L;

	public static final String REPORT_PATH = "Preventivi/Documenti";

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

	private Integer durataValiditaDefault;

	@ManyToOne
	private TipoAreaOrdine tipoDocumentoEvasione;

	private boolean processaSuAccettazione;

	/**
	 * inizializza le variabili.
	 */
	{
		this.tipoDocumento = new TipoDocumento();
	}

	@Override
	public Object clone() {
		TipoAreaPreventivo tipoAreaPreventivo = PanjeaEJBUtil.cloneObject(this);
		tipoAreaPreventivo.setId(null);
		return tipoAreaPreventivo;
	}

	/**
	 * @return the descrizionePerStampa
	 */
	@Override
	public String getDescrizionePerStampa() {
		return descrizionePerStampa;
	}

	/**
	 * @return the durataValiditaDefault
	 */
	public Integer getDurataValiditaDefault() {
		return durataValiditaDefault;
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
	 * @return the tipoDocumentoEvasione
	 */
	public TipoAreaOrdine getTipoDocumentoEvasione() {
		return tipoDocumentoEvasione;
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
	 * @return true se devo riportare le note sul documento di destinazione
	 */
	public boolean isNoteSuDestinazione() {
		return noteSuDestinazione;
	}

	/**
	 * @return the processaSuAccettazione
	 */
	public boolean isProcessaSuAccettazione() {
		return processaSuAccettazione;
	}

	/**
	 * @param dataDocLikeDataReg
	 *            the dataDocLikeDataReg to set
	 */
	public void setDataDocLikeDataReg(boolean dataDocLikeDataReg) {
		this.dataDocLikeDataReg = dataDocLikeDataReg;
	}

	/**
	 * @param descrizionePerStampa
	 *            the descrizionePerStampa to set
	 */
	public void setDescrizionePerStampa(String descrizionePerStampa) {
		this.descrizionePerStampa = descrizionePerStampa;
	}

	/**
	 * @param durataValiditaDefault
	 *            the durataValiditaDefault to set
	 */
	public void setDurataValiditaDefault(Integer durataValiditaDefault) {
		this.durataValiditaDefault = durataValiditaDefault;
	}

	/**
	 * @param noteSuDestinazione
	 *            true se devo riportare le note sul documento di destinazione
	 */
	public void setNoteSuDestinazione(boolean noteSuDestinazione) {
		this.noteSuDestinazione = noteSuDestinazione;
	}

	/**
	 * @param processaSuAccettazione
	 *            the processaSuAccettazione to set
	 */
	public void setProcessaSuAccettazione(boolean processaSuAccettazione) {
		this.processaSuAccettazione = processaSuAccettazione;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @param tipoDocumentoEvasione
	 *            the tipoDocumentoEvasione to set
	 */
	public void setTipoDocumentoEvasione(TipoAreaOrdine tipoDocumentoEvasione) {
		this.tipoDocumentoEvasione = tipoDocumentoEvasione;
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
		return "TipoAreaPreventivo [dataDocLikeDataReg=" + dataDocLikeDataReg + ", descrizionePerStampa="
				+ descrizionePerStampa + ", tipoDocumento=" + tipoDocumento + "]";
	}
}
