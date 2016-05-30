package it.eurotn.panjea.magazzino.manager.documento.totalizzatore;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.magazzino.domain.TotaliArea;
import it.eurotn.panjea.magazzino.util.queryExecutor.ITotalizzatoriQueryExecutor;

import java.util.List;

import javax.ejb.Local;

/**
 * Interfaccia per definire le implementazioni che prevedono la totalizzazione di un documento di magazzino.
 * 
 * @author Leonardo
 */
@Local
public interface Totalizzatore {

	/**
	 * Totalizza il documento<br/>
	 * . Calcola il totale del documento e i vari totalizzatori (spese, spese varie etc...).<br/>
	 * Non salva il documento lo totalizza solamente
	 * 
	 * @param documento
	 *            documento da totalizzare
	 * @param totaliArea
	 *            {@link TotaliArea}
	 * @param executor
	 *            {@link ITotalizzatoriQueryExecutor}
	 * @param righeIva
	 *            righeIva per il documento
	 * @return {@link Documento} totalizzato
	 */
	Documento totalizzaDocumento(Documento documento, TotaliArea totaliArea, ITotalizzatoriQueryExecutor executor,
			List<RigaIva> righeIva);

}
