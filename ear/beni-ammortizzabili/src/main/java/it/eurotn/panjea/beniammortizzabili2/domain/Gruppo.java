/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * @author Leonardo
 * 
 */
@Entity
@Audited
@Table(name = "bamm_gruppo")
@NamedQueries({
		@NamedQuery(name = "Gruppo.caricaAll", query = "from Gruppo"),
		@NamedQuery(name = "Gruppo.caricaByAzienda", query = " select gr from Gruppo gr left join fetch gr.specie sp left join fetch sp.sottoSpecie so inner join gr.aziende a where a.codice = :paramCodiceAzienda "),
		@NamedQuery(name = "Gruppo.caricaByCodice", query = " select gr from Gruppo gr inner join gr.aziende a where a.codice = :paramCodiceAzienda and gr.codice = :paramCodice") })
public class Gruppo extends EntityBase {

	private static final long serialVersionUID = 2280311072032178097L;

	public static final String REF = "Gruppo";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_DATE_UPDATE = "dateUpdate";

	@Column(length = 2, nullable = false)
	@Index(name = "index_codice")
	private String codice = null;

	private String descrizione = null;

	@OneToMany(mappedBy = "gruppo", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private Set<Specie> specie = null;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private Set<AziendaLite> aziende = null;

	/**
	 * Costruttore di default.
	 */
	public Gruppo() {
		initialize();
	}

	/**
	 * Aggiunge l'associazione del gruppo ad una azienda.
	 * 
	 * @param aziendaLite
	 *            Azienda da associare
	 */
	public void addToAzienda(AziendaLite aziendaLite) {
		if (aziende == null) {
			aziende = new TreeSet<AziendaLite>();
		}

		aziende.add(aziendaLite);
	}

	/**
	 * Aggiunge una specie al gruppo.
	 * 
	 * @param paramSpecie
	 *            {@link Specie} da aggiungere
	 */
	public void addTospecie(Specie paramSpecie) {
		if (null == getSpecie()) {
			setSpecie(new java.util.TreeSet<Specie>());
		}
		getSpecie().add(paramSpecie);
	}

	/**
	 * @return the aziende
	 */
	public Set<AziendaLite> getAziende() {
		return aziende;
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
	 * @return the specie
	 */
	public Set<Specie> getSpecie() {
		return specie;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {

	}

	/**
	 * @param aziende
	 *            the aziende to set
	 */
	public void setAziende(Set<AziendaLite> aziende) {
		this.aziende = aziende;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param specie
	 *            the specie to set
	 */
	public void setSpecie(Set<Specie> specie) {
		this.specie = specie;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Gruppo[");
		buffer.append("aziende = ").append(aziende);
		buffer.append(" codice = ").append(codice);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" specie(n) = ").append(specie != null ? specie.size() : null);
		buffer.append("]");
		return buffer.toString();
	}

}
