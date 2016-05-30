package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.dialog.InputApplicationDialog;

public class NuovaConfigurazioneDistintaCommand extends ActionCommand {

	public static final String CONF_DISTINTA_PARAMETER = "configurazioneDistinta";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private ConfigurazioneDistinta configurazioneDistinta;

	/**
	 * Costruttore.
	 *
	 * @param magazzinoAnagraficaBD
	 *            bd .
	 */
	public NuovaConfigurazioneDistintaCommand(final IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		super("nuovaConfigurazioneDistintaCommand");
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		final Articolo articoloDistinta = (Articolo) getParameter(CONF_DISTINTA_PARAMETER);
		InputApplicationDialog inputDialog = new InputApplicationDialog() {

			@Override
			protected void onCancel() {
				super.onCancel();
				configurazioneDistinta = null;
			}

			@Override
			protected boolean onFinish() {
				String nomeConfigurazione = (String) getInputValue();
				ConfigurazioneDistinta configurazione = new ConfigurazioneDistinta();
				configurazione.setNome(nomeConfigurazione);
				configurazione.setDistinta(articoloDistinta);
				configurazioneDistinta = magazzinoAnagraficaBD.salvaConfigurazioneDistinta(configurazione);
				return true;
			}
		};
		inputDialog.setInputLabelMessage("Nome distinta");
		inputDialog.setTitle("NOME CONFIGURAZIONE");
		inputDialog.showDialog();
		inputDialog.setCallingCommand(null);
	}

	/**
	 * @return Returns the configurazioneDistinta.
	 */
	public ConfigurazioneDistinta getConfigurazioneDistinta() {
		return configurazioneDistinta;
	}

}
