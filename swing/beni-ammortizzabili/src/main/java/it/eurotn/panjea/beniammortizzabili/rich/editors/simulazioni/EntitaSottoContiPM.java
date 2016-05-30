package it.eurotn.panjea.beniammortizzabili.rich.editors.simulazioni;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.beniammortizzabili2.domain.SottocontiBeni;

public class EntitaSottoContiPM {

	private EntityBase entity;

	private SottocontiBeni sottocontiBeni;

	/**
	 * Costruttore.
	 */
	public EntitaSottoContiPM() {
		super();
		this.sottocontiBeni = new SottocontiBeni();
	}

	/**
	 * Costruttore.
	 *
	 * @param entity
	 *            entita
	 * @param sottocontiBeni
	 *            sotto conti
	 */
	public EntitaSottoContiPM(final EntityBase entity, final SottocontiBeni sottocontiBeni) {
		super();
		this.entity = entity;
		this.sottocontiBeni = sottocontiBeni;
	}

	/**
	 * @return the entity
	 */
	public EntityBase getEntity() {
		return entity;
	}

	/**
	 * @return the entity
	 */
	public Class<?> getEntityClass() {
		return entity.getClass();
	}

	/**
	 * @return the sottocontiBeni
	 */
	public SottocontiBeni getSottocontiBeni() {
		return sottocontiBeni;
	}
}