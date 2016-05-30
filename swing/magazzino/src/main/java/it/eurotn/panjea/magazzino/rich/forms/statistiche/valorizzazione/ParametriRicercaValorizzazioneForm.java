/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.statistiche.valorizzazione;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.RicercaAvanzataArticoliCommand;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
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

/**
 * Form per l'input dei parametri di ricerca di {@link RigaValorizzazioneDTO}.
 *
 * @author fattazzo
 *
 */
public class ParametriRicercaValorizzazioneForm extends PanjeaAbstractForm {

	private class SvuotaRicercaArticoliCommand extends ActionCommand {
		public SvuotaRicercaArticoliCommand() {
			super("svuotaRicercaArticoliCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getValueModel("articoliLite").setValue(new ArrayList<ArticoloLite>());
			labelArticoli.setText("Tutti");
		}
	}

	public static final String FORM_ID = "parametriRicercaValorizzazioneForm";

	private RicercaAvanzataArticoliCommand ricercaAvanzataArticoliCommand;

	private IAnagraficaBD anagraficaBD;

	private AziendaCorrente aziendaCorrente;

	private ParametriValorizzazioneDepositiControl depositiControl;

	private JLabel labelArticoli;

	/**
	 *
	 * Costruttore.
	 *
	 * @param parametriRicercaValorizzazione
	 *            parametri di ricerca
	 */
	public ParametriRicercaValorizzazioneForm(final ParametriRicercaValorizzazione parametriRicercaValorizzazione) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaValorizzazione, false, FORM_ID), FORM_ID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:default, 4dlu, left:default, 10dlu,right:default, 4dlu,left:default, 10dlu,left:default:grow",
				"4dlu,default,4dlu,default,4dlu,default,4dlu,default,default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r,c");
		// builder.setComponentAttributes("l,t");
		builder.setRow(4);
		builder.addHorizontalSeparator("Num. articoli sel.", 1, 3);
		builder.addLabel("data", 1, 2);
		builder.addProperty("data", 3, 2);
		builder.addPropertyAndLabel("modalitaValorizzazione", 5, 2);
		builder.addPropertyAndLabel("consideraGiacenzaZero", 5, 4);
		builder.addPropertyAndLabel("consideraArticoliDisabilitati", 5, 6);
		JPanel commandsPanel = new JPanel(new FlowLayout());
		commandsPanel.setBorder(null);
		labelArticoli = new JLabel("Tutti");
		commandsPanel.add(labelArticoli);
		commandsPanel.add(getRicercaAvanzataArticoliCommand().createButton());
		commandsPanel.add(new SvuotaRicercaArticoliCommand().createButton());
		builder.addComponent(commandsPanel, 1, 5, 4, 4);

		depositiControl = new ParametriValorizzazioneDepositiControl(anagraficaBD, aziendaCorrente, getFormModel());
		builder.addComponent(depositiControl.getControl(), 9, 2, 1, 8);
		return builder.getPanel();
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
						List<ArticoloLite> articoli = new ArrayList<>(
								((ParametriRicercaValorizzazione) getFormObject()).getArticoliLite());
						for (ArticoloRicerca articoloRicerca : articoliRicerca) {
							articoli.add(articoloRicerca.createProxyArticoloLite());
						}
						getValueModel("articoliLite").setValue(articoli);
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

	/**
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
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

	/**
	 * @param depositiControl
	 *            The depositiControl to set.
	 */
	public void setDepositiControl(ParametriValorizzazioneDepositiControl depositiControl) {
		this.depositiControl = depositiControl;
	}

}
