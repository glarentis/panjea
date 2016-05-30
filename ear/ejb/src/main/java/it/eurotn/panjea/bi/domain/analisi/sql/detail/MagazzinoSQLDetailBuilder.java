package it.eurotn.panjea.bi.domain.analisi.sql.detail;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBILayout;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.FieldBILayout;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;

/**
 * @author fattazzo
 *
 */
public class MagazzinoSQLDetailBuilder extends SQLDetailBuilder {

    @Override
    public String getFromSQL(AnalisiBi analisiBi) {
        AnalisiBILayout analisiBILayout = analisiBi.getAnalisiLayout();

        String join = analisiBi.isAlternativeJoin() ? " left " : " inner ";

        Map<String, String> mapFilerJoin = new HashMap<String, String>();
        for (FieldBILayout fieldLayout : analisiBILayout.getFiltri()) {
            Colonna colonna = fieldLayout.getColonna();
            mapFilerJoin.put(colonna.getTabella().getAlias(), join);
        }

        StringBuilder sb = new StringBuilder(500);
        sb.append(" from dw_movimentimagazzino movimentimagazzino ");
        sb.append("inner join dw_articoli articoli on articoli.id=movimentimagazzino.articolo_id ");
        sb.append("inner join dw_depositi depositi on movimentimagazzino.deposito_id=depositi.id ");
        sb.append("inner join maga_righe_magazzino magaRighe on movimentimagazzino.idRiga=magaRighe.id ");
        sb.append("inner join maga_area_magazzino am on am.id=magaRighe.areaMagazzino_id ");
        sb.append("inner join docu_documenti Documenti on Documenti.id=am.documento_id ");
        sb.append("inner join docu_tipi_documento Tipidoc on Tipidoc.id=Documenti.tipo_documento_id ");
        sb.append("inner join dw_dimensionedata data on data.DATA_ID=movimentimagazzino.dataRegistrazione ");
        sb.append("inner join vista_magazzino magazzino on magaRighe.id=magazzino.idRiga ");
        String joinType = StringUtils.defaultString(mapFilerJoin.get("clienti"), " left ");
        sb.append(joinType);
        sb.append("join vista_Sedi clienti on movimentimagazzino.sedeEntita_id=clienti.sede_entita_id ");
        joinType = StringUtils.defaultString(mapFilerJoin.get("fornitori"), " left ");
        sb.append(joinType);
        sb.append("join vista_Sedi fornitori on movimentimagazzino.sedeEntita_id=fornitori.sede_entita_id ");
        joinType = StringUtils.defaultString(mapFilerJoin.get("entità"), " left ");
        sb.append(joinType);
        sb.append("join dw_sedientita entità on movimentimagazzino.sedeEntita_id=entità.sede_entita_id ");
        joinType = StringUtils.defaultString(mapFilerJoin.get("agenti"), " left ");
        sb.append(joinType);
        sb.append("join dw_sedientita agenti on movimentimagazzino.agente_Id=agenti.entita_id ");
        joinType = StringUtils.defaultString(mapFilerJoin.get("vettori"), " left ");
        sb.append(joinType);
        sb.append("join dw_sedientita vettori on movimentimagazzino.sedeVettore_id=vettori.sede_entita_id ");
        joinType = StringUtils.defaultString(mapFilerJoin.get("categorieEntita"), " left ");
        sb.append(joinType);
        sb.append(
                "join vista_CategorieEntita categorieEntita on categorieEntita.sede_entita_id = Documenti.sedeEntita_id ");
        return sb.toString();
    }

    @Override
    public String getSelectSQL() {
        StringBuilder sb = new StringBuilder(500);
        sb.append("select ");
        sb.append("articoli.id as idArticolo, ");
        sb.append("concat(articoli.codice) as codiceArticolo, ");
        sb.append("concat(articoli.descrizioneLinguaAziendale) as descrizioneArticolo, ");
        sb.append("articoli.categoria_id as idCategoria, ");
        sb.append("concat(articoli.codiceCategoria) as codiceCategoria, ");
        sb.append("concat(articoli.descrizioneCategoria) as descrizioneCategoria, ");
        sb.append("depositi.id as idDeposito, ");
        sb.append("concat(depositi.codice) as codiceDeposito, ");
        sb.append("concat(depositi.descrizione) as descrizioneDeposito, ");
        sb.append("Documenti.id as idDocumento, ");
        sb.append("movimentimagazzino.dataRegistrazione, ");
        sb.append("movimentimagazzino.dataDocumento, ");
        sb.append("movimentimagazzino.numeroDocumento, ");
        sb.append("movimentimagazzino.numeroDocumentoOrder, ");
        sb.append("Documenti.tipo_documento_id as idTipoDocumento, ");
        sb.append("concat(movimentimagazzino.tipoDocumentoCodice) as codiceTipoDocumento, ");
        sb.append("concat(movimentimagazzino.tipoDocumentoDescrizione) as descrizioneTipoDocumento, ");
        sb.append("entità.entita_id as idEntita, ");
        sb.append("entità.codice as codiceEntita, ");
        sb.append("concat(entità.denominazione) as descrizioneEntita, ");
        sb.append("concat(entità.TIPO_ANAGRAFICA) as tipoEntita, ");
        sb.append("magaRighe.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
        sb.append("magaRighe.numeroDecimaliQta as numeroDecimaliQuantita, ");
        sb.append("magaRighe.importoInValutaAzienda as prezzoUnitario, ");
        sb.append("magaRighe.importoInValutaAziendaNetto as prezzoNetto, ");
        sb.append("magaRighe.importoInValutaAziendaTotale as PrezzoTotale, ");
        sb.append("magaRighe.variazione1 as variazione1, ");
        sb.append("magaRighe.variazione2 as variazione2, ");
        sb.append("magaRighe.variazione3 as variazione3, ");
        sb.append("magaRighe.variazione4 as variazione4, ");
        sb.append("magaRighe.qta as qta, ");
        sb.append("magaRighe.qtaMagazzino as qtaMagazzino, ");
        sb.append("magaRighe.descrizione as descrizioneRiga, ");
        sb.append("magaRighe.noteRiga as noteRiga, ");
        sb.append("entità.sede_entita_id as idSedeEntita, ");
        sb.append("entità.codice_sede as codiceSedeEntita, ");
        sb.append("entità.descrizione_sede as descrizioneSedeEntita ");
        return sb.toString();
    }

}
