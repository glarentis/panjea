/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.RicercaAvanzataArticoliCommand;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;
import it.eurotn.rich.report.StampaCommand;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JRParameter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.toedter.calendar.JDateChooser;

/**
 * @author fattazzo
 *
 */
public class VersioniListinoTablePage extends AbstractTablePageEditor<VersioneListino> {

	public class CopiaVersioneListinoCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public CopiaVersioneListinoCommand() {
			super("CopiaVersioneListinoCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			final VersioneListino versioneListino = getTable().getSelectedObject();
			if (versioneListino == null) {
				return;
			}

			InputApplicationDialog dialog = new InputApplicationDialog("Data nuova versione", ((Window) null));
			JDateChooser dateChooser = new JDateChooser("dd/MM/yy", "00/00/00", '_');
			dateChooser.setDate(Calendar.getInstance().getTime());
			dialog.setInputField(dateChooser);
			dialog.setInputLabelMessage("Data");
			dialog.setFinishAction(new Closure() {

				@Override
				public Object call(Object paramObject) {
					Date dataVersione = (Date) paramObject;
					if (dataVersione != null) {
						magazzinoAnagraficaBD.copiaVersioneListino(versioneListino, dataVersione);

						listino = magazzinoAnagraficaBD.caricaListino(listino, true);

						refreshData();
					}
					return null;
				}
			});
			dialog.showDialog();
		}

	}

	public class StampaListinoCommand extends StampaCommand {
		private static final String CONTROLLER_ID = "printCommandListino";
		private final String codiceLingua;

		/**
		 * Costruttore.
		 *
		 * @param codiceLingua
		 *            codice della lingua da stampare
		 */
		public StampaListinoCommand(final String codiceLingua) {
			super(CONTROLLER_ID);
			this.codiceLingua = codiceLingua;
			this.setIcon(RcpSupport.getIcon(codiceLingua));
			this.setLabel(codiceLingua);
		}

		@Override
		protected Map<Object, Object> getParametri() {
			HashMap<Object, Object> parametri = new HashMap<Object, Object>();
			VersioneListino versione = VersioniListinoTablePage.this.getTable().getSelectedObject();
			parametri.put("descAzienda", aziendaCorrente.getDenominazione());
			parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
			parametri.put("id", versione.getId());
			parametri.put("articoli", StringUtils.join(articoli.iterator(), ","));
			parametri.put(JRParameter.REPORT_LOCALE, codiceLingua);
			return parametri;
		}

		@Override
		protected String getReportName() {
			VersioneListino versione = VersioniListinoTablePage.this.getTable().getSelectedObject();
			return "LISTINO " + versione.getListino().getCodice();
		}

		@Override
		protected String getReportPath() {
			return "Magazzino/Anagrafica/listino";
		}
	}

	private class SvuotaRicercaArticoliCommand extends ActionCommand {
		public SvuotaRicercaArticoliCommand() {
			super("svuotaRicercaArticoliCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			labelArticoli.setText("Tutti");
			articoli = new HashSet<>();
		}
	}

	private RicercaAvanzataArticoliCommand ricercaAvanzataArticoliCommand;

	public static final String PAGE_ID = "versioniListinoTablePage";

	private Listino listino = null;

	private AziendaCorrente aziendaCorrente = null;
	private Set<Integer> articoli = new HashSet<>();

	private IAnagraficaTabelleBD anagraficaTabelleBD;
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private CommandGroup stampaCommand;
	private CopiaVersioneListinoCommand copiaVersioneListinoCommand;

	private JLabel labelArticoli;

	/**
	 * Costruttore.
	 */
	public VersioniListinoTablePage() {
		super(PAGE_ID, new VersioniListinoTableModel());
		setShowTitlePane(false);
		getTable().addSelectionObserver(this);
	}

	@Override
	public JComponent createToolbar() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		JLabel label = getComponentFactory().createLabel(PAGE_ID + ".legendaTabella.label");
		label.setIcon(getIconSource().getIcon("apply"));
		panel.add(label, BorderLayout.WEST);
		JPanel commandsPanel = new JPanel(new FlowLayout());
		commandsPanel.setBorder(null);
		labelArticoli = new JLabel("Tutti");
		commandsPanel.add(labelArticoli);
		commandsPanel.add(getRicercaAvanzataArticoliCommand().createButton());
		commandsPanel.add(new SvuotaRicercaArticoliCommand().createButton());
		panel.add(commandsPanel, BorderLayout.CENTER);

