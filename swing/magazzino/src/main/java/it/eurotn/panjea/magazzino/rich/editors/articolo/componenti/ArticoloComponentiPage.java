package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ArticoloComponentiPage extends DockingCompositeDialogPage implements IPageLifecycleAdvisor {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private DistintaConfigurazioniController controller;

	/**
	 * Costruttore.
	 */
	public ArticoloComponentiPage() {
		super("articoliComponentiPage");
	}

	@Override
	protected JComponent createControl() {
		controller = new DistintaConfigurazioniController(magazzinoAnagraficaBD);
		for (Object page : getPages()) {
			((IPageLifecycleAdvisor) page).setFormObject(controller.getConfigurazioneCorrenteValueHolder());
		}
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(controller.getControl(), BorderLayout.NORTH);
		mainPanel.add(super.createControl(), BorderLayout.CENTER);
		lockLayout(false);
		return mainPanel;
	}

	/**
	 * @return restituisce la configurazione corrente
	 */
	public Object getConfigurazioneCorrente() {
		if (controller != null) {
			return controller.getConfigurazioneCorrenteValueHolder().getValue();
		}
		return null;
	}

	/**
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
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
	public void postSetFormObject(Object object) {

	}

	@Override
	public void preSetFormObject(Object object) {

	}

	@Override
	public void refreshData() {

	}

	/**
	 * Imposta l'articolo/configurazione per mostrare la configurazione distinta scelta.
	 * 
	 * @param articolo
	 *            articolo
	 * @param idConfigurazione
	 *            idConfigurazione
	 */
	public void setArticoloConfigurazione(ArticoloLite articolo, Integer idConfigurazione) {
		if (controller != null) {
			controller.setArticoloConfigurazione(articolo, idConfigurazione);
		}
	}

	@Override
	public void setFormObject(Object object) {
		if (controller != null) {
			controller.setArticolo(((Articolo) object).getArticoloLite());
		}
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
	}

}
