package it.eurotn.panjea.bi.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Contiene i paprametri per la creazione del grafico dell'analisi del datawarehouse.
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "bi_analisi_chart")
@Deprecated
public class AnalisiChartParametri extends EntityBase {

	/**
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum ChartType {
		XY_CHART, PIE_CHART
	}

	private static final long serialVersionUID = -5635269040867795185L;

	private ChartType chartType;

	private String chartTitle;

	private boolean showTitle;

	private boolean chart3D;

	private String rendererQuantita;

	private String axisQuantitaTitle;

	private String rendererImporto;

	private String axisImportoTitle;

	private String rendererCount;

	private String axisCountTitle;

	private String axisCategoryTitle;

	private String colorSerie1;

	private String colorSerie2;

	private String colorSerie3;

	private String colorSerie4;

	private String colorSerie5;

	private String colorSerie6;

	private String colorSerie7;

	private String colorSerie8;

	private String colorSerie9;

	private String colorSerie10;

	private String colorSerie11;

	private String colorSerie12;

	/**
	 * Costruttore di default.
	 */
	public AnalisiChartParametri() {
		super();
		initialize();
	}

	/**
	 * @return the axisCategoryTitle
	 */
	public String getAxisCategoryTitle() {
		return axisCategoryTitle;
	}

	/**
	 * @return the axisCountTitle
	 */
	public String getAxisCountTitle() {
		return axisCountTitle;
	}

	/**
	 * @return the axisImportoTitle
	 */
	public String getAxisImportoTitle() {
		return axisImportoTitle;
	}

	/**
	 * @return the axisQuantitaTitle
	 */
	public String getAxisQuantitaTitle() {
		return axisQuantitaTitle;
	}

	/**
	 * @return the chartTitle
	 */
	public String getChartTitle() {
		return chartTitle;
	}

	/**
	 * @return the chatType
	 */
	public ChartType getChartType() {
		return chartType;
	}

	/**
	 * @return the colorSerie1
	 */
	public String getColorSerie1() {
		return colorSerie1;
	}

	/**
	 * @return the colorSerie10
	 */
	public String getColorSerie10() {
		return colorSerie10;
	}

	/**
	 * @return the colorSerie11
	 */
	public String getColorSerie11() {
		return colorSerie11;
	}

	/**
	 * @return the colorSerie12
	 */
	public String getColorSerie12() {
		return colorSerie12;
	}

	/**
	 * @return the colorSerie2
	 */
	public String getColorSerie2() {
		return colorSerie2;
	}

	/**
	 * @return the colorSerie3
	 */
	public String getColorSerie3() {
		return colorSerie3;
	}

	/**
	 * @return the colorSerie4
	 */
	public String getColorSerie4() {
		return colorSerie4;
	}

	/**
	 * @return the colorSerie5
	 */
	public String getColorSerie5() {
		return colorSerie5;
	}

	/**
	 * @return the colorSerie6
	 */
	public String getColorSerie6() {
		return colorSerie6;
	}

	/**
	 * @return the colorSerie7
	 */
	public String getColorSerie7() {
		return colorSerie7;
	}

	/**
	 * @return the colorSerie8
	 */
	public String getColorSerie8() {
		return colorSerie8;
	}

	/**
	 * @return the colorSerie9
	 */
	public String getColorSerie9() {
		return colorSerie9;
	}

	/**
	 * @return the rendererCount
	 */
	public String getRendererCount() {
		return rendererCount;
	}

	/**
	 * @return the rendererImporto
	 */
	public String getRendererImporto() {
		return rendererImporto;
	}

	/**
	 * @return the rendererQuantità
	 */
	public String getRendererQuantita() {
		return rendererQuantita;
	}

	/**
	 * Inizialitta i valori di default.
	 */
	private void initialize() {
		this.chartType = ChartType.XY_CHART;
		this.chartTitle = "Grafico";
		this.showTitle = false;
		this.chart3D = false;

		this.rendererCount = "org.jfree.chart.renderer.category.BarRenderer";
		this.axisCountTitle = "Numero";

		this.rendererImporto = "org.jfree.chart.renderer.category.LineAndShapeRenderer";
		this.axisImportoTitle = "Importo";

		this.rendererQuantita = "org.jfree.chart.renderer.category.BarRenderer";
		this.axisQuantitaTitle = "Quantità";

		this.axisCategoryTitle = "Categorie";

		this.colorSerie1 = "#FF0000";
		this.colorSerie2 = "#FF9900";
		this.colorSerie3 = "#99CC00";
		this.colorSerie4 = "#339966";
		this.colorSerie5 = "#33CCCC";
		this.colorSerie6 = "#3366FF";

		this.colorSerie7 = "#800080";
		this.colorSerie8 = "#FF00FF";
		this.colorSerie9 = "#FFCC99";
		this.colorSerie10 = "#CCFFFF";
		this.colorSerie11 = "#CC99FF";
		this.colorSerie12 = "#993300";
	}

