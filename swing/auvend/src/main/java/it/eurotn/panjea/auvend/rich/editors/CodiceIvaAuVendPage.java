/**
 * 
 */
package it.eurotn.panjea.auvend.rich.editors;

import it.eurotn.panjea.auvend.domain.CodiceIvaAuVend;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.rich.forms.CodiceIvaAuVendForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;

/**
 * page per {@link CodiceIvaAuVend}.
 * 
 * @author angelo
 * @version 1.0, 09/11/2010
 * 
 */
public class CodiceIvaAuVendPage extends FormBackedDialogPageEditor implements InitializingBean {

	private Logger logger = Logger.getLogger(CodiceIvaAuVendPage.class);

	private static final String PAGE_ID = "codiceIvaAuVendPage";

	private IAuVendBD auVendBD;

	/**
	 * Costruttore.
	 * 
	 */
	public CodiceIvaAuVendPage() {
		super(PAGE_ID, new CodiceIvaAuVendForm());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("--> Enter afterPropertiesSet");
		org.springframework.util.Assert.notNull(auVendBD);
	}

	@Override
	protected Object doDelete() {
		CodiceIvaAuVend codiceIvaAuVend = (CodiceIvaAuVend) getForm().getFormObject();
		auVendBD.cancellaCodiceIvaAuVend(codiceIvaAuVend.getId());
		return codiceIvaAuVend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.FormBackedDialogPageEditor#doSave()
	 */
	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		CodiceIvaAuVend codiceIvaAuVend = (CodiceIvaAuVend) getForm().getFormObject();
		codiceIvaAuVend = auVendBD.salvaCodiceIvaAuVend(codiceIvaAuVend);
		logger.debug("--> Exit doSave");
		return codiceIvaAuVend;
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
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#onPostPageOpen()
	 */
	@Override
	public void onPostPageOpen() {
		// nothing to do
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
		// nothing to do
	}

	/**
	 * @param auVendBD
	 *            The auVendBD to set.
	 */
	public void setAuVendBD(IAuVendBD auVendBD) {
		this.auVendBD = auVendBD;
	}
}
