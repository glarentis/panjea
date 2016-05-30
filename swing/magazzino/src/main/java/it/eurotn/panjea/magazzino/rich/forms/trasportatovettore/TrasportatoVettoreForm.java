/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.trasportatovettore;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 * 
 */
public class TrasportatoVettoreForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "trasportatoVettoreForm";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	/**
	 * Costruttore.
	 */
	public TrasportatoVettoreForm() {
		super(PanjeaFormModelHelper.createFormModel(new TrasportatoVettorePM(), false, FORM_ID), FORM_ID);

		magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text dei vettori mi selezioni solo vettori e
		// non altri tipi entità
		List<TipoEntita> tipiEntitaVettore = new ArrayList<TipoEntita>();
		tipiEntitaVettore.add(TipoEntita.VETTORE);

		ValueModel tipiEntitaVettoreValueModel = new ValueHolder(tipiEntitaVettore);
		DefaultFieldMetadata tipiEntitaVettoreData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaVettoreValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaVettore", tipiEntitaVettoreValueModel, tipiEntitaVettoreData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default,10dlu,right:pref,4dlu,fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addPropertyAndLabel("periodo", 1, 2, 4);

		builder.addLabel("vettore", 1, 6);
		SearchPanel searchAgente = (SearchPanel) builder.addBinding(bf.createBoundSearchText("vettore", new String[] {
				"codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaVettore" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class), 3, 6);
		searchAgente.getTextFields().get("codice").setColumns(5);
		searchAgente.getTextFields().get("anagrafica.denominazione").setColumns(15);

		List<TipoAreaMagazzino> tipiAreeMagazzino = magazzinoDocumentoBD.caricaTipiAreaMagazzino(
				"tipoDocumento.codice", null, true);
		builder.addBinding(
				bf.createBoundCheckBoxTree("tipiAree", new String[] { "tipoDocumento.abilitato",
						"tipoDocumento.classeTipoDocumento" }, new ValueHolder(tipiAreeMagazzino)), 7, 2, 1, 6);

		return builder.getPanel();
	}

	/**
	 * Setta la ricerca con il vettore selezionato togliendo la possibilità di selezionarne un'altro o tutti i vettori.
	 * 
	 * @param vettore
	 *            vettore da impostare
	 */
	public void setVettore(VettoreLite vettore) {

		getValueModel("vettore").setValue(vettore);
		getFormModel().getFieldMetadata("vettore").setReadOnly(true);
	}

}
