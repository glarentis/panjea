package it.eurotn.panjea.magazzino.manager.sqlbuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ESezioneTipoMovimento;

/**
 * Costruisce le query native per la movimentazione.
 *
 * @author giangi
 */
public final class MovimentazioneQueryBuilder {

    /**
     * Classe di utility, costruttore privato.
     */
    private MovimentazioneQueryBuilder() {

    }

    /**
     * Metodo per caricare le righe di movimentazione del magazzino..
     *
     * @param articoloLite
     *            articoloLite
     * @param depositoLite
     *            depositoLite
     * @param entitaLite
     *            entitaLite
     * @param dataInizio
     *            dataInizio
     * @param dataFine
     *            dataFine
     * @param codiceAzienda
     *            codiceAzienda
     * @param useTipiAreaMagazzinoFilter
     *
     *            useTipiAreaMagazzinoFilter
     * @param sezioniTipoMovimento
     *            sezioniTipoMovimento
     * @param tipiAreaMagazzino
     *            tipi area magazzino da filtrare
     * @param noteRiga
     *            noteRiga
     * @param agenteLite
     *            agente
     * @param sedeEntita
     *            sede entita
     * @param descrizioneRiga
     *            descrizioneRiga
     * @param omaggio
     *            true per cercare le righe omaggio
     * @return String
     */
    public static String getSqlMovimentazione(ArticoloLite articoloLite, DepositoLite depositoLite,
            EntitaLite entitaLite, Date dataInizio, Date dataFine, String codiceAzienda,
            boolean useTipiAreaMagazzinoFilter, Collection<ESezioneTipoMovimento> sezioniTipoMovimento,
            Collection<TipoAreaMagazzino> tipiAreaMagazzino, String noteRiga, AgenteLite agenteLite,
            SedeEntita sedeEntita, String descrizioneRiga, boolean omaggio) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("art.id as idArticolo, ");
        sb.append("concat(art.codice) as codiceArticolo, ");
        sb.append("art.numeroDecimaliPrezzo as numeroDecimaliPrezzoArticolo, ");
        sb.append("aum.codice as um, ");
        sb.append("aum.id as umId, ");
        sb.append("concat(art.descrizioneLinguaAziendale) as descrizioneArticolo, ");
        sb.append("art.categoria_id as idCategoria, ");
        sb.append("concat(art.codiceCategoria) as codiceCategoria, ");
        sb.append("concat(art.descrizioneCategoria) as descrizioneCategoria, ");
        sb.append("dep.id as idDeposito, ");
        sb.append("concat(dep.codice) as codiceDeposito, ");
        sb.append("concat(dep.descrizione) as descrizioneDeposito, ");
        sb.append("mov.areaMagazzino_id as areaMagazzinoId, ");
        sb.append("mov.dataRegistrazione, ");
        sb.append("mov.dataDocumento, ");
        sb.append("mov.numeroDocumento, ");
        sb.append("mov.numeroDocumentoOrder, ");
        sb.append("mov.tipoDocumentoId as idTipoDocumento, ");
        sb.append("concat(mov.tipoDocumentoCodice) as codiceTipoDocumento, ");
        sb.append("concat(mov.tipoDocumentoDescrizione) as descrizioneTipoDocumento, ");
        sb.append("sedi.entita_id as idEntita, ");
        sb.append("sedi.codice as codiceEntita, ");
        sb.append("concat(sedi.denominazione) as descrizioneEntita, ");
        sb.append("concat(sedi.TIPO_ANAGRAFICA) as tipoEntita, ");
        sb.append("magaRighe.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
        sb.append("magaRighe.numeroDecimaliQta as numeroDecimaliQuantita, ");
        sb.append("magaRighe.importoInValutaAzienda as prezzoUnitario, ");
        sb.append("magaRighe.importoInValutaAziendaNetto as prezzoNetto, ");
        sb.append("magaRighe.importoInValutaAziendaTotale as PrezzoTotale, ");
        sb.append("magaRighe.variazione1 as variazione1, ");
        sb.append("magaRighe.variazione2 as variazione2, ");
        sb.append("magaRighe.variazione3 as variazione3, ");
        sb.append("magaRighe.variazione4 as variazione4, ");
        sb.append("qtaMagazzinoCarico+qtaMagazzinoCaricoAltro as qtaMagazzinoCaricoTotale, ");
        sb.append("qtaMagazzinoScarico+qtaMagazzinoScaricoAltro as qtaMagazzinoScaricoTotale, ");
        sb.append(
                "((qtaMagazzinoCarico+qtaMagazzinoCaricoAltro)-(qtaMagazzinoScarico+qtaMagazzinoScaricoAltro)) as qtaMovimentata, ");
        sb.append("importoCarico+importoCaricoAltro as importoCaricoTotale, ");
        sb.append("importoScarico+importoScaricoAltro as importoScaricoTotale, ");
        sb.append("importoFatturatoCarico, ");
        sb.append("importoFatturatoScarico, ");
        sb.append("magaRighe.descrizione as descrizioneRiga, ");
        sb.append("magaRighe.noteRiga as noteRiga, ");
        sb.append("mov.importoProvvigione as importoProvvigione, ");
        sb.append("sedi.sede_entita_id as idSedeEntita, ");
        sb.append("sedi.codice_sede as codiceSedeEntita, ");
        sb.append("sedi.descrizione_sede as descrizioneSedeEntita, ");
        sb.append("magaRighe.tipoOmaggio as tipoOmaggio, ");
        sb.append("az.id as idAzienda, ");
        sb.append("az.codice as codiceAzienda, ");
        sb.append("az.denominazione as denominazioneAzienda ");
        sb.append("from dw_movimentimagazzino mov ");
        sb.append("inner join dw_articoli art on art.id=mov.articolo_id ");
        sb.append("inner join maga_articoli mart on mart.id=art.id ");
        sb.append("inner join anag_unita_misura aum on aum.id=mart.unitaMisura_id ");
        sb.append("inner join dw_depositi dep on mov.deposito_id=dep.id ");
        sb.append("inner join maga_righe_magazzino magaRighe on mov.idRiga=magaRighe.id ");
        sb.append("left join dw_sedientita sedi on mov.sedeEntita_id=sedi.sede_entita_id ");
        sb.append("inner join anag_aziende az on mov.codiceAzienda = az.codice ");
        sb.append(" where mov.codiceAzienda='" + codiceAzienda + "'");
        if (!sezioniTipoMovimento.isEmpty()) {
            sb.append(" and mov.sezioneTipoMovimentoValore in (");
            for (ESezioneTipoMovimento sezioneTipoMovimento : sezioniTipoMovimento) {
                sb.append(sezioneTipoMovimento.ordinal() + ",");
            }
            // cancello l'ultima virgola che e' in piu' e chiudo la parentesi della IN
            sb.deleteCharAt(sb.length() - 1).append(") ");
        }
        if (articoloLite != null && articoloLite.getId() != null) {
            sb.append(" and art.id=" + articoloLite.getId());
        }

        if (omaggio) {
            sb.append(" and magaRighe.tipoOmaggio>0 ");
        }

        if (dataInizio != null && dataFine != null) {
            sb.append(" and mov.dataRegistrazione>='" + dateFormat.format(dataInizio) + "'");
            sb.append(" and mov.dataRegistrazione<='" + dateFormat.format(dataFine) + "'");
        }

        if (depositoLite != null && depositoLite.getId() != null) {
            sb.append(" and mov.deposito_id=" + depositoLite.getId());
        }

        if (sedeEntita != null && sedeEntita.getId() != null) {
            sb.append(" and sedi.sede_entita_id =" + sedeEntita.getId());
        } else {
            if (entitaLite != null && entitaLite.getId() != null) {
                sb.append(" and sedi.entita_id=" + entitaLite.getId());
            }
        }
        if (agenteLite != null && agenteLite.getId() != null) {
            sb.append(" and mov.agente_id=" + agenteLite.getId());
        }
        if (noteRiga != null && noteRiga.trim().length() > 0) {
            sb.append(" and magaRighe.noteRiga like '%" + noteRiga + "%'");
        }
        if (descrizioneRiga != null && descrizioneRiga.trim().length() > 0) {
            sb.append(" and magaRighe.descrizione like '%" + descrizioneRiga + "%'");
        }

        if (tipiAreaMagazzino != null) {
            if (tipiAreaMagazzino.size() == 1) {
                TipoAreaMagazzino tipoAreaMagazzino = tipiAreaMagazzino.iterator().next();
                sb.append(" and mov.tipoDocumentoId=" + tipoAreaMagazzino.getTipoDocumento().getId());
            } else if (tipiAreaMagazzino.size() > 1) {
                sb.append(" and mov.tipoDocumentoId in (");
                for (TipoAreaMagazzino tipoAreaMagazzino : tipiAreaMagazzino) {
                    sb.append(tipoAreaMagazzino.getTipoDocumento().getId() + ",");
                }
                // cancello l'ultima virgola che e' in piu' e chiudo la parentesi della IN
                sb.deleteCharAt(sb.length() - 1).append(") ");
            }
        }
        return sb.toString();
    }

