package it.eurotn.panjea.bi.domain.analisi.tabelle.fatti;

import java.math.BigDecimal;
import java.util.Map.Entry;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBILayout;
import it.eurotn.panjea.bi.domain.analisi.FieldBILayout;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasure;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasureFunction;
import it.eurotn.panjea.bi.domain.analisi.tabelle.TabellaFatti;

public class TabellaMovimenti extends TabellaFatti {
    private static final long serialVersionUID = -4622109451227321752L;

    public static final String SEZ_FATTURATO = "Fatturato";
    public static final String SEZ_IMPORTO = "Importo";
    public static final String SEZ_QTA_FATT = "Quantità fatturato";
    public static final String SEZ_QTA = "Quantità";
    public static final String SEZ_GIORNI = "Giorni";
    public static final String SEZ_MEDIE = "Medie";

    /**
     *
     * Costruttore.
     *
     */
    public TabellaMovimenti() {
        super("movimentimagazzino", "dw", null, "magazzino");

        colonne.putAll(ColumnMeasure.creaColonneAggregate("importoProvvigione", BigDecimal.class, this, "Provv.",
                SEZ_IMPORTO, 2, "€"));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("baseProvv", BigDecimal.class, this, "Base Provv.",
                SEZ_IMPORTO, 2, "€"));

