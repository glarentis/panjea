package it.eurotn.panjea.bi.rich.editors.analisi;

import it.eurotn.panjea.bi.rich.editors.analisi.commands.AutoResizeAnalisiCommand;
import it.eurotn.panjea.bi.rich.editors.analisi.commands.CaricaDataSourceCommand;
import it.eurotn.panjea.bi.rich.editors.analisi.commands.EsportaAnalisiCommand;
import it.eurotn.panjea.bi.rich.editors.analisi.commands.ExportToJrxmlCommand;
import it.eurotn.panjea.bi.rich.editors.analisi.commands.FlatLayoutCommand;
import it.eurotn.panjea.bi.rich.editors.analisi.commands.JoinTypeChangerCommand;
import it.eurotn.panjea.bi.rich.editors.analisi.commands.NuovoAnalisiCommand;
import it.eurotn.panjea.bi.rich.editors.analisi.commands.SalvaAnalisiCommand;
import it.eurotn.panjea.bi.rich.editors.analisi.commands.SlicingAnalisiCommand;
import it.eurotn.panjea.bi.rich.editors.analisi.commands.VisualizzaFieldChooserCommand;
import it.eurotn.panjea.bi.rich.editors.analisi.commands.VisualizzaPageToggleCommand;
import it.eurotn.rich.command.JECCommandGroup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.core.LabeledObjectSupport;

import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;
import com.jidesoft.swing.StyleRange;
import com.jidesoft.swing.StyledLabel;

/**
 * Costruisce e gestisce la toolbar per la gestione dell'editor dell'analisi.
 *
 * @author giangi
 */
public class ToolbarAnalisi extends JPanel {

	private class AggiornaNomeAnalisiCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand arg0) {
			labelAnalisi.setText(controller.getAnalisiBi().getNome());
			((LabeledObjectSupport) editor.getDescriptor()).setTitle(controller.getAnalisiBi().getNome());
		}

		@Override
		public boolean preExecution(ActionCommand arg0) {
			return true;
		}
	}

	private static final long serialVersionUID = 3167109352204507345L;
	private CaricaDataSourceCommand caricaDataSourceCommand;
	private SalvaAnalisiCommand salvaAnalisiCommand;
	private final AnalisiBiEditorController controller;
	private VisualizzaFieldChooserCommand visualizzaFieldChooserCommand;
	private NuovoAnalisiCommand nuovoAnalisiCommand;
	private VisualizzaPageToggleCommand visualizzaDettaglioCommand;
	private VisualizzaPageToggleCommand visualizzaGraficoCommand;
	private EsportaAnalisiCommand esportaAnalisiCommand;
	private SlicingAnalisiCommand slicingAnalisiCommand;
	private JoinTypeChangerCommand joinTypeChangerCommand;
	private ActionCommand autoResizeAnalisiCommand;

	private StyledLabel labelAnalisi;
	private final AbstractEditor editor;
	private JComponent toolbar;

	private AggiornaNomeAnalisiCommandInterceptor aggiornaNomeAnalisiCommandInterceptor = null;
	private ActionCommand exportToJrxmlCommand;
	private FlatLayoutCommand flatLayoutCommand;

	/**
	 * Costruttore della toolbar.
	 *
	 * @param controller
	 *            controller dell'editor dell'analisi
	 * @param editor
	 *            editor che contiene la toolbar.
	 */
	public ToolbarAnalisi(final AnalisiBiEditorController controller, final AbstractEditor editor) {
		super();
		this.controller = controller;
		this.setLayout(new BorderLayout());
		createControl();
		this.editor = editor;
	}

	/**
	 * Crea i controlli e li aggiunge al pannello (se stesso).
	 */
	private void createControl() {
		// Creo la combo con le analisi presenti
		// Lista dei jbutton
		JECCommandGroup commandGroup = new JECCommandGroup();
		commandGroup.add(getNuovoAnalisiCommand());
		commandGroup.add(getSalvaAnalisiCommand());
		commandGroup.addSeparator();
		commandGroup.add(getCaricaDataSourceCommand());
		commandGroup.addSeparator();
		commandGroup.add(getSlicingAnalisiCommand());
		commandGroup.add(getJoinTypeChangerCommand());
		commandGroup.addSeparator();
		commandGroup.add(getAutoResizeAnalisiCommand());
		commandGroup.add(getFlatLayoutCommand());
		commandGroup.add(getVisualizzaFieldChooserCommand());
		commandGroup.add(getVisualizzaDettaglioCommand());
		commandGroup.add(getVisualizzaGraficoCommand());
		commandGroup.addSeparator();
		commandGroup.add(getEsportaAnalisiCommand());
		commandGroup.add(getExportToJrxmlCommand());
		toolbar = commandGroup.createToolBar();
		this.add(toolbar, BorderLayout.CENTER);
		labelAnalisi = new StyledLabel("NOME ANALISI");
		labelAnalisi.addStyleRange(new StyleRange(Font.BOLD, Color.BLACK));
		this.add(labelAnalisi, BorderLayout.EAST);
	}

	/**
	 * dispose.
	 */
	public void dispose() {
		nuovoAnalisiCommand.removeCommandInterceptor(aggiornaNomeAnalisiCommandInterceptor);
		salvaAnalisiCommand.removeCommandInterceptor(aggiornaNomeAnalisiCommandInterceptor);

		nuovoAnalisiCommand = null;
		salvaAnalisiCommand = null;
		caricaDataSourceCommand = null;
		visualizzaFieldChooserCommand = null;
		slicingAnalisiCommand = null;
		visualizzaDettaglioCommand = null;
		visualizzaGraficoCommand = null;
		esportaAnalisiCommand = null;
	}

	/**
	 * @return the nuovoAnalisiCommandInterceptor to get
	 */
	private AggiornaNomeAnalisiCommandInterceptor getAggiornaNomeAnalisiCommandInterceptor() {
		if (aggiornaNomeAnalisiCommandInterceptor == null) {
			aggiornaNomeAnalisiCommandInterceptor = new AggiornaNomeAnalisiCommandInterceptor();
		}
		return aggiornaNomeAnalisiCommandInterceptor;
	}

	// /**
	// * @return command per selezionare e caricare un analisi
	// */
	// ActionCommand getCaricaAnalisiCommand() {
	// if (caricaAnalisiCommand == null) {
	// caricaAnalisiCommand = new CaricaAnalisiCommand(controller.getDatawarehouseBD());
	// caricaAnalisiCommand.addCommandInterceptor(getCaricaAnalisiCommandInterceptor());
	// }
	// return caricaAnalisiCommand;
	// }

	// /**
	// * @return the CaricaAnalisiCommandInterceptor to get
	// */
	// private CaricaAnalisiCommandInterceptor getCaricaAnalisiCommandInterceptor() {
	// if (caricaAnalisiCommandInterceptor == null) {
	// caricaAnalisiCommandInterceptor = new CaricaAnalisiCommandInterceptor();
	// }
	// return caricaAnalisiCommandInterceptor;
	// }

	/**
	 *
	 * @return command per eseguire l'autoresize di tutte le colonne
	 */
	ActionCommand getAutoResizeAnalisiCommand() {
		if (autoResizeAnalisiCommand == null) {
			autoResizeAnalisiCommand = new AutoResizeAnalisiCommand(controller);
		}
		return autoResizeAnalisiCommand;
	}

	/**
	 * Restituisce il command per caricare i dati dal dataSource al modello.
	 *
	 * @return command per caricare i dati
	 */
	ActionCommand getCaricaDataSourceCommand() {
		if (caricaDataSourceCommand == null) {
			caricaDataSourceCommand = new CaricaDataSourceCommand(controller);
		}
		return caricaDataSourceCommand;
	}

	/**
	 *
	 * @return command per esportare l'analisi
	 */
	ActionCommand getEsportaAnalisiCommand() {
		if (esportaAnalisiCommand == null) {
			esportaAnalisiCommand = new EsportaAnalisiCommand(controller);
		}
		return esportaAnalisiCommand;
	}

	/**
	 *
	 * @return command per eseguire l'esportazione dell'analisi a jasperserver
	 */
	ActionCommand getExportToJrxmlCommand() {
		if (exportToJrxmlCommand == null) {
			exportToJrxmlCommand = new ExportToJrxmlCommand(controller);
		}
		return exportToJrxmlCommand;
	}

	/**
	 *
	 * @return command per eseguire l'esportazione dell'analisi a jasperserver
	 */
	ActionCommand getFlatLayoutCommand() {
		if (flatLayoutCommand == null) {
			flatLayoutCommand = new FlatLayoutCommand(controller);
		}
		return flatLayoutCommand;
	}

	/**
	 * @return the joinTypeChangerCommand
	 */
	public JoinTypeChangerCommand getJoinTypeChangerCommand() {
		if (joinTypeChangerCommand == null) {
			joinTypeChangerCommand = new JoinTypeChangerCommand(controller);
		}

		return joinTypeChangerCommand;
	}

	/**
	 * Restituisce il comando per generare una nuova analisi.
	 *
	 * @return command
	 */
	private AbstractCommand getNuovoAnalisiCommand() {
		if (nuovoAnalisiCommand == null) {
			nuovoAnalisiCommand = new NuovoAnalisiCommand(controller);
			nuovoAnalisiCommand.addCommandInterceptor(getAggiornaNomeAnalisiCommandInterceptor());
		}
		return nuovoAnalisiCommand;
	}

	/**
	 * Restituisce il command per salvare l'analisi corrente.
	 *
	 * @return command per caricare i dati
	 */
	private ActionCommand getSalvaAnalisiCommand() {
		if (salvaAnalisiCommand == null) {
			salvaAnalisiCommand = new SalvaAnalisiCommand(controller);
			salvaAnalisiCommand.addCommandInterceptor(getAggiornaNomeAnalisiCommandInterceptor());
		}
		return salvaAnalisiCommand;
	}

	/**
	 * Restituisce il comando per eseguire lo slicing sulla pivot.
	 *
	 * @return comando creato
	 */
	public SlicingAnalisiCommand getSlicingAnalisiCommand() {
		if (slicingAnalisiCommand == null) {
			slicingAnalisiCommand = new SlicingAnalisiCommand(controller);
		}

		return slicingAnalisiCommand;
	}

	/**
	 * Restituisce il command per visualizzare il dettaglio.
	 *
	 * @return command per caricare i dati
	 */
	public ActionCommand getVisualizzaDettaglioCommand() {
		if (visualizzaDettaglioCommand == null) {
			visualizzaDettaglioCommand = new VisualizzaPageToggleCommand(controller.getDetailTablePage(),
					"DWVisualizzaDettaglioCommand");
		}
		return visualizzaDettaglioCommand;
	}

	/**
	 * Restituisce il comando per nascondere/visualizzare il pannello di scelta dei campi.
	 *
	 * @return command
	 */
	private ActionCommand getVisualizzaFieldChooserCommand() {
		if (visualizzaFieldChooserCommand == null) {
			visualizzaFieldChooserCommand = new VisualizzaFieldChooserCommand(controller);
		}
		return visualizzaFieldChooserCommand;
	}

	/**
	 * Restituisce il comando per nascondere/visualizzare il grafico.
	 *
	 * @return comando creato
	 */
	public VisualizzaPageToggleCommand getVisualizzaGraficoCommand() {
		if (visualizzaGraficoCommand == null) {
			visualizzaGraficoCommand = new VisualizzaPageToggleCommand(controller.getChartPage(),
					"DWVisualizzaGraficoCommand");
		}

		return visualizzaGraficoCommand;
	}

	/**
	 * Ricarica il titolo dell' analisi.
	 */
	public void refreshTitle() {
		labelAnalisi.setText(controller.getAnalisiBi().getNome());
		((LabeledObjectSupport) editor.getDescriptor()).setTitle(controller.getAnalisiBi().getNome());
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (Component component : toolbar.getComponents()) {
			component.setEnabled(enabled);
		}
	}
}
