package it.eurotn.panjea.tesoreria.rich.commands;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti.EStatoAcconto;
import it.eurotn.rich.dialog.MessageAlert;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class OpenAreaChiusuraPagamentoCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "openAreaChiusuraPagamentoCommand";

	private Pagamento pagamento;
	private ITesoreriaBD tesoreriaBD;

	/**
	 * Costruttore.
	 */
	public OpenAreaChiusuraPagamentoCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		if (pagamento != null) {
			if (!pagamento.isLiquidazione()) {
				AreaTesoreria areaTesoreria = getTesoreriaBD().caricaAreaTesoreria(pagamento);

				Object eventEditorObject = null;
				if (areaTesoreria instanceof AreaAcconto) {
					ParametriRicercaAcconti parametriRicercaAcconti = new ParametriRicercaAcconti();
					parametriRicercaAcconti.setDaDataDocumento(pagamento.getDataPagamento());
					parametriRicercaAcconti.setADataDocumento(pagamento.getDataPagamento());
					parametriRicercaAcconti.setTipoEntita(TipoEntita.AZIENDA);
					parametriRicercaAcconti.setStatoAcconto(EStatoAcconto.CHIUSO);
					parametriRicercaAcconti.setEffettuaRicerca(true);

					eventEditorObject = parametriRicercaAcconti;
				} else {
					eventEditorObject = ParametriRicercaAreeTesoreria.creaParametriRicercaAreeTesoreria(areaTesoreria);
				}
				LifecycleApplicationEvent event = new OpenEditorEvent(eventEditorObject);
				Application.instance().getApplicationContext().publishEvent(event);
			} else {
				new MessageAlert(new DefaultMessage(RcpSupport.getMessage("pagamento.liquidazione.message")))
						.showAlert();
			}
		}
	}

	/**
	 * @return the tesoreriaBD
	 */
	public ITesoreriaBD getTesoreriaBD() {
		if (tesoreriaBD == null) {
			tesoreriaBD = RcpSupport.getBean("tesoreriaBD");
		}

		return tesoreriaBD;
	}

	/**
	 * @param pagamento
	 *            the pagamento to set
	 */
	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}
}
