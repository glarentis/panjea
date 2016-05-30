package it.eurotn.panjea.magazzino.rich.editors.inventarioarticolo;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.rich.dialogs.AskDialog;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

public class PreparaInventariArticoloDialog extends AskDialog {

	private class FinishCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {

			Date data = dataPreparazione.getDate();
			List<DepositoLite> depositi = depositiControl.getDepositiSelezionati();

			if (data == null || depositi.isEmpty()) {
				MessageDialog dialog = new MessageDialog("ATTENZIONE", "Nessuna data o deposito selezionato.");
				dialog.showDialog();
			}

			return data != null && !depositi.isEmpty();
		}
	}

	private JDateChooser dataPreparazione;
	private PanjeaTextFieldDateEditor dateEditor;

	private DepositiInventarioArticoloControl depositiControl;

	private boolean preparaInventariConfirmed = false;

	{
		dateEditor = new PanjeaTextFieldDateEditor("dd/MM/yy", "##/##/##", '_');
		dataPreparazione = new JDateChooser(dateEditor);
		dataPreparazione.getDateEditor().getUiComponent().setName("dataPreparazione");
	}

	/**
	 * Costruttore.
	 *
	 */
	public PreparaInventariArticoloDialog() {
		super("Preparazione inventari", "Selezionare la data e i depositi per i quali preparare gli inventari.");
		setResizable(true);
	}

	@Override
	protected JComponent createDialogContentPane() {

		FormLayout formLayout = new FormLayout("right:pref, 4dlu, left:pref:grow",
				"default, 5dlu, fill:min(100dlu;pref):grow");
		JPanel rootPanel = getComponentFactory().createPanel(formLayout);

		CellConstraints cc = new CellConstraints();
		rootPanel.add(new JLabel(RcpSupport.getMessage("data")), cc.xy(1, 1));
		rootPanel.add(dataPreparazione, cc.xy(3, 1));

		depositiControl = new DepositiInventarioArticoloControl();
		rootPanel.add(depositiControl.getControl(), cc.xyw(1, 3, 3));

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(rootPanel, BorderLayout.CENTER);

		return panel;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		getFinishCommand().addCommandInterceptor(new FinishCommandInterceptor());
		return new AbstractCommand[] { getFinishCommand(), getNegateCommand() };
	}

	/**
	 * @return Returns the dataPreparazione.
	 */
	public Date getDataPreparazione() {
		return dataPreparazione.getDate();
	}

	/**
	 * @return Returns the depositi selezionati
	 */
	public List<DepositoLite> getDepositiSelezionati() {
		return depositiControl.getDepositiSelezionati();
	}

	@Override
	protected String getFinishCommandId() {
		return "okCommand";
	}

	/**
	 * @return Returns the preparaInventariConfirmed.
	 */
	public boolean isPreparaInventariConfirmed() {
		return preparaInventariConfirmed;
	}

	@Override
	protected void onConfirm() {
		preparaInventariConfirmed = Boolean.TRUE;
	}

	@Override
	protected void onNegate() {
		onCancel();
		preparaInventariConfirmed = Boolean.FALSE;
	}

}
