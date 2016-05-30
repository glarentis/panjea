package it.eurotn.panjea.tesoreria.solleciti;

import it.eurotn.panjea.rate.domain.Rata;

/**
 * Identifica un elemento da poter sostituire all'interno del template dei solleciti.
 * 
 * @author angelo
 * 
 */
public class TemplatePlaceHolder {

	private String classe;
	private String descrizione;
	private String propertyPath;
	private String codice;
	private String subCodice;

	/**
	 * default constructor.
	 */
	public TemplatePlaceHolder() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param classe
	 *            identifica un "raggruppamento" delle proprietà
	 * @param descrizione
	 *            descrizione del placeHolder (ad esempio "codice cliente")
	 * @param propertyPath
	 *            property path del valore da estrarre dall'oggetto {@link Rata}
	 */
	public TemplatePlaceHolder(final String classe, final String descrizione, final String propertyPath) {
		super();
		this.classe = classe;
		this.descrizione = descrizione;
		this.propertyPath = propertyPath;
		this.codice = "$" + classe + "." + descrizione + "$";
		this.subCodice = "#" + descrizione;
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
		TemplatePlaceHolder other = (TemplatePlaceHolder) obj;
		if (classe == null) {
			if (other.classe != null) {
				return false;
			}
		} else if (!classe.equals(other.classe)) {
			return false;
		}
		if (descrizione == null) {
			if (other.descrizione != null) {
				return false;
			}
		} else if (!descrizione.equals(other.descrizione)) {
			return false;
		}
		if (propertyPath == null) {
			if (other.propertyPath != null) {
				return false;
			}
		} else if (!propertyPath.equals(other.propertyPath)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the classe
	 */
	public String getClasse() {
		return classe;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the propertyPath
	 */
	public String getPropertyPath() {
		return propertyPath;
	}

	/**
	 * @return the subCodice
	 */
	public String getSubCodice() {
		return subCodice;
	}

	/**
	 * 
	 * @param rata
	 *            rata dalla quale si vuole estrarre il valore
	 * @return valore del campo nel propertypath partendo dall'oggetto rata
	 */
	public String getValue(Rata rata) {
		return "Implements";
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
		result = prime * result + ((classe == null) ? 0 : classe.hashCode());
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + ((propertyPath == null) ? 0 : propertyPath.hashCode());
		return result;
	}

}
