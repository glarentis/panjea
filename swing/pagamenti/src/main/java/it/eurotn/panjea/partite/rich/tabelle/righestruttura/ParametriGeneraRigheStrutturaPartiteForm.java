package it.eurotn.panjea.partite.rich.tabelle.righestruttura;

import it.eurotn.panjea.partite.rich.commands.GeneraRigheStrutturaCommand;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriGeneraRigheStrutturaPartiteForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "parametriGeneraRigheStrutturaPartiteForm";
	private GeneraRigheStrutturaCommand generaRigheStrutturaCommand;

	/**
	 * Costruttore di default.
	 */
	public ParametriGeneraRigheStrutturaPartiteForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriGeneraRigheStrutturaPartite(), false, FORM_ID),
				FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, left:default,4dlu,left:default, default", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r, c");

		((JTextField) builder.addPropertyAndLabel("numeroRate", 1)[1]).setColumns(4);
		((JTextField) builder.addPropertyAndLabel("intervallo", 5)[1]).setColumns(4);
		builder.addComponent(generaRigheStrutturaCommand.createButton(), 8);

		return builder.getPanel();
	}

	/**
	 * @param generaRigheStrutturaCommand
	 *            the generaRigheStrutturaCommand to set
	 */
	public void setGeneraRigheStrutturaCommand(GeneraRigheStrutturaCommand generaRigheStrutturaCommand) {
		this.generaRigheStrutturaCommand = generaRigheStrutturaCommand;
		new PanjeaFormGuard(getFormModel(), this.generaRigheStrutturaCommand);
	}

}
