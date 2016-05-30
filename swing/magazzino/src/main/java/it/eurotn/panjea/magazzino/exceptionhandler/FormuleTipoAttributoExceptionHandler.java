package it.eurotn.panjea.magazzino.exceptionhandler;

import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;

import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

public class FormuleTipoAttributoExceptionHandler extends MessagesDialogExceptionHandler {

	private int evaluatedChainedIndex = 0;

	@SuppressWarnings("rawtypes")
	@Override
	public Object createExceptionContent(Throwable throwable) {
		Throwable evaluatedThrowable = determineEvaluatedThrowable(throwable);
		Class clazz = evaluatedThrowable.getClass();
		while (clazz != Object.class) {
			clazz = clazz.getSuperclass();
		}
		String message = "Errore nella formula per il calcolo delle quantità";
		if (evaluatedThrowable instanceof FormuleTipoAttributoException) {
			message = ((FormuleTipoAttributoException) evaluatedThrowable).toString();
		}
		return message;
	}

	/**
	 * 
	 * @param throwable
	 *            throwable
	 * @return Throwable
	 */
	private Throwable determineEvaluatedThrowable(Throwable throwable) {
		Throwable evaluatedThrowable = throwable;
		for (int i = 0; i < evaluatedChainedIndex; i++) {
			Throwable cause = evaluatedThrowable.getCause();
			if (cause == null || cause == evaluatedThrowable) {
				break;
			} else {
				evaluatedThrowable = cause;
			}
		}
		return evaluatedThrowable;
	}

	/**
	 * @param evaluatedChainedIndex
	 *            The evaluatedChainedIndex to set.
	 */
	public void setEvaluatedChainedIndex(int evaluatedChainedIndex) {
		this.evaluatedChainedIndex = evaluatedChainedIndex;
	}
}
