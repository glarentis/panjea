/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.search;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.dialog.FormBackedDialogPage;

/**
 * 
 * @author Aracno
 * @version 1.0, 29/set/06
 */
public class RicercaBeniAmmortizzabiliDialog extends PanjeaTitledPageApplicationDialog {

	/**
	 * Classe per costruire la mappa di proprietà per eseguire la ricerca di una
	 * entità.
	 * 
	 * @author Leonardo
	 */
	public class BeneAmmortizzabileRicerca {

		private String codiceBene;
		private String descrizione;
		private boolean stato;
		private String annoAcquisto;
		private boolean beneDiProprieta;
		private boolean beneInLeasing;

		/**
		 * Costruttore.
		 * 
		 */
		public BeneAmmortizzabileRicerca() {
			setStato(false);
			setBeneDiProprieta(false);
			setBeneInLeasing(false);
		}

		/**
		 * @return the annoAcquisto
		 */
		public String getAnnoAcquisto() {
			return annoAcquisto;
		}

		/**
		 * @return the codiceBene
		 */
		public String getCodiceBene() {
			return codiceBene;
		}

		/**
		 * @return the descrizione
		 */
		public String getDescrizione() {
			return descrizione;
		}

		/**
		 * @return the beneDiProprieta
		 */
		public boolean isBeneDiProprieta() {
			return beneDiProprieta;
		}

		/**
		 * @return the beneInLeasing
		 */
		public boolean isBeneInLeasing() {
			return beneInLeasing;
		}

		/**
		 * @return the stato
		 */
		public boolean isStato() {
			return stato;
		}

		/**
		 * @param annoAcquisto
		 *            the annoAcquisto to set
		 */
		public void setAnnoAcquisto(String annoAcquisto) {
			this.annoAcquisto = annoAcquisto;
		}

		/**
		 * @param beneDiProprieta
		 *            the beneDiProprieta to set
		 */
		public void setBeneDiProprieta(boolean beneDiProprieta) {
			this.beneDiProprieta = beneDiProprieta;
		}

		/**
		 * @param beneInLeasing
		 *            the beneInLeasing to set
		 */
		public void setBeneInLeasing(boolean beneInLeasing) {
			this.beneInLeasing = beneInLeasing;
		}

		/**
		 * @param codiceBene
		 *            the codiceBene to set
		 */
		public void setCodiceBene(String codiceBene) {
			this.codiceBene = codiceBene;
		}

		/**
		 * @param descrizione
		 *            the descrizione to set
		 */
		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}

		/**
		 * @param stato
		 *            the stato to set
		 */
		public void setStato(boolean stato) {
			this.stato = stato;
		}

	}

	private static Logger logger = Logger.getLogger(RicercaBeniAmmortizzabiliDialog.class);

	private BeneAmmortizzabileRicercaForm beneAmmortizzabileRicercaForm;

	/**
	 * Costruttore.
	 * 
	 * @param id
	 *            id del dialog
	 */
	public RicercaBeniAmmortizzabiliDialog(final String id) {
		super();
		createDialog(id);
	}

	/**
	 * Crea il dialogo per l'entità scelta.
	 * 
	 * @param id
	 *            l'id definito nel commands-context.xml per identificare
	 *            l'entità
	 */
	protected void createDialog(String id) {
		BeneAmmortizzabileRicerca beneAmmortizzabileRicerca = new BeneAmmortizzabileRicerca();
		beneAmmortizzabileRicercaForm = new BeneAmmortizzabileRicercaForm(PanjeaFormModelHelper.createFormModel(
				beneAmmortizzabileRicerca, false, id + "Form"), id + "Form");
		FormBackedDialogPage formBackedDialogPage = new FormBackedDialogPage(id + "Dialog",
				beneAmmortizzabileRicercaForm);
		String ids = formBackedDialogPage.getId();
		logger.debug("--> id = " + ids);
		setDialogPage(formBackedDialogPage);
	}

	/**
	 * Aggiorna i valori di ricerca del bene.
	 * 
	 * @return parametri di ricerca
	 */
	private Map<String, Object> fillMapRicerca() {
		logger.debug("--> Enter fillMapRicerca");

		beneAmmortizzabileRicercaForm.commit();
		Map<String, Object> chiaviRicerca = new HashMap<String, Object>();

		BeneAmmortizzabileRicerca ricerca = (BeneAmmortizzabileRicerca) beneAmmortizzabileRicercaForm.getFormObject();

		if ((ricerca.getCodiceBene() != null) && (!ricerca.getCodiceBene().equals(""))) {
			chiaviRicerca.put(BeneAmmortizzabile.PROP_CODICE, ricerca.getCodiceBene());
		}

		if ((ricerca.getDescrizione() != null) && (!ricerca.getDescrizione().equals(""))) {
			chiaviRicerca.put(BeneAmmortizzabile.PROP_DESCRIZIONE, ricerca.getDescrizione());
		}

		if (ricerca.isStato()) {
			chiaviRicerca.put(BeneAmmortizzabile.PROP_ELIMINATO, ricerca.isStato());
		}

		if ((ricerca.getAnnoAcquisto() != null) && (!ricerca.getAnnoAcquisto().equals(""))) {
			chiaviRicerca.put(BeneAmmortizzabile.PROP_ANNO_ACQUISTO, ricerca.getAnnoAcquisto());
		}

		if (ricerca.isBeneDiProprieta()) {
			chiaviRicerca.put(BeneAmmortizzabile.PROP_BENE_DI_PROPRIETA, ricerca.isBeneDiProprieta());
		}

		if (ricerca.isBeneInLeasing()) {
			chiaviRicerca.put(BeneAmmortizzabile.PROP_BENE_IN_LEASING, ricerca.isBeneInLeasing());
		}

		logger.debug("--> Exit fillMapRicerca");
		return chiaviRicerca;
	}

	/**
	 * Apre la search result dell'entità scelta.
	 * 
	 * @return <code>true</code>
	 */
	@Override
	protected boolean onFinish() {
		ApplicationPage applicationPage = getActiveWindow().getPage();
		((PanjeaDockingApplicationPage) applicationPage).openResultView(
				"it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile", fillMapRicerca());
		return true;
	}
}
