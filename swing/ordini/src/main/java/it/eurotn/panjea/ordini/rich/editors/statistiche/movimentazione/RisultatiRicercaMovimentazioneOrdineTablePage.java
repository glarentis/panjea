package it.eurotn.panjea.ordini.rich.editors.statistiche.movimentazione;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.RigaMovimentazione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class RisultatiRicercaMovimentazioneOrdineTablePage extends AbstractTablePageEditor<RigaMovimentazione>
		implements InitializingBean {

	private class OpenAreaOrdineEditorCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "openAreaOrdineCommand";

		private IOrdiniDocumentoBD ordiniDocumentoBD = null;

		/**
		 * Costruisce il comando per aprire l'area ordine selezionata.
		 * 
		 * @param ordiniDocumentoBD
		 *            BD per caricare i dati dal service
		 */
		public OpenAreaOrdineEditorCommand(final IOrdiniDocumentoBD ordiniDocumentoBD) {
			super(COMMAND_ID);
			RcpSupport.configure(this);
			this.ordiniDocumentoBD = ordiniDocumentoBD;
		}

		@Override
		protected void doExecuteCommand() {
			RigaMovimentazione rigaMovimentazione = getTable().getSelectedObject();

			if (rigaMovimentazione != null) {
				AreaOrdine areaOrdine = new AreaOrdine();
				areaOrdine.setId(rigaMovimentazione.getAreaOrdineId());
				AreaOrdineFullDTO areaOrdineFullDTO = ordiniDocumentoBD.caricaAreaOrdineFullDTO(areaOrdine);
				LifecycleApplicationEvent event = new OpenEditorEvent(areaOrdineFullDTO);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}
	}

	public static final String PAGE_ID = "risultatiRicercaMovimentazioneOrdineTablePage";
	private ParametriRicercaMovimentazione parametriRicercaMovimentazione;
	private IOrdiniDocumentoBD ordiniDocumentoBD;
	private OpenAreaOrdineEditorCommand openAreaOrdineEditorCommand;
	private static final int SIZEPAGE = 5000;

	/**
	 * Costruttore di default.
	 */
	public RisultatiRicercaMovimentazioneOrdineTablePage() {
		super(PAGE_ID, new MovimentazioneOrdineBeanTableModel(PAGE_ID));
		getTable().setAggregatedColumns(new String[] { "dataRegistrazione" });
		getTable().getOverlayTable().setEnableCancelAction(true);

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		getTable().setPropertyCommandExecutor(getOpenAreaOrdineEditorCommand());
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getOpenAreaOrdineEditorCommand() };
	}

	/**
	 * @return Command per aprire l'area di magazzino selezionata
	 */
	private OpenAreaOrdineEditorCommand getOpenAreaOrdineEditorCommand() {
		if (openAreaOrdineEditorCommand == null) {
			openAreaOrdineEditorCommand = new OpenAreaOrdineEditorCommand(this.ordiniDocumentoBD);
		}
		return openAreaOrdineEditorCommand;
	}

	@Override
	public Collection<RigaMovimentazione> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void processTableData(Collection<RigaMovimentazione> results) {
		boolean fine = false;
		int page = 1;
		List<RigaMovimentazione> righeMovimentazioneResult = new ArrayList<RigaMovimentazione>();
		MovimentazioneOrdineBeanTableModel movimentazioneOrdineBeanTableModel = (MovimentazioneOrdineBeanTableModel) TableModelWrapperUtils
				.getActualTableModel(getTable().getTable().getModel());
		getTable().getOverlayTable().setCancel(false);
		if (parametriRicercaMovimentazione.isEffettuaRicerca()) {
			setRows(new ArrayList<RigaMovimentazione>());
			do {
				fine = Runtime.getRuntime().freeMemory() < 25000000;
				if (!fine) {
					List<RigaMovimentazione> righeMovimentazione = ordiniDocumentoBD.caricaMovimentazione(
							parametriRicercaMovimentazione, page, SIZEPAGE);

					if (!righeMovimentazione.isEmpty()) {
						if (righeMovimentazione.get(0).getNumeroDecimaliQuantita() != null) {
							movimentazioneOrdineBeanTableModel.setNumeroDecimaliQta(righeMovimentazione.get(0)
									.getNumeroDecimaliQuantita());
						} else {
							movimentazioneOrdineBeanTableModel.setNumeroDecimaliQta(6);
						}
						if (righeMovimentazione.get(0).getNumeroDecimaliPrezzo() != null) {
							movimentazioneOrdineBeanTableModel.setNumeroDecimaliPrezzo(righeMovimentazione.get(0)
									.getNumeroDecimaliPrezzo());
						} else {
							movimentazioneOrdineBeanTableModel.setNumeroDecimaliPrezzo(6);
						}
					}
					fine = righeMovimentazione.size() < SIZEPAGE;
					righeMovimentazioneResult.addAll(righeMovimentazione);
					page++;
				}
			} while (!getTable().getOverlayTable().isCancel() && !fine);
			if (!getTable().getOverlayTable().isCancel()) {
				if (!righeMovimentazioneResult.isEmpty()) {
					setRows(righeMovimentazioneResult);
				}
			}
		} else {
			movimentazioneOrdineBeanTableModel.setNumeroDecimaliPrezzo(0);
			movimentazioneOrdineBeanTableModel.setNumeroDecimaliQta(0);
			setRows(new ArrayList<RigaMovimentazione>());
		}
	}

	@Override
	public Collection<RigaMovimentazione> refreshTableData() {
		return Collections.emptyList();
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof ParametriRicercaMovimentazione) {
			this.parametriRicercaMovimentazione = (ParametriRicercaMovimentazione) object;
		} else {
			this.parametriRicercaMovimentazione = new ParametriRicercaMovimentazione();
		}
	}

	/**
	 * @param ordiniDocumentoBD
	 *            the ordiniDocumentoBD to set
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

}
