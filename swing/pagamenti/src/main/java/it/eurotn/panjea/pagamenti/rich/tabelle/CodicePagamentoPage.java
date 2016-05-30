package it.eurotn.panjea.pagamenti.rich.tabelle;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.rich.bd.IAnagraficaPagamentiBD;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;

public class CodicePagamentoPage extends FormBackedDialogPageEditor implements InitializingBean {
	private static Logger logger = Logger.getLogger(CodicePagamentoPage.class.getName());
	private IAnagraficaPagamentiBD anagraficaPagamentiBD;
	private IPartiteBD partiteBD;

	private static final String PAGE_ID = "codicePagamentoPage";

	/**
	 * Costruttore.
	 */
	public CodicePagamentoPage() {
		super(PAGE_ID, new CodicePagamentoForm(new CodicePagamento()));
		getBackingFormPage().getFormModel().addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("readOnly")) {
					((CodicePagamentoForm) getForm()).getRigheStrutturaPartiteTablePage().getAggiungiCommand()
							.setEnabled(!(Boolean) evt.getNewValue());
					((CodicePagamentoForm) getForm()).getRigheStrutturaPartiteTablePage().getEliminaCommand()
							.setEnabled(!(Boolean) evt.getNewValue());
				}
			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		((CodicePagamentoForm) getForm()).setAnagraficaPagamentiBD(anagraficaPagamentiBD);
		((CodicePagamentoForm) getForm()).setPartiteBD(this.partiteBD);
	}

	@Override
	protected Object doDelete() {
		anagraficaPagamentiBD.cancellaCodicePagamento((CodicePagamento) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		CodicePagamento codicePagamento = (CodicePagamento) this.getForm().getFormObject();
		CodicePagamento codicePagamentoSaved = anagraficaPagamentiBD.salvaCodicePagamento(codicePagamento);
		logger.debug("--> Exit doSave");
		return codicePagamentoSaved;
	}

	@Override
	public AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	/**
	 * @return Returns the partiteBD.
	 */
	public IPartiteBD getPartiteBD() {
		return partiteBD;
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {

	}

	/**
	 * @param anagraficaPagamentiBD
	 *            the anagraficaPagamentiBD to set
	 */
	public void setAnagraficaPagamentiBD(IAnagraficaPagamentiBD anagraficaPagamentiBD) {
		this.anagraficaPagamentiBD = anagraficaPagamentiBD;
	}

	@Override
	public void setFormObject(Object object) {
		CodicePagamento codicePagamento = (CodicePagamento) object;
		if (codicePagamento.getId() != null) {
			codicePagamento = anagraficaPagamentiBD.caricaCodicePagamento(codicePagamento.getId());
		}
		super.setFormObject(codicePagamento);
	}

	/**
	 * @param partiteBD
	 *            The partiteBD to set.
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

}
