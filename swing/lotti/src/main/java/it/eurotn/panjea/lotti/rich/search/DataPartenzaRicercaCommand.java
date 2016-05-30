package it.eurotn.panjea.lotti.rich.search;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.MenuItemCommand;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Dimension;
import java.awt.Window;
import java.text.SimpleDateFormat;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import org.joda.time.DateTime;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.factory.MenuFactory;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class DataPartenzaRicercaCommand extends MenuItemCommand {

	public class PeriodoPM  {

		private Periodo periodo;

		/**
		 * Costruttore.
		 */
		public PeriodoPM() {
			super();
		}

		/**
		 * Costruttore.
		 *
		 * @param periodo periodo
		 */
		public PeriodoPM(final Periodo periodo) {
			super();
			this.periodo = periodo;
		}

		/**
		 * @return the periodo
		 */
		public Periodo getPeriodo() {
			return periodo;
		}



		/**
		 * @param periodo the periodo to set
		 */
		public void setPeriodo(Periodo periodo) {
			this.periodo = periodo;
		}
	}

	private class PeriodoPMForm extends PanjeaAbstractForm {

		/**
		 * Costruttore.
		 */
		public PeriodoPMForm() {
			super(PanjeaFormModelHelper.createFormModel(new PeriodoPM(), false, "PeriodoPMForm"), "PeriodoPMForm");
		}

		@Override
		protected JComponent createFormControl() {
			final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
			FormLayout layout = new FormLayout("right:pref,4dlu,left:pref","2dlu,default");
			FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
			builder.setLabelAttributes("r, c");
			builder.setRow(2);

			builder.addPropertyAndLabel("periodo");

			return builder.getPanel();
		}

	}

	private static final String COMMAND_ID = "dataPartenzaRicercaCommand";

	private Periodo periodo;

	private JMenuItem menuItem;

	private String settingsKey;

	private PeriodoPMForm periodoPMForm = new PeriodoPMForm();

	private Settings settings;

	/**
	 * Costruttore.
	 *
	 * @param searchPanel serach panel
	 * @param settings settings
	 *
	 */
	public DataPartenzaRicercaCommand(final SearchPanel searchPanel, final Settings settings) {
		super(COMMAND_ID);
		this.settings = settings;
		RcpSupport.configure(this);
		settingsKey = new StringBuilder(200).append(searchPanel.getFormModel().getId()).append(".").append(searchPanel.getFormPropertyPath()).append(".").append(DataPartenzaRicercaCommand.COMMAND_ID).toString();
	}

	@Override
	public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
			CommandButtonConfigurer buttonConfigurer) {
		menuItem = super.createMenuItem(faceDescriptorId, menuFactory, buttonConfigurer);
		return menuItem;
	}

	@Override
	protected void doExecuteCommand() {

		periodoPMForm.setFormObject(new PeriodoPM(periodo));
		PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(periodoPMForm,(Window)null) {

			@Override
			protected boolean onFinish() {

				periodoPMForm.commit();

				PeriodoPM periodoPM = (PeriodoPM) periodoPMForm.getFormObject();
				setPeriodo(periodoPM.getPeriodo());
				savePeriodoSettings();

				return true;
			}
		};
		dialog.setTitlePaneTitle("Impostazione del periodo iniziale per la ricerca lotti");
		dialog.setPreferredSize(new Dimension(450,80));
		dialog.showDialog();
	}

	/**
	 * @return the periodo
	 */
	public Periodo getPeriodo() {
		return periodo;
	}

	/**
	 * @return the settingsKey
	 */
	public String getSettingsKey() {
		return settingsKey;
	}

	/**
	 * Carica il periodo dalle settings.
	 */
	public void loadPeriodoFromSettings() {

		Periodo periodoLoad = new Periodo();
		if(!settings.contains(settingsKey + ".tipoPeriodo")) {
			periodoLoad.setTipoPeriodo(TipoPeriodo.NESSUNO);
		} else {
			periodoLoad.setTipoPeriodo(TipoPeriodo.values()[settings.getInt(settingsKey + ".tipoPeriodo")]);
			switch (periodoLoad.getTipoPeriodo()) {
			case DATE:
				periodoLoad.setDataIniziale(new DateTime(settings.getLong(settingsKey + ".dataIniziale")).toDate());
				periodoLoad.setDataFinale(new DateTime(settings.getLong(settingsKey + ".dataFinale")).toDate());
				break;
			case ULTIMI_GIORNI:
				periodoLoad.setNumeroGiorni(settings.getInt(settingsKey + ".numeroGiorni"));
				break;
			default:
				break;
			}
		}
		setPeriodo(periodoLoad);
	}

	/**
	 * Salva il periodo corrente sulle settings.
	 */
	private void savePeriodoSettings() {
		settings.setInt(settingsKey + ".tipoPeriodo", periodo.getTipoPeriodo().ordinal());
		settings.remove(settingsKey + ".dataIniziale");
		settings.remove(settingsKey + ".dataFinale");
		settings.remove(settingsKey + ".numeroGiorni");

		switch (periodo.getTipoPeriodo()) {
		case DATE:
			settings.setLong(settingsKey + ".dataIniziale", periodo.getDataIniziale().getTime());
			settings.setLong(settingsKey + ".dataFinale", periodo.getDataFinale().getTime());
			break;
		case ULTIMI_GIORNI:
			settings.setInt(settingsKey + ".numeroGiorni", periodo.getNumeroGiorni());
			break;
		default:
			break;
		}
	}

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;

		String periodoString = RcpSupport.getMessage(periodo.getTipoPeriodo().getClass().getName() + "." + periodo.getTipoPeriodo().name());

		StringBuilder text = new StringBuilder();
		text.append("Periodo inizio ricerca: ");
		text.append(periodoString);
		switch (periodo.getTipoPeriodo()) {
		case DATE:
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			text.append(dateFormat.format(periodo.getDataIniziale()));
			text.append(" - ");
			text.append(dateFormat.format(periodo.getDataFinale()));
			break;
		case ULTIMI_GIORNI:
			text.append(" ");
			text.append(periodo.getNumeroGiorni());
			break;
		default:
			break;
		}

		menuItem.setText(text.toString());
	}
}