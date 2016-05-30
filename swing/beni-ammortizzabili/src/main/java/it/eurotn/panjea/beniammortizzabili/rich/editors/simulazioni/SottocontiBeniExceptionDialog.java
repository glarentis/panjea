/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.simulazioni;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;
import it.eurotn.panjea.beniammortizzabili2.domain.SottocontiBeni;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.richclient.dialog.MessageDialog;

import com.jidesoft.swing.JideLabel;

/**
 * @author fattazzo
 *
 */
public class SottocontiBeniExceptionDialog extends MessageDialog {

	private JideTableWidget<EntitaSottoContiPM> tableWidget;

	private SottocontiBeniNonValidiException exception;

	/**
	 * Costruttore.
	 */
	public SottocontiBeniExceptionDialog() {
		super("Sottoconti assenti", "_");
	}

	@Override
	protected JComponent createDialogContentPane() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 10));

		rootPanel
				.add(new JideLabel(
						"<html>Non tutti i sottoconti necessari alla generazione dei movimenti contabili sono presenti. Inserire quelli mancanti prima di poter proseguire.<br><br>I sottoconti ereditati sono evidenziati in verde.</html>"),
						BorderLayout.NORTH);

		tableWidget = new JideTableWidget<EntitaSottoContiPM>("sottocontiBeniTableWidget",
				new SottocontiBeniExceptionTableModel());
		Map<EntityBase, SottocontiBeni> sottocontiBeniNonValidi = exception.getSottocontiBeniNonValidi();

		List<EntitaSottoContiPM> entitaSottoContiPM = new ArrayList<EntitaSottoContiPM>();
		for (Entry<EntityBase, SottocontiBeni> entry : sottocontiBeniNonValidi.entrySet()) {
			entitaSottoContiPM.add(new EntitaSottoContiPM(entry.getKey(), entry.getValue()));
		}

		tableWidget.setRows(entitaSottoContiPM);

		rootPanel.add(new JScrollPane(tableWidget.getTable()), BorderLayout.CENTER);

		return rootPanel;
	}

	/**
	 * @param sottocontiBeniNonValidiException
	 *            the sottocontiBeniNonValidiException to set
	 */
	public void setSottocontiBeniException(SottocontiBeniNonValidiException sottocontiBeniNonValidiException) {
		this.exception = sottocontiBeniNonValidiException;
	}
}
