package it.eurotn.panjea.contabilita.rich.editors;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.pm.RigaConto;
import it.eurotn.panjea.contabilita.rich.pm.RigaContoCentroCosto;
import it.eurotn.panjea.contabilita.rich.pm.RigaMastro;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;
import it.eurotn.rich.report.FooterBean;
import it.eurotn.rich.report.HeaderBean;
import it.eurotn.rich.report.JecLocalReport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * TreeTable per presentare il bilancio.
 *
 * @author
 */
public class RisultatiRicercaBilancioTablePage extends AbstractTreeTableDialogPageEditor {

	private class StampaCommand extends ActionCommand {

		private boolean showconto;
		private boolean showsottoconto;
		private boolean showCentriCosto;

		/**
		 * Costruttore.
		 *
		 * @param idCommand
		 *            id del comando
		 * @param showconto
		 *            mostra conto
		 * @param showsottoconto
		 *            mostra sottoconto
		 * @param showCentriCosto
		 *            mostra centri di costo
		 */
		public StampaCommand(final String idCommand, final boolean showconto, final boolean showsottoconto,
				final boolean showCentriCosto) {
			super(RisultatiRicercaBilancioTablePage.this.getId() + idCommand);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getId() + idCommand);
			c.configure(this);
			this.showconto = showconto;
			this.showsottoconto = showsottoconto;
			this.showCentriCosto = showCentriCosto;
		}

