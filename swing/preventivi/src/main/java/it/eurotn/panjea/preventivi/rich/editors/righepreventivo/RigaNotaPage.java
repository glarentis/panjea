package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.preventivi.domain.RigaNota;
import it.eurotn.panjea.preventivi.rich.forms.righepreventivo.RigaNotaForm;

import org.apache.log4j.Logger;

public class RigaNotaPage extends AbstractRigaPreventivoPage<RigaNota> {

	private static final String PAGE_ID = "rigaNotaPage";
	private static Logger logger = Logger.getLogger(RigaNotaPage.class);

	/**
	 * costruttore.
	 */
	public RigaNotaPage() {
		super(PAGE_ID, new RigaNotaForm(), "rigaPreventivoNotaPage");
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected RigaNota getNewRigaPreventivo() {
		return new RigaNota();
	}

}
