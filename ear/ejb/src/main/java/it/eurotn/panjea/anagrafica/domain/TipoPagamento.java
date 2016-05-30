package it.eurotn.panjea.anagrafica.domain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public enum TipoPagamento {
	RIBA, BONIFICO, RID, BOLLETTINO_FRECCIA, RIMESSA_DIRETTA, F24;

	/**
	 * 
	 * @return isRapportoBancarioRequired
	 */
	public boolean isRapportoBancarioRequired() {
		switch (this) {
		case RIBA:
		case BONIFICO:
		case RID:
		case BOLLETTINO_FRECCIA:
		case F24:
			return true;
		default:
			return false;
		}
	}
}
