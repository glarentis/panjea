package it.eurotn.panjea.bi.domain.dashboard;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard.TIPO_VISUALIZZAZIONE;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.PostLoad;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "bi_dashboard_analisi")
public class DashBoardAnalisi extends EntityBase {
	private static final long serialVersionUID = 6647636283673078143L;
	@Column(length = 60)
	private String nomeAnalisi;
	@Column(length = 60)
	private String categoriaAnalisi;

	private boolean visualizzaDesrizioneFiltriApplicati;

	@CollectionOfElements(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "bi_associazione_filtri_fields")
	@NotAudited
	private Set<String> associazioneFiltriFields;

	@CollectionOfElements(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "bi_associazione_filtri_apply_fields")
	@NotAudited
	private Set<String> associazioneFiltriApplyFields;

	private TIPO_VISUALIZZAZIONE tipoVisualizzazione;

	private String frameKey;

	private int x;

	private int y;

	private int w;
	private int h;
	{
		tipoVisualizzazione = TIPO_VISUALIZZAZIONE.TABELLA;
		visualizzaDesrizioneFiltriApplicati = true;
		associazioneFiltriApplyFields = new HashSet<String>();
	}

	/**
	 * Costruttore.
	 */
	public DashBoardAnalisi() {
	}

	/**
	 * Costruttore.
	 *
	 * @param categoriaAnalisi
	 *            categoria dell'analisi
	 * @param nomeAnalisi
	 *            nome dell'analisi
	 */
	protected DashBoardAnalisi(final String categoriaAnalisi, final String nomeAnalisi) {
		this.nomeAnalisi = nomeAnalisi;
		this.categoriaAnalisi = categoriaAnalisi;
		initKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DashBoardAnalisi other = (DashBoardAnalisi) obj;
		if (getFrameKey() == null) {
			if (other.getFrameKey() != null) {
				return false;
			}
		} else if (!getFrameKey().equals(other.getFrameKey())) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the associazioneFiltriApplyFields.
	 */
	public Set<String> getAssociazioneFiltriApplyFields() {
		return associazioneFiltriApplyFields;
	}

	/**
	 * @return the associazioneFiltriFields
	 */
	public Set<String> getAssociazioneFiltriFields() {
		return associazioneFiltriFields;
	}

	/**
	 * @return Returns the categoriaAnalisi.
	 */
	public String getCategoriaAnalisi() {
		if (categoriaAnalisi == null) {
			return "";
		}
		return categoriaAnalisi;
	}

	/**
	 * @return Returns the frameKey.
	 */
	public String getFrameKey() {
		return frameKey;
	}

	/**
	 * @return Returns the h.
	 */
	public int getH() {
		return h;
	}

	/**
	 * @return Returns the nomeAnalisi.
	 */
	public String getNomeAnalisi() {
		return nomeAnalisi;
	}

	/**
	 * @return the tipoVisualizzazione
	 */
	public TIPO_VISUALIZZAZIONE getTipoVisualizzazione() {
		return tipoVisualizzazione;
	}

	/**
	 * @return Returns the w.
	 */
	public int getW() {
		return w;
	}

	/**
	 * @return Returns the x.
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return Returns the y.
	 */
	public int getY() {
		return y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getFrameKey() == null) ? 0 : getFrameKey().hashCode());
		return result;
	}

	@PostLoad
	private void initKey() {
		if (frameKey == null) {
			frameKey = categoriaAnalisi + "_" + nomeAnalisi + "_" + UUID.randomUUID();
		}
	}

	/**
	 * @return the visualizzaDesrizioneFiltriApplicati
	 */
	public boolean isVisualizzaDesrizioneFiltriApplicati() {
		return visualizzaDesrizioneFiltriApplicati;
	}

	/**
	 * @param associazioneFiltriApplyFields
	 *            The associazioneFiltriApplyFields to set.
	 */
	public void setAssociazioneFiltriApplyFields(Set<String> associazioneFiltriApplyFields) {
		this.associazioneFiltriApplyFields = associazioneFiltriApplyFields;
	}

	/**
	 * @param associazioneFiltriFields
	 *            the associazioneFiltriFields to set
	 */
	public void setAssociazioneFiltriFields(Set<String> associazioneFiltriFields) {
		this.associazioneFiltriFields = associazioneFiltriFields;
	}

	/**
	 * @param categoriaAnalisi
	 *            The categoriaAnalisi to set.
	 */
	public void setCategoriaAnalisi(String categoriaAnalisi) {
		this.categoriaAnalisi = categoriaAnalisi;
	}

	/**
	 * @param paramh
	 *            The h to set.
	 */
	public void setH(int paramh) {
		this.h = paramh;
	}

	/**
	 * @param nomeAnalisi
	 *            The nomeAnalisi to set.
	 */
	public void setNomeAnalisi(String nomeAnalisi) {
		this.nomeAnalisi = nomeAnalisi;
	}

	/**
	 * @param tipoVisualizzazione
	 *            the tipoVisualizzazione to set
	 */
	public void setTipoVisualizzazione(TIPO_VISUALIZZAZIONE tipoVisualizzazione) {
		this.tipoVisualizzazione = tipoVisualizzazione;
	}

	/**
	 * @param visualizzaDesrizioneFiltriApplicati
	 *            the visualizzaDesrizioneFiltriApplicati to set
	 */
	public void setVisualizzaDesrizioneFiltriApplicati(boolean visualizzaDesrizioneFiltriApplicati) {
		this.visualizzaDesrizioneFiltriApplicati = visualizzaDesrizioneFiltriApplicati;
	}

	/**
	 * @param paramw
	 *            The w to set.
	 */
	public void setW(int paramw) {
		this.w = paramw;
	}

	/**
	 * @param paramx
	 *            The x to set.
	 */
	public void setX(int paramx) {
		this.x = paramx;
	}

	/**
	 * @param paramy
	 *            The y to set.
	 */
	public void setY(int paramy) {
		this.y = paramy;
	}

}