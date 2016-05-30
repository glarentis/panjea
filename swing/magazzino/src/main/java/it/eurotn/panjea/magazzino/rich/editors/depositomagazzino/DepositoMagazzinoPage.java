package it.eurotn.panjea.magazzino.rich.editors.depositomagazzino;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.domain.DepositoMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.depositomagazzino.DepositoMagazzinoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;

public class DepositoMagazzinoPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "depositoMagazzinoPage";

	private static Logger logger = Logger.getLogger(DepositoMagazzinoPage.class);

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private Deposito deposito;

	/**
 * 
 */
	public DepositoMagazzinoPage() {
		super(PAGE_ID, new DepositoMagazzinoForm());
		setTitle(getMessage(PAGE_ID + ".title"));
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		DepositoMagazzino depositoMagazzino = (DepositoMagazzino) getBackingFormPage().getFormObject();
		depositoMagazzino = magazzinoAnagraficaBD.salvaDepositoMagazzino(depositoMagazzino);
		logger.debug("--> Exit doSave");
		return depositoMagazzino.getDeposito();

	}

	@Override
	public void loadData() {
		logger.debug("--> Enter loadData");
		if ((deposito != null) && (deposito.getId() != null)) {
			DepositoMagazzino depositoMagazzino = magazzinoAnagraficaBD.caricaDepositoMagazzinoByDeposito(deposito);
			super.setFormObject(depositoMagazzino);
		}
		logger.debug("--> Exit loadData");
	}

	@Override
	public void onPostPageOpen() {
		// nothing to do
	}

	@Override
	public boolean onPrePageOpen() {
		return (deposito != null) && (deposito.getId() != null);
	}

	@Override
	public void refreshData() {
		loadData();
	}

	/**
	 * override di setFormObject per aggiornare deposito corrente per caricare. il {@link DepositoMagazzino} del Form
	 * corrente
	 * 
	 * @param object
	 *            .
	 */

	@Override
	public void setFormObject(Object object) {
		logger.debug("--> Enter setFormObject");
		deposito = (Deposito) object;
		loadData();
		logger.debug("--> Exit setFormObject");
	}

	/**
	 * 
	 * @param magazzinoAnagraficaBD
	 *            tag.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
