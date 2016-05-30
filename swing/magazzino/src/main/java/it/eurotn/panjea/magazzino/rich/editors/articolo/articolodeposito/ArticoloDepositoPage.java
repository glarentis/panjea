/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.articolodeposito;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloDeposito;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author leonardo
 */
public class ArticoloDepositoPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "articoloDepositoPage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
	private Articolo articolo;

	/**
	 * Costruttore di default,inizializza un nuovo form.
	 */
	public ArticoloDepositoPage() {
		super(PAGE_ID, new ArticoloDepositoForm());
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaArticoloDeposito((ArticoloDeposito) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		ArticoloDeposito articoloDeposito = (ArticoloDeposito) this.getForm().getFormObject();
		articoloDeposito.setArticolo(articolo);
		return magazzinoAnagraficaBD.salvaArticoloDeposito(articoloDeposito);
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
	 * Imposta l'articolo corrente per la gestione del codiceArticoloEntita.
	 * 
	 * @param paramArticoloPM
	 *            l'articolo necessario per il salvataggio di codiceArticoloEntita
	 */
	public void setArticolo(Articolo paramArticoloPM) {
		articolo = paramArticoloPM;
	}

	/**
	 * Set magazzinoAnagraficaBD.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            il bd da utilizzare per accedere alle operazioni sui tipi mezzo trasporto
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
