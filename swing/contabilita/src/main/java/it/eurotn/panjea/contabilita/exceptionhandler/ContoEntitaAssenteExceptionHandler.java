package it.eurotn.panjea.contabilita.exceptionhandler;

import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

public class ContoEntitaAssenteExceptionHandler extends MessagesDialogExceptionHandler {

	private int evaluatedChainedIndex = 0;

	@Override
	public Object createExceptionContent(Throwable throwable) {
		List<String> messageDescriptionKeyList = new ArrayList<String>();
		Throwable evaluatedThrowable = determineEvaluatedThrowable(throwable);
		Class clazz = evaluatedThrowable.getClass();
		while (clazz != Object.class) {
			messageDescriptionKeyList.add(clazz.getName() + ".description");
			clazz = clazz.getSuperclass();
		}
		String[] codes = messageDescriptionKeyList.toArray(new String[messageDescriptionKeyList.size()]);
		String[] parameters;
		if (evaluatedThrowable instanceof ContoEntitaAssenteException) {
			ContoEntitaAssenteException contoAssenteException = (ContoEntitaAssenteException) evaluatedThrowable;

			String entita = contoAssenteException.getEntita();
			if (entita != null) {
				String entityType = messageSourceAccessor.getMessage(contoAssenteException.getEntitaLite().getClass()
						.getName());
				parameters = new String[] { entityType, "<" + entita + ">" };
			} else {
				parameters = new String[] { "<Sconosciuto>", "<Sconosciuto>" };
			}
		} else {
			parameters = new String[] { formatMessage(evaluatedThrowable.getMessage()) };
		}
		return messageSourceAccessor.getMessage(new DefaultMessageSourceResolvable(codes, parameters, codes[0]));
	}

	/**
	 * 
	 * @param throwable
	 * @return
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
