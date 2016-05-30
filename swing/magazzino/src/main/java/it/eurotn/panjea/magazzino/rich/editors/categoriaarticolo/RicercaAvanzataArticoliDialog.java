package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.rich.editors.ricercaarticoli.RicercaAvanzataArticoliPage;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.util.Collections;
import java.util.List;

import org.springframework.richclient.dialog.DialogPage;

public class RicercaAvanzataArticoliDialog extends PanjeaTitledPageApplicationDialog {

	private boolean confirmed = false;

	/**
	 * @param dialogPage
	 *            page da aggiungere come contenuto del dialog
	 */
	public RicercaAvanzataArticoliDialog(final DialogPage dialogPage) {
		super(dialogPage);
	}

	/**
	 * @return List<ArticoloRicerca> selezionati o vuoto se
	 */
	public List<ArticoloRicerca> getArticoliSelezionati() {
		List<ArticoloRicerca> articoliRicerca = Collections.emptyList();
		if (confirmed) {
			articoliRicerca = ((RicercaAvanzataArticoliPage) getDialogPage()).getArticoliSelezionati();
		}
		return articoliRicerca;
	}

	@Override
	protected boolean isMessagePaneVisible() {
		return false;
	}

	@Override
	protected void onAboutToShow() {
		confirmed = false;
		super.onAboutToShow();
		((RicercaAvanzataArticoliPage) getDialogPage()).onPostPageOpen();
	}

	@Override
	protected void onCancel() {
		super.onCancel();
		confirmed = false;
	}

	@Override
	protected boolean onFinish() {
		confirmed = true;
		return true;
	}

}