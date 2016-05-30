package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;

import java.util.Observable;
import java.util.Observer;

import org.springframework.richclient.dialog.DialogPage;

import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;

public class DatiGeograficiDockingCompositeDialogPage extends DockingCompositeDialogPage {

	private class LivelloAmministrativo1Observer implements Observer {

		private DatiGeograficiDockingCompositeDialogPage datiGeograficiDCDP = null;

		/**
		 * Costruttore.
		 * 
		 * @param dockingCompositeDialogPage
		 *            DatiGeograficiDockingCompositeDialogPage
		 */
		public LivelloAmministrativo1Observer(final DatiGeograficiDockingCompositeDialogPage dockingCompositeDialogPage) {
			super();
			datiGeograficiDCDP = dockingCompositeDialogPage;
		}

		@Override
		public void update(Observable o, Object obj) {
			DatiGeografici datiGeografici = livelloAmministrativo1TablePage.getDatiGeografici();
			datiGeografici.setLivelloAmministrativo1(null);
			datiGeografici.setLivelloAmministrativo2(null);
			datiGeografici.setLivelloAmministrativo3(null);
			datiGeografici.setLivelloAmministrativo4(null);
			datiGeografici.setLocalita(null);
			datiGeografici.setCap(null);
			if (obj != null && datiGeografici.getNazione() != null) {
				LivelloAmministrativo1 livelloAmministrativo1 = (LivelloAmministrativo1) obj;
				datiGeografici.setLivelloAmministrativo1(livelloAmministrativo1);
				datiGeograficiDCDP.updateFormObject(datiGeografici);

				if (datiGeografici.getNazione().hasLivelloAmministrativo2()) {
					livelloAmministrativo2TablePage.refreshData();
					localitaTablePage.refreshData();
					capTablePage.refreshData();
				} else {
					localitaTablePage.refreshData();
					capTablePage.refreshData();
				}
			} else {
				datiGeograficiDCDP.updateFormObject(datiGeografici);

				// se lo stato non ha livelli successivi devo eseguire il refresh anche delle altre pagine
				livelloAmministrativo2TablePage.refreshData();
				livelloAmministrativo3TablePage.refreshData();
				livelloAmministrativo4TablePage.refreshData();
				localitaTablePage.refreshData();
				capTablePage.refreshData();
			}
		}

	}

	private class LivelloAmministrativo2Observer implements Observer {

		private DatiGeograficiDockingCompositeDialogPage datiGeograficiDCDP = null;

		/**
		 * Costruttore.
		 * 
		 * @param dockingCompositeDialogPage
		 *            DatiGeograficiDockingCompositeDialogPage
		 */
		public LivelloAmministrativo2Observer(final DatiGeograficiDockingCompositeDialogPage dockingCompositeDialogPage) {
			super();
			datiGeograficiDCDP = dockingCompositeDialogPage;
		}

		@Override
		public void update(Observable o, Object obj) {
			DatiGeografici datiGeografici = livelloAmministrativo2TablePage.getDatiGeografici();
			datiGeografici.setLivelloAmministrativo2(null);
			datiGeografici.setLivelloAmministrativo3(null);
			datiGeografici.setLivelloAmministrativo4(null);
			datiGeografici.setLocalita(null);
			datiGeografici.setCap(null);
			if (obj != null) {
				LivelloAmministrativo2 livelloAmministrativo2 = (LivelloAmministrativo2) obj;
				datiGeografici.setLivelloAmministrativo2(livelloAmministrativo2);
				datiGeograficiDCDP.updateFormObject(datiGeografici);

				if (datiGeografici.getNazione().hasLivelloAmministrativo3()) {
					livelloAmministrativo3TablePage.refreshData();
					localitaTablePage.refreshData();
					capTablePage.refreshData();
				} else {
					localitaTablePage.refreshData();
					capTablePage.refreshData();
				}
			} else {
				datiGeograficiDCDP.updateFormObject(datiGeografici);

				livelloAmministrativo3TablePage.refreshData();
				livelloAmministrativo4TablePage.refreshData();
				localitaTablePage.refreshData();
				capTablePage.refreshData();
			}

		}

	}

	private class LivelloAmministrativo3Observer implements Observer {

		private DatiGeograficiDockingCompositeDialogPage datiGeograficiDCDP = null;

		/**
		 * Costruttore.
		 * 
		 * @param dockingCompositeDialogPage
		 *            DatiGeograficiDockingCompositeDialogPage
		 */
		public LivelloAmministrativo3Observer(final DatiGeograficiDockingCompositeDialogPage dockingCompositeDialogPage) {
			super();
			datiGeograficiDCDP = dockingCompositeDialogPage;
		}

