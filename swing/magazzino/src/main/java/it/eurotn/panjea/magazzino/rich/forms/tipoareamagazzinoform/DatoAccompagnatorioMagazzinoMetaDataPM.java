package it.eurotn.panjea.magazzino.rich.forms.tipoareamagazzinoform;

import it.eurotn.panjea.magazzino.domain.documento.DatoAccompagnatorioMagazzinoMetaData;

import java.io.Serializable;

public class DatoAccompagnatorioMagazzinoMetaDataPM implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4957492469044520565L;
	private final DatoAccompagnatorioMagazzinoMetaData datoAccompagnatorio;
	private boolean richiesto;

	/**
	 * costruttore.
	 */
	public DatoAccompagnatorioMagazzinoMetaDataPM() {
		this((DatoAccompagnatorioMagazzinoMetaData) null);
	}

	/**
	 * Richiesto viene impostato a true se l'id di datoAccompagnatorio non è null.
	 * 
	 * @param datoAccompagnatorioMetaData
	 *            datoAccompagnatorioMetaData
	 */
	public DatoAccompagnatorioMagazzinoMetaDataPM(final DatoAccompagnatorioMagazzinoMetaData datoAccompagnatorioMetaData) {
		this.datoAccompagnatorio = datoAccompagnatorioMetaData;
		if (datoAccompagnatorio != null) {
			richiesto = datoAccompagnatorio.getId() != null;
		}
	}

	/**
	 * Crea un nuovo dato accompagnatorio del tipo indicato.
	 * 
	 * @param tipo
	 *            Tipo dato accompagnatorio.
	 */
	public DatoAccompagnatorioMagazzinoMetaDataPM(
			final DatoAccompagnatorioMagazzinoMetaData.TipoDatoAccompagnatorioMagazzino tipo) {
		this(new DatoAccompagnatorioMagazzinoMetaData(tipo));
	}

	/**
	 * 
	 * @return dato accompagnatorio.
	 */
	public DatoAccompagnatorioMagazzinoMetaData getDatoAccompagnatorio() {
		return datoAccompagnatorio;
	}

	/**
	 * 
	 * @return vero se il dato accompagnatorio è richiesto.
	 */
	public boolean isRichiesto() {
		return richiesto;
	}

	/**
	 * 
	 * @param richiesto
	 *            richiesto
	 */
	public void setRichiesto(boolean richiesto) {
		this.richiesto = richiesto;
	}
}
