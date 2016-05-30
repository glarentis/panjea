package it.eurotn.panjea.lotti.exceptionhandler;

import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.exception.RimanenzaLottoNonValida;
import it.eurotn.panjea.lotti.exception.RimanenzeLottiNonValideException;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;

public class RimanenzeLottiNonValideExceptionHandler extends MessagesDialogExceptionHandler {

	private class ExceptionDialog extends MessageDialog {

		private RimanenzeLottiNonValideException exception;

		/**
		 * Costruttore.
		 * 
		 * @param exception
		 *            eccezione da gestire
		 */
		public ExceptionDialog(final RimanenzeLottiNonValideException exception) {
			super(RcpSupport.getMessage(TITLE), "message");
			this.exception = exception;
		}

		@Override
		protected JComponent createDialogContentPane() {

			JideTableWidget<RimanenzaLottoNonValida> tableWidget = new JideTableWidget<RimanenzaLottoNonValida>(
					"rimanenzaLottoNonValidaTableWidget", new RimananzaLottoTableModel());
			tableWidget.setRows(exception.getRimanenzeLottoNonValide());

			JPanel panel = new JPanel(new BorderLayout(0, 20));
			panel.add(new JLabel(RcpSupport.getMessage(MESSAGE)), BorderLayout.NORTH);
			panel.add(tableWidget.getComponent(), BorderLayout.CENTER);
			panel.setPreferredSize(new Dimension(650, 300));
			return panel;
		}
	}

	private class RimananzaLottoTableModel extends DefaultBeanTableModel<RimanenzaLottoNonValida> {

		private ConverterContext numberQtaContext;

		{
			numberQtaContext = new NumberWithDecimalConverterContext();
			numberQtaContext.setUserObject(new Integer(6));
		}

		private static final long serialVersionUID = 8695940393052463032L;

		/**
		 * Costruttore.
		 */
		public RimananzaLottoTableModel() {
			super("RimananzaLottoTableModel", new String[] { "articolo", "depositoOrigine", "lotto",
					"rimanenzaCalcolata", "qtaRichiesta" }, RimanenzaLottoNonValida.class);
		}

		@Override
		public ConverterContext getConverterContextAt(int row, int column) {
			switch (column) {
			case 3:
			case 4:
				return numberQtaContext;
			default:
				return null;
			}
		}

	}

	private static final String TITLE = RimanenzaLottiNonValidaException.class.getName() + ".caption";
	private static final String MESSAGE = RimanenzaLottiNonValidaException.class.getName() + ".description";

	@Override
	public void notifyUserAboutException(Thread thread, Throwable throwable) {

		ExceptionDialog exceptionDialog = new ExceptionDialog((RimanenzeLottiNonValideException) throwable);
		exceptionDialog.showDialog();

	}

}
