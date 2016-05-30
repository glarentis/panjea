package it.eurotn.panjea.mrp.rich.editors.risultato;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class RisultatoMrpFlatForm extends PanjeaAbstractForm {

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore.
	 */
	public RisultatoMrpFlatForm() {
		super(PanjeaFormModelHelper.createFormModel(new RisultatoMrpFlat(), false, "risultatoMrpFlatForm"));
		List<TipoEntita> tipiEntita = new ArrayList<TipoEntita>();
		tipiEntita.add(TipoEntita.FORNITORE);

		ValueModel tipiEntitaValueModel = new ValueHolder(tipiEntita);
		DefaultFieldMetadata tipiEntitaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaValueModel), List.class, true, null);
		getFormModel().add("tipiEntita", tipiEntitaValueModel, tipiEntitaData);

		ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:default,4dlu,left:default,10dlu,left:default,4dlu,left:default,10dlu,left:default,4dlu,left:default,10dlu,left:default,4dlu,left:default,10dlu",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.nextRow();
		builder.setRow(2);
		builder.addBinding(bf.createBoundSearchText("fornitore", new String[] { "codice", "anagrafica.denominazione" },
				new String[] { "tipiEntita" }, new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY },
				EntitaLite.class));
		builder.addPropertyAndLabel("dataDocumento", 5);
		builder.addPropertyAndLabel("dataConsegna", 9);
		JTextField qtaCalcolata = (JTextField) builder.addPropertyAndLabel("qtaCalcolata", 13)[1];
		qtaCalcolata.setColumns(5);
		builder.nextRow();
		builder.addBinding(
				bf.createBoundComboBox("tipoAreaOrdineDaGenerare", getTipidocumento(), "tipoDocumento.codice"), 1, 4,
				2, 1);
		return builder.getPanel();
	}

	/**
	 *
	 * @return valuemodel dei tipi documento che hanno un tipoAreaMagazzino
	 */
	private ValueModel getTipidocumento() {
		List<TipoAreaOrdine> tipiAreaOrdine = ordiniDocumentoBD.caricaTipiAreaOrdine("tipoDocumento.codice", null,
				false);
		return new ValueHolder(tipiAreaOrdine);
	}

}
