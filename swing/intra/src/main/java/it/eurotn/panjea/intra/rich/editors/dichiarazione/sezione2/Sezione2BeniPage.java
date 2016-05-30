package it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione2;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione2Intra;
import it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione1.Sezione1BeniPage;

public class Sezione2BeniPage extends Sezione1BeniPage {

	/**
	 * Costruttore.
	 */
	public Sezione2BeniPage() {
		super("sezione2BeniPage", new Sezione2BeniForm(new RigaSezione2Intra()));
	}

	/**
	 * @param dichiarazioneIntra
	 *            the dichiarazioneIntra to set
	 */
	@Override
	public void setDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		this.dichiarazioneIntra = dichiarazioneIntra;
		((Sezione2BeniForm) getBackingFormPage()).setDichiarazioneIntra(dichiarazioneIntra);
	}
}
