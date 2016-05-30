package it.eurotn.panjea.contabilita.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contiene la situazione EP dell'azienda.
 * 
 * @author giangi
 */
public class SituazioneEpDTO implements Serializable {

	private static final long serialVersionUID = -1555421861218136881L;
	/**
	 * @uml.property name="patrimoniale"
	 */
	private List<SaldoConto> patrimoniale;
	/**
	 * @uml.property name="economica"
	 */
	private List<SaldoConto> economica;
	/**
	 * @uml.property name="ordine"
	 */
	private List<SaldoConto> ordine;

	/**
	 * Costruttore.
	 * 
	 */
	public SituazioneEpDTO() {
		patrimoniale = new ArrayList<SaldoConto>();
		economica = new ArrayList<SaldoConto>();
		ordine = new ArrayList<SaldoConto>();

	}

	/**
	 * aggiunge {@link SaldoConto} a {@link List} economico.
	 * 
	 * @param conto
	 *            saldo da aggiungere
	 */
	public void addContoEconomico(SaldoConto conto) {
		economica.add(conto);

	}

	/**
	 * aggiunge {@link SaldoConto} a {@link List} ordine.
	 * 
	 * @param conto
	 *            saldo da aggiungere
	 */
	public void addContoOrdine(SaldoConto conto) {
		ordine.add(conto);
	}

	/**
	 * aggiunge {@link SaldoConto} a {@link List} patrimoniale.
	 * 
	 * @param conto
	 *            saldo da aggiungere
	 */
	public void addContoPatrimoniale(SaldoConto conto) {
		patrimoniale.add(conto);
	}

	/**
	 * @return economica
	 * @uml.property name="economica"
	 */
	public List<SaldoConto> getEconomica() {
		return economica;
	}

	/**
	 * @return ordine
	 * @uml.property name="ordine"
	 */
	public List<SaldoConto> getOrdine() {
		return ordine;
	}

	/**
	 * @return patrimoniale
	 * @uml.property name="patrimoniale"
	 */
	public List<SaldoConto> getPatrimoniale() {
		return patrimoniale;
	}
}
