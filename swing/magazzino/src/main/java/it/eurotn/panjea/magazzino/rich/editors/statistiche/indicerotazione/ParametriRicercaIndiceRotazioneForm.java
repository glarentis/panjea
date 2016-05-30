package it.eurotn.panjea.magazzino.rich.editors.statistiche.indicerotazione;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.RicercaAvanzataArticoliCommand;
import it.eurotn.panjea.magazzino.rich.forms.statistiche.valorizzazione.ParametriValorizzazioneDepositiControl;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCalcoloIndiciRotazioneGiacenza;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriRicercaIndiceRotazioneForm extends PanjeaAbstractForm {

	private class SvuotaRicercaArticoliCommand extends ActionCommand {
		public SvuotaRicercaArticoliCommand() {
			super("svuotaRicercaArticoliCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getValueModel("articoli").setValue(new ArrayList<ArticoloLite>());
			labelArticoli.setText("Tutti");
		}
	}

	public static final String FORM_ID = "parametriRicercaIndiceRotazioneForm";

	private JLabel labelArticoli;

	private RicercaAvanzataArticoliCommand ricercaAvanzataArticoliCommand;

	private IAnagraficaBD anagraficaBD;

	private ParametriValorizzazioneDepositiControl depositiControl;

	private AziendaCorrente aziendaCorrente;

	/**
	 * Costruttore.
	 *
	 * @param parametriCalcoloIndiciRotazioneGiacenza
	 *            formObject
	 */
	public ParametriRicercaIndiceRotazioneForm(
			final ParametriCalcoloIndiciRotazioneGiacenza parametriCalcoloIndiciRotazioneGiacenza) {
		super(PanjeaFormModelHelper.createFormModel(
				parametriCalcoloIndiciRotazioneGiacenza, false, FORM_ID),
				FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:default, 4dlu, left:default, 10dlu,right:default, 4dlu,left:default, 10dlu,left:default:grow",
				"4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// ,new FormDebugPanelNumbered());
		builder.setLabelAttributes("r,c");
		builder.setRow(2);
		// builder.addHorizontalSeparator("Num. articoli sel.", 9, 1);
		builder.addPropertyAndLabel("periodo");
		builder.nextRow();
		builder.addLabel("articoli");
		JPanel commandsPanel = new JPanel(new FlowLayout());
		commandsPanel.setBorder(null);
		labelArticoli = new JLabel("Tutti");
		commandsPanel.add(labelArticoli);
		commandsPanel.add(getRicercaAvanzataArticoliCommand().createButton());
		commandsPanel.add(new SvuotaRicercaArticoliCommand().createButton());
		builder.addComponent(commandsPanel, 3, 4, 1, 1);

		depositiControl = new ParametriValorizzazioneDepositiControl(
				anagraficaBD, aziendaCorrente, getFormModel());
		builder.addComponent(depositiControl.getControl(), 9, 2, 1, 11);
		return builder.getPanel();
	}

	/**
	 * @return the ricercaAvanzataArticoliCommand
	 */
	private RicercaAvanzataArticoliCommand getRicercaAvanzataArticoliCommand() {
		if (ricercaAvanzataArticoliCommand == null) {
			ricercaAvanzataArticoliCommand = new RicercaAvanzataArticoliCommand(
					"aggiungiRicercaAvanzataArticoliCommand");
			ricercaAvanzataArticoliCommand
			.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand command) {
					List<ArticoloRicerca> articoliRicerca = ((RicercaAvanzataArticoliCommand) command)
							.getArticoliSelezionati();
					if (articoliRicerca != null
							&& articoliRicerca.size() > 0) {
						List<ArticoloRicerca> articoli = new ArrayList<>(
								((ParametriCalcoloIndiciRotazioneGiacenza) getFormObject())
								.getArticoli());
						articoli.addAll(articoliRicerca);
						getValueModel("articoli").setValue(articoli);
						labelArticoli.setText(new Integer(articoli
								.size()).toString());
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

	/**
	 *
	 * @param anagraficaBD
	 *            bd anagrafica
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param aziendaCorrente
	 *            The aziendaCorrente to set.
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}
}