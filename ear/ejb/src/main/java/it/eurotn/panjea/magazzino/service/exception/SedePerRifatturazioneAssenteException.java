package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import java.util.List;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class SedePerRifatturazioneAssenteException extends Exception {

	private static final long serialVersionUID = 7927803995157758305L;

	private final SedeMagazzinoLite sedePerRifatturazione;

	private final List<AreaMagazzino> areeMagazzinoSenzaSedeRifatturazione;

	private final List<AreaMagazzino> areeMagazzinoConDiversaSedeRifatturazione;

	/**
	 * Costruttore.
	 * 
	 * @param sedePerRifatturazione
	 *            sede per rifatturazione
	 * @param areeMagazzinoSenzaSedeRifatturazione
	 *            aree magazzino che non hanno una sede per rifatturazione
	 * @param areeMagazzinoConDiversaSedeRifatturazione
	 *            aree magazzino che hanno una sede per rifatturazione diversa da quella di riferimento
	 */
	public SedePerRifatturazioneAssenteException(final SedeMagazzinoLite sedePerRifatturazione,
			final List<AreaMagazzino> areeMagazzinoSenzaSedeRifatturazione,
			final List<AreaMagazzino> areeMagazzinoConDiversaSedeRifatturazione) {
		super();
		this.sedePerRifatturazione = sedePerRifatturazione;
		this.areeMagazzinoSenzaSedeRifatturazione = areeMagazzinoSenzaSedeRifatturazione;
		this.areeMagazzinoConDiversaSedeRifatturazione = areeMagazzinoConDiversaSedeRifatturazione;
	}

	/**
	 * @return the areeMagazzinoConDiversaSedeRifatturazione
	 */
	public List<AreaMagazzino> getAreeMagazzinoConDiversaSedeRifatturazione() {
		return areeMagazzinoConDiversaSedeRifatturazione;
	}

	/**
	 * @return the areeMagazzinoSenzaSedeRifatturazione
	 */
	public List<AreaMagazzino> getAreeMagazzinoSenzaSedeRifatturazione() {
		return areeMagazzinoSenzaSedeRifatturazione;
	}

	/**
	 * @return the sedePerRifatturazione
	 */
	public SedeMagazzinoLite getSedePerRifatturazione() {
		return sedePerRifatturazione;
	}

}
