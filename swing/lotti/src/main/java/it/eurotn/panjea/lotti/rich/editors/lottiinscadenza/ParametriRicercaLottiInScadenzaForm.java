/**
 * 
 */
package it.eurotn.panjea.lotti.rich.editors.lottiinscadenza;

import it.eurotn.panjea.lotti.util.ParametriRicercaScadenzaLotti;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form per l'input dei parametri di ricerca di {@link RigaValorizzazioneDTO}.
 * 
 * @author fattazzo
 * 
 */
public class ParametriRicercaLottiInScadenzaForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRicercaLottiInScadenzaForm";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private ParametriCategorieControl categorieControl;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public ParametriRicercaLottiInScadenzaForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaScadenzaLotti(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:default, 4dlu, left:default, 10dlu,fill:pref:grow",
				"4dlu,default,4dlu,default,4dlu,default,default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r,c");

		builder.setRow(2);
		builder.addLabel("deposito", 1);
		Binding depositoBinding = bf.createBoundSearchText("deposito", new String[] { "codice", "descrizione" });
		SearchPanel searchPanelDeposito = (SearchPanel) builder.addBinding(depositoBinding, 3);
		searchPanelDeposito.getTextFields().get("codice").setColumns(5);
		builder.nextRow();
		builder.addPropertyAndLabel("dataScadenza", 1);
		builder.nextRow();

		categorieControl = new ParametriCategorieControl(magazzinoAnagraficaBD, getFormModel());
		builder.addComponent(categorieControl.getControl(), 5, 1, 1, 7);
		return builder.getPanel();
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
