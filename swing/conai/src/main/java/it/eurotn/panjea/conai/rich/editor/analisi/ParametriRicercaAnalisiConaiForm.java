package it.eurotn.panjea.conai.rich.editor.analisi;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.conai.rich.bd.ConaiBD;
import it.eurotn.panjea.conai.rich.bd.IConaiBD;
import it.eurotn.panjea.conai.util.parametriricerca.ParametriRicercaAnalisi;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriRicercaAnalisiConaiForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRicercaAnalisiConaiForm";

	private IConaiBD conaiBD;

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriRicercaAnalisiConaiForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaAnalisi(), false, FORM_ID), FORM_ID);

		ValueModel entitaClienteValueModel = new ValueHolder(TipoEntita.CLIENTE);
		DefaultFieldMetadata entitaClienteMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(entitaClienteValueModel), TipoEntita.class, true, null);
		getFormModel().add("tipiEntitaCliente", entitaClienteValueModel, entitaClienteMetaData);

		this.conaiBD = RcpSupport.getBean(ConaiBD.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,4dlu,fill:default:grow",
				"4dlu,default,2dlu,default,2dlu,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("l,c");

		builder.addPropertyAndLabel("periodo", 1, 2);

		builder.addLabel("cliente", 1, 4);
		SearchPanel searchAgente = (SearchPanel) builder.addBinding(bf.createBoundSearchText("cliente", new String[] {
				"codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaCliente" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY }, EntitaLite.class), 3);
		searchAgente.getTextFields().get("codice").setColumns(6);

		List<TipoAreaMagazzino> tipiAreeMagazzino = conaiBD.caricaTipiAreaMagazzinoConGestioneConai();
		builder.addBinding(
				bf.createBoundCheckBoxTree("tipiAreaMagazzino", new String[] { "tipoDocumento.abilitato",
						"tipoDocumento.classeTipoDocumento" }, new ValueHolder(tipiAreeMagazzino)), 5, 2, 1, 5);

		return builder.getPanel();
	}

}
