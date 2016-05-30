/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.ritenuteacconto.certificazioni;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author adriano
 * @version 1.0, 04/dic/06
 * 
 */
public class ReportCertificazioniCompensiForm extends PanjeaAbstractForm {

	private class AnnoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			Integer anno = (Integer) evt.getNewValue();

			Date dataCertif = ((ParametriCreazioneCertificazioniCompensi) getFormModel().getFormObject())
					.getDataCertificazione();

			if (anno != null && anno > 999) {
				Calendar calendar = Calendar.getInstance();
				if (dataCertif != null) {
					calendar.setTime(dataCertif);
				}
				// setto l'anno a +1
				calendar.set(Calendar.YEAR, anno + 1);
				getFormModel().getValueModel("dataCertificazione").setValue(calendar.getTime());
			}

		}

	}

	private static final String FORM_ID = "reportCertificazioniCompensiForm";

	private AnnoChangeListener annoChangeListener;

	/**
	 * Costruttore di default.
	 */
	public ReportCertificazioniCompensiForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriCreazioneCertificazioniCompensi(), false, FORM_ID),
				FORM_ID);

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
		SearchPanel searchPanelFornitore = (SearchPanel) builder.add(bf.createBoundSearchText("entita", new String[] {
				"codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaFornitori" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class))[1];
		searchPanelFornitore.getTextFields().get("codice").setColumns(5);
		builder.row();
		builder.add("dataCertificazione", "align=left colspan=2");

		annoChangeListener = new AnnoChangeListener();
		getFormModel().getValueModel("anno").addValueChangeListener(annoChangeListener);

		return builder.getForm();
	}

	@Override
	public void dispose() {
		getFormModel().getValueModel("anno").removeValueChangeListener(annoChangeListener);
		super.dispose();
	}

}
