/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta;

import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.pm.ParametriCreazioneAreaEffetti;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;
import java.util.Set;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author Leonardo
 */
public class GenerazioneDistintaBancariaDialog extends PanjeaTitledPageApplicationDialog {

	private GenerazioneDistintaBancariaPage generazioneDistintaBancariaPage = null;
	private ITesoreriaBD tesoreriaBD;
	private AreaEffetti areaEffetto;

	/**
	 * Costruttore.
	 *
	 * @param areaEffetti
	 *            areaeffetti di origine
	 */
	public GenerazioneDistintaBancariaDialog(final AreaEffetti areaEffetti) {
		super();
		this.areaEffetto = areaEffetti;
		generazioneDistintaBancariaPage = new GenerazioneDistintaBancariaPage(areaEffetti);
		this.setDialogPage(generazioneDistintaBancariaPage);
		this.setPreferredSize(new Dimension(700, 400));

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
		generazioneDistintaBancariaPage.addFormGuard(getFinishCommand());
	}

	@Override
	protected boolean onFinish() {
		// recupera i dati per la testata per la distinta da generare
		// AreaPagamentoPM areaPagamentoPM = (AreaPagamentoPM) generazioneDistintaBancariaPage.getAreaEffetti();
		// .getFormObject();
		// recupera l'areaPartiteFullDTO con gli effetti modificati
		Set<Effetto> effetti = this.generazioneDistintaBancariaPage.getEffetti();

		ParametriCreazioneAreaEffetti parametri = this.generazioneDistintaBancariaPage
				.getParametriCreazioneAreaEffetti();
		// crea la distinta bancaria con i dati passati
		AreaDistintaBancaria areaDistintaBancaria = tesoreriaBD.creaDistintaBancaria(parametri.getDataDocumento(),
				parametri.getNumeroDocumento(), areaEffetto, parametri.getSpese(), parametri.getSpeseDistinta(),
				effetti);

		// lancio la openEditor con la nuova distinta bancaria creata
		ParametriRicercaAreeTesoreria parametriRicercaAreeTesoreria = ParametriRicercaAreeTesoreria
				.creaParametriRicercaAreeTesoreria(areaDistintaBancaria);
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
