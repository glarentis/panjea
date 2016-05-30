package it.eurotn.panjea.partite.rich.tabelle;

import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * 
 * @author vittorio
 * @version 1.0, 30/apr/08
 * 
 */

public class CategorieRatePage extends FormBackedDialogPageEditor {
	private static Logger logger = Logger.getLogger(CategorieRatePage.class.getName());
	private IPartiteBD partiteBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della pagina
	 */
	public CategorieRatePage(final String pageID) {
		super(pageID, new CategoriaRateForm(new CategoriaRata()));
	}

	@Override
	protected Object doDelete() {
		partiteBD.cancellaCategoriaRata(((CategoriaRata) getBackingFormPage().getFormObject()).getId());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		CategoriaRata categoriaRata = (CategoriaRata) this.getForm().getFormObject();
		logger.debug("--> Exit doSave");
		return partiteBD.salvaCategoriaRata(categoriaRata);
	}

	@Override
	public AbstractCommand[] getCommand() {
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
	 * @param partiteBD
	 *            The partiteBD to set.
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}
}
