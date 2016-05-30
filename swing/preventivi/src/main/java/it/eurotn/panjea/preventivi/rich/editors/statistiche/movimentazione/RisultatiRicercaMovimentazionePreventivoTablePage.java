package it.eurotn.panjea.preventivi.rich.editors.statistiche.movimentazione;

import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.preventivi.util.RigaMovimentazione;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaMovimentazione;
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

public class RisultatiRicercaMovimentazionePreventivoTablePage extends AbstractTablePageEditor<RigaMovimentazione>
		implements InitializingBean {

	private class OpenAreaPreventivoEditorCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "openAreaPreventivoCommand";

		/**
		 * Costruisce il comando per aprire l'area preventivo selezionata.
		 * 
		 */
		public OpenAreaPreventivoEditorCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			RigaMovimentazione rigaMovimentazione = getTable().getSelectedObject();

			if (rigaMovimentazione != null) {
				AreaPreventivo areaPreventivo = new AreaPreventivo();
				areaPreventivo.setId(rigaMovimentazione.getAreaPreventivoId());
				AreaPreventivoFullDTO areaPreventivoFullDTO = preventiviBD.caricaAreaFullDTO(areaPreventivo);
				LifecycleApplicationEvent event = new OpenEditorEvent(areaPreventivoFullDTO);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}
	}

	public static final String PAGE_ID = "risultatiRicercaMovimentazionePreventivoTablePage";
	private ParametriRicercaMovimentazione parametriRicercaMovimentazione;
	private IPreventiviBD preventiviBD;
	private OpenAreaPreventivoEditorCommand openAreaPreventivoEditorCommand;
	private static final int SIZEPAGE = 5000;

	/**
	 * Costruttore di default.
	 */
	public RisultatiRicercaMovimentazionePreventivoTablePage() {
		super(PAGE_ID, new MovimentazionePreventivoBeanTableModel(PAGE_ID));
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
	private OpenAreaPreventivoEditorCommand getOpenAreaOrdineEditorCommand() {
		if (openAreaPreventivoEditorCommand == null) {
			openAreaPreventivoEditorCommand = new OpenAreaPreventivoEditorCommand();
		}
		return openAreaPreventivoEditorCommand;
	}

	/**
	 * @return the preventiviBD
	 */
	public IPreventiviBD getPreventiviBD() {
		return preventiviBD;
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
		int maxDecQta = 0;
		int maxDecPrezzo = 0;
		List<RigaMovimentazione> righeMovimentazioneResult = new ArrayList<RigaMovimentazione>();
		MovimentazionePreventivoBeanTableModel movimentazionePreventivoBeanTableModel = (MovimentazionePreventivoBeanTableModel) TableModelWrapperUtils
				.getActualTableModel(getTable().getTable().getModel());
		getTable().getOverlayTable().setCancel(false);
		if (parametriRicercaMovimentazione.isEffettuaRicerca()) {
			setRows(new ArrayList<RigaMovimentazione>());
			do {
				fine = Runtime.getRuntime().freeMemory() < 25000000;
				if (!fine) {
					List<RigaMovimentazione> righeMovimentazione = preventiviBD.caricaMovimentazione(
							parametriRicercaMovimentazione, page, SIZEPAGE);

					for (RigaMovimentazione rigaMovimentazione : righeMovimentazione) {
						if (rigaMovimentazione.getNumeroDecimaliPrezzo() > maxDecPrezzo) {
							maxDecPrezzo = rigaMovimentazione.getNumeroDecimaliPrezzo();
							movimentazionePreventivoBeanTableModel.setNumeroDecimaliPrezzo(maxDecPrezzo);
						}
						if (rigaMovimentazione.getNumeroDecimaliQuantita() > maxDecQta) {
							maxDecQta = rigaMovimentazione.getNumeroDecimaliQuantita();
							movimentazionePreventivoBeanTableModel.setNumeroDecimaliQta(maxDecQta);
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
			movimentazionePreventivoBeanTableModel.setNumeroDecimaliPrezzo(0);
			movimentazionePreventivoBeanTableModel.setNumeroDecimaliQta(0);
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
	 * @param preventiviBD
	 *            the preventiviBD to set
	 */
	public void setPreventiviBD(IPreventiviBD preventiviBD) {
		this.preventiviBD = preventiviBD;
	}
}
