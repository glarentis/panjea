/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.schedearticolo.elaborazioni;

import it.eurotn.panjea.magazzino.domain.SchedaArticolo.StatoScheda;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoSchedeArticoloBD;
import it.eurotn.panjea.magazzino.rich.editors.schedearticolo.StampaSchedeArticoloSelezionateCommand;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ElaborazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCreazioneSchedeArticoli;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaElaborazioni;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.grid.JideTable;
import com.jidesoft.swing.StyledLabel;
import com.jidesoft.swing.StyledLabelBuilder;

/**
 * @author fattazzo
 * 
 */
public class RisultatiRicercaElaborazioniTablePage extends AbstractTablePageEditor<ElaborazioneSchedaArticoloDTO> {

	private class CreaSchedeNonValideCommand extends ApplicationWindowAwareCommand {

		public static final String CREA_SCHEDE_NON_VALIDE_COMMAND = "creaSchedeNonValideCommand";

		/**
		 * Costruttore.
		 */
		public CreaSchedeNonValideCommand() {
			super(CREA_SCHEDE_NON_VALIDE_COMMAND);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			// uso un tree perchè l'articolo potrebbe essere selezionato più volte (elaborazioni diverse)
			Set<ArticoloRicerca> articoliDaCreare = new TreeSet<ArticoloRicerca>();

			List<ElaborazioneSchedaArticoloDTO> schedeSelezionati = getTable().getSelectedObjects();
			for (ElaborazioneSchedaArticoloDTO elaborazioneSchedaDTO : schedeSelezionati) {
				if (elaborazioneSchedaDTO.getStato() == StatoScheda.NON_VALIDO) {
					articoliDaCreare.add(elaborazioneSchedaDTO.getArticolo().createArticoloRicerca());
				}
			}

			if (articoliDaCreare.isEmpty()) {
				return;
			}

			ParametriCreazioneSchedeArticoli parametriCreazioneSchedeArticoli = new ParametriCreazioneSchedeArticoli();
			parametriCreazioneSchedeArticoli.setAnno(parametriRicercaElaborazioni.getAnno());
			parametriCreazioneSchedeArticoli.setMese(parametriRicercaElaborazioni.getMese());
			parametriCreazioneSchedeArticoli.setNote(null);
			parametriCreazioneSchedeArticoli.setArticoli(articoliDaCreare);

			magazzinoSchedeArticoloBD.creaSchedeArticolo(parametriCreazioneSchedeArticoli);

			RisultatiRicercaElaborazioniTablePage.this.refreshTableData();
		}

	}

	private class ModificaDescrizioneElaborazioneCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "modificaDescrizioneElaborazioneCommand";

		/**
		 * Costruttore.
		 */
		public ModificaDescrizioneElaborazioneCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			final ElaborazioneSchedaArticoloDTO elaborazione = getTable().getSelectedObject();

