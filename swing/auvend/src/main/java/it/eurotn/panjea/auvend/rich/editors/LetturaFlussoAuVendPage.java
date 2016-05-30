/**
 * 
 */
package it.eurotn.panjea.auvend.rich.editors;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.rich.forms.LetturaFlussoAuVendForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;

/**
 * Page per l'oggetto {@link LetturaFlussoAuVend}.
 * 
 * @author adriano
 * @version 1.0, 02/gen/2009
 * 
 */
public class LetturaFlussoAuVendPage extends FormBackedDialogPageEditor implements InitializingBean {

	private static Logger logger = Logger.getLogger(LetturaFlussoAuVendPage.class);

	private static final String PAGE_ID = "letturaFlussoAuVend";

	private IAuVendBD auVendBD;
	private IAnagraficaBD anagraficaBD;
	private AziendaCorrente aziendaCorrente;

	/**
	 * Costruttore.
	 */
	public LetturaFlussoAuVendPage() {
		super(PAGE_ID, new LetturaFlussoAuVendForm());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("--> Enter afterPropertiesSet");
		org.springframework.util.Assert.notNull(aziendaCorrente, "AziendaCorrente non inizializzata ");
		org.springframework.util.Assert.notNull(anagraficaBD, "AnagraficaBD non inizializzata ");
		org.springframework.util.Assert.notNull(auVendBD, "AuVendBD non inizializzato");
		((LetturaFlussoAuVendForm) getForm()).setAziendaCorrente(aziendaCorrente);
	}

	@Override
	protected Object doDelete() {
		LetturaFlussoAuVend letturaFlussoAuVend = (LetturaFlussoAuVend) getForm().getFormObject();
		auVendBD.cancellaLetturaFlussoAuVend(letturaFlussoAuVend);
		return letturaFlussoAuVend;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.rich.editors.FormBackedDialogPageEditor#doSave()
	 */
	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		LetturaFlussoAuVend letturaFlussoAuVend = (LetturaFlussoAuVend) getBackingFormPage().getFormObject();
		letturaFlussoAuVend = auVendBD.salvaLetturaFlussoAuVend(letturaFlussoAuVend);
		logger.debug("--> Exit doSave");
		return letturaFlussoAuVend;
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
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param auVendBD
	 *            The auVendBD to set.
	 */
	public void setAuVendBD(IAuVendBD auVendBD) {
		this.auVendBD = auVendBD;
	}

	/**
	 * @param aziendaCorrente
	 *            The aziendaCorrente to set.
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}
}