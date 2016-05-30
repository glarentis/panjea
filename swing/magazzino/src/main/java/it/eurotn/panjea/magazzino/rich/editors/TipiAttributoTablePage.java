/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.TipoAttributoEditFrame;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.TipoAttributoTableModel;
import it.eurotn.rich.command.support.JecGlobalCommandIds;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.command.TargetableActionCommand;

/**
 * @author fattazzo
 * 
 */
public class TipiAttributoTablePage extends AbstractTablePageEditor<TipoAttributo> implements InitializingBean,
		Observer {

	public static final String PAGE_ID = "tipiAttributoTablePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 * 
	 */
	public TipiAttributoTablePage() {
		super(PAGE_ID, new TipoAttributoTableModel());
		setEnableAutoResizeRow(true);
		getTable().setDelayForSelection(200);
		getTable().addSelectionObserver(this);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setEditPage(new TipoAttributoPage(magazzinoAnagraficaBD, anagraficaTabelleBD));
	}

	@Override
	protected EditFrame<TipoAttributo> createEditFrame() {
		return new TipoAttributoEditFrame(EEditPageMode.DETAIL, this, EditFrame.QUICK_ACTION_DEFAULT);

	}

	@Override
	public AbstractCommand[] getCommands() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getRefreshCommand() };
		return abstractCommands;
	}

	@Override
	public Object getManagedObject(Object pageObject) {
		return null;
	}

	@Override
	public Collection<TipoAttributo> loadTableData() {
		return magazzinoAnagraficaBD.caricaTipiAttributo();
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void onRefresh() {
		loadData();

	}

	@Override
	public void processTableData(Collection<TipoAttributo> results) {
		super.processTableData(results);

		// abilita/disabilita table page
		updateControl();
		updateCommands();
	}

	@Override
	public Collection<TipoAttributo> refreshTableData() {
		List<TipoAttributo> tipiAttributo = null;
		tipiAttributo = Collections.emptyList();
		return tipiAttributo;
	}

	/**
	 * metodo incaricato di aggiornare i global command con i commads della page corrente.
	 */
	@SuppressWarnings("unchecked")
	protected void registerGlobalCommands() {
		logger.debug("--> Enter registerGlobalCommands");
		// Aggiorno i globalCommand
		Map<String, ActionCommandExecutor> context = new HashMap<String, ActionCommandExecutor>();
		context.put(JecGlobalCommandIds.NEW, getEditorNewCommand());
		context.put(JecGlobalCommandIds.EDIT, getEditorLockCommand());
		context.put(JecGlobalCommandIds.UNDO_MODEL, getEditorUndoCommand());
		context.put(JecGlobalCommandIds.SAVE, getEditorSaveCommand());
		context.put(JecGlobalCommandIds.DELETE, getEditorDeleteCommand());
		for (Iterator<Object> i = getActiveWindow().getSharedCommands(); i.hasNext();) {
			TargetableActionCommand globalCommand = (TargetableActionCommand) i.next();
			if (context.containsKey(globalCommand.getId())) {
				logger.debug("--> register di " + globalCommand.getId() + " " + context.get(globalCommand.getId()));
				globalCommand.setCommandExecutor(context.get(globalCommand.getId()));
			}
		}
		logger.debug("--> Exit registerGlobalCommands");
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	@Override
	public void update(Observable o, Object obj) {
		updateCommands();
	}

	/**
	 * Aggiorna lo stato dei comandi.
	 */
	protected void updateCommands() {
		logger.debug("--> Enter updateCommands");
		boolean enabled = getTable().getRows().size() != 0;
		getEditorDeleteCommand().setEnabled(enabled);
		getRefreshCommand().setEnabled(true);
		logger.debug("--> Exit updateCommands");
	}

	/**
	 * abilita disabilita la table page corrente.
	 */
	protected void updateControl() {
	}
}
