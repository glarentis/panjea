package it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione4;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione4Intra;
import it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione1.Sezione1BeniPage;

public class Sezione4ServiziPage extends Sezione1BeniPage {

	/**
	 * Costruttore.
	 */
	public Sezione4ServiziPage() {
		super("sezione4ServiziPage", new Sezione4ServiziForm(new RigaSezione4Intra()));
	}

	/**
	 * @param dichiarazioneIntra
	 *            the dichiarazioneIntra to set
	 */
	@Override
	public void setDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		this.dichiarazioneIntra = dichiarazioneIntra;
		((Sezione4ServiziForm) getBackingFormPage()).setDichiarazioneIntra(dichiarazioneIntra);
	}

}