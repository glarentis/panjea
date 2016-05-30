package it.eurotn.panjea.beniammortizzabili.rich.editors.tabelleministeriali;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * Pagina per gestire una <code>SottoSpecie</code>. Non sovrascrivo la getCommand() per fare in modo di non mettere la
 * toolbar ma avere comunque la possibilita' di usare i command saveCommad e undoCommand
 * 
 * @author Aracno
 * @version 1.0, 27/set/06
 * 
 */
public class SottoSpeciePage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(SottoSpeciePage.class);

	private static final String PAGE_ID = "sottoSpeciePage";

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 * 
	 * @param sottoSpecie
	 *            sottoSpecie
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public SottoSpeciePage(final SottoSpecie sottoSpecie, final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(PAGE_ID, new SottoSpecieForm(sottoSpecie));
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	protected Object doSave() {
		SottoSpecie sottoSpecie = (SottoSpecie) getBackingFormPage().getFormObject();
		logger.debug("--> Salvo l'oggetto: " + sottoSpecie);
		SottoSpecie sottoSpecieSalvata = beniAmmortizzabiliBD.salvaSottoSpecie(sottoSpecie);
		setFormObject(sottoSpecieSalvata);
		return sottoSpecieSalvata;
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
