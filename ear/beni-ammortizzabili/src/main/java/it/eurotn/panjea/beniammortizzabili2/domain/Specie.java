/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.contabilita.domain.SottoConto;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
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
@Table(name = "bamm_specie")
@NamedQueries({ @NamedQuery(name = "Specie.caricaAll", query = " from Specie ") })
public class Specie extends EntityBase {

	private static final long serialVersionUID = -7381229997090696478L;

	public static final String REF = "Specie";
	public static final String PROP_ID = "id";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_GRUPPO = "gruppo";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_DATE_UPDATE = "dateUpdate";

	@Column(length = 2, nullable = false)
	@Index(name = "index_codice")
	private String codice = null;

	private String descrizione = null;

	@ManyToOne
	private Gruppo gruppo;

	@OneToMany(mappedBy = "specie", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private Set<SottoSpecie> sottoSpecie;

	@Embedded
	private SottocontiBeni sottocontiBeni;

	/**
	 * Costruttore di default.
	 */
	public Specie() {
		initialize();
	}

	/**
	 * Aggiunge una sottospecie alla specie.
	 *
	 * @param paramSottoSpecie
	 *            {@link SottoSpecie} da aggiungere
	 */
	public void addToSottoSpecie(SottoSpecie paramSottoSpecie) {
		if (null == getSottoSpecie()) {
			setSottoSpecie(new java.util.TreeSet<SottoSpecie>());
		}
		getSottoSpecie().add(paramSottoSpecie);
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
	 * @return the gruppo
	 */
	public Gruppo getGruppo() {
		return gruppo;
	}

	/**
	 * @return the sottocontiBeni
	 */
	public SottocontiBeni getSottocontiBeni() {
		if (sottocontiBeni == null) {
			sottocontiBeni = new SottocontiBeni();
		}
		return sottocontiBeni;
	}

	/**
	 * @return the sottoContoAmmortamento
	 */
	public SottoConto getSottoContoAmmortamento() {
		return sottocontiBeni == null ? null : sottocontiBeni.getSottoContoAmmortamento();
	}

	/**
	 * @return the sottoContoAmmortamentoAnticipato
	 */
	public SottoConto getSottoContoAmmortamentoAnticipato() {
		return sottocontiBeni == null ? null : sottocontiBeni.getSottoContoAmmortamentoAnticipato();
	}

	/**
	 * @return the sottoContoFondoAmmortamento
	 */
	public SottoConto getSottoContoFondoAmmortamento() {
		return sottocontiBeni == null ? null : sottocontiBeni.getSottoContoFondoAmmortamento();
	}

	/**
	 * @return the sottoContoFondoAmmortamentoAnticipato
	 */
	public SottoConto getSottoContoFondoAmmortamentoAnticipato() {
		return sottocontiBeni == null ? null : sottocontiBeni.getSottoContoFondoAmmortamentoAnticipato();
	}

	/**
	 * @return the sottoSpecie
	 */
	public Set<SottoSpecie> getSottoSpecie() {
		return sottoSpecie;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		codice = "";
		gruppo = new Gruppo();
		sottocontiBeni = new SottocontiBeni();
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
	 * @param gruppo
	 *            the gruppo to set
	 */
	public void setGruppo(Gruppo gruppo) {
		this.gruppo = gruppo;
	}

	/**
	 * @param sottocontiBeni
	 *            the sottocontiBeni to set
	 */
	public void setSottocontiBeni(SottocontiBeni sottocontiBeni) {
		this.sottocontiBeni = sottocontiBeni;
	}

	/**
	 * @param sottoSpecie
	 *            the sottoSpecie to set
	 */
	public void setSottoSpecie(Set<SottoSpecie> sottoSpecie) {
		this.sottoSpecie = sottoSpecie;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Specie[");
		buffer.append("codice = ").append(codice);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" gruppo = ").append(gruppo != null ? gruppo.getId() : null);
		buffer.append(" sottoSpecie(n) = ").append(sottoSpecie != null ? sottoSpecie.size() : null);
		buffer.append("]");
		return buffer.toString();
	}

}
