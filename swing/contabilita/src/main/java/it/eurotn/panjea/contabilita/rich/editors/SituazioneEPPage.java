package it.eurotn.panjea.contabilita.rich.editors;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.pm.RigaConto;
import it.eurotn.panjea.contabilita.rich.pm.RigaContoCentroCosto;
import it.eurotn.panjea.contabilita.rich.pm.RigaMastro;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSituazioneEP;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;
import it.eurotn.rich.report.FooterBean;
import it.eurotn.rich.report.HeaderBean;
import it.eurotn.rich.report.JecLocalReport;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
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
 * Situazione Economica patrimoniale; questa classe viene utilizzata nella TabbedDialogPage
 * RisultatiRicercaSituazioneEPPage e viene utilizzata per presentare sia la parte patrimoniale che profitti che conti
 * ordine.
 * 
 * Questa treeTable oltre al suo tableHeader standard e' racchiusa da un header e un footer (JTableHeader)
 * 
 * header | | labelPerDare || labelPerAvere |
 * 
 * header dati | tree | conto | descr. | saldo || conto | descr. | saldo |
 * 
 * footer | | descrPerDare | tot || descrPerAvere | tot |
 * 
 * Nel footer solo una colonna tot e' visualizzata con il risultato; se D-A<0 valorizzo tot per Dare; se D-A>0 valorizzo
 * tot per Avere; se D-A=0 non valorizzo nessuno dei due.
 * 
 * 
 * @author giangi,Leonardo
 */
public class SituazioneEPPage extends AbstractTreeTableDialogPageEditor {

	/**
	 * Listener legato ai cambiamenti di larghezza del columnModel della treeTable; Se cambio la larghezza di una
	 * colonna modifico la larghezza dell'headControl e del customControl (footer).
	 * 
	 * @author Leonardo
	 */
	public class ResizeColumnModelListener implements TableColumnModelListener {

		@Override
		public void columnAdded(TableColumnModelEvent e) {
			// non mi serve
		}

		@Override
		public void columnMarginChanged(ChangeEvent e) {

			DefaultTableColumnModel colModel = (DefaultTableColumnModel) e.getSource();

			try {
				// tabella che presenta i dati della situazione E/P
				int col0 = colModel.getColumn(0).getWidth();
				int col1 = colModel.getColumn(1).getWidth();
				int col2 = colModel.getColumn(2).getWidth();
				int col3 = colModel.getColumn(3).getWidth();
				int col4 = colModel.getColumn(4).getWidth();
				int col5 = colModel.getColumn(5).getWidth();
				int col6 = colModel.getColumn(6).getWidth();
				int col7 = colModel.getColumn(7).getWidth();

				// dimensioni per header table

				int widthCosti = col1 + col2 + col3;
				int widthRicavi = col5 + col6 + col7;

				// header della tabella
				TableColumnModel colHeadModel = ((JTableHeader) getHeadControl()).getColumnModel();
				// setto le dimensioni per allineare l'header con l'header
				// tabella
				colHeadModel.getColumn(0).setWidth(col0);
				colHeadModel.getColumn(1).setWidth(widthCosti);
				colHeadModel.getColumn(2).setWidth(col4);
				colHeadModel.getColumn(3).setWidth(widthRicavi);

				int widthDescCosti = col1 + col2;
				int widthDescRicavi = col5 + col6;

				// header della tabella
				TableColumnModel colFootModel = ((JTableHeader) getCustomControl()).getColumnModel();
				// setto le dimensioni per allineare il footer con l'header
				// tabella
				colFootModel.getColumn(0).setWidth(col0);
				colFootModel.getColumn(1).setWidth(widthDescCosti);
				colFootModel.getColumn(2).setWidth(col3);
				colFootModel.getColumn(3).setWidth(col4);
				colFootModel.getColumn(4).setWidth(widthDescRicavi);
				colFootModel.getColumn(5).setWidth(col7);
			} catch (RuntimeException e1) {
				logger.warn("--> Impossibile installare il listener per il changeColumn", e1);
			}

		}

		@Override
		public void columnMoved(TableColumnModelEvent e) {
			// non mi serve
		}

