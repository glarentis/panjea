package it.eurotn.panjea.lotti.exceptionhandler;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.swing.JComponent;

import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.utils.StringUtils;

public class RimanenzaLottiNonValidaExceptionHandler extends MessagesDialogExceptionHandler {

	private class ExceptionDialog extends MessageDialog {

		private RimanenzaLottiNonValidaException exception;

		/**
		 * Costruttore.
		 * 
		 * @param exception
		 *            eccezione da gestire
		 */
		public ExceptionDialog(final RimanenzaLottiNonValidaException exception) {
			super(RcpSupport.getMessage(TITLE), "message");
			this.exception = exception;
		}

		@Override
		protected JComponent createDialogContentPane() {

			Object[] rows = new String[(exception.getLotti().isEmpty()) ? exception.getRigheLotto().size() : exception
					.getLotti().size()];
			if (rows.length == 0) {
				rows = new String[1];
			}
			Arrays.fill(rows, "default");

			String layoutRows = StringUtils.stringList("", "", ",", rows);

			FormLayout layout = new FormLayout("left:200dlu,left:80dlu,right:80dlu,left:default:grow",
					"default,bottom:20dlu,default," + layoutRows);
			PanelBuilder builder = new PanelBuilder(layout); // , new
																// FormDebugPanel());
			CellConstraints cc = new CellConstraints();
			builder.addLabel(RcpSupport.getMessage(MESSAGE), cc.xyw(1, 1, 4));
			builder.nextRow();
			builder.addLabel(RcpSupport.getMessage("articolo"), cc.xy(1, 2));
			builder.addLabel(RcpSupport.getMessage("lotto"), cc.xy(2, 2));
			builder.addLabel(RcpSupport.getMessage("rimanenzaCalcolata"), cc.xy(3, 2));
			builder.nextRow();
			builder.addSeparator("", 3);
			builder.nextRow();

			String decimalString = "0000000000000";

			int idxRiga = 4;
			int decimeliQtaArticolo = 0;
			if (exception.getRigaArticolo() != null) {
				decimeliQtaArticolo = exception.getRigaArticolo().getArticolo().getNumeroDecimaliQta();
			}
			String qtaPattern = "#,##0." + decimalString.substring(0, decimeliQtaArticolo);
			DecimalFormat format = new DecimalFormat(qtaPattern);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
			if (!exception.getLotti().isEmpty()) {
				for (Lotto lotto : exception.getLotti()) {
					builder.addLabel(ObjectConverterManager.toString(lotto.getArticolo()), cc.xy(1, idxRiga));
					builder.addLabel(lotto.getCodice() + " scad. " + dateFormat.format(lotto.getDataScadenza()),
							cc.xy(2, idxRiga));
					builder.addLabel(format.format(lotto.getRimanenza()), cc.xy(3, idxRiga));
					idxRiga++;
				}
			} else {
				if (exception.getRigheLotto().isEmpty()) {
					builder.addLabel(ObjectConverterManager.toString(exception.getRigaArticolo().getArticolo()),
							cc.xy(1, idxRiga));
					builder.addLabel("Nessun carico", cc.xy(2, idxRiga));
					builder.addLabel(format.format(exception.getRigaArticolo().getQta()), cc.xy(3, idxRiga));
				} else {
					for (RigaLotto rigaLotto : exception.getRigheLotto()) {
						builder.addLabel(ObjectConverterManager.toString(rigaLotto.getLotto().getArticolo()),
								cc.xy(1, idxRiga));
						builder.addLabel(
								rigaLotto.getLotto().getCodice() + " scad. "
										+ dateFormat.format(rigaLotto.getLotto().getDataScadenza()), cc.xy(2, idxRiga));
						builder.addLabel(format.format(rigaLotto.getQuantita()), cc.xy(3, idxRiga));
						idxRiga++;
					}
				}

			}

			return builder.getPanel();
		}
	}

	private static final String TITLE = RimanenzaLottiNonValidaException.class.getName() + ".caption";
	private static final String MESSAGE = RimanenzaLottiNonValidaException.class.getName() + ".description";

	@Override
	public void notifyUserAboutException(Thread thread, Throwable throwable) {
		ExceptionDialog exceptionDialog = new ExceptionDialog((RimanenzaLottiNonValidaException) throwable);
		exceptionDialog.showDialog();

	}

}
