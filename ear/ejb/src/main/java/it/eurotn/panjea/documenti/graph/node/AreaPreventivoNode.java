package it.eurotn.panjea.documenti.graph.node;

import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AreaPreventivoNode extends AreaDocumentoNode {

	private static final long serialVersionUID = 6130612390937128360L;

	private Date dataRegistrazionePreventivo;

	private String codiceSedeEntita;
	private String indirizzoSedeEntita;
	private String descrizioneSedeEntita;
	private String localitaSedeEntita;

	private StatoAreaPreventivo statoAreaPreventivo;

	/**
	 * Costruttore.
	 */
	public AreaPreventivoNode() {
		super();
	}

	@Override
	public String getHTMLDescription() {

		StringBuilder sb = new StringBuilder();
		sb.append("<table cellpadding='2' cellspacing='0' border='0'>");
		sb.append("<tr><td><b>Area Preventivo</b></td></tr>");

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
		sb.append(new SimpleDateFormat("dd/MM/yyyy").format(dataRegistrazionePreventivo));
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>Stato</td>");
		sb.append("<td>");
		sb.append(statoAreaPreventivo.name());
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("</table>");

		return sb.toString();
	}

	@Override
	public String getTipoArea() {
		return AreaPreventivo.class.getName();
	}

	/**
	 * @param codiceSedeEntita
	 *            The codiceSedeEntita to set.
	 */
	public void setCodiceSedeEntita(String codiceSedeEntita) {
		this.codiceSedeEntita = codiceSedeEntita;
	}

	/**
	 * @param dataRegistrazionePreventivo
	 *            the dataRegistrazionePreventivo to set
	 */
	public void setDataRegistrazionePreventivo(Date dataRegistrazionePreventivo) {
		this.dataRegistrazionePreventivo = dataRegistrazionePreventivo;
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
	 * @param statoAreaPreventivo
	 *            The statoAreaPreventivo to set.
	 */
	public void setStatoAreaPreventivo(StatoAreaPreventivo statoAreaPreventivo) {
		this.statoAreaPreventivo = statoAreaPreventivo;
	}

}
