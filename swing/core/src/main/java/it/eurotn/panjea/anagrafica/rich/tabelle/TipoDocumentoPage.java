/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.anagrafica.rich.editors.documento.TipoDocumentoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.dialog.TitledApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * 
 * @author adriano
 * @version 1.0, 17/mag/07
 */
public class TipoDocumentoPage extends FormBackedDialogPageEditor {

	private class CopiaTipoDocumentoCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "copiaTipoDocumentoCommand";

		/**
		 * Costruttore.
		 */
		public CopiaTipoDocumentoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ApplicationDialog dialog = new CopiaTipoDocumentoDialog();
			dialog.showDialog();
		}

	}

	private class CopiaTipoDocumentoDialog extends TitledApplicationDialog {

		private JTextField codiceTextField;
		private JTextField descrizioneTextField;

		/**
		 * Costruttore.
		 */
		public CopiaTipoDocumentoDialog() {
			super("Copia tipo documento", null);

		}

		@Override
		protected JComponent createDialogContentPane() {
			FormLayout layout = new FormLayout("right:pref,4dlu,fill:50dlu,fill:100dlu",
					"2dlu,default,10dlu,default,2dlu,default");
			JPanel panel = new JPanel(layout);
			panel.setPreferredSize(new Dimension(440, 140));
			panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
			CellConstraints cc = new CellConstraints();

			panel.add(new JLabel("<html><b>Inserire il codice e la descrizione del nuovo tipo documento</b></html>"),
					cc.xyw(1, 2, 4));

			panel.add(new JLabel("Codice"), cc.xy(1, 4));
			codiceTextField = new JTextField();
			panel.add(codiceTextField, cc.xy(3, 4));

			panel.add(new JLabel("Descrizione"), cc.xy(1, 6));
			descrizioneTextField = new JTextField();
			panel.add(descrizioneTextField, cc.xyw(3, 6, 2));

			return panel;
		}

		@Override
		protected JComponent createTitledDialogContentPane() {
			return null;
		}

		@Override
		protected String getCancelCommandId() {
			return "cancelCommand";
		}

		@Override
		protected String getFinishCommandId() {
			return "okCommand";
		}

		@Override
		protected boolean onFinish() {
			if (!codiceTextField.getText().trim().isEmpty() && !descrizioneTextField.getText().trim().isEmpty()) {
				TipoDocumento tipoDocumento = (TipoDocumento) getBackingFormPage().getFormObject();

				TipoDocumento tipoDocumentoCopia = documentiBD.copiaTipoDocumento(codiceTextField.getText().trim(),
						descrizioneTextField.getText().trim(), tipoDocumento);

				tipoDocumentoCopia = documentiBD.caricaTipoDocumento(tipoDocumentoCopia.getId());

				LifecycleApplicationEvent event = new OpenEditorEvent(tipoDocumentoCopia);
				Application.instance().getApplicationContext().publishEvent(event);

			} else {
				MessageDialog dialog = new MessageDialog("ATTENZIONE",
						"Inserire il codice e la descrizione del nuovo tipo documento");
				dialog.showDialog();
				return false;
			}
			return true;
		}
	}

	private static Logger logger = Logger.getLogger(TipoDocumentoPage.class);
	private IDocumentiBD documentiBD;

	private static final String PAGE_ID = "tipoDocumentoPage";

	private String newTipoDocumentoCommand;

	private CopiaTipoDocumentoCommand copiaTipoDocumentoCommand;

	/**
	 * Costruttore.
	 * 
	 * @param tipoDocumento
	 *            {@link TipoDocumento}
	 * @param anagraficaBD
	 *            {@link IAnagraficaBD}
	 */
	public TipoDocumentoPage(final TipoDocumento tipoDocumento, final IAnagraficaBD anagraficaBD) {
		super(PAGE_ID, new TipoDocumentoForm(tipoDocumento, anagraficaBD));
	}

	@Override
	protected Object doDelete() {
		TipoDocumento tipoDocumento = (TipoDocumento) TipoDocumentoPage.this.getBackingFormPage().getFormObject();
		if (tipoDocumento.getId() != null) {
			TipoDocumentoPage.this.documentiBD.cancellaTipoDocumento(tipoDocumento);
			return tipoDocumento;
		}
		return null;
	}

	@Override
	protected Object doSave() {
		Object object = getBackingFormPage().getFormObject();
		TipoDocumento tipoDocumento = (TipoDocumento) object;
		String registroProtocollo = tipoDocumento.getRegistroProtocollo();
		if (registroProtocollo == null) {
			registroProtocollo = "";
		}
		if (registroProtocollo.equals(getMessage(TipoDocumentoForm.COMBO_BOX_VALUE_MANUALE))) {
			tipoDocumento.setRegistroProtocollo(null);
		} else {
			String[] protocolloSplit = tipoDocumento.getRegistroProtocollo().split(" - ");
			tipoDocumento.setRegistroProtocollo(protocolloSplit[0]);
		}
		tipoDocumento = documentiBD.salvaTipoDocumento(tipoDocumento);
		return tipoDocumento;
	}

	@Override
	public AbstractCommand[] getCommand() {
		return new AbstractCommand[] { toolbarPageEditor.getNewCommand(), toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
				toolbarPageEditor.getDeleteCommand(), getCopiaTipoDocumentoCommand() };
	}

	/**
	 * @return Returns the copiaTipoDocumentoCommand.
	 */
	public CopiaTipoDocumentoCommand getCopiaTipoDocumentoCommand() {
		if (copiaTipoDocumentoCommand == null) {
			copiaTipoDocumentoCommand = new CopiaTipoDocumentoCommand();
		}

		return copiaTipoDocumentoCommand;
	}

	/**
	 * @return Returns the documentiBD.
	 */
	public IDocumentiBD getDocumentiBD() {
		return documentiBD;
	}

	/**
	 * @return Returns the newTipoDocumentoCommand.
	 */
	public String getNewTipoDocumentoCommand() {
		return newTipoDocumentoCommand;
	}

	@Override
	public void loadData() {
		// lanciare un firepropertychange genera diversi problemi e' quindi
		// necessario
		// trovare un'altra soluzione quando ho un tipodocumento di classeOrdine
		// non ho
		// attiva la sezione tipoAreaContabile, deve essere quindi tolto il
		// button per
		// raggiungere tale area ( che rimane invece attiva all'apertura
		// dell'editor ).
		logger.debug("--> Enter loadData");
	}

	@Override
	public void onPostPageOpen() {
		updateCommands();
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void postSetFormObject(Object object) {

	}

	@Override
	public void preSetFormObject(Object object) {

	}

	@Override
	public void refreshData() {
		logger.debug("--> Enter refreshData");
	}

	/**
	 * @param documentiBD
	 *            The documentiBD to set.
	 */
	public void setDocumentiBD(IDocumentiBD documentiBD) {
		this.documentiBD = documentiBD;
	}

	/**
	 * @param newTipoDocumentoCommand
	 *            The newTipoDocumentoCommand to set.
	 */
	public void setNewTipoDocumentoCommand(String newTipoDocumentoCommand) {
		this.newTipoDocumentoCommand = newTipoDocumentoCommand;
	}
}
