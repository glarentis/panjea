/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloBene;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * 
 * @author Aracno
 * @version 1.0, 13/nov/06
 * 
 */
public class PoliticaCalcoloBeneForm extends PanjeaAbstractForm {

	private class ApriSimulazioneCommand extends ApplicationWindowAwareCommand {

		private static final String APRI_SIMULAZIONE_COMMAND_ID = "politicaCalcoloBeneForm" + ".apriSimulazioneCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public ApriSimulazioneCommand() {
			super(APRI_SIMULAZIONE_COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			PoliticaCalcoloBene politicaCalcoloBene = (PoliticaCalcoloBene) getFormObject();

			Simulazione simulazione = politicaCalcoloBene.getSimulazione();
			LifecycleApplicationEvent event = new OpenEditorEvent(beniAmmortizzabiliBD.caricaSimulazione(simulazione));
			Application.instance().getApplicationContext().publishEvent(event);
		}

	}

	public static final String FORM_ID = "politicaCalcoloBeneForm";

	private ApriSimulazioneCommand apriSimulazioneCommand = null;
	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 * 
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 * 
	 */
	public PoliticaCalcoloBeneForm(final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(PanjeaFormModelHelper.createFormModel(new PoliticaCalcoloBene(), false, FORM_ID), FORM_ID);
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	protected JComponent createFormControl() {
		final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();

		builder.add("politicaCalcoloFiscale.ammortamentoOrdinario", "colSpan=1 align=left");
		((JTextField) builder.add("politicaCalcoloFiscale.percAmmortamentoOrdinario", "colSpan=1 align=left")[1])
				.setColumns(4);
		builder.row();
		builder.add("politicaCalcoloFiscale.ammortamentoAnticipato", "colSpan=1 align=left");
		((JTextField) builder.add("politicaCalcoloFiscale.percAmmortamentoAnticipato", "colSpan=1 align=left")[1])
				.setColumns(4);
		builder.row();
		builder.add("politicaCalcoloFiscale.ammortamentoAccelerato", "colSpan=1 align=left");
		((JTextField) builder.add("politicaCalcoloFiscale.percAmmortamentoAccelerato", "colSpan=1 align=left")[1])
				.setColumns(4);
		builder.row();
		builder.add("politicaCalcoloFiscale.ammortamentoRidotto", "colSpan=1 align=left");
		((JTextField) builder.add("politicaCalcoloFiscale.percAmmortamentoRidotto", "colSpan=1 align=left")[1])
				.setColumns(4);
		builder.row();
		builder.row();
		builder.getLayoutBuilder().cell(getApriSimulazioneCommand().createButton(), "align=left");
		builder.row();

		builder.row();

		return builder.getForm();
	}

	/**
	 * @return AbstractCommand
	 */
	private AbstractCommand getApriSimulazioneCommand() {
		if (apriSimulazioneCommand == null) {
			apriSimulazioneCommand = new ApriSimulazioneCommand();
		}
		return apriSimulazioneCommand;
	}

}
