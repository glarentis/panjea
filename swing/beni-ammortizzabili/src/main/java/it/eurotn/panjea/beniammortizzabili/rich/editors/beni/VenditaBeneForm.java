/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.TipologiaEliminazione;
import it.eurotn.panjea.beniammortizzabili2.domain.VenditaBene;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author Aracno
 * @version 1.0, 12/ott/06
 * 
 */
public class VenditaBeneForm extends PanjeaAbstractForm {

	private class PluMinusValorePropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			BigDecimal importoValoreVendita = BigDecimal.ZERO;
			BigDecimal importoStornoFondo = BigDecimal.ZERO;
			BigDecimal importoStornoValoreBene = BigDecimal.ZERO;

			if ((BigDecimal) getFormModel().getValueModel(VenditaBene.PROP_VALORE_VENDITA).getValue() != null) {
				importoValoreVendita = (BigDecimal) getFormModel().getValueModel(VenditaBene.PROP_VALORE_VENDITA)
						.getValue();
			}

			if ((BigDecimal) getFormModel().getValueModel(VenditaBene.PROP_IMPORTO_STORNO_FONDO_AMMORTAMENTO)
					.getValue() != null) {
				importoStornoFondo = (BigDecimal) getFormModel().getValueModel(
						VenditaBene.PROP_IMPORTO_STORNO_FONDO_AMMORTAMENTO).getValue();
			}

			if ((BigDecimal) getFormModel().getValueModel(VenditaBene.PROP_IMPORTO_STORNO_VALORE_BENE).getValue() != null) {
				importoStornoValoreBene = (BigDecimal) getFormModel().getValueModel(
						VenditaBene.PROP_IMPORTO_STORNO_VALORE_BENE).getValue();
			}

			getFormModel().getValueModel(VenditaBene.PROP_PLUS_MINUS_VALORE).setValue(
					importoValoreVendita.subtract(importoStornoValoreBene.subtract(importoStornoFondo)));
		}
	}

	private static Logger logger = Logger.getLogger(VenditaBeneForm.class);
	private static final String FORM_ID = "venditaBeneForm";
	private final IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	private BeneAmmortizzabile beneAmmortizzabile;

	private JTextField jcValoreBene;
	private JTextField jcValoreFondo;

	/**
	 * Costruttore.
	 * 
	 * @param beneAmmortizzabile
	 *            bene
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public VenditaBeneForm(final BeneAmmortizzabile beneAmmortizzabile, final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(PanjeaFormModelHelper.createFormModel(new VenditaBene(), false, FORM_ID), FORM_ID);
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
		this.beneAmmortizzabile = beneAmmortizzabile;

		// Aggiungo il value model che mi servirà solamente nella search text
		// delle entità
		// per cercare i clienti solamente
		ValueModel tipoEntitaValueModel = new ValueHolder(TipoEntita.CLIENTE);
		DefaultFieldMetadata tipoEntitaMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipoEntitaValueModel), TipoEntita.class, true, null);
		getFormModel().add("tipoEntita", tipoEntitaValueModel, tipoEntitaMetaData);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form della vendite bene");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		builder.add(VenditaBene.PROP_DATA_VENDITA, "colSpan=1 align=left");
		((JTextField) builder.add(VenditaBene.PROP_NUMERO_PROTOCOLLO_VENDITA, "colSpan=1 align=left")[1])
				.setColumns(10);
		builder.row();
		SearchPanel searchPanelCliente = (SearchPanel) builder.add(bf.createBoundSearchText("cliente", new String[] {
				"codice", "anagrafica.denominazione" }, new String[] { "tipoEntita" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY }), "colSpan=1 align=left")[1];
		searchPanelCliente.getTextFields().get("codice").setColumns(10);
		builder.row();
		((JTextField) builder.add(VenditaBene.PROP_IMPORTO_FATTURA_VENDITA, "colSpan=1 align=left")[1]).setColumns(10);
		builder.add(VenditaBene.PROP_DATA_FATTURA_VENDITA, "align=left");
		builder.row();
		jcValoreBene = (JTextField) builder.add(VenditaBene.PROP_VALORE_BENE, "colSpan=1 align=left")[1];
		jcValoreBene.setColumns(10);
		jcValoreFondo = (JTextField) builder.add(VenditaBene.PROP_VALORE_FONDO, "colSpan=1 align=left")[1];
		jcValoreFondo.setColumns(10);
		builder.row();
		((JTextField) builder.add(VenditaBene.PROP_IMPORTO_STORNO_VALORE_BENE, "colSpan=1 align=left")[1])
				.setColumns(10);
		builder.row();
		((JTextField) builder.add(VenditaBene.PROP_VALORE_VENDITA, "colSpan=1 align=left")[1]).setColumns(10);
		builder.row();
		((JTextField) builder.add(VenditaBene.PROP_IMPORTO_VENDITA, "colSpan=1 align=left")[1]).setColumns(10);
		builder.row();
		((JTextField) builder.add(VenditaBene.PROP_PLUS_MINUS_VALORE, "colSpan=1 align=left")[1]).setColumns(10);
		builder.row();
		((JTextField) builder.add(VenditaBene.PROP_IMPORTO_STORNO_FONDO_AMMORTAMENTO, "colSpan=1 align=left")[1])
				.setColumns(10);
		builder.row();
		SearchPanel searchPanelTipologiaEliminazione = (SearchPanel) builder.add(bf.createBoundSearchText(
				"tipologiaEliminazione", new String[] { TipologiaEliminazione.PROP_DESCRIZIONE }),
				"colSpan=1 align=left")[1];
		searchPanelTipologiaEliminazione.getTextFields().get(TipologiaEliminazione.PROP_DESCRIZIONE).setColumns(20);
		builder.add(VenditaBene.PROP_VENDITA_TOTALE);
		builder.row();
		getFormModel().getValueModel(VenditaBene.PROP_IMPORTO_STORNO_VALORE_BENE).addValueChangeListener(
				new PluMinusValorePropertyChangeListener());
		getFormModel().getValueModel(VenditaBene.PROP_IMPORTO_STORNO_FONDO_AMMORTAMENTO).addValueChangeListener(
				new PluMinusValorePropertyChangeListener());
		getFormModel().getValueModel(VenditaBene.PROP_VALORE_VENDITA).addValueChangeListener(
				new PluMinusValorePropertyChangeListener());
		return builder.getForm();
	}

	@Override
	protected Object createNewObject() {
		VenditaBene venditaBene = beniAmmortizzabiliBD.creaNuovaVenditaBene(beneAmmortizzabile);
		return venditaBene;
	}

	/**
	 * Disabilita tutti i campi calcolati.
	 */
	private void disabilitaCampiCalcolati() {
		if (jcValoreBene != null) {
			jcValoreBene.setEnabled(false);
		}
		if (jcValoreFondo != null) {
			jcValoreFondo.setEnabled(false);
		}
	}

	/**
	 * @param beneAmmortizzabile
	 *            the beneAmmortizzabile to set
	 */
	public void setBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		this.beneAmmortizzabile = beneAmmortizzabile;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		disabilitaCampiCalcolati();
	}

}
