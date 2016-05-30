/**
 * 
 */
package it.eurotn.panjea.auvend.rich.editors;

import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.rich.forms.TipoDocumentoBaseAuVendForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * Page per l'oggetto {@link TipoDocumentoBaseAuVend}.
 * 
 * @author adriano
 * @version 1.0, 30/dic/2008.
 * 
 */
public class TipoDocumentoBaseAuVendPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "tipoDocumentoBaseAuVendPage";

	private static Logger logger = Logger.getLogger(TipoDocumentoBaseAuVendPage.class);

	private IAuVendBD auVendBD;

	/**
	 * costruttore.
	 */
	public TipoDocumentoBaseAuVendPage() {
		super(PAGE_ID, new TipoDocumentoBaseAuVendForm(new TipoDocumentoBaseAuVend()));
	}

	@Override
	protected Object doDelete() {
		logger.debug("--> Enter doDelete");
		TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend = (TipoDocumentoBaseAuVend) getForm().getFormObject();
		auVendBD.cancellaTipoDocumentoBaseAuVend(tipoDocumentoBaseAuVend);
		logger.debug("--> Exit doDelete");
		return tipoDocumentoBaseAuVend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.FormBackedDialogPageEditor#doSave()
	 */
	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend = (TipoDocumentoBaseAuVend) getBackingFormPage()
				.getFormObject();
		tipoDocumentoBaseAuVend = auVendBD.salvaTipoDocumentoBaseAuVend(tipoDocumentoBaseAuVend);
		logger.debug("--> Exit doSave");
		return tipoDocumentoBaseAuVend;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#loadData()
	 */
	@Override
	public void loadData() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#onPostPageOpen()
	 */
	@Override
	public void onPostPageOpen() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#onPrePageOpen()
	 */
	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#refreshData()
	 */
	@Override
	public void refreshData() {

	}

	/**
	 * @param auVendBD
	 *            The auVendBD to set.
	 */
	public void setAuVendBD(IAuVendBD auVendBD) {
		this.auVendBD = auVendBD;
	}
}
