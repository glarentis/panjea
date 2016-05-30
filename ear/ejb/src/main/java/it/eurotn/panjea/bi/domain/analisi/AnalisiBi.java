package it.eurotn.panjea.bi.domain.analisi;

import it.eurotn.entity.EntityBase;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * Contiene i parametri per le analisi del datawarehouse.
 *
 * @author giangi
 */
@Entity
@Audited
@Table(name = "bi_analisi", uniqueConstraints = @UniqueConstraint(columnNames = { "nome", "categoria" }))
@NamedQueries({ @NamedQuery(name = "AnalisiBi.caricaByNomeCategoria", query = "select a from AnalisiBi a left join fetch a.filtri inner join fetch a.layouts where a.nome=:nomeAnalisi and a.categoria=:categoriaAnalisi") })
public class AnalisiBi extends EntityBase {

	private static final long serialVersionUID = 4757473115159263946L;

	private static final String NOME_NUOVA_ANALISI = "Nuova analisi";

	@Column(length = 50)
	private String nome;

	@Column(length = 50)
	private String categoria;

	@Column(length = 50)
	private String descrizione;

	@NotAudited
	@Lob
	private byte[] chartConfiguration;

	@NotAudited
	@Lob
	private byte[] pivotLayout; // Disposizione delle pagine

