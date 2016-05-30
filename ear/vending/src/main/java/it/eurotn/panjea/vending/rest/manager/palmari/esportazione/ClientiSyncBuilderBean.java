package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ClientiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.ClientiSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ClientiSyncBuilder")
public class ClientiSyncBuilderBean extends SqlGeneratorBean implements ClientiSyncBuilder {

    @Override
    protected Map<String, String> getSelectInsertFields() {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("ent.codice", "Cliente");
        fields.put("anag.denominazione", "Ragione_sociale");
        fields.put("sanag.indirizzo", "Indirizzo");
        fields.put("cap.descrizione", "Cap");
        fields.put("loc.descrizione", "Citta");
        fields.put("prov.sigla", "Provincia");
        fields.put("anag.partita_iva", "Partita_IVA");
        fields.put("anag.codice_fiscale", "Codice_fiscale");
        fields.put("cpag.descrizione", "Descrizione_pagamento");
        fields.put("ent.fatturaPalmare", "Da_fatturare");
        fields.put("10", "Aliquota_IVA");
        return fields;
    }

    @Override
    protected String getSQLCreateTable(String codiceOperatore) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("CREATE TABLE ");
        sb.append(getTableName());
        sb.append("( ");
        sb.append(
                "Cliente nvarchar(6) NOT NULL,Ragione_sociale nvarchar(255) NULL,Seconda_riga nvarchar(255) NULL,");
        sb.append(
                "Indirizzo nvarchar(255) NULL,Indirizzo_2 nvarchar(255) NULL,Cap nvarchar(6) NULL,");
        sb.append(
                "Citta nvarchar(255) NULL,Provincia nvarchar(2) NULL,Partita_IVA nvarchar(11) NULL,");
        sb.append(
                "Codice_fiscale nvarchar(16) NULL,Descrizione_pagamento nvarchar(40) NULL,Da_fatturare bit NOT NULL,");
        sb.append(
                "Aliquota_IVA float NOT NULL,Messaggio nvarchar(255) NULL,Data_messaggio_visualizzato datetime NULL,");
        sb.append(
                "Cauzione float NOT NULL DEFAULT (0),RaccoltaPunti nvarchar(6) NULL,Punti int NOT NULL DEFAULT (0),");
        sb.append(
                "PRIMARY KEY (Cliente))");
        return sb.toString();
    }

    @Override
    protected String getSQLSelectRows(String codiceOperatore) {
        StringBuilder sb = new StringBuilder(500);
        sb.append("from anag_entita ent ");
        sb.append("inner join anag_anagrafica anag on ent.anagrafica_id = anag.id ");
        sb.append("inner join anag_sedi_anagrafica sanag on sanag.id = anag.sede_anagrafica_id ");
        sb.append("inner join geog_localita loc on loc.id = sanag.localita_id ");
        sb.append("left join geog_cap cap on cap.id = loc.id ");
        sb.append("inner join geog_livello_2 prov on prov.id = loc.livello1_id ");
        sb.append("inner join anag_sedi_entita sent on sent.sede_anagrafica_id = sanag.id ");
        sb.append("left join part_sedi_pagamento psedi on psedi.sedeEntita_id = sent.id "); 
        sb.append("left join part_codici_pagamento cpag on cpag.id = psedi.codicePagamento_id ");
        sb.append("where ent.TIPO_ANAGRAFICA = 'C' ");
        return sb.toString();
    }

    @Override
    protected String getTableName() {
        return "Clienti";
    }

}
