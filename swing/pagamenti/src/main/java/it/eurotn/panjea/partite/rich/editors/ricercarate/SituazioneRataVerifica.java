/**
 *
 */
package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.util.Date;
import java.util.List;

import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class SituazioneRataVerifica {

	private CarrelloPagamentiTablePage carrelloPagamentiTablePage;

	/**
	 * Costruttore.
	 * 
	 * @param carrelloPagamentiTablePage
	 *            carrello pagamenti
	 */
	public SituazioneRataVerifica(final CarrelloPagamentiTablePage carrelloPagamentiTablePage) {
		super();
		this.carrelloPagamentiTablePage = carrelloPagamentiTablePage;
	}

	/**
	 * Verifica che il codice valuta sia valido per la situazione rata specificata.
	 * 
	 * @param situazioneRata
	 *            situazione rata
	 * @return <code>true</code> se valida
	 */
	private boolean verificaCodiceValuta(SituazioneRata situazioneRata) {
		if (carrelloPagamentiTablePage.getCodiceValuta() != null
				&& !situazioneRata.getRata().getImporto().getCodiceValuta()
						.equals(carrelloPagamentiTablePage.getCodiceValuta())) {
			String title = RcpSupport.getMessage("valutaRataNonCoerente.ask.title");
			String message = RcpSupport.getMessage("valutaRataNonCoerente.ask.message");
			MessageDialog alert = new MessageDialog(title, message);
			alert.showDialog();
			return false;
		}

		return true;
	}

	/**
	 * Veifica se la situazione rata è valida per la compensazione.
	 * 
	 * @param situazioneRata
	 *            situazione rata
	 * @return <code>true</code> se la situazione rata è valida
	 */
	private boolean verificaPerCompensazione(SituazioneRata situazioneRata) {
		// Verifico che la rata da aggiungere abbia la stessa anagrafica
		if (carrelloPagamentiTablePage.getTable().getRows().size() > 0) {
			boolean codiceValutaValid = verificaCodiceValuta(situazioneRata);

			AnagraficaLite anagrafica = carrelloPagamentiTablePage.getTable().getRows().get(0).getEntita()
					.getAnagrafica();

			if (codiceValutaValid && anagrafica != null
					&& !anagrafica.equals(situazioneRata.getEntita().getAnagrafica())) {
				String title = RcpSupport.getMessage("anagraficaNonCoerente.ask.title");
				String message = RcpSupport.getMessage("anagraficaNonCoerente.ask.message");
				MessageDialog alert = new MessageDialog(title, message);
				alert.showDialog();
				return false;
			}
		}

		return true;
	}

	/**
	 * Verifica se la situazione rata è valida per la creazione dei pagamenti.
	 * 
	 * @param situazioneRata
	 *            situazione rata
	 * @return <code>true</code> se valida
	 */
	private boolean verificaPerPagamenti(SituazioneRata situazioneRata) {
		// Verifico che la rata da aggiungere abbia lo stesso tipo
		if (carrelloPagamentiTablePage.getTable().getRows().size() > 0) {
			boolean codiceValutaValid = verificaCodiceValuta(situazioneRata);

			TipoPartita tipoPartitaInCarrello = carrelloPagamentiTablePage.getTable().getRows().get(0).getRata()
					.getRata().getAreaRate().getTipoAreaPartita().getTipoPartita();

			if (codiceValutaValid
					&& tipoPartitaInCarrello != null
					&& tipoPartitaInCarrello != situazioneRata.getRata().getAreaRate().getTipoAreaPartita()
							.getTipoPartita()) {
				String title = RcpSupport.getMessage("tipoRataNonCoerente.ask.title");
				String message = RcpSupport.getMessage("tipoRataNonCoerente.ask.message");
				MessageDialog alert = new MessageDialog(title, message);
				alert.showDialog();
				return false;
			}
		}

		return true;
	}

	/**
	 * Nel caso in cui ho selezionato un tipo documento di tipoOperazione ANTICIPO_FATTURE, verifica che tutte le rate
	 * abbiano data scadenza uguale.
	 * 
	 * @param parametriCreazioneAreaChiusure
	 *            parametriCreazioneAreaChiusure
	 * @return true o false, mostrando un messaggio all'utente nel secondo caso
	 */
	public boolean verificaScadenzeAnticipoFatture(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) {
		List<PagamentoPM> rows = carrelloPagamentiTablePage.getTable().getRows();
		if (rows.size() > 0
				&& parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoOperazione()
						.equals(TipoOperazione.ANTICIPO_FATTURE)) {
			Date dateConfronto = rows.get(0).getDataScadenza();
			for (PagamentoPM pagamentoPM : rows) {
				if (pagamentoPM.getDataScadenza().compareTo(dateConfronto) != 0) {
					String title = RcpSupport.getMessage("scadenzeAnticipoFattureDiverse.title");
					String message = RcpSupport.getMessage("scadenzeAnticipoFattureDiverse.message");
					MessageDialog alert = new MessageDialog(title, message);
					alert.showDialog();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Verifica che la situazione rata sia valida per essere aggiunta al carrello.
	 * 
	 * @param situazioneRata
	 *            situazione rata
	 * @return <code>true</code> se valida
	 */
	public boolean verificaSituazioneRata(SituazioneRata situazioneRata) {

		boolean compensazione = carrelloPagamentiTablePage.getTableModelRicercaRate().isCompensazione();

		boolean result = Boolean.FALSE;

		if (compensazione) {
			result = verificaPerCompensazione(situazioneRata);
		} else {
			result = verificaPerPagamenti(situazioneRata);
		}

		return result;
	}

}
