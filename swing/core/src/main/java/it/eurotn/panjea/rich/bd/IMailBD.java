package it.eurotn.panjea.rich.bd;

import java.util.List;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.MailDTO;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaMail;

public interface IMailBD {

    /**
     * Carica la lista di Mail salvate.
     *
     * @param parametriRicercaMail
     *            i parametri con il periodo scelto e l'entità
     * @return lista di mail spedite nel periodo. <br/>
     *         <B>NB.Gli allegati non sono avvalorati. Utilizzare il metodo caricaAllegato(mail,estensione)</B>
     */
    List<MailDTO> caricaMails(ParametriRicercaMail parametriRicercaMail);

    /**
     * Restituisce il numero delle mail che possono ancora essere importate.
     *
     * @return numero mail
     */
    Integer caricaNumeroMailDaImportare();

    /**
     * Importa tutte le mail della vecchia gestione nel dms.
     */
    void importaMail();

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

}
