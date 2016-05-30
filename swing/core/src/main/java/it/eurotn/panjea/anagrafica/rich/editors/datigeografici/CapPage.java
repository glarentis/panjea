package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

public class CapPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "capPage";
	private IDatiGeograficiBD datiGeograficiBD;
	private DatiGeografici datiGeografici;

	/**
	 * Costruttore.
	 * 
	 * @param datiGeograficiBD
	 *            datiGeograficiBD
	 */
	public CapPage(final IDatiGeograficiBD datiGeograficiBD) {
		super(PAGE_ID, new CapForm(datiGeograficiBD));
		this.datiGeograficiBD = datiGeograficiBD;
	}

	@Override
	protected Object doDelete() {
		datiGeograficiBD.cancellaCap((Cap) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		Cap cap = (Cap) this.getForm().getFormObject();
		List<Localita> localitaDaSalvare = new ArrayList<Localita>();
		List<Localita> localitaCap = new ArrayList<Localita>();
		if (cap.getLocalita() != null) {
			localitaCap.addAll(cap.getLocalita());
			localitaDaSalvare.addAll(localitaCap);
			cap.getLocalita().clear();
		}
		Cap capSalvato = datiGeograficiBD.salvaCap(cap, localitaDaSalvare);
		return capSalvato;
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
		LivelloAmministrativo1 livelloAmministrativo1 = null;
		if (datiGeografici.hasLivelloAmministrativo1()) {
			livelloAmministrativo1 = datiGeografici.getLivelloAmministrativo1();
		}
		LivelloAmministrativo2 livelloAmministrativo2 = null;
		if (datiGeografici.hasLivelloAmministrativo2()) {
			livelloAmministrativo2 = datiGeografici.getLivelloAmministrativo2();
		}
		LivelloAmministrativo3 livelloAmministrativo3 = null;
		if (datiGeografici.hasLivelloAmministrativo3()) {
			livelloAmministrativo3 = datiGeografici.getLivelloAmministrativo3();
		}
		LivelloAmministrativo4 livelloAmministrativo4 = null;
		if (datiGeografici.hasLivelloAmministrativo4()) {
			livelloAmministrativo4 = datiGeografici.getLivelloAmministrativo4();
		}
		getBackingFormPage().getFormModel().getValueModel("livelloAmministrativo1").setValue(livelloAmministrativo1);
		getBackingFormPage().getFormModel().getValueModel("livelloAmministrativo2").setValue(livelloAmministrativo2);
		getBackingFormPage().getFormModel().getValueModel("livelloAmministrativo3").setValue(livelloAmministrativo3);
		getBackingFormPage().getFormModel().getValueModel("livelloAmministrativo4").setValue(livelloAmministrativo4);
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
		((CapForm) getBackingFormPage()).setDatiGeografici(datiGeografici);
	}

	/**
	 * @param datiGeograficiBD
	 *            the datiGeograficiBD to set
	 */
	public void setDatiGeograficiBD(IDatiGeograficiBD datiGeograficiBD) {
		this.datiGeograficiBD = datiGeograficiBD;
	}

}
