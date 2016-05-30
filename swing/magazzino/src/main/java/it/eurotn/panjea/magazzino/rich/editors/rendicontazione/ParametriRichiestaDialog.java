package it.eurotn.panjea.magazzino.rich.editors.rendicontazione;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;

import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.ui.tabbedui.VerticalLayout;
import org.springframework.richclient.dialog.ApplicationDialog;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class ParametriRichiestaDialog extends ApplicationDialog {

	private TipoEsportazione tipoEsportazione;
	private JDateChooser dateChooser = null;
	private Date dataInserita = null;
	private Map<String, Object> result = null;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param tipoEsportazione
	 *            tipo esportazione.
	 */
	public ParametriRichiestaDialog(final TipoEsportazione tipoEsportazione) {
		this.tipoEsportazione = tipoEsportazione;
		result = null;
	}

	@Override
	protected JComponent createDialogContentPane() {
		JPanel rootPanel = new JPanel(new VerticalLayout());
		if (tipoEsportazione.isRichiediData()) {
			IDateEditor textFieldDateEditor = new PanjeaTextFieldDateEditor("dd/MM/yy HH:mm", "##/##/## ##:##", '_');
			dateChooser = ((PanjeaComponentFactory) getComponentFactory()).createDateChooser(textFieldDateEditor);
			dateChooser.cleanup();
			dateChooser.setDate(Calendar.getInstance().getTime());
			dataInserita = dateChooser.getDate();
			dateChooser.addPropertyChangeListener("date", new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					// CAUSA BUG del dateChooser devo impostare la data sul propertyChange
					dataInserita = (Date) evt.getNewValue();
				}
			});
			((JTextFieldDateEditor) dateChooser.getDateEditor()).setEditable(true);
			JPanel datePanel = new JPanel(new FlowLayout());
			datePanel.add(new JLabel("Data: "));
			datePanel.add(dateChooser);
			rootPanel.add(datePanel);
		}
		((JTextField) dateChooser.getDateEditor().getUiComponent()).selectAll();
		return rootPanel;
	}

	/**
	 * 
	 * @return parametri selezionati
	 */
	public Map<String, Object> getParametri() {
		return result;
	}

	@Override
	protected boolean onFinish() {
		result = new HashMap<String, Object>();
		boolean valid = true;
		if (dateChooser != null) {
			valid = dataInserita != null;
			result.put("data", dataInserita);
		}
		if (!valid) {
			result = null;
		}
		return valid;
	}

}
