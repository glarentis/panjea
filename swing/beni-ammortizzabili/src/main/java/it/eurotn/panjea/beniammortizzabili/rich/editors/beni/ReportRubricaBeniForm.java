/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.beniammortizzabili2.util.parametriricerca.ParametriRicercaBeni;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author adriano
 * @version 1.0, 24/nov/06
 * 
 */
public class ReportRubricaBeniForm extends PanjeaAbstractForm {

	/**
	 * 
	 * Implementazione di PropertyChangeListeners per la gestione delle attività legate alla spece.
	 * 
	 * @author fattazzo
	 * @version 1.0, 04/gen/08
	 * 
	 */
	private class SpecieChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Specie specieNew = (Specie) evt.getNewValue();
			Specie specieOld = (Specie) evt.getOldValue();
			if (specieNew == null) {
				// specie set a null, non azzera i valori
				return;
			}
			if (!specieNew.equals(specieOld)) {
				getFormModel().getValueModel("sottoSpecie").setValueSilently(null, this);
			}
		}
	}

	private static final String FORM_ID = "reportRubricaBeniForm";

	/**
	 * Costruttore di default.
	 * 
	 * @param parametriRicercaBeni
	 *            Parametri di ricerca
	 */
	public ReportRubricaBeniForm(final ParametriRicercaBeni parametriRicercaBeni) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaBeni, false, FORM_ID), FORM_ID);
	}

	/**
	 * Crea la search text per la sottospecie legata alla proprietà specie.
	 * 
	 * @param bf
	 *            PanjeaSwingBindingFactory
	 * @return Search text delal sottospecie
	 */
	private Binding createBindingSottoSpecie(PanjeaSwingBindingFactory bf) {
		Binding bindingSottoSpecie = bf.createBoundSearchText("sottoSpecie", new String[] { "codice", "descrizione" },
				new String[] { "specie" }, new String[] { "Spece" });
		((SearchPanel) bindingSottoSpecie.getControl()).getTextFields().get("codice").setColumns(3);
		return bindingSottoSpecie;

	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		builder.add(bf.createBoundSearchText("ubicazione", new String[] { "codice" }));
		builder.row();
		Binding bindingSpecie = bf.createBoundSearchText("specie", new String[] { "codice", "descrizione" });
		((SearchPanel) bindingSpecie.getControl()).getTextFields().get("codice").setColumns(3);
		builder.add(bindingSpecie);
		builder.row();
		builder.add(createBindingSottoSpecie(bf));
		builder.row();
		builder.add(bf.createBoundCheckBox("visualizzaFigli"));
		builder.row();
		builder.add(bf.createBoundCheckBox("stampaRaggruppamento"));
		builder.add(bf.createBoundCheckBox("visualizzaEliminati"));
		builder.row();

		addFormValueChangeListener("specie", new SpecieChangeListener());
		return builder.getForm();
	}

}
