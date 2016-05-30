package it.eurotn.panjea.bi.domain.analisi.tabelle;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBILayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class Tabella implements Serializable {
	public static final String QUOTE_CHARACTER = "'";
	private static final long serialVersionUID = -363276331630764024L;

	private String nome;

	private String prefisso;

	protected Map<String, Colonna> colonne;

	private String where;// where da aggiungere alla tabella
	private String title;// title della tabella
	private String contentTableAlias;// indica in quale pannello della gui visualizzare le colonne. Se null viene creato

	// un suo pannello

	/**
	 *
	 * Costruttore.
	 *
	 * @param nome
	 *            nome della tabella
	 * @param prefisso
	 *            prefisso della tabella
	 * @param where
	 *            eventuale where se la tabella è un filtro su una tabella (null se non usato)
	 * @param title
	 *            alias della tabella (null se non usato).
	 */
	public Tabella(final String nome, final String prefisso,
			final String where, final String title) {
		super();
		this.nome = nome;
		this.prefisso = prefisso;
		this.where = ObjectUtils.defaultIfNull(where, "");
		this.title = ObjectUtils.defaultIfNull(title, "");
		this.colonne = new HashMap<String, Colonna>();
		this.contentTableAlias = null;
	}

	/**
	 *
	 * @param nomeColonna
	 *            nome della colonna da aggiungere
	 * @param columnClass
	 *            classe della colonna da visualizzare
	 * @param aliasColumn
	 *            colonna di alias.
	 * @return colonna creata
	 */
	public Colonna addColumn(String nomeColonna, Class<?> columnClass,
			String aliasColumn) {
		Colonna colonna = new Colonna(nomeColonna, columnClass, this,
				aliasColumn);
		colonne.put(colonna.getKey(), colonna);
		return colonna;
	}

	/**
	 *
	 * @param nomeColonna
	 *            nome della colonna da aggiungere
	 * @param columnClass
	 *            classe della colonna da visualizzare
	 * @param isSeparatorVisible
	 *            separatore Visibile
	 * @param aliasColumn
	 *            colonna di alias.
	 * @return colonna creata
	 */
	public Colonna addColumn(String nomeColonna, Class<?> columnClass,
			String aliasColumn, boolean isSeparatorVisible) {
		Colonna colonna = new Colonna(nomeColonna, columnClass, this,
				aliasColumn, isSeparatorVisible);
		colonne.put(colonna.getKey(), colonna);
		return colonna;
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Tabella other = (Tabella) obj;
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the alias.
	 */
	public String getAlias() {
		return StringUtils.deleteWhitespace(title);
	}

	/**
	 * @return Returns the colonne.
	 */
	public Map<String, Colonna> getColonne() {
		return colonne;
	}

	/**
	 * @return Returns the contentTableAlias.
	 */
	public String getContentTableAlias() {
		return contentTableAlias;
	}

	/**
	 *
	 * @return nome completo della tabella.
	 */
	public String getFullName() {
		return new StringBuilder(prefisso).append("_").append(nome).toString();
	}

	/**
	 * @param alternativeJoin
	 *            indica se restituire quella di default o quella alternativa
	 * @return the join
	 */
	public String getJoin(boolean alternativeJoin) {
		if (alternativeJoin) {
			return " left join ";
		}
		return " inner join ";
	}

	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return Returns the prefisso.
	 */
	public String getPrefisso() {
		return prefisso;
	}

	/**
	 *
	 * @param analisiBILayout
	 *            layout analisi
	 * @return tabella sql.
	 */
	public String getSqlTable(AnalisiBILayout analisiBILayout) {
		return getFullName() + " " + getAlias();
	}

	/**
	 *
	 * @return title della tabella
	 */
	public String getTitle() {
		return StringUtils.capitalize(title);
	}

	/**
	 * @return Returns the where.
	 */
	public String getWhere() {
		return where;
	}

	/**
	 *
	 * @return where sql da eseguire sulla tabella base.
	 */
	public String getWhereSql() {
		return getAlias() + "." + getWhere();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	/**
	 * @param contentTableAlias
	 *            The contentTableAlias to set.
	 */
	public void setContentTableAlias(String contentTableAlias) {
		this.contentTableAlias = contentTableAlias;
	}

	/**
	 * @param nome
	 *            The nome to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param prefisso
	 *            The prefisso to set.
	 */
	public void setPrefisso(String prefisso) {
		this.prefisso = prefisso;
	}

	/**
	 * @param where
	 *            The where to set.
	 */
	public void setWhere(String where) {
		this.where = where;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tabella [nome=" + nome + "]";
	}
}
