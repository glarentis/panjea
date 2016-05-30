/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.contratto.stampa;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaStampaContratti;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 *
 * @author adriano
 * @version 1.0, 12/dic/06
 */
public class ParametriRicercaStampaContrattiForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "reportVenditaBeniForm";

	/**
	 * Costruttore.
	 *
	 */
	public ParametriRicercaStampaContrattiForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaStampaContratti(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:200dlu", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addLabel("entita", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(5);
		searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(18);
		builder.nextRow();

		builder.addLabel("articolo", 1);
		searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(5);
		searchPanel.getTextFields().get("descrizione").setColumns(18);
		builder.nextRow();

		builder.addPropertyAndLabel("escludiContrattiNonAttivi", 1);

		return builder.getPanel();
	}

}
