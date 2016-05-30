package it.eurotn.panjea.documenti.graph.node;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AreaMagazzinoNode extends AreaDocumentoNode {

	private static final long serialVersionUID = -6505932487829650855L;

	private Date dataRegistrazione;

	private String codiceSedeEntita;
	private String indirizzoSedeEntita;
	private String descrizioneSedeEntita;
	private String localitaSedeEntita;

	private StatoAreaMagazzino statoAreaMagazzino;

	@Override
	public String getHTMLDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table cellpadding='2' cellspacing='0' border='0'>");
		sb.append("<tr><td><b>Area Magazzino</b></td></tr>");

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
		sb.append("<td>Data registrazione</td>");
		sb.append("<td>");
		sb.append(new SimpleDateFormat("dd/MM/yyyy").format(dataRegistrazione));
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>Stato</td>");
		sb.append("<td>");
		sb.append(statoAreaMagazzino.name());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");

		return sb.toString();
	}

	@Override
	public String getTipoArea() {
		return AreaMagazzino.class.getName();
	}

	/**
	 * @param codiceSedeEntita
	 *            The codiceSedeEntita to set.
	 */
	public void setCodiceSedeEntita(String codiceSedeEntita) {
		this.codiceSedeEntita = codiceSedeEntita;
	}

	/**
	 * @param dataRegistrazione
	 *            The dataRegistrazione to set.
	 */
	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
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
	 * @param statoAreaMagazzino
	 *            The statoAreaMagazzino to set.
	 */
	public void setStatoAreaMagazzino(StatoAreaMagazzino statoAreaMagazzino) {
		this.statoAreaMagazzino = statoAreaMagazzino;
	}

}
