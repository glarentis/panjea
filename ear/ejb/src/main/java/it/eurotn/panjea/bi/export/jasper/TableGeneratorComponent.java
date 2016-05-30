package it.eurotn.panjea.bi.export.jasper;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.FieldBILayout;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;

public class TableGeneratorComponent extends AbstractAnalisiGeneratorComponent {

	private StandardTable table;

	/**
	 * Costruttore
	 * 
	 * @param analisiBi
	 *            analisi
	 * @param jd
	 *            design jasper
	 */
	public TableGeneratorComponent(final AnalisiBi analisiBi, final JasperDesign jd) {
		super(analisiBi, jd);
	}

	/**
	 * 
	 * @param field
	 *            colonna del modello
	 * @param fieldBILayout
	 *            colonna con layout della jidetable
	 * @return colonna della tabella per il report
	 */
	public BaseColumn createColumn(Colonna field, FieldBILayout fieldBILayout) {
		StandardColumn column = new StandardColumn();

		// Column header
		DesignCell cell = new DesignCell();
		cell.setHeight(30);
		applyStyle("table_TH", cell);
		column.setWidth(fieldBILayout.getWidth());
		column.setColumnHeader(cell);
		if (field != null) {
			JRDesignStaticText text = new JRDesignStaticText(jd);
			text.setText(fieldBILayout.getTitle());
			text.setWidth(fieldBILayout.getWidth());
			text.setHeight(30);
			applyStyle("table_TH_text", text);
			cell.addElement(text);
		}
		// Details cell....
		cell = new DesignCell();
		cell.setHeight(30);
		applyStyle("table_TD", cell);
		column.setDetailCell(cell);
		column.setWidth(fieldBILayout.getWidth());

		if (field != null) {
			JRDesignTextField text = new JRDesignTextField(jd);
			text.setExpression(createExpression("$F{" + field.getNome() + "}"));
			text.setWidth(fieldBILayout.getWidth() - 5);
			text.setHeight(30);
			text.setX(5);
			text.setPattern(getFormat(field));
			applyStyle("table_TD_text", text);
			if (isNumeric(field)) {
				text.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
			}
			cell.addElement(text);
		}
		return column;
	}

	@Override
	public JRDesignElement createReportElement() {
		// Inserisco una tabella nella banda del titolo
		JRDesignComponentElement component = new JRDesignComponentElement();
		table = new StandardTable();
		table.setDatasetRun(datasetRun);
		int widthComponent = 0;
		// createRowColumns();
		// // Aggiungo le righe
		Colonna colonna = null;
		for (FieldBILayout fieldBILayout : analisiBi.getAnalisiLayout().getRighe()) {
			colonna = fieldBILayout.getColonna();
			BaseColumn designColumn = null;
			designColumn = createColumn(colonna, fieldBILayout);
			table.addColumn(designColumn);
			widthComponent += fieldBILayout.getWidth();
		}

		// Aggiungo le misure
		colonna = null;
		for (FieldBILayout fieldBILayout : analisiBi.getAnalisiLayout().getMisure()) {
			// Trovo la colonna nel modello relativo alla misura
			colonna = fieldBILayout.getColonna();
			BaseColumn column = createColumn(colonna, fieldBILayout);
			table.addColumn(column);
			widthComponent += fieldBILayout.getWidth();
		}

		component.setKey(analisiBi.getNomeSafe());
		component.setComponent(table);
		component.setComponentKey(new ComponentKey("http://jasperreports.sourceforge.net/jasperreports/components",
				"jr", "table"));
		component.setHeight(60);
		component.setWidth(widthComponent);

		applyStyle("table", component);

		return component;
	}

	// public void createRowColumns() {
	// Colonna colonna = null;
	// BaseColumn column = null;
	// for (String nomeColonna : analisiParametri.getRows()) {
	// FieldReport fieldReport = docLayout.getFields(nomeColonna);
	// if (fieldReport.getSubTotalTypeForRow().length > 0) {
	// colonna = getColonnaFromCaption(nomeColonna);
	// StandardColumnGroup columnGroup = new StandardColumnGroup();
	// columnGroup.setWidth(223);
	// DesignCell cell = new DesignCell();
	// cell.setHeight(30);
	// applyStyle("table_TH", cell);
	//
	// JRDesignTextField text = new JRDesignTextField(jd);
	// text.setExpression(createExpression("$F{" + colonna.getAliasCompleto() + "}"));
	// text.setWidth(fieldReport.getWidth() - 5);
	// text.setHeight(30);
	// text.setX(5);
	// text.setPattern(getFormat(colonna));
	//
	// applyStyle("table_TD_text", text);
	// if (isNumeric(colonna)) {
	// text.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
	// }
	// cell.addElement(text);
	// StandardGroupCell groupCell = new StandardGroupCell(colonna.getAliasWithoutQuote(), cell);
	// columnGroup.addGroupHeader(groupCell);
	// column = columnGroup;
	// for (String colonnaInGroup : analisiParametri.getRows()) {
	// columnGroup.addColumn(createColumn(getColonnaFromCaption(colonnaInGroup),
	// docLayout.getFields(colonnaInGroup)));
	// // StandardColumn columnMerge = new StandardColumn();
	// // columnMerge.setWidth(docLayout.getFields(colonnaInGroup).getWidth());
	// // column.setColumnHeader(cell);
	// // columnGroup.addColumn(columnMerge);
	// }
	// table.addColumn(column);
	// } else {
	// // column = createColumn(colonna, fieldReport);
	// }
	// }
	// }
}
