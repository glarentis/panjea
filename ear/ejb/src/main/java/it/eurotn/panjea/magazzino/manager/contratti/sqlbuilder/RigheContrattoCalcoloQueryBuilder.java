package it.eurotn.panjea.magazzino.manager.contratti.sqlbuilder;

import java.util.List;

/**
 * Query builder per comporre la query che cerca le righe contratto che soddisfano la combinazione di parametri
 * ricevuta.
 *
 * @author leonardo
 */
public final class RigheContrattoCalcoloQueryBuilder {

    /**
     * Costruttore di default privato.
     */
    private RigheContrattoCalcoloQueryBuilder() {

    }

    private static String getFragmentSqlForArticolo(Integer idArticolo, Integer idCategoriaCommercialeArticolo,
            Integer idCategoriaCommercialeArticolo2) {
        StringBuilder sb = new StringBuilder();
        if (idArticolo != null) {
            sb.append(" ( riga.articolo_id=:idArticolo ");
            sb.append("  or riga.categoriaCommercialeArticolo_id=:idCategoriaCommercialeArticolo ");
            if (idCategoriaCommercialeArticolo2 != null) {
                sb.append("  or riga.categoriaCommercialeArticolo_id=:idCategoriaCommercialeArticolo2 ");
            }
            sb.append(" or riga.tutteLeCategorie=1 ) ");
            sb.append(" and (");
        }
        if (idArticolo == null && idCategoriaCommercialeArticolo != null) {
            sb.append(" ( riga.categoriaCommercialeArticolo_id=:idCategoriaCommercialeArticolo ");
            if (idCategoriaCommercialeArticolo2 != null) {
                sb.append(" or riga.categoriaCommercialeArticolo_id=:idCategoriaCommercialeArticolo2 ");
            }
            sb.append(" or riga.tutteLeCategorie=1 ) ");
            sb.append(" and riga.articolo_id is null ");
            sb.append(" and (");
        }
        if (idArticolo == null && idCategoriaCommercialeArticolo == null) {
            sb.append(" ( riga.articolo_id is null ");
            sb.append(" and riga.categoriaCommercialeArticolo_id is null ");
            if (idCategoriaCommercialeArticolo2 == null) {
                sb.append(" and riga.categoriaCommercialeArticolo_id is null ");
            }
            sb.append(") and (");
        }
        return sb.toString();
    }

    /**
     * Compone la parte di query sql relativa alla combinazione delle variabili riguardanti l'entità
     * (idEntita,idSedeMagazzino,idCategoriaSedeMagazzino).
     *
     * @param idEntita
     *            idEntita
     * @param idSedeMagazzino
     *            idSedeMagazzino
     * @param idCategoriaSedeMagazzino
     *            idCategoriaSedeMagazzino
     * @param idCategorieSedePerEntita
     *            idCategorieSedePerEntita
     * @return parte della query sql che riguarda le combinazioni con le variabili dell'entità
     */
    private static String getFragmentSqlForEntita(Integer idEntita, Integer idSedeMagazzino,
            Integer idCategoriaSedeMagazzino, List<Integer> idCategorieSedePerEntita) {
        StringBuilder sb = new StringBuilder();

        // se ci sono tutti i parametri metto tutti i tre livelli partendo dalla situazione più specifica
        // (categoria,ent,sede) a quella intermedia (categoria,ent) a quella più generale (categoria)
        if (idCategoriaSedeMagazzino != null && idEntita != null && idSedeMagazzino != null) {
            sb.append(
                    " ((contratto.tutteCategorieSedeMagazzino=true or categorieSedi.categorieSediMagazzino_id=:idCategoriaSede) or contrattiEntita.entita_id=:entitaId or sediMagazzino.sediMagazzino_id=:idSede) ");
            sb.append(" or ");
        }

        if (idCategoriaSedeMagazzino == null && idEntita != null && idSedeMagazzino != null) {
            sb.append(
                    " (contratto.tutteCategorieSedeMagazzino=false and categorieSedi.categorieSediMagazzino_id is null and sediMagazzino.sediMagazzino_id=:idSede) ");
            sb.append(
                    " or (contratto.tutteCategorieSedeMagazzino=false and categorieSedi.categorieSediMagazzino_id is null and contrattiEntita.entita_id=:entitaId and (sediMagazzino.sediMagazzino_id is null or se.entita_id <> :entitaId)) ");
            sb.append(" or ");
        }

        // livello ent: sede + entita + categoria sede
        if (idCategoriaSedeMagazzino == null && idEntita == null && idSedeMagazzino != null) {
            sb.append(
                    " (contratto.tutteCategorieSedeMagazzino=false and categorieSedi.categorieSediMagazzino_id is null and contrattiEntita.entita_id is null and sediMagazzino.sediMagazzino_id=:idSede) ");
            sb.append(
                    " or (contratto.tutteCategorieSedeMagazzino=false and categorieSedi.categorieSediMagazzino_id is null and contrattiEntita.entita_id=:entitaId and (sediMagazzino.sediMagazzino_id is null or se.entita_id <> :entitaId)) ");
            if (!idCategorieSedePerEntita.isEmpty()) {
                sb.append(
                        " or (contratto.tutteCategorieSedeMagazzino=false and categorieSedi.categorieSediMagazzino_id in (:categorieSedeEntita) and contrattiEntita.entita_id is null and sediMagazzino.sediMagazzino_id is null)");
            }
            sb.append(" or ");
        }

        // livello ent: entita + categoria sede
        if (idCategoriaSedeMagazzino == null && idEntita != null && idSedeMagazzino == null) {
            sb.append(
                    " (contratto.tutteCategorieSedeMagazzino=false and categorieSedi.categorieSediMagazzino_id is null and contrattiEntita.entita_id=:entitaId and (sediMagazzino.sediMagazzino_id is null or se.entita_id <> :entitaId)) ");
            if (!idCategorieSedePerEntita.isEmpty()) {
                sb.append(
                        " or (contratto.tutteCategorieSedeMagazzino=false and categorieSedi.categorieSediMagazzino_id in (:categorieSedeEntita) and contrattiEntita.entita_id is null and sediMagazzino.sediMagazzino_id is null)");
            }
            sb.append(" or ");
        }

        // livello ent: categoria sede
        if (idCategoriaSedeMagazzino != null && idEntita == null && idSedeMagazzino == null) {
            sb.append(
                    " (contratto.tutteCategorieSedeMagazzino=false and categorieSedi.categorieSediMagazzino_id=:idCategoriaSede and contrattiEntita.entita_id is null and sediMagazzino.sediMagazzino_id is null) ");
            sb.append(" or ");
        }

        // livello ent: tutte le categorie, va ad aggiungersi sempre
        sb.append(
                " (contratto.tutteCategorieSedeMagazzino=true and categorieSedi.categorieSediMagazzino_id is null and contrattiEntita.entita_id is null and sediMagazzino.sediMagazzino_id is null) ");
        // livello ent: nessuno, va ad aggiungersi sempre
        sb.append(
                " or (contratto.tutteCategorieSedeMagazzino=false and categorieSediMagazzino_id is null and contrattiEntita.entita_id is null and sediMagazzino.sediMagazzino_id is null) ");

        return sb.toString();
    }

