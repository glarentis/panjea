/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.rich.DescrizioniEstesaEntityPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;

/**
 * @author fattazzo
 * 
 */
public class DescrizioniEsteseCategoriaForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "descrizioniEsteseCategoriaForm";

	private final AziendaCorrente aziendaCorrente;

	private final IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model della categoria
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD
	 * @param aziendaCorrente
	 *            aziendaCorrente
	 */
	public DescrizioniEsteseCategoriaForm(final FormModel formModel, final IAnagraficaTabelleBD anagraficaTabelleBD,
			final AziendaCorrente aziendaCorrente) {
		super(formModel, FORM_ID);
		this.anagraficaTabelleBD = anagraficaTabelleBD;
		this.aziendaCorrente = aziendaCorrente;
	}

	@Override
	protected JComponent createFormControl() {
		return new DescrizioniEstesaEntityPanel(getFormModel(), "descrizioniLinguaEstesa", null, anagraficaTabelleBD,
				aziendaCorrente);
	}

}
