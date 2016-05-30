package it.eurotn.panjea.rate.rich.editors.calendarirate;

import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.RigaCalendarioRate;
import it.eurotn.panjea.rate.rich.bd.ICalendariRateBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RigheCalendarioRateTablePage extends AbstractTablePageEditor<RigaCalendarioRate> {

	public static final String PAGE_ID = "righeCalendarioRateTablePage";

	private CalendarioRate calendarioRate;

	private ICalendariRateBD calendariRateBD;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	protected RigheCalendarioRateTablePage() {
		super(PAGE_ID, new String[] { "dataIniziale", "dataFinale", "ripeti", "dataAlternativa", "note" },
				RigaCalendarioRate.class);
	}

	@Override
	public Collection<RigaCalendarioRate> loadTableData() {
		List<RigaCalendarioRate> righeCalendario = new ArrayList<RigaCalendarioRate>();
		if (this.calendarioRate.getId() != null) {
			righeCalendario = calendariRateBD.caricaRigheCalendarioRate(calendarioRate);
		}

		return righeCalendario;

	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<RigaCalendarioRate> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param calendariRateBD
	 *            the calendariRateBD to set
	 */
	public void setCalendariRateBD(ICalendariRateBD calendariRateBD) {
		this.calendariRateBD = calendariRateBD;
	}

	@Override
	public void setFormObject(Object object) {
		this.calendarioRate = (CalendarioRate) object;

		((RigaCalendarioRatePage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME))
				.setCalendarioRate(calendarioRate);
	}

}
