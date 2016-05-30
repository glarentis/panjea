package it.eurotn.panjea.magazzino.rich.editors.righemagazzino.datiaccompagnatori;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.documento.DatoAccompagnatorioMagazzinoMetaData;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.VettorePropertyChange;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class RichiestaDatiAccompagnatoriForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "richiestaDatiAccompagnatoriForm";

	/**
	 * Costruttore.
	 *
	 * @param areaMagazzinoFullDTO
	 *            areaMagazzinoFullDTO
	 */
	public RichiestaDatiAccompagnatoriForm(final AreaMagazzinoFullDTO areaMagazzinoFullDTO) {
		super(PanjeaFormModelHelper.createFormModel(areaMagazzinoFullDTO, false, FORM_ID), FORM_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text dei vettori mi selezioni solo vettori e
		// non altri tipi entità
		List<TipoEntita> tipiEntitaVettori = new ArrayList<TipoEntita>();
		tipiEntitaVettori.add(TipoEntita.VETTORE);

		ValueModel tipiEntitaVettoriValueModel = new ValueHolder(tipiEntitaVettori);
		DefaultFieldMetadata tipiEntitaVettoriData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaVettoriValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaVettori", tipiEntitaVettoriValueModel, tipiEntitaVettoriData);
	}

	/**
	 *
	 * @param datoAccompagnatorio
	 *            il dato accompagnatorio da inserire nel form
	 * @param builder
	 *            builder del form
	 * @param bf
	 *            il factory per la creazione dei componenti swing bindati
	 */
	protected void addDatoAccompagnatorioComponent(final DatoAccompagnatorioMagazzinoMetaData datoAccompagnatorio,
			FormLayoutFormBuilder builder, final PanjeaSwingBindingFactory bf) {

		builder.nextRow();
		int currentRow = builder.getRow();

		String propertyName = "areaMagazzino." + datoAccompagnatorio.getName();

		switch (datoAccompagnatorio.getTipoDatoAccompagnatorioMagazzino()) {
		case VETTORE:
			builder.addLabel(propertyName, 1);

			Binding bindingEntita = bf.createBoundSearchText("areaMagazzino.vettore", new String[] { "codice",
			"anagrafica.denominazione" }, new String[] { "tipiEntitaVettori" },
			new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class);
			((SearchPanel) bindingEntita.getControl()).getTextFields().get("codice").setColumns(5);
			((SearchPanel) bindingEntita.getControl()).getTextFields().get("anagrafica.denominazione").setColumns(15);
			builder.addBinding(bindingEntita, 3, currentRow, 2, 1);

			builder.addLabel("areaMagazzino.sedeVettore", 6);

			Binding sedeEntitaBinding = bf.createBoundSearchText("areaMagazzino.sedeVettore",
					new String[] { "sede.descrizione" }, new String[] { "areaMagazzino.vettore" },
					new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
			builder.addBinding(sedeEntitaBinding, 8);

			VettorePropertyChange vettorePropertyChange = new VettorePropertyChange(
					getValueModel("areaMagazzino.sedeVettore"));
			vettorePropertyChange.setFormModel(getFormModel());
			vettorePropertyChange.setAnagraficaBD((IAnagraficaBD) RcpSupport.getBean(AnagraficaBD.BEAN_ID));
			getFormModel().getValueModel(propertyName).addValueChangeListener(vettorePropertyChange);

			break;

		case DATA_INIZIO_TRASPORTO:
			builder.addLabel(propertyName, 1);
			builder.addBinding(bf.createBoundCalendar(propertyName, "dd/MM/yy HH:mm", "##/##/## ##:##"), 3, currentRow,
					2, 1);
			break;

		case ASPETTO_ESTERIORE:
			builder.addLabel(propertyName, 1);
			builder.addBinding(bf.createBoundSearchText(propertyName, null, AspettoEsteriore.class), 3, currentRow, 2,
					1);
			break;

		case TRASPORTO_CURA:
			builder.addLabel(propertyName, 1);
			builder.addBinding(bf.createBoundSearchText(propertyName, null, TrasportoCura.class), 3, currentRow, 2, 1);
			break;

		case CAUSALE_TRASPORTO:
			builder.addLabel(propertyName, 1);
			builder.addBinding(bf.createBoundSearchText(propertyName, null, CausaleTrasporto.class), 3, currentRow, 2,
					1);
			break;

		case TIPO_PORTO:
			builder.addLabel(propertyName, 1);
			builder.addBinding(bf.createBoundSearchText(propertyName, null, TipoPorto.class), 3, currentRow, 2, 1);
			break;

		case RESPONSABILE_RITIRO:
			builder.addPropertyAndLabel(propertyName, 1, currentRow, 2);
			break;

		case MEZZO_TRASPORTO:
			builder.addLabel(propertyName, 1);
			builder.addBinding(bf.createBoundSearchText(propertyName, new String[] { "targa", "descrizione" },
					new String[] { "areaMagazzino.documento.entita" }, new String[] { "entita" }), 3, currentRow, 2, 1);

			// SearchPanel searchPaneMezzoTrasporto=(SearchPanel)
			// builder.addBinding(bf.createBoundSearchText("areaMagazzino.mezzoTrasporto", new String[] {
			// "targa", "descrizione" }, new String[] { "areaMagazzino.documento.entita" }, new String[] { "entita"
			// }),currentRow,2,1);
			// searchPaneMezzoTrasporto.getTextFields().get("targa").setColumns(5);
			// searchPaneMezzoTrasporto.getTextFields().get("descrizione").setColumns(20);
			break;
		default:
			builder.addPropertyAndLabel(propertyName);
		}
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,60dlu,100dlu,10dlu,right:default,4dlu,default:grow",
				"3dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // new
		// FormDebugPanel()

		Set<DatoAccompagnatorioMagazzinoMetaData> datiAccompagnatori = ((AreaMagazzinoFullDTO) getFormObject())
				.getAreaMagazzino().getTipoAreaMagazzino().getDatiAccompagnatoriMetaData();

		builder.setRow(2);

		for (DatoAccompagnatorioMagazzinoMetaData datoAccompagnatorio : datiAccompagnatori) {
			addDatoAccompagnatorioComponent(datoAccompagnatorio, builder, bf);
		}

		JPanel panel = builder.getPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		return panel;
	}

}
