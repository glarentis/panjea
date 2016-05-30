package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;

import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.DefaultMessageAreaPane;

/**
 * Visualizzato se il totale documento o le righe iva non corrisponde quando confermo le righe magazzino<br/>
 * . L'utente può decidere se annullare la conferma o se far passare lo stato del documento a FORZATO<br/>
 * 
 * 
 * @author giangi
 * 
 */
public class TotaleDocumentoNonCoerenteDialog extends ConfirmationDialog {
	private static final String CONFIRMATION_DIALOG_ICON = "confirmationDialog.icon";

	private static final String NUMBER_FORMAT = "#,##0.00";

	/**
	 * Eccezione che il dialogo deve gestire.
	 */
	private final TotaleDocumentoNonCoerenteException totaleDocumentoNonCoerenteException;
	private String confirmationMessage = "";
	private DefaultMessageAreaPane messageAreaPane;
	private boolean cambiaStato = false;

	/**
	 * Costruttore.
	 * 
	 * @param totaleDocumentoNonCoerenteException
	 *            eccezione che il dialogo deve gestire
	 */
	public TotaleDocumentoNonCoerenteDialog(
			final TotaleDocumentoNonCoerenteException totaleDocumentoNonCoerenteException) {
		super();
		this.totaleDocumentoNonCoerenteException = totaleDocumentoNonCoerenteException;
	}

	@Override
	protected JComponent createDialogContentPane() {
		StringBuffer stringBuffer = new StringBuffer("<HTML>" + this.confirmationMessage);
		if (totaleDocumentoNonCoerenteException.getRigheIva() == null) {
			stringBuffer.append(getMessage("totaleDocumentoNonCoerente"));
			stringBuffer.append(totaleDocumentoNonCoerenteException.getTotaleAreaMagazzino().getImportoInValuta());
		} else {
			Format format = new DecimalFormat(NUMBER_FORMAT);

			stringBuffer.append(getMessage("areaIvaNonCoerente"));
			stringBuffer.append("<HTML><TABLE><TR><TD>Codice</TD><TD>Importo</TD><TD>Imponibile</TD></TR>");
			for (RigaIva rigaIva : totaleDocumentoNonCoerenteException.getRigheIva()) {
				stringBuffer.append("<TR>");
				stringBuffer.append("<TD>");
				stringBuffer.append("<b>" + rigaIva.getCodiceIva().getCodice() + "</b>");
				stringBuffer.append("</TD>");
				stringBuffer.append("<TD>");
				stringBuffer.append("<b>" + format.format(rigaIva.getImponibile().getImportoInValuta()) + "</b>");
				stringBuffer.append("</TD>");
				stringBuffer.append("<TD>");
				stringBuffer.append("<b>" + format.format(rigaIva.getImposta().getImportoInValuta()) + "<b>");
				stringBuffer.append("</TD>");
				stringBuffer.append("</TR>");
			}
			stringBuffer.append("</TABLE>");
		}
		stringBuffer.append(getMessage("confermaNonCoerente"));
		stringBuffer.append("</HTML>");

		this.messageAreaPane = new DefaultMessageAreaPane(10);
		Icon icon = getIconSource().getIcon(CONFIRMATION_DIALOG_ICON);
		if (icon == null) {
			icon = UIManager.getIcon("OptionPane.questionIcon");
		}
		this.messageAreaPane.setDefaultIcon(icon);
		this.messageAreaPane.setMessage(new DefaultMessage(stringBuffer.toString()));
		return messageAreaPane.getControl();
	};

	@Override
	protected String getTitle() {
		return "ATTENZIONE";
	}

	/**
	 * 
	 * @return cambiaStato
	 */
	public boolean isCambiaStato() {
		return cambiaStato;
	}

	@Override
	protected void onCancel() {
		super.onCancel();
		cambiaStato = true;
	}

	@Override
	protected void onConfirm() {
		this.messageAreaPane = null;
	}

	@Override
	public void setConfirmationMessage(String message) {
		super.setConfirmationMessage(message);
		this.confirmationMessage = message;
	}
}
