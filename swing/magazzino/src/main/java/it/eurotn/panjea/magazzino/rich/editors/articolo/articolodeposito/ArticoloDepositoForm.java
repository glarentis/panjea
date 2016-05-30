/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.articolodeposito;

import it.eurotn.panjea.magazzino.domain.ArticoloDeposito;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author leonardo
 */
public class ArticoloDepositoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "articoloDepositoForm";

	/**
	 * Costruttore di default, inizializza un nuovo CodiceArticoloEntita.
	 */
	public ArticoloDepositoForm() {
		super(PanjeaFormModelHelper.createFormModel(new ArticoloDeposito(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:90dlu,fill:120dlu", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addLabel("deposito", 1);
		builder.addBinding(getDepositoBinding(), 3, 2, 2, 1);
		builder.nextRow();
		builder.addPropertyAndLabel("scorta", 1);
		builder.nextRow();

		return builder.getPanel();
	}

	/**
	 * 
	 * @return Binding per l'entità
	 */
	private Binding getDepositoBinding() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		Binding bindingEntita = bf.createBoundSearchText("deposito", new String[] { "codice", "descrizione" });
		((SearchPanel) bindingEntita.getControl()).getTextFields().get("codice").setColumns(5);
		((SearchPanel) bindingEntita.getControl()).getTextFields().get("descrizione").setColumns(25);
		return bindingEntita;
	}

}
