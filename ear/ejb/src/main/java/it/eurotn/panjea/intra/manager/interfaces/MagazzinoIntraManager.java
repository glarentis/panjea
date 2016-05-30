/**
 *
 */
package it.eurotn.panjea.intra.manager.interfaces;

import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.GruppoCondizioneConsegna;
import it.eurotn.panjea.intra.domain.NaturaTransazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import java.util.List;

import javax.ejb.Local;

/**
 * @author leonardo
 */
@Local
public interface MagazzinoIntraManager {

	/**
	 * Carica la lista di {@link GruppoCondizioneConsegna}.
	 * 
	 * @return List<GruppoCondizioneConsegna>
	 */
	List<GruppoCondizioneConsegna> caricaGruppiCondizioneConsegna();

	/**
	 * Carica il {@link GruppoCondizioneConsegna} associato al tipo porto richiesto.
	 * 
	 * @param tipoPorto
	 *            il tipo porto di cui trovare il gruppo
	 * @return GruppoCondizioneConsegna
	 */
	GruppoCondizioneConsegna caricaGruppoCondizioneConsegna(String tipoPorto);

	/**
	 * Carica la {@link NaturaTransazione} associata alla causale di trasporto richiesta.
	 * 
	 * @param causaleTrasporto
	 *            la causale di cui trovare la natura transazione
	 * @return NaturaTransazione
	 */
	NaturaTransazione caricaNaturaTransazione(String causaleTrasporto);

	/**
	 * Carica la lista di {@link NaturaTransazione}.
	 * 
	 * @return List<NaturaTransazione>
	 */
	List<NaturaTransazione> caricaNatureTransazione();

	/**
	 * Genera l'area intra e le relative righe dall'area magazzino.<br>
	 * Le righe articolo vengono raggruppate per nomenclatura e quindi per ogni nomenclatura viene generata una riga
	 * intra.
	 * 
	 * @param areaMagazzino
	 *            l'area magazzino da cui generare l'area intra
	 * @return AreaIntra
	 */
	AreaIntra generaAreaIntra(AreaMagazzino areaMagazzino);

	/**
	 * genera le aree intra per i documenti.<br/>
	 * <b>NB</b>Considera solamente i documenti con areamagazzino
	 * 
	 * @param documenti
	 *            documenti per i quali generare le aree intra
	 */
	void generaAreeIntra(List<Integer> documenti);

}
