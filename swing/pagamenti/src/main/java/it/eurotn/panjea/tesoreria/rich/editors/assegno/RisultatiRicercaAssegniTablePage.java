package it.eurotn.panjea.tesoreria.rich.editors.assegno;

import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.RisultatiRicercaAreaTesoreriaPage;
import it.eurotn.panjea.tesoreria.util.AssegnoDTO;
import it.eurotn.panjea.tesoreria.util.AssegnoDTO.StatoCarrello;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAssegni;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSplitPane;

/**
 * @author leonardo
 */
public class RisultatiRicercaAssegniTablePage extends AbstractTablePageEditor<AssegnoDTO> implements InitializingBean,
		Observer {

	private class AggiungiAssegniSelezionatiCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "aggiungiAssegniSelezionatiCommand";

		/**
		 * Costruttore.
		 */
		public AggiungiAssegniSelezionatiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			List<AssegnoDTO> assegniDaAggiungere = new ArrayList<AssegnoDTO>();
			if (getTable().getTable().getCellEditor() != null) {
				getTable().getTable().getCellEditor().stopCellEditing();
			}

			for (AssegnoDTO assegno : getTableModel().getObjects()) {
				if (assegno.getStatoCarrello() == StatoCarrello.DA_AGGIUNGERE) {
					assegniDaAggiungere.add(assegno);
				}
			}
			if (!assegniDaAggiungere.isEmpty()) {
				carrelloAssegniTablePage.addAssegni(assegniDaAggiungere);
				updateStatoCarrelloAssegni();
			}
		}

	}

	private class GeneraAccreditoCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand arg0) {
			loadData();
		}

		@Override
		public boolean preExecution(ActionCommand arg0) {
			return true;
		}

	}

	private class RimuoviAssegnoCommandInterceptor implements ActionCommandInterceptor {
		@Override
		public void postExecution(ActionCommand actioncommand) {
			updateStatoCarrelloAssegni();
		}

		@Override
		public boolean preExecution(ActionCommand actioncommand) {
			return true;
		}
	}

	public static final String PAGE_ID = "risultatiRicercaAssegniTablePage";

	public static final String AREA_TESORERIA_SELEZIONATA = "areaTesoreriaSelezionata";

	private ITesoreriaBD tesoreriaBD = null;

	private ParametriRicercaAssegni parametriRicercaAssegni = null;
	private CarrelloAssegniTablePage carrelloAssegniTablePage = null;
	private JideSplitPane splitPane = null;
	private AggiungiAssegniSelezionatiCommand aggiungiAssegniSelezionatiCommand = null;
	private RimuoviAssegnoCommandInterceptor rimuoviAssegnoCommandInterceptor = null;

	private GeneraAccreditoCommandInterceptor generaAccreditoCommandInterceptor = null;

	private SelectAssegniCommand selectAssegniCommand;
	private SelectAssegniCommand unselectAssegniCommand;

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaAssegniTablePage() {
		super(PAGE_ID, new RisultatiRicercaAssegniTableModel(PAGE_ID));
		// blocco la possibilità di mettere la colonna STATO_CARRELLO gruppata
		((AggregateTable) getTable().getTable()).getAggregateTableModel().getField(0).setAllowedAsRowField(false);
		setShowTitlePane(false);
		new SelectRowListener(getTable().getTable());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(carrelloAssegniTablePage, "carrelloAssegniTablePage cannot be null!");

		carrelloAssegniTablePage.getRimuoviAssegniSelezionatiCommand().addCommandInterceptor(
				getRimuoviAssegnoCommandInterceptor());
		carrelloAssegniTablePage.getSvuotaCarrelloCommand()
				.addCommandInterceptor(getRimuoviAssegnoCommandInterceptor());
		carrelloAssegniTablePage.getGeneraAccreditoAssegnoCommand().addCommandInterceptor(
				getGeneraAccreditoCommandInterceptor());
		carrelloAssegniTablePage.getAssegnaRapportoBancarioCommand().addCommandInterceptor(
				getGeneraAccreditoCommandInterceptor());

	}

	@Override
	public void dispose() {
		carrelloAssegniTablePage.getRimuoviAssegniSelezionatiCommand().removeCommandInterceptor(
				getRimuoviAssegnoCommandInterceptor());
		carrelloAssegniTablePage.getSvuotaCarrelloCommand().removeCommandInterceptor(
				getRimuoviAssegnoCommandInterceptor());
		carrelloAssegniTablePage.getGeneraAccreditoAssegnoCommand().removeCommandInterceptor(
				getGeneraAccreditoCommandInterceptor());
		carrelloAssegniTablePage.getAssegnaRapportoBancarioCommand().removeCommandInterceptor(
				getGeneraAccreditoCommandInterceptor());
		super.dispose();
	}

	/**
	 * @return AggiungiAssegniSelezionatiCommand
	 */
	private AbstractCommand getAggiungiAssegniSelezionatiCommand() {
		if (aggiungiAssegniSelezionatiCommand == null) {
			aggiungiAssegniSelezionatiCommand = new AggiungiAssegniSelezionatiCommand();
		}
		return aggiungiAssegniSelezionatiCommand;
	}

	/**
	 * Carica la lista degli assegni a seconda dei parametriRicercaAssegni passati.
	 * 
	 * @return List<AssegnoDTO>
	 */
	private List<AssegnoDTO> getAssegni() {
		List<AssegnoDTO> assegni = new ArrayList<AssegnoDTO>();
		if (this.parametriRicercaAssegni != null && this.parametriRicercaAssegni.isEffettuaRicerca()) {
			assegni = tesoreriaBD.caricaAssegni(parametriRicercaAssegni);
		}
		return assegni;
	}

	/**
	 * @return CarrelloAssegniTablePage
	 */
	public CarrelloAssegniTablePage getCarrelloAssegniTablePage() {
		return carrelloAssegniTablePage;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getSelectAssegniCommand(), getUnselectAssegniCommand(),
				getAggiungiAssegniSelezionatiCommand() };
	}

	@Override
	public JComponent getControl() {
		if (splitPane == null) {
			splitPane = new JideSplitPane(JSplitPane.VERTICAL_SPLIT);
			splitPane.setProportionalLayout(true);
			splitPane.add(super.getControl(), JideBoxLayout.FLEXIBLE);
			splitPane.add(carrelloAssegniTablePage.getControl(), JideBoxLayout.FLEXIBLE);
		}
		return splitPane;
	}

	/**
	 * @return GeneraAccreditoCommandInterceptor
	 */
	private GeneraAccreditoCommandInterceptor getGeneraAccreditoCommandInterceptor() {
		if (generaAccreditoCommandInterceptor == null) {
			generaAccreditoCommandInterceptor = new GeneraAccreditoCommandInterceptor();
		}
		return generaAccreditoCommandInterceptor;
	}

	/**
	 * @return RimuoviAssegnoCommandInterceptor
	 */
	private ActionCommandInterceptor getRimuoviAssegnoCommandInterceptor() {
		if (rimuoviAssegnoCommandInterceptor == null) {
			rimuoviAssegnoCommandInterceptor = new RimuoviAssegnoCommandInterceptor();
		}
		return rimuoviAssegnoCommandInterceptor;
	}

	/**
	 * @return the selectAssegniCommand
	 */
	public SelectAssegniCommand getSelectAssegniCommand() {
		if (selectAssegniCommand == null) {
			selectAssegniCommand = new SelectAssegniCommand(SelectAssegniCommand.SELECT_COMMAND_ID, true, this);
		}

		return selectAssegniCommand;
	}

	/**
	 * @return tableModel della pagina
	 */
	RisultatiRicercaAssegniTableModel getTableModel() {
		RisultatiRicercaAssegniTableModel tableModel = (RisultatiRicercaAssegniTableModel) TableModelWrapperUtils
				.getActualTableModel(getTable().getTable().getModel());
		return tableModel;
	}

	/**
	 * @return the tesoreriaBD
	 */
	public ITesoreriaBD getTesoreriaBD() {
		return tesoreriaBD;
	}

	/**
	 * @return the unselectAssegniCommand
	 */
	public SelectAssegniCommand getUnselectAssegniCommand() {
		if (unselectAssegniCommand == null) {
			unselectAssegniCommand = new SelectAssegniCommand(SelectAssegniCommand.UNSELECT_COMMAND_ID, false, this);
		}

		return unselectAssegniCommand;
	}

	@Override
	public Collection<AssegnoDTO> loadTableData() {
		return getAssegni();
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void processTableData(Collection<AssegnoDTO> results) {
		super.processTableData(results);
		updateStatoCarrelloAssegni();
	}

	@Override
	public Collection<AssegnoDTO> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param carrelloAssegniTablePage
	 *            the carrelloAssegniTablePage to set
	 */
	public void setCarrelloAssegniTablePage(CarrelloAssegniTablePage carrelloAssegniTablePage) {
		this.carrelloAssegniTablePage = carrelloAssegniTablePage;
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof ParametriRicercaAssegni) {
			this.parametriRicercaAssegni = (ParametriRicercaAssegni) object;
		} else {
			this.parametriRicercaAssegni = new ParametriRicercaAssegni();
		}

	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

	@Override
	public void update(Observable observable, Object obj) {
		AssegnoDTO assegno = (AssegnoDTO) obj;
		AreaAssegno areaAssegno = null;
		if (assegno != null) {
			areaAssegno = assegno.getAreaAssegno();
		}
		RisultatiRicercaAssegniTablePage.this.firePropertyChange(
				RisultatiRicercaAreaTesoreriaPage.AREA_TESORERIA_SELEZIONATA, -1, areaAssegno);
	}

	/**
	 * Aggiorna lo stato carrello dei risultati confrontandolo con la lista di dati presenti nel carrello.
	 */
	public void updateStatoCarrelloAssegni() {
		List<AssegnoDTO> assegniCarrello = carrelloAssegniTablePage.getTable().getRows();
		List<AssegnoDTO> assegni = getTable().getRows();
		for (AssegnoDTO assegnoDTO : assegni) {
			if (!assegnoDTO.getStatoCarrello().equals(StatoCarrello.NON_SELEZIONABILE)) {
				assegnoDTO.setStatoCarrello(StatoCarrello.SELEZIONABILE);
			}
		}
		List<AssegnoDTO> assegniAggiuntiNelCarrello = new ArrayList<AssegnoDTO>(assegniCarrello);
		assegniAggiuntiNelCarrello.retainAll(assegni);

		for (AssegnoDTO assegnoDTO : assegniAggiuntiNelCarrello) {
			int index = assegni.indexOf(assegnoDTO);
			if (index != -1) {
				assegni.get(index).setStatoCarrello(StatoCarrello.AGGIUNTO_CARRELLO);
			}
		}
		((AggregateTable) getTable().getTable()).getAggregateTableModel().fireTableDataChanged();
	}

}
