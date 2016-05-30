/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.bonifico;

import it.eurotn.panjea.tesoreria.domain.AreaBonifico;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.rich.dialog.DefaultTitledPageApplicationDialog;

import java.awt.Dimension;
import java.util.Set;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author leonardo
 */
public class GenerazioneBonificoFornitoreDialog extends DefaultTitledPageApplicationDialog {

	private ITesoreriaBD tesoreriaBD;
	private GenerazioneBonificoFornitorePage generazioneBonificoFornitorePage;
	private AreaPagamenti areaPagamenti;

	/**
	 * Costruttore.
	 * 
	 * @param areaPagamenti
	 *            areaChiusure di origine
	 */
	public GenerazioneBonificoFornitoreDialog(final AreaPagamenti areaPagamenti) {
		super(areaPagamenti, null, new GenerazioneBonificoFornitorePage());
		this.setPreferredSize(new Dimension(700, 400));
		this.generazioneBonificoFornitorePage = (GenerazioneBonificoFornitorePage) getDialogPage();
		this.areaPagamenti = areaPagamenti;
	}

	/**
	 * @return the tesoreriaBD
	 */
	public ITesoreriaBD getTesoreriaBD() {
		return tesoreriaBD;
	}

	@Override
	protected String getTitle() {
		return getDialogPage().getTitle();
	}

	@Override
	protected void onAboutToShow() {

	}

	@Override
	protected boolean onFinish() {
		// recupera i dati per la testata per la distinta da generare

		GenerazioneBonificoFornitoreForm form = (GenerazioneBonificoFornitoreForm) generazioneBonificoFornitorePage
				.getBackingFormPage();
		AreaPagamenti areaConDatiDaSalvare = (AreaPagamenti) form.getFormObject();
		Set<Pagamento> pagamentiDaSalvare = form.getPagamenti();
		int size = pagamentiDaSalvare.size();
		if (size == 0) {
			return false;
		}

		// crea la distinta bancaria con i dati passati
		AreaBonifico areaBonifico = tesoreriaBD.creaAreaBonifico(
				areaConDatiDaSalvare.getDocumento().getDataDocumento(), areaConDatiDaSalvare.getDocumento().getCodice()
						.getCodice(), areaPagamenti, areaConDatiDaSalvare.getSpeseIncasso(), pagamentiDaSalvare);

		// lancio la openEditor con la nuova distinta bancaria creata
		ParametriRicercaAreeTesoreria parametriRicercaAreeTesoreria = ParametriRicercaAreeTesoreria
				.creaParametriRicercaAreeTesoreria(areaBonifico);
		LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaAreeTesoreria);
		Application.instance().getApplicationContext().publishEvent(event);
		return true;
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}
}