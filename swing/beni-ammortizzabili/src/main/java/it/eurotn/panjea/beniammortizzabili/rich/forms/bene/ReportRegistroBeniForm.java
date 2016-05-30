/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.forms.bene;

import it.eurotn.panjea.beniammortizzabili2.domain.Gruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.beniammortizzabili2.util.parametriricerca.ParametriRicercaBeni;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author adriano
 * @version 1.0, 24/nov/06
 * 
 */
public class ReportRegistroBeniForm extends PanjeaAbstractForm {

	/**
	 * 
	 * Implementazione di PropertyChangeListeners per la gestione delle attività legate al gruppo.
	 * 
	 * @author fattazzo
	 * @version 1.0, 04/gen/08
	 * 
	 */
	private class GruppoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Gruppo gruppoNew = (Gruppo) evt.getNewValue();
			Gruppo gruppoOld = (Gruppo) evt.getOldValue();
			if (gruppoNew == null) {
				// specie set a null, non azzera i valori
				return;
			}
			if (!gruppoNew.equals(gruppoOld)) {
				getFormModel().getValueModel("specie").setValueSilently(null, this);
				getFormModel().getValueModel("sottoSpecie").setValueSilently(null, this);
			}
		}
	}

	/**
	 * 
	 * Implementazione di PropertyChangeListeners per la gestione delle attività legate alla sottospece.
	 * 
	 * @author fattazzo
	 * @version 1.0, 04/gen/08
	 * 
	 */
	private class SottoSpecieChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			SottoSpecie sottoSpecieNew = (SottoSpecie) evt.getNewValue();
			SottoSpecie sottoSpecieOld = (SottoSpecie) evt.getOldValue();
			if (sottoSpecieNew == null) {
				// specie set a null, non azzera i valori
				return;
			}
			if (!sottoSpecieNew.equals(sottoSpecieOld)) {
				getFormModel().getValueModel("specie").setValueSilently(null, this);
				getFormModel().getValueModel("gruppo").setValueSilently(null, this);
			}
		}
	}

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
				getFormModel().getValueModel("gruppo").setValueSilently(null, this);
				getFormModel().getValueModel("sottoSpecie").setValueSilently(null, this);
			}
		}
	}

	private static final String FORM_ID = "reportRegistroBeniForm";

	/**
	 * Costruttore di default.
	 * 
	 * @param parametriRicercaBeni
	 *            Parametri di ricerca
	 */
	public ReportRegistroBeniForm(final ParametriRicercaBeni parametriRicercaBeni) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaBeni, false, FORM_ID), FORM_ID);
	}

	/**
	 * Crea la search text per il gruppo.
	 * 
	 * @param bf
	 *            PanjeaSwingBindingFactory
	 * @return Search text del gruppo
	 */
	private Binding createBindingGruppo(PanjeaSwingBindingFactory bf) {
		Binding bindingGruppo = bf.createBoundSearchText("gruppo", new String[] { "codice", "descrizione" });
		((SearchPanel) bindingGruppo.getControl()).getTextFields().get("codice").setColumns(3);
		return bindingGruppo;

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

	/**
	 * Crea la search text per la specie legata alla proprietà gruppo.
	 * 
	 * @param bf
	 *            PanjeaSwingBindingFactory
	 * @return Search text delal specie
	 */
	private Binding createBindingSpecie(PanjeaSwingBindingFactory bf) {
		Binding bindingSpecie = bf.createBoundSearchText("specie", new String[] { "codice", "descrizione" },
				new String[] { "gruppo" }, new String[] { "Gruppo" });
		((SearchPanel) bindingSpecie.getControl()).getTextFields().get("codice").setColumns(3);
		return bindingSpecie;

	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("anno", "align=left colspan=2")[1]).setColumns(4);
		builder.row();
		builder.add(createBindingGruppo(bf));
		builder.row();
		builder.add(createBindingSpecie(bf));
		builder.row();
		builder.add(createBindingSottoSpecie(bf));
		builder.row();

		addFormValueChangeListener("gruppo", new GruppoChangeListener());
		addFormValueChangeListener("specie", new SpecieChangeListener());
		addFormValueChangeListener("sottoSpecie", new SottoSpecieChangeListener());
		return builder.getForm();
	}

}
