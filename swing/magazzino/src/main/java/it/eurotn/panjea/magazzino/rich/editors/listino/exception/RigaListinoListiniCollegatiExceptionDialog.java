/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.listino.exception;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.service.exception.RigaListinoListiniCollegatiException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.AlertMessageAreaPane;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

/**
 * @author fattazzo
 * 
 */
public class RigaListinoListiniCollegatiExceptionDialog extends ConfirmationDialog {

	private class ListinoListRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -1747018675393324712L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			// passo false al posto di isSelected e cellHasFocus perchè non vloglio che si possa selezionare qualcosa
			// nella lista
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, false, false);

			label.setIcon(RcpSupport.getIcon(Listino.class.getName()));
			label.setText(ObjectConverterManager.toString(value));

			return label;
		}

	}

	private RigaListinoListiniCollegatiException exception;
	private boolean aggiornaListini;

	/**
	 * Costruttore.
	 * 
	 * @param exception
	 *            eccezione da gestire
	 */
	public RigaListinoListiniCollegatiExceptionDialog(final RigaListinoListiniCollegatiException exception) {
		super("ATTENZIONE", "Listini collegati");
		this.exception = exception;
		setPreferredSize(new Dimension(500, 200));
		aggiornaListini = true;
	}

	@Override
	protected JComponent createDialogContentPane() {
		StringBuilder sb = new StringBuilder();
		sb.append("I seguenti listini sono collegati al listino <b>");
		sb.append(ObjectConverterManager.toString(exception.getRigaListino().getVersioneListino().getListino()));
		sb.append("</b> della riga <br>che si stà cercando di salvare.<br><br>Aggiornare automaticamente il loro prezzo? ");

		DefaultMessage message = new DefaultMessage(sb.toString(), Severity.WARNING);
		AlertMessageAreaPane messageAreaPane = new AlertMessageAreaPane();
		messageAreaPane.setMessage(message);

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 10));
		rootPanel.add(messageAreaPane.getControl(), BorderLayout.NORTH);

		JList<Listino> listListini = new JList<Listino>(exception.getListiniCollegati().toArray(
				new Listino[exception.getListiniCollegati().size()]));
		listListini.setCellRenderer(new ListinoListRenderer());
		rootPanel.add(new JScrollPane(listListini), BorderLayout.CENTER);
		return rootPanel;
	}

	/**
	 * @return Returns the aggiornaListini.
	 */
	public boolean isAggiornaListini() {
		return aggiornaListini;
	}

	@Override
	protected void onCancel() {
		aggiornaListini = false;
		super.onCancel();
	}

	@Override
	protected void onConfirm() {
		aggiornaListini = true;
	}

}