	@NotAudited
	@Lob
	private byte[] dockingLayout; // Disposizione delle pagine

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@MapKey(name = "nomeCampo")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.JOIN)
	private Map<String, AnalisiValueSelected> filtri;

	@CollectionOfElements(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "bi_analisi_inputcontrols")
	@NotAudited
	private List<String> inputControls;

	// @Column(length = 200)
	// private String pathTemplateJRXML;

	private boolean flatLayout;

	// mappo una one to many solo perchè non posso avere una one to one con la delete orphan
	@NotAudited
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "analisiBi_id")
	private List<AnalisiBILayout> layouts;

	private boolean alternativeJoin;

	private boolean raggruppaGrafico;

	private boolean showFieldList;

	@Column(length = 400)
	private String nomeFileMappa;

	{
		nome = NOME_NUOVA_ANALISI;
		categoria = "";
		alternativeJoin = false;
		raggruppaGrafico = false;
		nomeFileMappa = "";
	}

	/**
	 * Costruttore.
	 */
	public AnalisiBi() {
	}

	/**
	 * Esegue una copia dell'analisi.
	 *
	 * @return copia dell'analisi
	 */
	public AnalisiBi copia() {
		AnalisiBi nuovaAnalisi = PanjeaEJBUtil.cloneObject(this);
		nuovaAnalisi.setVersion(0);
		nuovaAnalisi.setId(null);
		nuovaAnalisi.setNome(nome + "copia");

		if (nuovaAnalisi.getAnalisiLayout() != null) {
			nuovaAnalisi.getAnalisiLayout().setId(null);
			nuovaAnalisi.getAnalisiLayout().setVersion(null);
			for (Entry<String, FieldBILayout> field : nuovaAnalisi.getAnalisiLayout().getFields().entrySet()) {
				field.getValue().setId(null);
				field.getValue().setVersion(null);
			}
		}

		if (nuovaAnalisi.getFiltri() != null) {
			for (Entry<String, AnalisiValueSelected> filtroEntry : nuovaAnalisi.getFiltri().entrySet()) {
				filtroEntry.getValue().setId(null);
				filtroEntry.getValue().setVersion(null);
			}
		}
		inputControls = new ArrayList<String>();
		return nuovaAnalisi;
	}

	/**
	 * @return Returns the analisiLayout.
	 */
	public AnalisiBILayout getAnalisiLayout() {
		if (layouts != null && !layouts.isEmpty()) {
			return layouts.get(0);
		}
		return null;
	}

	/**
	 * @return categoria
	 */
	public String getCategoria() {
		if (categoria.isEmpty()) {
			return "";
		}
		return categoria;
	}

	/**
	 * @return Returns the chartConfiguration.
	 */
	public byte[] getChartConfiguration() {
		return chartConfiguration;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return docking layout
	 */
	public byte[] getDockingLayout() {
		return dockingLayout;
	}

	/**
	 * @return mappa dei filtri associati all'analisi. La chiave è il nome del campo
	 */
	public Map<String, AnalisiValueSelected> getFiltri() {
		return filtri;
	}

	/**
	 *
	 * @param colonna
	 *            colonna con l'inputControl associato
	 * @return id dell'input control associato, Vuoto se non ho nessun input control
	 */
	public String getInputControl(String colonna) {
		String idControl = "";
		for (String inputControl : getInputControls()) {
			if (inputControl.endsWith(colonna)) {
				idControl = inputControl;
			}
		}
		return idControl;
	}

	/**
	 * @return Returns the inputControls.
	 */
	public List<String> getInputControls() {
		return inputControls;
	}

	/**
	 * @return nome dell'analisi
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return the nomeFileMappa
	 */
	public String getNomeFileMappa() {
		return nomeFileMappa;
	}

	/**
	 *
	 * @return node dell'analisi senza caratteri . e spazi
	 */
	public String getNomeSafe() {
		return nome.replaceAll("\\.", "").replaceAll(" ", "_");
	}

	/**
	 * @return Returns the pathTemplateJRXML.
	 */
	public String getPathTemplateJRXML() {
		return "";// pathTemplateJRXML;
	}

	/**
	 * @return Returns the pivotLayout.
	 */
	public byte[] getPivotLayout() {
		return pivotLayout;
	}

	/**
	 *
	 * @return true se attivo il join alternativo delle tabelle verso la tabella dei fatti. Solitamente una left join al
	 *         posto di una inner.
	 */
	public boolean isAlternativeJoin() {
		return alternativeJoin;
	}

	/**
	 * @return Returns the flatLayout.
	 */
	public boolean isFlatLayout() {
		return flatLayout;
	}

	/**
	 * @return true se l'analisi non è stata salvata.
	 */
	public boolean isNuova() {
		return NOME_NUOVA_ANALISI.equals(nome);
	}

	/**
	 * @return Returns the raggruppaGrafico.
	 */
	public boolean isRaggruppaGrafico() {
		return raggruppaGrafico;
	}

	/**
	 * @return Returns the showFieldList.
	 */
	public boolean isShowFieldList() {
		return showFieldList;
	}

	/**
	 * @param alternativeJoin
	 *            The alternativeJoin to set.
	 */
	public void setAlternativeJoin(boolean alternativeJoin) {
		this.alternativeJoin = alternativeJoin;
	}

	/**
	 * @param analisiLayout
	 *            the analisiLayout to set
	 */
	public void setAnalisiLayout(AnalisiBILayout analisiLayout) {
		if (layouts == null) {
			layouts = new ArrayList<AnalisiBILayout>();
		}
		layouts.clear();
		layouts.add(analisiLayout);
	}

	/**
	 * @param categoria
	 *            categoria
	 */
	public void setCategoria(String categoria) {
		if (categoria == null) {
			categoria = "";
		}
		this.categoria = categoria;
	}

	/**
	 * @param chartConfiguration
	 *            The chartConfiguration to set.
	 */
	public void setChartConfiguration(byte[] chartConfiguration) {
		this.chartConfiguration = chartConfiguration;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param dockingLayout
	 *            layout del docking
	 */
	public void setDockingLayout(byte[] dockingLayout) {
		this.dockingLayout = dockingLayout;
	}

	/**
	 * @param filtri
	 *            filtri associati all'analisi. La chiave è il nome del campo.
	 */
	public void setFiltri(Map<String, AnalisiValueSelected> filtri) {
		this.filtri = filtri;
	}

	/**
	 *
	 * @param flatLayout
	 *            true se voglio un layout flaty
	 */
	public void setFlatLayout(boolean flatLayout) {
		this.flatLayout = flatLayout;
	}

	/**
	 * @param inputControls
	 *            The inputControls to set.
	 */
	public void setInputControls(List<String> inputControls) {
		this.inputControls = inputControls;
	}

	/**
	 * @param nome
	 *            nome dell'analisi
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param nomeFileMappa
	 *            the nomeFileMappa to set
	 */
	public void setNomeFileMappa(String nomeFileMappa) {
		this.nomeFileMappa = nomeFileMappa;
	}

	/**
	 * @param pathTemplateJRXML
	 *            The pathTemplateJRXML to set.
	 */
	public void setPathTemplateJRXML(String pathTemplateJRXML) {
		// this.pathTemplateJRXML = pathTemplateJRXML;
	}

	/**
	 * @param raggruppaGrafico
	 *            The raggruppaGrafico to set.
	 */
	public void setRaggruppaGrafico(boolean raggruppaGrafico) {
		this.raggruppaGrafico = raggruppaGrafico;
	}

	/**
	 * @param showFieldList
	 *            the showFieldList to set
	 */
	public void setShowFieldList(boolean showFieldList) {
		this.showFieldList = showFieldList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AnalisiParametri [categoria=");
		builder.append(categoria);
		builder.append(", nome=");
		builder.append(nome);
		builder.append("]");
		return builder.toString();
	}
}
