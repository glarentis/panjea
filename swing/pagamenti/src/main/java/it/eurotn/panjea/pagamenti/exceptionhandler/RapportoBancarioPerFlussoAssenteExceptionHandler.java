/**
 * 
 */
package it.eurotn.panjea.pagamenti.exceptionhandler;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.panjea.tesoreria.service.exception.RapportoBancarioPerFlussoAssenteException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

/**
 * @author Leonardo
 * 
 */
public class RapportoBancarioPerFlussoAssenteExceptionHandler extends
		MessagesDialogExceptionHandler {

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
		String[] codes = messageDescriptionKeyList
				.toArray(new String[messageDescriptionKeyList.size()]);
		String[] parameters = new String[] { formatMessage(evaluatedThrowable
				.getMessage()) };
		if (evaluatedThrowable instanceof RapportoBancarioPerFlussoAssenteException) {
			RapportoBancarioPerFlussoAssenteException ex = (RapportoBancarioPerFlussoAssenteException) evaluatedThrowable;
			List<EntitaLite> entities = ex.getErrors();
			// devo lanciare l'apertura della search result entita' riempiendo
			// la tabella con
			// le entita' contenute nell'exception
			if (entities != null && entities.size() > 0) {
				try {
					ApplicationPage applicationPage = Application.instance()
							.getActiveWindow().getPage();
					((PanjeaDockingApplicationPage) applicationPage)
							.openResultView(ClienteLite.class.getName(),
									entities);
				} catch (Exception e) {
					logger.error("--> errore in createExceptionContent", e);
				}
			}
		}
		return messageSourceAccessor
				.getMessage(new DefaultMessageSourceResolvable(codes,
						parameters, codes[0]));
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
