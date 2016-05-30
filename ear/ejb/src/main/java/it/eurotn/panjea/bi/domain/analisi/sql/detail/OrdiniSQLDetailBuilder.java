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
public class OrdiniSQLDetailBuilder extends SQLDetailBuilder {

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
        sb.append(" from vista_ordini ordini ");
        sb.append("inner join dw_articoli articoli on articoli.id=ordini.articolo_id ");
        sb.append("inner join docu_documenti Documenti on Documenti.id=ordini.documentoOrdineId ");
        sb.append("inner join dw_depositi depositi on ordini.depositoOrigine_id=depositi.id ");
        sb.append("inner join ordi_righe_ordine ordRighe on ordini.idRigaOrdine=ordRighe.id ");
        sb.append("inner join ordi_area_ordine ao on ao.id=ordRighe.areaOrdine_id ");
        sb.append("inner join docu_tipi_documento Tipidoc on Tipidoc.id=Documenti.tipo_documento_id ");
        sb.append("inner join dw_dimensionedata data on data.DATA_ID=ordini.dataRegistrazione ");
        String joinType = StringUtils.defaultString(mapFilerJoin.get("clienti"), " left ");
        sb.append(joinType);
        sb.append("join vista_Sedi clienti on Documenti.sedeEntita_id=clienti.sede_entita_id ");
        joinType = StringUtils.defaultString(mapFilerJoin.get("fornitori"), " left ");
        sb.append(joinType);
        sb.append("join vista_Sedi fornitori on Documenti.sedeEntita_id=fornitori.sede_entita_id ");
        joinType = StringUtils.defaultString(mapFilerJoin.get("entità"), " left ");
        sb.append(joinType);
        sb.append("join dw_sedientita entità on Documenti.sedeEntita_id=entità.sede_entita_id ");
        joinType = StringUtils.defaultString(mapFilerJoin.get("agenti"), " left ");
        sb.append(joinType);
        sb.append("join dw_sedientita agenti on ordini.agente_Id=agenti.entita_id ");
        joinType = StringUtils.defaultString(mapFilerJoin.get("vettori"), " left ");
        sb.append(joinType);
        sb.append("join dw_sedientita vettori on ordini.sedeVettore_id=vettori.sede_entita_id ");
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
        sb.append("ordini.dataRegistrazione, ");
        sb.append("Documenti.dataDocumento, ");
        sb.append("Documenti.codice as numeroDocumento, ");
        sb.append("Documenti.codiceOrder as numeroDocumentoOrder, ");
        sb.append("Documenti.tipo_documento_id as idTipoDocumento, ");
        sb.append("concat(Tipidoc.codice) as codiceTipoDocumento, ");
        sb.append("concat(Tipidoc.descrizione) as descrizioneTipoDocumento, ");
        sb.append("entità.entita_id as idEntita, ");
        sb.append("entità.codice as codiceEntita, ");
        sb.append("concat(entità.denominazione) as descrizioneEntita, ");
        sb.append("concat(entità.TIPO_ANAGRAFICA) as tipoEntita, ");
        sb.append("ordRighe.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
        sb.append("ordRighe.numeroDecimaliQta as numeroDecimaliQuantita, ");
        sb.append("ordRighe.importoInValutaAzienda as prezzoUnitario, ");
        sb.append("ordRighe.importoInValutaAziendaNetto as prezzoNetto, ");
        sb.append("ordRighe.importoInValutaAziendaTotale as PrezzoTotale, ");
        sb.append("ordRighe.variazione1 as variazione1, ");
        sb.append("ordRighe.variazione2 as variazione2, ");
        sb.append("ordRighe.variazione3 as variazione3, ");
        sb.append("ordRighe.variazione4 as variazione4, ");
        sb.append("ordRighe.qta as qta, ");
        sb.append("null as qtaMagazzino, ");
        sb.append("ordRighe.descrizione as descrizioneRiga, ");
        sb.append("ordRighe.noteRiga as noteRiga, ");
        sb.append("entità.sede_entita_id as idSedeEntita, ");
        sb.append("entità.codice_sede as codiceSedeEntita, ");
        sb.append("entità.descrizione_sede as descrizioneSedeEntita ");
        return sb.toString();
    }

}
