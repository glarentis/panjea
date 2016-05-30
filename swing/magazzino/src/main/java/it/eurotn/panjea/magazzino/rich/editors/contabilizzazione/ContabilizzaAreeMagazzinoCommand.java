package it.eurotn.panjea.magazzino.rich.editors.contabilizzazione;

import it.eurotn.panjea.contabilita.service.exception.ContiEntitaAssentiException;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoContabilizzazioneBD;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class ContabilizzaAreeMagazzinoCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "contabilizzaAreeMagazzinoCommand";

	public static final String PARAM_AREE_DA_CONTABILIZZARE = "paramAreeDaContabilizzare";
	public static final String PARAM_ANNO_CONTABILE = "paramAnnoContabile";

	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;

	/**
	 * Costruttore.
	 * 
	 */
	public ContabilizzaAreeMagazzinoCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.magazzinoContabilizzazioneBD = RcpSupport.getBean(MagazzinoContabilizzazioneBD.BEAN_ID);
	}

	@Override
	protected void doExecuteCommand() {

		int annoContabile = (int) getParameter(PARAM_ANNO_CONTABILE);

		@SuppressWarnings("unchecked")
		List<AreaMagazzinoRicerca> aree = (List<AreaMagazzinoRicerca>) getParameter(PARAM_AREE_DA_CONTABILIZZARE);

		List<Integer> idAree = new ArrayList<Integer>();

		for (AreaMagazzinoRicerca areaMagazzinoRicerca : aree) {
			idAree.add(areaMagazzinoRicerca.getIdAreaMagazzino());
		}

		try {
			magazzinoContabilizzazioneBD.contabilizzaAreeMagazzino(idAree, true, annoContabile);
		} catch (ContabilizzazioneException e) {
			LifecycleApplicationEvent event = new OpenEditorEvent(e);
			Application.instance().getApplicationContext().publishEvent(event);
		} catch (Exception e) {
			onPostExecute();
			logger.error("-->errore durante la contabilizzaizone dei documenti.", e);
			if (e.getCause() instanceof ContiEntitaAssentiException) {
				new ContiEntitaAssentiExceptionDialog((ContiEntitaAssentiException) e.getCause()).showDialog();
			} else {
				PanjeaSwingUtil.checkAndThrowException(e);
			}
		}

		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		if (pluginManager.isPresente(PluginManager.PLUGIN_CENTRO_COSTI)) {
			ActionCommand commandVerificaCentriDiCosto = RcpSupport.getCommand("verificaCentriDiCostoCommand");
			if (commandVerificaCentriDiCosto != null) {
				commandVerificaCentriDiCosto.addParameter("check", true);
				commandVerificaCentriDiCosto.execute();
			}
		}
	}

}
