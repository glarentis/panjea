package it.eurotn.panjea.magazzino.exceptionhandler;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.exception.DistintaCircolareException;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;
import org.springframework.richclient.util.RcpSupport;

public class DistintaCircolareExceptionHandler extends MessagesDialogExceptionHandler {

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

		StringBuilder sb = new StringBuilder();
		sb.append("<html>Riferimento circolare nel seguente ramo:<br>");
		List<ArticoloLite> componentiElaborati = ((DistintaCircolareException) evaluatedThrowable)
				.getComponentiElaborati();
		for (ArticoloLite articoloLite : componentiElaborati) {
			sb.append("->");
			sb.append(articoloLite.getCodice());
		}
		sb.append("<br>Alla chiusura del messaggio verrà rimosso il riferimento circolare.<br>");
		// sb.append("<br>Alla chiusura del messaggio il riferimento circolare verrà eliminato.");
		sb.append("</html>");
		return new JLabel(sb.toString());
	}

	/**
	 * Determine evaluated throwable.
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

	@Override
	public void notifyUserAboutException(Thread thread, Throwable throwable) {
		super.notifyUserAboutException(thread, throwable);
		IMagazzinoAnagraficaBD magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
		magazzinoAnagraficaBD.rimuoviReferenzaCircolare(((DistintaCircolareException) throwable).getArticolo());
	}

	/**
	 * @param evaluatedChainedIndex
	 *            The evaluatedChainedIndex to set.
	 */
	public void setEvaluatedChainedIndex(int evaluatedChainedIndex) {
		this.evaluatedChainedIndex = evaluatedChainedIndex;
	}
}
