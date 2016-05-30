package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.DocumentoEntitaDTO;
import it.eurotn.panjea.contabilita.util.RisultatoControlloProtocolli;
import it.eurotn.panjea.contabilita.util.RisultatoControlloRateSaldoContabile;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface ControlliManager {

	/**
	 * Carica tutti i documenti delle entità che fanno parte della blacklist per il periodo indicato.
	 *
	 * @param dataIniziale
	 *            data inizio ricerca
	 * @param dataFinale
	 *            data fine ricerca
	 * @return documenti caricati
	 */
	List<DocumentoEntitaDTO> caricaRiepilogoDocumentiBlacklist(Date dataIniziale, Date dataFinale);

	/**
	 *
	 * @param anno
	 *            di partenza per la verifica
	 * @return lista con i codici dei protocolli che hanno data inferiore al protocollo che lo "segue"
	 */
	List<RisultatoControlloProtocolli> verificaDataProtocolli(Integer anno);

	/**
	 * Verifica se nei documenti contabili ci sono dei "buchi" di numerazione.
	 *
	 * @param anno
	 *            anno di partenza per la verifica
	 * @return lista con i codici mancanti. Lista vuota se non ci sono codici
	 */
	List<RisultatoControlloProtocolli> verificaProtocolliMancanti(Integer anno);

	/**
	 *
	 * @return lista di righe contabili che dovrebbero avere un centro ma non ne è assegnato nemmeno uno.
	 */
	List<AreaContabileDTO> verificaRigheSenzaCentriDiCosto();

	/**
	 * Verifica eventuali discrepanze trai saldi contabili e il residuo rate di clienti e fornitori.
	 *
	 * @param sottotipoConto
	 *            SottotipoConto del conto (cliente o fornitore) per il quale verificare i saldi.
	 * @return lista di risultati con le differenze diverse da zero.
	 */
	List<RisultatoControlloRateSaldoContabile> verificaSaldi(SottotipoConto sottotipoConto);
}
