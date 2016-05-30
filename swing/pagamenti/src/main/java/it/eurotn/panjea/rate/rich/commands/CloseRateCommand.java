/**
 * 
 */
package it.eurotn.panjea.rate.rich.commands;

import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.rich.editors.rate.RateTablePage;
import it.eurotn.panjea.rate.rich.forms.AbstractAreaRateModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * 
 * Command per la conferma delle {@link Rata} di {@link AreaRate} e per segnalare la chiusura del documento.
 * 
 * @author adriano
 * @version 1.0, 16/lug/08
 * 
 */
public class CloseRateCommand extends ActionCommand implements PropertyChangeListener {

	private static final String COMMAND_ID = ".confermaRateCommand";

	private AbstractAreaRateModel areaRateModel;

	/**
	 * Costruttore.
	 */
	public CloseRateCommand() {
		super(RateTablePage.PAGE_ID + COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(RateTablePage.PAGE_ID + ".controller");
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		// conferma del documento
		if (areaRateModel.isAreaRatePresente()) {
			areaRateModel.validaAreaRate();
			setEnabled(false);
		}
	}

	/**
	 * abilito il command se l'area risulta essere quadrata e non valida.
	 * 
	 * @param evt
	 *            evento
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean rateDaConfermare = false;
		if (areaRateModel.getAreaRate() != null && areaRateModel.getAreaRate().getRate() != null
				&& areaRateModel.getAreaRate().getRate().size() > 0) {
			rateDaConfermare = true;
		}
		CloseRateCommand.this.setEnabled(areaRateModel.isAreaRatePresente() && areaRateModel.isAreaRateQuadrata()
				&& !areaRateModel.isAreaRateValida() && rateDaConfermare);
	}

	/**
	 * @param areaPartiteModel
	 *            The areaPartiteModel to set.
	 */
	public void setAreaRateModel(AbstractAreaRateModel areaPartiteModel) {
		this.areaRateModel = areaPartiteModel;
		this.areaRateModel.addPropertyChangeListener(AbstractAreaRateModel.AREA_MODEL_AGGIORNATA, this);
		this.areaRateModel.addPropertyChangeListener(AbstractAreaRateModel.RIGA_AGGIORNATA, this);
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (areaRateModel.isAreaRateValida()) {
			enabled = false;
		}
		super.setEnabled(enabled);
	}
}