package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.AreaMagazzinoRiepilogoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Gestisce il riepilogo per l'area magazzino.
 * 
 * @author giangi
 * 
 */
public class AreaMagazzinoRiepilogoPage extends FormBackedDialogPageEditor {
	public static final String PAGE_ID = "areaMagazzinoRiepilogoPage";

	/**
	 * Costruttore.
	 * 
	 */
	public AreaMagazzinoRiepilogoPage() {
		super(PAGE_ID, new AreaMagazzinoRiepilogoForm());
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand() };
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
