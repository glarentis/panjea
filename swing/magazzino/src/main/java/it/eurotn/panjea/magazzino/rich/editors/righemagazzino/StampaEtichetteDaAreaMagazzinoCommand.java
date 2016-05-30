package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.etichetta.EtichettaArticolo;
import it.eurotn.panjea.magazzino.domain.etichetta.ParametriStampaEtichetteArticolo;
import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;

import java.util.List;

import javax.swing.AbstractButton;

import org.springframework.core.ReflectiveVisitorHelper;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.util.Assert;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class StampaEtichetteDaAreaMagazzinoCommand extends ApplicationWindowAwareCommand {

	public class EtichettaArticoloVisitor {

		/**
		 * Crea una etichetta articolo da una riga articolo.
		 * 
		 * @param rigaArticolo
		 *            riga articolo
		 */
		public void visit(RigaArticolo rigaArticolo) {
			EtichettaArticolo etichettaArticolo = new EtichettaArticolo();
			etichettaArticolo.setArticolo(rigaArticolo.getArticolo());
			etichettaArticolo.setNumeroCopiePerStampa(rigaArticolo.getQta().intValue());
			etichettaArticolo.setNumeroDecimali(rigaArticolo.getNumeroDecimaliPrezzo());
			etichettaArticolo.setQuantita(rigaArticolo.getQta());
			etichettaArticolo.setDescrizione(rigaArticolo.getDescrizione());
			etichettaArticolo.setSedeEntita(areaMagazzino.getDocumento().getSedeEntita());
			etichettaArticolo.setPercApplicazioneCodiceIva(rigaArticolo.getCodiceIva().getPercApplicazione());

			parametriEtichette.getEtichetteArticolo().add(etichettaArticolo);
		}

		/**
		 * Crea una etichetta articolo da una riga articolo dto.
		 * 
		 * @param rigaArticoloDTO
		 *            riga articolo dto
		 */
		public void visit(RigaArticoloDTO rigaArticoloDTO) {
			EtichettaArticolo etichettaArticolo = new EtichettaArticolo();
			etichettaArticolo.setArticolo(rigaArticoloDTO.getArticolo());
			etichettaArticolo.setNumeroCopiePerStampa(rigaArticoloDTO.getQta().intValue());
			etichettaArticolo.setNumeroDecimali(rigaArticoloDTO.getNumeroDecimaliPrezzo());
			etichettaArticolo.setQuantita(rigaArticoloDTO.getQta());
			etichettaArticolo.setDescrizione(rigaArticoloDTO.getDescrizione());
			etichettaArticolo.setSedeEntita(areaMagazzino.getDocumento().getSedeEntita());
			etichettaArticolo.setPercApplicazioneCodiceIva(rigaArticoloDTO.getCodiceIva().getPercApplicazione());

			parametriEtichette.getEtichetteArticolo().add(etichettaArticolo);
		}

		/**
		 * Crea una etichetta articolo da una riga lotto.
		 * 
		 * @param rigaLotto
		 *            riga lotto
		 */
		public void visit(RigaLotto rigaLotto) {
			EtichettaArticolo etichettaArticolo = new EtichettaArticolo();
			etichettaArticolo.setArticolo(rigaLotto.getRigaArticolo().getArticolo());
			etichettaArticolo.setNumeroCopiePerStampa(1);
			etichettaArticolo.setNumeroDecimali(rigaLotto.getRigaArticolo().getNumeroDecimaliPrezzo());
			etichettaArticolo.setLotto(rigaLotto.getLotto());
			etichettaArticolo.setDataDocumento(rigaLotto.getRigaArticolo().getAreaMagazzino().getDocumento()
					.getDataDocumento());
			etichettaArticolo.setQuantita(rigaLotto.getQuantita());
			etichettaArticolo.setDescrizione(rigaLotto.getRigaArticolo().getDescrizione());
			etichettaArticolo.setSedeEntita(areaMagazzino.getDocumento().getSedeEntita());
			etichettaArticolo.setPercApplicazioneCodiceIva(rigaLotto.getRigaArticolo().getCodiceIva()
					.getPercApplicazione());

			parametriEtichette.setGestioneLotti(Boolean.TRUE);
			parametriEtichette.getEtichetteArticolo().add(etichettaArticolo);
		}
	}

	public static final String PARAM_LIST_RIGHE_MAGAZZINO = "paramListRigheMagazzino";
	public static final String PARAM_AREA_MAGAZZINO = "paramAreaMagazzino";

	public static final String COMMAND_ID = "stampaEtichetteDaAreaMagazzinoCommand";

	private ReflectiveVisitorHelper visitorHelper;
	private EtichettaArticoloVisitor rowVisitor;

	private ParametriStampaEtichetteArticolo parametriEtichette;

	private AreaMagazzino areaMagazzino;

	/**
	 * Costruttore.
	 */
	public StampaEtichetteDaAreaMagazzinoCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);

		this.visitorHelper = new ReflectiveVisitorHelper();
		this.rowVisitor = new EtichettaArticoloVisitor();
	}

	/**
	 * Costruttore.
	 * 
	 * @param commandId
	 *            id del comando
	 */
	public StampaEtichetteDaAreaMagazzinoCommand(final String commandId) {
		super(commandId);
		RcpSupport.configure(this);

		this.visitorHelper = new ReflectiveVisitorHelper();
		this.rowVisitor = new EtichettaArticoloVisitor();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doExecuteCommand() {

		areaMagazzino = (AreaMagazzino) getParameter(PARAM_AREA_MAGAZZINO, null);
		Assert.notNull(this.areaMagazzino, "Area magazzino nulla");

		List<Object> righe = (List<Object>) getParameter(PARAM_LIST_RIGHE_MAGAZZINO, java.util.Collections.EMPTY_LIST);

		if (!righe.isEmpty()) {
			parametriEtichette = new ParametriStampaEtichetteArticolo();
			for (Object riga : righe) {
				visitorHelper.invokeVisit(rowVisitor, riga);
			}

			LifecycleApplicationEvent event = new OpenEditorEvent(parametriEtichette);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	@Override
	protected void onButtonAttached(AbstractButton button) {
		super.onButtonAttached(button);
		button.setName(COMMAND_ID);
	}

	@Override
	public void setEnabled(boolean enabled) {
		// rimane sempre abilitato
		super.setEnabled(true);
	}

}
