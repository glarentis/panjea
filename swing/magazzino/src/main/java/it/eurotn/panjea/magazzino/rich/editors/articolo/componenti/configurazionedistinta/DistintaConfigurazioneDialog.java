package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.ArticoloComponentiPage;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

public class DistintaConfigurazioneDialog extends PanjeaTitledPageApplicationDialog {

	private ArticoloComponentiPage page = null;
	private Closure closureOnfinish = null;

	/**
	 * Costruttore.
	 *
	 * @param articoloLite
	 *            articoloLite
	 * @param idConfigurazioneCollegata
	 *            idConfigurazioneCollegata
	 * @param closureOnfinish
	 *            closureOnfinish
	 */
	public DistintaConfigurazioneDialog(final ArticoloLite articoloLite, final Integer idConfigurazioneCollegata,
			final Closure closureOnfinish) {
		super();
		this.page = RcpSupport.getBean("articoloComponentiPage");
		this.closureOnfinish = closureOnfinish;
		setDialogPage(page);
		IMagazzinoAnagraficaBD magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
		page.setMagazzinoAnagraficaBD(magazzinoAnagraficaBD);
		page.getControl();
		page.setArticoloConfigurazione(articoloLite, idConfigurazioneCollegata);
	}

	@Override
	protected boolean onFinish() {
		ArticoloConfigurazioneDistinta configurazioneCorrente = (ArticoloConfigurazioneDistinta) page
				.getConfigurazioneCorrente();
		if (configurazioneCorrente != null && closureOnfinish != null) {
			closureOnfinish.call(configurazioneCorrente.getConfigurazioneDistinta());
		}
		return true;
	}

