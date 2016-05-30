/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.listino.exception;

import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.service.exception.RigaListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.service.exception.RigheListinoListiniCollegatiException;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.AlertMessageAreaPane;
import org.springframework.richclient.dialog.ConfirmationDialog;

/**
 * @author fattazzo
 * 
 */
public class RigheListinoListiniCollegatiExceptionDialog extends ConfirmationDialog {

	private RigheListinoListiniCollegatiException exception;

	private JideTableWidget<RigaListinoCollegataPM> tableWidget;

	private List<RigaListino> righeListino;

	private HashMap<Boolean, List<RigaListino>> righeDaAggiornare;

	/**
	 * Costruttore.
	 * 
	 * @param righeListino
	 *            righe listino da salvare
	 * @param exception
	 *            eccezione da gestire
	 */
	public RigheListinoListiniCollegatiExceptionDialog(final List<RigaListino> righeListino,
			final RigheListinoListiniCollegatiException exception) {
		super("ATTENZIONE", "Listini collegati");
		this.exception = exception;
		this.righeListino = righeListino;
		setPreferredSize(new Dimension(500, 200));
	}

	@Override
	protected JComponent createDialogContentPane() {
		StringBuilder sb = new StringBuilder();
		sb.append("Per i seguenti listini sono configurati dei listini collegati.<br>Aggiornare automaticamente il loro prezzo? ");

		DefaultMessage message = new DefaultMessage(sb.toString(), Severity.WARNING);
		AlertMessageAreaPane messageAreaPane = new AlertMessageAreaPane();
		messageAreaPane.setMessage(message);

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 10));
		rootPanel.add(messageAreaPane.getControl(), BorderLayout.NORTH);

		this.tableWidget = new JideTableWidget<>("righeListinoListiniCollegatiTable",
				new RigheListinoListiniCollegatiTableModel());
		rootPanel.add(tableWidget.getComponent(), BorderLayout.CENTER);

		List<RigaListinoCollegataPM> righePM = new ArrayList<RigaListinoCollegataPM>();
		for (RigaListinoListiniCollegatiException rigaExc : exception.getExceptions()) {
			righePM.add(new RigaListinoCollegataPM(rigaExc));
		}
		tableWidget.setRows(righePM);

		return rootPanel;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return new AbstractCommand[] { getFinishCommand() };
	}

	@Override
	protected String getFinishCommandId() {
		return "okCommand";
	}

	/**
	 * @return Returns the righeDaAggiornare.
	 */
	public HashMap<Boolean, List<RigaListino>> getRigheDaAggiornare() {
		return righeDaAggiornare;
	}

	@Override
	protected void onCancel() {
	}

	@Override
	protected void onConfirm() {

		// se non ho editato alcun valore della tabella il cellEditor è null!
		if ((tableWidget.getTable()).getCellEditor() != null) {
			(tableWidget.getTable()).getCellEditor().stopCellEditing();
		}

		righeDaAggiornare = new HashMap<Boolean, List<RigaListino>>();

		// ricavo la mappa con le righe di cui aggiornare i prezzi e quelle che non li aggiornano in base alla selezione
		// in tabella
		List<RigaListinoCollegataPM> righePM = tableWidget.getRows();
		for (RigaListinoCollegataPM rigaListinoCollegataPM : righePM) {
			List<RigaListino> righe = righeDaAggiornare.get(rigaListinoCollegataPM.getAggiornaListiniCollegati());
			if (righe == null) {
				righe = new ArrayList<RigaListino>();
			}
			righe.add(rigaListinoCollegataPM.getRigaListino());
			righeDaAggiornare.put(rigaListinoCollegataPM.getAggiornaListiniCollegati(), righe);
		}

		// delle righe iniziali da salvare tolgo quelle che aggiornano il prezzo
		if (righeDaAggiornare.get(true) != null) {
			righeListino.removeAll(righeDaAggiornare.get(true));
		}
		righeDaAggiornare.put(false, righeListino);
	}

}