		@Override
		public void columnRemoved(TableColumnModelEvent e) {
			// non mi serve
		}

		@Override
		public void columnSelectionChanged(ListSelectionEvent e) {
			// non mi serve
		}
	}

	private class StampaCommand extends ActionCommand {

		private final boolean showconto;
		private final boolean showsottoconto;

		/**
		 * Stampa Command.
		 * 
		 * @param commandId
		 *            commandId
		 * @param showconto
		 *            showconto
		 * @param showsottoconto
		 *            showsottoconto
		 */
		public StampaCommand(final String commandId, final boolean showconto, final boolean showsottoconto) {
			super(SituazioneEPPage.this.getId() + commandId);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getId() + commandId);
			c.configure(this);
			this.showconto = showconto;
			this.showsottoconto = showsottoconto;
		}

		@Override
		protected void doExecuteCommand() {
			openReportRisultati(showconto, showsottoconto);
		}
	}

	private static Logger logger = Logger.getLogger(SituazioneEPPage.class);
	private AbstractCommand stampaMastroCommand;
	private AbstractCommand stampaContoCommand;
	private AbstractCommand stampaSottoContoCommand;
	private static final String STAMPA_MASTRO_COMMAND = ".stampaMastroCommand";
	private static final String STAMPA_CONTO_COMMAND = ".stampaContoCommand";
	private static final String STAMPA_SOTTOCONTO_COMMAND = ".stampaSottoContoCommand";
	private static final String STAMPA_COMMAND_GROUP = ".stampaCommandGroup";
	private List<SaldoConto> righeSituazionePatrimoniale;
	private BigDecimal totaleDare = BigDecimal.ZERO;
	private BigDecimal totaleAvere = BigDecimal.ZERO;
	private final String labelDare = new String();
	private ParametriRicercaSituazioneEP parametriRicercaSituazioneEP = null;

	private final String labelAvere = new String();
	private AziendaCorrente aziendaCorrente = null;
	private JTableHeader headerOfTable = null;
	private JTableHeader footerOfTable = null;

	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	{
		contabilitaAnagraficaBD = RcpSupport.getBean(ContabilitaAnagraficaBD.BEAN_ID);
	}

	/**
	 * Default constructor.
	 * 
	 * @param pageId
	 *            pageId
	 * @param aziendaCorrente
	 *            aziendaCorrente
	 */
	public SituazioneEPPage(final String pageId, final AziendaCorrente aziendaCorrente) {
		super(pageId);
		this.aziendaCorrente = aziendaCorrente;
		setShowTitlePane(false);
		righeSituazionePatrimoniale = new ArrayList<SaldoConto>();
	}

	/**
	 * Calcola i totali di dare e avere.
	 */
	private void calcolaTotali() {
		logger.debug("--> Enter calcolaTotali");
		totaleAvere = BigDecimal.ZERO;
		totaleDare = BigDecimal.ZERO;
		BigDecimal saldo = BigDecimal.ZERO;

		for (SaldoConto saldoConto : righeSituazionePatrimoniale) {
			saldo = saldoConto.getSaldo();
			// se D-A < 0 ---> dare
			if (saldo.signum() > 0) {
				totaleDare = totaleDare.add(saldo.abs());
			} else {
				totaleAvere = totaleAvere.add(saldo.abs());
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> saldo totale=" + totaleDare.subtract(totaleAvere));
			logger.debug("--> totale dare " + totaleDare.toString());
			logger.debug("--> totale avere " + totaleAvere.toString());
		}
	}

	@Override
	protected void configureTreeTable(final JXTreeTable treeTable) {
		// non permetto di riordinare le colonne
		treeTable.getTableHeader().setReorderingAllowed(false);
		// listener per i cambiamenti sulla larghezza delle colonne tabella
		treeTable.getColumnModel().addColumnModelListener(new ResizeColumnModelListener());
	}

	/**
	 * Costruisce il tree per il treeTableModel, il mastro e' il nodo principale seguito da conto e sottoconto di quel
	 * mastro.
	 * 
	 * @return DefaultMutableTreeTableNode che rappresenta la situazione E/P raggruppate per mastro e conto
	 */
	private DefaultMutableTreeTableNode createTreeNode() {
		return new SaldoContoTreeTableNode(righeSituazionePatrimoniale);
	}

	@Override
	protected TreeTableModel createTreeTableModel() {
		return getTreeTableModel();
	}

	@Override
	public AbstractCommand[] getCommand() {
		AbstractCommand[] commands = new AbstractCommand[] { getExpandCommand(), getStampaCommandGroup() };
		return commands;
	}

	/**
	 * Crea il footer della tabella che mostra la differenza di Attivita' o Costi - passivita' o ricavi; se la
	 * differenza e' negativa il risultato viene mostrato sotto attivita' o costi; se positiva viene mostrata sotto
	 * passivita' o ricavi con descrizioni diverse a seconda se si visualizza la situazione patrimoniale e ordine oppure
	 * profitti/perdite. Per l'aggiornamento vengono chiamati nella loadData calcolaTotali() e updateLabel()
	 * 
	 * @return JComponent
	 */
	@Override
	public JComponent getCustomControl() {
		if (footerOfTable == null) {
			footerOfTable = new JTableHeader();
			footerOfTable.setReorderingAllowed(false);
			footerOfTable.setFocusable(false);
			footerOfTable.setResizingAllowed(false);

			TableColumnModel tableColumnModel = new DefaultTableColumnModel();
			tableColumnModel.setColumnSelectionAllowed(false);

			// colonna 0
			TableColumn columnTree = new TableColumn();
			columnTree.setHeaderValue(getMessage(getId() + "FooterColumnTree"));

			// colonna 1
			TableColumn columnDescrizioneCosti = new TableColumn();
			columnDescrizioneCosti.setHeaderValue(getMessage(getId() + "FooterColumn0"));

			// colonna 2
			TableColumn columnSaldoCosti = new TableColumn();
			columnSaldoCosti.setHeaderValue(labelDare);

			// colonna 3
			TableColumn columnGap = new TableColumn();
			columnGap.setHeaderValue(getMessage(getId() + "FooterColumnGap"));

			// colonna 4
			TableColumn columnDescrizioneRicavi = new TableColumn();
			columnDescrizioneRicavi.setHeaderValue(getMessage(getId() + "FooterColumn1"));

			// colonna 5
			TableColumn columnSaldoRicavi = new TableColumn();
			columnSaldoRicavi.setHeaderValue(labelAvere);

			tableColumnModel.addColumn(columnTree);
			tableColumnModel.addColumn(columnDescrizioneCosti);
			tableColumnModel.addColumn(columnSaldoCosti);
			tableColumnModel.addColumn(columnGap);
			tableColumnModel.addColumn(columnDescrizioneRicavi);
			tableColumnModel.addColumn(columnSaldoRicavi);

			footerOfTable.setColumnModel(tableColumnModel);
		}
		return footerOfTable;
	}

	/**
	 * Crea un tableHeader al di sopra dela tabella per avere un riepilogo che raggruppa le colonne della tabella.
	 * 
	 * @return JComponent
	 */
	@Override
	public JComponent getHeadControl() {
		if (headerOfTable == null) {
			headerOfTable = new JTableHeader();
			headerOfTable.setReorderingAllowed(false);
			headerOfTable.setFocusable(false);
			headerOfTable.setResizingAllowed(false);
			headerOfTable.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));

			TableColumnModel tableColumnModel = new DefaultTableColumnModel();
			tableColumnModel.setColumnSelectionAllowed(false);

			// colonna 0
			TableColumn columnTree = new TableColumn();
			columnTree.setHeaderValue(getMessage(getId() + "HeaderColumnTree"));

			// colonna 1
			TableColumn columnCosti = new TableColumn();
			columnCosti.setHeaderValue(getMessage(getId() + "HeaderColumn0"));

			// colonna 2
			TableColumn columnGap = new TableColumn();
			columnGap.setHeaderValue(getMessage(getId() + "HeaderColumnGap"));

			// colonna 3
			TableColumn columnRicavi = new TableColumn();
			columnRicavi.setHeaderValue(getMessage(getId() + "HeaderColumn1"));

			tableColumnModel.addColumn(columnTree);
			tableColumnModel.addColumn(columnCosti);
			tableColumnModel.addColumn(columnGap);
			tableColumnModel.addColumn(columnRicavi);

			headerOfTable.setColumnModel(tableColumnModel);
		}
		return headerOfTable;
	}

	/**
	 * @return stampa command group per scegliere se stampare a livello di mastro, conto o sottoconto.
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
	 * @return stampa conto command
	 */
	public AbstractCommand getStampaContoCommand() {
		if (stampaContoCommand == null) {
			stampaContoCommand = new StampaCommand(STAMPA_CONTO_COMMAND, true, false);
			stampaContoCommand.setEnabled(false);
		}
		return stampaContoCommand;
	}

	/**
	 * Command che chiama la funzione di stampa delle righe contabili visualizzate.
	 * 
	 * @return stampa mastro command
	 */
	public AbstractCommand getStampaMastroCommand() {
		if (stampaMastroCommand == null) {
			stampaMastroCommand = new StampaCommand(STAMPA_MASTRO_COMMAND, false, false);
			stampaMastroCommand.setEnabled(false);
		}
		return stampaMastroCommand;
	}

	/**
	 * @return stampa sotto conto command
	 */
	public AbstractCommand getStampaSottoContoCommand() {
		if (stampaSottoContoCommand == null) {
			stampaSottoContoCommand = new StampaCommand(STAMPA_SOTTOCONTO_COMMAND, true, true);
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
				c.setText("");

				if (node.getUserObject() instanceof RigaMastro) {
					c.setIcon(iconSource.getIcon(Mastro.class.getName()));
					// c.setText(((RigaMastro)
					// node.getUserObject()).getMastroCodice());
				} else if (node.getUserObject() instanceof RigaConto) {
					c.setIcon(iconSource.getIcon(Conto.class.getName()));
					// c.setText(((RigaConto)
					// node.getUserObject()).getContoCodice());
				} else if (node.getUserObject() instanceof RigaContoCentroCosto) {
					c.setIcon(iconSource.getIcon(CentroCosto.class.getName()));
					// c.setText(((RigaContoCentroCosto) node.getUserObject()).getCentroCostoCodice());
				} else if (node.getUserObject() instanceof SaldoConto) {
					SaldoConto sc = (SaldoConto) node.getUserObject();
					c.setIcon(iconSource.getIcon(SottoConto.class.getName()));
					// c.setText(((SottoContoDTO)
					// node.getUserObject()).getSottoContoCodice());
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
	 * @return TreeTableModel
	 */
	private TreeTableModel getTreeTableModel() {
		DefaultMutableTreeTableNode root;
		root = createTreeNode();
		return new SituazionePatrimonialeTableModel(root);
	}

	/**
	 * Ricrea il treeNode aggiornando i totali; metodo richiamato dalla tabbed RisultatiRicercaSituazioneEPPage alla
	 * creazione della page e alla chiamata del suo refresh().
	 */
	@Override
	public void loadData() {
		setTableData(createTreeNode());
		if (getTreeTable() != null) {
			calcolaTotali();
			updateLabel();
		}
		getStampaMastroCommand().setEnabled(righeSituazionePatrimoniale.size() != 0);
		getStampaContoCommand().setEnabled(righeSituazionePatrimoniale.size() != 0);
		getStampaSottoContoCommand().setEnabled(righeSituazionePatrimoniale.size() != 0);
		getExpandCommand().setEnabled(righeSituazionePatrimoniale.size() != 0);
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
	 *            decide se mostrare a livello di conto
	 * @param showsottoconto
	 *            decide se mostrare a livello di sotto conto
	 */
	private void openReportRisultati(boolean showconto, boolean showsottoconto) {
		JecLocalReport jecJasperReport = new JecLocalReport();
		jecJasperReport.setReportName("Situazione E/P");
		jecJasperReport.setXmlReportResource(new ClassPathResource(
				"/it/eurotn/panjea/contabilita/rich/reports/resources/StampaSituazioneEP.jasper"));
		jecJasperReport.setMessages("it.eurotn.panjea.messages.cont.reports.messages");
		jecJasperReport.setDataReport(righeSituazionePatrimoniale);
		jecJasperReport.getReportParameters().put("stampaConto", showconto);
		jecJasperReport.getReportParameters().put("stampaSottoConto", showsottoconto);
		jecJasperReport.getReportParameters().put("htmlParameters",
				this.parametriRicercaSituazioneEP.getHtmlParameters());
		jecJasperReport.getReportParameters().put("titoloTabella1",
				getMessage(getId() + "HeaderColumn0").replaceAll("(?i)</?(HTML|FONT|DIV|B)\\b[^>]*>", ""));
		jecJasperReport.getReportParameters().put("titoloTabella2",
				getMessage(getId() + "HeaderColumn1").replaceAll("(?i)</?(HTML|FONT|DIV|B)\\b[^>]*>", ""));
		jecJasperReport.getReportParameters().put("footerTotaliLeft", getMessage(getId() + "FooterColumn0"));
		jecJasperReport.getReportParameters().put("footerTotaliRight", getMessage(getId() + "FooterColumn1"));

		jecJasperReport.getReportParameters().put("PAGE_ID", this.getTitle()); // getId());
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
				parametriRicercaEstrattoConto.setAnnoCompetenza(parametriRicercaSituazioneEP.getAnnoCompetenza());
				parametriRicercaEstrattoConto.setDataRegistrazione(parametriRicercaSituazioneEP.getDataRegistrazione());
				parametriRicercaEstrattoConto.setSottoConto(contabilitaAnagraficaBD.caricaSottoConto(saldoConto
						.getSottoContoId()));
				parametriRicercaEstrattoConto.setCentroCosto(parametriRicercaSituazioneEP.getCentroCosto());
				parametriRicercaEstrattoConto.setStatiAreaContabile(parametriRicercaSituazioneEP
						.getStatiAreaContabile());
				parametriRicercaEstrattoConto.setEffettuaRicerca(true);

				LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaEstrattoConto);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}
	}

	/**
	 * Metodo non utilizzato. Il ciclo di vita della page e' gestito sulla tabbed e non su questa page che e' contenuta
	 * dalla tabbed. Al posto di questa viene richiamata direttamente la loadData()
	 */
	@Override
	public void refreshData() {
	}

	@Override
	protected void selectionChanged(DefaultMutableTreeTableNode node) {
		// non faccio niente sul cambio selezione
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setFormObject(Object object) {
		this.righeSituazionePatrimoniale = (List<SaldoConto>) object;
	}

	/**
	 * @param parametriRicercaSituazioneEP
	 *            the parametriRicercaSituazioneEP to set
	 */
	public void setParametriRicercaSituazioneEP(ParametriRicercaSituazioneEP parametriRicercaSituazioneEP) {
		this.parametriRicercaSituazioneEP = parametriRicercaSituazioneEP;
	}

	/**
	 * Aggiorna la differenza D-A nel footer della tabella; c'e' una colonna sotto la parte Attivita' o Costi e un'altra
	 * colonna sotto ricavi o passivita', se <code>D-A<0</code> viene presentata la differenza sotto la parte 1
	 * altrimenti sotto la parte 2.
	 */
	private void updateLabel() {
		BigDecimal differenza = totaleDare.abs().subtract(totaleAvere.abs());
		Format format = new DecimalFormat(ValutaAzienda.MASCHERA_VALUTA_GENERICA);
		((JTableHeader) getCustomControl()).getColumnModel().getColumn(2).setHeaderValue("");
		((JTableHeader) getCustomControl()).getColumnModel().getColumn(5).setHeaderValue("");
		// if D-A<0 => update dare o costi o attivita'
		if (differenza.signum() < 0) {
			((JTableHeader) getCustomControl()).getColumnModel().getColumn(2)
					.setHeaderValue("<HTML><b>" + format.format(differenza.abs()) + "</b></HTML>");
		} else if (differenza.signum() > 0) {
			// if D-A>0 => update avere o ricavi o passivita'
			((JTableHeader) getCustomControl()).getColumnModel().getColumn(5)
					.setHeaderValue("<HTML><b>" + format.format(differenza.abs()) + "</b></HTML>");
		}
		// aggiorno il component altimenti finche' non passo con il mouse non si
		// aggiorna il valore
		((JTableHeader) getCustomControl()).repaint();
	}
}