	// private class CancellaComponenteAction extends AbstractAction {
	// private static final long serialVersionUID = 4747623287231034068L;
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// int selectedIndex = table.getSelectedRow();
	// if (selectedIndex == -1) {
	// return;
	// }
	// Componente componenteSelezionato = ((ComponenteRow) table.getRowAt(table.getActualRowAt(selectedIndex)))
	// .getComponente();
	// componenteSelezionato
	// .setConfigurazioneComponente((ConfigurazioneDistinta) configurazioneSelezionataValueModel
	// .getValue());
	// bd.cancellaComponenteConfigurazioneDistinta(componenteSelezionato);
	// treeModel.removeRow(table.getActualRowAt(table.getSelectedRow()));
	// if (selectedIndex > (treeModel.getRowCount() - 1)) {
	// selectedIndex--;
	// }
	// table.getSelectionModel().setSelectionInterval(selectedIndex, selectedIndex);
	// }
	// }
	//
	// private class ConfiguazioneSelezionataChangeListener implements PropertyChangeListener {
	//
	// @Override
	// public void propertyChange(PropertyChangeEvent evt) {
	// ConfigurazioneDistinta c = (ConfigurazioneDistinta) configurazioneSelezionataValueModel.getValue();
	// Articolo articolo = distinta.creaProxyArticolo();
	// c.setDistinta(articolo);
	// treeModel = new ConfigurazioneDistintaTableModel(bd.caricaComponenti(c));
	// table.setModel(treeModel);
	// table.expandAll();
	// articolo.setFasiLavorazioneArticolo(bd.caricaFasiLavorazioneArticolo(c));
	// formFasi.setFormObject(c);
	//
	// cancellaConf.setEnabled(!(c instanceof ConfigurazioneDistintaBase));
	// nuovaConf.setEnabled(c instanceof ConfigurazioneDistintaBase);
	// nuovaConfPersonalizzata.setEnabled(c instanceof ConfigurazioneDistintaBase);
	// cancellaComponenteAction.setEnabled(!(c instanceof ConfigurazioneDistintaBase));
	// inserisciComponenteAction.setEnabled(!(c instanceof ConfigurazioneDistintaBase));
	// inserisciComponenteRootAction.setEnabled(!(c instanceof ConfigurazioneDistintaBase));
	// sostituisciComponenteAction.setEnabled(!(c instanceof ConfigurazioneDistintaBase));
	// }
	// }
	//
	// private class ConfigurazioniDistintaClosure implements Closure {
	//
	// @Override
	// public Object call(Object paramObject) {
	// List<ConfigurazioneDistinta> configurazioni = bd.caricaConfigurazioniDistinta(distinta);
	// // Se la conf. collegata è una personalizzata la aggiungo (non è contenuta nelle configurazioni std)
	// if (configurazioneCollegata != null && !configurazioni.contains(configurazioneCollegata)) {
	// configurazioni.add(0, configurazioneCollegata);
	// }
	// return configurazioni;
	// }
	//
	// }
	//
	// private class InserisciComponenteAction extends AbstractAction {
	// private static final long serialVersionUID = 1L;
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// RicercaAvanzataArticoliCommand ricercaCommand = new RicercaAvanzataArticoliCommand();
	// ricercaCommand.execute();
	//
	// ComponenteRow rigaSelezionata = (ComponenteRow) table
	// .getRowAt(table.getActualRowAt(table.getSelectedRow()));
	// // bloccare o forse se non presente aggiungerlo al root ?
	// if (rigaSelezionata == null) {
	// return;
	// }
	// ConfigurazioneDistinta configurazioneDistinta = (ConfigurazioneDistinta) configurazioneSelezionataValueModel
	// .getValue();
	// List<ArticoloRicerca> articoliDaInserire = ricercaCommand.getArticoliSelezionati();
	// for (ArticoloRicerca articoloRicerca : articoliDaInserire) {
	// ArticoloLite articoloDaInserire = bd.caricaArticoloLite(articoloRicerca.getId());
	// // può essere null se elimino tutte le righe della configurazione/personalizzazione
	// Componente componente = bd.aggiungiComponenteAConfigurazione(configurazioneDistinta,
	// rigaSelezionata.getComponente(), articoloDaInserire.creaProxyArticolo());
	// ComponenteRow rigaDaAggiungere = treeModel.creaRiga(componente);
	// rigaSelezionata.addChild(rigaDaAggiungere);
	// treeModel.expandAll();
	// }
	// }
	// }
	//
	// private class InserisciComponenteRootAction extends AbstractAction {
	// private static final long serialVersionUID = 1L;
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// RicercaAvanzataArticoliCommand ricercaCommand = new RicercaAvanzataArticoliCommand();
	// ricercaCommand.execute();
	//
	// ConfigurazioneDistinta configurazioneDistinta = (ConfigurazioneDistinta) configurazioneSelezionataValueModel
	// .getValue();
	// List<ArticoloRicerca> articoliDaInserire = ricercaCommand.getArticoliSelezionati();
	// for (ArticoloRicerca articoloRicerca : articoliDaInserire) {
	// ArticoloLite articoloDaInserire = bd.caricaArticoloLite(articoloRicerca.getId());
	// Componente componente = bd.aggiungiComponenteAConfigurazione(configurazioneDistinta, null,
	// articoloDaInserire.creaProxyArticolo());
	// ComponenteRow rigaDaAggiungere = treeModel.creaRiga(componente);
	// treeModel.addRow(rigaDaAggiungere);
	// treeModel.expandAll();
	// }
	// }
	// }
	//
	// private class SostituisciComponenteAction extends AbstractAction {
	// private static final long serialVersionUID = 1L;
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	//
	// int selectedIndex = table.getSelectedRow();
	// if (selectedIndex == -1) {
	// return;
	// }
	//
	// ComponenteRow rigaSelezionata = ((ComponenteRow) table.getRowAt(table.getActualRowAt(selectedIndex)));
	// Componente componenteSelezionato = rigaSelezionata.getComponente();
	// ArticoloLite articoloSelezionato = componenteSelezionato.getArticolo();
	// Articolo articolo = bd.caricaArticolo(articoloSelezionato.creaProxyArticolo(), false);
	// ConfigurazioneDistinta configurazioneDistinta = (ConfigurazioneDistinta) configurazioneSelezionataValueModel
	// .getValue();
	//
	// componenteSelezionato.setConfigurazioneComponente(configurazioneDistinta);
	//
	// ArticoliAlternativiDialog articoliAlternativiDialog = new ArticoliAlternativiDialog(articolo);
	// articoliAlternativiDialog.showDialog();
	//
	// ArticoloRicerca articoloSostitutivoRicerca = articoliAlternativiDialog.getArticoloSelezionato();
	// if (articoloSostitutivoRicerca != null) {
	// // Cancello il componente corrente
	// bd.cancellaComponenteConfigurazioneDistinta(componenteSelezionato);
	//
	// // Inserisco nel db il nuovo componente
	// // Recupero il componente padre
	// ComponenteRow rigaPadre = rigaSelezionata.getParent() instanceof ComponenteRow ? (ComponenteRow) rigaSelezionata
	// .getParent() : null;
	// Componente componentePadre = rigaPadre != null ? rigaPadre.getComponente() : null;
	//
	// // Aggiungo il nuovo articolo al padre
	// Articolo articoloSostitutivo = new Articolo();
	// articoloSostitutivo.setId(articoloSostitutivoRicerca.getId());
	// articoloSostitutivo.setVersion(articoloSostitutivoRicerca.getVersion());
	// Componente componente = bd.aggiungiComponenteAConfigurazione(configurazioneDistinta, componentePadre,
	// articoloSostitutivo);
	// componente.setFormula(componenteSelezionato.getFormula());
	//
	// // Aggiorno la gui
	// ComponenteRow rigaDaAggiungere = treeModel.creaRiga(componente);
	//
	// // Aggiungo la riga al parent
	// rigaSelezionata.getParent().addChild(rigaDaAggiungere);
	// // Rimuovo la selezionata
	// treeModel.removeRow(rigaSelezionata);
	// treeModel.expandAll();
	// table.getSelectionModel().setSelectionInterval(selectedIndex, selectedIndex);
	// }
	// }
	// }
	//
	// private static final String KEY_DIALOG_TITLE = "distintaConfigurazioneDialog.title";
	// private ArticoloLite distinta;
	// private IMagazzinoAnagraficaBD bd;
	// private ConfigurazioneDistintaTableModel treeModel;
	// private TreeTable table;
	// private RefreshableValueHolder configurazioniDistintaValueHolder;
	// private ValueModel configurazioneSelezionataValueModel;
	// private CommandGroup toolbarCommand;
	// private NuovaConfigurazioneDistintaCommand nuovaConf;
	// private NuovaConfigurazioneDistintaPersonalizzataCommand nuovaConfPersonalizzata;
	// private ActionCommand cancellaConf;
	// private AbstractAction cancellaComponenteAction;
	// private AbstractAction inserisciComponenteAction;
	// private AbstractAction inserisciComponenteRootAction;
	// private ConfigurazioneDistinta configurazioneCollegata;
	// private Closure closureOnfinish;
	// private boolean addPersonalizzataCommand;
	// private FasiLavorazioneForm formFasi;
	// private SostituisciComponenteAction sostituisciComponenteAction;
	//
	// /**
	// *
	// * @param distinta
	// * distinta da configuarare
	// * @param closureOnfinish
	// * closure richiamata quando chiudo il dialogo e contenente la distinta sel. Null se preme annulla.
	// * @param addPersonalizzataCommand
	// * addPersonalizzataCommand
	// */
	// public DistintaConfigurazioneDialog(final ArticoloLite distinta, final Closure closureOnfinish,
	// final boolean addPersonalizzataCommand) {
	// super();
	// this.distinta = distinta;
	// this.closureOnfinish = closureOnfinish;
	// this.addPersonalizzataCommand = addPersonalizzataCommand;
	// this.distinta.setVersion(0);
	// bd = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
	// // ConfigurazioneDistintaBase base = new ConfigurazioneDistintaBase(articolo);
	// // articolo.setFasiLavorazioneArticolo(bd.caricaFasiLavorazioneArticolo(base));
	// }
	//
	// /**
	// *
	// * @param idConfigurazioneCollegata
	// * id configurazione collegata ad una riga ordine. Solo l'id perchè non volgio legate package magazzino a
	// * package ordine
	// * @param closureOnfinish
	// * closure richiamata quando chiudo il dialogo e contenente la distinta sel. Null se preme annulla.
	// */
	// public DistintaConfigurazioneDialog(final int idConfigurazioneCollegata, final Closure closureOnfinish) {
	// super();
	// this.closureOnfinish = closureOnfinish;
	// addPersonalizzataCommand = true;
	// bd = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
	// configurazioneCollegata = bd.caricaConfigurazioneDistinta(idConfigurazioneCollegata);
	// if (configurazioneCollegata == null) {
	// throw new IllegalArgumentException("La configurazione collegata per la riga articolo nopn esiste "
	// + idConfigurazioneCollegata);
	// }
	// this.distinta = configurazioneCollegata.getDistinta().getArticoloLite();
	// }
	//
	// @Override
	// protected void addDialogComponents() {
	// super.addDialogComponents();
	// getCancelCommand().setVisible(false);
	// }
	//
	// @Override
	// protected JComponent createDialogContentPane() {
	// if (configurazioneCollegata == null) {
	// configurazioneCollegata = new ConfigurazioneDistintaBase(distinta.creaProxyArticolo());
	// } else {
	// configurazioneCollegata.getDistinta().setFasiLavorazioneArticolo(
	// bd.caricaFasiLavorazioneArticolo(configurazioneCollegata));
	// }
	// // if (configurazioneCollegata != null) {
	// treeModel = new ConfigurazioneDistintaTableModel(bd.caricaComponenti(configurazioneCollegata));
	// // } else {
	// // treeModel = new ConfigurazioneDistintaTableModel(bd.caricaComponenti(distinta.getId()));
	// // }
	//
	// JPanel rootPanel = new JPanel(new VerticalLayout(2));
	//
	// table = new TreeTable(treeModel);
	// table.expandAll();
	// table.setRowHeight(25);
	// table.sortColumn(0);
	//
	// cancellaComponenteAction = new CancellaComponenteAction();
	// inserisciComponenteAction = new InserisciComponenteAction();
	// inserisciComponenteRootAction = new InserisciComponenteRootAction();
	// sostituisciComponenteAction = new SostituisciComponenteAction();
	//
	// cancellaComponenteAction.setEnabled(false);
	// inserisciComponenteAction.setEnabled(false);
	// inserisciComponenteRootAction.setEnabled(false);
	// sostituisciComponenteAction.setEnabled(false);
	//
	// table.getInputMap(JTable.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "cancellaComponente");
	// table.getActionMap().put("cancellaComponente", cancellaComponenteAction);
	// table.getInputMap(JTable.WHEN_FOCUSED)
	// .put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), "inserisciComponente");
	// table.getActionMap().put("inserisciComponente", inserisciComponenteAction);
	// table.getInputMap(JTable.WHEN_FOCUSED).put(
	// KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.CTRL_DOWN_MASK), "inserisciComponenteRoot");
	// table.getActionMap().put("inserisciComponenteRoot", inserisciComponenteRootAction);
	// table.getInputMap(JTable.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
	// "sostituisciComponente");
	// table.getActionMap().put("sostituisciComponente", sostituisciComponenteAction);
	//
	// JScrollPane sp = new JScrollPane(table);
	// rootPanel.add(sp);
	// formFasi = new FasiLavorazioneForm(configurazioneCollegata, bd);
	// rootPanel.add(formFasi.getControl());
	// rootPanel.add(createToolBar());
	//
	// return rootPanel;
	// }
	//
	// /**
	// * @return toolbar JPanel
	// */
	// @SuppressWarnings("unchecked")
	// private JPanel createToolBar() {
	// JPanel toolbar = new JPanel(new HorizontalLayout());
	//
	// final JComboBox<ConfigurazioneDistinta> configurazioniComboBox = new ComboBoxConfigurazioni();
	//
	// configurazioneSelezionataValueModel = new ValueHolder();
	//
	// configurazioniDistintaValueHolder = new RefreshableValueHolder(new ConfigurazioniDistintaClosure());
	//
	// configurazioniDistintaValueHolder.refresh();
	// if (configurazioneCollegata != null) {
	// configurazioneSelezionataValueModel.setValue(configurazioneCollegata);
	// }
	//
	// DynamicComboBoxListModel configurazioniModel = new DynamicComboBoxListModel(
	// configurazioneSelezionataValueModel, configurazioniDistintaValueHolder);
	//
	// toolbar.add(configurazioniComboBox);
	// configurazioniComboBox.setModel(configurazioniModel);
	//
	// nuovaConf = new NuovaConfigurazioneDistintaCommand(bd, configurazioniDistintaValueHolder,
	// configurazioneSelezionataValueModel);
	//
	// nuovaConfPersonalizzata = new NuovaConfigurazioneDistintaPersonalizzataCommand(bd,
	// configurazioniDistintaValueHolder, configurazioneSelezionataValueModel);
	//
	// cancellaConf = new CancellaConfigurazioneDistintaCommand(bd, configurazioniDistintaValueHolder,
	// configurazioneSelezionataValueModel);
	// cancellaConf.setEnabled(false);
	//
	// toolbarCommand = new JECCommandGroup();
	// toolbarCommand.add(nuovaConf);
	// if (addPersonalizzataCommand) {
	// toolbarCommand.add(nuovaConfPersonalizzata);
	// }
	// toolbarCommand.add(cancellaConf);
	// toolbar.add(toolbarCommand.createToolBar());
	//
	// ConfiguazioneSelezionataChangeListener changeListener = new ConfiguazioneSelezionataChangeListener();
	// configurazioneSelezionataValueModel.addValueChangeListener(new ConfiguazioneSelezionataChangeListener());
	// changeListener.propertyChange(null);
	//
	// return toolbar;
	// }
	//
	// @Override
	// protected Object[] getCommandGroupMembers() {
	// return new AbstractCommand[] { getFinishCommand() };
	// }
	//
	// @Override
	// protected String getTitle() {
	// return RcpSupport.getMessage(KEY_DIALOG_TITLE);
	// }
	//
	// @Override
	// protected boolean onFinish() {
	// if (configurazioneSelezionataValueModel.getValue() != null && closureOnfinish != null) {
	// closureOnfinish.call(configurazioneSelezionataValueModel.getValue());
	// }
	// return true;
	// }

}
