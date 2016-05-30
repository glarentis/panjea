/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.ritenuteacconto.situazione;

import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.contabilita.rich.bd.IRitenutaAccontoBD;
import it.eurotn.panjea.contabilita.util.ParametriSituazioneRitenuteAcconto;
import it.eurotn.panjea.contabilita.util.SituazioneRitenuteAccontoDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collections;
import java.util.List;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

public class RisultatiRicercaSituazioneRitenuteTablePage extends AbstractTablePageEditor<SituazioneRitenuteAccontoDTO> {

	protected ParametriSituazioneRitenuteAcconto parametriSituazioneRitenuteAcconto = null;
	public static final String PAGE_ID = "risultatiRicercaSituazioneRitenuteTablePage";

	private IRitenutaAccontoBD ritenutaAccontoBD;

	private OpenAreeDocumentoCommand openAreeDocumentoCommand;

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaSituazioneRitenuteTablePage() {
		super(PAGE_ID, new RisultatiRicercaSituazioneRitenuteTableModel());
		getTable().setPropertyCommandExecutor(getOpenAreeDocumentoCommand());
	}

	/**
	 * @return the openAreaDocumentoRataCommand
	 */
	private OpenAreeDocumentoCommand getOpenAreeDocumentoCommand() {
		if (openAreeDocumentoCommand == null) {
			openAreeDocumentoCommand = new OpenAreeDocumentoCommand();
			openAreeDocumentoCommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand arg0) {
				}

				@Override
				public boolean preExecution(ActionCommand command) {
					SituazioneRitenuteAccontoDTO situazione = getTable().getSelectedObject();
					if (situazione != null) {
						command.addParameter(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO, situazione.getIdDocumento());
						return true;
					}
					return false;
				}
			});
		}

		return openAreeDocumentoCommand;
	}

	@Override
	public List<SituazioneRitenuteAccontoDTO> loadTableData() {
		List<SituazioneRitenuteAccontoDTO> situazione = Collections.emptyList();

		if (parametriSituazioneRitenuteAcconto.isEffettuaRicerca()) {
			situazione = ritenutaAccontoBD.caricaSituazioneRitenuteAccont(parametriSituazioneRitenuteAcconto);
		}
		return situazione;
	}

	@Override
	public void onPostPageOpen() {
		// nothing to do
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	// /**
	// * Lancia l'evento per aprire l'area di magazzino selezionata.
	// */
	// private void openAreaMagazzinoEditor() {
	// logger.debug("--> Enter openAreaMagazzinoEditor");
	// AreaMagazzinoRicerca areaMagazzinoRicerca = getTable().getSelectedObject();
	// if (areaMagazzinoRicerca == null) {
	// return;
	// }
	// AreaMagazzino areaMagazzino = new AreaMagazzino();
	// areaMagazzino.setId(areaMagazzinoRicerca.getIdAreaMagazzino());
	// // areaMagazzino.setDepositoOrigine(areaMagazzinoRicerca.getDepositoOrigine());
	//
	// AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);
	// LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
	// Application.instance().getApplicationContext().publishEvent(event);
	// logger.debug("--> Exit openAreaMagazzinoEditor");
	// }

	@Override
	public List<SituazioneRitenuteAccontoDTO> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		this.parametriSituazioneRitenuteAcconto = (ParametriSituazioneRitenuteAcconto) object;
	}

	/**
	 * @param ritenutaAccontoBD
	 *            the ritenutaAccontoBD to set
	 */
	public void setRitenutaAccontoBD(IRitenutaAccontoBD ritenutaAccontoBD) {
		this.ritenutaAccontoBD = ritenutaAccontoBD;
	}
}