    /**
     * Costruisce la query sql per caricare le righe contratto che soddisfano la combinazione dei parametri ricevuta.
     *
     * @param idArticolo
     *            idArticolo
     * @param idCategoriaCommercialeArticolo
     *            idCategoriaCommercialeArticolo
     * @param idCategoriaCommercialeArticolo2
     *            idCategoriaCommercialeArticolo2
     * @param idCategorieSedePerEntita
     *            idCategorieSedePerEntita
     * @param idCategoriaSedeMagazzino
     *            idCategoriaSedeMagazzino
     * @param idEntita
     *            idEntita
     * @param idSedeMagazzino
     *            idSedeMagazzino
     * @param codiceValuta
     *            codiceValuta
     * @param tutteLeRighe
     *            tutteLeRighe
     * @return query sql completa
     */
    public static String getSql(Integer idArticolo, Integer idCategoriaCommercialeArticolo,
            Integer idCategoriaCommercialeArticolo2, List<Integer> idCategorieSedePerEntita,
            Integer idCategoriaSedeMagazzino, Integer idEntita, Integer idSedeMagazzino, String codiceValuta,
            boolean tutteLeRighe) {

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("  contratto.tutteCategorieSedeMagazzino as tutteLeCategorieSediMagazzino, ");
        sb.append("  contratto.codice as codiceContratto, ");
        sb.append("  contratto.dataInizio as dataInizioContratto, ");
        sb.append("  contratto.dataFine as dataFineContratto, ");
        sb.append("  contratto.descrizione as descrizioneContratto, ");
        sb.append("  sediMagazzino.sediMagazzino_id as idSedeMagazzino, ");
        sb.append("  categorieSedi.categorieSediMagazzino_id as idCategoriaSede, ");
        sb.append("  riga.id as id, ");
        sb.append("  riga.articolo_id as idArticolo, ");

        // devo valorizzare la categoria commerciale 1 di rigaContrattoCalcolo se la categoria commerciale della riga
        // contratto è la categoria commerciale 1 dell'articolo che sto verificando
        if (idArticolo != null && idCategoriaCommercialeArticolo != null) {
            sb.append(
                    " if((articolo.categoriaCommercialeArticolo_id is not null and riga.categoriaCommercialeArticolo_id=:idCategoriaCommercialeArticolo),riga.categoriaCommercialeArticolo_id,null) as idCategoriaCommercialeArticolo, ");
        } else {
            sb.append("  riga.categoriaCommercialeArticolo_id as idCategoriaCommercialeArticolo, ");
        }

        // devo valorizzare la categoria commerciale 2 di rigaContrattoCalcolo se la categoria commerciale della riga
        // contratto è la categoria commerciale 2 dell'articolo che sto verificando
        if (idArticolo != null && idCategoriaCommercialeArticolo2 != null) {
            sb.append(
                    " if((articolo.categoriaCommercialeArticolo2_id is not null and riga.categoriaCommercialeArticolo_id=:idCategoriaCommercialeArticolo2),riga.categoriaCommercialeArticolo_id,null) as idCategoriaCommercialeArticolo2, ");
        } else {
            sb.append(" null as idCategoriaCommercialeArticolo2, ");
        }
        sb.append("  riga.contratto_id as idContratto, ");
        sb.append("  riga.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
        if (idArticolo != null) {
            sb.append(" articolo.numeroDecimaliPrezzo as numeroDecimaliPrezzoArticolo, ");
        }
        sb.append("  riga.azionePrezzo as azionePrezzo, ");
        sb.append("  riga.bloccoPrezzo as bloccoPrezzo, ");
        sb.append("  riga.ignoraBloccoPrezzoPrecedente as ignoraBloccoPrezzoPrecedente, ");
        sb.append("  riga.quantitaSogliaPrezzo as quantitaSogliaPrezzo, ");
        sb.append("  riga.tipoValorePrezzo as tipoValorePrezzo, ");
        sb.append("  riga.valorePrezzo as valorePrezzo, ");
        sb.append("  riga.azioneSconto as azioneSconto, ");
        sb.append("  riga.bloccoSconto as bloccoSconto, ");
        sb.append("  riga.ignoraBloccoScontoPrecedente as ignoraBloccoScontoPrecedente, ");
        sb.append("  riga.quantitaSogliaSconto as quantitaSogliaSconto, ");
        sb.append("  riga.tutteLeCategorie as tutteLeCategorieArticolo, ");

        sb.append("  riga.strategiaPrezzoAbilitata as strategiaPrezzoAbilitata, ");
        sb.append("  riga.strategiaScontoAbilitata as strategiaScontoAbilitata, ");

        sb.append("  sconti.sconto1 as sconto1, ");
        sb.append("  sconti.sconto2 as sconto2, ");
        sb.append("  sconti.sconto3 as sconto3, ");
        sb.append("  sconti.sconto4 as sconto4, ");
        sb.append("  contrattiEntita.entita_id as idEntita, ");
        sb.append("  rigaContrattoAgente.agente_id as idAgente, ");
        sb.append("  rigaContrattoAgente.valoreProvvigione as provvigineAgente, ");
        sb.append("  ifnull(rigaContrattoAgente.blocco,false) as bloccoProvvigione, ");
        sb.append("  ifnull(rigaContrattoAgente.ignoraBloccoPrecedente,false) as ignoraBloccoProvvigionePrecedente, ");
        sb.append("  rigaContrattoAgente.azione as azioneProvvigione ");
        sb.append(" from ");
        sb.append(" maga_righe_contratto riga ");
        sb.append(" left outer join maga_contratti contratto on riga.contratto_id=contratto.id ");
        if (idArticolo != null) {
            sb.append(" left outer join maga_articoli articolo on articolo.id=:idArticolo ");
        }
        sb.append(
                " left outer join maga_contratti_maga_categorie_sedi_magazzino categorieSedi  on contratto.id=categorieSedi.maga_contratti_id ");
        sb.append(
                " left outer join maga_contratti_maga_sedi_magazzino sediMagazzino on contratto.id=sediMagazzino.maga_contratti_id ");
        sb.append(" left outer join maga_sconti sconti  on sconti.id=riga.sconto_id ");
        sb.append(
                " left outer join maga_righe_contratto_agente rigaContrattoAgente on rigaContrattoAgente.rigaContratto_id = riga.id ");
        sb.append(
                " left outer join maga_contratti_anag_entita contrattiEntita on contrattiEntita.maga_contratti_id=contratto.id  ");
        sb.append("left outer join maga_sedi_magazzino sm on sm.id = sediMagazzino.sediMagazzino_id ");
        sb.append("left outer join anag_sedi_entita se on se.id = sm.sedeEntita_id ");

        if (!tutteLeRighe) {
            sb.append(" where ");

            String sqlFragmentArticolo = getFragmentSqlForArticolo(idArticolo, idCategoriaCommercialeArticolo,
                    idCategoriaCommercialeArticolo2);
            sb.append(sqlFragmentArticolo);

            String sqlFragmentEntita = getFragmentSqlForEntita(idEntita, idSedeMagazzino, idCategoriaSedeMagazzino,
                    idCategorieSedePerEntita);
            sb.append(sqlFragmentEntita);

            sb.append(" ) ");

            sb.append(" and contratto.dataInizio<=:data and contratto.dataFine>=:data ");

            if (codiceValuta != null && !codiceValuta.isEmpty()) {
                sb.append(" and contratto.codiceValuta = :codiceValuta ");
            }
        }
        sb.append(
                " order by contratto.dataInizio,riga.quantitaSogliaPrezzo,riga.quantitaSogliaSconto,contratto.id,rigaContrattoAgente.agente_id desc ");

        return sb.toString();
    }

}
