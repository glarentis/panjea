package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Lanciata quando non esistono combinazioni categoriacontabileArticolo-categoriacontabileDeposito-categoriacontabileSedeMagazzino.<br> L'eccezione contiene una stringa formattata nel seguentew modo:<br> codiceCategoriacontabileDeposito | codiceCategoriacontabileSedeMagazzino | codiceCategoriacontabileArticolo
 * @author  fattazzo
 */
public class SottoContiContabiliAssentiException extends Exception {

	private static final long serialVersionUID = -1864659823944114641L;

	/**
	 * @uml.property  name="sottoContoContabileAssenteExceptions"
	 */
	private final List<SottoContoContabileAssenteException> sottoContoContabileAssenteExceptions;

	/**
	 * Costruttore.
	 */
	public SottoContiContabiliAssentiException() {
		super();
		sottoContoContabileAssenteExceptions = new ArrayList<SottoContoContabileAssenteException>();
	}

	/**
	 * Permette di aggiungere i risultati di eventuali exception ricevuto da più documenti.
	 * 
	 * @param sottoContiContabileAssenteException sottoContiContabileAssenteException
	 */
	public void add(SottoContiContabiliAssentiException sottoContiContabileAssenteException) {
		sottoContoContabileAssenteExceptions.addAll(sottoContiContabileAssenteException
				.getSottoContoContabileAssenteExceptions());
	}

	/**
	 * Aggiunge un {@link SottoContoContabileAssenteException}.
	 * 
	 * @param sottoContoContabileAssenteException sottoContoContabileAssenteException
	 */
	public void add(SottoContoContabileAssenteException sottoContoContabileAssenteException) {
		sottoContoContabileAssenteExceptions.add(sottoContoContabileAssenteException);
	}

	/**
	 * @return the sottoContoContabileAssenteExceptions
	 */
	public List<SottoContoContabileAssenteException> getAllSottoContoContabileAssenteExceptions() {

		List<SottoContoContabileAssenteException> list = new ArrayList<SottoContoContabileAssenteException>();

		for (SottoContoContabileAssenteException exception : sottoContoContabileAssenteExceptions) {

			for (ArticoloLite articolo : exception.getArticoli()) {
				list.add(new SottoContoContabileAssenteException(exception.getCategoriaContabileArticolo(), exception
						.getCategoriaContabileDeposito(), exception.getCategoriaContabileSedeMagazzino(), articolo,
						exception.getDeposito(), exception.getSedeEntita()));
			}
		}

		return list;
	}

	/**
	 * @return  the sottoContoContabileAssenteExceptions
	 * @uml.property  name="sottoContoContabileAssenteExceptions"
	 */
	public List<SottoContoContabileAssenteException> getSottoContoContabileAssenteExceptions() {
		return sottoContoContabileAssenteExceptions;
	}
}
