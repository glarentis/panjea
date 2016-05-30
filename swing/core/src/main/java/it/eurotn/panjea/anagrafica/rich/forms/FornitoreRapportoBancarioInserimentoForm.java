package it.eurotn.panjea.anagrafica.rich.forms;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.AbstractFocussableForm;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class FornitoreRapportoBancarioInserimentoForm extends AbstractFocussableForm {

	private class FornitorePropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (getFormModel().isReadOnly()) {
				return;
			}

			EntitaLite entita = (EntitaLite) getValueModel("fornitore").getValue();

			SedeEntita sedeEntita = null;
			if (entita != null && !entita.isNew()) {
				sedeEntita = anagraficaBD.caricaSedePrincipaleEntita(entita.creaProxyEntita());
			}
			getValueModel("sedeEntita").setValue(sedeEntita);
		}

	}

	public static final String FORM_ID = "fornitoreRapportoBancarioInserimentoForm";

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 *
	 *
	 */
	public FornitoreRapportoBancarioInserimentoForm() {
		super(PanjeaFormModelHelper.createFormModel(new SedeFornitoreLitePM(), false, FORM_ID), FORM_ID);
		this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);

		// per escludere le entità potenziali
		ValueModel entitaPotenzialiInRicercaValueModel = new ValueHolder(Boolean.FALSE);
		DefaultFieldMetadata entitaPotenzialimetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(entitaPotenzialiInRicercaValueModel), Boolean.class, true, null);
		getFormModel().add("entitaPotenzialiPerRicerca", entitaPotenzialiInRicercaValueModel, entitaPotenzialimetaData);

		// Aggiungo il value model che mi servirà solamente nella search text delle entità
		// per cercare solo le entità abilitate
		ValueModel entitaAbilitateInRicercaValueModel = new ValueHolder(Boolean.TRUE);
		DefaultFieldMetadata entitaAbilitateMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(entitaAbilitateInRicercaValueModel), Boolean.class, true, null);
		getFormModel().add("entitaAbilitateInRicerca", entitaAbilitateInRicercaValueModel, entitaAbilitateMetaData);

		// Aggiungo il value model che mi servirà solamente nella search text delle entità
		// per cercare solo i fornitori
		ValueModel entitaFornitoriValueModel = new ValueHolder(TipoEntita.FORNITORE);
		DefaultFieldMetadata entitaFornitoriMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(entitaFornitoriValueModel), TipoEntita.class, true, null);
		getFormModel().add("tipoEntita", entitaFornitoriValueModel, entitaFornitoriMetaData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:200dlu,fill:150dlu", "default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("l,c");

		Binding bindingEntita = bf.createBoundSearchText("fornitore", new String[] { "codice",
		"anagrafica.denominazione" }, new String[] { "tipoEntita", "entitaPotenzialiPerRicerca",
		"entitaAbilitateInRicerca" }, new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY,
				EntitaByTipoSearchObject.INCLUDI_ENTITA_POTENZIALI, EntitaByTipoSearchObject.FILTRO_ENTITA_ABILITATO },
				EntitaLite.class);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(bindingEntita);
		searchPanel.getTextFields().get("codice").getTextField().setColumns(10);
		setFocusControl(searchPanel.getTextFields().get("codice").getTextField());

		Binding sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", null, new String[] { "fornitore" },
				new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID }, SedeEntita.class);
		builder.addBinding(sedeEntitaBinding, 2);

		getValueModel("fornitore").addValueChangeListener(new FornitorePropertyChangeListener());

		return builder.getPanel();
	}

}
