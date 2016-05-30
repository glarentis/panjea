package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.verificaprezzo.ParametriCalcoloPrezziPM;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

import java.util.Date;

import javax.swing.AbstractButton;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class OpenDescrizioneCalcoloPrezzoCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "openDescrizioneCalcoloPrezzoCommand";
	public static final String ARTICOLO_PARAM = "articolo";
	public static final String LISTINO_PARAM = "listino";
	public static final String LISTINO_ALTERNATIVO_PARAM = "listinoAlternativo";
	public static final String DATA_PARAM = "data";
	public static final String ENTITA_PARAM = "entita";
	public static final String SEDE_ENTITA = "sedeEntita";
	public static final String CODICE_VALUTA_PARAM = "codiceValuta";
	public static final String AGENTE_PARAM = "agente";
	public static final String CODICE_PAGAMENTO_PARAM = "codicePagamento";
	public static final String MEZZO_TRASPORTO_PARAM = "mezzoTrasporto";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	/**
	 * Costruttore di default.
	 */
	public OpenDescrizioneCalcoloPrezzoCommand() {
		super(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);

		this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
	}

	@Override
	public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer buttonConfigurer) {
		AbstractButton button = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
		button.setFocusable(false);
		return button;
	}

	@Override
	protected void doExecuteCommand() {

		ParametriCalcoloPrezziPM parametriCalcoloPrezziPM = new ParametriCalcoloPrezziPM();
		ArticoloLite articolo = (ArticoloLite) getParameter(ARTICOLO_PARAM);
		Listino listino = (Listino) getParameter(LISTINO_PARAM);
		Listino listinoAlternativo = (Listino) getParameter(LISTINO_ALTERNATIVO_PARAM);
		Date data = (Date) getParameter(DATA_PARAM);
		EntitaLite entita = (EntitaLite) getParameter(ENTITA_PARAM);
		SedeEntita sede = (SedeEntita) getParameter(SEDE_ENTITA);
		String codiceValuta = (String) getParameter(CODICE_VALUTA_PARAM);
		AgenteLite agente = (AgenteLite) getParameter(AGENTE_PARAM);
		CodicePagamento codicePagamento = (CodicePagamento) getParameter(CODICE_PAGAMENTO_PARAM);
		MezzoTrasporto mezzoTrasporto = (MezzoTrasporto) getParameter(MEZZO_TRASPORTO_PARAM);

		if (articolo == null) {
			return;
		}

		parametriCalcoloPrezziPM.setArticolo(articolo);
		parametriCalcoloPrezziPM.setData(data);
		if (entita != null) {
			parametriCalcoloPrezziPM.setEntita(entita);
			parametriCalcoloPrezziPM.setSedeEntita(sede);
			SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO = magazzinoDocumentoBD.caricaSedeAreaMagazzinoDTO(sede);
			parametriCalcoloPrezziPM.setIdZonaGeografica(sedeAreaMagazzinoDTO.getIdZonaGeografica());
		}

		if (agente != null) {
			parametriCalcoloPrezziPM.setAgente(agente);
		}

		if (mezzoTrasporto != null) {
			parametriCalcoloPrezziPM.setTipoMezzoTrasporto(mezzoTrasporto.getTipoMezzoTrasporto());
		}

		parametriCalcoloPrezziPM.setListino(listino);

		if (listinoAlternativo != null) {
			parametriCalcoloPrezziPM.setListinoAlternativo(listinoAlternativo);
		}

		if (codicePagamento != null) {
			parametriCalcoloPrezziPM.setCodicePagamento(codicePagamento);
		}

		parametriCalcoloPrezziPM.setProvenienzaPrezzo(ProvenienzaPrezzo.LISTINO);
		parametriCalcoloPrezziPM.setCodiceValuta(codiceValuta);
		parametriCalcoloPrezziPM.setEffettuaRicerca(true);

		LifecycleApplicationEvent event = new OpenEditorEvent(parametriCalcoloPrezziPM);
		Application.instance().getApplicationContext().publishEvent(event);
	}
}