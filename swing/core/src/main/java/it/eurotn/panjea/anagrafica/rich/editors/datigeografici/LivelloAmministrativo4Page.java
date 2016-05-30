package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class LivelloAmministrativo4Page extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "livelloAmministrativo4Page";
	private IDatiGeograficiBD datiGeograficiBD;
	private DatiGeografici datiGeografici = null;

	/**
	 * Costruttore.
	 */
	public LivelloAmministrativo4Page() {
		super(PAGE_ID, new LivelloAmministrativo4Form());
		setShowTitlePane(false);
	}

	@Override
	protected Object doDelete() {
		datiGeograficiBD.cancellaSuddivisioneAmministrativa((LivelloAmministrativo4) getBackingFormPage()
				.getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		LivelloAmministrativo4 livelloAmministrativo = (LivelloAmministrativo4) this.getForm().getFormObject();
		return datiGeograficiBD.salvaSuddivisioneAmministrativa(livelloAmministrativo);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	/**
	 * @return the datiGeografici
	 */
	public DatiGeografici getDatiGeografici() {
		return datiGeografici;
	}

	/**
	 * @return the datiGeograficiBD
	 */
	public IDatiGeograficiBD getDatiGeograficiBD() {
		return datiGeograficiBD;
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onNew() {
		super.onNew();
		getBackingFormPage().getFormModel().getValueModel("nazione").setValue(datiGeografici.getNazione());

		LivelloAmministrativo3 livelloAmministrativo3 = null;
		if (datiGeografici.hasLivelloAmministrativo3()) {
			livelloAmministrativo3 = datiGeografici.getLivelloAmministrativo3();
		}
		getBackingFormPage().getFormModel().getValueModel("suddivisioneAmministrativaPrecedente")
		.setValue(livelloAmministrativo3);
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {

	}

	/**
	 * @param datiGeografici
	 *            the datiGeografici to set
	 */
	public void setDatiGeografici(DatiGeografici datiGeografici) {
		this.datiGeografici = datiGeografici;
	}

	/**
	 * @param datiGeograficiBD
	 *            the datiGeograficiBD to set
	 */
	public void setDatiGeograficiBD(IDatiGeograficiBD datiGeograficiBD) {
		this.datiGeograficiBD = datiGeograficiBD;
	}

}
