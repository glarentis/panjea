package it.eurotn.panjea.magazzino.rich.editors.articolo.alternativi;

import it.eurotn.panjea.magazzino.domain.ArticoloAlternativo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author
 */
public class ArticoloAlternativoForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "articoloAlternativoForm";

	/**
	 * Costruttore.
	 */
	public ArticoloAlternativoForm() {
		super(PanjeaFormModelHelper.createFormModel(new ArticoloAlternativo(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default,10dlu,right:pref,4dlu,fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addLabel("articoloAlternativo", 1);
		SearchPanel searchPanelArticolo = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articoloAlternativo", new String[] { "codice", "descrizione" }), 3);
		searchPanelArticolo.getTextFields().get("codice").setColumns(10);
		searchPanelArticolo.getTextFields().get("descrizione").setColumns(25);
		builder.nextRow();

		builder.addPropertyAndLabel("corrispondenza", 1);
		builder.nextRow();

		return builder.getPanel();
	}

}
