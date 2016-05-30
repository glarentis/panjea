package it.eurotn.panjea.anagrafica.rich.editors.entita;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * Pagina contenente le note della sede entita'.
 */
public class NoteSedeEntitaPage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(NoteSedeEntitaPage.class);

	private IAnagraficaBD anagraficaBD = null;
	public static final String PAGE_ID = "noteSedeEntitaPage";

	/**
	 * Costruttore di default.
	 */
	public NoteSedeEntitaPage() {
		super(PAGE_ID, new NoteSedeEntitaForm(new SedeEntita()));

	}

	@Override
	protected Object doSave() {
		SedeEntita sedeEntitaDaSalvare = (SedeEntita) getForm().getFormObject();
		// rimuovo HTML code
		if (isHtmlTextEmpty(sedeEntitaDaSalvare.getNote())) {
			sedeEntitaDaSalvare.setNote(null);
		}
		if (isHtmlTextEmpty(sedeEntitaDaSalvare.getNoteStampa())) {
			sedeEntitaDaSalvare.setNoteStampa(null);
		}

		SedeEntita sedeEntitaResult;
		try {
			sedeEntitaResult = anagraficaBD.salvaSedeEntita(sedeEntitaDaSalvare);
		} catch (SedeEntitaPrincipaleAlreadyExistException e) {
			// qui non ci deve passare dato che salvo solo le note sede
			throw new IllegalArgumentException(e);
		}
		return sedeEntitaResult;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		logger.debug("--> Enter getCommand");
		AbstractCommand[] commands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand() };
		logger.debug("--> Exit getCommand");
		return commands;
	}

	/**
	 * prende un testo un formato html e controla se è vuoto.
	 * 
	 * @param testoHtml
	 *            in formato html
	 * @return boolean
	 */
	public boolean isHtmlTextEmpty(String testoHtml) {
		boolean result = true;
		if (testoHtml != null) {
			String testo = testoHtml.replaceAll("\\<.*?>", "").replaceAll("\n", "");
			if (!testo.trim().isEmpty()) {
				result = false;
			}
		}
		return result;
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
		loadData();
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
		logger.debug("--> Enter setFormObject");
		SedeEntita sedeEntita = (SedeEntita) object;
		if (sedeEntita != null && sedeEntita.getId() != null) {
			sedeEntita = anagraficaBD.caricaSedeEntita(sedeEntita.getId());
		}
		super.setFormObject(sedeEntita);
		logger.debug("--> Exit setFormObject");
	}
}
