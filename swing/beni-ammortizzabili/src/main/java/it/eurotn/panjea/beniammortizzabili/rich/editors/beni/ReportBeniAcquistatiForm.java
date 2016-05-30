/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.beniammortizzabili2.util.parametriricerca.ParametriRicercaBeni;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author adriano
 * @version 1.0, 04/dic/06
 * 
 */
public class ReportBeniAcquistatiForm extends PanjeaAbstractForm {

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
				getFormModel().getValueModel("sottoSpecie").setValueSilently(new SottoSpecie(), null);
			}
		}
	}

	private static final String FORM_ID = "reportBeniAcquistatiForm";

	/**
	 * Costruttore di default.
	 * 
	 * @param parametriRicercaBeni
	 *            Parametri di ricerca
	 */
	public ReportBeniAcquistatiForm(final ParametriRicercaBeni parametriRicercaBeni) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaBeni, false, FORM_ID), FORM_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text dei fornitori mi selezioni solo
		// fornitori e
		// non altri tipi entità
		List<TipoEntita> tipiEntitaFornitore = new ArrayList<TipoEntita>();
		tipiEntitaFornitore.add(TipoEntita.FORNITORE);

		ValueModel tipiEntitaFornitoriValueModel = new ValueHolder(tipiEntitaFornitore);
		DefaultFieldMetadata tipiEntitaFornitoriData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaFornitoriValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaFornitori", tipiEntitaFornitoriValueModel, tipiEntitaFornitoriData);
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
		((SearchPanel) bindingSottoSpecie.getControl()).getTextFields().get("codice").setColumns(5);
		return bindingSottoSpecie;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("anno", "align=left colspan=2")[1]).setColumns(4);
		builder.row();
		Binding bindingSpecie = bf.createBoundSearchText("specie", new String[] { "codice", "descrizione" });
		((SearchPanel) bindingSpecie.getControl()).getTextFields().get("codice").setColumns(5);
		builder.add(bindingSpecie);
		builder.row();
		builder.add(createBindingSottoSpecie(bf));
		builder.row();
		SearchPanel searchPanelFornitore = (SearchPanel) builder.add(bf.createBoundSearchText("fornitoreLite",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaFornitori" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class))[1];
		searchPanelFornitore.getTextFields().get("codice").setColumns(5);

		builder.row();
		builder.add(bf.createBoundCheckBox("visualizzaEliminati"));
		builder.add(bf.createBoundCheckBox("raggruppaUbicazione"));
		builder.row();

		addFormValueChangeListener("specie", new SpecieChangeListener());
		return builder.getForm();
	}

}
