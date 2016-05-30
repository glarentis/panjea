/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.export.exporter;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

public class RendicontazioneBeanExporter {

	private String codiceCliente;

	private AreaMagazzino areaMagazzino;

	private RigaArticolo rigaArticolo;

	/**
	 * Costruttore.
	 * 
	 * @param codiceCliente
	 *            codice cliente
	 * @param rigaArticolo
	 *            riga articolo
	 */
	public RendicontazioneBeanExporter(final String codiceCliente, final RigaArticolo rigaArticolo) {
		super();
		this.codiceCliente = codiceCliente;
		this.rigaArticolo = rigaArticolo;
		this.areaMagazzino = rigaArticolo.getAreaMagazzino();
	}

	/**
	 * @return Returns the articolo
	 */
	public ArticoloLite getArticolo() {
		return rigaArticolo.getArticolo();
	}

	/**
	 * @return Returns the barCode
	 */
	public String getBarCode() {

		String barCode = "";

		if (getArticolo().getBarCode() != null && getArticolo().getBarCode().length() > 1) {
			barCode = getArticolo().getBarCode();
		}

		return barCode;
	}

	/**
	 * @return Returns the codiceCliente.
	 */
	public String getCodiceCliente() {
		return codiceCliente;
	}

	/**
	 * @return Returns the documento.
	 */
	public Documento getDocumento() {
		return areaMagazzino.getDocumento();
	}

	/**
	 * @return Returns the rigaArticolo.
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}

	/**
	 * @return Returns the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return areaMagazzino.getDocumento().getSedeEntita();
	}

	/**
	 * @return Returns the tipoAreaMagazzino
	 */
	public TipoAreaMagazzino getTipoAreaMagazzino() {
		return areaMagazzino.getTipoAreaMagazzino();
	}

	/**
	 * @return Returns the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return areaMagazzino.getTipoAreaMagazzino().getTipoDocumento();
	}

	/**
	 * @return Returns the tipoVendita.
	 */
	public String getTipoVendita() {
		// vendita o omaggio
		String result = "V";

		if (rigaArticolo.isOmaggio()) {
			result = "O";
		}

		return result;
	}

	/**
	 * @param articoloLite
	 *            The articoloLite to set.
	 */
	public void setArticolo(ArticoloLite articoloLite) {
	}

	/**
	 * @param barCode
	 *            The barCode to set.
	 */
	public void setBarCode(String barCode) {
	}

	/**
	 * @param codiceCliente
	 *            The codiceCliente to set.
	 */
	public void setCodiceCliente(String codiceCliente) {
	}

	/**
	 * @param documento
	 *            The documento to set.
	 */
	public void setDocumento(Documento documento) {
	}

	/**
	 * @param rigaArticolo
	 *            The rigaArticolo to set.
	 */
	public void setRigaArticolo(RigaArticolo rigaArticolo) {
		this.rigaArticolo = rigaArticolo;
	}

	/**
	 * @param sedeEntita
	 *            The sedeEntita to set.
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
	}

	/**
	 * @param tipoAreaMagazzino
	 *            The tipoAreaMagazzino to set.
	 */
	public void setTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
	}

	/**
	 * @param tipoDocumento
	 *            The tipoDocumento to set.
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
	}

	/**
	 * @param tipoVendita
	 *            The tipoVendita to set.
	 */
	public void setTipoVendita(String tipoVendita) {
	}
}
