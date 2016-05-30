package it.eurotn.panjea.anagrafica.rich.commands;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class ChooseAreaForDocumentoDialog extends MessageDialog {

	private class CloseDialogCommand extends ApplicationWindowAwareCommand {

		public static final String CLOSE_COMMAND_ID = "closeAreaForAreaPartiteDialogCommand";

		/**
		 * Costruttore.
		 */
		public CloseDialogCommand() {
			super(CLOSE_COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ChooseAreaForDocumentoDialog.this.getFinishCommand().execute();
		}

	}

	private List<Object> listAree = null;
	private Documento documento = null;

	private CloseDialogCommand closeDialogCommand;

	/**
	 * Costruttore.
	 * 
	 * @param documento
	 *            documento
	 * @param listAree
	 *            aree
	 */
	public ChooseAreaForDocumentoDialog(final Documento documento, final List<Object> listAree) {
		super("Selezione area documento", "testo");
		this.listAree = listAree;
		this.documento = documento;
	}

	@Override
	protected JComponent createDialogContentPane() {
		JPanel panel = getComponentFactory().createPanel(new GridLayout(4, 1));

		JLabel text1Label = getComponentFactory().createLabel(
				"Il documento " + ObjectConverterManager.toString(documento.getCodice()) + " del "
						+ new SimpleDateFormat("dd/MM/yyyy").format(documento.getDataDocumento())
						+ " risulta avere più aree.");
		panel.add(text1Label);
		JLabel text2Label = getComponentFactory().createLabel("Selezionare l'area da visualizzare.");
		panel.add(text2Label);

		for (final Object area : listAree) {

			JButton button = getComponentFactory().createButton(RcpSupport.getMessage(area.getClass().getName()));
			button.setAction(new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					LifecycleApplicationEvent event = new OpenEditorEvent(area);
					Application.instance().getApplicationContext().publishEvent(event);
					getCloseDialogCommand().execute();
				}
			});
			button.setText(RcpSupport.getMessage(area.getClass().getName()));
			button.setIcon(getIconSource().getIcon(area.getClass().getName()));
			panel.add(button);
		}

		registerDefaultCommand(getCloseDialogCommand());

		return panel;
	}

	/**
	 * @return close command
	 */
	public CloseDialogCommand getCloseDialogCommand() {
		if (closeDialogCommand == null) {
			closeDialogCommand = new CloseDialogCommand();
		}

		return closeDialogCommand;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return new AbstractCommand[] { getCloseDialogCommand() };
	}

	@Override
	protected void onAboutToShow() {
		super.onAboutToShow();

		getCloseDialogCommand().setDefaultButtonIn(getDialog());
	}

}
