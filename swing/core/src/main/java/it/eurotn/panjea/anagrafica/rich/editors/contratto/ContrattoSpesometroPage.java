/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.contratto;

import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.forms.contratto.ContrattoSpesometroForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author leonardo
 */
public class ContrattoSpesometroPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "contrattoSpesometroPage";
	private IAnagraficaTabelleBD anagraficaTabelleBD = null;

	/**
	 * Costruttore.
	 */
	public ContrattoSpesometroPage() {
		super(PAGE_ID, new ContrattoSpesometroForm());
	}

	@Override
	protected Object doDelete() {
		anagraficaTabelleBD.cancellaContratto((ContrattoSpesometro) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		ContrattoSpesometro contratto = anagraficaTabelleBD.salvaContratto((ContrattoSpesometro) getBackingFormPage()
				.getFormObject());
		return contratto;
	}

	/**
	 * @return the anagraficaTabelleBD
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	public void loadData() {

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
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
		((ContrattoSpesometroForm) getBackingFormPage()).setAnagraficaTabelleBD(anagraficaTabelleBD);
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		((ContrattoSpesometroForm) getBackingFormPage()).setEntita(entita);
	}

}
