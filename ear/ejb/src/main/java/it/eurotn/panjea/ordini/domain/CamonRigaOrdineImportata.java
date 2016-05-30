package it.eurotn.panjea.ordini.domain;

/**
 * Mappa le righe degli ordini specifici per camon che ha deciso che il codice articolo è distribuito sui campi
 * codiceArticolo, attributo1 e attributo2.<br/>
 * 
 * @author leonardo
 * @version 1.0, 24/feb/2014
 */
public class CamonRigaOrdineImportata extends RigaOrdineImportata {

	private static final long serialVersionUID = 2455237120993357972L;

	/**
	 * @return non ritorno nulla, mi serve solo per BeanIO, mi interessa solo il set
	 */
	public String getAttributo2() {
		return "";
	}

	@Override
	public void setAttributo1(String attributo1) {
		String codiceArticolo = getCodiceArticolo().trim().concat(attributo1.trim());
		setCodiceArticolo(codiceArticolo);
	}

	/**
	 * @param attributo2
	 *            parte del codice Articolo per camon
	 */
	public void setAttributo2(String attributo2) {
		String codiceArticolo = getCodiceArticolo().trim().concat(attributo2.trim());
		setCodiceArticolo(codiceArticolo);
	}

}
