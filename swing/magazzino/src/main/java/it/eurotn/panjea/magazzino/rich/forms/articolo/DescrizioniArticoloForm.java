package it.eurotn.panjea.magazzino.rich.forms.articolo;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.rich.DescrizioniEntityPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Form di Descrizioni in lingua di {@link Articolo}.
 *
 * @author adriano
 * @version 1.0, 21/apr/08
 *
 */
public class DescrizioniArticoloForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "descrizioniArticoloForm";
	public static final String FORMMODEL_ID = "descrizioniArticoloFormModel";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 *
	 * @param formModel
	 *            form model
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD
	 */
	public DescrizioniArticoloForm(final FormModel formModel, final IAnagraficaTabelleBD anagraficaTabelleBD) {
		super(formModel, FORM_ID);
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	@Override
	protected JComponent createFormControl() {
		return new DescrizioniEntityPanel(getFormModel(), "descrizioniLingua", anagraficaTabelleBD);
	}

}
