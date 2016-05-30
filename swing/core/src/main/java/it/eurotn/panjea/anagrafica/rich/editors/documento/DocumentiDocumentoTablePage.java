/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.util.ParametriRicercaDocumento;
import it.eurotn.panjea.anagrafica.rich.forms.documento.DocumentiDocumentoForm;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * Tabella di documenti con possibilità di aggiungere un elemento via searchText o rimuoverlo dalla lista.<br>
 * Questa classe astratta lascia l'implementazione di aggiunta e rimozione del documento alla derivata.<br>
 * La pagina è composta da un header dove è presente una searchText di documento con relativo filtro su entità e da una
 * tabella dove vengono visualizzati i documenti con relativo command per la rimozione.
 * 
 * @author leonardo
 */
public abstract class DocumentiDocumentoTablePage extends DocumentiTablePage {

	/**
	 * Aggiunge un elemento alla tabella solo dopo essere stato aggiunto correttamente dal metodo
	 * it.eurotn.panjea.anagrafica.rich.editors.documento.DocumentiDocumentoTablePage.doAddDocumento(Documento).
	 * 
	 * @author leonardo
	 */
	private class AddDocumentoCommand extends ActionCommand {

		public static final String COMMAND_ID = "addDocumentoCommand";

		/**
		 * Costruttore.
		 */
		public AddDocumentoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			Documento documento = getDocumentiDocumentoForm().getDocumento();
			if (documento != null && documento.getId() != null) {
				Documento documentoAggiunto = doAddDocumento(documento);
				if (documentoAggiunto != null) {
					loadTableData();

					ParametriRicercaDocumento parametriDocumento = new ParametriRicercaDocumento();
					parametriDocumento
							.setEntita(DocumentiDocumentoTablePage.this.parametriRicercaDocumento.getEntita());
					getDocumentiDocumentoForm().setFormObject(parametriDocumento);
				}
			}
		}

	}

	/**
	 * Rimuove un elemento dalla tabella solo dopo essere stato rimosso correttamente dal metodo
	 * it.eurotn.panjea.anagrafica.rich.editors.documento.DocumentiDocumentoTablePage.doRemoveDocumento(Documento).
	 * 
	 * @author leonardo
	 */
	private class RimuoviDocumentoCommand extends ActionCommand {

		private static final String COMMAND_ID = "deleteCommand";
		private Documento documento = null;

		/**
		 * Costruttore di default.
		 */
		public RimuoviDocumentoCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			if (documento != null && documento.getId() != null) {
				Documento documentoRimosso = doRemoveDocumento(documento);
				if (documentoRimosso != null) {
					getTable().removeRowObject(documentoRimosso);
				}
			}
		}

		/**
		 * @param documento
		 *            the documento to set
		 */
		public void setDocumento(Documento documento) {
			this.documento = documento;
		}

	}

	private ParametriRicercaDocumento parametriRicercaDocumento = null;
	private RimuoviDocumentoCommand rimuoviDocumentoCommand = null;
	private AddDocumentoCommand addDocumentoCommand = null;
	private DocumentiDocumentoForm documentiDocumentoForm = null;

	/**
	 * Costruttore.
	 */
	public DocumentiDocumentoTablePage() {
		super();
	}

	/**
	 * Metodo per implementare le operazioni di aggiunta del documento.
	 * 
	 * @param documento
	 *            il documento da aggiungere
	 * @return il documento aggiunto
	 */
	public abstract Documento doAddDocumento(Documento documento);

	/**
	 * Metodo per implementare le operazioni di rimozione del documento.
	 * 
	 * @param documento
	 *            il documento da rimuovere
	 * @return il documento rimosso
	 */
	public abstract Documento doRemoveDocumento(Documento documento);

	/**
	 * @return AddDocumentoToContrattoCommand
	 */
	private AddDocumentoCommand getAddDocumentoCommand() {
		if (addDocumentoCommand == null) {
			addDocumentoCommand = new AddDocumentoCommand();
		}
		return addDocumentoCommand;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getRimuoviDocumentoCommand() };
	}

	/**
	 * @return documentiDocumentoForm to get
	 */
	private DocumentiDocumentoForm getDocumentiDocumentoForm() {
		if (documentiDocumentoForm == null) {
			documentiDocumentoForm = new DocumentiDocumentoForm();
		}
		return documentiDocumentoForm;
	}

	@Override
	public JComponent getHeaderControl() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 0));
		panel.add(getDocumentiDocumentoForm().getControl(), BorderLayout.CENTER);
		panel.add(getAddDocumentoCommand().createButton(), BorderLayout.EAST);
		return panel;
	}

	/**
	 * @return RimuoviDocumentoDaDocumentoCommand
	 */
	private RimuoviDocumentoCommand getRimuoviDocumentoCommand() {
		if (rimuoviDocumentoCommand == null) {
			rimuoviDocumentoCommand = new RimuoviDocumentoCommand();
		}
		return rimuoviDocumentoCommand;
	}

	@Override
	public boolean onPrePageOpen() {
		super.onPostPageOpen();
		Component componentFocusable = PanjeaSwingUtil.findComponentFocusable(getDocumentiDocumentoForm().getControl()
				.getComponents());
		componentFocusable.requestFocusInWindow();
		return true;
	}

	@Override
	public void setFormObject(Object object) {
		super.setFormObject(object);
		// aggiorna i parametri dall'esterno impostando i parametri con eventuali filtri (i.e.entita)
		parametriRicercaDocumento = (ParametriRicercaDocumento) object;
		getDocumentiDocumentoForm().setFormObject(parametriRicercaDocumento);
	}

	@Override
	public void update(Observable observable, Object obj) {
		super.update(observable, obj);
		// aggiorna nel remove command il documento in modo da eliminare quello selezionato in tabella
		getRimuoviDocumentoCommand().setDocumento((Documento) obj);
	}

}
