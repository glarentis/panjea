/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.anticipifattura;

import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author leonardo
 */
public class AnticipiFatturaTablePage extends AbstractTablePageEditor<SituazioneRata> {

	private class ApriRicercaRateCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "apriRicercaRateCommand";

		/**
		 * Costruttore.
		 */
		public ApriRicercaRateCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriRicercaRate parametriRicercaRate = (ParametriRicercaRate) parametriRicercaAnticipiFatturaForm
					.getFormObject();
			parametriRicercaRate.setEffettuaRicerca(false);
			parametriRicercaRate.setRate(getTable().getRows());

			LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaRate);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	private class CercaAnticipiFatturaCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "cercaAnticipiFatturaCommand";

		/**
		 * Costruttore.
		 */
		public CercaAnticipiFatturaCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			refreshData();
		}
	}

	public static final String PAGE_ID = "anticipiFatturaTablePage";

	private ITesoreriaBD tesoreriaBD = null;
	private ParametriRicercaAnticipiFatturaForm parametriRicercaAnticipiFatturaForm = null;
	private CercaAnticipiFatturaCommand cercaAnticipiFatturaCommand = null;
	private ApriRicercaRateCommand apriRicercaRateCommand = null;

	/**
	 * AnticipiFatturaTablePage.
	 */
	protected AnticipiFatturaTablePage() {
		super(PAGE_ID, new AnticipiFatturaTableModel());
		getTable().setTableType(TableType.AGGREGATE);
	}

	/**
	 * @return ApriRicercaRateCommand
	 */
	public ApriRicercaRateCommand getApriRicercaRateCommand() {
		if (apriRicercaRateCommand == null) {
			apriRicercaRateCommand = new ApriRicercaRateCommand();
		}
		return apriRicercaRateCommand;
	}

	/**
	 * @return the cercaSollecitiCommand
	 */
	public CercaAnticipiFatturaCommand getCercaSollecitiCommand() {
		if (cercaAnticipiFatturaCommand == null) {
			cercaAnticipiFatturaCommand = new CercaAnticipiFatturaCommand();
		}
		return cercaAnticipiFatturaCommand;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getApriRicercaRateCommand(), getRefreshCommand() };
	}

	@Override
	public JComponent getHeaderControl() {
		JPanel filterPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT));
		filterPanel.setBorder(BorderFactory.createTitledBorder("Parametri ricerca"));

		ParametriRicercaRate parametriRicercaRate = new ParametriRicercaRate();
		Set<StatoRata> statiRata = new HashSet<StatoRata>();
		statiRata.add(StatoRata.ANTICIPO_FATTURA);
		parametriRicercaRate.setStatiRata(statiRata);

		parametriRicercaAnticipiFatturaForm = new ParametriRicercaAnticipiFatturaForm(parametriRicercaRate);

		filterPanel.add(parametriRicercaAnticipiFatturaForm.getControl(), BorderLayout.WEST);
		filterPanel.add(getCercaSollecitiCommand().createButton(), BorderLayout.EAST);

		return filterPanel;
	}

	@Override
	public Collection<SituazioneRata> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	protected void onRefresh() {
		refreshData();
	}

	@Override
	public Collection<SituazioneRata> refreshTableData() {
		List<SituazioneRata> rateAnticipiFattura = new ArrayList<SituazioneRata>();

		rateAnticipiFattura = tesoreriaBD.ricercaRate((ParametriRicercaRate) parametriRicercaAnticipiFatturaForm
				.getFormObject());
		return rateAnticipiFattura;
	}

	@Override
	public void setFormObject(Object object) {

	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

}
