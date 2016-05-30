/**
 * 
 */
package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.rich.commands.OpenPreviewAssegnoCommand;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author leonardo
 * 
 */
public class AreaAssegnoForm extends PanjeaAbstractForm {

	public class AssegnoCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand actioncommand) {
		}

		@Override
		public boolean preExecution(ActionCommand actioncommand) {
			AreaAssegno areaAssegno = (AreaAssegno) getFormObject();
			actioncommand.addParameter(OpenPreviewAssegnoCommand.IMMAGINE_ASSEGNO_PARAMETER,
					areaAssegno.getImmagineAssegno());
			return true;
		}

	}

	private static final String FORM_ID = "areaAssegnoForm";
	private OpenPreviewAssegnoCommand assegnoCommand;

	/**
	 * Costruttore.
	 */
	public AreaAssegnoForm() {
		super(PanjeaFormModelHelper.createFormModel(new AreaAssegno(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,10dlu,left:pref,4dlu,fill:150dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r,c");

		// ################################### : data documento
		builder.addPropertyAndLabel("documento.dataDocumento");

		Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaPartita", new String[] { "tipoDocumento.codice",
				"tipoDocumento.descrizione" }, null, null);
		builder.addLabel("tipoAreaPartita", 5);
		builder.addBinding(bindingTipoDoc, 7);
		SearchPanel componentTipoAreaPartita = (SearchPanel) bindingTipoDoc.getControl();
		componentTipoAreaPartita.getTextFields().get("tipoDocumento.codice").setColumns(5);
		componentTipoAreaPartita.getTextFields().get("tipoDocumento.descrizione").setColumns(18);
		builder.nextRow();

		// ################################### : num.doc.
		builder.addLabel("documento.codice");
		builder.addBinding(bf.createBoundCodice("documento.codice", "tipoAreaPartita.tipoDocumento.registroProtocollo",
				"documento.valoreProtocollo", "tipoAreaPartita.tipoDocumento.patternNumeroDocumento", null), 3);

		// ################################### : TOTALE DOCUMENTO
		builder.addLabel("documento.totale", 5);
		builder.addBinding(bf.createBoundImportoTextField("documento.totale"), 7);
		builder.nextRow();

		// ################################### : NUMERO ASSEGNO
		JComponent[] compsNumAssegno = builder.addPropertyAndLabel("numeroAssegno", 1);
		((JTextField) compsNumAssegno[1]).setColumns(9);

		assegnoCommand = new OpenPreviewAssegnoCommand();
		AbstractButton assegnoButton = assegnoCommand.createButton();
		builder.addComponent(assegnoButton, 4);
		assegnoCommand.addCommandInterceptor(new AssegnoCommandInterceptor());

		builder.nextRow();

		// ################################### : ABI E CAB ASSEGNO
		JComponent[] compsAbiAssegno = builder.addPropertyAndLabel("abi", 1);
		JComponent[] compsCabAssegno = builder.addPropertyAndLabel("cab", 5);
		((JTextField) compsAbiAssegno[1]).setColumns(5);
		((JTextField) compsCabAssegno[1]).setColumns(5);
		builder.nextRow();

		return builder.getPanel();
	}

}
