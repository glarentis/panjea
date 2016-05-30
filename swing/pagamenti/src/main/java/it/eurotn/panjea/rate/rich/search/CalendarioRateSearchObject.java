package it.eurotn.panjea.rate.rich.search;

import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.rich.bd.ICalendariRateBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class CalendarioRateSearchObject extends AbstractSearchObject {

	private ICalendariRateBD calendariRateBD;

	/**
	 * Costruttore.
	 * 
	 */
	public CalendarioRateSearchObject() {
		super("calendarioRateSearchObject");
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return calendariRateBD.caricaCalendariRate(fieldSearch, valueSearch);
	}

	@Override
	public void openEditor(Object object) {

		CalendarioRate calendarioRate = (CalendarioRate) object;
		if (calendarioRate.getId() != null) {
			calendarioRate = calendariRateBD.caricaCalendarioRate(calendarioRate, true);
		}
		LifecycleApplicationEvent event = new OpenEditorEvent(calendarioRate);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	/**
	 * @param calendariRateBD
	 *            the calendariRateBD to set
	 */
	public void setCalendariRateBD(ICalendariRateBD calendariRateBD) {
		this.calendariRateBD = calendariRateBD;
	}
}
