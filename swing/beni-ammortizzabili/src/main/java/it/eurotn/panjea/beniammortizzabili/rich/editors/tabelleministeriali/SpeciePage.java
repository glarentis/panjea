package it.eurotn.panjea.beniammortizzabili.rich.editors.tabelleministeriali;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * Pagina per gestire una <code>SottoSpecie</code>. Non sovrascrivo la getCommand() per fare in modo di non mettere la
 * toolbar ma avere comunque la possibilita' di usare i command saveCommad e undoCommand
 * 
 * @author Aracno
 * @version 1.0, 27/set/06
 * 
 */
public class SpeciePage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(SpeciePage.class);

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	private static final String PAGE_ID = "speciePage";

	/**
	 * Costruttore.
	 * 
	 * @param specie
	 *            specie
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public SpeciePage(final Specie specie, final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(PAGE_ID, new SpecieForm(specie));
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	protected Object doSave() {
		Specie specie = (Specie) getBackingFormPage().getFormObject();
		logger.debug("--> Salvo l'oggetto: " + specie);
		Specie specieSalvata = beniAmmortizzabiliBD.salvaSpecie(specie);
		setFormObject(specieSalvata);
		return specieSalvata;
	}

	/**
	 * @return lockCommand
	 */
	public AbstractCommand getLockCommand() {
		return toolbarPageEditor.getLockCommand();
	}

	/**
	 * @return undoCommand
	 */
	public AbstractCommand getUndoCommand() {
		return toolbarPageEditor.getUndoCommand();
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

}
