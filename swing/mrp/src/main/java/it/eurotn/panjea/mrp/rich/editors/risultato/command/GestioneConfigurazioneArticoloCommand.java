package it.eurotn.panjea.mrp.rich.editors.risultato.command;

import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta.DistintaConfigurazioneDialog;
import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.rich.factory.navigationloader.AbstractLoaderActionCommand;
import it.eurotn.util.PanjeaEJBUtil;

import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

public class GestioneConfigurazioneArticoloCommand extends AbstractLoaderActionCommand {

	private class AssociaConfigurazioneClosure implements Closure {

		private RigaArticolo ro;

		/**
		 *
		 * @param ro
		 *            riga ordine da associare
		 */
		public AssociaConfigurazioneClosure(final RigaArticolo ro) {
			this.ro = ro;
		}

		@Override
		public Object call(Object paramObject) {
			ConfigurazioneDistinta conf = (ConfigurazioneDistinta) paramObject;
			bd.associaConfigurazioneDistintaARigaOrdine(ro, conf);
			return null;
		}
	}

	private IOrdiniDocumentoBD bd;

	/**
	 * Costruttore.
	 */
	public GestioneConfigurazioneArticoloCommand() {
		super("gestioneConfigurazioneArticolo");
		bd = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		RisultatoMrpFlat risultatoMRP = (RisultatoMrpFlat) getParameter(PARAM_LOADER_CONTEXT_OBJECT);
		RigaArticolo ro = new RigaArticolo();
		ro.setId(risultatoMRP.getIdRigaOrdine());
		ro = (RigaArticolo) bd.caricaRigaOrdine(ro);
		// ricarico la riga ordine perchè potrebbe avermi cambiato la configurazione
		ro = (RigaArticolo) bd.caricaRigaOrdine(ro);
		Integer idConfigurazione = PanjeaEJBUtil.getLazyId(ro.getConfigurazioneDistinta());
		DistintaConfigurazioneDialog dialog = null;
		dialog = new DistintaConfigurazioneDialog(ro.getArticolo(), idConfigurazione, new AssociaConfigurazioneClosure(
				ro));
		dialog.showDialog();
		//
		//
		//
		// Integer idConfigurazione = PanjeaEJBUtil.getLazyId(ro.getConfigurazioneDistinta());
		// DistintaConfigurazioneDialog dialog = null;
		// if (idConfigurazione == null) {
		// dialog = new DistintaConfigurazioneDialog(risultatoMRP.getDistinta(), new AssociaConfigurazioneClosure(ro),
		// true);
		// } else {
		// dialog = new DistintaConfigurazioneDialog(idConfigurazione, new AssociaConfigurazioneClosure(ro));
		// }
		// dialog.showDialog();
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { ConfigurazioneDistinta.class };
	}

}
