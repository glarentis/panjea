package it.eurotn.panjea.contabilita.rich.editors;

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
import it.eurotn.panjea.contabilita.rich.pm.RigaContoCentroCostoConfronto;
import it.eurotn.panjea.contabilita.rich.pm.RigaContoConfronto;
import it.eurotn.panjea.contabilita.rich.pm.RigaMastro;
import it.eurotn.panjea.contabilita.rich.pm.RigaMastroConfronto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancioConfronto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.contabilita.util.SaldoContoConfronto;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;
import it.eurotn.rich.report.FooterBean;
import it.eurotn.rich.report.HeaderBean;
import it.eurotn.rich.report.JecLocalReport;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
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
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * TreeTable per presentare il bilancio.
 * 
 * @author Leonardo
 */
public class RisultatiRicercaBilancioConfrontoTablePage extends AbstractTreeTableDialogPageEditor {

	/**
	 * Classe derivata da SottoContoDTO per presentare solo le informazioni del conto.
	 * 
	 * @author Leonardo
	 */

	private class StampaCommand extends ActionCommand {

		private boolean showconto;
		private boolean showsottoconto;

		/**
		 * Costruttore.
		 * 
		 * @param idCommand
		 *            id del comando
		 * @param showconto
		 *            mostra conto
		 * @param showsottoconto
		 *            mostra sottoconto
		 */
		public StampaCommand(final String idCommand, final boolean showconto, final boolean showsottoconto) {
			super(RisultatiRicercaBilancioConfrontoTablePage.this.getId() + idCommand);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			this.setSecurityControllerId(getId() + idCommand);
			c.configure(this);
			this.showconto = showconto;
			this.showsottoconto = showsottoconto;
		}

		@Override
		protected void doExecuteCommand() {
			RisultatiRicercaBilancioConfrontoTablePage.logger.debug("--> Stampa");
			openReportRisultati(showconto, showsottoconto);
		}
	}

	private static Logger logger = Logger.getLogger(RisultatiRicercaBilancioConfrontoTablePage.class);
	public static final String PAGE_ID = "risultatiRicercaBilancioConfrontoTablePage";
	private AbstractCommand stampaMastroCommand;
	private AbstractCommand stampaContoCommand;
	private AbstractCommand stampaSottoContoCommand;
	private static final String STAMPA_MASTRO_COMMAND = ".stampaMastroCommand";
	private static final String STAMPA_CONTO_COMMAND = ".stampaContoCommand";
	private static final String STAMPA_SOTTOCONTO_COMMAND = ".stampaSottoContoCommand";
	private static final String STAMPA_COMMAND_GROUP = ".stampaCommandGroup";
	private List<SaldoContoConfronto> righeBilancioConfronto;
	private ParametriRicercaBilancioConfronto parametriRicercaBilancioConfronto = null;
	private AziendaCorrente aziendaCorrente = null;

	private IContabilitaBD contabilitaBD;
	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

	{
		contabilitaAnagraficaBD = RcpSupport.getBean(ContabilitaAnagraficaBD.BEAN_ID);
	}

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaBilancioConfrontoTablePage() {
		super(PAGE_ID);
	}

	@Override
	protected void configureTreeTable(final JXTreeTable treeTable) {
	}