		@Override
		protected void doExecuteCommand() {
			RisultatiRicercaBilancioTablePage.logger.debug("--> Stampa");
			openReportRisultati(showconto, showsottoconto, showCentriCosto);
		}
	}

	private static Logger logger = Logger.getLogger(RisultatiRicercaBilancioTablePage.class);
	public static final String PAGE_ID = "risultatiRicercaBilancioTablePage";
	private AbstractCommand stampaMastroCommand;
	private AbstractCommand stampaContoCommand;
	private AbstractCommand stampaSottoContoCommand;
	private static final String STAMPA_MASTRO_COMMAND = ".stampaMastroCommand";
	private static final String STAMPA_CONTO_COMMAND = ".stampaContoCommand";
	private static final String STAMPA_SOTTOCONTO_COMMAND = ".stampaSottoContoCommand";
	private static final String STAMPA_COMMAND_GROUP = ".stampaCommandGroup";
	private List<SaldoConto> righeBilancio;
	private ParametriRicercaBilancio parametriRicercaBilancio = null;
	private AziendaCorrente aziendaCorrente = null;
	private IContabilitaBD contabilitaBD;
	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;
	private BigDecimal totaleDare = BigDecimal.ZERO;

	private BigDecimal totaleAvere = BigDecimal.ZERO;

	private JLabel labelDare = new JLabel();
	private JLabel labelAvere = new JLabel();
	private JLabel labelDifferenza = new JLabel();

	{
		contabilitaAnagraficaBD = RcpSupport.getBean(ContabilitaAnagraficaBD.BEAN_ID);
	}

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaBilancioTablePage() {
		super(PAGE_ID);
	}

	/**
	 * Calcola il totale dare e avere in base alla lista dei saldi passata come parametro.
	 *
	 * @param list
	 *            lista dei saldi
	 */
	private void calcolaTotali(List<SaldoConto> list) {
		totaleAvere = BigDecimal.ZERO;
		totaleDare = BigDecimal.ZERO;

		for (SaldoConto rigaContabileBilancio : list) {
			totaleAvere = totaleAvere.add(rigaContabileBilancio.getImportoAvere());
			totaleDare = totaleDare.add(rigaContabileBilancio.getImportoDare());
		}
	}

	@Override
	protected void configureTreeTable(final JXTreeTable treeTable) {
	}

	private DefaultMutableTreeTableNode createTreeNode(List<SaldoConto> righeBilancio2) {
		return new SaldoContoTreeTableNode(righeBilancio2);
	}

	@Override
	protected TreeTableModel createTreeTableModel() {
		return getTreeTableModel(null);

	}

	/**
	 * @return the aziendaCorrente
	 */
	public AziendaCorrente getAziendaCorrente() {
		return aziendaCorrente;
	}

	@Override
	public AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getExpandCommand(), getStampaCommandGroup() };
		return commands;
	}

	@Override
	public JComponent getCustomControl() {
		JPanel panelTotali = getComponentFactory().createPanel(new FlowLayout(FlowLayout.RIGHT));

		panelTotali.add(getComponentFactory().createLabel("<HTML><B>" + getMessage("totaliDare") + "</B></HTML>"),
				BorderLayout.EAST);
		panelTotali.add(labelDare);
		panelTotali.add(Box.createRigidArea(new Dimension(20, 10)));
		panelTotali.add(getComponentFactory().createLabel("<HTML><B>" + getMessage("totaliAvere") + "</B></HTML>"),
				BorderLayout.EAST);
		panelTotali.add(labelAvere);
		panelTotali.add(Box.createRigidArea(new Dimension(20, 10)));
		panelTotali.add(
				getComponentFactory().createLabel("<HTML><B>" + getMessage("totaliDifferenza") + "</B></HTML>"),
				BorderLayout.EAST);
		panelTotali.add(labelDifferenza);

		updateLabel();

		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(getComponentFactory().createLabeledSeparator(getMessage("totaliTotali")), BorderLayout.NORTH);

		JPanel panel2 = getComponentFactory().createPanel(new BorderLayout());
		panel2.add(panelTotali, BorderLayout.CENTER);
		panel2.add(getComponentFactory().createLabeledSeparator(""), BorderLayout.SOUTH);

		panel.add(panel2, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Carica le righe contabili Gestisce inoltre lo stato enabled sul command di stampa.
	 *
	 * @return List<RigaContabile>
	 */
	private List<SaldoConto> getRigheBilancio() {
		// caso forzato in cui setto il form object a null per pulire i risultati ricerca
		// in ParametriRicercaControlloMovimentoContabilitaPage nella execute di resetRicercaCommand
		// lancio il propertyChange della property IPageLifecycleAdvisor.OBJECT_CHANGED con value a null
		// cosi che l'editor risetta il formobject a null per entrambe le dialog page.
		if (this.parametriRicercaBilancio != null && this.parametriRicercaBilancio.isEffettuaRicerca()) {
			righeBilancio = contabilitaBD.caricaBilancio(parametriRicercaBilancio);
		} else {
			righeBilancio = new ArrayList<SaldoConto>();
		}
		return righeBilancio;
	}

	/**
	 * @return the stampaCommandGorup
	 */
	private CommandGroup getStampaCommandGroup() {
		CommandGroup commandGroup = new CommandGroup(getId() + STAMPA_COMMAND_GROUP);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		c.configure(commandGroup);
		commandGroup.add(getStampaMastroCommand());
		commandGroup.add(getStampaContoCommand());
		commandGroup.add(getStampaSottoContoCommand());
		return commandGroup;
	}

	/**
	 * @return the stampaContoCommand
	 */
	public AbstractCommand getStampaContoCommand() {
		if (stampaContoCommand == null) {
			stampaContoCommand = new StampaCommand(STAMPA_CONTO_COMMAND, true, false, false);
			stampaContoCommand.setEnabled(false);
		}
		return stampaContoCommand;
	}

	/**
	 * Command che chiama la funzione di stampa delle righe contabili bilancio visualizzate.
	 *
	 * @return il command di stampa
	 */
	public AbstractCommand getStampaMastroCommand() {
		if (stampaMastroCommand == null) {
			stampaMastroCommand = new StampaCommand(STAMPA_MASTRO_COMMAND, false, false, false);
			stampaMastroCommand.setEnabled(false);
		}
		return stampaMastroCommand;
	}

	/**
	 * @return the stampaContoCommand
	 */
	public AbstractCommand getStampaSottoContoCommand() {
		if (stampaSottoContoCommand == null) {
			boolean stampaCentriCosto = parametriRicercaBilancio == null ? false : parametriRicercaBilancio
					.getStampaCentriCosto();
			stampaSottoContoCommand = new StampaCommand(STAMPA_SOTTOCONTO_COMMAND, true, true, stampaCentriCosto);
			stampaSottoContoCommand.setEnabled(false);
		}
		return stampaSottoContoCommand;
	}

	@Override
	protected TreeCellRenderer getTreeCellRender() {
		return new DefaultTreeCellRenderer() {

			private static final long serialVersionUID = -8813682511205137384L;

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
				JLabel c = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
				c.setForeground(Color.BLACK);
				c.setToolTipText(null);
				if (node.getUserObject() instanceof RigaMastro) {
					c.setIcon(iconSource.getIcon(Mastro.class.getName()));
					c.setText(((RigaMastro) node.getUserObject()).getMastroCodice());
				} else if (node.getUserObject() instanceof RigaConto) {
					c.setIcon(iconSource.getIcon(Conto.class.getName()));
					c.setText(((RigaConto) node.getUserObject()).getContoCodice());
				} else if (node.getUserObject() instanceof RigaContoCentroCosto) {
					c.setIcon(iconSource.getIcon(CentroCosto.class.getName()));
					c.setText(((RigaContoCentroCosto) node.getUserObject()).getCentroCostoCodice());
				} else if (node.getUserObject() instanceof SaldoConto) {
					SaldoConto sc = (SaldoConto) node.getUserObject();
					c.setIcon(iconSource.getIcon(SottoConto.class.getName()));
					c.setText(sc.getSottoContoCodice());
					if (!sc.isTotaleCentriCostoValido()) {
						c.setIcon(iconSource.getIcon("severity.warning"));
						c.setToolTipText(" " + sc.getImportoCentriCostoDare() + " - " + sc.getImportoCentriCostoAvere());
					}
				}
				return c;
			}
		};
	}

	/**
	 * Aggiorna il modello e quindi la tabella ricaricando le righe contabili.
	 *
	 * @param rootNode
	 *            root node
	 * @return table model
	 */
	private TreeTableModel getTreeTableModel(DefaultMutableTreeTableNode rootNode) {
		DefaultMutableTreeTableNode root;
		if (rootNode != null) {
			root = rootNode;
		} else {
			root = createTreeNode(getRigheBilancio());
		}
		return new RigheBilancioTableModel(root);
	}

	@Override
	public void loadData() {
		setTableData(createTreeNode(getRigheBilancio()));
		if (getTreeTable() != null) {
			calcolaTotali(righeBilancio);
			updateLabel();
		}
		getStampaMastroCommand().setEnabled(righeBilancio.size() != 0);
		getStampaContoCommand().setEnabled(righeBilancio.size() != 0);
		getStampaSottoContoCommand().setEnabled(righeBilancio.size() != 0);
		getExpandCommand().setEnabled(righeBilancio.size() != 0);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	/**
	 * Apre il report controllo movimenti contabili.
	 *
	 * @param showconto
	 *            mostra conto
	 * @param showsottoconto
	 *            mostra sottoconto
	 * @param showCentriCosto
	 *            mostra centri costo
	 */
	private void openReportRisultati(boolean showconto, boolean showsottoconto, boolean showCentriCosto) {
		JecLocalReport jecJasperReport = new JecLocalReport();
		jecJasperReport.setReportName("Bilancio di verifica");
		jecJasperReport.setXmlReportResource(new ClassPathResource(
				"/it/eurotn/panjea/contabilita/rich/reports/resources/StampaBilancioContabilita.jasper"));
		jecJasperReport.setMessages("it.eurotn.panjea.messages.cont.reports.messages");
		jecJasperReport.setDataReport(righeBilancio);
		jecJasperReport.getReportParameters().put("stampaConto", showconto);
		jecJasperReport.getReportParameters().put("stampaSottoConto", showsottoconto);
		jecJasperReport.getReportParameters().put("stampaCentriCosto", showCentriCosto);
		jecJasperReport.getReportParameters().put("htmlParameters", this.parametriRicercaBilancio.getHtmlParameters());
		HeaderBean headerBean = new HeaderBean();
		headerBean.setCodiceAzienda(aziendaCorrente.getDenominazione());
		headerBean.setUtenteCorrente(PanjeaSwingUtil.getUtenteCorrente().getUserName());
		FooterBean footerBean = new FooterBean();
		jecJasperReport.setDataHeader(headerBean);
		jecJasperReport.setDataFooter(footerBean);
		jecJasperReport.execute();
	}

	@Override
	protected void openSelectedNode(DefaultMutableTreeTableNode node) {
		if (node.getUserObject() instanceof SaldoConto) {
			SaldoConto saldoConto = (SaldoConto) node.getUserObject();

			if (saldoConto.getSottoContoId() != null && saldoConto.getSottoContoId() != -1) {
				ParametriRicercaEstrattoConto parametriRicercaEstrattoConto = new ParametriRicercaEstrattoConto();
				parametriRicercaEstrattoConto.setAnnoCompetenza(parametriRicercaBilancio.getAnnoCompetenza());
				parametriRicercaEstrattoConto.setDataRegistrazione(parametriRicercaBilancio.getDataRegistrazione());
				parametriRicercaEstrattoConto.setSottoConto(contabilitaAnagraficaBD.caricaSottoConto(saldoConto
						.getSottoContoId()));
				parametriRicercaEstrattoConto.setCentroCosto(parametriRicercaBilancio.getCentroCosto());
				parametriRicercaEstrattoConto.setStatiAreaContabile(parametriRicercaBilancio.getStatiAreaContabile());
				parametriRicercaEstrattoConto.setEffettuaRicerca(true);

				LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaEstrattoConto);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}
	}

	@Override
	public void refreshData() {
		loadData();
	}

	@Override
	protected void selectionChanged(DefaultMutableTreeTableNode node) {
		// non faccio niente sul cambio selezione
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param contabilitaBD
	 *            the contabilitaBD to set
	 */
	public void setContabilitaBD(IContabilitaBD contabilitaBD) {
		this.contabilitaBD = contabilitaBD;
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof ParametriRicercaBilancio) {
			this.parametriRicercaBilancio = (ParametriRicercaBilancio) object;
		} else {
			this.parametriRicercaBilancio = new ParametriRicercaBilancio();
		}
	}

	/**
	 * Aggiorna le label del dare e avere.
	 */
	private void updateLabel() {
		Format format = new DecimalFormat(ValutaAzienda.MASCHERA_VALUTA_GENERICA);
		labelAvere.setText(format.format(totaleAvere));
		labelDare.setText(format.format(totaleDare));
		labelDifferenza.setText(format.format(totaleDare.subtract(totaleAvere)));
	}
}
