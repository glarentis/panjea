package it.eurotn.panjea.ordini.rich.forms.righeordine;

import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta.DistintaConfigurazioneDialog;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.util.PanjeaEJBUtil;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

public class GestioneConfigurazioneArticoloCommand extends ActionCommand {

	private class AssociaConfigurazioneClosure implements Closure {

		private RigaArticolo ro;

		/**
		 * @param ro
		 *            riga ordine da associare
		 */
		public AssociaConfigurazioneClosure(final RigaArticolo ro) {
			this.ro = ro;
		}

		@Override
		public Object call(Object paramObject) {
			ConfigurazioneDistinta conf = (ConfigurazioneDistinta) paramObject;
			rigaArticolo = bd.associaConfigurazioneDistintaARigaOrdine(ro, conf);
			return null;
		}
	}

	public static final String PARAM_ID_RIGA_ORDINE = "idRigaOrdine";

	private IOrdiniDocumentoBD bd = null;
	private RigaArticolo rigaArticolo = null;

	/**
	 * Costruttore.
	 */
	public GestioneConfigurazioneArticoloCommand() {
		super("gestioneConfigurazioneArticoloCommand");
		bd = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		rigaArticolo = null;
		RigaArticolo ro = (RigaArticolo) getParameter(PARAM_ID_RIGA_ORDINE);
		if (!ro.isNew()) {
			// ricarico la riga ordine perchè potrebbe avermi cambiato la configurazione
			ro = (RigaArticolo) bd.caricaRigaOrdine(ro);
			Integer idConfigurazione = PanjeaEJBUtil.getLazyId(ro.getConfigurazioneDistinta());
			DistintaConfigurazioneDialog dialog = null;
			dialog = new DistintaConfigurazioneDialog(ro.getArticolo(), idConfigurazione,
					new AssociaConfigurazioneClosure(ro));
			dialog.showDialog();
		}
	}

	/**
	 * @return RigaArticolo
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}

}
