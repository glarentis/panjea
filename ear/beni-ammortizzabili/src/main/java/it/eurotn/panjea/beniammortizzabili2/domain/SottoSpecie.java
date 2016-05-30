/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.contabilita.domain.SottoConto;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * @author Leonardo
 *
 */
@Entity
@Audited
@Table(name = "bamm_sottospecie")
@NamedQueries({
		@NamedQuery(name = "SottoSpecie.caricaAll", query = "from SottoSpecie"),
		@NamedQuery(name = "SottoSpecie.caricaByAzienda", query = " select sp from SottoSpecie sp inner join sp.specie s inner join s.gruppo g inner join g.aziende a where a.codice = :paramCodiceAzienda ") })
public class SottoSpecie extends EntityBase {

	private static final long serialVersionUID = -9013353171811551959L;

	public static final String REF = "SottoSpecie";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_SPECIE = "specie";
	public static final String PROP_PERCENTUALE_AMMORTAMENTO = "percentualeAmmortamento";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_DATE_UPDATE = "dateUpdate";

	@Column(length = 2, nullable = false)
	@Index(name = "index_codice")
	private String codice = null;

	private String descrizione = null;

	private double percentualeAmmortamento;

	@ManyToOne
	private Specie specie;

	@Embedded
	private SottocontiBeni sottocontiBeni;

	/**
	 * Costruttore di default.
	 */
	public SottoSpecie() {
		initialize();
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
	 * @return the percentualeAmmortamento
	 */
	public double getPercentualeAmmortamento() {
		return percentualeAmmortamento;
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
	 * @return codice della sottospecie
	 */
	public String getSottoSpecieCodice() {
		if ("".equals(this.codice)) {
			return codice;
		}
		StringBuffer sottoSpecieCodiceBuffer = new StringBuffer();
		sottoSpecieCodiceBuffer.append(specie.getCodice()).append(".").append(this.codice);
		return sottoSpecieCodiceBuffer.toString();
	}

	/**
	 * @return the specie
	 */
	public Specie getSpecie() {
		return specie;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		percentualeAmmortamento = 0;
		codice = "";
		specie = new Specie();
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
	 * @param percentualeAmmortamento
	 *            the percentualeAmmortamento to set
	 */
	public void setPercentualeAmmortamento(double percentualeAmmortamento) {
		this.percentualeAmmortamento = percentualeAmmortamento;
	}

	/**
	 * @param sottocontiBeni
	 *            the sottocontiBeni to set
	 */
	public void setSottocontiBeni(SottocontiBeni sottocontiBeni) {
		this.sottocontiBeni = sottocontiBeni;
	}

	/**
	 * @param specie
	 *            the specie to set
	 */
	public void setSpecie(Specie specie) {
		this.specie = specie;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SottoSpecie[");
		buffer.append("codice = ").append(codice);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" percentualeAmmortamento = ").append(percentualeAmmortamento);
		buffer.append(" specie = ").append(specie != null ? specie.getId() : null);
		buffer.append("]");
		return buffer.toString();
	}

}