		panel.add(super.createToolbar(), BorderLayout.EAST);
		return panel;
	}

	/**
	 * @return Returns the anagraficaTabelleBD.
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getEditorNewCommand(), getEditorLockCommand(), getEditorDeleteCommand(),
				getPrintCommand(), getCopiaVersioneListinoCommand() };
	}

	/**
	 * @return the copiaVersioneListinoCommand
	 */
	private CopiaVersioneListinoCommand getCopiaVersioneListinoCommand() {
		if (copiaVersioneListinoCommand == null) {
			copiaVersioneListinoCommand = new CopiaVersioneListinoCommand();
		}

		return copiaVersioneListinoCommand;
	}

	/**
	 * @return the magazzinoAnagraficaBD
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	@Override
	public Object getManagedObject(Object pageObject) {
		this.listino = ((VersioneListino) pageObject).getListino();
		return listino;
	}

	/**
	 *
	 * @return command per la stampa della versione del listino.
	 */
	public AbstractCommand getPrintCommand() {
		if (stampaCommand == null) {
			List<Lingua> lingueAziendali = anagraficaTabelleBD.caricaLingue();
			StampaCommand[] stampaCommands = new StampaCommand[lingueAziendali.size() + 1];

			stampaCommands[0] = new StampaListinoCommand(aziendaCorrente.getLingua());
			int index = 1;
			for (Lingua lingua : lingueAziendali) {
				stampaCommands[index++] = new StampaListinoCommand(lingua.getCodice());
			}
			stampaCommand = CommandGroup.createCommandGroup("printCommandListino", stampaCommands);
			stampaCommand.setIcon(RcpSupport.getIcon("stampa"));
			stampaCommand.setLabel(RcpSupport.getMessage("stampa"));
		}
		return stampaCommand;
	}

	/**
	 * @return the ricercaAvanzataArticoliCommand
	 */
	private RicercaAvanzataArticoliCommand getRicercaAvanzataArticoliCommand() {
		if (ricercaAvanzataArticoliCommand == null) {
			ricercaAvanzataArticoliCommand = new RicercaAvanzataArticoliCommand(
					"aggiungiRicercaAvanzataArticoliCommand");
			ricercaAvanzataArticoliCommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand command) {
					List<ArticoloRicerca> articoliRicerca = ((RicercaAvanzataArticoliCommand) command)
							.getArticoliSelezionati();
					if (articoliRicerca != null && articoliRicerca.size() > 0) {
						for (ArticoloRicerca articoloRicerca : articoliRicerca) {
							articoli.add(articoloRicerca.getId());
						}
						labelArticoli.setText(new Integer(articoli.size()).toString());
					}

				}

				@Override
				public boolean preExecution(ActionCommand command) {
					return true;
				}
			});
		}

		return ricercaAvanzataArticoliCommand;
	}

	@Override
	public Collection<VersioneListino> loadTableData() {
		return Collections.emptyList();
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
		this.listino = (Listino) object;
		((VersioneListinoPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setListino(listino);
		super.postSetFormObject(object);
	}

	@Override
	public void processTableData(Collection<VersioneListino> results) {

		setRows(new ArrayList<VersioneListino>());

		if (this.listino.isNew()) {
			setReadOnly(true);
		} else {
			setReadOnly(false);
			if (this.listino.getVersioniListino() != null) {
				setRows(this.listino.getVersioniListino());
			}

			VersioneListino versioneListinoInVigore = magazzinoAnagraficaBD.caricaVersioneListinoByData(listino,
					new Date());

			((VersioniListinoTableModel) TableModelWrapperUtils.getActualTableModel(getTable().getTable().getModel()))
			.setVersioneListinoInVigore(versioneListinoInVigore);

			getTable().selectRowObject(versioneListinoInVigore, null);
		}
	}

	@Override
	public Collection<VersioneListino> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void restoreState(Settings settings) {
		super.restoreState(settings);

		getTable().selectRowObject(
				((VersioniListinoTableModel) TableModelWrapperUtils.getActualTableModel(getTable().getTable()
						.getModel())).getVersioneListinoInVigore(), null);
	}

	/**
	 * @param anagraficaTabelleBD
	 *            The anagraficaTabelleBD to set.
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	@Override
	public void update(Observable o, Object obj) {
		super.update(o, obj);

	}
}
