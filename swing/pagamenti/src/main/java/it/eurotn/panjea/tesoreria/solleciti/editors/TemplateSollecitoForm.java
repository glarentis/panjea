package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.solleciti.TemplatePlaceHolder;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;
import it.eurotn.rich.components.htmleditor.HTMLEditorPane;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.rich.report.ReportManager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TemplateSollecitoForm extends PanjeaAbstractForm {
	private static final String FORM_ID = "templateSollecitoForm";

	private JComponent currentEditTestArea = null;

	private ReportManager reportManager;

	/**
	 * default constructor.
	 */
	public TemplateSollecitoForm() {
		super(PanjeaFormModelHelper.createFormModel(new TemplateSolleciti(), false, FORM_ID), FORM_ID);
		reportManager = (ReportManager) Application.instance().getApplicationContext().getBean("reportManager");
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref, 4dlu, fill:pref:grow, 4dlu, left:default",
				"4dlu,default, 4dlu,default, 2dlu,default, 2dlu,default, 2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("r,c");

		builder.nextRow();
		builder.setRow(2);
		builder.addPropertyAndLabel("descrizione");
		builder.nextRow();

		JTabbedPane tabbedPane = getComponentFactory().createTabbedPane();
		tabbedPane.addTab("Report", RcpSupport.getIcon("stampa"), createReportComponents(bf));
		tabbedPane.addTab("EMail", RcpSupport.getIcon("email"), createMailComponents(bf));

		builder.addComponent(tabbedPane, 1, 4, 3, 3);

		builder.nextRow();
		builder.addComponent(
				getComponentFactory().createTitledBorderFor("Dati per l'inserimento",
						(createPropertiesTable()).getComponent()), 5);
		builder.nextRow();

		return builder.getPanel();
	}

	/**
	 * Crea i componenti per la gestione dei valori usati per l'invio mail.
	 * 
	 * @param bf
	 *            {@link PanjeaSwingBindingFactory}
	 * @return componenti creati
	 */
	private JComponent createMailComponents(PanjeaSwingBindingFactory bf) {

		FormLayout layout = new FormLayout("right:pref,4dlu,fill:pref:grow",
				"4dlu,default,4dlu,default,4dlu,fill:pref:grow");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		ComboBoxBinding bindingReport = (ComboBoxBinding) bf.createBoundComboBox("reportNameMail",
				reportManager.listReport(TemplateSolleciti.REPORT_PATH));
		bindingReport.setEmptySelectionValue("");
		builder.addLabel(RcpSupport.getMessage("reportName"), cc.xy(1, 2));
		builder.add(bindingReport.getControl(), cc.xy(3, 2));
		builder.nextRow();

		builder.addLabel(RcpSupport.getMessage("oggetto.label"), cc.xy(1, 4));
		final JTextField textField = (JTextField) bf.createBoundTextField("oggettoMail").getControl();
		textField.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				currentEditTestArea = textField;
			}
		});

		builder.add(textField, cc.xy(3, 4));

		Binding testoBinding = bf.createBoundHTMLEditor("testoMail");

		JComponent testoComponent = testoBinding.getControl();
		testoComponent.setBorder(new TitledBorder("Testo"));
		final HTMLEditorPane areaTestoMailComponent = (HTMLEditorPane) ((JPanel) testoComponent.getComponent(0));
		areaTestoMailComponent.getWysEditor().addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				currentEditTestArea = areaTestoMailComponent;
			}
		});
		builder.add(testoComponent, cc.xyw(1, 6, 3));

		return builder.getPanel();
	}

	/**
	 * crea la tabelle con le propieta che possono essere aggiunte al template
	 * del solecito.
	 * 
	 * @return tabella delle properties.
	 */
	protected JideTableWidget<TemplatePlaceHolder> createPropertiesTable() {
		final JideTableWidget<TemplatePlaceHolder> tableProperties = new JideTableWidget<TemplatePlaceHolder>(
				"propertiesTable", new String[] { "classe", "descrizione" }, TemplatePlaceHolder.class);
		tableProperties.setAggregatedColumns(new String[] { "classe" });
		tableProperties.setRows(TemplateSolleciti.getTemplatePlaceHolders());
		tableProperties.setPropertyCommandExecutor(new ActionCommandExecutor() {

			@Override
			public void execute() {
				try {
					insertCodice(tableProperties);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return tableProperties;

	}

	/**
	 * Crea i componenti per la gestione dei valori usati dal report.
	 * 
	 * @param bf
	 *            {@link PanjeaSwingBindingFactory}
	 * @return componenti creati
	 */
	private JComponent createReportComponents(PanjeaSwingBindingFactory bf) {

		JPanel testoPanel = new JPanel(new GridLayout(2, 1, 3, 3));

		ComboBoxBinding bindingReport = (ComboBoxBinding) bf.createBoundComboBox("reportName",
				reportManager.listReport(TemplateSolleciti.REPORT_PATH));
		bindingReport.setEmptySelectionValue("");
		JLabel reportLabel = new JLabel(RcpSupport.getMessage("reportName"));
		JPanel reportPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		reportPanel.add(reportLabel);
		reportPanel.add(bindingReport.getControl());

		Binding testoBinding = bf.createBoundHTMLEditor("testo");

		JComponent testoComponent = testoBinding.getControl();
		testoComponent.setBorder(new TitledBorder("Corpo"));
		final HTMLEditorPane areaTestoComponent = (HTMLEditorPane) ((JPanel) testoComponent.getComponent(0));
		areaTestoComponent.getWysEditor().addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				currentEditTestArea = areaTestoComponent;
			}
		});

		Binding testoFooterBinding = bf.createBoundHTMLEditor("testoFooter");
		JComponent footerComponent = testoFooterBinding.getControl();
		final HTMLEditorPane areaTestoFooterComponent = (HTMLEditorPane) ((JPanel) footerComponent.getComponent(0));
		areaTestoFooterComponent.getWysEditor().addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				currentEditTestArea = areaTestoFooterComponent;
			}
		});
		footerComponent.setBorder(new TitledBorder("Piede"));
		testoPanel.add(testoComponent);
		testoPanel.add(footerComponent);

		JPanel rootPanel = new JPanel(new BorderLayout(0, 10));
		rootPanel.add(reportPanel, BorderLayout.NORTH);
		rootPanel.add(testoPanel, BorderLayout.CENTER);
		GuiStandardUtils.attachBorder(rootPanel);

		return rootPanel;
	}

	/**
	 * inserisco il codice di una propieta normale.
	 * 
	 * @param tableProperties
	 *            .
	 */
	private void insertCodice(JideTableWidget<TemplatePlaceHolder> tableProperties) {

		if (currentEditTestArea == null) {
			return;
		}

		if (currentEditTestArea instanceof HTMLEditorPane) {
			try {
				int caretPos = ((HTMLEditorPane) currentEditTestArea).getWysEditor().getCaretPosition();

				((HTMLEditorPane) currentEditTestArea).insertText(
						((HTMLEditorPane) currentEditTestArea).getWysEditor(), tableProperties.getSelectedObject()
								.getCodice(), caretPos);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			((HTMLEditorPane) currentEditTestArea).getWysEditor().requestFocusInWindow();
		} else if (currentEditTestArea instanceof JTextField) {
			try {
				int caretPos = ((JTextField) currentEditTestArea).getCaretPosition();

				((JTextField) currentEditTestArea).getDocument().insertString(caretPos,
						tableProperties.getSelectedObject().getCodice(), null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			((JTextField) currentEditTestArea).requestFocusInWindow();
		}
	}

}
