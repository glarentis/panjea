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
public abstract class SuddivisioneAmministrativa extends EntityBase {

	public enum NumeroLivelloAmministrativo {
		LVL1, LVL2, LVL3, LVL4
	}

	private static final long serialVersionUID = 7906992970849830008L;

	@Column(name = "nome", length = 120, nullable = false)
	@Index(name = "idx_name_lvl_amm")
	private String nome;

	@ManyToOne(optional = true)
	@JoinColumn(name = "nazione_id")
	private Nazione nazione;

	/**
	 * Costruttore.
	 */
	public SuddivisioneAmministrativa() {
		super();
	}

	/**
	 * @return the nazione
	 */
	public Nazione getNazione() {
		return nazione;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return il LivelloAmministrativo della classe concreta.
	 */
	public abstract NumeroLivelloAmministrativo getNumeroLivelloAmministrativo();

	/**
	 * @return the suddivisioneAmministrativa
	 */
	public abstract SuddivisioneAmministrativa getSuddivisioneAmministrativaPrecedente();

	/**
	 * @param nazione
	 *            the nazione to set
	 */
	public void setNazione(Nazione nazione) {
		this.nazione = nazione;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

}
