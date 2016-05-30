package it.eurotn.panjea.bi.manager;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.AnalisiValueSelected;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.domain.dashboard.DashBoardAnalisi;
import it.eurotn.panjea.bi.export.jasper.AbstractAnalisiGeneratorComponent;
import it.eurotn.panjea.bi.manager.interfaces.AnalisiBIManager;
import it.eurotn.panjea.bi.manager.interfaces.DashBoardManager;
import it.eurotn.panjea.bi.manager.interfaces.JasperReportExport;
import it.eurotn.panjea.magazzino.exception.AnalisiNonPresenteException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.JasperReportExport")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.JasperReportExport")
public class JasperReportExportBean implements JasperReportExport {
	@EJB
	private AnalisiBIManager analisiBIManager;
	@EJB
	private DashBoardManager dashBoardManager;

	protected String name;
	protected String language = "LANGUAGE_JAVA";
	protected int columnCount = 1;
	protected PrintOrderEnum printOrderValue = PrintOrderEnum.VERTICAL;
	protected RunDirectionEnum columnDirection = RunDirectionEnum.LTR;
	protected int pageWidth = 595;
	protected int pageHeight = 842;
	protected OrientationEnum orientationValue = OrientationEnum.PORTRAIT;
	protected WhenNoDataTypeEnum whenNoDataTypeValue = WhenNoDataTypeEnum.NO_PAGES;
	protected int columnWidth = 555;
	protected int columnSpacing;
	protected int leftMargin = 20;
	protected int rightMargin = 20;
	protected int topMargin = 30;
	protected int bottomMargin = 30;
	protected boolean isTitleNewPage;
	protected boolean isSummaryNewPage;
	protected boolean isSummaryWithPageHeaderAndFooter;
	protected boolean isFloatColumnFooter;
	protected boolean ignorePagination;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.bi.manager.JasperReportExport#creaJrxml(it.eurotn.panjea.bi.domain.analisi.AnalisiBi,
	 * java.lang.String)
	 */
	@Override
	public String creaJrxml(AnalisiBi analisiBi, String template) {
		String jrxml = "";
		try {
			List<AnalisiBi> analisiToExport = new ArrayList<AnalisiBi>();
			analisiToExport.add(analisiBi);
			JasperDesign des = getJasperDesign(analisiToExport, template, new HashMap<String, DashBoardAnalisi>(),
					null, null);
			JasperReport jasperReport = JasperCompileManager.compileReport(des);
			ByteArrayOutputStream jrxmlOut = new ByteArrayOutputStream(1500);
			JasperCompileManager.writeReportToXmlStream(jasperReport, jrxmlOut);
			jrxml = jrxmlOut.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jrxml;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.bi.manager.JasperReportExport#creaJrxml(it.eurotn.panjea.bi.domain.dashboard.DashBoard,
	 * java.lang.String)
	 */
	@Override
	public String creaJrxml(DashBoard dashBoard, String template) {
		String jrxml = "";
		try {
			List<AnalisiBi> analisiToExport = new ArrayList<AnalisiBi>();
			Map<String, DashBoardAnalisi> dashBoardAnalisiToExport = new HashMap<String, DashBoardAnalisi>();
			for (DashBoardAnalisi analisiToAdd : dashBoard.getAnalisi().values()) {
				try {
					AnalisiBi analisiBi;
					dashBoardAnalisiToExport.put(analisiToAdd.getNomeAnalisi() + analisiToAdd.getCategoriaAnalisi(),
							analisiToAdd);
					analisiBi = analisiBIManager.caricaAnalisi(analisiToAdd.getNomeAnalisi(),
							analisiToAdd.getCategoriaAnalisi());
					analisiToExport.add(analisiBi);
				} catch (AnalisiNonPresenteException e) {
					e.printStackTrace();
				}
			}
			JasperDesign des = getJasperDesign(analisiToExport, template, dashBoardAnalisiToExport,
					dashBoard.getWidth(), dashBoard.getHeight());
			JasperReport jasperReport = JasperCompileManager.compileReport(des);
			ByteArrayOutputStream jrxmlOut = new ByteArrayOutputStream(1500);
			JasperCompileManager.writeReportToXmlStream(jasperReport, jrxmlOut);
			jrxml = jrxmlOut.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return jrxml;
	}

	private JasperDesign getJasperDesign(List<AnalisiBi> listaAnalisi, String template,
			Map<String, DashBoardAnalisi> layoutDashboard, Integer width, Integer height) {
		// JasperDesign
		JasperDesign jd = null;
		try {
			InputStream templateStream = new ByteArrayInputStream(template.getBytes("UTF-8"));
			jd = JRXmlLoader.load(templateStream);
			JRDesignBand titleBand = (JRDesignBand) jd.getTitle();
			StringBuilder sbWhere = new StringBuilder(500);
			sbWhere.append("\"<HTML>\"");

			for (AnalisiBi analisiBi : listaAnalisi) {
				// Aggiungo i parametri al report per gli userControl
				// Map<String, Colonna> colonne = model.getColumnMap();
				for (String inputControl : analisiBi.getInputControls()) {
					JRDesignParameter parameter = new JRDesignParameter();
					parameter.setName(inputControl);
					parameter.setValueClass(java.util.List.class);
					if (!jd.getMainDesignDataset().getParametersMap().containsKey(parameter.getName())) {
						jd.getMainDesignDataset().addParameter(parameter);
					}
				}

				AbstractAnalisiGeneratorComponent componentGenerator = AbstractAnalisiGeneratorComponent
						.createGenerator(analisiBi, jd);
				JRDesignElement componente = componentGenerator.createReportElement();
				// Aggiungo il componente alla banda del titolo
				titleBand.setHeight(titleBand.getHeight() + componente.getHeight());
				componente.setY(titleBand.getHeight() - componente.getHeight());
				// se sto esportando un componente di una dashboard setto dimensione e posizione come nella dashboard
				if (width != null) {
					jd.setPageWidth(width + jd.getLeftMargin() + jd.getRightMargin());
					titleBand.setHeight(height);
					jd.setPageHeight(height + jd.getBottomMargin() + jd.getTopMargin() + 500);
				} else {
					if (componente.getWidth() > jd.getPageWidth()) {
						jd.setPageWidth(componente.getWidth() + jd.getLeftMargin() + jd.getRightMargin());
					}
				}

				DashBoardAnalisi dashBoardAnalisi = layoutDashboard.get(analisiBi.getNome() + analisiBi.getCategoria());
				if (dashBoardAnalisi != null) {
					componente.setHeight(dashBoardAnalisi.getH());
					componente.setWidth(dashBoardAnalisi.getW());
					componente.setX(dashBoardAnalisi.getX());
					componente.setY(dashBoardAnalisi.getY());
				}
				titleBand.addElement(titleBand.getElements().length, componente);

				for (String inputControl : analisiBi.getInputControls()) {
					sbWhere.append(" + ");
					sbWhere.append("\"<B>").append(StringEscapeUtils.escapeHtml4(inputControl.split("_")[0]))
							.append(":</B>\" + $P{").append(inputControl).append("}").append(" +\"<BR/>\"");
				}

				for (AnalisiValueSelected filtro : analisiBi.getFiltri().values()) {
					sbWhere.append(" + ");
					sbWhere.append("\"<B>").append(StringUtils.capitalize(filtro.getNomeCampo().replace(".", " ")))
							.append(":</B> ")
							.append(StringEscapeUtils.escapeHtml4(StringUtils.join(filtro.getParameter())))
							.append("\"");
				}
			}
			sbWhere.append(" + \"</HTML>\"");
			JRDesignQuery query = new JRDesignQuery();
			query.setText("select 1");
			jd.setQuery(query);

			// Filtri
			JRElement componentFilter = titleBand.getElementByKey("filtri_text");
			JRDesignTextField textFilter = (JRDesignTextField) componentFilter;
			if (componentFilter instanceof JRDesignTextField) {
				JRDesignExpression exp = new JRDesignExpression();
				exp.setText(sbWhere.toString());
				textFilter.setExpression(exp);
				System.out.println("DEBUG:JasperReportExportBean->getJasperDesign:" + sbWhere.toString());
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return jd;
	}
}
