package it.eurotn.panjea.conai.domain;

import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class ConaiParametriCreazione implements Serializable {

	public enum PERIODICITA {
		ANNUALE, TRIMESTRALE, MENSILE;

		/**
		 * 
		 * @param parametroAnno
		 *            anno per il calcolo
		 * @param parametroTrimestre
		 *            trimestre per il calcolo
		 * @param parametroMese
		 *            mese per il calcolo
		 * @return data finale del periodo
		 */
		public Date calcolaPeriodoFinale(int parametroAnno, int parametroTrimestre, int parametroMese) {
			Calendar calendar = Calendar.getInstance();
			if ("ANNUALE".equals(name())) {
				calendar.set(parametroAnno, 11, 31);
			} else if ("TRIMESTRALE".equals(name())) {
				calendar.set(parametroAnno, (parametroTrimestre * 3) - 1, 1);

			} else if ("MENSILE".equals(name())) {
				calendar.set(parametroAnno, parametroMese, 1);
			}
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			return calendar.getTime();
		}

		/**
		 * 
		 * @param parametroAnno
		 *            anno per il calcolo
		 * @param parametroTrimestre
		 *            trimestre per il calcolo
		 * @param parametroMese
		 *            mese per il calcolo
		 * @return data iniziale del periodo
		 */
		public Date calcolaPeriodoIniziale(int parametroAnno, Integer parametroTrimestre, Integer parametroMese) {
			Calendar calendar = Calendar.getInstance();
			if ("ANNUALE".equals(name())) {
				calendar.set(parametroAnno, 0, 1);
			} else if ("TRIMESTRALE".equals(name())) {
				calendar.set(parametroAnno, parametroTrimestre * 3 - 3, 1);
			} else if ("MENSILE".equals(name())) {
				calendar.set(parametroAnno, parametroMese, 1);
			}
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			return calendar.getTime();
		}
	}

	private static final long serialVersionUID = 5952138453539339011L;;

	private ConaiMateriale materiale;
	private PERIODICITA periodicita;
	private String anno;
	private Integer mese;
	private String trimestre;

	private String referente;
	private String telReferente;
	private String faxReferente;
	private String emailReferente;

	private String indirizzoFatturazione;

	private Date data;

	private BigDecimal prezzoMateriale;

	private String pathCreazione;

	/**
	 * Costruttore.
	 */
	public ConaiParametriCreazione() {
		anno = new Integer(Calendar.getInstance().get(Calendar.YEAR)).toString();
		data = Calendar.getInstance().getTime();
		periodicita = PERIODICITA.ANNUALE;
		materiale = ConaiMateriale.ACCIAIO;
		prezzoMateriale = BigDecimal.ZERO;
	}

	/**
	 * @return Returns the anno.
	 */
	public String getAnno() {
		return anno;
	}

	/**
	 * @return Returns the data.
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @return data iniziale per la generazione del contributo
	 */
	public Date getDataFinale() {
		Integer iTrimestre = 0;
		try {
			iTrimestre = Integer.parseInt(trimestre);
		} catch (NumberFormatException e) {
			iTrimestre = 0;
		}
		if (mese == null) {
			mese = 0;
		}

		return periodicita.calcolaPeriodoFinale(Integer.parseInt(anno), iTrimestre, mese);
	}

	/**
	 * @return data finaleper la generazione deo contributo
	 */
	public Date getDataIniziale() {
		Integer iTrimestre = 0;
		try {
			iTrimestre = Integer.parseInt(trimestre);
		} catch (NumberFormatException e) {
			iTrimestre = 0;
		}
		if (mese == null) {
			mese = 0;
		}

		return periodicita.calcolaPeriodoIniziale(Integer.parseInt(anno), iTrimestre, mese);
	}

	/**
	 * @return the emailReferente
	 */
	public String getEmailReferente() {
		return emailReferente;
	}

	/**
	 * @return the faxReferente
	 */
	public String getFaxReferente() {
		return faxReferente;
	}

	/**
	 * @return Returns the indirizzoFatturazione.
	 */
	public String getIndirizzoFatturazione() {
		return indirizzoFatturazione;
	}

	/**
	 * @return Returns the materiale.
	 */
	public ConaiMateriale getMateriale() {
		return materiale;
	}

	/**
	 * @return Returns the mese.
	 */
	public Integer getMese() {
		return mese;
	}

	/**
	 * @return Returns the pathCreazione.
	 */
	public String getPathCreazione() {
		return pathCreazione;
	}

	/**
	 * @return Returns the periodicita.
	 */
	public PERIODICITA getPeriodicita() {
		return periodicita;
	}

	/**
	 * @return the prezzoMateriale
	 */
	public BigDecimal getPrezzoMateriale() {
		return prezzoMateriale;
	}

	/**
	 * @return Returns the referente.
	 */
	public String getReferente() {
		return referente;
	}

	/**
	 * @return the telReferente
	 */
	public String getTelReferente() {
		return telReferente;
	}

	/**
	 * @return Returns the trimestre.
	 */
	public String getTrimestre() {
		return trimestre;
	}

	/**
	 * @param anno
	 *            The anno to set.
	 */
	public void setAnno(String anno) {
		this.anno = anno;
	}

	/**
	 * @param data
	 *            The data to set.
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @param emailReferente
	 *            the emailReferente to set
	 */
	public void setEmailReferente(String emailReferente) {
		this.emailReferente = emailReferente;
	}

	/**
	 * @param faxReferente
	 *            the faxReferente to set
	 */
	public void setFaxReferente(String faxReferente) {
		this.faxReferente = faxReferente;
	}

	/**
	 * @param indirizzoFatturazione
	 *            The indirizzoFatturazione to set.
	 */
	public void setIndirizzoFatturazione(String indirizzoFatturazione) {
		this.indirizzoFatturazione = indirizzoFatturazione;
	}

	/**
	 * @param materiale
	 *            The materiale to set.
	 */
	public void setMateriale(ConaiMateriale materiale) {
		this.materiale = materiale;
	}

	/**
	 * @param mese
	 *            The mese to set.
	 */
	public void setMese(Integer mese) {
		this.mese = mese;
	}

	/**
	 * @param pathCreazione
	 *            The pathCreazione to set.
	 */
	public void setPathCreazione(String pathCreazione) {
		this.pathCreazione = pathCreazione;
	}

	/**
	 * @param periodicita
	 *            The periodicita to set.
	 */
	public void setPeriodicita(PERIODICITA periodicita) {
		this.periodicita = periodicita;
	}

	/**
	 * @param prezzoMateriale
	 *            the prezzoMateriale to set
	 */
	public void setPrezzoMateriale(BigDecimal prezzoMateriale) {
		this.prezzoMateriale = prezzoMateriale;
	}

	/**
	 * @param referente
	 *            The referente to set.
	 */
	public void setReferente(String referente) {
		this.referente = referente;
	}

	/**
	 * @param telReferente
	 *            the telReferente to set
	 */
	public void setTelReferente(String telReferente) {
		this.telReferente = telReferente;
	}

	/**
	 * @param trimestre
	 *            The trimestre to set.
	 */
	public void setTrimestre(String trimestre) {
		this.trimestre = trimestre;
	}

	@Override
	public String toString() {
		return "ConaiParametriCreazione [periodicita=" + periodicita + ", referente=" + referente + ", data=" + data
				+ ", indirizzoFatturazione=" + indirizzoFatturazione + ", materiale=" + materiale + ", anno=" + anno
				+ ", mese=" + mese + ", trimestre=" + trimestre + ", pathCreazione=" + pathCreazione + "]";
	}

}
