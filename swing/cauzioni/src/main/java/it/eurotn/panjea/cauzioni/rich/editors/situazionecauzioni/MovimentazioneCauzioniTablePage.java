package it.eurotn.panjea.cauzioni.rich.editors.situazionecauzioni;

import it.eurotn.panjea.cauzioni.rich.bd.ICauzioniBD;
import it.eurotn.panjea.cauzioni.rich.editors.entita.situazionecauzioni.MovimentazioneCauzioniTableModel;
import it.eurotn.panjea.cauzioni.util.parametriricerca.MovimentazioneCauzioneDTO;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class MovimentazioneCauzioniTablePage extends AbstractTablePageEditor<MovimentazioneCauzioneDTO> {

	private class OpenAreaMagazzinoEditor extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "openAreaMagazzinoCommand";

		private IMagazzinoDocumentoBD magazzinoDocumentoBD;

		/**
		 * Costruttore.
		 */
		public OpenAreaMagazzinoEditor() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
			this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
		}

		@Override
		protected void doExecuteCommand() {
			MovimentazioneCauzioneDTO movimentazioneCauzioneDTO = getTable().getSelectedObject();
			if (movimentazioneCauzioneDTO == null) {
				return;
			}
			AreaMagazzino areaMagazzino = new AreaMagazzino();
			areaMagazzino.setId(movimentazioneCauzioneDTO.getIdAreaMagazzino());

			AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);
			LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	public static final String PAGE_ID = "movimentazioneCauzioniTablePage";

	private OpenAreaMagazzinoEditor openAreaMagazzinoEditor;

	private ICauzioniBD cauzioniBD;

	private ParametriRicercaDettaglioMovimentazioneCauzioni parametri;

	/**
	 * Costruttore.
	 */
	protected MovimentazioneCauzioniTablePage() {
		super(PAGE_ID, new MovimentazioneCauzioniTableModel());
		setVisible(false);
		openAreaMagazzinoEditor = new OpenAreaMagazzinoEditor();
		getTable().setPropertyCommandExecutor(openAreaMagazzinoEditor);
	}

	@Override
	public Collection<MovimentazioneCauzioneDTO> loadTableData() {

		List<MovimentazioneCauzioneDTO> movimentazione = new ArrayList<MovimentazioneCauzioneDTO>();

		// se la pagina del dettaglio non è visualizzata non devo eseguire la ricerca
		if (!isVisible()) {
			return null;
		}

		if (parametri != null) {
			movimentazione = cauzioniBD.caricaMovimentazioneCauzioniArticolo(parametri.getIdEntita(),
					parametri.getIdSedeEntita(), parametri.getIdArticolo());
		}

		return movimentazione;

	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public Collection<MovimentazioneCauzioneDTO> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param cauzioniBD
	 *            The cauzioniBD to set.
	 */
	public void setCauzioniBD(ICauzioniBD cauzioniBD) {
		this.cauzioniBD = cauzioniBD;
	}

	@Override
	public void setFormObject(Object object) {
		parametri = null;
		if (object instanceof ParametriRicercaDettaglioMovimentazioneCauzioni) {
			parametri = (ParametriRicercaDettaglioMovimentazioneCauzioni) object;
		}
	}

}
