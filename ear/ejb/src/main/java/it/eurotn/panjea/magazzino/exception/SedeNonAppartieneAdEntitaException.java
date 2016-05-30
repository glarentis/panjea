package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;

import java.util.List;

public class SedeNonAppartieneAdEntitaException extends Exception {

	List<Documento> documenti;

	public SedeNonAppartieneAdEntitaException(List<Documento> documenti) {
		this.documenti = documenti;
	}

	public List<Documento> getDocumenti() {
		return documenti;
	}
}
