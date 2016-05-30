package it.eurotn.panjea.anagrafica.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.MailDTO;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaMail;

@Local
public interface MailManager {

    /**
     * Cancella la mail con l'id specificato. NB: le mail ora non vengono più salvate sul DB ma questo metodo viene solo
     * utilizzato durante l'importazione dalla vecchia gestione per eseguire la cancellazione su una diversa
     * transazione.
     *
     * @param id
     *            id mail
     */
    void cancellaMail(Integer id);

    /**
     *
     * @param parametriRicercaMail
     *            i parametri ricerca mail con periodo ed entità
     * @return lista di mail spedite nel periodo. <br/>
     *         <B>NB.Gli allegati non sono avvalorati. Utilizzare il metodo caricaAllegato(mail,estensione)</B>
     */
    List<MailDTO> caricaMails(ParametriRicercaMail parametriRicercaMail);

    /**
     * salva la mail con i suoi allegati se presenti.
     *
     * @param emlFile
     *            file eml
     * @param entita
     *            entità di riferimento
     * @param inviata
     *            indica se l'invio della mail è riuscito senza errori
     */
    void salvaMail(byte[] emlFile, EntitaLite entita, boolean inviata);

    /**
     * salva la mail con i suoi allegati se presenti.
     *
     * @param emlFile
     *            file eml
     * @param entita
     *            entità di riferimento
     * @param inviata
     *            indica se l'invio della mail è riuscito senza errori
     * @param utente
     *            utente
     */
    void salvaMail(byte[] emlFile, EntitaLite entita, boolean inviata, String utente);
}
