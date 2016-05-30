package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.SospesiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.SospesiSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SospesiSyncBuilder")
public class SospesiSyncBuilderBean extends SqlGeneratorBean implements SospesiSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		Map<String, String> fields = new TreeMap<String, String>();
        fields.put("Documenti.dataDocumento", "Data");
        fields.put("concat_ws(' ', Tipi.codice, Documenti.codice, 'del', Documenti.dataDocumento)", "Note");
        fields.put("Anag.codice", "Cliente");
        fields.put("cast(Rate.importoInValutaAzienda as decimal(10,2))", "Sospeso");
        fields.put("cast((select coalesce(sum(Pagamenti.importoInValutaAzienda),0) from part_pagamenti Pagamenti where Pagamenti.rata_id = Rate.id and Pagamenti.dataPagamento is not null)	as decimal(10,2))", "Recuperato");
        return fields;
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE Sospesi(" +
			"Cliente nvarchar(6) NOT NULL," +
			"Data datetime NOT NULL," +
			"Sospeso money NOT NULL," +
			"Recuperato money NOT NULL," +
			"Note nvarchar(1000) NOT NULL)";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return 
			"from part_rate Rate " +
			"join part_area_partite Partite on Partite.id = Rate.areaRate_id " +
			"join docu_documenti Documenti on Documenti.id = Partite.documento_id " +
			"join docu_tipi_documento Tipi on Tipi.id = Documenti.tipo_documento_id " +
			"join maga_tipi_area_magazzino Tam on Tam.tipoDocumento_id = Tipi.id " +
			"join anag_entita Anag on Anag.id = Documenti.entita_id and Anag.TIPO_ANAGRAFICA = 'C' " +
			"where " +
			"Rate.importoInValutaAzienda - (select coalesce(sum(importoInValutaAzienda),0) from part_pagamenti where part_pagamenti.rata_id = Rate.id) > 0 ";
	}

	@Override
	protected String getTableName() {
		return "Sospesi";
	}

}
