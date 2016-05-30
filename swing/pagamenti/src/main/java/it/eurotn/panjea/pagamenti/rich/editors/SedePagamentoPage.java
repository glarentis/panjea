/**
 * 
 */
package it.eurotn.panjea.pagamenti.rich.editors;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.rich.bd.IAnagraficaPagamentiBD;
import it.eurotn.panjea.pagamenti.rich.forms.SedePagamentoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;

/**
 * Page per {@link SedePagamento}.
 * 
 * @author adriano
 * @version 1.0, 19/dic/2008
 */
public class SedePagamentoPage extends FormBackedDialogPageEditor {

	private static Logger logger = Logger.getLogger(SedePagamentoPage.class);

	private static final String PAGE_ID = "sedePagamentoPage";

	private IAnagraficaPagamentiBD anagraficaPagamentiBD;

	private SedeEntita sedeEntita;

	/**
	 * Costruttore di default.
	 */
	public SedePagamentoPage() {
		super(PAGE_ID, new SedePagamentoForm());
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		SedePagamento sedePagamento = (SedePagamento) getBackingFormPage().getFormObject();
		sedePagamento = anagraficaPagamentiBD.salvaSedePagamento(sedePagamento);
		return sedeEntita;
	}

	@Override
	public void loadData() {
		logger.debug("--> Enter loadData");
		if ((sedeEntita != null) && (sedeEntita.getId() != null)) {
			SedePagamento sedePagamento = anagraficaPagamentiBD.caricaSedePagamentoBySedeEntita(sedeEntita.getId());
			sedePagamento.setSedeEntita(sedeEntita);
			super.setFormObject(sedePagamento);
		}
		logger.debug("--> Exit loadData");
	}

	@Override
	public void onPostPageOpen() {
		// nothing to do

	}

	@Override
	public boolean onPrePageOpen() {
		return (sedeEntita != null) && (sedeEntita.getId() != null);
	}

	@Override
	public void refreshData() {
		loadData();

	}

	/**
	 * @param anagraficaPagamentiBD
	 *            The anagraficaPagamentiBD to set.
	 */
	public void setAnagraficaPagamentiBD(IAnagraficaPagamentiBD anagraficaPagamentiBD) {
		this.anagraficaPagamentiBD = anagraficaPagamentiBD;
	}

	@Override
	public void setFormObject(Object object) {
		logger.debug("--> Enter setFormObject");
		sedeEntita = (SedeEntita) object;
		loadData();
		logger.debug("--> Exit setFormObject");
	}

}