			if (elaborazione != null) {
				InputApplicationDialog dialog = new InputApplicationDialog("Modifica la descrizione", (Window) null);
				dialog.setInputLabelMessage("Nuova descrizione");
				dialog.setMessage(new DefaultMessage("Descrizione precedente: <b>" + elaborazione.getNota()
						+ "</b>. Inserire la nuova descrizione per l'elaborazione selezionata."));
				dialog.setFinishAction(new Closure() {

					@Override
					public Object call(Object argument) {

						String descrizioneNew = (String) argument;
						if (!descrizioneNew.isEmpty()) {
							magazzinoSchedeArticoloBD.modificaDescrizioneElaborazione(elaborazione.getNota(),
									descrizioneNew);
							RisultatiRicercaElaborazioniTablePage.this.refreshData();
						}

						return argument;
					}
				});
				dialog.showDialog();
			}
		}

	}

	private class RefreshSchedeArticoloCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "refreshCommand";

		/**
		 * Costruttore.
		 */
		public RefreshSchedeArticoloCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			RisultatiRicercaElaborazioniTablePage.this.refreshData();
		}
	}

	public static final String PAGE_ID = "risultatiRicercaElaborazioniTablePage";

	private ParametriRicercaElaborazioni parametriRicercaElaborazioni;

	private IMagazzinoSchedeArticoloBD magazzinoSchedeArticoloBD;

	private StyledLabel numeroSchedeArticoloInElaborazioneLabel;

	private StampaSchedeArticoloSelezionateCommand stampaSchedeArticoloSelezionateCommand;
	private RefreshSchedeArticoloCommand refreshSchedeArticoloCommand;
	private CreaSchedeNonValideCommand creaSchedeArticoloNonValideCommand;
	private ModificaDescrizioneElaborazioneCommand modificaDescrizioneElaborazioneCommand;

	{
		numeroSchedeArticoloInElaborazioneLabel = new StyledLabel();
	}

	/**
	 * Costruttore.
	 */
	protected RisultatiRicercaElaborazioniTablePage() {
		super(PAGE_ID, new RisultatiRicercaElaborazioniTableModel());
		((JideTable) getTable().getTable()).setRowAutoResizes(true);
	}

	/**
	 * Aggiorna i controlli che visualizzano il numero delle schede articolo che sono attualmente in elaborazione.
	 */
	private void aggiornaSchedeArticoloInElaborazioneControl() {

		int schede = magazzinoSchedeArticoloBD.caricaNumeroSchedeArticoloInCodaDiElaborazione();

		numeroSchedeArticoloInElaborazioneLabel.clearStyleRanges();

		String style = "{Nessuna scheda articolo in elaborazione:b}";
		if (schede > 0) {
			style = "{" + schede + " schede articolo ancora in elaborazione:b,f:red}";
		}

		StyledLabelBuilder.setStyledText(numeroSchedeArticoloInElaborazioneLabel, style);

		getRefreshSchedeArticoloCommand().setEnabled(schede > 0);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getModificaDescrizioneElaborazioneCommand(),
				getCreaSchedeArticoloNonValideCommand(), getStampaSchedeArticoloSelezionateCommand() };
	}

	/**
	 * @return the CreaSchedeNonValideCommand
	 */
	private CreaSchedeNonValideCommand getCreaSchedeArticoloNonValideCommand() {
		if (creaSchedeArticoloNonValideCommand == null) {
			creaSchedeArticoloNonValideCommand = new CreaSchedeNonValideCommand();
		}

		return creaSchedeArticoloNonValideCommand;
	}

	@Override
	public JComponent getHeaderControl() {
		JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		GuiStandardUtils.attachBorder(panel);
		panel.add(numeroSchedeArticoloInElaborazioneLabel);
		panel.add(getRefreshSchedeArticoloCommand().createButton());
		return panel;
	}

	/**
	 * @return the modificaDescrizioneElaborazioneCommand
	 */
	private ModificaDescrizioneElaborazioneCommand getModificaDescrizioneElaborazioneCommand() {
		if (modificaDescrizioneElaborazioneCommand == null) {
			modificaDescrizioneElaborazioneCommand = new ModificaDescrizioneElaborazioneCommand();
		}

		return modificaDescrizioneElaborazioneCommand;
	}

	/**
	 * @return the RefreshSchedeArticoloCommand
	 */
	private RefreshSchedeArticoloCommand getRefreshSchedeArticoloCommand() {
		if (refreshSchedeArticoloCommand == null) {
			refreshSchedeArticoloCommand = new RefreshSchedeArticoloCommand();
		}

		return refreshSchedeArticoloCommand;
	}

	/**
	 * @return the stampaSchedeArticoloSelezionateCommand
	 */
	private StampaSchedeArticoloSelezionateCommand getStampaSchedeArticoloSelezionateCommand() {
		if (stampaSchedeArticoloSelezionateCommand == null) {
			stampaSchedeArticoloSelezionateCommand = new StampaSchedeArticoloSelezionateCommand();
			stampaSchedeArticoloSelezionateCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
				@Override
				public boolean preExecution(ActionCommand command) {
					List<ElaborazioneSchedaArticoloDTO> elaborazioni = getTable().getSelectedObjects();
					List<ArticoloRicerca> articoli = new ArrayList<ArticoloRicerca>();
					Integer anno = null;
					Integer mese = null;

					for (ElaborazioneSchedaArticoloDTO elaborazioneSchedaDTO : elaborazioni) {
						articoli.add(elaborazioneSchedaDTO.getArticolo().createArticoloRicerca());
						anno = elaborazioneSchedaDTO.getAnno();
						mese = elaborazioneSchedaDTO.getMese();
					}

					command.addParameter(StampaSchedeArticoloSelezionateCommand.PARAM_ARTICOLI, articoli);
					command.addParameter(StampaSchedeArticoloSelezionateCommand.PARAM_ANNO, anno);
					command.addParameter(StampaSchedeArticoloSelezionateCommand.PARAM_MESE, mese);
					return true;
				}
			});
		}

		return stampaSchedeArticoloSelezionateCommand;
	}

	@Override
	public Collection<ElaborazioneSchedaArticoloDTO> loadTableData() {
		List<ElaborazioneSchedaArticoloDTO> elaborazioni = new ArrayList<ElaborazioneSchedaArticoloDTO>();

		if (parametriRicercaElaborazioni.isEffettuaRicerca()) {
			elaborazioni = magazzinoSchedeArticoloBD.caricaElaborazioniSchedeArticolo(parametriRicercaElaborazioni);
		}

		return elaborazioni;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public void processTableData(Collection<ElaborazioneSchedaArticoloDTO> results) {
		aggiornaSchedeArticoloInElaborazioneControl();
		super.processTableData(results);
	}

	@Override
	public Collection<ElaborazioneSchedaArticoloDTO> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		this.parametriRicercaElaborazioni = (ParametriRicercaElaborazioni) object;
	}

	/**
	 * @param magazzinoSchedeArticoloBD
	 *            the magazzinoSchedeArticoloBD to set
	 */
	public void setMagazzinoSchedeArticoloBD(IMagazzinoSchedeArticoloBD magazzinoSchedeArticoloBD) {
		this.magazzinoSchedeArticoloBD = magazzinoSchedeArticoloBD;
	}

}