		@Override
		public void update(Observable o, Object obj) {
			DatiGeografici datiGeografici = livelloAmministrativo3TablePage.getDatiGeografici();
			datiGeografici.setLivelloAmministrativo3(null);
			datiGeografici.setLivelloAmministrativo4(null);
			datiGeografici.setLocalita(null);
			datiGeografici.setCap(null);
			if (obj != null) {
				LivelloAmministrativo3 livelloAmministrativo3 = (LivelloAmministrativo3) obj;
				datiGeografici.setLivelloAmministrativo3(livelloAmministrativo3);
				datiGeograficiDCDP.updateFormObject(datiGeografici);

				if (datiGeografici.getNazione().hasLivelloAmministrativo4()) {
					livelloAmministrativo4TablePage.refreshData();
					localitaTablePage.refreshData();
					capTablePage.refreshData();
				} else {
					localitaTablePage.refreshData();
					capTablePage.refreshData();
				}
			} else {
				datiGeograficiDCDP.updateFormObject(datiGeografici);

				livelloAmministrativo4TablePage.refreshData();
				localitaTablePage.refreshData();
				capTablePage.refreshData();
			}
		}

	}

	private class LivelloAmministrativo4Observer implements Observer {

		private DatiGeograficiDockingCompositeDialogPage datiGeograficiDCDP = null;

		/**
		 * Costruttore.
		 * 
		 * @param dockingCompositeDialogPage
		 *            DatiGeograficiDockingCompositeDialogPage
		 */
		public LivelloAmministrativo4Observer(final DatiGeograficiDockingCompositeDialogPage dockingCompositeDialogPage) {
			super();
			datiGeograficiDCDP = dockingCompositeDialogPage;
		}

		@Override
		public void update(Observable o, Object obj) {
			DatiGeografici datiGeografici = livelloAmministrativo4TablePage.getDatiGeografici();
			datiGeografici.setLivelloAmministrativo4(null);
			if (obj != null) {
				LivelloAmministrativo4 livelloAmministrativo4 = (LivelloAmministrativo4) obj;
				datiGeografici.setLivelloAmministrativo4(livelloAmministrativo4);
			}
			datiGeograficiDCDP.updateFormObject(datiGeografici);
			localitaTablePage.refreshData();
			capTablePage.refreshData();
		}

	}

	private class NazioneObserver implements Observer {

		private DatiGeograficiDockingCompositeDialogPage datiGeograficiDCDP = null;

		/**
		 * Costruttore.
		 * 
		 * @param dockingCompositeDialogPage
		 *            DatiGeograficiDockingCompositeDialogPage
		 */
		public NazioneObserver(final DatiGeograficiDockingCompositeDialogPage dockingCompositeDialogPage) {
			super();
			datiGeograficiDCDP = dockingCompositeDialogPage;
		}

		@Override
		public void update(Observable o, Object obj) {
			if (obj != null) {
				Nazione nazione = ((NazioneUI) obj).getNazione();
				boolean lvl1presente = nazione.hasLivelloAmministrativo1();
				boolean lvl2presente = nazione.hasLivelloAmministrativo2();
				boolean lvl3presente = nazione.hasLivelloAmministrativo3();
				boolean lvl4presente = nazione.hasLivelloAmministrativo4();

				String lvl1 = nazione.getLivelloAmministrativo1();
				String lvl2 = nazione.getLivelloAmministrativo2();
				String lvl3 = nazione.getLivelloAmministrativo3();
				String lvl4 = nazione.getLivelloAmministrativo4();

				// aggiorna il nome del docked con il nome del livello amministrativo della nazione
				datiGeograficiDCDP.setFrameTitle(LivelloAmministrativo1TablePage.PAGE_ID, lvl1);
				datiGeograficiDCDP.setFrameTitle(LivelloAmministrativo2TablePage.PAGE_ID, lvl2);
				datiGeograficiDCDP.setFrameTitle(LivelloAmministrativo3TablePage.PAGE_ID, lvl3);
				datiGeograficiDCDP.setFrameTitle(LivelloAmministrativo4TablePage.PAGE_ID, lvl4);

				// nasconde i livelli amministrativi che non sono impostati per la nazione
				// per mantenere l'odine del docked devo nascondere dal 4 all'1 e visualizzare dall'1 al 4
				if (!lvl4presente) {
					datiGeograficiDCDP.hideFrame(LivelloAmministrativo4TablePage.PAGE_ID);
				}
				if (!lvl3presente) {
					datiGeograficiDCDP.hideFrame(LivelloAmministrativo3TablePage.PAGE_ID);
				}
				if (!lvl2presente) {
					datiGeograficiDCDP.hideFrame(LivelloAmministrativo2TablePage.PAGE_ID);
				}
				if (!lvl1presente) {
					datiGeograficiDCDP.hideFrame(LivelloAmministrativo1TablePage.PAGE_ID);
				}

				if (lvl1presente) {
					datiGeograficiDCDP.showFrame(LivelloAmministrativo1TablePage.PAGE_ID);
				}
				if (lvl2presente) {
					datiGeograficiDCDP.showFrame(LivelloAmministrativo2TablePage.PAGE_ID);
				}
				if (lvl3presente) {
					datiGeograficiDCDP.showFrame(LivelloAmministrativo3TablePage.PAGE_ID);
				}
				if (lvl4presente) {
					datiGeograficiDCDP.showFrame(LivelloAmministrativo4TablePage.PAGE_ID);
				}

				DatiGeografici datiGeografici = new DatiGeografici();
				datiGeografici.setNazione(nazione);
				datiGeograficiDCDP.updateFormObject(datiGeografici);

				if (nazione.hasLivelloAmministrativo1()) {
					livelloAmministrativo1TablePage.refreshData();
					localitaTablePage.refreshData();
					capTablePage.refreshData();
				} else {
					localitaTablePage.refreshData();
					capTablePage.refreshData();
				}
			}
		}
	}

