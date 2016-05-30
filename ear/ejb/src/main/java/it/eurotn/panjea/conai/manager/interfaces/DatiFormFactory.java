package it.eurotn.panjea.conai.manager.interfaces;

import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;

/**
 * 
 * 
 * @author giangi
 * @version 1.0, 03/mag/2012
 * 
 */
public final class DatiFormFactory {
	/**
	 * 
	 * @param materiale
	 *            materiale per il quale recuparare i dati per il form
	 * @return dati per riempire il form
	 */
	public static DatiForm getDatiForm(ConaiMateriale materiale) {
		DatiForm result;
		switch (materiale) {
		case ACCIAIO:
			result = new AcciaioDatiForm();
			break;
		case ALLUMINIO:
			result = new AlluminioDatiForm();
			break;
		case CARTA:
			result = new CartaDatiForm();
			break;
		case LEGNO:
			result = new LegnoDatiForm();
			break;
		case PLASTICA:
			result = new PlasticaDatiForm();
			break;
		case VETRO:
			result = new VetroDatiForm();
			break;
		default:
			throw new UnsupportedOperationException("Tipo di materiale non previsto " + materiale.name());
		}
		return result;
	}

	/**
	 * Costruttore.
	 * 
	 */
	private DatiFormFactory() {
	}
}
