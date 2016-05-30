package it.eurotn.panjea.auvend.rich.editors.importazioni.carichi;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.auvend.domain.Articolo;
import it.eurotn.panjea.auvend.domain.Documento;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.rich.editors.importazioni.ArticoliMancantiComponent;
import it.eurotn.panjea.auvend.rich.editors.importazioni.MovimentiDaAggiornareComponent;
import it.eurotn.panjea.auvend.rich.editors.importazioni.StatoImportazioneDepositiComponent;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.util.Assert;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.GroupingList;

import com.toedter.calendar.JDateChooser;

public class ImportazioneCarichiTablePage extends AbstractTablePageEditor<ParametriImportazioneCarichi> implements
		InitializingBean {

	private class SediActionListener implements ActionListener {

		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			depositiSedeSelezionata = (List<Deposito>) ((JComboBox) e.getSource()).getSelectedItem();
			refreshData();
		}

	}

	private class SediListCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -4864803095092309721L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			@SuppressWarnings("unchecked")
			List<Deposito> listDep = (List<Deposito>) value;

			if (listDep != null) {
				label.setText(listDep.get(0).getSedeDeposito().getSede().getDescrizione());
				label.setIcon(getIconSource().getIcon(SedeEntita.class.getName()));
			}

			return label;
		}

	}

	private static Logger logger = Logger.getLogger(ImportazioneCarichiTablePage.class);

	public static final String PAGE_ID = "importazioneCarichiTablePage";

	private JDateChooser dataFineImportazione;

	private IAuVendBD auVendBD;
	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private GroupingList<Deposito> groupingListDepositi;

	private Map<Deposito, LetturaFlussoAuVend> mapLetture;
	private List<Deposito> depositiSedeSelezionata = null;

	private VerificaImportazioneCarichiCommand verificaImportazioneCarichiCommand;

	private ImportaCarichiCommand importaCarichiCommand;

	private MovimentiDaAggiornareComponent movimentiDaAggiornareComponent;
	private ArticoliMancantiComponent articoliMancantiComponent;

	private StatoImportazioneDepositiComponent statoImportazioneDepositiComponent;

	private JComboBox cmbSedi;

	/**
	 * Costruttore.
	 * 
	 */
	public ImportazioneCarichiTablePage() {
		super(PAGE_ID, new String[] { "selezionato", "letturaFlussoAuVend.deposito",
				"letturaFlussoAuVend.ultimaLetturaFlussoCarichi", "statisticaImportazione.articoliMancanti" },
				ParametriImportazioneCarichi.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.auVendBD, "AuVendBD non può essere nullo.");
		Assert.notNull(this.magazzinoDocumentoBD, "MagazzinoDocumentoBD non può essere nullo.");

		movimentiDaAggiornareComponent = new MovimentiDaAggiornareComponent(this.magazzinoDocumentoBD);
		articoliMancantiComponent = new ArticoliMancantiComponent();
		statoImportazioneDepositiComponent = new StatoImportazioneDepositiComponent();
	}

	/**
	 * @param map
	 *            statistiche di importazione da aggiungere
	 */
	public void aggiungiStatisticheImportazione(Map<String, StatisticaImportazione> map) {

		for (ParametriImportazioneCarichi parametriImportazioneCarichi : getTable().getRows()) {
			// se trovo il deposito nella mappa aggiungo le sue statistiche
			if (map.containsKey(parametriImportazioneCarichi.getLetturaFlussoAuVend().getDeposito().getCodice())) {
				parametriImportazioneCarichi.setStatisticaImportazione(map.get(parametriImportazioneCarichi
						.getLetturaFlussoAuVend().getDeposito().getCodice()));
			}
		}

		importaCarichiCommand.setEnabled(false);
		updateAnomalieDepositi();
		// cancello l'eventuale risultato importazione precedente
		statoImportazioneDepositiComponent.clearStatoDepositi();

	}

	/**
	 * @return crea i parametri di importazione per i carichi
	 */
	private List<ParametriImportazioneCarichi> createParametriImportazioneCarichi() {
		List<ParametriImportazioneCarichi> listParametri = new ArrayList<ParametriImportazioneCarichi>();

		if (this.depositiSedeSelezionata != null) {
			for (Deposito deposito : this.depositiSedeSelezionata) {
				listParametri.add(new ParametriImportazioneCarichi(mapLetture.get(deposito)));
			}
		}

		return listParametri;
	}

	/**
	 * crea il pannello che contiene la combobox per la scelta della sede.
	 * 
	 * @return pannello creato
	 */
	private JComponent createSediControl() {
		JPanel sediPanel = getComponentFactory().createPanel(new BorderLayout());
		sediPanel.setBorder(BorderFactory.createTitledBorder("Sedi"));
		sediPanel.setPreferredSize(new Dimension(300, 50));

		cmbSedi = new JComboBox();
		for (List<Deposito> list : groupingListDepositi) {
			cmbSedi.addItem(list);
		}
		cmbSedi.setRenderer(new SediListCellRenderer());
		cmbSedi.addActionListener(new SediActionListener());
		sediPanel.add(cmbSedi, BorderLayout.CENTER);

		return sediPanel;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getImportaCarichiCommand(), getVerificaImportazioneCarichiCommand() };
	}

	/**
	 * @return the dataFineImportazione
	 */
	public Date getDataFineImportazione() {
		return dataFineImportazione.getDate();
	}

	/**
	 * @return the depositiSedeSelezionata
	 */
	public List<Deposito> getDepositiSedeSelezionata() {
		return depositiSedeSelezionata;
	}

	/**
	 * Restituisce la lista di tutti i depositi selezionati.
	 * 
	 * @return lista
	 */
	private List<Deposito> getDepositiSelezionati() {

		List<Deposito> listDepositi = new ArrayList<Deposito>();

		for (ParametriImportazioneCarichi parametriImportazione : getTable().getRows()) {
			if (parametriImportazione.getSelezionato()) {
				listDepositi.add(parametriImportazione.getLetturaFlussoAuVend().getDeposito());
			}
		}
		return listDepositi;
	}

	@Override
	public JComponent getFooterControl() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(articoliMancantiComponent.getControl());
		panel.add(movimentiDaAggiornareComponent.getControl());
		panel.add(statoImportazioneDepositiComponent.getControl());

		// rootPanel.add(createRadioButtonAnomalieControl(), BorderLayout.NORTH);
		rootPanel.add(panel, BorderLayout.CENTER);

		return rootPanel;
	}

	@Override
	public JComponent getHeaderControl() {
		prepareData();

		JPanel rootPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

		JPanel dataPanel = getComponentFactory().createPanel(new BorderLayout());
		dataPanel.setBorder(BorderFactory.createTitledBorder("Data riferimento"));
		dataPanel.setPreferredSize(new Dimension(200, 50));
		this.dataFineImportazione = new JDateChooser(Calendar.getInstance().getTime(), "dd/MM/yyyy");
		dataPanel.add(this.dataFineImportazione, BorderLayout.CENTER);

		rootPanel.add(createSediControl());
		rootPanel.add(dataPanel);

		return rootPanel;
	}

	/**
	 * @return importaCarichiCommand
	 */
	public ImportaCarichiCommand getImportaCarichiCommand() {
		if (importaCarichiCommand == null) {
			importaCarichiCommand = new ImportaCarichiCommand(this);
			importaCarichiCommand.setEnabled(false);
		}

		return importaCarichiCommand;
	}

	/**
	 * @return verificaImportazioneCarichiCommand
	 */
	public VerificaImportazioneCarichiCommand getVerificaImportazioneCarichiCommand() {
		if (verificaImportazioneCarichiCommand == null) {
			verificaImportazioneCarichiCommand = new VerificaImportazioneCarichiCommand(this, this.auVendBD);
		}

		return verificaImportazioneCarichiCommand;
	}

	/**
	 * Importa i depositi selezionati.
	 */
	public void importaDepositiSelezionati() {
		/*
		 * List<Deposito> listDepositiDaImportare = getDepositiSelezionati();
		 * 
		 * statoImportazioneDepositiComponent.clearStatoDepositi();
		 * statoImportazioneDepositiComponent.setNumeroDepositiDaImportare(listDepositiDaImportare.size());
		 * 
		 * // importo i carichi dei depositi selezionati try { for (Deposito deposito : listDepositiDaImportare) {
		 * statoImportazioneDepositiComponent.aggiungiDeposito(deposito); if
		 * (!statoImportazioneDepositiComponent.isCancelImport()) { boolean depositoImportato = auVendBD
		 * .importaCarichi(deposito.getCodice(), getDataFineImportazione());
		 * statoImportazioneDepositiComponent.aggiornaStatoDeposito(deposito, depositoImportato); } } } catch (Exception
		 * e) { logger.error("-->errore nell 'importazione dei depositi ", e); } finally { // Recupero la data minore di
		 * aggiornamento per partire da lì ad aggiornare il // datawarehouse Map<Deposito, LetturaFlussoAuVend>
		 * letturaflussoCarichi = auVendBD.caricaLettureFlussoCarichi(); Date dataAggiornamento =
		 * Calendar.getInstance().getTime(); for (Entry<Deposito, LetturaFlussoAuVend> entry :
		 * letturaflussoCarichi.entrySet()) { if
		 * (entry.getValue().getUltimaLetturaFlussoCarichi().before(dataAggiornamento)) { dataAggiornamento =
		 * entry.getValue().getUltimaLetturaFlussoCarichi(); } }
		 * magazzinoAnagraficaBD.asyncAggiornaMovimenti(dataAggiornamento); } prepareData(); loadData();
		 */
	}

	@Override
	public Collection<ParametriImportazioneCarichi> loadTableData() {
		return createParametriImportazioneCarichi();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onPostPageOpen() {
		this.depositiSedeSelezionata = (List<Deposito>) cmbSedi.getSelectedItem();
		loadData();
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void postSetFormObject(Object object) {
		super.postSetFormObject(object);
	}

	/**
	 * Prepara i dati per l'importazione.
	 */
	private void prepareData() {
		mapLetture = this.auVendBD.caricaLettureFlussoCarichi();
		Comparator<Deposito> comparator = new Comparator<Deposito>() {
			@Override
			public int compare(Deposito o1, Deposito o2) {
				return o1.getSedeDeposito().getId().compareTo(o2.getSedeDeposito().getId());
			}
		};

		BasicEventList<Deposito> basicEventList = new BasicEventList<Deposito>();
		basicEventList.addAll(mapLetture.keySet());
		groupingListDepositi = new GroupingList<Deposito>(basicEventList, comparator);
	}

	@Override
	public void processTableData(Collection<ParametriImportazioneCarichi> results) {
		super.processTableData(results);
		this.importaCarichiCommand.setEnabled(false);
	}

	@Override
	public Collection<ParametriImportazioneCarichi> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param auVendBD
	 *            the auVendBD to set
	 */
	public void setAuVendBD(IAuVendBD auVendBD) {
		this.auVendBD = auVendBD;
	}

	@Override
	public void setFormObject(Object object) {

	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            the magazzinoDocumentoBD to set
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	/**
	 * Aggiorna tutte le anomalie dei depositi.
	 */
	private void updateAnomalieDepositi() {
		Set<Articolo> setArticoli = new TreeSet<Articolo>();
		Set<Documento> setDocumenti = new TreeSet<Documento>();
		for (ParametriImportazioneCarichi parametriImportazione : getTable().getRows()) {
			parametriImportazione.setSelezionato(true);
			if (parametriImportazione.getStatisticaImportazione() != null
					&& parametriImportazione.getStatisticaImportazione().getArticoliMancanti() != null) {
				setArticoli.addAll(parametriImportazione.getStatisticaImportazione().getArticoliMancanti());
				parametriImportazione.setSelezionato(false);

			}

			if (parametriImportazione.getStatisticaImportazione() != null
					&& parametriImportazione.getStatisticaImportazione().getDocumentiDaAggiornare() != null) {
				setDocumenti.addAll(parametriImportazione.getStatisticaImportazione().getDocumentiDaAggiornare());
				parametriImportazione.setSelezionato(false);
			}

			importaCarichiCommand.setEnabled(parametriImportazione.getSelezionato());

		}
		articoliMancantiComponent.aggiungiArticoliMancanti(setArticoli);
		movimentiDaAggiornareComponent.aggiungiDocumentiDaAggiornare(setDocumenti);
	}
}
