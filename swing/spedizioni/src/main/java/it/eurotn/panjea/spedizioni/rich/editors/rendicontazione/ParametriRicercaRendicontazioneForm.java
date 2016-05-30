/**
 * 
 */
package it.eurotn.panjea.spedizioni.rich.editors.rendicontazione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.spedizioni.util.ParametriRicercaRendicontazione;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriRicercaRendicontazioneForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "parametriRicercaRendicontazioneForm";

	/**
	 * @param parametriRicercaAreaMagazzino
	 *            .
	 */
	public ParametriRicercaRendicontazioneForm(final ParametriRicercaRendicontazione parametriRicercaRendicontazione) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaRendicontazione, false, FORM_ID), FORM_ID);

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
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "2dlu,default,2dlu,default,2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.addPropertyAndLabel("dataRegistrazione", 1, 2);

		Binding bindingEntita = bf.createBoundSearchText("vettore",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaVettori" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class);
		((SearchPanel) bindingEntita.getControl()).getTextFields().get("codice").setColumns(6);
		builder.addLabel("vettore", 1, 4);
		builder.addBinding(bindingEntita, 3, 4);

		builder.addPropertyAndLabel("soloNonRendicontati", 1, 6);

		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		logger.debug("--> Enter createNewObject");
		ParametriRicercaRendicontazione parametriRicercaRendicontazione = new ParametriRicercaRendicontazione();
		parametriRicercaRendicontazione.setStatiAreaMagazzino(new HashSet<AreaMagazzino.StatoAreaMagazzino>(Arrays
				.asList(AreaMagazzino.StatoAreaMagazzino.values())));
		parametriRicercaRendicontazione.setAnnoCompetenza(-1);
		parametriRicercaRendicontazione.setTipiGenerazione(null);
		parametriRicercaRendicontazione.setSoloNonRendicontati(Boolean.TRUE);
		logger.debug("--> Exit createNewObject");
		return parametriRicercaRendicontazione;
	}

}
