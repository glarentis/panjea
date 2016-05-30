package it.eurotn.panjea.anagrafica.domain.datigeografici;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Index;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class IdentificativoLocalita extends EntityBase {

	private static final long serialVersionUID = 6103371285798747262L;

	@Column(name = "descrizione", length = 60)
	@Index(name = "descrizione")
	private String descrizione;

	@ManyToOne(optional = true)
	@JoinColumn(name = "nazione_id")
	private Nazione nazione;

	@ManyToOne(optional = true)
	@JoinColumn(name = "livello1_id")
	private LivelloAmministrativo1 livelloAmministrativo1;

	@ManyToOne(optional = true)
	@JoinColumn(name = "livello2_id")
	private LivelloAmministrativo2 livelloAmministrativo2;

	@ManyToOne(optional = true)
	@JoinColumn(name = "livello3_id")
	private LivelloAmministrativo3 livelloAmministrativo3;

	@ManyToOne(optional = true)
	@JoinColumn(name = "livello4_id")
	private LivelloAmministrativo4 livelloAmministrativo4;

	/**
	 * @return the descrizione
	 */
	public java.lang.String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the livelloAmministrativo1
	 */
	public LivelloAmministrativo1 getLivelloAmministrativo1() {
		return livelloAmministrativo1;
	}

	/**
	 * @return the livelloAmministrativo2
	 */
	public LivelloAmministrativo2 getLivelloAmministrativo2() {
		return livelloAmministrativo2;
	}

	/**
	 * @return the livelloAmministrativo3
	 */
	public LivelloAmministrativo3 getLivelloAmministrativo3() {
		return livelloAmministrativo3;
	}

	/**
	 * @return the livelloAmministrativo4
	 */
	public LivelloAmministrativo4 getLivelloAmministrativo4() {
		return livelloAmministrativo4;
	}

	/**
	 * 
	 * @return ultimo livello livelloAmministrativo configurato per la nazione e collegato all'identificativo località
	 */
	public SuddivisioneAmministrativa getLivelloAmministrativoPrecedente() {
		if (nazione.hasLivelloAmministrativo4()) {
			return livelloAmministrativo4;
		} else if (nazione.hasLivelloAmministrativo3()) {
			return livelloAmministrativo3;
		} else if (nazione.hasLivelloAmministrativo2()) {
			return livelloAmministrativo2;
		} else {
			return livelloAmministrativo1;
		}
	}

	/**
	 * @return the nazione
	 */
	public Nazione getNazione() {
		return nazione;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(java.lang.String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param livelloAmministrativo1
	 *            the livelloAmministrativo1 to set
	 */
	public void setLivelloAmministrativo1(LivelloAmministrativo1 livelloAmministrativo1) {
		this.livelloAmministrativo1 = livelloAmministrativo1;
	}

	/**
	 * @param livelloAmministrativo2
	 *            the livelloAmministrativo2 to set
	 */
	public void setLivelloAmministrativo2(LivelloAmministrativo2 livelloAmministrativo2) {
		this.livelloAmministrativo2 = livelloAmministrativo2;
	}

	/**
	 * @param livelloAmministrativo3
	 *            the livelloAmministrativo3 to set
	 */
	public void setLivelloAmministrativo3(LivelloAmministrativo3 livelloAmministrativo3) {
		this.livelloAmministrativo3 = livelloAmministrativo3;
	}

	/**
	 * @param livelloAmministrativo4
	 *            the livelloAmministrativo4 to set
	 */
	public void setLivelloAmministrativo4(LivelloAmministrativo4 livelloAmministrativo4) {
		this.livelloAmministrativo4 = livelloAmministrativo4;
	}

	/**
	 * @param nazione
	 *            the nazione to set
	 */
	public void setNazione(Nazione nazione) {
		this.nazione = nazione;
	}

}
