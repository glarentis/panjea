package it.eurotn.panjea.preventivi.rich.bd;

public interface ICopiaDocumentoBD {

	/**
	 * @param idArea
	 *            id del preventivo da copiare
	 * @return copia effettuata
	 */
	Object copiaArea(Integer idArea);
}
