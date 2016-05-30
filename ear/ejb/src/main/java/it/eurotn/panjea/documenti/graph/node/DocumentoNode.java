package it.eurotn.panjea.documenti.graph.node;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DocumentoNode implements Serializable, Comparable<DocumentoNode> {

	private static final long serialVersionUID = 8040041465527472555L;

	private List<AreaDocumentoNode> areeDocumentoNode;

	private Integer idDocumento;
	private String codiceDocumento;

	private Date dataDocumento;

	protected String descrizioneTipoDocumento;

	private String classeTipoDocumento;

	private BigDecimal importoInValuta;

	private String codiceValuta;

	private Integer codiceEntita;
	private String descrizioneEntita;

	private String descrizioneCollegamento;

	private String codiceAzienda;

	private String descrizioneRapportoBancario;

	private TipoEntita tipoEntitaTipoDocumento;
	private String colore;
	private boolean fullDescription;
	/**
	 * La mappa dei nodi precedenti e successivi è composta dalla chiave che rappresenta il nodo e dal valore che
	 * rappresenta il tipo di relazione con il nodo di riferimento.
	 */
	private Map<DocumentoNode, String> nextNodes;

	private Map<DocumentoNode, String> previousNodes;

	private Set<DocumentoNode> linkNodesDestinazione;

	private Set<DocumentoNode> linkNodesOrigine;

	/**
	 * Costruttore.
	 */
	public DocumentoNode() {
		super();
		this.areeDocumentoNode = new ArrayList<AreaDocumentoNode>();

		this.nextNodes = new HashMap<DocumentoNode, String>();
		this.previousNodes = new HashMap<DocumentoNode, String>();
		this.linkNodesDestinazione = new TreeSet<DocumentoNode>();
		this.linkNodesOrigine = new TreeSet<DocumentoNode>();

		this.fullDescription = false;
	}

	@Override
	public int compareTo(DocumentoNode o) {
		return this.idDocumento.compareTo(o.getIdDocumento());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DocumentoNode other = (DocumentoNode) obj;
		if (idDocumento == null) {
			if (other.idDocumento != null) {
				return false;
			}
		} else if (!idDocumento.equals(other.idDocumento)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the areeDocumentoNode.
	 */
	public List<AreaDocumentoNode> getAreeDocumentoNode() {
		return areeDocumentoNode;
	}

	/**
	 * @return Returns the classeTipoDocumento.
	 */
	public String getClasseTipoDocumento() {
		return classeTipoDocumento;
	}

	/**
	 * @return the codiceDocumento
	 */
	public String getCodiceDocumento() {
		return codiceDocumento;
	}

	/**
	 * @return Returns the colore.
	 */
	public String getColore() {
		if (colore == null) {
			return "#FFFF00";
		}
		return colore;
	}

	/**
	 *
	 * @return stringa di descrizione del collegamento.
	 */
	public String getDescrizioneCollegamento() {
		return descrizioneCollegamento;
	}

	/**
	 * Restituisce la descrizione del documento.
	 *
	 * @return descrzione
	 */
	public String getDescrizioneDocumento() {
		StringBuilder sb = new StringBuilder();

		sb.append(descrizioneTipoDocumento);
		sb.append(" n. ");
		sb.append(codiceDocumento);
		return sb.toString();
	}

	/**
	 * Restituisce la descrizione dell'entità del documento.
	 *
	 * @return descrzione
	 */
	private String getDescrizioneEntita() {
		StringBuilder sb = new StringBuilder();

		switch (tipoEntitaTipoDocumento) {
		case AZIENDA:
			sb.append(codiceAzienda);
			break;
		case BANCA:
			sb.append(descrizioneRapportoBancario);
			break;
		default:
			sb.append(descrizioneEntita);
			sb.append(" - ");
			sb.append(codiceEntita);
			break;
		}
		return sb.toString();
	}

	/**
	 * Restituisce la descrizione HTML del nodo cone le sue aree.
	 *
	 * @return descrizione
	 */
	public String getHTMLDescription() {

		StringBuilder sb = new StringBuilder();
		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0'><tr>");

		// descrizione documento
		sb.append("<td align='center'><b>");
		sb.append(getDescrizioneDocumento());
		sb.append("</b></td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td><hr></td>");
		sb.append("</tr>");
		sb.append("</table>");

		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0' valign='top'><tr>");
		sb.append("<td>");
		sb.append(getDescrizioneEntita());
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");

		sb.append("<table width='100%' cellpadding='2' cellspacing='0' border='0' valign='top'><tr>");
		sb.append("<td>Data</td>");
		sb.append("<td>");
		sb.append(new SimpleDateFormat("dd/MM/yyyy").format(dataDocumento));
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td>Totale</td>");
		sb.append("<td>");
		sb.append(new DecimalFormat("#,##0.00####").format(importoInValuta));
		sb.append(" ");
		sb.append(codiceValuta);
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");

		if (fullDescription) {
			for (AreaDocumentoNode node : areeDocumentoNode) {
				sb.append(node.getHTMLDescription());
			}
		}

		return sb.toString();
	}

	/**
	 * @return Returns the idDocumento.
	 */
	public Integer getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @return Returns the linkNodes.
	 */
	public Set<DocumentoNode> getLinkNodesDestinazione() {
		return linkNodesDestinazione;
	}

	/**
	 * @return Returns the linkNodesOrigine.
	 */
	public Set<DocumentoNode> getLinkNodesOrigine() {
		return linkNodesOrigine;
	}

	/**
	 * @return Returns the nextNodes.
	 */
	public Map<DocumentoNode, String> getNextNodes() {
		return nextNodes;
	}

	/**
	 * @return Returns the previousNodes.
	 */
	public Map<DocumentoNode, String> getPreviousNodes() {
		return previousNodes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDocumento == null) ? 0 : idDocumento.hashCode());
		return result;
	}

	/**
	 * @return Returns the fullDescription.
	 */
	public boolean isFullDescription() {
		return fullDescription;
	}

	/**
	 * @param areeDocumentoNode
	 *            The areeDocumentoNode to set.
	 */
	public void setAreeDocumentoNode(List<AreaDocumentoNode> areeDocumentoNode) {
		this.areeDocumentoNode = areeDocumentoNode;
	}

	/**
	 * @param classeTipoDocumento
	 *            The classeTipoDocumento to set.
	 */
	public void setClasseTipoDocumento(String classeTipoDocumento) {
		this.classeTipoDocumento = classeTipoDocumento;
	}

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param codiceDocumento
	 *            The codiceDocumento to set.
	 */
	public void setCodiceDocumento(String codiceDocumento) {
		this.codiceDocumento = codiceDocumento;
	}

	/**
	 * @param codiceEntita
	 *            The codiceEntita to set.
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		this.codiceEntita = codiceEntita;
	}

	/**
	 * @param codiceValuta
	 *            The codiceValuta to set.
	 */
	public void setCodiceValuta(String codiceValuta) {
		this.codiceValuta = codiceValuta;
	}

	/**
	 * @param colore
	 *            The colore to set.
	 */
	public void setColore(String colore) {
		this.colore = colore;
	}

	/**
	 * @param dataDocumento
	 *            The dataDocumento to set.
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param descrizioneCollegamento
	 *            The descrizioneCollegamento to set.
	 */
	public void setDescrizioneCollegamento(String descrizioneCollegamento) {
		this.descrizioneCollegamento = descrizioneCollegamento;
	}

	/**
	 * @param descrizioneEntita
	 *            The descrizioneEntita to set.
	 */
	public void setDescrizioneEntita(String descrizioneEntita) {
		this.descrizioneEntita = descrizioneEntita;
	}

	/**
	 * @param descrizioneRapportoBancario
	 *            The descrizioneRapportoBancario to set.
	 */
	public void setDescrizioneRapportoBancario(String descrizioneRapportoBancario) {
		this.descrizioneRapportoBancario = descrizioneRapportoBancario;
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            The descrizioneTipoDocumento to set.
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.descrizioneTipoDocumento = descrizioneTipoDocumento;
	}

	/**
	 * @param fullDescription
	 *            The fullDescription to set.
	 */
	public void setFullDescription(boolean fullDescription) {
		this.fullDescription = fullDescription;
	}

	/**
	 * @param idDocumento
	 *            the idDocumento to set
	 */
	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	/**
	 * @param importoInValuta
	 *            The importoInValuta to set.
	 */
	public void setImportoInValuta(BigDecimal importoInValuta) {
		this.importoInValuta = importoInValuta;
	}

	/**
	 * @param tipoEntitaTipoDocumento
	 *            The tipoEntitaTipoDocumento to set.
	 */
	public void setTipoEntitaTipoDocumento(TipoEntita tipoEntitaTipoDocumento) {
		this.tipoEntitaTipoDocumento = tipoEntitaTipoDocumento;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("ID documento: " + idDocumento + "\n");
		sb.append(super.toString());

		sb.append("Punto a " + nextNodes.size() + " documenti:\n");
		for (DocumentoNode nextNode : nextNodes.keySet()) {
			sb.append("Documento " + nextNode.getIdDocumento());
			sb.append("\n");
		}

		sb.append("Vengo puntato da " + previousNodes.size() + " documenti:\n");
		for (DocumentoNode prevNode : previousNodes.keySet()) {
			sb.append("Documento " + prevNode.getIdDocumento());
			sb.append("\n");
		}

		sb.append("\n --------------------------- Proprietà aree --------------------------- \n");
		for (AreaDocumentoNode node : areeDocumentoNode) {
			sb.append(node);
			sb.append("\n");
		}

		return sb.toString();
	}
}
