package it.eurotn.panjea.bi.export.jasper;

import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.FieldBILayout;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.CalculationEnum;

import org.apache.commons.lang3.StringUtils;

/**
 * Inserisce il componente con i dati dell'analisi.
 *
 * @author giangi
 * @version 1.0, 15/apr/2014
 *
 */
public abstract class AbstractAnalisiGeneratorComponent {

	public static final List<Class<?>> NUMBER_CLASS;

	public static final CalculationEnum[] FUNZIONI_AGGREGAZIONE = new CalculationEnum[] { CalculationEnum.SUM,
		CalculationEnum.HIGHEST, CalculationEnum.LOWEST, CalculationEnum.AVERAGE, CalculationEnum.VARIANCE,
		CalculationEnum.STANDARD_DEVIATION, CalculationEnum.COUNT };

	private Map<String, Colonna> colonneCache = new HashMap<String, Colonna>();

	static {

		NUMBER_CLASS = new ArrayList<Class<?>>();
		NUMBER_CLASS.add(Double.class);
		NUMBER_CLASS.add(Float.class);
		NUMBER_CLASS.add(Integer.class);
		NUMBER_CLASS.add(Long.class);
		NUMBER_CLASS.add(Short.class);
		NUMBER_CLASS.add(BigDecimal.class);

	}

	/**
	 * Metodo di factory per craere i generatori di componente dipendenti all'analisi
	 *
	 * @param analisiBi
	 *            analisi
	 * @param jd
	 *            design jasper
	 * @return generatore per il componente
	 */
	public static AbstractAnalisiGeneratorComponent createGenerator(final AnalisiBi analisiBi, final JasperDesign jd) {
		if (analisiBi.getAnalisiLayout().getColonne().size() == 0) {
			return new TableGeneratorComponent(analisiBi, jd);
		}
		return new CrossTableGeneratorComponent(analisiBi, jd);
	}

	protected AnalisiBi analisiBi;

	protected JasperDesign jd;

	protected TableLayout pivotLayout;

	protected JRDesignDataset dataset;

	protected JRDesignDatasetRun datasetRun;

	/**
	 * Costruttore
	 *
	 * @param analisiBi
	 *            analisi
	 * @param jd
	 *            design jasper
	 */
	public AbstractAnalisiGeneratorComponent(final AnalisiBi analisiBi, final JasperDesign jd) {
		super();
		this.analisiBi = analisiBi;
		// this.model = model;
		this.jd = jd;
		// configuro il dataSet
		configuraDataset();
	}

	/**
	 * Applica lo stile alla se presente
	 *
	 * @param styleName
	 *            nome dello stile
	 * @param component
	 *            cella
	 */
	protected void applyStyle(String styleName, DesignCell component) {
		JRStyle tableStyle = jd.getStylesMap().get(styleName);
		if (tableStyle != null) {
			component.setStyle(tableStyle);
		}
	}

	/**
	 * Applica lo stile al componente se presente
	 *
	 * @param styleName
	 *            nome dello stile
	 * @param component
	 *            componente
	 */
	protected void applyStyle(String styleName, JRDesignElement component) {
		JRStyle tableStyle = jd.getStylesMap().get(styleName);
		if (tableStyle != null) {
			component.setStyle(tableStyle);
		}
	}

	/**
	 *
	 * @param componentImpl
	 *            componente
	 * @return dataset con i campi e query
	 */
	protected void configuraDataset() {
		try {
			dataset = new JRDesignDataset(false);
			datasetRun = new JRDesignDatasetRun();
			// CONFIGURO IL DATASET
			dataset.setName(analisiBi.getNomeSafe() + "dataSet");
			JRDesignQuery query = new JRDesignQuery();
			AnalisiBIReportSqlGenerator sqlGenerator = new AnalisiBIReportSqlGenerator(analisiBi);
			query.setText(sqlGenerator.buildSql());
			dataset.setQuery(query);
			JRDesignField field = null;

			for (FieldBILayout fieldLayout : analisiBi.getAnalisiLayout().getFields().values()) {
				Colonna colonna = fieldLayout.getColonna();
				field = new JRDesignField();
				field.setName(colonna.getNome());
				field.setValueClass(JasperReportUtils.getJRFieldType(colonna.getColumnClass()));
				dataset.addField(field);
				// Per adesso raggruppo solamente il subTotale sulle colonne
				if (fieldLayout.getSubtotalForColumn() != null) {
					JRDesignGroup group = new JRDesignGroup();
					group.setName(colonna.getKey());
					group.setExpression(createExpression("$F{" + colonna.getNome() + "}"));
					dataset.addGroup(group);
				}
			}

			// Aggiungo i parametri
			for (String inputControl : analisiBi.getInputControls()) {
				JRDesignParameter parameter = new JRDesignParameter();
				parameter.setName(inputControl);
				parameter.setValueClass(java.util.List.class);
				dataset.addParameter(parameter);
			}

			// CONFIGURO IL DATASETRUN
			datasetRun.setDatasetName(dataset.getName());
			JRDesignExpression exp = new JRDesignExpression();
			exp.setText("$P{REPORT_CONNECTION}");
			datasetRun.setConnectionExpression(exp);
			for (String inputControl : analisiBi.getInputControls()) {
				JRDesignDatasetParameter parameter = new JRDesignDatasetParameter();
				parameter.setName(inputControl);
				parameter.setExpression(createExpression("$P{" + inputControl + "}"));
				datasetRun.addParameter(parameter);
			}

			try {
				jd.addDataset(dataset);
			} catch (JRException e) {
				throw new RuntimeException("Errore nell'aggiungere il dataset all'esportazione dell'analisi "
						+ analisiBi.getNome(), e);
			}
		} catch (JRException e) {
			throw new RuntimeException("Errore nel generare il dataset", e);
		}
	}

	/**
	 *
	 * @param className
	 *            classname
	 * @param text
	 *            testo
	 * @return design expression
	 */
	protected JRDesignExpression createExpression(String text) {
		if (text == null || text.trim().length() == 0) {
			return null;
		}
		JRDesignExpression exp = new JRDesignExpression();
		if (text.toLowerCase().contains("mese")) {
			text = new StringBuilder(200).append("new it.eurotn.comparator.jasperreport.MeseComparator().getName(")
					.append(text).append(")").toString();
		}
		exp.setText(text);
		return exp;
	}

	/**
	 * Aggiunge il componente che descrive l'analisi al report.
	 *
	 * @return elemento creato da aggiungere al report
	 */
	public abstract JRDesignElement createReportElement();

	protected String getFormat(Colonna colonna) {
		StringBuilder pattern = new StringBuilder(50);
		if (colonna.isSeparatorVisible()) {
			pattern.append("#,##0");
		} else {
			pattern.append("0");
		}
		int numDecimali = colonna.getNumeroDecimali();
		if (numDecimali > 0) {
			pattern.append(".");
			pattern.append(StringUtils.repeat("0", numDecimali));
		}
		return pattern.toString();
	}

	protected boolean isNumeric(Colonna colonna) {
		Class<?> clazz = colonna.getColumnClass();
		Class<?> clazzWrapper = JasperReportUtils.getJRFieldType(clazz);
		if (clazzWrapper != null) {
			clazz = clazzWrapper;
		}
		return NUMBER_CLASS.contains(clazz);
	}
}