	/**
	 * Costruisce il tree per il treeTableModel, il mastro è il nodo principale seguito da conto e sottoconto di quel
	 * mastro.
	 * 
	 * @param paramRigheBilancioConfronto
	 *            le righe bilancio ordinate per mastro, conto, sottoconto
	 * @return DefaultMutableTreeTableNode che rappresenta le righe bilancio raggruppate per mastro e conto
	 */
	private DefaultMutableTreeTableNode createTreeNode(List<SaldoContoConfronto> paramRigheBilancioConfronto) {
		// root su cui aggiungere i mastri
		DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();

		SaldoContoConfronto oldRiga = null;

		RigaMastroConfronto currentMastro = null;
		RigaContoConfronto currentConto = null;
		SaldoContoConfronto currentSottoconto = null;

		DefaultMutableTreeTableNode nodeMastro = null;
		DefaultMutableTreeTableNode nodeConto = null;
		DefaultMutableTreeTableNode nodeSottoConto = null;

		// scorro le righe bilancio
		for (SaldoContoConfronto saldoContoConfronto : paramRigheBilancioConfronto) {

			logger.debug("--> mastro corrente " + saldoContoConfronto.getMastroCodice());
			// se la riga precedente ha lo stesso codice mastro aggiungo il dare e avere della riga corrente sul 'old
			// RigaMastro
			if (oldRiga != null && saldoContoConfronto.getMastroCodice().equals(oldRiga.getMastroCodice())) {
				logger.debug("--> aggiorno mastro corrente");
				currentMastro.aggiungiImportoDare(saldoContoConfronto.getImportoDare());
				currentMastro.aggiungiImportoAvere(saldoContoConfronto.getImportoAvere());

				currentMastro.aggiungiImportoDare2(saldoContoConfronto.getImportoDare2());
				currentMastro.aggiungiImportoAvere2(saldoContoConfronto.getImportoAvere2());
			} else {
				// altrimenti creo una nuava riga mastro con le sole info del mastro e dare e avere e la aggiungo al
				// tree
				RigaMastro saldoContoMastro = new RigaMastro(saldoContoConfronto.getMastroCodice(),
						saldoContoConfronto.getMastroDescrizione(), saldoContoConfronto.getMastroId(),
						saldoContoConfronto.getTipoConto(), saldoContoConfronto.getSottoTipoConto(),
						saldoContoConfronto.getImportoDare(), saldoContoConfronto.getImportoAvere());
				currentMastro = new RigaMastroConfronto(saldoContoMastro, saldoContoConfronto.getImportoDare2(),
						saldoContoConfronto.getImportoAvere2());
				nodeMastro = new DefaultMutableTreeTableNode(currentMastro);
				logger.debug("--> creo mastro nuovo e aggiungo a root il nodo " + nodeMastro);
				rootNode.add(nodeMastro);
			}

			// se la riga precedente ha la stessa riga conto aggiungo dare e avere
			if (oldRiga != null && oldRiga.getContoCodice().equals(saldoContoConfronto.getContoCodice())
					&& oldRiga.getMastroCodice().equals(saldoContoConfronto.getMastroCodice())) {
				logger.debug("--> aggiorno conto corrente");
				currentConto.aggiungiImportoDare(saldoContoConfronto.getImportoDare());
				currentConto.aggiungiImportoAvere(saldoContoConfronto.getImportoAvere());

				currentConto.aggiungiImportoDare2(saldoContoConfronto.getImportoDare2());
				currentConto.aggiungiImportoAvere2(saldoContoConfronto.getImportoAvere2());
			} else {
				// altrimenti creo una nuova rigaConto da associare al mastro

				RigaConto saldoContoConto = new RigaConto(saldoContoConfronto.getContoCodice(),
						saldoContoConfronto.getContoDescrizione(), saldoContoConfronto.getContoId(),
						saldoContoConfronto.getTipoConto(), saldoContoConfronto.getSottoTipoConto(),
						saldoContoConfronto.getImportoDare(), saldoContoConfronto.getImportoAvere());
				currentConto = new RigaContoConfronto(saldoContoConto, saldoContoConfronto.getImportoDare2(),
						saldoContoConfronto.getImportoAvere2());
				nodeConto = new DefaultMutableTreeTableNode(currentConto);
				logger.debug("--> creo conto nuovo e aggiungo node conto " + nodeConto + " a node mastro " + nodeMastro);
				nodeMastro.add(nodeConto);
			}

			// non aggiungo il sottoconto con sottoconto codice vuoto perche' e' un sottoconto cliente o fornitore che
			// non serve visualizzare
			if (!saldoContoConfronto.getSottoContoCodice().equals(SottoConto.DEFAULT_CODICE)) {
				currentSottoconto = (SaldoContoConfronto) PanjeaSwingUtil.cloneObject(saldoContoConfronto);
				// se non ho centro di costo devo mettere l'importo di controllo = all'importo
				nodeSottoConto = new DefaultMutableTreeTableNode(currentSottoconto);
				nodeConto.add(nodeSottoConto);

				if (currentSottoconto.getCentriCostoConfronto() != null) {
					for (SaldoContoConfronto centroCosto : currentSottoconto.getCentriCostoConfronto()) {
						RigaContoCentroCosto saldoCentroCosto = new RigaContoCentroCosto(
								centroCosto.getCentroCostoCodice(), centroCosto.getCentroCostoDescrizione(),
								centroCosto.getCentroCostoId(), centroCosto.getTipoConto(),
								centroCosto.getSottoTipoConto(), centroCosto.getImportoDare(),
								centroCosto.getImportoAvere());
						RigaContoCentroCostoConfronto rigaContoCentroCosto = new RigaContoCentroCostoConfronto(
								saldoCentroCosto, centroCosto.getImportoDare2(), centroCosto.getImportoAvere2());
						nodeSottoConto.add(new DefaultMutableTreeTableNode(rigaContoCentroCosto));
					}
				}

			}

			// aggiorno i riferimenti di old mastro e old conto per il sottoconto successivo
			oldRiga = (SaldoContoConfronto) PanjeaSwingUtil.cloneObject(saldoContoConfronto);
		}

		return rootNode;
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

	/**
	 * Carica le righe contabili Gestisce inoltre lo stato enabled sul command di stampa.
	 * 
	 * @return List<RigaContabile>
	 */
	private List<SaldoContoConfronto> getRigheBilancio() {
		// caso forzato in cui setto il form object a null per pulire i risultati ricerca
		// in ParametriRicercaControlloMovimentoContabilitaPage nella execute di resetRicercaCommand
		// lancio il propertyChange della property IPageLifecycleAdvisor.OBJECT_CHANGED con value a null
		// cosi che l'editor risetta il formobject a null per entrambe le dialog page.
		if (this.parametriRicercaBilancioConfronto != null
				&& this.parametriRicercaBilancioConfronto.isEffettuaRicerca()) {
			righeBilancioConfronto = contabilitaBD.caricaBilancioConfronto(parametriRicercaBilancioConfronto);
		} else {
			righeBilancioConfronto = new ArrayList<SaldoContoConfronto>();
		}
		return righeBilancioConfronto;
	}

	/**
	 * @return stampaCommandGroup
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
	 * @return stampaContoGroup
	 */
	public AbstractCommand getStampaContoCommand() {
		if (stampaContoCommand == null) {
			stampaContoCommand = new StampaCommand(STAMPA_CONTO_COMMAND, true, false);
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
			stampaMastroCommand = new StampaCommand(STAMPA_MASTRO_COMMAND, false, false);
			stampaMastroCommand.setEnabled(false);
		}
		return stampaMastroCommand;
	}

	/**
	 * @return stampaSottoContoCommand
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

				if (node.getUserObject() instanceof RigaMastroConfronto) {
					c.setIcon(iconSource.getIcon(Mastro.class.getName()));
					c.setText(((RigaMastroConfronto) node.getUserObject()).getMastroCodice());
				} else if (node.getUserObject() instanceof RigaContoConfronto) {
					c.setIcon(iconSource.getIcon(Conto.class.getName()));
					c.setText(((RigaContoConfronto) node.getUserObject()).getContoCodice());
				} else if (node.getUserObject() instanceof RigaContoCentroCostoConfronto) {
					c.setIcon(iconSource.getIcon(CentroCosto.class.getName()));
					c.setText(((RigaContoCentroCostoConfronto) node.getUserObject()).getCentroCostoCodice());
				} else if (node.getUserObject() instanceof SaldoContoConfronto) {
					c.setIcon(iconSource.getIcon(SottoConto.class.getName()));
					c.setText(((SaldoConto) node.getUserObject()).getSottoContoCodice());
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
		return new RigheBilancioConfrontoTableModel(root);
	}

	@Override
	public void loadData() {
		setTableData(createTreeNode(getRigheBilancio()));
		getStampaMastroCommand().setEnabled(righeBilancioConfronto.size() != 0);
		getStampaContoCommand().setEnabled(righeBilancioConfronto.size() != 0);
		getStampaSottoContoCommand().setEnabled(righeBilancioConfronto.size() != 0);
		getExpandCommand().setEnabled(righeBilancioConfronto.size() != 0);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	/**
	 * Apre il report bilancio a confronto.
	 * 
	 * @param showconto
	 *            mostra conto
	 * @param showsottoconto
	 *            mostra sottoconto
	 */
	private void openReportRisultati(boolean showconto, boolean showsottoconto) {
		JecLocalReport jecJasperReport = new JecLocalReport();
		jecJasperReport.setReportName("Bilancio a confronto");
		jecJasperReport.setDataReport(righeBilancioConfronto);
		jecJasperReport.setXmlReportResource(new ClassPathResource(
				"/it/eurotn/panjea/contabilita/rich/reports/resources/StampaBilancioConfronto.jasper"));
		jecJasperReport.setMessages("it.eurotn.panjea.messages.cont.reports.messages");
		jecJasperReport.getReportParameters().put("stampaConto", showconto);
		jecJasperReport.getReportParameters().put("stampaSottoConto", showsottoconto);
		jecJasperReport.getReportParameters().put("htmlParameters",
				this.parametriRicercaBilancioConfronto.getHtmlParameters());
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
				final ParametriRicercaEstrattoConto parametriRicercaEstrattoConto = new ParametriRicercaEstrattoConto();
				parametriRicercaEstrattoConto.setSottoConto(contabilitaAnagraficaBD.caricaSottoConto(saldoConto
						.getSottoContoId()));
				parametriRicercaEstrattoConto.setCentroCosto(parametriRicercaBilancioConfronto.getCentroCosto());
				parametriRicercaEstrattoConto.setStatiAreaContabile(parametriRicercaBilancioConfronto
						.getStatiAreaContabile());
				parametriRicercaEstrattoConto.setEffettuaRicerca(true);
				ConfirmationDialog dialog = new ConfirmationDialog("Selezione periodo",
						"Selezionare il periodo desiderato") {

					@Override
					protected String getCancelCommandId() {
						return "cancelCommand";
					}

					@Override
					protected Object[] getCommandGroupMembers() {
						ActionCommand primoPeriodoCommand = new ApplicationWindowAwareCommand("primoPeriodoCommand") {

							@Override
							protected void doExecuteCommand() {
								onCancel();
								parametriRicercaEstrattoConto.setAnnoCompetenza(parametriRicercaBilancioConfronto
										.getAnnoCompetenza());
								parametriRicercaEstrattoConto.setDataRegistrazione(parametriRicercaBilancioConfronto
										.getDataRegistrazione());
								LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaEstrattoConto);
								Application.instance().getApplicationContext().publishEvent(event);
								onCancel();
							}
						};
						RcpSupport.configure(primoPeriodoCommand);

						return new Object[] { primoPeriodoCommand, getFinishCommand(), getCancelCommand() };
					}

					@Override
					protected String getFinishCommandId() {
						return "secondoPeriodoCommand";
					}

					@Override
					protected void onConfirm() {
						parametriRicercaEstrattoConto.setAnnoCompetenza(parametriRicercaBilancioConfronto
								.getAnnoCompetenza2());
						parametriRicercaEstrattoConto.setDataRegistrazione(parametriRicercaBilancioConfronto
								.getDataRegistrazione2());
						LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaEstrattoConto);
						Application.instance().getApplicationContext().publishEvent(event);
					}
				};
				dialog.showDialog();
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
		if (object instanceof ParametriRicercaBilancioConfronto) {
			this.parametriRicercaBilancioConfronto = (ParametriRicercaBilancioConfronto) object;
		} else {
			this.parametriRicercaBilancioConfronto = new ParametriRicercaBilancioConfronto();
		}
	}
}
