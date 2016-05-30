package it.eurotn.panjea.rate.rich.editors.rate.riemissione;

import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.rich.bd.IRateBD;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.springframework.richclient.dialog.AbstractDialogPage;

public class RiemettiRataPage extends AbstractDialogPage {

	private class RateDaRiemettereObserver implements Observer {

		@Override
		public void update(Observable arg0, Object arg1) {
			RataRiemessa insoluto = (RataRiemessa) arg1;
			if (!insoluto.isInizialized()) {
				List<Rata> rateRiemesse = rateBD.caricaRateCollegate(insoluto);
				insoluto.setRateRiemesse(rateRiemesse);
			}
			rateDaCreareTableModel.setRataDaRiemettere(insoluto);
		}

	}

	public static final String PAGE_ID = "riemettiRataPage";

	private JideTableWidget<Rata> rateDaCreareTable;
	private JideTableWidget<RataRiemessa> rateDaRiemettereTable;

	private IRateBD rateBD;

	private List<RataRiemessa> insoluti;

	private JPanel rootPanel;

	private RateDaCreareTableModel rateDaCreareTableModel;

	/**
	 * Costruttore.
	 * 
	 * @param rateBD
	 *            bd delle rate
	 * @param insoluti
	 *            insoluti da riemettere
	 */
	public RiemettiRataPage(final IRateBD rateBD, final List<RataRiemessa> insoluti) {
		super(PAGE_ID);
		this.insoluti = insoluti;
		this.rateBD = rateBD;
	}

	@Override
	protected JComponent createControl() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerSize(1);
		splitPane.setBorder(null);
		rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.add(splitPane);

		JComponent rateDaCreareTableComponent = createRateDaCreareTableComponent();
		JComponent rateDaRiemettereTableComponent = createRateDaRiemettereTableComponent();

		rateDaCreareTableComponent.setPreferredSize(new Dimension(640, 250));
		rateDaRiemettereTableComponent.setPreferredSize(new Dimension(640, 250));

		splitPane.setTopComponent(rateDaRiemettereTableComponent);
		splitPane.setBottomComponent(rateDaCreareTableComponent);

		rateDaRiemettereTable.getTable().getSelectionModel().setSelectionInterval(0, 0);
		return rootPanel;
	}

	/**
	 * Crea i controlli per l'inserimento,modifica e cancellazione delle rate da creare.
	 * 
	 * @return controlli creati
	 */
	private JComponent createRateDaCreareTableComponent() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());

		rateDaCreareTableModel = new RateDaCreareTableModel();
		rateDaCreareTable = new JideTableWidget<Rata>("rateDaCreareTable", rateDaCreareTableModel);
		rateDaCreareTable.getTable().getTableHeader().setReorderingAllowed(false);

		panel.add(rateDaCreareTable.getComponent(), BorderLayout.CENTER);

		panel.setBorder(BorderFactory.createTitledBorder("Rate da generare"));

		return panel;
	}

	/**
	 * Crea i controlli per la lista di rate da riemettere, cambiando rata da riemettere verrà caricata la lista di rate
	 * da creare.
	 * 
	 * @return i controlli creati
	 */
	private JComponent createRateDaRiemettereTableComponent() {
		rateDaRiemettereTable = new JideTableWidget<RataRiemessa>("rateDaRiemettereTableModel",
				new RateDaRiemettereTableModel());
		RateDaRiemettereObserver observer = new RateDaRiemettereObserver();
		rateDaRiemettereTable.addSelectionObserver(observer);
		rateDaRiemettereTable.setRows(insoluti);
		return rateDaRiemettereTable.getComponent();
	}

	/**
	 * @return insoluti della pagina.
	 */
	public List<RataRiemessa> getInsoluti() {
		return insoluti;
	}

	/**
	 * Controlla se esiste nella tabella almeno 1 rata da creare e se i dati ( importo e data scadenza sono validi ).
	 * 
	 * @return <code>true</code> se le rate da creare sono valide
	 */
	public boolean isRateValid() {
		for (RataRiemessa rataRiemessa : insoluti) {
			for (Rata rataDaCreare : rataRiemessa.getRateDaCreare()) {
				if (rataDaCreare.getImporto().getImportoInValuta() == null
						|| rataDaCreare.getImporto().getImportoInValuta().equals(BigDecimal.ZERO)
						|| rataDaCreare.getDataScadenza() == null) {
					return Boolean.FALSE;
				}
			}
		}
		return true;
	}
}
