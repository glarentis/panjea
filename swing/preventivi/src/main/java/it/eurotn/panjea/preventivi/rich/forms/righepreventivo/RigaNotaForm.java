package it.eurotn.panjea.preventivi.rich.forms.righepreventivo;

import it.eurotn.panjea.preventivi.domain.RigaNota;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class RigaNotaForm extends PanjeaAbstractForm {

	protected static Logger logger = Logger.getLogger(RigaNotaForm.class);
	public static final String FORM_ID = "rigaNotaForm";

	/**
	 * Costruttore.
	 */
	public RigaNotaForm() {
		super(PanjeaFormModelHelper.createFormModel(new RigaNota(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref, 4dlu, fill:pref:grow", "4dlu,default, 4dlu,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r,c");

		builder.nextRow();
		builder.setRow(2);

		builder.setComponentAttributes("f, f");
		Binding descrizioneBinding = bf.createBoundHTMLEditor("nota");
		JPanel offertaPanel = new JPanel(new BorderLayout());
		offertaPanel.add(descrizioneBinding.getControl(), BorderLayout.CENTER);
		builder.addComponent(offertaPanel, 1, 4, 3, 1);
		builder.nextRow();
		return builder.getPanel();
	}

}
