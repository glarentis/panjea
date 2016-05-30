package it.eurotn.panjea.intra.manager.interfaces;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.domain.TotaliDichiarazione;

import java.util.List;

import javax.ejb.Local;

@Local
public interface DichiarazioneIntraManager {

	/**
	 * 
	 * @param id
	 *            id della dichiarazione
	 * @return totali della dichiarazione
	 */
	TotaliDichiarazione calcolaTotaliDichiarazione(Integer id);

	/**
	 * Cancella una dichiarazione intra.
	 * 
	 * @param dichiarazioneIntra
	 *            dichiarazione da cancellare.
	 */
	void cancellaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra);

	/**
	 * 
	 * @param rigaSezioneIntra
	 *            riga da cancellare
	 */
	void cancellaRigaSezioneDichiarazione(RigaSezioneIntra rigaSezioneIntra);

	/**
	 * 
	 * @param id
	 *            id della dichiarazione
	 * @return dichiarazione caricata
	 */
	DichiarazioneIntra caricaDichiarazioneIntra(int id);

	/**
	 * 
	 * @return lista di dichiarazioniIntra
	 */
	List<DichiarazioneIntra> caricaDichiarazioniIntra();

	/**
	 * 
	 * @return lista di dichiarazioniIntra da presentare
	 */
	List<DichiarazioneIntra> caricaDichiarazioniIntraDaPresentare();

	/**
	 * Carica le righe della dichiarazione intra per la sezione.
	 * 
	 * @param <T>
	 * 
	 * @param dichiarazioneIntra
	 *            dichiarazione
	 * @param classeSezione
	 *            sezione della dichiarazione
	 * @return righe della sezione per la dichiarazione
	 * @param <T>
	 *            tipo
	 */
	<T extends RigaSezioneIntra> List<T> caricaRigheSezioniDichiarazione(DichiarazioneIntra dichiarazioneIntra,
			Class<T> classeSezione);

	/**
	 * Da una dichiarazione intra genera tutti i dati (righe e riepiloghi). Se la dichiarazione utilizzata come
	 * parametro ha delle righe queste verranno cancellate
	 * 
	 * @param dichiarazioneIntra
	 *            testata della dichiarazione da compilare
	 * @return dichiarazione con tutti i dati generati (frontespizio e righe).
	 */
	DichiarazioneIntra compilaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra);

	/**
	 * Crea una dichiarazione intra in base all'ultima dichiarazione creata.
	 * 
	 * @param tipodDichiarazione
	 *            tiplogia della dichiarazione da creare.
	 * 
	 * @return nuova dichiarazione con i dati inizializzati in base all'ultima dichiarazione
	 */
	DichiarazioneIntra creaDichiarazioneIntra(TipoDichiarazione tipodDichiarazione);

	/**
	 * Salva una dichiarazione intra.
	 * 
	 * @param dichiarazioneIntra
	 *            dichiarazione da salvare
	 * @return dichiarazione salvata.
	 */
	DichiarazioneIntra salvaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra);

	/**
	 * @param riga
	 *            riga da salvare
	 * @return riga salvata.
	 */
	RigaSezioneIntra salvaRigaSezioneDichiarazione(RigaSezioneIntra riga);
}
