package it.eurotn.panjea.bi.export.jasper;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBILayout;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.FieldBILayout;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;

import java.util.List;

import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabDataset;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;

public class CrossTableGeneratorComponent extends AbstractAnalisiGeneratorComponent {

	// public static final CalculationEnum[] FUNZIONI_AGGREGAZIONE = new CalculationEnum[] { CalculationEnum.SUM,
	// CalculationEnum.HIGHEST, CalculationEnum.LOWEST, CalculationEnum.AVERAGE, CalculationEnum.VARIANCE,
	// CalculationEnum.STANDARD_DEVIATION, CalculationEnum.COUNT };

	private static final int CELL_HEIGHT = 28;

	/**
	 * Costruttore
	 *
	 * @param analisiBi
	 *            analisi
	 * @param jd
	 *            design jasper
	 */
	public CrossTableGeneratorComponent(final AnalisiBi analisiBi, final JasperDesign jd) {
		super(analisiBi, jd);
	}

	private JRDesignTextField createField(JRDesignExpression exp, int x, int y, int w, int h, String styleName,
			String format) {
		JRDesignTextField element = new JRDesignTextField();
		element.setX(x);
		element.setY(y);
		element.setWidth(w);
		element.setHeight(h);
		element.setExpression(exp);
		element.setPattern(format);
		applyStyle(styleName, element);
		return element;
	}

