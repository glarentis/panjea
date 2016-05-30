package it.eurotn.panjea.aton.domain;

import it.eurotn.panjea.ordini.domain.NotaOrdineImportata;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class OrdiniImportati {
	private List<OrdineImportato> ordini;

	private static Logger logger = Logger.getLogger(OrdiniImportati.class);

	/**
	 * Costruttore.
	 */
	public OrdiniImportati() {
		ordini = new ArrayList<OrdineImportato>();
	}

	/**
	 * Aggiunge una riga nota agli ordini importati.
	 * 
	 * @param notaOrdine
	 *            nota da aggiungere
	 */
	public void aggiungiNota(NotaOrdineImportata notaOrdine) {
		OrdineImportato ordine = notaOrdine.getOrdine();
		int indexOrdine = ordini.indexOf(ordine);
		if (indexOrdine == -1) {
			ordini.add(ordine);
			indexOrdine = (ordini.size() - 1);
			if (logger.isDebugEnabled()) {
				logger.debug("--> Non trovo l'ordine per le note, salto");
			}
		}
		ordine = ordini.get(indexOrdine);
		ordine.addNotaOrdine(notaOrdine);

	}

	/**
	 * Aggiunge una riga agli ordini.
	 * 
	 * @param riga
	 *            riga importata
	 */
	public void aggiungiRiga(RigaOrdineImportata riga) {
		OrdineImportato ordine = riga.getOrdine();
		int indexOrdine = ordini.indexOf(ordine);
		if (indexOrdine == -1) {
			ordini.add(ordine);
			indexOrdine = (ordini.size() - 1);
		}
		ordine = ordini.get(indexOrdine);
		// giro lo sconto perchè per noi è una variazione quindi va negativa
		if (riga.getSconto1() != null) {
			riga.setSconto1(riga.getSconto1().negate());
		}
		if (riga.getSconto2() != null) {
			riga.setSconto2(riga.getSconto2().negate());
		}
		if (riga.getSconto3() != null) {
			riga.setSconto3(riga.getSconto3().negate());
		}
		if (riga.getSconto4() != null) {
			riga.setSconto4(riga.getSconto4().negate());
		}
		ordine.addRiga(riga);
	}

	/**
	 * aggiunge una testata ordine.
	 * 
	 * @param ordineImportato
	 *            testata ordine importata
	 */
	public void aggiungiTestata(OrdineImportato ordineImportato) {
		ordineImportato.setProvenienza(EProvenienza.ATON);
		ordini.add(ordineImportato);
	}

	/**
	 * @return Returns the ordini.
	 */
	public List<OrdineImportato> getOrdini() {
		return ordini;
	}
}
