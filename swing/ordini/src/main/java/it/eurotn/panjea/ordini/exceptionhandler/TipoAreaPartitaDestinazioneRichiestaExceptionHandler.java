package it.eurotn.panjea.ordini.exceptionhandler;

import it.eurotn.panjea.ordini.exception.TipoAreaPartitaDestinazioneRichiestaException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

import com.jidesoft.converter.ObjectConverterManager;

public class TipoAreaPartitaDestinazioneRichiestaExceptionHandler extends MessagesDialogExceptionHandler {

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
		if (evaluatedThrowable instanceof TipoAreaPartitaDestinazioneRichiestaException) {
			TipoAreaPartitaDestinazioneRichiestaException exception = (TipoAreaPartitaDestinazioneRichiestaException) evaluatedThrowable;
			parameters = new String[] { ObjectConverterManager.toString(exception.getTipoDocumentoOrdineDaEvadere()),
					ObjectConverterManager.toString(exception.getTipoDocumentoDestinazione()) };
		} else {
			parameters = new String[] { formatMessage(evaluatedThrowable.getMessage()) };
		}
		return messageSourceAccessor.getMessage(new DefaultMessageSourceResolvable(codes, parameters, codes[0]));
	}

	/**
	 * 
	 * @param throwable
	 *            throwable
	 * @return throwable
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
