package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class LivelloAmministrativo1Page extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "livelloAmministrativo1Page";
	private IDatiGeograficiBD datiGeograficiBD = null;
	private DatiGeografici datiGeografici = null;

	/**
	 * Costruttore.
	 */
	public LivelloAmministrativo1Page() {
		super(PAGE_ID, new LivelloAmministrativo1Form());
		setShowTitlePane(false);
	}

	@Override
	protected Object doDelete() {
		datiGeograficiBD.cancellaSuddivisioneAmministrativa((LivelloAmministrativo1) getBackingFormPage()
				.getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		LivelloAmministrativo1 livelloAmministrativo1 = (LivelloAmministrativo1) this.getForm().getFormObject();
		return datiGeograficiBD.salvaSuddivisioneAmministrativa(livelloAmministrativo1);
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
