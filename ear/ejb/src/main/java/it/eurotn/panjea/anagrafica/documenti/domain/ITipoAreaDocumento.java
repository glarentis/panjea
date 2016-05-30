package it.eurotn.panjea.anagrafica.documenti.domain;

public interface ITipoAreaDocumento {

	/**
	 * @return the descrizionePerStampa
	 */
	String getDescrizionePerStampa();

	/**
	 *
	 * @return formula standard per il numero di copie da mettere sul layout
	 */
	String getFormulaStandardNumeroCopie();

	/**
	 * @return id
	 */
	Integer getId();

	/**
	 *
	 * @return percorso dove risiedono i report per quel tipo documento
	 */
	String getReportPath();

	/**
	 * @return tipoDocumento associato
	 */
	TipoDocumento getTipoDocumento();
}
