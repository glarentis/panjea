package it.eurotn.panjea.intra.manager.esportazione;

import java.text.DecimalFormat;
import java.text.Format;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class Intestazione {
	private String codiceUA;
	private int progressivoGiorno;

	private String sezioneDoganale;

	private String codiceFiscaleSpedizionere;

	private String progessivoSede;
	private int numeroRecord;

	/**
	 * @return Returns the codiceFiscaleSpedizionere.
	 */
	public String getCodiceFiscaleSpedizionere() {
		// robe da pazzi
		String result = "";
		if (StringUtils.isNumeric(codiceFiscaleSpedizionere)) {
			result = StringUtils.rightPad(codiceFiscaleSpedizionere, 16, " ");
		} else {
			result = StringUtils.leftPad(codiceFiscaleSpedizionere, 16, " ");
		}
		return result;
	}

	/**
	 * @return Returns the codiceUA.
	 */
	public String getCodiceUA() {
		return codiceUA;
	}

	/**
	 * @return Returns the nomeFlusso.
	 */
	public String getNomeFlusso() {
		DateTime date = new org.joda.time.DateTime();
		StringBuilder sbNomeFlusso = new StringBuilder();
		Format format = new DecimalFormat("00");

		sbNomeFlusso.append(getCodiceUA() != null ? getCodiceUA() : "");
		sbNomeFlusso.append(format.format(date.getMonthOfYear()));
		sbNomeFlusso.append(format.format(date.getDayOfMonth()));
		sbNomeFlusso.append(".I");
		sbNomeFlusso.append(format.format(getProgressivoGiorno()));
		return sbNomeFlusso.toString();
	}

	/**
	 * @return Returns the numeroRecord.
	 */
	public int getNumeroRecord() {
		return numeroRecord;
	}

	/**
	 * @return Returns the progessivoSede.
	 */
	public String getProgessivoSede() {
		return progessivoSede;
	}

	/**
	 * @return Returns the progressivoGiorno.
	 */
	public int getProgressivoGiorno() {
		return progressivoGiorno;
	}

	/**
	 * @return Returns the sezioneDoganale.
	 */
	public String getSezioneDoganale() {
		return sezioneDoganale;
	}

	/**
	 * @param codiceFiscaleSpedizionere
	 *            The codiceFiscaleSpedizionere to set.
	 */
	public void setCodiceFiscaleSpedizionere(String codiceFiscaleSpedizionere) {
		this.codiceFiscaleSpedizionere = codiceFiscaleSpedizionere;
	}

	/**
	 * @param codiceUA
	 *            The codiceUA to set.
	 */
	public void setCodiceUA(String codiceUA) {
		this.codiceUA = codiceUA;
	}

	/**
	 * @param numeroRecord
	 *            The numeroRecord to set.
	 */
	public void setNumeroRecord(int numeroRecord) {
		this.numeroRecord = numeroRecord;
	}

	/**
	 * @param progessivoSede
	 *            The progessivoSede to set.
	 */
	public void setProgessivoSede(String progessivoSede) {
		this.progessivoSede = progessivoSede;
	}

	/**
	 * @param progressivoGiorno
	 *            The progressivoGiorno to set.
	 */
	public void setProgressivoGiorno(int progressivoGiorno) {
		this.progressivoGiorno = progressivoGiorno;
	}

	/**
	 * @param sezioneDoganale
	 *            The sezioneDoganale to set.
	 */
	public void setSezioneDoganale(String sezioneDoganale) {
		this.sezioneDoganale = sezioneDoganale;
	}

}
