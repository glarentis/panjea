package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta;

import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistintaBase;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.command.AbstractDeleteCommand;

import java.util.List;

import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.RefreshableValueHolder;

public class CancellaConfigurazioneDistintaCommand extends AbstractDeleteCommand {

	private RefreshableValueHolder configurazioniDistintaValueHolder;
	private ValueModel configurazioneSelezionataValueModel;
	private IMagazzinoAnagraficaBD bd;

	/**
	 * Costruttore.
	 * 
	 * @param configurazioneSelezionataValueModel
	 *            .
	 * @param configurazioniDistintaValueHolder
	 *            .
	 * @param bd
	 *            .
	 */
	public CancellaConfigurazioneDistintaCommand(final IMagazzinoAnagraficaBD bd,
			final RefreshableValueHolder configurazioniDistintaValueHolder,
			final ValueModel configurazioneSelezionataValueModel) {
		super("cancellaConfigurazioneDistintaCommand");
		this.bd = bd;
		this.configurazioniDistintaValueHolder = configurazioniDistintaValueHolder;
		this.configurazioneSelezionataValueModel = configurazioneSelezionataValueModel;
	}

	@Override
	public Object onDelete() {
		ConfigurazioneDistinta configurazione = (ConfigurazioneDistinta) configurazioneSelezionataValueModel.getValue();
		if (configurazione instanceof ConfigurazioneDistintaBase) {
			return null;
		}
		bd.cancellaConfigurazioneDistinta(configurazione);
		configurazioniDistintaValueHolder.refresh();
		@SuppressWarnings("unchecked")
		List<ConfigurazioneDistinta> configurazioni = (List<ConfigurazioneDistinta>) configurazioniDistintaValueHolder
				.getValue();
		configurazioni.remove(configurazione);
		configurazioniDistintaValueHolder.setValue(configurazioni);
		configurazioneSelezionataValueModel.setValue(configurazioni.get(0));
		return null;
	}

}
