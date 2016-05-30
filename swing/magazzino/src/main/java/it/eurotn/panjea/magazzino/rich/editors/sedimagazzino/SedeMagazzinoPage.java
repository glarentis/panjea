/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.sedimagazzino;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.sedimagazzino.SedeMagazzinoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;

/**
 * Pagina che gestisce tutti i dati della sede magazzino.
 * 
 * @author fattazzo
 */
public class SedeMagazzinoPage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(SedeMagazzinoPage.class);

	private static final String PAGE_ID = "sedeMagazzinoPage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private SedeEntita sedeEntita;

	/**
	 * Costruttore di default.
	 */
	public SedeMagazzinoPage() {
		super(PAGE_ID, new SedeMagazzinoForm());
		setTitle(getMessage(PAGE_ID + ".title"));
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		SedeMagazzino sedeMagazzino = (SedeMagazzino) getBackingFormPage().getFormObject();
		sedeMagazzino = magazzinoAnagraficaBD.salvaSedeMagazzino(sedeMagazzino);
		logger.debug("--> Exit doSave");
		return sedeEntita;
	}

	@Override
	public void loadData() {
		logger.debug("--> Enter loadData");
		if ((sedeEntita != null) && (sedeEntita.getId() != null)) {
			SedeMagazzino sedeMagazzino = magazzinoAnagraficaBD.caricaSedeMagazzinoBySedeEntita(sedeEntita, false);
			super.setFormObject(sedeMagazzino);
		}
		logger.debug("--> Exit loadData");
	}

	@Override
	public void onPostPageOpen() {
		// nothing to do
	}

	@Override
	public boolean onPrePageOpen() {
		return (sedeEntita != null) && (sedeEntita.getId() != null);
	}

	@Override
	public void refreshData() {
		loadData();
	}

	/**
	 * override di setFormObject per aggiornare sedeEntita corrente per caricare la {@link SedeMagazzino} del Form
	 * corrente.
	 * 
	 * @param object
	 *            l'oggetto da settare al form
	 */
	@Override
	public void setFormObject(Object object) {
		logger.debug("--> Enter setFormObject");
		sedeEntita = (SedeEntita) object;
		loadData();
		logger.debug("--> Exit setFormObject");
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
