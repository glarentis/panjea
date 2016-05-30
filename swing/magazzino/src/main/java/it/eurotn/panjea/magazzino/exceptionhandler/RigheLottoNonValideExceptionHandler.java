package it.eurotn.panjea.magazzino.exceptionhandler;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

import com.jidesoft.converter.ObjectConverterManager;

public class RigheLottoNonValideExceptionHandler extends MessagesDialogExceptionHandler {

	private int evaluatedChainedIndex = 0;

	@Override
	public Object createExceptionContent(Throwable throwable) {
		List<String> messageDescriptionKeyList = new ArrayList<String>();
		Throwable evaluatedThrowable = determineEvaluatedThrowable(throwable);
		Class<?> clazz = evaluatedThrowable.getClass();
		while (clazz != Object.class) {
			messageDescriptionKeyList.add(clazz.getName() + ".description");
			clazz = clazz.getSuperclass();
		}
		String[] codes = messageDescriptionKeyList.toArray(new String[messageDescriptionKeyList.size()]);
		String[] parameters;
		if (evaluatedThrowable instanceof RigheLottiNonValideException) {
			RigheLottiNonValideException righeLottiNonValideException = (RigheLottiNonValideException) evaluatedThrowable;
			ArticoloLite articolo = righeLottiNonValideException.getRigaArticolo().getArticolo();
			parameters = new String[] { ObjectConverterManager.toString(articolo) };
		} else {
			parameters = new String[] { formatMessage(evaluatedThrowable.getMessage()) };
		}
		return messageSourceAccessor.getMessage(new DefaultMessageSourceResolvable(codes, parameters, codes[0]));
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
