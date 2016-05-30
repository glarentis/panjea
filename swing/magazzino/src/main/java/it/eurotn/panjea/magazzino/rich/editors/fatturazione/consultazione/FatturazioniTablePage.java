/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ExclusiveCommandGroup;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author fattazzo
 *
 */
public class FatturazioniTablePage extends AbstractTablePageEditor<DatiGenerazione> {

	private class AnnoRiferimentoChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			setRows(loadFatturazioneDefinitiva());
		}
	}

	private class CancellaFatturazioneActionCommandInterceptor extends ActionCommandInterceptorAdapter {
		@Override
		public boolean preExecution(ActionCommand command) {
			return !getTable().getRows().isEmpty();
		}
	}

	private class CancellaFatturazioneCommand extends ActionCommand {

		public static final String COMMAND_ID = "cancellaFatturazioneCommand";

		/**
		 * Costruttore di default.
		 */
		public CancellaFatturazioneCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			if (getTable().getSelectedObject() == null) {
				return;
			}
			// eseguo il catch/finally per riabilitare il command dopo averlo disabilitato per evitare di premerlo più
			// volte ed evitare errori
			try {
				setEnabled(false);
				// Carico i documenti collegati alle fatture generate ed apro la
				// ricerca per non far ripetere tutte le ricerche fatte per creare il carrello
				List<MovimentoFatturazioneDTO> fatture = magazzinoDocumentoBD.caricaMovimentPerFatturazione(null,
						getTable().getSelectedObject().getUtente());
				List<AreaMagazzino> areeMagazzinoFatture = new ArrayList<AreaMagazzino>();
				for (MovimentoFatturazioneDTO movimentoFatturazioneDTO : fatture) {
					areeMagazzinoFatture.add(movimentoFatturazioneDTO.getAreaMagazzino());
				}

				ParametriRicercaFatturazione parametriRicercaFatturazione = new ParametriRicercaFatturazione();
				parametriRicercaFatturazione.setEffettuaRicerca(true);

				if (!areeMagazzinoFatture.isEmpty()) {
					List<AreaMagazzinoLite> areeMagazzinoOrigine = magazzinoDocumentoBD
							.caricaAreeMagazzinoCollegate(areeMagazzinoFatture);
					parametriRicercaFatturazione.setAreeMagazzino(areeMagazzinoOrigine);
				}
				magazzinoDocumentoBD.cancellaMovimentiInFatturazione(getTable().getSelectedObject().getUtente());
				LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaFatturazione);
				Application.instance().getApplicationContext().publishEvent(event);

				getLoadFatturazioneTemporaneaCommand().setSelected(true);
			} catch (Exception e) {
				throw new PanjeaRuntimeException(e);
			} finally {
				setEnabled(true);
			}
		}
	}

	private class LoadFatturazioneTemporaneaCommand extends JideToggleCommand {

		public static final String COMMAND_ID = "loadFatturazioneTemporaneaCommand";

		/**
		 * Costruttore.
		 *
		 * @param commandId
		 */
		public LoadFatturazioneTemporaneaCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void onSelection() {
			super.onSelection();

			setRows(loadFatturazioneTemporanea());
			updateControl(TipoFatturazione.TEMPORANEA);
		}

	}

	private class LoadFatturazioniDefinitiveCommand extends JideToggleCommand {

		public static final String COMMAND_ID = "loadFatturazioniDefinitiveCommand";

		/**
		 * Costruttore.
		 *
		 * @param commandId
		 */
		public LoadFatturazioniDefinitiveCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void onSelection() {
			super.onSelection();

			setRows(loadFatturazioneDefinitiva());
			updateControl(TipoFatturazione.DEFINITIVA);
		}
	}

	private enum TipoFatturazione {
		DEFINITIVA, TEMPORANEA
	}

	public static final String PAGE_ID = "fatturazioniTablePage";

	private TipoFatturazione tipoFatturazione;

	private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;
	private LoadFatturazioneTemporaneaCommand loadFatturazioneTemporaneaCommand = null;
	private LoadFatturazioniDefinitiveCommand loadFatturazioniDefinitiveCommand = null;
	private CancellaFatturazioneCommand cancellaFatturazioneCommand = null;

	private CancellaFatturazioneActionCommandInterceptor cancellaFatturazioneActionCommandInterceptor = null;
	private AnnoRiferimentoChangeListener annoRiferimentoChangeListener = null;

	private DatiGenerazione datiGenerazione;
	private final JSpinner annoRiferimento = new JSpinner(new SpinnerNumberModel(Calendar.getInstance().get(
			Calendar.YEAR), 0, 9999, 1));

	/**
	 * Costruttore.
	 */
	protected FatturazioniTablePage() {
		super(PAGE_ID, new String[] { "utente", "dataGenerazione", "dataCreazione", "note" }, DatiGenerazione.class);
		getTable().setDelayForSelection(500);
	}

	@Override
	public void dispose() {
		if (cancellaFatturazioneCommand != null) {
			cancellaFatturazioneCommand.removeCommandInterceptor(getCancellaFatturazioneActionCommandInterceptor());
		}
		if (annoRiferimento != null) {
			annoRiferimento.removeChangeListener(getAnnoRiferimentoChangeListener());
		}
		annoRiferimentoChangeListener = null;
		cancellaFatturazioneActionCommandInterceptor = null;
		cancellaFatturazioneCommand = null;

		super.dispose();
	}

	/**
	 * @return AnnoRiferimentoChangeListener
	 */
	private AnnoRiferimentoChangeListener getAnnoRiferimentoChangeListener() {
		if (annoRiferimentoChangeListener == null) {
			annoRiferimentoChangeListener = new AnnoRiferimentoChangeListener();
		}
		return annoRiferimentoChangeListener;
	}

	/**
	 * @return CancellaFatturazioneActionCommandInterceptor
	 */
	private CancellaFatturazioneActionCommandInterceptor getCancellaFatturazioneActionCommandInterceptor() {
		if (cancellaFatturazioneActionCommandInterceptor == null) {
			cancellaFatturazioneActionCommandInterceptor = new CancellaFatturazioneActionCommandInterceptor();
		}
		return cancellaFatturazioneActionCommandInterceptor;
	}

	/**
	 * @return the cancellaFatturazioneCommand
	 */
	public CancellaFatturazioneCommand getCancellaFatturazioneCommand() {
		if (cancellaFatturazioneCommand == null) {
			cancellaFatturazioneCommand = new CancellaFatturazioneCommand();
			cancellaFatturazioneCommand.addCommandInterceptor(getCancellaFatturazioneActionCommandInterceptor());
		}
		return cancellaFatturazioneCommand;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getRefreshCommand() };
	}

	@Override
	public JComponent getHeaderControl() {

		annoRiferimento.addChangeListener(getAnnoRiferimentoChangeListener());

		// creo il pannello che conterrà i button per la scelta di visualizzaizone
		JPanel buttonPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

		ExclusiveCommandGroup commandGroup = new ExclusiveCommandGroup();
		commandGroup.add(getLoadFatturazioniDefinitiveCommand());
		commandGroup.add(getLoadFatturazioneTemporaneaCommand());

		buttonPanel.add(getLoadFatturazioniDefinitiveCommand().createButton());
		buttonPanel.add(getLoadFatturazioneTemporaneaCommand().createButton());

		// creo il pannello che conterrà i controlli delle fatturazione (spinner dell'anno per quelle definitive,
		// cancella fatturazione per quella definitiva )
		JPanel controlsPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

		controlsPanel.add(annoRiferimento);
		controlsPanel.add(getCancellaFatturazioneCommand().createButton());

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.add(buttonPanel, BorderLayout.EAST);
		rootPanel.add(controlsPanel, BorderLayout.WEST);

		return rootPanel;
	}

	/**
	 * @return the loadFatturazioneTemporaneaCommand
	 */
	public LoadFatturazioneTemporaneaCommand getLoadFatturazioneTemporaneaCommand() {
		if (loadFatturazioneTemporaneaCommand == null) {
			loadFatturazioneTemporaneaCommand = new LoadFatturazioneTemporaneaCommand();
		}
		return loadFatturazioneTemporaneaCommand;
	}

	/**
	 * @return the loadFatturazioniDefinitiveCommand
	 */
	public LoadFatturazioniDefinitiveCommand getLoadFatturazioniDefinitiveCommand() {
		if (loadFatturazioniDefinitiveCommand == null) {
			loadFatturazioniDefinitiveCommand = new LoadFatturazioniDefinitiveCommand();
		}
		return loadFatturazioniDefinitiveCommand;
	}

	/**
	 * @return List<DatiGenerazione> fatturazione definitiva dell'anno di riferimento
	 */
	private List<DatiGenerazione> loadFatturazioneDefinitiva() {
		List<DatiGenerazione> fatturazioni = magazzinoDocumentoBD.caricaFatturazioni((Integer) annoRiferimento
				.getValue());
		return fatturazioni;
	}

	/**
	 * @return List<DatiGenerazione> fatturazione temporanea
	 */
	private List<DatiGenerazione> loadFatturazioneTemporanea() {
		List<DatiGenerazione> list = new ArrayList<DatiGenerazione>();
		List<DatiGenerazione> datiGenerazioneTmp = magazzinoDocumentoBD.caricaDatiGenerazioneFatturazioneTemporanea();
		if (datiGenerazioneTmp != null) {
			list.addAll(datiGenerazioneTmp);
		}
		return list;
	}

	@Override
	public Collection<DatiGenerazione> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
		switch (tipoFatturazione) {
		case TEMPORANEA:
			getLoadFatturazioneTemporaneaCommand().setSelected(true);
			break;
		default:
			getLoadFatturazioniDefinitiveCommand().setSelected(true);
			getTable().selectRowObject(datiGenerazione, null);
			break;
		}
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	protected void onRefresh() {
		switch (tipoFatturazione) {
		case TEMPORANEA:
			getLoadFatturazioneTemporaneaCommand().setSelected(true);
			break;
		default:
			getLoadFatturazioniDefinitiveCommand().setSelected(true);
			getTable().selectRowObject(datiGenerazione, null);
			break;
		}
	}

	@Override
	public Collection<DatiGenerazione> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		this.tipoFatturazione = TipoFatturazione.DEFINITIVA;
		this.datiGenerazione = null;
		this.annoRiferimento.setValue(Calendar.getInstance().get(Calendar.YEAR));

		// se arriva un dati generazione uso quello altrimenti vado a vedere se
		// esiste una fatturazione temporanea per visualizzarla
		if (object instanceof DatiGenerazione) {
			datiGenerazione = (DatiGenerazione) object;
		} else {
			List<DatiGenerazione> dati = magazzinoDocumentoBD.caricaDatiGenerazioneFatturazioneTemporanea();
			datiGenerazione = dati != null && !dati.isEmpty() ? dati.get(0) : null;
		}

		if (datiGenerazione != null) {
			Calendar dataGenerazioneCal = Calendar.getInstance();
			dataGenerazioneCal.setTime(datiGenerazione.getDataGenerazione());
			this.annoRiferimento.setValue(dataGenerazioneCal.get(Calendar.YEAR));

			if (datiGenerazione.getDataCreazione() == null) {
				this.tipoFatturazione = TipoFatturazione.TEMPORANEA;
			}
		}

	}

	/**
	 * @param magazzinoDocumentoBD
	 *            the magazzinoDocumentoBD to set
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		getRefreshCommand().setEnabled(!readOnly);

		getLoadFatturazioniDefinitiveCommand().setEnabled(!readOnly);
		getLoadFatturazioneTemporaneaCommand().setEnabled(!readOnly);
		getCancellaFatturazioneCommand().setEnabled(!readOnly);

		getTable().getTable().setEnabled(!readOnly);
	}

	/**
	 * Aggiorna lo stato dei controlli in base al tipo di fatturazione.
	 *
	 * @param paramTipoFatturazione
	 *            tipo di fatturazione
	 */
	private void updateControl(TipoFatturazione paramTipoFatturazione) {

		this.tipoFatturazione = paramTipoFatturazione;

		getCancellaFatturazioneCommand().setVisible(tipoFatturazione == TipoFatturazione.TEMPORANEA);

		annoRiferimento.setVisible(tipoFatturazione == TipoFatturazione.DEFINITIVA);
	}
}
