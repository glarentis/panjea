package it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione3;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione3Intra;
import it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione1.Sezione1BeniPage;

public class Sezione3ServiziPage extends Sezione1BeniPage {

	/**
	 * Costruttore.
	 */
	public Sezione3ServiziPage() {
		super("sezione3ServiziPage", new Sezione3ServiziForm(new RigaSezione3Intra()));
	}

	/**
	 * @param dichiarazioneIntra
	 *            the dichiarazioneIntra to set
	 */
	@Override
	public void setDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		this.dichiarazioneIntra = dichiarazioneIntra;
		((Sezione3ServiziForm) getBackingFormPage()).setDichiarazioneIntra(dichiarazioneIntra);
	}

}