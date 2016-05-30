package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;

import java.awt.Component;
import java.awt.Window;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.richclient.selection.dialog.ListSelectionDialog;

/**
 * Dialogo di selezione delle simulazioni.
 * 
 * @author Leonardo
 */
public class SimulazioneSelectionDialog extends ListSelectionDialog {

	private Simulazione simulazioneSelected = null;

	/**
	 * Costruttore di default.
	 * 
	 * @param title
	 *            titolo del dialogo
	 * @param parent
	 *            parent window
	 * @param list
	 *            Lista simulazioni
	 */
	public SimulazioneSelectionDialog(final String title, final Window parent, final List<?> list) {
		super(title, parent, list);
		setRenderer(new DefaultListCellRenderer() {
			/**
			 * Comment for <code>serialVersionUID</code>
			 */
			private static final long serialVersionUID = -6600465552132434943L;

			@Override
			public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
				JLabel label = (JLabel) super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				label.setText(
				// visualizzo se la simulazione � consolidata (C) o se � simulazione (S)
				getMessage("selezioneSimulazioneDialog.simulazione.consolidata." + ((Simulazione) arg1).isConsolidata())
						+ " - "
						+ format.format(((Simulazione) arg1).getData())
						+ " - "
						+ ((Simulazione) arg1).getDescrizione());
				return label;
			}
		});
	}

	/**
	 * @return simulazione selezionata
	 */
	public Simulazione getSimulazioneSelected() {
		return simulazioneSelected;
	}

	@Override
	protected void onSelect(Object selection) {
		simulazioneSelected = (Simulazione) selection;
	}
}
