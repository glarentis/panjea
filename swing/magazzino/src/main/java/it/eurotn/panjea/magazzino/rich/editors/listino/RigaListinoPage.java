/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.listino.exception.RigaListinoListiniCollegatiExceptionDialog;
import it.eurotn.panjea.magazzino.rich.forms.listino.RigaListinoForm;
import it.eurotn.panjea.magazzino.service.exception.RigaListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * Page per la gestione del Form di {@link RigaListino}.
 *
 * @author adriano
 * @version 1.0, 19/giu/08
 */
public class RigaListinoPage extends FormBackedDialogPageEditor {

	/**
	 * @author fattazzo
	 *
	 */
	private final class RigaListinoSwingWorker extends SwingWorker<BigDecimal, Void> {

		private RigaListino rigaListino;

		/**
		 * Costruttore.
		 *
		 * @param rigaListino
		 *            riga listino
		 */
		public RigaListinoSwingWorker(final RigaListino rigaListino) {
			this.rigaListino = rigaListino;
		}

		@Override
		protected BigDecimal doInBackground() throws Exception {

			Integer idArticolo = rigaListino.getArticolo() == null ? null : rigaListino.getArticolo().getId();

			BigDecimal result = null;
			if (getListinoCorrente() != null && getListinoCorrente().getListinoBase() != null
					&& getListinoCorrente().getListinoBase().getId() != null) {
				result = magazzinoAnagraficaBD.caricaImportoListino(getListinoCorrente().getListinoBase().getId(),
						idArticolo);
			}

			return result;
		}

		@Override
		protected void done() {
			try {
				if (!isCancelled()) {
					BigDecimal importo = get();

					// la scale è presa dal numero decimali prezzo della riga listino di base
					((RigaListinoForm) RigaListinoPage.super.getBackingFormPage()).getValueModel("importoBase")
							.setValue(importo);
					((RigaListinoForm) RigaListinoPage.super.getBackingFormPage()).getValueModel(
							"numeroDecimaliPrezzoBase").setValue(importo != null ? importo.scale() : null);

					if (((RigaListinoForm) RigaListinoPage.super.getBackingFormPage()).getFormModel().isCommittable()) {
						((RigaListinoForm) RigaListinoPage.super.getBackingFormPage()).getFormModel().commit();
					}
					((RigaListinoForm) RigaListinoPage.super.getBackingFormPage())
							.updateListinoBaseControlVisibility(importo != null);
				}
			} catch (InterruptedException e) {
				logger.warn("--> caricamento prezzo base listino interrotto. ", e);
			} catch (ExecutionException e) {
				logger.error("--> errore durante il caricamento del prezzo base del listino", e);
			}
		}
	}

	private static Logger logger = Logger.getLogger(RigaListinoPage.class);

	private static final String PAGE_ID = "rigaListinPage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private VersioneListino versioneListino;
	private Listino listino;

	private RigaListinoSwingWorker rigaListinoSwingWorker;

	// private RigaListino rigaListino;

	/**
	 * Costruttore di default.
	 */
	public RigaListinoPage() {
		super(PAGE_ID, new RigaListinoForm());
	}

	@Override
	protected Object doDelete() {
		RigaListino rigaListinoToDelete = (RigaListino) getBackingFormPage().getFormObject();
		magazzinoAnagraficaBD.cancellaRigaListino(rigaListinoToDelete);
		return rigaListinoToDelete;
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		RigaListino rigaListino = (RigaListino) getBackingFormPage().getFormObject();
		// Potrei modificare la riga listino sia dalla pagina dei listini che
		// dalla pagina degli articoli.
		// Se sono nella pagina articoli non setto la variabile versioneListino
		// perchè l'ho già caricata
		// la prendo dall'articolo.
		VersioneListino versioneListinoArticolo = null;
		if (versioneListino == null) {
			versioneListinoArticolo = rigaListino.getVersioneListino();
		} else {
			rigaListino.setVersioneListino(versioneListino);
		}
		try {
			rigaListino = magazzinoAnagraficaBD.salvaRigaListino(rigaListino);
		} catch (RigaListinoListiniCollegatiException e) {
			// chiedo all'utente se aggiornare il prezzo sui listini collegati
			RigaListinoListiniCollegatiExceptionDialog dialog = new RigaListinoListiniCollegatiExceptionDialog(e);
			dialog.showDialog();
			rigaListino = magazzinoAnagraficaBD.salvaRigaListino(rigaListino, dialog.isAggiornaListini());
			dialog = null;
		}

		if (versioneListinoArticolo != null) {
			rigaListino.setVersioneListino(versioneListinoArticolo);
		}

		logger.debug("--> Exit doSave");
		return rigaListino;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
				toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
		return commands;
	}

