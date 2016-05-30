package it.eurotn.panjea.bi.domain.dashboard;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.bi.domain.analisi.AnalisiValueSelected;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "bi_dashboard", uniqueConstraints = @UniqueConstraint(columnNames = { "utente", "nome" }))
@NamedQueries({
	// @NamedQuery(name = "DashBoard.caricaByUtente", query =
	// "select d.id,d.nome,d.cateogoria from DashBoard d where d.utente=:utente and privata=0"),
	@NamedQuery(name = "DashBoard.caricaByNomeEUtente", query = "select d from DashBoard d where d.utente=:utente and d.nome=:nome") })
public class DashBoard extends EntityBase {

	public enum LayoutFiltri {
		FILL, ORIZZONTALE, VERTICALE
	}

	public enum TIPO_VISUALIZZAZIONE {
		TABELLA, GRAFICO
	}

	private static final long serialVersionUID = -5575025991223739707L;
	@Column(length = 20)
	private String utente;
	@Column(length = 40, nullable = false)
	private String nome;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@MapKey(name = "frameKey")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "dashboard_id", nullable = false)
	@AuditJoinTable(name = "bi_dashboard_dashboardanalisi_aud")
	@Fetch(FetchMode.JOIN)
	private Map<String, DashBoardAnalisi> analisi;
	@NotAudited
	@Lob
	private byte[] dockingLayout; // Disposizione delle pagine
	private String categoria;
	private String descrizione;
	private Boolean privata;
	private boolean frameFiltriVisible;
	private int width;
	private int height;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@MapKey(name = "nomeCampo")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.JOIN)
	private Map<String, AnalisiValueSelected> filtri;
	private LayoutFiltri layoutFiltri;

	@Transient
	private Set<String> filtriPrivati;

	/**
	 * Costruttore.
	 */
	public DashBoard() {
		analisi = new HashMap<String, DashBoardAnalisi>();
		privata = false;
		filtriPrivati = new TreeSet<String>();
		layoutFiltri = LayoutFiltri.FILL;
		frameFiltriVisible = true;
	}

	/**
	 * Aggiunge un analisi alla dashBoard.
	 *
	 * @param categoriaAnalisi
	 *            cateogira analisi
	 * @param nomeAnalisi
	 *            nome dell'analisi da aggiungere
	 * @return analisi aggiunta, null se l'analisi era già presente
	 */
	public DashBoardAnalisi aggiungiAnalisi(final String categoriaAnalisi, final String nomeAnalisi) {
		DashBoardAnalisi analisiToAdd = new DashBoardAnalisi(categoriaAnalisi, nomeAnalisi);
		if (analisi.containsKey(analisiToAdd.getFrameKey())) {
			return null;
		}
		analisi.put(analisiToAdd.getFrameKey(), analisiToAdd);
		return analisiToAdd;
	}

	/**
	 * @return Returns the analisi.
	 */
	public Map<String, DashBoardAnalisi> getAnalisi() {
		return analisi;
	}

	/**
	 * @return Returns the categoria.
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * @return Returns the descrizione.
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return Returns the dockingLayout.
	 */
	public byte[] getDockingLayout() {
		return dockingLayout;
	}

	/**
	 * @return Returns the filtri.
	 */
	public Map<String, AnalisiValueSelected> getFiltri() {
		if (filtri == null) {
			return new HashMap<String, AnalisiValueSelected>();
		}
		return filtri;
	}

	/**
	 * @return the filtriPrivati
	 */
	public Set<String> getFiltriPrivati() {
		return filtriPrivati;
	}

	/**
	 * @return Returns the height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the layoutFiltri
	 */
	public LayoutFiltri getLayoutFiltri() {
		return layoutFiltri;
	}

	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return Returns the privata.
	 */
	public Boolean getPrivata() {
		if (privata == null) {
			return false;
		}
		return privata;
	}

	/**
	 * @return Returns the utente.
	 */
	public String getUtente() {
		return utente;
	}

	/**
	 * @return Returns the width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return Returns the frameFiltriVisible.
	 */
	public boolean isFrameFiltriVisible() {
		return frameFiltriVisible;
	}

	/**
	 * @param analisiToRemove
	 *            rimuove l'analisi
	 */
	public void removeAnalisi(DashBoardAnalisi analisiToRemove) {
		analisi.remove(analisiToRemove.getFrameKey());
	}

	/**
	 * @param categoria
	 *            The categoria to set.
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param dockingLayout
	 *            The dockingLayout to set.
	 */
	public void setDockingLayout(byte[] dockingLayout) {
		this.dockingLayout = dockingLayout;
	}

	/**
	 * @param filtri
	 *            The filtri to set.
	 */
	public void setFiltri(Map<String, AnalisiValueSelected> filtri) {
		this.filtri = filtri;
	}

	/**
	 * @param filtriPrivati
	 *            the filtriPrivati to set
	 */
	public void setFiltriPrivati(Set<String> filtriPrivati) {
		this.filtriPrivati = filtriPrivati;
	}

	/**
	 * @param frameFiltriVisible
	 *            The frameFiltriVisible to set.
	 */
	public void setFrameFiltriVisible(boolean frameFiltriVisible) {
		this.frameFiltriVisible = frameFiltriVisible;
	}

	/**
	 * @param height
	 *            The height to set.
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @param layoutFiltri
	 *            the layoutFiltri to set
	 */
	public void setLayoutFiltri(LayoutFiltri layoutFiltri) {
		this.layoutFiltri = layoutFiltri;
	}

	/**
	 * @param nome
	 *            The nome to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param privata
	 *            The privata to set.
	 */
	public void setPrivata(Boolean privata) {
		this.privata = privata;
	}

	/**
	 * @param utente
	 *            The utente to set.
	 */
	public void setUtente(String utente) {
		this.utente = utente;
	}

	/**
	 * @param width
	 *            The width to set.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public String toString() {
		return "DashBoard [utente=" + utente + ", nome=" + nome;
	}
}
