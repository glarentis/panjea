package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.preventivi.domain.RigaTestata;
import it.eurotn.panjea.preventivi.rich.forms.righepreventivo.RigaTestataForm;

import org.apache.log4j.Logger;

public class RigaTestataPage extends AbstractRigaPreventivoPage<RigaTestata> {

	private static final String PAGE_ID = "rigaTestataPage";
	private static Logger logger = Logger.getLogger(RigaTestataPage.class);

	/**
	 * costruttore.
	 */
	public RigaTestataPage() {
		super(PAGE_ID, new RigaTestataForm(), "rigaPreventivoTestataPage");
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected RigaTestata getNewRigaPreventivo() {
		return new RigaTestata();
	}
}
