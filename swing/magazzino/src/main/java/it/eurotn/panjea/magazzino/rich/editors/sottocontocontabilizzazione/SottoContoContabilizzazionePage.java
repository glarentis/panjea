package it.eurotn.panjea.magazzino.rich.editors.sottocontocontabilizzazione;

import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.panjea.magazzino.rich.forms.sottocontocontabilizzazione.SottoContoContabilizzazioneForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.beans.factory.InitializingBean;

/**
 * Viene utilizzata per modificare un sottoContoContabilizzazione.<br/>
 * Modifica sia un sottoconto per i RICAVI o per i COSTI<br/>
 * .
 * 
 * 
 * @author giangi
 * 
 */
public class SottoContoContabilizzazionePage extends FormBackedDialogPageEditor implements InitializingBean {

	private static final String PAGE_ID = "sottoContoContabilizzazionePage";
	private final ETipoEconomico tipoEconomico;
	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	/**
	 * PAGE_ID server per poter configurare i messaggi perchè la pagina<br/>
	 * viene utilizzata sia per i costi che per i ricavi.
	 * 
	 * @param tipoEconomico
	 *            {@link ETipoEconomico}
	 */
	public SottoContoContabilizzazionePage(final ETipoEconomico tipoEconomico) {
		super(PAGE_ID + tipoEconomico.name().toLowerCase(), new SottoContoContabilizzazioneForm(
				new SottoContoContabilizzazione()));
		this.tipoEconomico = tipoEconomico;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		((SottoContoContabilizzazioneForm) getForm()).setTipoEconomico(tipoEconomico);
	}

	@Override
	protected Object doDelete() {
		magazzinoContabilizzazioneBD.cancellaSottoContoContabilizzazione(((SottoContoContabilizzazione) getForm()
				.getFormObject()).getId());
		return getForm().getFormObject();
	}

	@Override
	protected Object doSave() {
		return magazzinoContabilizzazioneBD.salvaSottoContoContabilizzazione((SottoContoContabilizzazione) getForm()
				.getFormObject());

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
	 * @param magazzinoContabilizzazioneBD
	 *            magazzinoContabilizzazioneBD to set
	 */
	public void setMagazzinoContabilizzazioneBD(IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD) {
		this.magazzinoContabilizzazioneBD = magazzinoContabilizzazioneBD;
	}

}
