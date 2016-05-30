package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.AggiungiNotaAnagraficaCommand;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.components.htmleditor.HTMLEditorPane;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

public class RigaNotaForm extends PanjeaAbstractForm {
	private class RigaNotaReadonlyPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			getAggiungiNotaAnagraficaCommand().setEnabled(!getFormModel().isReadOnly());
		}

	}

	protected static Logger logger = Logger.getLogger(RigaNotaForm.class);

	public static final String FORM_ID = "rigaNotaForm";

	private HTMLEditorPane noteEditorPane;

	private AggiungiNotaAnagraficaCommand aggiungiNotaAnagraficaCommand;

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
		FormLayout layout = new FormLayout("left:pref, 4dlu,left:pref, right:pref:grow",
				"4dlu,default, 4dlu,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("r,c");

		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("noteSuDestinazione");
		builder.nextRow();

		builder.setComponentAttributes("f, f");
		Binding descrizioneBinding = bf.createBoundHTMLEditor("nota");
		noteEditorPane = (HTMLEditorPane) descrizioneBinding.getControl().getComponent(0);
		builder.addComponent(descrizioneBinding.getControl(), 1, 4, 4, 1);
		builder.nextRow();

		builder.setComponentAttributes("r, f");

		builder.addComponent(getAggiungiNotaAnagraficaCommand().createButton(), 4, 2);
		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new RigaNotaReadonlyPropertyChange());

		return builder.getPanel();
	}

	/**
	 * @return the aggiungiNotaAnagraficaCommand
	 */
	public AggiungiNotaAnagraficaCommand getAggiungiNotaAnagraficaCommand() {
		if (aggiungiNotaAnagraficaCommand == null) {
			aggiungiNotaAnagraficaCommand = new AggiungiNotaAnagraficaCommand(new Closure() {

				@Override
				public Object call(Object obj) {
					noteEditorPane.setText(noteEditorPane.getText() + "<BR>" + ((NotaAnagrafica) obj).getDescrizione());
					return null;
				}
			});
		}

		return aggiungiNotaAnagraficaCommand;
	}

}