	private static final String DOCKING_COMPOSITE_DIALOG_PAGE_ID = "datiGeograficiDockingCompositeDialogPage";

	private NazioniTablePage nazioniTablePage;
	private LivelloAmministrativo1TablePage livelloAmministrativo1TablePage;
	private LivelloAmministrativo2TablePage livelloAmministrativo2TablePage;
	private LivelloAmministrativo3TablePage livelloAmministrativo3TablePage;
	private LivelloAmministrativo4TablePage livelloAmministrativo4TablePage;
	private LocalitaTablePage localitaTablePage;
	private CapTablePage capTablePage;

	/**
	 * Costruttore.
	 */
	public DatiGeograficiDockingCompositeDialogPage() {
		super(DOCKING_COMPOSITE_DIALOG_PAGE_ID);
	}

	@Override
	public void addPage(DialogPage page) {
		super.addPage(page);
		if (NazioniTablePage.PAGE_ID.equals(page.getId())) {
			nazioniTablePage = (NazioniTablePage) page;
			nazioniTablePage.getTable().addSelectionObserver(new NazioneObserver(this));
		}
		if (LivelloAmministrativo1TablePage.PAGE_ID.equals(page.getId())) {
			livelloAmministrativo1TablePage = (LivelloAmministrativo1TablePage) page;
			livelloAmministrativo1TablePage.getTable().addSelectionObserver(new LivelloAmministrativo1Observer(this));
		}
		if (LivelloAmministrativo2TablePage.PAGE_ID.equals(page.getId())) {
			livelloAmministrativo2TablePage = (LivelloAmministrativo2TablePage) page;
			livelloAmministrativo2TablePage.getTable().addSelectionObserver(new LivelloAmministrativo2Observer(this));
		}
		if (LivelloAmministrativo3TablePage.PAGE_ID.equals(page.getId())) {
			livelloAmministrativo3TablePage = (LivelloAmministrativo3TablePage) page;
			livelloAmministrativo3TablePage.getTable().addSelectionObserver(new LivelloAmministrativo3Observer(this));
		}
		if (LivelloAmministrativo4TablePage.PAGE_ID.equals(page.getId())) {
			livelloAmministrativo4TablePage = (LivelloAmministrativo4TablePage) page;
			livelloAmministrativo4TablePage.getTable().addSelectionObserver(new LivelloAmministrativo4Observer(this));
		}
		if (LocalitaTablePage.PAGE_ID.equals(page.getId())) {
			localitaTablePage = (LocalitaTablePage) page;
		}
		if (CapTablePage.PAGE_ID.equals(page.getId())) {
			capTablePage = (CapTablePage) page;
		}
	}

	@Override
	protected void configureFrame(DockableFrame frame) {
		super.configureFrame(frame);
		getDockingManager().setFloatable(false);
		getDockingManager().setAutohidable(false);
		getDockingManager().setHidable(false);
		getDockingManager().setRearrangable(false);
		frame.getContext().setInitSide(DockContext.STATE_FRAMEDOCKED);
		frame.getContext().setInitSide(DockContext.DOCK_SIDE_NORTH);
	}

	/**
	 * Imposta al frame specificato il titolo scelto.
	 * 
	 * @param frameid
	 *            l'id del dockable frame di cui impostare il titolo
	 * @param title
	 *            il titolo da impostare al dockableFrame
	 */
	public void setFrameTitle(String frameid, String title) {
		if (title == null) {
			title = "";
		}
		DockableFrame lvl1frame = getFrame(frameid);
		lvl1frame.setTitle(title);
	}

	/**
	 * Aggiorna il form object delle pagine presenti con il nuovo DatiGeografici object.
	 * 
	 * @param datiGeografici
	 *            i dati geografici da impostare alle pagine del docking da usare come filtri
	 */
	public void updateFormObject(DatiGeografici datiGeografici) {
		nazioniTablePage.setFormObject(datiGeografici);
		livelloAmministrativo1TablePage.setFormObject(datiGeografici);
		livelloAmministrativo2TablePage.setFormObject(datiGeografici);
		livelloAmministrativo3TablePage.setFormObject(datiGeografici);
		livelloAmministrativo4TablePage.setFormObject(datiGeografici);
		localitaTablePage.setFormObject(datiGeografici);
		capTablePage.setFormObject(datiGeografici);
	}

}