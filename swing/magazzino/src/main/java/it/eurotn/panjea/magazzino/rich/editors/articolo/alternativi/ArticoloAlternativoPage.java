/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.alternativi;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloAlternativo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author leonardo
 */
public class ArticoloAlternativoPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "articoloAlternativoPage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
	private Articolo articolo;

	/**
	 * Costruttore di default,inizializza un nuovo form.
	 */
	public ArticoloAlternativoPage() {
		super(PAGE_ID, new ArticoloAlternativoForm());
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD.cancellaArticoloAlternativo((ArticoloAlternativo) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		ArticoloAlternativo articoloAlternativo = (ArticoloAlternativo) this.getForm().getFormObject();
		articoloAlternativo.setArticolo(articolo.getArticoloLite());
		return magazzinoAnagraficaBD.salvaArticoloAlternativo(articoloAlternativo);
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
	 * Imposta l'articolo corrente per la gestione dell'articoloAlternativo.
	 * 
	 * @param paramArticolo
	 *            l'articolo necessario per il salvataggio dell'articoloAlternativo
	 */
	public void setArticolo(Articolo paramArticolo) {
		articolo = paramArticolo;
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
