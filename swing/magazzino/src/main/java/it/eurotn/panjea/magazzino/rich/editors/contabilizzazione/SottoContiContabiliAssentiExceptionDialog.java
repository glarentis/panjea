package it.eurotn.panjea.magazzino.rich.editors.contabilizzazione;

import it.eurotn.panjea.magazzino.service.exception.SottoContiContabiliAssentiException;
import it.eurotn.panjea.magazzino.service.exception.SottoContoContabileAssenteException;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

public class SottoContiContabiliAssentiExceptionDialog extends ApplicationDialog {

	public static final String PAGE_ID = "sottoContiContabiliExceptionDialog";

	private final SottoContiContabiliAssentiException exception;

	private JPanel rootPanel;

	/**
	 * Costruttore.
	 * 
	 * @param exception
	 *            eccezione da gestire
	 */
	public SottoContiContabiliAssentiExceptionDialog(final SottoContiContabiliAssentiException exception) {
		super();
		this.exception = exception;
		this.setPreferredSize(new Dimension(800, 400));
	}

	@Override
	protected JComponent createDialogContentPane() {

		return getRootPanel();
	}

	/**
	 * Crea la tabella che visualizza le combinazioni non trovate.
	 * 
	 * @return componenti creati
	 */
	private JideTableWidget<SottoContoContabileAssenteException> createTable() {

		JideTableWidget<SottoContoContabileAssenteException> tableWidget = new JideTableWidget<SottoContoContabileAssenteException>(
				PAGE_ID, new String[] { "articolo", "deposito", "sedeEntita", "categoriaContabileArticolo",
						"categoriaContabileDeposito", "categoriaContabileSedeMagazzino" },
				SottoContoContabileAssenteException.class);

		return tableWidget;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return (new AbstractCommand[] { getFinishCommand() });
	}

	/**
	 * @return the rootPanel
	 */
	public JPanel getRootPanel() {
		rootPanel = getComponentFactory().createPanel(new BorderLayout());

		JideTableWidget<SottoContoContabileAssenteException> table = createTable();
		rootPanel.add(table.getComponent(), BorderLayout.CENTER);

		table.setRows(this.exception.getAllSottoContoContabileAssenteExceptions());

		return rootPanel;
	}

	@Override
	protected String getTitle() {
		return RcpSupport.getMessage(PAGE_ID + ".title");
	}

	@Override
	protected boolean onFinish() {
		return true;
	}

}
