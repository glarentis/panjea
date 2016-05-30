package it.eurotn.panjea.magazzino.rich.editors.etichette;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.etichetta.EtichettaArticolo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * Aggiorna la lista di etichetteArticolo con i dati presenti sul formModel passato nel costruttore.
 * 
 * @author giangi
 * 
 */
public class AggiornaDatiCommand extends ActionCommand {

	private static final String COMMAND_ID = "refreshCommand";
	public static final String PARAM_ETICHETTE_ARTICOLO = "param_etichette_articolo";

	private final IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private final FormModel parametriStampaFormModel;

	private ValutaAziendaCache valutaAziendaCache;

	/**
	 * @param magazzinoDocumentoBD
	 *            bd per calcolare il prezzo
	 * @param parametriStampaFormModel
	 *            formModel dei parametri di stampa
	 */
	public AggiornaDatiCommand(final IMagazzinoDocumentoBD magazzinoDocumentoBD,
			final FormModel parametriStampaFormModel) {
		super(COMMAND_ID, "AggiornaDatiCommandController");
		RcpSupport.configure(this);
		getFaceDescriptor().setCaption("");
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
		this.parametriStampaFormModel = parametriStampaFormModel;

		valutaAziendaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doExecuteCommand() {
		final List<EtichettaArticolo> etichette = (List<EtichettaArticolo>) getParameter(PARAM_ETICHETTE_ARTICOLO,
				Collections.emptyList());
		Date data = (Date) parametriStampaFormModel.getValueModel("data").getValue();
		Listino listino = (Listino) parametriStampaFormModel.getValueModel("listino").getValue();
		Boolean aggiungiIva = (Boolean) parametriStampaFormModel.getValueModel("aggiungiIva").getValue();

		for (EtichettaArticolo etichettaArticolo : etichette) {
			etichettaArticolo.setAggiungiIva(aggiungiIva);

			if (listino != null) {
				ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(etichettaArticolo
						.getArticolo().getId(), data, listino.getId(), null, null, null, null, null,
						ProvenienzaPrezzo.LISTINO, null, null, null, valutaAziendaCache.caricaValutaAziendaCorrente()
								.getCodiceValuta(), null, null);
				PoliticaPrezzo politicaPrezzo = magazzinoDocumentoBD.calcolaPrezzoArticolo(parametriCalcoloPrezzi);
				// recupero lo scaglione a 0;
				BigDecimal prezzoNetto = politicaPrezzo.getPrezzoNetto(0,
						etichettaArticolo.getPercApplicazioneCodiceIva());
				etichettaArticolo.setPrezzoNettoCalcolato(prezzoNetto);
				if (!aggiungiIva) {
					RisultatoPrezzo<BigDecimal> risultatoPrezzo = politicaPrezzo.getPrezzi().getRisultatoPrezzo(0.0);
					if (risultatoPrezzo != null) {
						etichettaArticolo.setNumeroDecimali(risultatoPrezzo.getNumeroDecimali());
					}
				} else {
					if (politicaPrezzo.isPrezzoIvato()) {
						prezzoNetto = politicaPrezzo.getPrezzi().getRisultatoPrezzo(0.0).getValue();
						RisultatoPrezzo<Sconto> variazione = politicaPrezzo.getSconti().getRisultatoPrezzo(0.0);
						if (variazione != null) {
							prezzoNetto = variazione.getValue().applica(prezzoNetto, 2);
						}
					} else if (etichettaArticolo.getPercApplicazioneCodiceIva() != null) {
						CodiceIva codiceIva = new CodiceIva();
						codiceIva.setPercApplicazione(etichettaArticolo.getPercApplicazioneCodiceIva());
						prezzoNetto = codiceIva.applica(prezzoNetto);
					}
					etichettaArticolo.setNumeroDecimali(2);
				}
				etichettaArticolo.setPrezzoNetto(prezzoNetto);
			}
		}
	}
}
