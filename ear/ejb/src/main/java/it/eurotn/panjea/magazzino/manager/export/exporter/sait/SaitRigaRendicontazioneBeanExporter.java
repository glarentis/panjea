package it.eurotn.panjea.magazzino.manager.export.exporter.sait;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.manager.export.exporter.RendicontazioneBeanExporter;

public class SaitRigaRendicontazioneBeanExporter extends RendicontazioneBeanExporter {

	private int progressivo;

	private String codiceArticoloEntita;

	/**
	 * Costruttore.
	 * 
	 * @param rigaArticolo
	 *            riga articolo
	 * @param progressivo
	 *            progressivo
	 * @param codiceArticoloEntita
	 *            codice articolo dell'entita
	 */
	public SaitRigaRendicontazioneBeanExporter(final RigaArticolo rigaArticolo, final int progressivo,
			final String codiceArticoloEntita) {
		super(null, rigaArticolo);
		this.progressivo = progressivo;
		this.codiceArticoloEntita = codiceArticoloEntita;
	}

	/**
	 * @return Returns the codiceArticoloEntita.
	 */
	public String getCodiceArticoloEntita() {
		return codiceArticoloEntita;
	}

	/**
	 * @return Returns the progressivo.
	 */
	public int getProgressivo() {
		return progressivo;
	}

	/**
	 * @return Returns the tipoIva.
	 */
	public String getTipoIva() {

		String result;

		switch (getRigaArticolo().getCodiceIva().getTipoCodiceIva()) {
		case ESCLUSO:
			result = "2";
			break;
		case ESENTE:
			result = "1";
			break;
		default:
			result = "";
			break;
		}

		return result;
	}

	/**
	 * @return Returns the tipoRecord.
	 */
	public int getTipoRecord() {
		return 2;
	}

	/**
	 * @return Returns the tipoReso.
	 */
	public String getTipoReso() {
		String result = "";
		if (getTipoDocumento().isNotaCreditoEnable()) {
			result = "1";
		}

		return result;
	}

	/**
	 * @param codiceArticoloEntita
	 *            The codiceArticoloEntita to set.
	 */
	public void setCodiceArticoloEntita(String codiceArticoloEntita) {
	}

	/**
	 * @param progressivo
	 *            The progressivo to set.
	 */
	public void setProgressivo(int progressivo) {
	}

	/**
	 * @param tipoIva
	 *            The tipoIva to set.
	 */
	public void setTipoIva(String tipoIva) {
	}

	/**
	 * @param tipoRecord
	 *            The tipoRecord to set.
	 */
	public void setTipoRecord(int tipoRecord) {
	}

	/**
	 * @param tipoReso
	 *            The tipoReso to set.
	 */
	public void setTipoReso(String tipoReso) {
	}

}
