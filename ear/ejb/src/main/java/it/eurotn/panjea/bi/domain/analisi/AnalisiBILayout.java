package it.eurotn.panjea.bi.domain.analisi;

import it.eurotn.entity.EntityBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.NotAudited;

/**
 * 
 * Definisce il layout dell'analisi dal quale viene derivata la query.
 * 
 * @author giangi
 * @version 1.0, 21/mag/2014
 * 
 */
@Entity
@Table(name = "bi_analisi_layout")
public class AnalisiBILayout extends EntityBase {
	private static final long serialVersionUID = 7589653759198452049L;

	@Transient
	private List<FieldBILayout> filtri;

	@Transient
	private List<FieldBILayout> righe;

	@Transient
	private List<FieldBILayout> colonne;

	@Transient
	private List<FieldBILayout> misure;

	private boolean totalForRow;
	private boolean totalForColumn;

	@NotAudited
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@MapKey(name = "keyModel")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "analisiBILayout_id")
	private Map<String, FieldBILayout> fields;

	/**
	 * Costruttore.
	 */
	public AnalisiBILayout() {
		init();
	}

	/**
	 * Aggiunge un field al layout.
	 * 
	 * @param field
	 *            field da aggiungere
	 */
	public void addField(FieldBILayout field) {
		if (fields == null) {
			fields = new HashMap<String, FieldBILayout>();
		}
		colonne = null;
		righe = null;
		misure = null;
		filtri = null;
		fields.put(field.getName(), field);
	}

	/**
	 * @return Returns the colonne.
	 */
	public List<FieldBILayout> getColonne() {
		if (colonne == null) {
			colonne = java.util.Collections.unmodifiableList(getFieldByAreaType(1));
		}

		return colonne;
	}

	private List<FieldBILayout> getFieldByAreaType(int areaType) {
		List<FieldBILayout> results = new ArrayList<FieldBILayout>();
		if (fields != null) {
			for (FieldBILayout field : fields.values()) {
				if (field.getAreaType() == areaType) {
					results.add(field);
				}
			}
		}
		Collections.sort(results, new Comparator<FieldBILayout>() {

			@Override
			public int compare(FieldBILayout o1, FieldBILayout o2) {
				if (o1.getAreaIndex() < o2.getAreaIndex()) {
					return -1;
				} else if (o1.getAreaIndex() > o2.getAreaIndex()) {
					return 1;
				}
				return 0;
			}
		});
		return results;
	}

	/**
	 * 
	 * @return tutti i fields dell'analisi
	 */
	public Map<String, FieldBILayout> getFields() {
		return java.util.Collections.unmodifiableMap(fields);
	}

	/**
	 * @return Returns the filtri.
	 */
	public List<FieldBILayout> getFiltri() {
		if (filtri == null) {
			filtri = java.util.Collections.unmodifiableList(getFieldByAreaType(2));
		}

		return filtri;
	}

	/**
	 * @return Returns the misure.
	 */
	public List<FieldBILayout> getMisure() {
		if (misure == null) {
			misure = java.util.Collections.unmodifiableList(getFieldByAreaType(3));
		}

		return misure;
	}

	/**
	 * @return Returns the righe.
	 */
	public List<FieldBILayout> getRighe() {
		if (righe == null) {
			righe = java.util.Collections.unmodifiableList(getFieldByAreaType(0));
		}
		return righe;
	}

	private void init() {
	}

	/**
	 * @return Returns the totalForColumn.
	 */
	public boolean isTotalForColumn() {
		return totalForColumn;
	}

	/**
	 * @return Returns the totalForRow.
	 */
	public boolean isTotalForRow() {
		return totalForRow;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(Map<String, FieldBILayout> fields) {
		this.fields = fields;
	}

	/**
	 * @param totalForColumn
	 *            the totalForColumn to set
	 */
	public void setTotalForColumn(boolean totalForColumn) {
		this.totalForColumn = totalForColumn;
	}

	/**
	 * @param totalForRow
	 *            the totalForRow to set
	 */
	public void setTotalForRow(boolean totalForRow) {
		this.totalForRow = totalForRow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnalisiBILayout [totalForRow=" + totalForRow + ", totalForColumn=" + totalForColumn + ", fields="
				+ fields + "]";
	}

}
