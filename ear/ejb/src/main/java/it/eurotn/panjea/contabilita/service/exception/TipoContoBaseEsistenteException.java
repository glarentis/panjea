/**
 * 
 */
package it.eurotn.panjea.contabilita.service.exception;

/**
 * Eccezione sollevata quando esiste già un tipo conto base salvato. 
 * 
 * @author fattazzo
 * @version 1.0, 27/ott/07
 * 
 */
public class TipoContoBaseEsistenteException extends Exception {

	private static final long serialVersionUID = -4592140113305820530L;

	public TipoContoBaseEsistenteException(String arg0) {
		super(arg0);
	}

	public TipoContoBaseEsistenteException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TipoContoBaseEsistenteException(Throwable arg0) {
		super(arg0);
	}

}
