package it.eurotn.panjea.beniammortizzabili.rich.exceptionhandler;

import it.eurotn.panjea.beniammortizzabili.exception.AreeContabiliDaSimulazionePresentiException;

import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

public class AreeContabiliDaSimulazionePresentiExceptionExceptionHandler extends MessagesDialogExceptionHandler {

	private int evaluatedChainedIndex = 0;

	@SuppressWarnings("rawtypes")
	@Override
	public Object createExceptionContent(Throwable throwable) {
		Throwable evaluatedThrowable = determineEvaluatedThrowable(throwable);
		Class clazz = evaluatedThrowable.getClass();
		while (clazz != Object.class) {
			clazz = clazz.getSuperclass();
		}
		String message = "Impossibile effettuare la generazione dei documenti.\nEsistono già delle aree contabili generate dalle simulazioni per l'anno ";
		if (evaluatedThrowable instanceof AreeContabiliDaSimulazionePresentiException) {
			message = message + ((AreeContabiliDaSimulazionePresentiException) evaluatedThrowable).getAnnoMovimento();
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