    /**
     * Sql per caricare le righe della movimentazione per articolo.
     *
     * @param articolo
     *            articolo
     * @param dataInizio
     *            dataInizio
     * @param dataFine
     *            dataFine
     * @param depositoLite
     *            deposito
     * @param codiceAzienda
     *            codiceAzienda
     * @param sezioniTipoMovimento
     *            sezioni tipo movimento
     * @param entitaLite
     *            entità
     * @param sedeEntita
     *            sede entità
     * @return stringa sql
     */
    public static String getSqlMovimentazioneArticolo(ArticoloLite articolo, Date dataInizio, Date dataFine,
            DepositoLite depositoLite, String codiceAzienda, List<ESezioneTipoMovimento> sezioniTipoMovimento,
            EntitaLite entitaLite, SedeEntita sedeEntita) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("art.id as idArticolo, ");
        sb.append("concat(art.codice) as codiceArticolo, ");
        sb.append("concat(art.descrizioneLinguaAziendale) as descrizioneArticolo, ");
        sb.append("concat(art.codiceCategoria) as codiceCategoria, ");
        sb.append("concat(art.descrizioneCategoria) as descrizioneCategoria, ");
        sb.append("concat(dep.codice) as codiceDeposito, ");
        sb.append("concat(dep.descrizione) as descrizioneDeposito, ");
        sb.append("mov.areaMagazzino_id as areaMagazzinoId, ");
        sb.append("mov.dataRegistrazione, ");
        sb.append("mov.dataDocumento, ");
        sb.append("mov.numeroDocumento, ");
        sb.append("mov.numeroDocumentoOrder, ");
        sb.append("mov.tipoDocumentoId as idTipoDocumento, ");
        sb.append("concat(mov.tipoDocumentoCodice) as codiceTipoDocumento, ");
        sb.append("concat(mov.tipoDocumentoDescrizione) as descrizioneTipoDocumento, ");
        sb.append("sedi.entita_id as idEntita, ");
        sb.append("sedi.codice as codiceEntita, ");
        sb.append("concat(sedi.denominazione) as descrizioneEntita, ");
        sb.append("concat(sedi.TIPO_ANAGRAFICA) as tipoEntita, ");
        sb.append("magaRighe.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
        sb.append("magaRighe.numeroDecimaliQta as numeroDecimaliQuantita, ");
        sb.append("magaRighe.importoInValutaAzienda as prezzoUnitario, ");
        sb.append("magaRighe.importoInValutaAziendaNetto as prezzoNetto, ");
        sb.append("magaRighe.importoInValutaAziendaTotale as PrezzoTotale, ");
        sb.append("qtaMagazzinoCarico+qtaMagazzinoCaricoAltro as qtaMagazzinoCaricoTotale, ");
        sb.append("qtaMagazzinoScarico+qtaMagazzinoScaricoAltro as qtaMagazzinoScaricoTotale, ");
        sb.append("sedi.sede_entita_id as idSedeEntita, ");
        sb.append("sedi.codice_sede as codiceSedeEntita, ");
        sb.append("sedi.descrizione_sede as descrizioneSedeEntita, ");
        sb.append("mov.unitaMisura as um, ");
        sb.append("mov.tipoOperazione as tipoOperazione, ");
        sb.append("az.id as idAzienda, ");
        sb.append("az.codice as codiceAzienda, ");
        sb.append("az.denominazione as denominazioneAzienda ");
        sb.append("from dw_movimentimagazzino mov ");
        sb.append("inner join dw_articoli art on art.id=mov.articolo_id ");
        sb.append("inner join dw_depositi dep on mov.deposito_id=dep.id ");
        sb.append("left join dw_sedientita sedi on mov.sedeEntita_id=sedi.sede_entita_id ");
        sb.append("inner join maga_righe_magazzino magaRighe on mov.idRiga=magaRighe.id ");
        sb.append("inner join anag_aziende az on mov.codiceAzienda = az.codice ");
        sb.append(" where mov.codiceAzienda='" + codiceAzienda + "'");
        sb.append(" and mov.tipoMovimento <> 0 ");
        if (!sezioniTipoMovimento.isEmpty()) {
            sb.append(" and mov.sezioneTipoMovimentoValore in (");
            for (ESezioneTipoMovimento sezioneTipoMovimento : sezioniTipoMovimento) {
                sb.append(sezioneTipoMovimento.ordinal() + ",");
            }
            // cancello l'ultima virgola che e' in piu' e chiudo la parentesi della IN
            sb.deleteCharAt(sb.length() - 1).append(") ");
        }
        if (articolo != null) {
            sb.append(" and art.id=" + articolo.getId());
        }
        if (sedeEntita != null && sedeEntita.getId() != null) {
            sb.append(" and sedi.sede_entita_id=" + sedeEntita.getId());
        } else if (entitaLite != null && entitaLite.getId() != null) {
            sb.append(" and sedi.entita_id=" + entitaLite.getId());
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        sb.append(" and dataRegistrazione>='" + dateFormat.format(dataInizio) + "'");
        sb.append(" and dataRegistrazione<='" + dateFormat.format(dataFine) + "'");
        if (depositoLite != null) {
            sb.append(" and deposito_id=" + depositoLite.getId());
        }
        sb.append(" order by dataRegistrazione asc");
        return sb.toString();
    }
}
