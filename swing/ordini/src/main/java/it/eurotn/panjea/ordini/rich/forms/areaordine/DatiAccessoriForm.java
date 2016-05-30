package it.eurotn.panjea.ordini.rich.forms.areaordine;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.VettorePropertyChange;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class DatiAccessoriForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "datiAccessoriForm";

	private final IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageFormModel
	 *            form model
	 * @param anagraficaBD
	 *            anagraficaBD
	 */
	public DatiAccessoriForm(final FormModel pageFormModel, final IAnagraficaBD anagraficaBD) {
		super(pageFormModel, FORM_ID);
		this.anagraficaBD = anagraficaBD;

		// aggiungo la finta proprietà tipiEntita per far si che la search text dei vettori mi selezioni solo vettori e
		// non altri tipi entità
		List<TipoEntita> tipiEntitaVettori = new ArrayList<TipoEntita>();
		tipiEntitaVettori.add(TipoEntita.VETTORE);

		ValueModel tipiEntitaVettoriValueModel = new ValueHolder(tipiEntitaVettori);
		DefaultFieldMetadata tipiEntitaVettoriData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaVettoriValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaVettori", tipiEntitaVettoriValueModel, tipiEntitaVettoriData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		// RIGA 1:vettore e sede
		Binding entitaBinding = getVettoreBinding(bf);
		builder.add(entitaBinding);
		Binding sedeEntitaBinding = bf.createBoundSearchText("areaOrdine.sedeVettore",
				new String[] { "sede.descrizione" }, new String[] { "areaOrdine.vettore" },
				new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
		builder.add(sedeEntitaBinding, "align=left");
		builder.row();

		// RIGA 2
		builder.add(bf.createBoundSearchText("areaOrdine.causaleTrasporto", null, CausaleTrasporto.class), "align=left");

		builder.add(bf.createBoundSearchText("areaOrdine.trasportoCura", null, TrasportoCura.class), "align=left");
		builder.row();

		// RIGA 3
		builder.add(bf.createBoundSearchText("areaOrdine.tipoPorto", null, TipoPorto.class), "align=left");
		builder.row();

		// RIGA 5
		VettorePropertyChange vettorePropertyChange = new VettorePropertyChange(getValueModel("areaOrdine.sedeVettore"));
		vettorePropertyChange.setFormModel(getFormModel());
		vettorePropertyChange.setAnagraficaBD(this.anagraficaBD);
		getFormModel().getValueModel("areaOrdine.vettore").addValueChangeListener(vettorePropertyChange);

		return builder.getForm();
	}

	/**
	 * Crea il binding per il vettore.
	 * 
	 * @param bf
	 *            SwingBindingFactory
	 * @return binding creato
	 */
	private Binding getVettoreBinding(PanjeaSwingBindingFactory bf) {
		Binding bindingEntita = bf.createBoundSearchText("areaOrdine.vettore", new String[] { "codice",
				"anagrafica.denominazione" }, new String[] { "tipiEntitaVettori" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class);
		((SearchPanel) bindingEntita.getControl()).getTextFields().get("codice").setColumns(5);
		((SearchPanel) bindingEntita.getControl()).getTextFields().get("anagrafica.denominazione").setColumns(15);
		return bindingEntita;
	}

}