	/**
	 * @return the chart3D
	 */
	public boolean isChart3D() {
		return chart3D;
	}

	/**
	 * @return the showTitle
	 */
	public boolean isShowTitle() {
		return showTitle;
	}

	/**
	 * @param axisCategoryTitle
	 *            the axisCategoryTitle to set
	 */
	public void setAxisCategoryTitle(String axisCategoryTitle) {
		this.axisCategoryTitle = axisCategoryTitle;
	}

	/**
	 * @param axisCountTitle
	 *            the axisCountTitle to set
	 */
	public void setAxisCountTitle(String axisCountTitle) {
		this.axisCountTitle = axisCountTitle;
	}

	/**
	 * @param axisImportoTitle
	 *            the axisImportoTitle to set
	 */
	public void setAxisImportoTitle(String axisImportoTitle) {
		this.axisImportoTitle = axisImportoTitle;
	}

	/**
	 * @param axisQuantitaTitle
	 *            the axisQuantitaTitle to set
	 */
	public void setAxisQuantitaTitle(String axisQuantitaTitle) {
		this.axisQuantitaTitle = axisQuantitaTitle;
	}

	/**
	 * @param chart3d
	 *            the chart3D to set
	 */
	public void setChart3D(boolean chart3d) {
		chart3D = chart3d;
	}

	/**
	 * @param chartTitle
	 *            the chartTitle to set
	 */
	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	/**
	 * @param chartType
	 *            the chatType to set
	 */
	public void setChartType(ChartType chartType) {
		this.chartType = chartType;
	}

	/**
	 * @param colorSerie1
	 *            the colorSerie1 to set
	 */
	public void setColorSerie1(String colorSerie1) {
		this.colorSerie1 = colorSerie1;
	}

	/**
	 * @param colorSerie10
	 *            the colorSerie10 to set
	 */
	public void setColorSerie10(String colorSerie10) {
		this.colorSerie10 = colorSerie10;
	}

	/**
	 * @param colorSerie11
	 *            the colorSerie11 to set
	 */
	public void setColorSerie11(String colorSerie11) {
		this.colorSerie11 = colorSerie11;
	}

	/**
	 * @param colorSerie12
	 *            the colorSerie12 to set
	 */
	public void setColorSerie12(String colorSerie12) {
		this.colorSerie12 = colorSerie12;
	}

	/**
	 * @param colorSerie2
	 *            the colorSerie2 to set
	 */
	public void setColorSerie2(String colorSerie2) {
		this.colorSerie2 = colorSerie2;
	}

	/**
	 * @param colorSerie3
	 *            the colorSerie3 to set
	 */
	public void setColorSerie3(String colorSerie3) {
		this.colorSerie3 = colorSerie3;
	}

	/**
	 * @param colorSerie4
	 *            the colorSerie4 to set
	 */
	public void setColorSerie4(String colorSerie4) {
		this.colorSerie4 = colorSerie4;
	}

	/**
	 * @param colorSerie5
	 *            the colorSerie5 to set
	 */
	public void setColorSerie5(String colorSerie5) {
		this.colorSerie5 = colorSerie5;
	}

	/**
	 * @param colorSerie6
	 *            the colorSerie6 to set
	 */
	public void setColorSerie6(String colorSerie6) {
		this.colorSerie6 = colorSerie6;
	}

	/**
	 * @param colorSerie7
	 *            the colorSerie7 to set
	 */
	public void setColorSerie7(String colorSerie7) {
		this.colorSerie7 = colorSerie7;
	}

	/**
	 * @param colorSerie8
	 *            the colorSerie8 to set
	 */
	public void setColorSerie8(String colorSerie8) {
		this.colorSerie8 = colorSerie8;
	}

	/**
	 * @param colorSerie9
	 *            the colorSerie9 to set
	 */
	public void setColorSerie9(String colorSerie9) {
		this.colorSerie9 = colorSerie9;
	}

	/**
	 * @param rendererCount
	 *            the rendererCount to set
	 */
	public void setRendererCount(String rendererCount) {
		this.rendererCount = rendererCount;
	}

	/**
	 * @param rendererImporto
	 *            the rendererImporto to set
	 */
	public void setRendererImporto(String rendererImporto) {
		this.rendererImporto = rendererImporto;
	}

	/**
	 * @param rendererQuantita
	 *            the rendererQuantità to set
	 */
	public void setRendererQuantita(String rendererQuantita) {
		this.rendererQuantita = rendererQuantita;
	}

	/**
	 * @param showTitle
	 *            the showTitle to set
	 */
	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

}
