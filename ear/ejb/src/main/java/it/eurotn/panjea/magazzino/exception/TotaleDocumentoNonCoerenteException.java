package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.iva.domain.RigaIva;

import java.util.List;

/**
 * Indica che il totale documento calcolato nell'area magazzino non corrisponde <br/>
 * al totale documento inserito da un'altra area.
 * 
 * @author giangi
 */
public class TotaleDocumentoNonCoerenteException extends Exception {
	private static final long serialVersionUID = -8436330124727021727L;

	/**
	 * Se il documento contiene un'area Iva<br/>
	 * tengo il riferimento alle righe iva calcolate.
	 * 
	 * @uml.property name="righeIva"
	 */
	private List<RigaIva> righeIva;

	/**
	 * @uml.property name="totaleAreaMagazzino"
	 * @uml.associationEnd
	 */
	private Importo totaleAreaMagazzino;

	/**
	 * Costruttore.
	 * 
	 * @param totaleAreaMagazzino
	 *            totale dell'area magazzino
	 */
	public TotaleDocumentoNonCoerenteException(final Importo totaleAreaMagazzino) {
		super();
		this.totaleAreaMagazzino = totaleAreaMagazzino;
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param righeIvaDaControllare
	 *            righe da controllare
	 */
	public TotaleDocumentoNonCoerenteException(final List<RigaIva> righeIvaDaControllare) {
		super();
		this.righeIva = righeIvaDaControllare;
	}

	/**
	 * @return righeIva
	 * @uml.property name="righeIva"
	 */
	public List<RigaIva> getRigheIva() {
		return righeIva;
	}

	/**
	 * @return totaleAreaMagazzino
	 * @uml.property name="totaleAreaMagazzino"
	 */
	public Importo getTotaleAreaMagazzino() {
		return totaleAreaMagazzino;
	}

	/**
	 * @param righeIva
	 *            the righeIva to set
	 * @uml.property name="righeIva"
	 */
	public void setRigheIva(List<RigaIva> righeIva) {
		this.righeIva = righeIva;
	}

	/**
	 * @param totaleAreaMagazzino
	 *            the totaleAreaMagazzino to set
	 * @uml.property name="totaleAreaMagazzino"
	 */
	public void setTotaleAreaMagazzino(Importo totaleAreaMagazzino) {
		this.totaleAreaMagazzino = totaleAreaMagazzino;
	}
}