	@Override
	public JRDesignElement createReportElement() {
		int cWidth = 0;
		int cHeight = CELL_HEIGHT;
		JRDesignCrosstab crosstab = new JRDesignCrosstab();

		((JRDesignCrosstabDataset) crosstab.getDataset()).setDatasetRun(datasetRun);

		int x = 0;

		try {
			JRDesignCellContents titleContent = new JRDesignCellContents();
			JRDesignStaticText titleLabel = new JRDesignStaticText();
			titleLabel.setText(analisiBi.getNome());
			titleLabel.setStretchType(StretchTypeEnum.RELATIVE_TO_BAND_HEIGHT);
			titleContent.addElement(titleLabel);
			crosstab.setHeaderCell(titleContent);

			// Creo la cella per la misura. Se ci sono più misure aggiungo alla stessa cella
			x = 0;
			JRDesignCrosstabCell cellMeasure = new JRDesignCrosstabCell();
			cellMeasure.setHeight(CELL_HEIGHT);
			cellMeasure.setContents(new JRDesignCellContents());
			crosstab.addCell(cellMeasure);
			cellMeasure.setWidth(0);
			Colonna colonna = null;
			AnalisiBILayout layout = analisiBi.getAnalisiLayout();
			x = 0;
			for (FieldBILayout fieldBILayout : layout.getMisure()) {
				colonna = fieldBILayout.getColonna();
				int width = fieldBILayout.getWidth();
				JRDesignCrosstabMeasure measure = new JRDesignCrosstabMeasure();
				measure.setName(colonna.getNome());
				JRDesignExpression measureExp = new JRDesignExpression();
				measureExp.setText("$F{" + colonna.getNome() + "}");
				measure.setValueExpression(measureExp);
				measure = setCalculationToMeasure(measure, "");
				measure.setValueClassName(JasperReportUtils.getJRFieldType(colonna.getColumnClass()).getName());
				crosstab.addMeasure(measure);
				cellMeasure.setWidth(cellMeasure.getWidth() + width);
				JRDesignTextField field = createField(createExpression("$V{" + measure.getName() + "}"), x, 0, width,
						CELL_HEIGHT, "Crosstab Data Text", getFormat(colonna));
				if (isNumeric(colonna)) {
					field.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
				}
				((JRDesignCellContents) cellMeasure.getContents()).addElement(field);
				JRStyle contentStyle = jd.getStylesMap().get("crosstable_Data_Text_Box");
				((JRDesignCellContents) cellMeasure.getContents()).setStyle(contentStyle);
				x += width;
			}

			// Aggiungo le righe
			x = 0;
			boolean aggiungiTotale = layout.isTotalForRow();
			for (FieldBILayout fielsBiLayout : layout.getRighe()) {
				JRDesignCrosstabRowGroup rowGroup = new JRDesignCrosstabRowGroup();
				colonna = fielsBiLayout.getColonna();
				int width = fielsBiLayout.getWidth();
				rowGroup.setName(fielsBiLayout.getName());
				rowGroup.setWidth(width);
				// rowGroup.setPosition(CrosstabRowPositionEnum.STRETCH);
				rowGroup.setTotalPosition(CrosstabTotalPositionEnum.NONE);
				if (aggiungiTotale) {
					rowGroup.setTotalPosition(CrosstabTotalPositionEnum.START);
				}
				// devo creare i subtotali in base al field precedente quindi aggiorno la variabile adesso per
				// l'eventuale field successivo
				aggiungiTotale = fielsBiLayout.getSubtotalForRow() != null;
				JRDesignCrosstabBucket bucket = new JRDesignCrosstabBucket();
				JRDesignExpression rowExp = new JRDesignExpression();
				rowExp.setText("$F{" + colonna.getNome() + "}");
				bucket.setExpression(rowExp);
				bucket.setValueClassName(JasperReportUtils.getJRFieldType(colonna.getColumnClass()).getName());
				rowGroup.setBucket(bucket);
				crosstab.addRowGroup(rowGroup);

				JRDesignTextField field = createField(createExpression("$V{" + rowGroup.getName() + "}"), 0, 0, width,
						CELL_HEIGHT, "Crosstab Data Text", getFormat(colonna));
				if (isNumeric(colonna)) {
					field.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
				}
				((JRDesignCellContents) rowGroup.getHeader()).addElement(field);
				JRStyle headerStyle = jd.getStylesMap().get("crosstable_TH_box");
				((JRDesignCellContents) rowGroup.getHeader()).setStyle(headerStyle);
				cWidth += width;
				x += width;
			}

			x = 0;
			aggiungiTotale = layout.isTotalForColumn();
			for (FieldBILayout fielsBiLayout : layout.getColonne()) {
				JRDesignCrosstabColumnGroup columnGroup = new JRDesignCrosstabColumnGroup();
				colonna = fielsBiLayout.getColonna();
				columnGroup.setName(fielsBiLayout.getName());
				columnGroup.setHeight(CELL_HEIGHT);
				// columnGroup.setPosition(CrosstabColumnPositionEnum.STRETCH);

				columnGroup.setTotalPosition(CrosstabTotalPositionEnum.NONE);
				if (aggiungiTotale) {
					columnGroup.setTotalPosition(CrosstabTotalPositionEnum.START);
				}
				aggiungiTotale = fielsBiLayout.getSubtotalForColumn() != null;

				int width = fielsBiLayout.getWidth();
				JRDesignCrosstabBucket bucketCol = new JRDesignCrosstabBucket();
				JRDesignExpression colExp = new JRDesignExpression();
				colExp.setText("$F{" + colonna.getNome() + "}");
				bucketCol.setExpression(colExp);
				bucketCol.setValueClassName(JasperReportUtils.getJRFieldType(colonna.getColumnClass()).getName());
				columnGroup.setBucket(bucketCol);
				crosstab.addColumnGroup(columnGroup);
				// la larghezza della colonna è la larghezza della cella delle misure
				JRDesignCellContents headerCell = ((JRDesignCellContents) columnGroup.getHeader());
				headerCell.addElement(createField(createExpression("$V{" + columnGroup.getName() + "}"), 0, 0,
						cellMeasure.getWidth(), CELL_HEIGHT, "Crosstab Data Text", getFormat(colonna)));

				// Aggiungo il nome delle misure alla caption dell'ultima colonna
				// solamente se ho più misure.
				if (analisiBi.getAnalisiLayout().getMisure().size() > 1) {
					if (fielsBiLayout.getName().equals(
							layout.getColonne().get(layout.getColonne().size() - 1).getName())) {
						columnGroup.setHeight(CELL_HEIGHT * 2);
						x = 0;
						for (FieldBILayout fieldMisura : layout.getMisure()) {
							colonna = fieldMisura.getColonna();
							JRDesignStaticText headerMisura = new JRDesignStaticText();
							headerMisura.setText(fieldMisura.getTitle());
							headerMisura.setHeight(CELL_HEIGHT);
							headerMisura.setX(x);
							headerMisura.setY(CELL_HEIGHT);
							headerMisura.setWidth(fieldMisura.getWidth());
							headerMisura.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
							applyStyle("Crosstab Data Text", headerMisura);
							headerCell.addElement(headerMisura);
							x += fieldMisura.getWidth();
						}
					}
				}

				JRStyle headerStyle = jd.getStylesMap().get("crosstable_TH_box");
				((JRDesignCellContents) columnGroup.getHeader()).setStyle(headerStyle);
				cHeight += CELL_HEIGHT;
				cWidth += width;
			}

			// Aggiungo i subTotali per le colonne o righe che lo richiedono
			JRStyle headerStyle = jd.getStylesMap().get("crosstable_THT_box");
			List<JRCrosstabRowGroup> rows = crosstab.getRowGroupsList();
			List<JRCrosstabColumnGroup> cols = crosstab.getColumnGroupsList();
			for (int i = 0; i < rows.size(); ++i) {
				JRDesignCrosstabRowGroup row = (JRDesignCrosstabRowGroup) rows.get(i);
				JRDesignCrosstabCell rowCell = new JRDesignCrosstabCell();
				rowCell.setRowTotalGroup(row.getName());
				rowCell.setContents(new JRDesignCellContents());
				rowCell.setHeight(CELL_HEIGHT);
				cHeight += CELL_HEIGHT;
				x = 0;
				for (FieldBILayout fieldMisura : layout.getMisure()) {
					colonna = fieldMisura.getColonna();
					JRDesignTextField field = createField(createExpression("$V{" + colonna.getNome() + "}"), x, 0,
							fieldMisura.getWidth(), CELL_HEIGHT, "Crosstab Data Text", getFormat(colonna));
					field.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
					((JRDesignCellContents) rowCell.getContents()).addElement(field);
					x += fieldMisura.getWidth();
				}
				((JRDesignCellContents) rowCell.getContents()).setStyle(headerStyle);
				crosstab.addCell(rowCell);

				// HEADER CAPTION
				JRDesignStaticText elementTotalHeader = new JRDesignStaticText();
				elementTotalHeader.setText("Totale");
				elementTotalHeader.setHeight(CELL_HEIGHT);
				elementTotalHeader.setWidth(row.getWidth());
				elementTotalHeader.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
				applyStyle("Crosstab Data Text", elementTotalHeader);
				((JRDesignCellContents) row.getTotalHeader()).addElement(elementTotalHeader);
				((JRDesignCellContents) row.getTotalHeader()).setStyle(headerStyle);

				for (int j = 0; j < cols.size(); ++j) {
					JRDesignCrosstabColumnGroup col = (JRDesignCrosstabColumnGroup) cols.get(j);
					if (i == 0) {
						int width = 0;
						JRDesignCrosstabCell colCell = new JRDesignCrosstabCell();
						colCell.setColumnTotalGroup(col.getName());
						colCell.setContents(new JRDesignCellContents());
						x = 0;
						for (FieldBILayout fieldMisura : layout.getMisure()) {
							colonna = fieldMisura.getColonna();
							width += fieldMisura.getWidth();
							cWidth += width;
							JRDesignTextField field = createField(createExpression("$V{" + colonna.getNome() + "}"), x,
									0, fieldMisura.getWidth(), CELL_HEIGHT, "Crosstab Data Text", getFormat(colonna));
							field.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
							((JRDesignCellContents) colCell.getContents()).addElement(field);
							x += fieldMisura.getWidth();
						}
						((JRDesignCellContents) colCell.getContents()).setStyle(headerStyle);
						crosstab.addCell(colCell);
					}
					JRDesignCrosstabCell subtotalCell = new JRDesignCrosstabCell();
					subtotalCell.setRowTotalGroup(row.getName());
					subtotalCell.setColumnTotalGroup(col.getName());
					subtotalCell.setContents(new JRDesignCellContents());
					int width = 0;
					x = 0;
					for (FieldBILayout fieldMisura : layout.getMisure()) {
						colonna = fieldMisura.getColonna();
						width += fieldMisura.getWidth();
						JRDesignTextField field = createField(createExpression("$V{" + colonna.getNome() + "}"), x, 0,
								fieldMisura.getWidth(), CELL_HEIGHT, "Crosstab Data Text", getFormat(colonna));
						field.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
						((JRDesignCellContents) subtotalCell.getContents()).addElement(field);
						x += fieldMisura.getWidth();
					}
					((JRDesignCellContents) subtotalCell.getContents()).setStyle(headerStyle);
					crosstab.addCell(subtotalCell);

					// HEADER CAPTION
					elementTotalHeader = new JRDesignStaticText();
					elementTotalHeader.setText("Totale ");
					elementTotalHeader.setHeight(CELL_HEIGHT);
					elementTotalHeader.setWidth(width);
					elementTotalHeader.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
					applyStyle("Crosstab Data Text", elementTotalHeader);
					((JRDesignCellContents) col.getTotalHeader()).addElement(elementTotalHeader);
					((JRDesignCellContents) col.getTotalHeader()).setStyle(headerStyle);
				}
			}
			crosstab.setHeight(cHeight);
			crosstab.setWidth(cWidth);
			crosstab.setPositionType(PositionTypeEnum.FLOAT);
		} catch (JRException e) {
			throw new RuntimeException(e);
		}
		return crosstab;
	}

	private JRDesignCrosstabMeasure setCalculationToMeasure(JRDesignCrosstabMeasure measure, String nomeMisura) {
		// Verifico se la misura è in colonne o righe
		// FieldReport fieldReport = docLayout.getFields(nomeMisura);
		// CalculationEnum calculationAction = CalculationEnum.SUM;
		// if (fieldReport.getSubTotalTypeForColumn().length > 0) {
		// calculationAction = fieldReport.getSubTotalTypeForColumn()[0];
		// }
		// measure.setCalculation(calculationAction);
		measure.setCalculation(CalculationEnum.SUM);
		return measure;
	}
}
