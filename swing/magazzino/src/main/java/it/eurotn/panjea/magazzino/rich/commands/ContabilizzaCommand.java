package it.eurotn.panjea.magazzino.rich.commands;

import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.rich.dialog.MessageAlert;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractButton;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class ContabilizzaCommand extends ActionCommand {
	private static final String COMMAND_ID = "contabilizzaCommand";
	private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD;
	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private AreaMagazzinoFullDTO areaMagazzinoFullDTO;

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id della pagina.
	 */
	public ContabilizzaCommand(final String pageId) {
		super(COMMAND_ID);
		setSecurityControllerId(pageId + ".controller");
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);
		String messaggio = messageSource.getMessage("contabilizzaAsyncTask.run", null, Locale.getDefault());
		Message message = new DefaultMessage(messaggio, Severity.INFO);
		MessageAlert messageAlert = new MessageAlert(message, 0);
		messageAlert.showAlert();
		List<Integer> idAree = Arrays.asList(areaMagazzinoFullDTO.getId());
		try {
			magazzinoContabilizzazioneBD.contabilizzaAreeMagazzino(idAree, true);
			// La contabilizzazione mi modifica anche l'area magazzino, ricafico il fullDTO
			areaMagazzinoFullDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzinoFullDTO
					.getAreaMagazzino());
			updateEnabled();
		} catch (ContabilizzazioneException e) {
			LifecycleApplicationEvent event = new OpenEditorEvent(e);
			Application.instance().getApplicationContext().publishEvent(event);
		} finally {
			messageAlert.closeAlert();
		}
	}

	/**
	 * @return areaMagazznioFullDto.
	 */
	public AreaMagazzinoFullDTO getAreaMagazzinoFullDTO() {
		return areaMagazzinoFullDTO;
	}

	@Override
	protected void onButtonAttached(AbstractButton button) {
		super.onButtonAttached(button);
		button.setName(getId());
	}

	/**
	 * 
	 * @param areaMagazzinoFullDTO
	 *            setter AmreaagazzinoFullDTO
	 */
	public void setAreaMagazzinoFullDTO(AreaMagazzinoFullDTO areaMagazzinoFullDTO) {
		this.areaMagazzinoFullDTO = areaMagazzinoFullDTO;
		updateEnabled();
	}

	/**
	 * 
	 * @param magazzinoContabilizzazioneBD
	 *            setter for magazzinoContabilizzazioneBD
	 */
	public void setMagazzinoContabilizzazioneBD(IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD) {
		this.magazzinoContabilizzazioneBD = magazzinoContabilizzazioneBD;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            The magazzinoDocumentoBD to set.
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	/**
	 * Aggiorna lo stato enabled del command.
	 */
	public void updateEnabled() {
		boolean enabled = false;

		if (areaMagazzinoFullDTO != null && areaMagazzinoFullDTO.getId() != null) {

			// stato confermato o forzato
			boolean statoPerContabilizzazioneOk = areaMagazzinoFullDTO.getAreaMagazzino().getStatoAreaMagazzino() == StatoAreaMagazzino.CONFERMATO
					|| areaMagazzinoFullDTO.getAreaMagazzino().getStatoAreaMagazzino() == StatoAreaMagazzino.FORZATO;

			// classe tipo documento fattura
			boolean isFattura = areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino().getTipoDocumento()
					.getClasseTipoDocumentoInstance() instanceof ClasseFattura;

			// area contabile presente, nel qual caso devo disabilitare
			boolean hasAreaContabile = areaMagazzinoFullDTO.getAreaContabileLite() != null
					&& areaMagazzinoFullDTO.getAreaContabileLite().getId() != null;

			enabled = areaMagazzinoFullDTO.isAreaContabileEnabled() && !hasAreaContabile && statoPerContabilizzazioneOk
					&& isFattura;
		}
		setEnabled(enabled);
	}
}