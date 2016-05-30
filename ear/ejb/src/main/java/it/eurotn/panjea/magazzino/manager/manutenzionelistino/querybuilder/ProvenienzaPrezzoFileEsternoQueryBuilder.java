package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;

public class ProvenienzaPrezzoFileEsternoQueryBuilder extends ProvenienzaPrezzoQueryBuilder {

    @Override
    public String getPrezzoSql(ParametriRicercaManutenzioneListino parametri) {
        StringBuffer sb = new StringBuffer();
        sb.append("update maga_riga_manutenzione_listino ");
        sb.append("inner join maga_articoli art on art.id=maga_riga_manutenzione_listino.articolo_id ");
        sb.append("inner join ");
        sb.append(parametri.getTableNameFileEsterno());
        sb.append(" man on man.COD_ART=art.codice ");
        sb.append("set maga_riga_manutenzione_listino.valoreOriginale=man.prezzo");
        return sb.toString();
    }

}