	/**
	 * @return the listino
	 */
	public Listino getListinoCorrente() {
		return listino;
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
	 *
	 * @param paramEnabledArticolo
	 *            false rende sempre non editable l'articolo;
	 */
	public void setArticoloEnabled(boolean paramEnabledArticolo) {
		((RigaListinoForm) getForm()).setArticoloEnabled(paramEnabledArticolo);
	}

	@Override
	public void setFormObject(Object object) {

		RigaListino rigaListino = null;
		if (object instanceof RigaListinoDTO) {
			RigaListinoDTO rigaListinoDTO = (RigaListinoDTO) object;
			rigaListino = magazzinoAnagraficaBD.caricaRigaListino(rigaListinoDTO.getId());
		} else {
			rigaListino = (RigaListino) object;
		}
		// questa riga mi imposta a readonly la page se cancello la riga listino
		// e la lista rimane vuota.
		// serve a qualcosa in particolare ???
		// setReadOnly(rigaListino.getId() == null);
		if (rigaListino.getVersioneListino() != null && versioneListino == null) {
			setListinoCorrente(rigaListino.getVersioneListino().getListino());
		}

		if (rigaListinoSwingWorker != null) {
			rigaListinoSwingWorker.cancel(true);
		}

		rigaListinoSwingWorker = new RigaListinoSwingWorker(rigaListino);

		super.setFormObject(rigaListino);

		rigaListinoSwingWorker.execute();

		// if (listino != null && listino.getListinoBase() != null && listino.getListinoBase().getId() != null) {
		//
		// final Integer idArticolo = rigaListino.getArticolo() == null ? null : rigaListino.getArticolo().getId();
		//
		// if (importoBaseSwingWorker != null) {
		// importoBaseSwingWorker.cancel(true);
		// }
		// importoBaseSwingWorker = new SwingWorker<BigDecimal, Void>() {
		//
		// @Override
		// protected BigDecimal doInBackground() throws Exception {
		// BigDecimal result = null;
		// if (listino != null && listino.getListinoBase() != null && listino.getListinoBase().getId() != null) {
		// result = magazzinoAnagraficaBD.caricaImportoListino(listino.getListinoBase().getId(),
		// idArticolo);
		// }
		//
		// return result;
		// };
		//
		// @Override
		// protected void done() {
		// if (!isCancelled()) {
		// try {
		// BigDecimal importo = get();
		// rigaListino.setImportoBase(importo);
		// // la scale è presa dal numero decimali prezzo della riga listino di
		// // base
		// rigaListino.setNumeroDecimaliPrezzoBase(importo != null ? importo.scale() : null);
		// RigaListinoPage.super.setFormObject(rigaListino);
		// } catch (InterruptedException e) {
		// logger.warn("--> caricamento prezzo base listino interrotto. ", e);
		// } catch (ExecutionException e) {
		// logger.error("--> errore durante il caricamento del prezzo base del listino", e);
		// }
		// }
		// };
		// };
		// } else {
		// super.setFormObject(rigaListino);
		// }
	}

	/**
	 * @param idListino
	 *            setter for id Listino
	 */
	public void setListinoCorrente(Integer idListino) {
		Listino listinoCorrente = new Listino();
		listinoCorrente.setId(idListino);
		listinoCorrente = magazzinoAnagraficaBD.caricaListino(listinoCorrente, false);
		setListinoCorrente(listinoCorrente);
	}

	/**
	 * @param paramListino
	 *            setter for Listino.
	 **/
	public void setListinoCorrente(Listino paramListino) {
		this.listino = paramListino;
		((RigaListinoForm) getForm()).setListino(paramListino);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	/**
	 * @param paramVersioneListino
	 *            setter for versioneListino.
	 **/
	public void setVersioneCorrente(VersioneListino paramVersioneListino) {
		this.versioneListino = paramVersioneListino;
		if (versioneListino != null && versioneListino.getListino() != null) {
			setListinoCorrente(versioneListino.getListino());
		}
	}
}
