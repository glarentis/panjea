package it.eurotn.panjea.rate.rich.search;

import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.rich.bd.ICalendariRateBD;
import it.eurotn.panjea.rich.pages.AbstractTableSearchResult;

import java.util.Collection;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

public class SearchResultCalendariRate extends AbstractTableSearchResult<CalendarioRate> {

	private static final String VIEW_ID = "searchResultCalendariRate";

	private ICalendariRateBD calendariRateBD;

	@Override
	protected CalendarioRate doDelete(CalendarioRate objectToDelete) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getColumnPropertyNames() {
		return new String[] { "descrizione" };
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { getRefreshCommand() };
	}

	@Override
	protected Collection<CalendarioRate> getData(Map<String, Object> parameters) {
		return calendariRateBD.caricaCalendariRate("descrizione", null);
	}

	@Override
	public String getId() {
		return VIEW_ID;
	}

	@Override
	protected Class<CalendarioRate> getObjectsClass() {
		return CalendarioRate.class;
	}

	@Override
	protected Map<String, Object> getParameters() {
		return null;
	}

	@Override
	public Object reloadObject(CalendarioRate object) {
		return calendariRateBD.caricaCalendarioRate(object, true);
	}

	/**
	 * @param calendariRateBD
	 *            the calendariRateBD to set
	 */
	public void setCalendariRateBD(ICalendariRateBD calendariRateBD) {
		this.calendariRateBD = calendariRateBD;
	}

}
