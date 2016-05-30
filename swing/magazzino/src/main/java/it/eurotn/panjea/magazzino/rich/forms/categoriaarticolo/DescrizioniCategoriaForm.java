/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.magazzino.rich.DescrizioniEntityPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;

/**
 * @author fattazzo
 * 
 */
public class DescrizioniCategoriaForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "descrizioniCategoriaForm";

	private final IAnagraficaTabelleBD anagraficaTabelleBD;

	private DescrizioniEntityPanel descrizioniEntityPanel = null;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model della categoria
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD
	 */
	public DescrizioniCategoriaForm(final FormModel formModel, final IAnagraficaTabelleBD anagraficaTabelleBD) {
		super(formModel, FORM_ID);
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	protected JComponent createFormControl() {
		return descrizioniEntityPanel;
	}

	/**
	 * @return <code>true</code> se non ci sono informazioni lingua avvalorate
	 */
	public boolean isEmpty() {
		if (descrizioniEntityPanel == null) {
			descrizioniEntityPanel = new DescrizioniEntityPanel(getFormModel(), "informazioniLingua",
					anagraficaTabelleBD);
		}

		return descrizioniEntityPanel.isEmpty();
	}
}
