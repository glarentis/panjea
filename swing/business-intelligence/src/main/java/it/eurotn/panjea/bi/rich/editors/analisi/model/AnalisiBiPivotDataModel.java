package it.eurotn.panjea.bi.rich.editors.analisi.model;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIDomain;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasure;
import it.eurotn.panjea.bi.rich.editors.analisi.converter.NumberPivotContext;
import it.eurotn.panjea.bi.rich.editors.analisi.converter.TipoArticoloConverter;
import it.eurotn.panjea.bi.rich.editors.analisi.converter.TipoEntitaConverter;
import it.eurotn.panjea.bi.rich.editors.analisi.style.PivotCellStyleProvider;

import java.math.BigDecimal;
import java.util.Date;

import com.jidesoft.pivot.CalculatedPivotDataModel;
import com.jidesoft.pivot.PivotDataSource;
import com.jidesoft.pivot.PivotField;

public class AnalisiBiPivotDataModel extends CalculatedPivotDataModel {

	/**
	 * Costruttore.
	 *
	 * @param paramPivotDataSource
	 *            dataSource per il pivotDataModel
	 */
	public AnalisiBiPivotDataModel(final PivotDataSource paramPivotDataSource) {
		super(paramPivotDataSource);
		setValueImmutable(true);
		setCellStyleProvider(new PivotCellStyleProvider());
		setAutoUpdate(false);
		setHideRedundantSubtotal(true);
		initFields();
	}

	/**
	 * Aggiorna tutti i fields del model.
	 */
	private void initFields() {
		for (PivotField field : getFields()) {
			// per i campi data permetto il filtro custom
			field.setCustomFilterAllowed(field.getType() == Date.class);
			field.setPreferSelectedPossibleValues(true);
			if (field.getType() == int.class || field.getType() == double.class || field.getType() == BigDecimal.class) {
				Colonna colonna = AnalisiBIDomain.getColonna(field.getName());

				// controllo se il numero decimali qta da considerare è quello specifico della colonna o quello degli
				// articoli
				int numDecimaliQta = colonna.getNumeroDecimali();
				if (colonna.isDecimaliDaArticoli()) {
					numDecimaliQta = ((AnalisiBiDataSource) getDataSource()).getNumDecimaliQtaMax();
				}
				field.setConverterContext(new NumberPivotContext(numDecimaliQta, colonna.isSeparatorVisible(), colonna
						.getPreFisso()));

				boolean misureAllowed = (colonna instanceof ColumnMeasure);
				field.setAllowedAsDataField(misureAllowed);
				field.setAllowedAsColumnField(!misureAllowed);
				field.setAllowedAsRowField(!misureAllowed);
				field.setPreferSelectedPossibleValues(true);
				field.setCustomFilterAllowed(true);
			}
		}

		// Custom
		// for (PivotField f : getFields()) {
		// System.out.println(f.getName());
		// }

		PivotField tipoEntita = getField("entità_Tipo_Ent_");
		tipoEntita.setConverterContext(TipoEntitaConverter.CONTEXT_TIPO_ENTITA);

		PivotField tipoArticolo = getField("articoli_Tipo_art_");
		tipoArticolo.setConverterContext(TipoArticoloConverter.CONTEXT_TIPO_ARTICOLO);

		PivotField meseField = getField("data_Mese");
		meseField.setConverterContext(it.eurotn.panjea.rich.converter.MeseConverter.CONTEXT);
	}
}
