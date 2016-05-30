package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.TipoDocumentoNonConformeException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.contabilita.util.LiquidazioneIvaDTO;
import it.eurotn.panjea.contabilita.util.RegistroLiquidazioneDTO;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoNonTrovatoException;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface LiquidazioneIvaManager {
	/**
	 * Calcola la liquidazione Iva per le variabili la data della liquidazione viene calcolata in base alla data del
	 * documento dell'area contabile e del periodo scelto nei settings contabilita'<BR>
	 * <B>NB.</B> il documento di liquidazione deve avere la data di registrazione = ultimo giorno<br>
	 * del trimenstre. Per questo faccio un -2 senza controllare altro
	 * 
	 * @param dataRegistrazione
	 *            dataRegistrazione
	 * @return LiquidazioneIvaDTO
	 */
	LiquidazioneIvaDTO calcolaLiquidazione(Date dataRegistrazione);

	/**
	 * Carica la liquidazione iva.
	 * 
	 * @param dataInizioPeriodo
	 *            dataInizioPeriodo
	 * @param dataFinePeriodo
	 *            dataFinePeriodo
	 * @return LiquidazioneIvaDTO
	 */
	LiquidazioneIvaDTO calcolaLiquidazione(Date dataInizioPeriodo, Date dataFinePeriodo);

	/**
	 * Carica il giornale iva definitivo per il mese e anno specificato.
	 * 
	 * @param anno
	 *            anno di riferimento
	 * @param mese
	 *            mese di riferimento
	 * @return giornale caricato
	 * @throws ContabilitaException
	 *             eccezione generica
	 */
	GiornaleIva caricaGiornaleIvaDefinitivo(int anno, int mese) throws ContabilitaException;

	/**
	 * Carica i registri liquidazione.
	 * 
	 * @param anno
	 *            anno
	 * @return List<RegistroLiquidazioneDTO>
	 * @throws TipoDocumentoBaseException
	 *             se non sono impostati i tipi documento base per la contabilita'.
	 */
	List<RegistroLiquidazioneDTO> caricaRegistriLiquidazione(Integer anno) throws TipoDocumentoBaseException;

	/**
	 * Verifica se il documento è un documento di liquidazione.
	 * 
	 * @param documento
	 *            il documento da verificare
	 * @return true or false
	 */
	boolean isDocumentoLiquidazione(Documento documento);

	/**
	 * Salva il documento di liquidazione.
	 * 
	 * @param areaContabile
	 *            areaContabile
	 * @return AreaContabileFullDTO
	 * @throws AreaContabileDuplicateException
	 *             area contabile duplicata secondo le regole di dominio
	 * @throws DocumentoDuplicateException
	 *             documento duplicato secondo le regole di dominio
	 * @throws FormulaException
	 *             errore nella formula
	 * @throws CodicePagamentoNonTrovatoException
	 *             codice pagamento assente
	 * @throws ContoRapportoBancarioAssenteException
	 *             conto su rapporto bancario assente
	 * @throws TipoDocumentoNonConformeException
	 *             errore nella configurazione del tipo documento
	 */
	AreaContabileFullDTO salvaDocumentoLiquidazione(AreaContabile areaContabile)
			throws AreaContabileDuplicateException, DocumentoDuplicateException, FormulaException,
			CodicePagamentoNonTrovatoException, ContoRapportoBancarioAssenteException,
			TipoDocumentoNonConformeException;
}
