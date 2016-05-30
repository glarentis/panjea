package it.eurotn.panjea.magazzino.rich.editors.righemagazzino.datiaccompagnatori;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import org.springframework.richclient.dialog.FormBackedDialogPage;

/**
 * @author mattia
 */
public class DatiAccompagnatoriDialog extends PanjeaTitledPageApplicationDialog {

	private AreaMagazzinoFullDTO areaMagazzinoFullDTO;
	private RichiestaDatiAccompagnatoriForm richiestaForm;

	/**
	 * @param areaMagazzinoFullDTO
	 *            areaMagazzinoFullDTO
	 */
	public DatiAccompagnatoriDialog(final AreaMagazzinoFullDTO areaMagazzinoFullDTO) {
		super();
		this.areaMagazzinoFullDTO = areaMagazzinoFullDTO;
		richiestaForm = new RichiestaDatiAccompagnatoriForm(areaMagazzinoFullDTO);
		FormBackedDialogPage formBackedDialogPage = new FormBackedDialogPage(richiestaForm);
		setDialogPage(formBackedDialogPage);
	}

	/**
	 * 
	 * @return areaMagazzinoFullDTO
	 */
	public AreaMagazzinoFullDTO getAreaMagazzinoFullDTO() {
		return this.areaMagazzinoFullDTO;
	}

	@Override
	protected String getTitle() {
		return getMessage("datiAccompagnatoriDialog.title.label");
	}

	@Override
	protected boolean isMessagePaneVisible() {
		return false;
	}

	@Override
	protected void onCancel() {
		richiestaForm.getFormModel().revert();
		super.onCancel();
	}

	@Override
	protected boolean onFinish() {
		// non salvo l'area magazzino, modifico solo i dati dell'area magazzino
		// il salvataggio e' delegato ad altro processo
		return true;
	}
}
