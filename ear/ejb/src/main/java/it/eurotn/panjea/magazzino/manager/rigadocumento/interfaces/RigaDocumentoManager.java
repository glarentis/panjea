package it.eurotn.panjea.magazzino.manager.rigadocumento.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;

@Local
public interface RigaDocumentoManager {

	/**
	 * Crea gli attributi alla riga in base all'articolo.
	 *
	 * @param riga
	 *            riga
	 * @param articolo
	 *            articolo
	 * @return riga con gli attributi creati
	 */
	IRigaArticoloDocumento creaAttributiRiga(IRigaArticoloDocumento riga, Articolo articolo);

	/**
	 * Crea una riga articolo.
	 *
	 * @param rigaArticolo
	 *            rigaArticolo prototype
	 * @param parametriCreazioneArticolo
	 *            parametri con i dati utili alla creazione della riga articolo *
	 * @return rigaArticolo con i parametri settatti
	 */
	IRigaArticoloDocumento creaRigaArticoloDocumento(IRigaArticoloDocumento rigaArticolo,
			ParametriCreazioneRigaArticolo parametriCreazioneArticolo);

}
