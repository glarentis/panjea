package it.eurotn.panjea.auvend.rich.editors.importazioni.fatture;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.auvend.domain.Articolo;
import it.eurotn.panjea.auvend.domain.Cliente;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.rich.editors.importazioni.ArticoliMancantiComponent;
import it.eurotn.panjea.auvend.rich.editors.importazioni.ClientiDaVerificareComponent;
import it.eurotn.panjea.auvend.rich.editors.importazioni.StatoImportazioneDepositiComponent;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.util.Assert;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.GroupingList;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.toedter.calendar.JDateChooser;

public class ImportazioneFattureTablePage extends AbstractTablePageEditor<ParametriImportazioneFatture> implements
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
		public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

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

	public static final String PAGE_ID = "importazioneFattureTablePage";

	private ArticoliMancantiComponent articoliMancantiComponent;
	private StatoImportazioneDepositiComponent statoImportazioneDepositiComponent;
	private ClientiDaVerificareComponent clientiDaVerificareComponent;

	private JComboBox cmbSedi;
	private Map<Deposito, LetturaFlussoAuVend> mapLetture;
	private GroupingList<Deposito> groupingListDepositi;

	private IAuVendBD auVendBD;
	private IAnagraficaBD anagraficaBD;
	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	private List<Deposito> depositiSedeSelezionata = null;

	private JDateChooser dataFineImportazione;

	private VerificaimportazioneFattureCommand verificaimportazioneFattureCommand;

	private ImportaFattureCommand importaFattureCommand;

	/**
	 * Costruttore.
	 * 
	 */
	public ImportazioneFattureTablePage() {
		super(PAGE_ID, new String[] { "selezionato", "letturaFlussoAuVend.deposito",
				"letturaFlussoAuVend.ultimaLetturaFlussoFatture", "statisticaImportazione.numeroMovimentiDaInserire",
				"statisticaImportazione.articoliMancanti", "statisticaImportazione.clientiDaVerificare" },
				ParametriImportazioneFatture.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.auVendBD, "AuVendBD non può essere nullo.");
		Assert.notNull(this.anagraficaBD, "AnagraficaBD non può essere nullo.");

		articoliMancantiComponent = new ArticoliMancantiComponent();
		statoImportazioneDepositiComponent = new StatoImportazioneDepositiComponent();
		clientiDaVerificareComponent = new ClientiDaVerificareComponent(this.anagraficaBD);

		prepareData();
	}

	/**
	 * @param map
	 *            agginge le statistiche passate come parametro
	 */
	public void aggiungiStatisticheImportazione(Map<String, StatisticaImportazione> map) {

		// ricreo la lista di parametriimportazione
		List<ParametriImportazioneFatture> listParametriImp = createParametriImportazioneFatture();

		for (ParametriImportazioneFatture parametriImportazioneFatture : listParametriImp) {
			// se trovo il deposito nella mappa aggiungo le sue statistiche
			if (map.containsKey(parametriImportazioneFatture.getLetturaFlussoAuVend().getDeposito().getCodice())) {
				parametriImportazioneFatture.setStatisticaImportazione(map.get(parametriImportazioneFatture
						.getLetturaFlussoAuVend().getDeposito().getCodice()));
			}
		}

		setRows(selezionaDepositiValidi(listParametriImp));

		this.importaFattureCommand.setEnabled(true);
		updateAnomalieDepositi();
		// cancello l'eventuale risultato importazione precedente
		statoImportazioneDepositiComponent.clearStatoDepositi();
	}

	/**
	 * @return restituisce i parametri di importazione delle fatture
	 */
	private List<ParametriImportazioneFatture> createParametriImportazioneFatture() {
		List<ParametriImportazioneFatture> listParametri = new ArrayList<ParametriImportazioneFatture>();

		if (this.depositiSedeSelezionata != null) {
			for (Deposito deposito : this.depositiSedeSelezionata) {

				listParametri.add(new ParametriImportazioneFatture(mapLetture.get(deposito)));
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
		return new AbstractCommand[] { getImportaFattureCommand(), getVerificaimportazioneFattureCommand() };
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

		for (ParametriImportazioneFatture parametriImportazione : getTable().getRows()) {
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
		panel.add(clientiDaVerificareComponent.getControl());
		panel.add(statoImportazioneDepositiComponent.getControl());

		rootPanel.add(panel, BorderLayout.CENTER);

		return rootPanel;
	}

	@Override
	public JComponent getHeaderControl() {
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
	 * @return importaFattureCommand
	 */
	public ImportaFattureCommand getImportaFattureCommand() {
		if (importaFattureCommand == null) {
			importaFattureCommand = new ImportaFattureCommand(this);
		}

		return importaFattureCommand;
	}

	/**
	 * 
	 * @return verificaimportazioneFattureCommand
	 */
	public VerificaimportazioneFattureCommand getVerificaimportazioneFattureCommand() {
		if (verificaimportazioneFattureCommand == null) {
			verificaimportazioneFattureCommand = new VerificaimportazioneFattureCommand(this, auVendBD);
		}

		return verificaimportazioneFattureCommand;
	}

	/**
	 * Esegue l'importazione dei depositi selezionati.
	 */
	public void importaDepositiSelezionati() {

		List<Deposito> listDepositiDaImportare = getDepositiSelezionati();

		statoImportazioneDepositiComponent.clearStatoDepositi();
		statoImportazioneDepositiComponent.setNumeroDepositiDaImportare(listDepositiDaImportare.size());

		// importo i carichi dei depositi selezionati
		ContabilizzazioneException contabilizzazioneException = new ContabilizzazioneException();

		for (Deposito deposito : listDepositiDaImportare) {
			statoImportazioneDepositiComponent.aggiungiDeposito(deposito);

			boolean depositoImportato = false;

			try {
				depositoImportato = auVendBD.importaFatture(deposito.getCodice(), getDataFineImportazione());
				List<Integer> areeDaContabilizzare = auVendBD.chiudiFatture(deposito.getCodice(),
						getDataFineImportazione());
				magazzinoContabilizzazioneBD.contabilizzaAreeMagazzino(areeDaContabilizzare, true);
			} catch (ContabilizzazioneException e) {
				contabilizzazioneException.add(e);
			}
			statoImportazioneDepositiComponent.aggiornaStatoDeposito(deposito, depositoImportato);
		}

		if (!contabilizzazioneException.isEmpty()) {
			LifecycleApplicationEvent event = new OpenEditorEvent(contabilizzazioneException);
			Application.instance().getApplicationContext().publishEvent(event);
		}

		// aggiorno i dati della tabella
		prepareData();
		loadData();
	}

	@Override
	public Collection<ParametriImportazioneFatture> loadTableData() {
		return createParametriImportazioneFatture();
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

	/**
	 * Prepara i dati per l'importazione.
	 */
	private void prepareData() {
		mapLetture = this.auVendBD.caricaLettureFlussoFatture();

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
	public void processTableData(Collection<ParametriImportazioneFatture> results) {
		super.processTableData(results);
		this.importaFattureCommand.setEnabled(false);
	}

	@Override
	public Collection<ParametriImportazioneFatture> refreshTableData() {
		return loadTableData();
	}

	/**
	 * Seleziona in automatico tutti i dati validi dei parametri di riferimento.
	 * 
	 * @param listParametri
	 *            parametri di riferimento
	 * @return parametri selezionati
	 */
	private List<ParametriImportazioneFatture> selezionaDepositiValidi(List<ParametriImportazioneFatture> listParametri) {

		List<ParametriImportazioneFatture> listParametriSelezionati = new ArrayList<ParametriImportazioneFatture>();

		for (ParametriImportazioneFatture parametriImportazioneFatture : listParametri) {

			if (parametriImportazioneFatture.getStatisticaImportazione() != null
					&& parametriImportazioneFatture.getStatisticaImportazione().getArticoliMancanti() == null
					&& parametriImportazioneFatture.getStatisticaImportazione().getClientiDaVerificare() == null) {
				parametriImportazioneFatture.setSelezionato(true);
			} else {
				parametriImportazioneFatture.setSelezionato(false);
			}

			listParametriSelezionati.add(parametriImportazioneFatture);
		}

		return listParametriSelezionati;
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
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
	 * @param magazzinoContabilizzazioneBD
	 *            The magazzinoContabilizzazioneBD to set.
	 */
	public void setMagazzinoContabilizzazioneBD(IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD) {
		this.magazzinoContabilizzazioneBD = magazzinoContabilizzazioneBD;
	}

	/**
	 * Aggiorna le anomalie dei depositi.
	 */
	private void updateAnomalieDepositi() {

		Set<Articolo> setArticoli = new HashSet<Articolo>();
		Set<Cliente> setClienti = new HashSet<Cliente>();

		for (ParametriImportazioneFatture parametriImportazione : getTable().getRows()) {

			if (parametriImportazione.getStatisticaImportazione() != null
					&& parametriImportazione.getStatisticaImportazione().getArticoliMancanti() != null) {
				setArticoli.addAll(parametriImportazione.getStatisticaImportazione().getArticoliMancanti());
			}

			if (parametriImportazione.getStatisticaImportazione() != null
					&& parametriImportazione.getStatisticaImportazione().getClientiDaVerificare() != null) {
				setClienti.addAll(parametriImportazione.getStatisticaImportazione().getClientiDaVerificare());
			}
		}

		articoliMancantiComponent.aggiungiArticoliMancanti(setArticoli);
		clientiDaVerificareComponent.aggiungiClientiDaVerificare(setClienti);
	}
}
