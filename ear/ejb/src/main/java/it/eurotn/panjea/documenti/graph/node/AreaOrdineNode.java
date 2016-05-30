package it.eurotn.panjea.documenti.graph.node;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AreaOrdineNode extends AreaDocumentoNode {

	private static final long serialVersionUID = 6130612390937128360L;

	private Date dataRegistrazioneOrdine;

	private Boolean ordineEvaso;

	private String codiceSedeEntita;
	private String indirizzoSedeEntita;
	private String descrizioneSedeEntita;
	private String localitaSedeEntita;

	private StatoAreaOrdine statoAreaOrdine;

	/**
	 * Costruttore.
	 */
	public AreaOrdineNode() {
		super();
	}

	@Override
	public String getHTMLDescription() {

		StringBuilder sb = new StringBuilder();
		sb.append("<table cellpadding='2' cellspacing='0' border='0'>");
		sb.append("<tr><td><b>Area Ordine</b></td></tr>");

		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0'");
		sb.append("<tr><td>");
		sb.append(codiceSedeEntita);
		sb.append(" - ");
		sb.append(descrizioneSedeEntita);
		sb.append(" - ");
		sb.append(indirizzoSedeEntita);
		sb.append(" - ");
		sb.append(localitaSedeEntita);
		sb.append("</td></tr></table>");

		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0' valign='top'><tr>");
		sb.append("<td>Data</td>");
		sb.append("<td>");
		sb.append(new SimpleDateFormat("dd/MM/yyyy").format(dataRegistrazioneOrdine));
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>Stato</td>");
		sb.append("<td>");
		sb.append(statoAreaOrdine.name());
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>Evaso</td>");
		sb.append("<td>");
		sb.append(ordineEvaso ? "Si" : "No");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");

		return sb.toString();
	}

	@Override
	public String getTipoArea() {
		return AreaOrdine.class.getName();
	}

	/**
	 * @param codiceSedeEntita
	 *            The codiceSedeEntita to set.
	 */
	public void setCodiceSedeEntita(String codiceSedeEntita) {
		this.codiceSedeEntita = codiceSedeEntita;
	}

	/**
	 * @param descrizioneSedeEntita
	 *            The descrizioneSedeEntita to set.
	 */
	public void setDescrizioneSedeEntita(String descrizioneSedeEntita) {
		this.descrizioneSedeEntita = descrizioneSedeEntita;
	}

	/**
	 * @param indirizzoSedeEntita
	 *            The indirizzoSedeEntita to set.
	 */
	public void setIndirizzoSedeEntita(String indirizzoSedeEntita) {
		this.indirizzoSedeEntita = indirizzoSedeEntita;
	}

	/**
	 * @param localitaSedeEntita
	 *            The localitaSedeEntita to set.
	 */
	public void setLocalitaSedeEntita(String localitaSedeEntita) {
		this.localitaSedeEntita = localitaSedeEntita;
	}

	/**
	 * @param ordineEvaso
	 *            The ordineEvaso to set.
	 */
	public void setOrdineEvaso(Boolean ordineEvaso) {
		this.ordineEvaso = ordineEvaso;
	}

	/**
	 * @param statoAreaOrdine
	 *            The statoAreaOrdine to set.
	 */
	public void setStatoAreaOrdine(StatoAreaOrdine statoAreaOrdine) {
		this.statoAreaOrdine = statoAreaOrdine;
	}

}