        colonne.putAll(ColumnMeasure.creaColonneAggregate("importoFatturatoScarico", BigDecimal.class, this,
                "Fatt. scarico", SEZ_FATTURATO, 2, "€"));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("importoFatturatoCarico", BigDecimal.class, this,
                "Fatt. carico", SEZ_FATTURATO, 2, "€"));

        colonne.putAll(ColumnMeasure.creaColonneAggregate(
                "importoCarico+importoCaricoAltro+importoScarico+importoScaricoAltro", BigDecimal.class, this,
                "Importo", SEZ_IMPORTO, 2, "€"));

        colonne.putAll(ColumnMeasure.creaColonneAggregate("importoCarico+importoCaricoAltro", BigDecimal.class, this,
                "Importo carico tot.", SEZ_IMPORTO, 2, "€"));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("importoCarico", BigDecimal.class, this, "Importo carico",
                SEZ_IMPORTO, 2, "€"));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("importoCaricoAltro", BigDecimal.class, this,
                "Importo carico altro", SEZ_IMPORTO, 2, "€"));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("importoScarico+importoScaricoAltro", BigDecimal.class, this,
                "Importo scarico tot.", SEZ_IMPORTO, 2, "€"));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("importoScarico", BigDecimal.class, this, "Importo scarico",
                SEZ_IMPORTO, 2, "€"));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("importoScaricoAltro", BigDecimal.class, this,
                "Importo scarico altro", SEZ_IMPORTO, 2, "€"));

        colonne.putAll(ColumnMeasure.creaColonneAggregate("importoFatturatoScarico-importoFatturatoCarico",
                BigDecimal.class, this, "Fatt.", SEZ_FATTURATO, 2, "€"));

        colonne.putAll(ColumnMeasure.creaColonneAggregate("qtaFatturatoScarico - qtaFatturatoCarico ", double.class,
                this, "Qta fatt.", SEZ_QTA_FATT, null));

        colonne.putAll(ColumnMeasure.creaColonneAggregate("qtaFatturatoScarico", double.class, this,
                "Qta fatt. scarico", SEZ_QTA_FATT, null));

        colonne.putAll(ColumnMeasure.creaColonneAggregate("qtaFatturatoCarico", double.class, this, "Qta fatt. carico",
                SEZ_QTA_FATT, null));

        colonne.putAll(ColumnMeasure.creaColonneAggregate(
                "qtaMagazzinoCarico+qtaMagazzinoCaricoAltro-qtaMagazzinoScarico-qtaMagazzinoScaricoAltro", double.class,
                this, "Qta", SEZ_QTA, null));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("qtaMagazzinoCarico+qtaMagazzinoCaricoAltro", double.class,
                this, "Qta carico tot.", SEZ_QTA, null));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("qtaMagazzinoCarico", double.class, this, "Qta carico",
                SEZ_QTA, null));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("qtaMagazzinoCaricoAltro", double.class, this,
                "Qta carico altro", SEZ_QTA, null));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("qtaMagazzinoScarico+qtaMagazzinoScaricoAltro", double.class,
                this, "Qta scarico tot.", SEZ_QTA, null));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("qtaMagazzinoScarico", double.class, this, "Qta scarico",
                SEZ_QTA, null));
        colonne.putAll(ColumnMeasure.creaColonneAggregate("qtaMagazzinoScaricoAltro", double.class, this,
                "Qta scarico altro", SEZ_QTA, null));

        colonne.putAll(ColumnMeasure.creaColonneAggregate(
                "if(isnull(rm.rigaOrdineCollegata_Id),0,(rm.moltQtaOrdine * rm.qta))", double.class, this, "Qta evasa",
                SEZ_QTA, null));

        colonne.putAll(ColumnMeasure.creaColonneAggregate(
                "if(((to_days(`am`.`dataRegistrazione`) - to_days(`ro`.`dataConsegna`)) <= 0),0,abs((to_days(`ro`.`dataConsegna`) - to_days(`am`.`dataRegistrazione`))))",
                BigDecimal.class, this, "GG ritardo consegna", SEZ_GIORNI, 0, null));

        colonne.putAll(ColumnMeasure.creaColonneAggregate(
                "(to_days(`am`.`dataRegistrazione`) - to_days(`ao`.`dataRegistrazione`)) ", BigDecimal.class, this,
                "GG consegna", SEZ_GIORNI, 0, null));

        Colonna col = new ColumnMeasureFunction(
                "sum(importoCarico+importoCaricoAltro+importoScarico+importoScaricoAltro)/sum(qtaMagazzinoCarico+qtaMagazzinoCaricoAltro-qtaMagazzinoScarico-qtaMagazzinoScaricoAltro) ",
                double.class, this, "Prz. medio", SEZ_MEDIE, 2);
        colonne.put(col.getKey(), col);

        col = new ColumnMeasureFunction(
                "sum(importoCarico+importoCaricoAltro)/sum(qtaMagazzinoCarico+qtaMagazzinoCaricoAltro) ", double.class,
                this, "Prz. medio carico tot.", SEZ_MEDIE, 2);
        colonne.put(col.getKey(), col);

        col = new ColumnMeasureFunction("sum(importoCaricoAltro)/sum(qtaMagazzinoCaricoAltro) ", double.class, this,
                "Prz. medio carico altro", SEZ_MEDIE, 2);
        colonne.put(col.getKey(), col);

        col = new ColumnMeasureFunction("sum(importoCarico)/sum(qtaMagazzinoCarico) ", double.class, this,
                "Prz. medio carico", SEZ_MEDIE, 2);
        colonne.put(col.getKey(), col);

        col = new ColumnMeasureFunction("sum(importoScarico)/sum(qtaMagazzinoScarico) ", double.class, this,
                "Prz. medio scarico tot.", SEZ_MEDIE, 2);
        colonne.put(col.getKey(), col);

        col = new ColumnMeasureFunction(
                "sum(importoScarico+importoScaricoAltro)/sum(qtaMagazzinoScarico-qtaMagazzinoScaricoAltro) ",
                double.class, this, "Prz. medio scarico", SEZ_MEDIE, 2);
        colonne.put(col.getKey(), col);

        col = new ColumnMeasureFunction("sum(importoScaricoAltro)/sum(qtaMagazzinoScaricoAltro) ", double.class, this,
                "Prz. medio scarico altro", SEZ_MEDIE, 2);
        colonne.put(col.getKey(), col);
    }

    @Override
    public String getSqlTable(AnalisiBILayout analisiBILayout) {
        StringBuilder sqlTable = new StringBuilder(super.getSqlTable(analisiBILayout));
        if (analisiBILayout == null) {
            return sqlTable.toString();
        }
        // se ho dei campi che richiedono l'evaso devo mettere in join con le tabelle degli ordini
        for (Entry<String, FieldBILayout> field : analisiBILayout.getFields().entrySet()) {
            if (field.getKey().contains("magazzino_Qta_evasa") || field.getKey().contains("_GG_ritardo_consegna")
                    || field.getKey().contains("_GG_consegna")) {
                if (sqlTable.indexOf(" inner join maga_righe_magazzino") == -1) {
                    sqlTable.append(" inner join maga_righe_magazzino rm on rm.id=magazzino.idRiga ");
                }
            }
            if (field.getKey().contains("magazzino_GG_ritardo_consegna")
                    || field.getKey().contains("magazzino_GG_consegna")) {
                if (sqlTable.indexOf(" inner join ordi_righe_ordine") == -1) {
                    sqlTable.append(" inner join maga_area_magazzino am on am.id=rm.areaMagazzino_id ");
                    sqlTable.append(" inner join ordi_righe_ordine ro on ro.id=rm.rigaOrdineCollegata_Id ");
                    sqlTable.append(" inner join ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");
                }
            }

            if (field.getKey().contains("_Numero_doc_")) {
                if (sqlTable.indexOf("am.id") == -1) {
                    sqlTable.append(" inner join maga_area_magazzino am on magazzino.areaMagazzino_id=am.id ");
                }
            }
            if (field.getKey().startsWith("pagamenti")) {
                if (sqlTable.indexOf("am.id") == -1) {
                    sqlTable.append(" inner join maga_area_magazzino am on magazzino.areaMagazzino_id=am.id ");
                }
                if (sqlTable.indexOf("ap.documento_id") == -1) {
                    sqlTable.append("left join part_area_partite ap on am.documento_id =ap.documento_id ");
                }
            }

            if (field.getKey().startsWith("operatori") || (field.getKey().startsWith("distributore"))) {
                if (sqlTable.indexOf("vend_area_rifornimento") == -1) {
                    sqlTable.append(
                            " left join vend_area_rifornimento arif on magazzino.areaMagazzino_id=arif.areaMagazzino_id ");
                }
            }
        }
        return sqlTable.toString();
    }
}
