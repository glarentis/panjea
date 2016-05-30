package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

import java.util.ArrayList;
import java.util.Collection;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;

public abstract class ProvenienzaPrezzoQueryBuilder {

    /**
     *
     * @param parametri
     *            parametri per la creazione del listino
     * @return Builder per costruire l'sql per assegnare il prezzo alla riga
     */
    public static ProvenienzaPrezzoQueryBuilder getInstance(ParametriRicercaManutenzioneListino parametri) {
        switch (parametri.getProvenienzaPrezzoManutenzioneListino()) {
        case LISTINO:
            return new ProvenienzaPrezzoListinoQueryBuilder();
        case ULTIMO_COSTO_AZIENDA:
            return new ProvenienzaPrezzoUltimoCostoAziendaQueryBuilder();
        case FILE_ESTERNO:
            return new ProvenienzaPrezzoFileEsternoQueryBuilder();
        default:
            return new ProvenienzaPrezzoValorizzazioneDepositoQueryBuilder();
        }
    }

    /**
     * @param parametri
     *            parametri selezionati per la creazioen del listino.
     * @return Ritorna l'sql per inserire il prezzoOriginale in base alla provenienza selezionata.
     */
    public abstract String getPrezzoSql(ParametriRicercaManutenzioneListino parametri);

    /**
     *
     * @param parametri
     *            parametri di manutenzione
     * @return Ritorna una serie di sql per aggiornare il prezzo in base alla provenienza
     *         selezionata.
     */
    public Collection<String> getSql(ParametriRicercaManutenzioneListino parametri) {
        Collection<String> result = new ArrayList<>();
        result.add(getPrezzoSql(parametri));
        // Aggiungo la query che calcola il prezzo variato
        StringBuilder sb = new StringBuilder("update maga_riga_manutenzione_listino ");
        switch (parametri.getTipoVariazione()) {
        case IMPORTO:
            sb.append(" set valore=valoreOriginale+");
            sb.append(parametri.getVariazione().toString());
            break;
        case PERCENTUALE:
            sb.append(" set valore=valoreOriginale+(valoreOriginale * (");
            sb.append(parametri.getVariazione().toString());
            sb.append("/100))");
            break;
        default:
            throw new UnsupportedOperationException("Tipo variazione non valida.");
        }

        sb.append(" where maga_riga_manutenzione_listino.numero=:numeroInserimento");
        sb.append(" and userManutenzione=:userManutenzione ");
        result.add(sb.toString());
        return result;
    }

}
