package it.eurotn.panjea.ordini.rich.editors.righeinserimento;

import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;

public class RigheInserimentoController {

	private RigheInserimentoPage righeInserimentoPage;

	/**
	 * Costruttore.
	 */
	public RigheInserimentoController() {
		super();
	}

	/**
	 * Carica le righe inserimento in base ai parametri.
	 *
	 * @param parametri
	 *            parametri di ricerca
	 */
	public void loadRigheInserimento(ParametriRigheOrdineInserimento parametri) {

		righeInserimentoPage.update(parametri);

	}

	/**
	 * @param righeInserimentoPage
	 *            the righeInserimentoPage to set
	 */
	public void setRigheInserimentoPage(RigheInserimentoPage righeInserimentoPage) {
		this.righeInserimentoPage = righeInserimentoPage;
	}
}
