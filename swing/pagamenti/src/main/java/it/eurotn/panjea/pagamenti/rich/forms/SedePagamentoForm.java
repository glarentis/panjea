/**
 * 
 */
package it.eurotn.panjea.pagamenti.rich.forms;

import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form per la creazione dei controlli per l'oggetto di dominio
 * {@link SedePagamento}.
 * 
 * @author adriano
 * @version 1.0, 19/dic/2008
 */
public class SedePagamentoForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(SedePagamentoForm.class);

	private static final String FORM_ID = "sedePagamentoForm";

	/**
	 * Costruttore di default.
	 */
	public SedePagamentoForm() {
		super(PanjeaFormModelHelper.createFormModel(new SedePagamento(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:90dlu,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, fill:default:grow",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		Binding bindingCodPag = bf.createBoundSearchText("codicePagamento", new String[] { "codicePagamento",
				"descrizione" });
		builder.addLabel("codicePagamento", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(bindingCodPag, 3);
		searchPanel.getTextFields().get("codicePagamento").setColumns(6);
		searchPanel.getTextFields().get("descrizione").setColumns(27);

		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}

}
