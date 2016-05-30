package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author leonardo
 *
 */
public class ParametriRicercaSommaForm extends PanjeaAbstractForm {

	public class AssegnaCommand extends ApplicationWindowAwareCommand {

		/**
		 * Costruttore.
		 */
		public AssegnaCommand() {
			super("assegnaCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

		}
	}

	private class CalcolaCommand extends ApplicationWindowAwareCommand {

		/**
		 * Costruttore.
		 */
		public CalcolaCommand() {
			super("calcolaCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			combinazioniValueHolder.refresh();
			ParametriRicercaSomma parametri = (ParametriRicercaSomma) getFormObject();
			if (parametri.getCombinazioni().size() > 0) {
				getValueModel("combinazioneSelezionataIndex").setValue(-1);
				getValueModel("combinazioneSelezionataIndex").setValue(0);
			}
		}
	}

	private class CombinazioniValueHolderClosure implements Closure {

		@Override
		public Object call(Object argument) {
			ParametriRicercaSomma parametri = (ParametriRicercaSomma) getFormObject();
			return new ValueHolder(parametri.calcola());
		}

	}

	public static final String FORM_ID = "parametriRicercaSommaForm";
	public static final String FORMMODEL_ID = "parametriRicercaSommaFormModel";
	private RefreshableValueHolder combinazioniValueHolder;
	private ActionCommand assegnaDataCommand;

	/**
	 * Costruttore.
	 *
	 * @param areaEffetti
	 *            effetto con i pagamenti
	 */
	public ParametriRicercaSommaForm(final AreaEffetti areaEffetti) {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaSomma(areaEffetti), false, FORMMODEL_ID),
				FORM_ID);
		assegnaDataCommand = new AssegnaCommand();
	}

	@Override
	protected JComponent createFormControl() {
		combinazioniValueHolder = new RefreshableValueHolder(new CombinazioniValueHolderClosure());
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:50dlu, 10dlu, right:pref,4dlu,left:default, 10dlu",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("sommaDaCercare");
		builder.addComponent(new CalcolaCommand().createButton(), 4);
		combinazioniValueHolder.refresh();
		builder.addBinding(bf.createBoundComboBox("combinazioneSelezionataIndex", combinazioniValueHolder), 5);
		builder.nextRow();
		builder.addPropertyAndLabel("dataValutaDaAssegnare");
		builder.addComponent(assegnaDataCommand.createButton(), 4);
		builder.nextRow();
		return builder.getPanel();
	}

	public ActionCommand getAssegnaDataCommand() {
		return assegnaDataCommand;
	}

}
