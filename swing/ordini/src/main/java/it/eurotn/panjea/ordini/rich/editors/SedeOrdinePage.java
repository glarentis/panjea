/**
 * 
 */
package it.eurotn.panjea.ordini.rich.editors;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.ordini.domain.SedeOrdine;
import it.eurotn.panjea.ordini.rich.bd.IAnagraficaOrdiniBD;
import it.eurotn.panjea.ordini.rich.forms.SedeOrdineForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;

/**
 * Page per {@link SedeOrdine}.
 * 
 * @author fattazzo
 * @version 1.0, 19/dic/2008
 */
public class SedeOrdinePage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(SedeOrdinePage.class);

	private static final String PAGE_ID = "sedeOrdinePage";

	private IAnagraficaOrdiniBD anagraficaOrdiniBD;

	private SedeEntita sedeEntita;

	/**
	 * Costruttore di default.
	 */
	public SedeOrdinePage() {
		super(PAGE_ID, new SedeOrdineForm());
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		SedeOrdine sedeOrdine = (SedeOrdine) getBackingFormPage().getFormObject();
		sedeOrdine = anagraficaOrdiniBD.salvaSedeOrdine(sedeOrdine);
		return sedeEntita;
	}

	@Override
	public void loadData() {
		logger.debug("--> Enter loadData");
		if ((sedeEntita != null) && (sedeEntita.getId() != null)) {
			SedeOrdine sedeOrdine = anagraficaOrdiniBD.caricaSedeOrdineBySedeEntita(sedeEntita, false);
			sedeOrdine.setSedeEntita(sedeEntita);
			super.setFormObject(sedeOrdine);
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
	 * @param anagraficaOrdiniBD
	 *            the anagraficaOrdiniBD to set
	 */
	public void setAnagraficaOrdiniBD(IAnagraficaOrdiniBD anagraficaOrdiniBD) {
		this.anagraficaOrdiniBD = anagraficaOrdiniBD;
	}

	@Override
	public void setFormObject(Object object) {
		logger.debug("--> Enter setFormObject");
		sedeEntita = (SedeEntita) object;
		loadData();
		logger.debug("--> Exit setFormObject");
	}

}
